package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;

import java.util.ArrayList;

public class _15_T_Assign_Q extends IRCode {
    String t_left;
    int right_val;

    public _15_T_Assign_Q(String t_left, int right_val) {
        this.t_left = t_left;
        this.right_val = right_val;
    }

    @Override
    public String toString() {
        return String.format("%s = %d\n", t_left, right_val);
    }

    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        int t_num = MIPSTbl.allocate_t_reg(t_left);
        mips_text.add(new MIPSCode.LI(t_num, right_val));
    }
}
