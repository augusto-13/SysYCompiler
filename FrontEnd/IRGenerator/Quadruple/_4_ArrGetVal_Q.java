package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;

import java.util.ArrayList;

import static BackEnd.MIPSTbl.t0;
import static BackEnd.MIPSTbl.t1;

public class _4_ArrGetVal_Q extends IRCode {
    String temp;
    String name;
    boolean o_isVal;
    String o_var;
    int o_val;

    public _4_ArrGetVal_Q(String t, String n, String o) {
        temp = t;
        name = n;
        o_isVal = false;
        o_var = o;
    }

    public _4_ArrGetVal_Q(String t, String n, int o) {
        temp = t;
        name = n;
        o_isVal = true;
        o_val = o;
    }

    @Override
    public String toString() {
        return o_isVal ? String.format("%s = %s[%d]\n", temp, name, o_val) : String.format("%s = %s[%s]\n", temp, name, o_var);
    }

    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        // 先为temp分配寄存器
        int t_num = MIPSTbl.allocate_t_reg(temp);
        if (t_num != -1) {
            if (name.charAt(0) == '@') {
                // 全局变量
                int g_addr = MIPSTbl.global_name2addr.get(name);
                if (o_isVal) {
                    // %s[%d]
                    mips_text.add(new MIPSCode.LW(t_num, g_addr + (o_val << 2), 0));
                }
                else {
                    // %s[%s]
                    if (o_var.charAt(0) == 't') {
                        if (MIPSTbl.regOrMem_trueIfReg(o_var)) {
                            // 分配了寄存器
                            mips_text.add(new MIPSCode.Cal_RI(t0, MIPSTbl.get_t_num(o_var), "<<", 2));
                            mips_text.add(new MIPSCode.LW(t_num, g_addr, t0));
                        }
                        else {
                            // 未分配寄存器
                            int t_addr_in = MIPSTbl.get_t_addr(o_var);
                            mips_text.add(new MIPSCode.LW(t0, t_addr_in, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                            mips_text.add(new MIPSCode.LW(t_num, g_addr, t0));
                        }
                    }
                    else if (o_var.charAt(0) == '@') {
                        int g_addr_in = MIPSTbl.global_name2addr.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, g_addr_in, 0));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.LW(t_num, g_addr, t0));
                    }
                    else if (o_var.charAt(0) == '%') {
                        if (MIPSTbl.regOrMem_trueIfReg(o_var)) {
                            // 分配了寄存器
                            mips_text.add(new MIPSCode.Cal_RI(t0, MIPSTbl.get_s_num(o_var), "<<", 2));
                            mips_text.add(new MIPSCode.LW(t_num, g_addr, t0));
                        }
                        else {
                            // 未分配寄存器
                            int m_addr_in = MIPSTbl.main_name2addr.get(o_var);
                            mips_text.add(new MIPSCode.LW(t0, m_addr_in, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                            mips_text.add(new MIPSCode.LW(t_num, g_addr, t0));
                        }
                    }
                    else if (o_var.charAt(0) == '^') {
                        int sp_offset_in = MIPSTbl.func_name2offset.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, MIPSTbl.sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.LW(t_num, g_addr, t0));
                    }
                    else if (o_var.charAt(0) == '!') {
                        int o_var_sp_offset_in = MIPSTbl.get_para_name_2_sp_offset(o_var);
                        mips_text.add(new MIPSCode.LW(t0, o_var_sp_offset_in, MIPSTbl.sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.LW(t_num, g_addr, t0));
                    }
                    else {
                        System.out.println("Something's wrong with _4_Q <2>");
                    }
                }
            }
            else if (name.charAt(0) == '%') {
                // 局部变量
                int m_addr = MIPSTbl.main_name2addr.get(name);
                if (o_isVal) {
                    mips_text.add(new MIPSCode.LW(t_num, m_addr + (o_val << 2), 0));
                }
                else {
                    if (o_var.charAt(0) == 't') {
                        if (MIPSTbl.regOrMem_trueIfReg(o_var)) {
                            // 分配了寄存器
                            mips_text.add(new MIPSCode.Cal_RI(t0, MIPSTbl.get_t_num(o_var), "<<", 2));
                            mips_text.add(new MIPSCode.LW(t_num, m_addr, t0));
                        }
                        else {
                            // 未分配寄存器
                            int t_addr_in = MIPSTbl.get_t_addr(o_var);
                            mips_text.add(new MIPSCode.LW(t0, t_addr_in, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                            mips_text.add(new MIPSCode.LW(t_num, m_addr, t0));
                        }
                    }
                    else if (o_var.charAt(0) == '@') {
                        int g_addr_in = MIPSTbl.global_name2addr.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, g_addr_in, 0));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.LW(t_num, m_addr, t0));
                    }
                    else if (o_var.charAt(0) == '%') {
                        if (MIPSTbl.regOrMem_trueIfReg(o_var)) {
                            // 分配了寄存器
                            mips_text.add(new MIPSCode.Cal_RI(t0, MIPSTbl.get_s_num(o_var), "<<", 2));
                            mips_text.add(new MIPSCode.LW(t_num, m_addr, t0));
                        }
                        else {
                            // 未分配寄存器
                            int m_addr_in = MIPSTbl.main_name2addr.get(o_var);
                            mips_text.add(new MIPSCode.LW(t0, m_addr_in, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                            mips_text.add(new MIPSCode.LW(t_num, m_addr, t0));
                        }
                    }
                    else {
                        System.out.println("Something's wrong with _4_Q <3>");
                    }
                }
            }
            else if (name.charAt(0) == '^') {
                // 函数声明内局部变量
                int sp_offset = MIPSTbl.func_name2offset.get(name);
                if (o_isVal) {
                    mips_text.add(new MIPSCode.LW(t_num, sp_offset + (o_val << 2), MIPSTbl.sp));
                }
                else {
                    if (o_var.charAt(0) == 't') {
                        int t_in = MIPSTbl.get_t_num(o_var);
                        if (t_in != -1) {
                            mips_text.add(new MIPSCode.Cal_RI(t0, t_in, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.sp));
                            mips_text.add(new MIPSCode.LW(t_num, sp_offset, t0));
                        }
                        else {
                            int t_in_addr = MIPSTbl.get_t_addr(o_var);
                            mips_text.add(new MIPSCode.LW(t0, t_in_addr, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.sp));
                            mips_text.add(new MIPSCode.LW(t_num, sp_offset, t0));
                        }

                    }
                    else if (o_var.charAt(0) == '@') {
                        int g_addr_in = MIPSTbl.global_name2addr.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, g_addr_in, 0));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.sp));
                        mips_text.add(new MIPSCode.LW(t_num, sp_offset, t0));
                    }
                    else if (o_var.charAt(0) == '^') {
                        int sp_offset_in = MIPSTbl.func_name2offset.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, MIPSTbl.sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.sp));
                        mips_text.add(new MIPSCode.LW(t_num, sp_offset, t0));
                    }
                    else if (o_var.charAt(0) == '!') {
                        int o_var_sp_offset_in = MIPSTbl.get_para_name_2_sp_offset(o_var);
                        mips_text.add(new MIPSCode.LW(t0, o_var_sp_offset_in, MIPSTbl.sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.sp));
                        mips_text.add(new MIPSCode.LW(t_num, sp_offset, t0));
                    }
                    else {
                        System.out.println("Something's wrong with _4_Q <4>");
                    }
                }
            }
            else if (name.charAt(0) == '!'){
                // 函数声明的形式参数
                int nameAddr_sp_offset = MIPSTbl.get_para_name_2_sp_offset(name);
                mips_text.add(new MIPSCode.LW(t0, nameAddr_sp_offset, MIPSTbl.sp));
                if (o_isVal) {
                    mips_text.add(new MIPSCode.LW(t_num, o_val << 2, t0));
                }
                else {
                    if (o_var.charAt(0) == 't') {
                        int t_in = MIPSTbl.get_t_num(o_var);
                        if (t_in != -1) {
                            mips_text.add(new MIPSCode.Cal_RI(MIPSTbl.t1, t_in, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RR(t0, MIPSTbl.t1, "+", t0));
                            mips_text.add(new MIPSCode.LW(t_num, 0, t0));
                        }
                        else {
                            int t_in_addr = MIPSTbl.get_t_addr(o_var);
                            mips_text.add(new MIPSCode.LW(t1, t_in_addr, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t1, t1, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RR(t0, t1, "+", t0));
                            mips_text.add(new MIPSCode.LW(t_num, 0, t0));
                        }

                    }
                    else if (o_var.charAt(0) == '@') {
                        int g_addr_in = MIPSTbl.global_name2addr.get(o_var);
                        mips_text.add(new MIPSCode.LW(MIPSTbl.t1, g_addr_in, 0));
                        mips_text.add(new MIPSCode.Cal_RI(MIPSTbl.t1, MIPSTbl.t1, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.t1));
                        mips_text.add(new MIPSCode.LW(t_num, 0, t0));
                    }
                    else if (o_var.charAt(0) == '^') {
                        int sp_offset_in = MIPSTbl.func_name2offset.get(o_var);
                        mips_text.add(new MIPSCode.LW(MIPSTbl.t1, sp_offset_in, MIPSTbl.sp));
                        mips_text.add(new MIPSCode.Cal_RI(MIPSTbl.t1, MIPSTbl.t1, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.t1));
                        mips_text.add(new MIPSCode.LW(t_num, 0, t0));
                    }
                    else if (o_var.charAt(0) == '!') {
                        int o_var_sp_offset_in = MIPSTbl.get_para_name_2_sp_offset(o_var);
                        mips_text.add(new MIPSCode.LW(MIPSTbl.t1, o_var_sp_offset_in, MIPSTbl.sp));
                        mips_text.add(new MIPSCode.Cal_RI(MIPSTbl.t1, MIPSTbl.t1, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.t1));
                        mips_text.add(new MIPSCode.LW(t_num, 0, t0));
                    }
                    else {
                        System.out.println("Something's wrong with _4_Q <5>");
                    }
                }
            }
            else {
                System.out.println("Something's wrong with _4_Q <1>");
            }
        }
        else {
            int t_addr = MIPSTbl.allocate_t_addr(temp);
            if (name.charAt(0) == '@') {
                // 全局变量
                int g_addr = MIPSTbl.global_name2addr.get(name);
                if (o_isVal) {
                    // %s[%d]
                    mips_text.add(new MIPSCode.LW(t0, g_addr + (o_val << 2), 0));
                    mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                }
                else {
                    // %s[%s]
                    if (o_var.charAt(0) == 't') {
                        if (MIPSTbl.regOrMem_trueIfReg(o_var)) {
                            // 分配了寄存器
                            mips_text.add(new MIPSCode.Cal_RI(t0, MIPSTbl.get_t_num(o_var), "<<", 2));
                            mips_text.add(new MIPSCode.LW(t0, g_addr, t0));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                        else {
                            // 未分配寄存器
                            int t_addr_in = MIPSTbl.get_t_addr(o_var);
                            mips_text.add(new MIPSCode.LW(t0, t_addr_in, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                            mips_text.add(new MIPSCode.LW(t0, g_addr, t0));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                    }
                    else if (o_var.charAt(0) == '@') {
                        int g_addr_in = MIPSTbl.global_name2addr.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, g_addr_in, 0));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.LW(t0, g_addr, t0));
                        mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                    }
                    else if (o_var.charAt(0) == '%') {
                        if (MIPSTbl.regOrMem_trueIfReg(o_var)) {
                            // 分配了寄存器
                            mips_text.add(new MIPSCode.Cal_RI(t0, MIPSTbl.get_s_num(o_var), "<<", 2));
                            mips_text.add(new MIPSCode.LW(t0, g_addr, t0));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                        else {
                            // 未分配寄存器
                            int m_addr_in = MIPSTbl.main_name2addr.get(o_var);
                            mips_text.add(new MIPSCode.LW(t0, m_addr_in, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                            mips_text.add(new MIPSCode.LW(t0, g_addr, t0));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                    }
                    else if (o_var.charAt(0) == '^') {
                        int sp_offset_in = MIPSTbl.func_name2offset.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, MIPSTbl.sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.LW(t0, g_addr, t0));
                        mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                    }
                    else if (o_var.charAt(0) == '!') {
                        int o_var_sp_offset_in = MIPSTbl.get_para_name_2_sp_offset(o_var);
                        mips_text.add(new MIPSCode.LW(t0, o_var_sp_offset_in, MIPSTbl.sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.LW(t0, g_addr, t0));
                        mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                    }
                    else {
                        System.out.println("Something's wrong with _4_Q <2>");
                    }
                }
            }
            else if (name.charAt(0) == '%') {
                // 局部变量
                int m_addr = MIPSTbl.main_name2addr.get(name);
                if (o_isVal) {
                    mips_text.add(new MIPSCode.LW(t0, m_addr + (o_val << 2), 0));
                    mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                }
                else {
                    if (o_var.charAt(0) == 't') {
                        if (MIPSTbl.regOrMem_trueIfReg(o_var)) {
                            // 分配了寄存器
                            mips_text.add(new MIPSCode.Cal_RI(t0, MIPSTbl.get_t_num(o_var), "<<", 2));
                            mips_text.add(new MIPSCode.LW(t0, m_addr, t0));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                        else {
                            // 未分配寄存器
                            int t_addr_in = MIPSTbl.get_t_addr(o_var);
                            mips_text.add(new MIPSCode.LW(t0, t_addr_in, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                            mips_text.add(new MIPSCode.LW(t0, m_addr, t0));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                    }
                    else if (o_var.charAt(0) == '@') {
                        int g_addr_in = MIPSTbl.global_name2addr.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, g_addr_in, 0));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.LW(t0, m_addr, t0));
                        mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                    }
                    else if (o_var.charAt(0) == '%') {
                        if (MIPSTbl.regOrMem_trueIfReg(o_var)) {
                            // 分配了寄存器
                            mips_text.add(new MIPSCode.Cal_RI(t0, MIPSTbl.get_s_num(o_var), "<<", 2));
                            mips_text.add(new MIPSCode.LW(t0, m_addr, t0));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                        else {
                            // 未分配寄存器
                            int m_addr_in = MIPSTbl.main_name2addr.get(o_var);
                            mips_text.add(new MIPSCode.LW(t0, m_addr_in, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                            mips_text.add(new MIPSCode.LW(t0, m_addr, t0));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                    }
                    else {
                        System.out.println("Something's wrong with _4_Q <3>");
                    }
                }
            }
            else if (name.charAt(0) == '^') {
                // 函数声明内局部变量
                int sp_offset = MIPSTbl.func_name2offset.get(name);
                if (o_isVal) {
                    mips_text.add(new MIPSCode.LW(t0, sp_offset + (o_val << 2), MIPSTbl.sp));
                    mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                }
                else {
                    if (o_var.charAt(0) == 't') {
                        int t_in = MIPSTbl.get_t_num(o_var);
                        if (t_in != -1) {
                            mips_text.add(new MIPSCode.Cal_RI(t0, t_in, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.sp));
                            mips_text.add(new MIPSCode.LW(t0, sp_offset, t0));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                        else {
                            int t_in_addr = MIPSTbl.get_t_addr(o_var);
                            mips_text.add(new MIPSCode.LW(t0, t_in_addr, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.sp));
                            mips_text.add(new MIPSCode.LW(t0, sp_offset, t0));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }

                    }
                    else if (o_var.charAt(0) == '@') {
                        int g_addr_in = MIPSTbl.global_name2addr.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, g_addr_in, 0));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.sp));
                        mips_text.add(new MIPSCode.LW(t0, sp_offset, t0));
                        mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                    }
                    else if (o_var.charAt(0) == '^') {
                        int sp_offset_in = MIPSTbl.func_name2offset.get(o_var);
                        mips_text.add(new MIPSCode.LW(t0, sp_offset_in, MIPSTbl.sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.sp));
                        mips_text.add(new MIPSCode.LW(t0, sp_offset, t0));
                        mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                    }
                    else if (o_var.charAt(0) == '!') {
                        int o_var_sp_offset_in = MIPSTbl.get_para_name_2_sp_offset(o_var);
                        mips_text.add(new MIPSCode.LW(t0, o_var_sp_offset_in, MIPSTbl.sp));
                        mips_text.add(new MIPSCode.Cal_RI(t0, t0, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.sp));
                        mips_text.add(new MIPSCode.LW(t0, sp_offset, t0));
                        mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                    }
                    else {
                        System.out.println("Something's wrong with _4_Q <4>");
                    }
                }
            }
            else if (name.charAt(0) == '!'){
                // 函数声明的形式参数
                int nameAddr_sp_offset = MIPSTbl.get_para_name_2_sp_offset(name);
                mips_text.add(new MIPSCode.LW(t0, nameAddr_sp_offset, MIPSTbl.sp));
                if (o_isVal) {
                    mips_text.add(new MIPSCode.LW(t0, o_val << 2, t0));
                    mips_text.add(new MIPSCode.SW(t0, t0, 0));
                }
                else {
                    if (o_var.charAt(0) == 't') {
                        int t_in = MIPSTbl.get_t_num(o_var);
                        if (t_in != -1) {
                            mips_text.add(new MIPSCode.Cal_RI(MIPSTbl.t1, t_in, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RR(t0, MIPSTbl.t1, "+", t0));
                            mips_text.add(new MIPSCode.LW(t0, 0, t0));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                        else {
                            int t_in_addr = MIPSTbl.get_t_addr(o_var);
                            mips_text.add(new MIPSCode.LW(t1, t_in_addr, 0));
                            mips_text.add(new MIPSCode.Cal_RI(t1, t1, "<<", 2));
                            mips_text.add(new MIPSCode.Cal_RR(t0, t1, "+", t0));
                            mips_text.add(new MIPSCode.LW(t0, 0, t0));
                            mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                        }
                    }
                    else if (o_var.charAt(0) == '@') {
                        int g_addr_in = MIPSTbl.global_name2addr.get(o_var);
                        mips_text.add(new MIPSCode.LW(MIPSTbl.t1, g_addr_in, 0));
                        mips_text.add(new MIPSCode.Cal_RI(MIPSTbl.t1, MIPSTbl.t1, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.t1));
                        mips_text.add(new MIPSCode.LW(t0, 0, t0));
                        mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                    }
                    else if (o_var.charAt(0) == '^') {
                        int sp_offset_in = MIPSTbl.func_name2offset.get(o_var);
                        mips_text.add(new MIPSCode.LW(MIPSTbl.t1, sp_offset_in, MIPSTbl.sp));
                        mips_text.add(new MIPSCode.Cal_RI(MIPSTbl.t1, MIPSTbl.t1, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.t1));
                        mips_text.add(new MIPSCode.LW(t0, 0, t0));
                        mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                    }
                    else if (o_var.charAt(0) == '!') {
                        int o_var_sp_offset_in = MIPSTbl.get_para_name_2_sp_offset(o_var);
                        mips_text.add(new MIPSCode.LW(MIPSTbl.t1, o_var_sp_offset_in, MIPSTbl.sp));
                        mips_text.add(new MIPSCode.Cal_RI(MIPSTbl.t1, MIPSTbl.t1, "<<", 2));
                        mips_text.add(new MIPSCode.Cal_RR(t0, t0, "+", MIPSTbl.t1));
                        mips_text.add(new MIPSCode.LW(t0, 0, t0));
                        mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
                    }
                    else {
                        System.out.println("Something's wrong with _4_Q <5>");
                    }
                }
            }
            else {
                System.out.println("Something's wrong with _4_Q <1>");
            }
        }
    }
}
