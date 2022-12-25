package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;
import FrontEnd.IRGenerator.Quadruple.Elements.LVal;

import java.util.ArrayList;

import static BackEnd.MIPSTbl.*;

public class _3_Assign_Q extends IRCode {
    LVal left;
    boolean R_isVal;
    String var;
    int right_val;

    public _3_Assign_Q(LVal l, String r) {
        left = l;
        var = r;
        R_isVal = false;
    }

    public _3_Assign_Q(LVal l, int r) {
        left = l;
        right_val = r;
        R_isVal = true;
    }

    @Override
    public String toString() {
        return R_isVal ? String.format("%s = %d\n", left, right_val) : String.format("%s = %s\n", left, var);
    }

    // OK!!!
    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        // 特殊处理：可能分配了寄存器的'%_'变量
        if (R_isVal && !left.isArr) {
            String var = left.name;
            if (var.charAt(0) == '%' && MIPSTbl.regOrMem_trueIfReg(var)) {
                int s_reg_num = MIPSTbl.get_s_num(var);
                mips_text.add(new MIPSCode.LI(s_reg_num, right_val));
                return;
            }
        }
        int r_reg_num = getRightRegNum(mips_text);
        String lName = left.name;
        if (!left.isArr) {
            // %s = $r
            if (lName.charAt(0) == '@') {
                int g_addr = global_name2addr.get(lName);
                mips_text.add(new MIPSCode.SW(r_reg_num, g_addr, 0));
            } else if (lName.charAt(0) == '%') {
                if (regOrMem_trueIfReg(lName)) {
                    mips_text.add(new MIPSCode.Move(get_s_num(lName), r_reg_num));
                } else {
                    int m_addr = main_name2addr.get(lName);
                    mips_text.add(new MIPSCode.SW(r_reg_num, m_addr, 0));
                }
            } else if (lName.charAt(0) == '!' || lName.charAt(0) == '^') {
                int sp_offset = (lName.charAt(0) == '!') ? get_para_name_2_sp_offset(lName) : func_name2offset.get(lName);
                mips_text.add(new MIPSCode.SW(r_reg_num, sp_offset, sp));
            } else {
                System.out.println("Something's wrong with _3_Q!!! <2>");
            }
        } else if (left.o_isVal) {
            // %s[%d] = $r
            int l_in = left.o_val;
            if (lName.charAt(0) == '@') {
                int g_addr = global_name2addr.get(lName);
                mips_text.add(new MIPSCode.SW(r_reg_num, g_addr + (l_in << 2), 0));
            } else if (lName.charAt(0) == '%') {
                int m_addr = main_name2addr.get(lName);
                mips_text.add(new MIPSCode.SW(r_reg_num, m_addr + (l_in << 2), 0));
            } else if (lName.charAt(0) == '!') {
                int sp_offset = get_para_name_2_sp_offset(lName);
                mips_text.add(new MIPSCode.LW(t0, sp_offset, sp));
                mips_text.add(new MIPSCode.SW(r_reg_num, l_in << 2, t0));
            } else if (lName.charAt(0) == '^') {
                int sp_offset = func_name2offset.get(lName);
                mips_text.add(new MIPSCode.SW(r_reg_num, (l_in << 2) + sp_offset, sp));
            } else {
                System.out.println("Something's wrong with _3_Q!!! <3>");
            }
        } else {
            // %s[%s] = $r
            int l_in_reg_num = getRegNum(left.o_var, mips_text);
            if (lName.charAt(0) == '@') {
                int g_addr = global_name2addr.get(lName);
                mips_text.add(new MIPSCode.Cal_RI(t0, l_in_reg_num, "<<", 2));
                mips_text.add(new MIPSCode.SW(r_reg_num, g_addr, t0));
            } else if (lName.charAt(0) == '%') {
                int m_addr = main_name2addr.get(lName);
                mips_text.add(new MIPSCode.Cal_RI(t0, l_in_reg_num, "<<", 2));
                mips_text.add(new MIPSCode.SW(r_reg_num, m_addr, t0));
            } else if (lName.charAt(0) == '!') {
                // t1: r_reg_num占用
                // t0: l_in_reg可能占用 (l_in_reg << 2 结果可以用t0存储)
                // t2: 使用LW后获取的地址
                int sp_offset = get_para_name_2_sp_offset(lName);
                mips_text.add(new MIPSCode.LW(t2, sp_offset, sp));
                mips_text.add(new MIPSCode.Cal_RI(t0, l_in_reg_num, "<<", 2));
                mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", t2));
                mips_text.add(new MIPSCode.SW(r_reg_num, 0, t0));
            } else if (lName.charAt(0) == '^') {
                int sp_offset = func_name2offset.get(lName);
                mips_text.add(new MIPSCode.Cal_RI(t0, l_in_reg_num, "<<", 2));
                mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", sp));
                mips_text.add(new MIPSCode.SW(r_reg_num, sp_offset, t0));
            } else {
                System.out.println("Something's wrong with _3_Q!!! <4>");
            }
        }
    }

    private int getRightRegNum(ArrayList<MIPSCode> mips_text) {
        if (R_isVal) {
            mips_text.add(new MIPSCode.LI(t1, right_val));
            return t1;
        } else {
            if (var.charAt(0) == 't') {
                int t_num = MIPSTbl.get_t_num(var);
                if (t_num == -1) {
                    int t_addr = MIPSTbl.get_t_addr(var);
                    mips_text.add(new MIPSCode.LW(t1, t_addr, 0));
                    return t1;
                }
                else return t_num;
            } else if (var.charAt(0) == '@') {
                int g_addr = MIPSTbl.global_name2addr.get(var);
                mips_text.add(new MIPSCode.LW(t1, g_addr, 0));
                return t1;
            } else if (var.charAt(0) == '%') {
                if (MIPSTbl.regOrMem_trueIfReg(var)) {
                    return MIPSTbl.get_s_num(var);
                } else {
                    int m_addr = MIPSTbl.main_name2addr.get(var);
                    mips_text.add(new MIPSCode.LW(t1, m_addr, 0));
                    return t1;
                }
            } else if (var.charAt(0) == '^' || var.charAt(0) == '!') {
                int sp_offset = (var.charAt(0) == '^') ? MIPSTbl.func_name2offset.get(var) : MIPSTbl.get_para_name_2_sp_offset(var);
                mips_text.add(new MIPSCode.LW(t1, sp_offset, MIPSTbl.sp));
                return t1;
            } else {
                System.out.println("Something's wrong with _3_Q!!! <1>");
                return -1;
            }
        }
    }

    private int getRegNum(String var, ArrayList<MIPSCode> mips_text) {
        if (var.charAt(0) == 't') {
            int t_num = MIPSTbl.get_t_num(var);
            if (t_num == -1) {
                int t_addr = MIPSTbl.get_t_addr(var);
                mips_text.add(new MIPSCode.LW(t0, t_addr, 0));
                return t0;
            }
            else return t_num;
        } else if (var.charAt(0) == '@') {
            int g_addr = MIPSTbl.global_name2addr.get(var);
            mips_text.add(new MIPSCode.LW(t0, g_addr, 0));
            return t0;
        } else if (var.charAt(0) == '%') {
            if (MIPSTbl.regOrMem_trueIfReg(var)) {
                return MIPSTbl.get_s_num(var);
            } else {
                int m_addr = MIPSTbl.main_name2addr.get(var);
                mips_text.add(new MIPSCode.LW(t0, m_addr, 0));
                return t0;
            }
        } else if (var.charAt(0) == '^' || var.charAt(0) == '!') {
            int sp_offset = (var.charAt(0) == '^') ? MIPSTbl.func_name2offset.get(var) : MIPSTbl.get_para_name_2_sp_offset(var);
            mips_text.add(new MIPSCode.LW(t0, sp_offset, MIPSTbl.sp));
            return t0;
        } else {
            System.out.println("Something's wrong with _3_Q!!!");
            return -1;
        }
    }
}

