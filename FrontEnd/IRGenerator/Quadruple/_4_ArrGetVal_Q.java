package FrontEnd.IRGenerator.Quadruple;

public class _4_ArrGetVal_Q extends IRCode {
    String temp;
    String name;
    boolean o_isVal;
    String o_var;
    int o_val;

    public _4_ArrGetVal_Q(String t, String n, String o) {
        temp = t;
        name = n;
        o_isVal = false;
        o_var = o;
    }

    public _4_ArrGetVal_Q(String t, String n, int o) {
        temp = t;
        name = n;
        o_isVal = true;
        o_val = o;
    }

    @Override
    public String toString() {
        return o_isVal ? String.format("%s = %s[%d]\n", temp, name, o_val) : String.format("%s = %s[%s]\n", temp, name, o_var);
    }
}
