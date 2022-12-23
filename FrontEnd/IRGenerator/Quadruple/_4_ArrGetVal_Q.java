package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;

import java.util.ArrayList;

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
                    int t_in = MIPSTbl.get_t_num(o_var);
                    mips_text.add(new )
                }
                else if (o_var.charAt(0) == '@') {

                }
                else if (o_var.charAt(0) == '%') {

                }
                else if (o_var.charAt(0) == '^') {

                }
                else if (o_var.charAt(0) == '!') {

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
                //
            }
        }
        else if (name.charAt(0) == '^') {
            // 函数声明内局部变量
            int sp_offset = MIPSTbl.func_name2offset.get(name);
            if (o_isVal) {
                mips_text.add(new MIPSCode.LW(t_num, sp_offset + (o_val << 2), MIPSTbl.sp));
            }
            else {

            }
        }
        else if (name.charAt(0) == '!'){
            // 函数声明的形式参数
            int nameAddr_sp_offset = MIPSTbl.get_para_name_2_sp_offset(name);
            mips_text.add(new MIPSCode.LW(MIPSTbl.t0, nameAddr_sp_offset, MIPSTbl.sp));
            if (o_isVal) {
                mips_text.add(new MIPSCode.LW(t_num, o_val << 2, MIPSTbl.t0));
            }
            else {

            }
        }
        else {
            System.out.println("Something's wrong with _4_Q <1>");
        }
    }
}
