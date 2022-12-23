package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import FrontEnd.IRGenerator.Quadruple.Elements.LVal;

import java.util.ArrayList;

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

    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        /* TODO */
        if (type.equals("main")) {
            // 主函数中
            if (R_isVal) {
                // 右值是%d
                if (!left.isArr) {
                    // %s = %d

                }
                else if (left.o_isVal) {
                    // %s[%d] = %d
                }
                else {
                    // %s[%s] = %d
                }
            }
            else {
                // 右值是%s
                if (!left.isArr) {
                    // %s = %s
                }
                else if (left.o_isVal) {
                    // %s[%d] = %s
                }
                else {
                    // %s[%s] = %s
                }
            }
        }
        else {

        }
    }
}
