package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;

import java.util.ArrayList;

import static BackEnd.MIPSTbl.t0;
import static BackEnd.MIPSTbl.v0;

public class _10_In_Q extends IRCode {
    String temp;

    public _10_In_Q(String t) {
        temp = t;
    }

    @Override
    public String toString() {
        return String.format("scanf %s\n", temp);
    }

    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        // OK!!!
        int t_num = MIPSTbl.allocate_t_reg(temp);
        if (t_num != -1) {
            mips_text.add(new MIPSCode.LI(v0, 5));
            mips_text.add(new MIPSCode.Sys());
            mips_text.add(new MIPSCode.Move(t_num, v0));
        }
        else {
            int t_addr = MIPSTbl.allocate_t_addr(temp);
            mips_text.add(new MIPSCode.LI(v0, 5));
            mips_text.add(new MIPSCode.Sys());
            mips_text.add(new MIPSCode.SW(v0, t_addr, 0));
        }
    }
}
