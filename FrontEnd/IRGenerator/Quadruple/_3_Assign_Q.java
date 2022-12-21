package FrontEnd.IRGenerator.Quadruple;

import FrontEnd.IRGenerator.Quadruple.Elements.LVal;

public class _3_Assign_Q extends IRCode {
    LVal left;
    boolean R_isVal;
    String right_var;
    int right_val;

    public _3_Assign_Q(LVal l, String r) {
        left = l;
        right_var = r;
        R_isVal = false;
    }

    public _3_Assign_Q(LVal l, int r) {
        left = l;
        right_val = r;
        R_isVal = true;
    }

    @Override
    public String toString() {
        return R_isVal ? String.format("%s = %d\n", left, right_val) : String.format("%s = %s\n", left, right_var);
    }
}
