package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;

import java.util.ArrayList;

import static BackEnd.MIPSTbl.v0;

public class _16_T_FuncCallRet_Q extends IRCode {
    String temp;
    boolean release = false;

    public _16_T_FuncCallRet_Q(String temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return (!release) ? String.format("%s = RET\n", temp) : "";
    }

    // OK!!!
    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        if (!release) {
            int t_num = MIPSTbl.allocate_t_reg(temp);
            if (t_num != -1) {
                mips_text.add(new MIPSCode.Move(t_num, v0));
            }
            else {
                int t_addr = MIPSTbl.allocate_t_addr(temp);
                mips_text.add(new MIPSCode.SW(v0, t_addr, 0));
            }
        }
    }

    @Override
    public void release_t() {
        release = true;
    }
}
