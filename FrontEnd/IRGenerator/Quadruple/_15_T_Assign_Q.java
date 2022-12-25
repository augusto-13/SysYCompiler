package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;

import java.util.ArrayList;

import static BackEnd.MIPSTbl.t0;

public class _15_T_Assign_Q extends IRCode {
    String t_left;
    int right_val;
    boolean release = false;

    public _15_T_Assign_Q(String t_left, int right_val) {
        this.t_left = t_left;
        this.right_val = right_val;
    }

    // OK!!!
    @Override
    public String toString() {
        return (!release) ? String.format("%s = %d\n", t_left, right_val) : "";
    }

    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        if (!release) {
            int t_num = MIPSTbl.allocate_t_reg(t_left);
            if (t_num != -1) {
                mips_text.add(new MIPSCode.LI(t_num, right_val));
            }
            else {
                int t_addr = MIPSTbl.allocate_t_addr(t_left);
                mips_text.add(new MIPSCode.LI(t0, right_val));
                mips_text.add(new MIPSCode.SW(t0, t_addr, 0));
            }
        }
    }

    @Override
    public void release_t() {
        release = true;
    }
}
