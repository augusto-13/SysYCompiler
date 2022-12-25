package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;

import java.util.ArrayList;

import static BackEnd.MIPSTbl.allocate_t_addr;
import static BackEnd.MIPSTbl.get_t_addr;
import static BackEnd.MIPSTbl.t0;
import static BackEnd.MIPSTbl.t1;
import static BackEnd.MIPSTbl.t2;


public class _6_Exp_Q extends IRCode {

    private final String res;
    private final String op;
    private final String arg1;
    private final boolean isImm;
    private String arg2;
    private int imm;
    private boolean release = false;


    public _6_Exp_Q(String r, String a1, String o, String a2) {
        res = r;
        arg1 = a1;
        op = o;
        arg2 = a2;
        isImm = false;
    }

    public _6_Exp_Q(String r, String a1, String o, int i) {
        res = r;
        arg1 = a1;
        op = o;
        imm = i;
        isImm = true;
    }

    @Override
    public String toString() {
        return isImm ? String.format("%s = %s %s %d\n", res, arg1, op, imm) : String.format("%s = %s %s %s\n", res, arg1, op, arg2);
    }

    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        // Yes!!!
        int res_num = MIPSTbl.allocate_t_reg(res);
        int arg1_reg_num = getRegNum(arg1, t0, mips_text);
        if (res_num != -1) {
            if (isImm) {
                if (op.equals("slti") && !isConst16(imm)) {
                    mips_text.add(new MIPSCode.LI(t1, imm));
                    mips_text.add(new MIPSCode.Cal_RR(res_num, arg1_reg_num, "slt", t1));
                    if (release) MIPSTbl.release_t(res);
                    return;
                }
                mips_text.add(new MIPSCode.Cal_RI(res_num, arg1_reg_num, op, imm));
            } else {
                int arg2_reg_num = getRegNum(arg2, t1, mips_text);
                mips_text.add(new MIPSCode.Cal_RR(res_num, arg1_reg_num, op, arg2_reg_num));
            }
        }
        else {
            int res_addr = allocate_t_addr(res);
            if (isImm) {
                if (op.equals("slti") && !isConst16(imm)) {
                    mips_text.add(new MIPSCode.LI(t1, imm));
                    mips_text.add(new MIPSCode.Cal_RR(t1, arg1_reg_num, "slt", t1));
                    mips_text.add(new MIPSCode.SW(t1, res_addr, 0));
                    if (release) MIPSTbl.release_t(res);
                    return;
                }
                mips_text.add(new MIPSCode.Cal_RI(t1, arg1_reg_num, op, imm));
                mips_text.add(new MIPSCode.SW(t1, res_addr, 0));
            } else {
                int arg2_reg_num = getRegNum(arg2, t1, mips_text);
                mips_text.add(new MIPSCode.Cal_RR(t0, arg1_reg_num, op, arg2_reg_num));
                mips_text.add(new MIPSCode.SW(t0, res_addr, 0));
            }
        }
        if (release) MIPSTbl.release_t(res);
    }

    private int getRegNum(String var, int target_num, ArrayList<MIPSCode> mips_text) {
        if (var.charAt(0) == 't') {
            if (MIPSTbl.regOrMem_trueIfReg(var)) {
                return MIPSTbl.get_t_num(var);
            } else {
                int t_addr = MIPSTbl.tName2tAddr.get(var);
                mips_text.add(new MIPSCode.LW(target_num, t_addr, 0));
                return target_num;
            }
        } else if (var.charAt(0) == '@') {
            int g_addr = MIPSTbl.global_name2addr.get(var);
            mips_text.add(new MIPSCode.LW(target_num, g_addr, 0));
            return target_num;
        } else if (var.charAt(0) == '%') {
            if (MIPSTbl.regOrMem_trueIfReg(var)) {
                return MIPSTbl.get_s_num(var);
            } else {
                int m_addr = MIPSTbl.main_name2addr.get(var);
                mips_text.add(new MIPSCode.LW(target_num, m_addr, 0));
                return target_num;
            }
        } else if (var.charAt(0) == '^' || var.charAt(0) == '!') {
            int sp_offset = (var.charAt(0) == '^') ? MIPSTbl.func_name2offset.get(var) : MIPSTbl.get_para_name_2_sp_offset(var);
            mips_text.add(new MIPSCode.LW(target_num, sp_offset, MIPSTbl.sp));
            return target_num;
        } else if (var.equals("$0")) {
            return 0;
        } else {
            System.out.println(String.format("var = \"%s\"", var));
            System.out.println("Something's wrong with _6_Q!!!");
            return -1;
        }
    }

    private boolean isConst16(int i) {
        return (i <= ((1 << 15) - 1)) && (i >= (-(1 << 15)));
    }

    @Override
    public void release_t() {
        release = true;
    }
}
