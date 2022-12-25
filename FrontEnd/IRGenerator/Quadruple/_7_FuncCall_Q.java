package FrontEnd.IRGenerator.Quadruple;


import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    // OK???
    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        // gist: 1) sw $ra, 0($sp)
        //       2) 将变量移入寄存器$t0，压栈 sw $t0, -4($sp) <内部计数>
        //       3) $sp -=
        //       4) jal
        //       5) $sp +=
        //       6) lw $ra, 0($sp)
        //       7) lw $_, -4 * k($sp)

        // 1. 先将$ra压入栈中
        mips_text.add(new MIPSCode.SW(ra, MIPSTbl.sp_offset, sp));
        // 2-1. 趁tName2tNum表中信息尚未删除，将参数压栈
        // 这里需要进行一步预计算：先计算出被占用临时变量数目k
        // 初始化：sp_offset = -4 - 4 * k
        ArrayList<MIPSCode> temp_mips_text = new ArrayList<>();
        int sp_offset = MIPSTbl.sp_offset - 4 - 4 * MIPSTbl.tName2tNum.size() - 4 * MIPSTbl.tName2tAddr.size();
        for (String arg : args) {
            int arg_reg_num = getRegNum(arg, temp_mips_text);
            temp_mips_text.add(new MIPSCode.SW(arg_reg_num, sp_offset, sp));
            sp_offset -= 4;
        }
        // 2-2. 将临时变量寄存器值压入栈中
        int _sp_offset = MIPSTbl.sp_offset - 4;
        HashMap<String, Integer> tNum2push = MIPSTbl.tNum2push();
        for (Map.Entry<String, Integer> t : tNum2push.entrySet()) {
            mips_text.add(new MIPSCode.SW(t.getValue(), _sp_offset, sp));
            _sp_offset -= 4;
        }
        HashMap<String, Integer> tAddr2push = MIPSTbl.tAddr2push();
        for (Map.Entry<String, Integer> t : tAddr2push.entrySet()) {
            mips_text.add(new MIPSCode.LW(t0, t.getValue(), 0));
            mips_text.add(new MIPSCode.SW(t0, _sp_offset, sp));
            _sp_offset -= 4;
        }
        mips_text.addAll(temp_mips_text);
        // 3
        mips_text.add(new MIPSCode.Cal_RI(sp, sp, "-", -sp_offset));
        // 4
        mips_text.add(new MIPSCode.JAL(name));
        // 5
        mips_text.add(new MIPSCode.Cal_RI(sp, sp, "+", -sp_offset));
        // 6
        mips_text.add(new MIPSCode.LW(ra, MIPSTbl.sp_offset, sp));
        // 7
        MIPSTbl.restoreAll_tNum(tNum2push);
        MIPSTbl.restoreAll_tAddr(tAddr2push);
        sp_offset = MIPSTbl.sp_offset - 4;
        for (Map.Entry<String, Integer> t : tNum2push.entrySet()) {
            mips_text.add(new MIPSCode.LW(t.getValue(), sp_offset, sp));
            sp_offset -= 4;
        }
        for (Map.Entry<String, Integer> t : tAddr2push.entrySet()) {
            mips_text.add(new MIPSCode.LW(t0, sp_offset, sp));
            mips_text.add(new MIPSCode.SW(t0, t.getValue(), 0));
            sp_offset -= 4;
        }
    }

    private int getRegNum(String var, ArrayList<MIPSCode> mips_text) {
        if (var.charAt(0) == 't') {
            if (MIPSTbl.regOrMem_trueIfReg(var)) {
                return MIPSTbl.get_t_num(var);
            }
            else {
                int t_addr = MIPSTbl.get_t_addr(var);
                mips_text.add(new MIPSCode.LW(t0, t_addr, 0));
                return t0;
            }
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
