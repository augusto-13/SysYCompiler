package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;

import java.util.ArrayList;

import static BackEnd.MIPSTbl.allocate_t_addr;
import static BackEnd.MIPSTbl.get_t_addr;
import static BackEnd.MIPSTbl.sp;
import static BackEnd.MIPSTbl.t0;
import static BackEnd.MIPSTbl.t1;

public class _5_ArrGetAddr_Q extends IRCode {
    String temp;
    String name;
    boolean o_valid;
    boolean o_isVal;
    String o_var;
    int o_val;

    public _5_ArrGetAddr_Q(String t, String n, String o) {
        temp = t;
        name = n;
        o_valid = true;
        o_isVal = false;
        o_var = o;
    }

    public _5_ArrGetAddr_Q(String t, String n, int o) {
        temp = t;
        name = n;
        o_valid = true;
        o_isVal = true;
        o_val = o;
    }

    public _5_ArrGetAddr_Q(String t, String n) {
        o_valid = false;
        temp = t;
        name = n;
    }

    @Override
    public String toString() {
        return !o_valid ? String.format("%s = &%s\n", temp, name) :
                o_isVal ? String.format("%s = &%s[%d]\n", temp, name, o_val) :
                          String.format("%s = &%s[%s]\n", temp, name, o_var);
    }

    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        int t_num = MIPSTbl.allocate_t_reg(temp);
        if (t_num != -1) {
            if (!o_valid) {
                // %s = &%s
                if (name.charAt(0) == '@') {
                    int g_addr = MIPSTbl.global_name2addr.get(name);
                    mips_text.add(new MIPSCode.LI(t_num, g_addr));
                }
                else if (name.charAt(0) == '%') {
                    int m_addr = MIPSTbl.main_name2addr.get(name);
                    mips_text.add(new MIPSCode.LI(t_num, m_addr));
                }
                else if (name.charAt(0) == '!') {
                    int sp_offset = MIPSTbl.get_para_name_2_sp_offset(name);
                    mips_text.add(new MIPSCode.LW(t_num, sp_offset, sp));
                }
                else if (name.charAt(0) == '^') {
                    int sp_offset = MIPSTbl.func_name2offset.get(name);
                    mips_text.add(new MIPSCode.Cal_RI(t_num, sp, "+", sp_offset));
                }
                else {
                    System.out.println("Something's wrong with _5_Q <1>");
                }
            }
            else if (o_isVal) {
                // %s = &%s[%d]
                if (name.charAt(0) == '@') {
                    int g_addr = MIPSTbl.global_name2addr.get(name);
                    mips_text.add(new MIPSCode.LI(t_num, g_addr + (o_val << 2)));
                }
                else if (name.charAt(0) == '%') {
                    int m_addr = MIPSTbl.main_name2addr.get(name);
                    mips_text.add(new MIPSCode.LI(t_num, m_addr + (o_val << 2)));
                }
                else if (name.charAt(0) == '!') {
                    int sp_offset = MIPSTbl.get_para_name_2_sp_offset(name);
                    mips_text.add(new MIPSCode.LW(t0, sp_offset, sp));
                    mips_text.add(new MIPSCode.Cal_RI(t_num, t0, "+", (o_val << 2)));
                }
                else if (name.charAt(0) == '^') {
                    int sp_offset = MIPSTbl.func_name2offset.get(name);
                    mips_text.add(new MIPSCode.Cal_RI(t_num, sp, "+", sp_offset + (o_val << 2)));
                }
                else {
                    System.out.println("Something's wrong with _5_Q <2>");
                }
            }
            else {
                // %s = &%s[%s]
                if (name.charAt(0) == '@' || name.charAt(0) == '%') {
                    int addr = (name.charAt(0) == '@') ? MIPSTbl.global_name2addr.get(name) : MIPSTbl.main_name2addr.get(name);
                    // gist: 1) $t0 = o_var << 2
                    //       2) $t_num = $t0 + g_addr
                    if (o_var.charAt(0) == 't') {
                        if (MIPSTbl.regOrMem_trueIfReg(o_var)) {
                            // 寄存器中
                            int t_in = MIPSTbl.get_t_num(o_var);
                            mips_text.add(new MIPSCode.Cal_RI(t0, t_in, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RI(t_num, t0, "+", addr));
                        }
                        else {
                            // 内存中
                            int t_in_addr = MIPSTbl.get_t_addr(o_var);
                            mips_text.add(new MIPSCode.LW(t0, t_in_addr, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RI(t_num, t0, "+", addr));
                        }
                    }
                    else if (o_var.charAt(0) == '@') {
                        int g_addr_in = MIPSTbl.global_name2addr.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, g_addr_in, 0));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RI(t_num, t0, "+", addr));
                    }
                    else if (o_var.charAt(0) == '%') {
                        if (MIPSTbl.regOrMem_trueIfReg(o_var)) {
                            // 寄存器中
                            int s_reg_in = MIPSTbl.get_s_num(o_var);
                            mips_text.add(new MIPSCode.Cal_RI(t0, s_reg_in, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RI(t_num, t0, "+", addr));
                        }
                        else {
                            // 内存中
                            int s_addr_in = MIPSTbl.main_name2addr.get(o_var);
                            mips_text.add(new MIPSCode.LW(t0, s_addr_in, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RI(t_num, t0, "+", addr));
                        }
                    }
                    else if (o_var.charAt(0) == '!') {
                        int sp_offset_in = MIPSTbl.get_para_name_2_sp_offset(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RI(t_num, t0, "+", addr));
                    }
                    else if (o_var.charAt(0) == '^') {
                        int sp_offset_in = MIPSTbl.func_name2offset.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RI(t_num, t0, "+", addr));
                    }
                    else {
                        System.out.println("Something's wrong with _5_Q <3>");
                    }
                }
                else if (name.charAt(0) == '!') {
                    int sp_offset = MIPSTbl.get_para_name_2_sp_offset(name);
                    int addr_reg = t1;
                    mips_text.add(new MIPSCode.LW(addr_reg, sp_offset, sp));
                    // mips_text.add(new MIPSCode.Cal_RI(t_num, sp, "+", sp_offset + (o_val << 2)));
                    // gist: 1) $t0 = o_var << 2
                    //       2) $t0 = $t0 + sp_offset
                    //       3) $t_num = $sp + $t0
                    if (o_var.charAt(0) == 't') {
                        int t_num_in = MIPSTbl.get_t_num(o_var);
                        if (t_num_in != -1) {
                            mips_text.add(new MIPSCode.Cal_RI(t0, t_num_in, "<<", 2));
                        }
                        else {
                            int t_in_addr = get_t_addr(o_var);
                            mips_text.add(new MIPSCode.LW(t0, t_in_addr, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        }
                    }
                    else if (o_var.charAt(0) == '@') {
                        int g_addr_in = MIPSTbl.global_name2addr.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, g_addr_in, 0));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                    }
                    else if (o_var.charAt(0) == '!') {
                        int sp_offset_in = MIPSTbl.get_para_name_2_sp_offset(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                    }
                    else if (o_var. charAt(0) == '^') {
                        int sp_offset_in = MIPSTbl.func_name2offset.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                    }
                    else {
                        System.out.println("Something's wrong with _5_Q <4>");
                    }
                    mips_text.add(new MIPSCode.Cal_RR(t_num, t0, "+", addr_reg));
                }
                else if (name.charAt(0) == '^') {
                    int sp_offset = MIPSTbl.func_name2offset.get(name);
                    // mips_text.add(new MIPSCode.Cal_RI(t_num, sp, "+", sp_offset + (o_val << 2)));
                    // gist: 1) $t0 = o_var << 2
                    //       2) $t0 = $t0 + sp_offset
                    //       3) $t_num = $sp + $t0
                    if (o_var.charAt(0) == 't') {
                        int t_num_in = MIPSTbl.get_t_num(o_var);
                        if (t_num_in != -1) {
                            mips_text.add(new MIPSCode.Cal_RI(t0, t_num_in, "<<", 2));
                        }
                        else {
                            int t_in_addr = get_t_addr(o_var);
                            mips_text.add(new MIPSCode.LW(t0, t_in_addr, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        }
                    }
                    else if (o_var.charAt(0) == '@') {
                        int g_addr_in = MIPSTbl.global_name2addr.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, g_addr_in, 0));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                    }
                    else if (o_var.charAt(0) == '!') {
                        int sp_offset_in = MIPSTbl.get_para_name_2_sp_offset(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                    }
                    else if (o_var. charAt(0) == '^') {
                        int sp_offset_in = MIPSTbl.func_name2offset.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                    }
                    else {
                        System.out.println("Something's wrong with _5_Q <4>");
                    }
                    mips_text.add(new MIPSCode.Cal_RI(t0, t0, "+", sp_offset));
                    mips_text.add(new MIPSCode.Cal_RR(t_num, sp, "+", t0));
                }
                else {
                    System.out.println("Something's wrong with _5_Q <2>");
                }
            }
        }
        else {
            int t_addr = allocate_t_addr(temp);
            if (!o_valid) {
                // %s = &%s
                if (name.charAt(0) == '@') {
                    int g_addr = MIPSTbl.global_name2addr.get(name);
                    mips_text.add(new MIPSCode.LI(t0, g_addr));
                    mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                }
                else if (name.charAt(0) == '%') {
                    int m_addr = MIPSTbl.main_name2addr.get(name);
                    mips_text.add(new MIPSCode.LI(t0, m_addr));
                    mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                }
                else if (name.charAt(0) == '!') {
                    int sp_offset = MIPSTbl.get_para_name_2_sp_offset(name);
                    mips_text.add(new MIPSCode.LW(t0, sp_offset, sp));
                    mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                }
                else if (name.charAt(0) == '^') {
                    int sp_offset = MIPSTbl.func_name2offset.get(name);
                    mips_text.add(new MIPSCode.Cal_RI(t0, sp, "+", sp_offset));
                    mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                }
                else {
                    System.out.println("Something's wrong with _5_Q <1>");
                }
            }
            else if (o_isVal) {
                // %s = &%s[%d]
                if (name.charAt(0) == '@') {
                    int g_addr = MIPSTbl.global_name2addr.get(name);
                    mips_text.add(new MIPSCode.LI(t0, g_addr + (o_val << 2)));
                    mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                }
                else if (name.charAt(0) == '%') {
                    int m_addr = MIPSTbl.main_name2addr.get(name);
                    mips_text.add(new MIPSCode.LI(t0, m_addr + (o_val << 2)));
                    mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                }
                else if (name.charAt(0) == '!') {
                    int sp_offset = MIPSTbl.get_para_name_2_sp_offset(name);
                    mips_text.add(new MIPSCode.LW(t0, sp_offset, sp));
                    mips_text.add(new MIPSCode.Cal_RI(t0, t0, "+", (o_val << 2)));
                    mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                }
                else if (name.charAt(0) == '^') {
                    int sp_offset = MIPSTbl.func_name2offset.get(name);
                    mips_text.add(new MIPSCode.Cal_RI(t0, sp, "+", sp_offset + (o_val << 2)));
                    mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                }
                else {
                    System.out.println("Something's wrong with _5_Q <2>");
                }
            }
            else {
                // %s = &%s[%s]
                if (name.charAt(0) == '@' || name.charAt(0) == '%') {
                    int addr = (name.charAt(0) == '@') ? MIPSTbl.global_name2addr.get(name) : MIPSTbl.main_name2addr.get(name);
                    // gist: 1) $t0 = o_var << 2
                    //       2) $t_num = $t0 + g_addr
                    if (o_var.charAt(0) == 't') {
                        if (MIPSTbl.regOrMem_trueIfReg(o_var)) {
                            // 寄存器中
                            int t_in = MIPSTbl.get_t_num(o_var);
                            mips_text.add(new MIPSCode.Cal_RI(t0, t_in, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "+", addr));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                        else {
                            // 内存中
                            int t_in_addr = MIPSTbl.get_t_addr(o_var);
                            mips_text.add(new MIPSCode.LW(t0, t_in_addr, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "+", addr));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                    }
                    else if (o_var.charAt(0) == '@') {
                        int g_addr_in = MIPSTbl.global_name2addr.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, g_addr_in, 0));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "+", addr));
                        mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                    }
                    else if (o_var.charAt(0) == '%') {
                        if (MIPSTbl.regOrMem_trueIfReg(o_var)) {
                            // 寄存器中
                            int s_reg_in = MIPSTbl.get_s_num(o_var);
                            mips_text.add(new MIPSCode.Cal_RI(t0, s_reg_in, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "+", addr));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                        else {
                            // 内存中
                            int s_addr_in = MIPSTbl.main_name2addr.get(o_var);
                            mips_text.add(new MIPSCode.LW(t0, s_addr_in, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "+", addr));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                    }
                    else if (o_var.charAt(0) == '!') {
                        int sp_offset_in = MIPSTbl.get_para_name_2_sp_offset(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "+", addr));
                        mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                    }
                    else if (o_var.charAt(0) == '^') {
                        int sp_offset_in = MIPSTbl.func_name2offset.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "+", addr));
                        mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                    }
                    else {
                        System.out.println("Something's wrong with _5_Q <3>");
                    }
                }
                else if (name.charAt(0) == '!') {
                    int sp_offset = MIPSTbl.get_para_name_2_sp_offset(name);
                    int addr_reg = t1;
                    mips_text.add(new MIPSCode.LW(addr_reg, sp_offset, sp));
                    // mips_text.add(new MIPSCode.Cal_RI(t_num, sp, "+", sp_offset + (o_val << 2)));
                    // gist: 1) $t0 = o_var << 2
                    //       2) $t0 = $t0 + sp_offset
                    //       3) $t_num = $sp + $t0
                    if (o_var.charAt(0) == 't') {
                        int t_num_in = MIPSTbl.get_t_num(o_var);
                        if (t_num_in != -1) {
                            mips_text.add(new MIPSCode.Cal_RI(t0, t_num_in, "<<", 2));
                        }
                        else {
                            int t_in_addr = get_t_addr(o_var);
                            mips_text.add(new MIPSCode.LW(t0, t_in_addr, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        }
                    }
                    else if (o_var.charAt(0) == '@') {
                        int g_addr_in = MIPSTbl.global_name2addr.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, g_addr_in, 0));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                    }
                    else if (o_var.charAt(0) == '!') {
                        int sp_offset_in = MIPSTbl.get_para_name_2_sp_offset(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                    }
                    else if (o_var. charAt(0) == '^') {
                        int sp_offset_in = MIPSTbl.func_name2offset.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                    }
                    else {
                        System.out.println("Something's wrong with _5_Q <4>");
                    }
                    mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", addr_reg));
                    mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                }
                else if (name.charAt(0) == '^') {
                    int sp_offset = MIPSTbl.func_name2offset.get(name);
                    // mips_text.add(new MIPSCode.Cal_RI(t_num, sp, "+", sp_offset + (o_val << 2)));
                    // gist: 1) $t0 = o_var << 2
                    //       2) $t0 = $t0 + sp_offset
                    //       3) $t_num = $sp + $t0
                    if (o_var.charAt(0) == 't') {
                        int t_num_in = MIPSTbl.get_t_num(o_var);
                        if (t_num_in != -1) {
                            mips_text.add(new MIPSCode.Cal_RI(t0, t_num_in, "<<", 2));
                        }
                        else {
                            int t_in_addr = get_t_addr(o_var);
                            mips_text.add(new MIPSCode.LW(t0, t_in_addr, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        }
                    }
                    else if (o_var.charAt(0) == '@') {
                        int g_addr_in = MIPSTbl.global_name2addr.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, g_addr_in, 0));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                    }
                    else if (o_var.charAt(0) == '!') {
                        int sp_offset_in = MIPSTbl.get_para_name_2_sp_offset(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                    }
                    else if (o_var. charAt(0) == '^') {
                        int sp_offset_in = MIPSTbl.func_name2offset.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                    }
                    else {
                        System.out.println("Something's wrong with _5_Q <4>");
                    }
                    mips_text.add(new MIPSCode.Cal_RI(t0, t0, "+", sp_offset));
                    mips_text.add(new MIPSCode.Cal_RR(t0, sp, "+", t0));
                    mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                }
                else {
                    System.out.println("Something's wrong with _5_Q <2>");
                }
            }
        }
    }
}
