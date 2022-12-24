package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;

import java.util.ArrayList;

import static BackEnd.MIPSTbl.v0;

public class _16_T_FuncCallRet_Q extends IRCode {
    String temp;

    public _16_T_FuncCallRet_Q(String temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return String.format("%s = RET\n", temp);
    }

    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        int t_num = MIPSTbl.allocate_t_reg(temp);
        mips_text.add(new MIPSCode.Move(t_num, v0));
    }
}
