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
        assert t_left.charAt(0) == 't';
        int t_num = MIPSTbl.get_t_num(t_left);
        mips_text.add(new MIPSCode.LoadImm(t_num, right_val));
    }
}
