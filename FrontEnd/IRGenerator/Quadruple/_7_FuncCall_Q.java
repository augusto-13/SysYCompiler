package FrontEnd.IRGenerator.Quadruple;


import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;

import java.util.ArrayList;

import static BackEnd.MIPSTbl.ra;
import static BackEnd.MIPSTbl.sp;
import static BackEnd.MIPSTbl.t0;

public class _7_FuncCall_Q extends IRCode {
    String name;
    ArrayList<String> args;

    public _7_FuncCall_Q(String name, ArrayList<String> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (String arg : args) {
            ret.append("push ").append(arg).append("\n");
        }
        ret.append("call ").append(name).append("\n");
        return ret.toString();
    }

    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        // gist: 1) sw $ra, 0($sp)
        //       2) 将变量移入寄存器$t0，压栈 sw $t0, -4($sp) <内部计数>
        //       3) $sp -=
        //       4) jal
        //       5) $sp +=
        //       6) lw $ra, 0($sp)
        mips_text.add(new MIPSCode.SW(ra, 0, sp));
        int sp_offset = -4;
        for (String arg : args) {
            int arg_reg_num = getRegNum(arg, mips_text);
            mips_text.add(new MIPSCode.SW(arg_reg_num, sp_offset, sp));
            sp_offset -= 4;
        }
        mips_text.add(new MIPSCode.Cal_RI(sp, sp, "-", -sp_offset));
        mips_text.add(new MIPSCode.JAL(name));
        mips_text.add(new MIPSCode.Cal_RI(sp, sp, "+", -sp_offset));
        mips_text.add(new MIPSCode.LW(ra, 0, sp));
    }

    private int getRegNum(String var, ArrayList<MIPSCode> mips_text) {
        if (var.charAt(0) == 't') {
            return MIPSTbl.get_t_num(var);
        }
        else if (var.charAt(0) == '@') {
            int g_addr = MIPSTbl.global_name2addr.get(var);
            mips_text.add(new MIPSCode.LW(t0, g_addr, 0));
            return t0;
        }
        else if (var.charAt(0) == '%') {
            if (MIPSTbl.regOrMem_trueIfReg(var)) {
                return MIPSTbl.get_s_num(var);
            }
            else {
                int m_addr = MIPSTbl.main_name2addr.get(var);
                mips_text.add(new MIPSCode.LW(t0, m_addr, 0));
                return t0;
            }
        }
        else if (var.charAt(0) == '^' || var.charAt(0) == '!') {
            int sp_offset = (var.charAt(0) == '^') ? MIPSTbl.func_name2offset.get(var) : MIPSTbl.get_para_name_2_sp_offset(var);
            mips_text.add(new MIPSCode.LW(t0, sp_offset, MIPSTbl.sp));
            return t0;
        }
        else {
            System.out.println(String.format("var = \"%s\"", var));
            System.out.println("Something's wrong with _7_Q!!!");
            return -1;
        }
    }
}
