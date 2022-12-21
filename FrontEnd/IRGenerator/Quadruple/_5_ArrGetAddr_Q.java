package FrontEnd.IRGenerator.Quadruple;

public class _5_ArrGetAddr_Q extends IRCode {
    String temp;
    String name;
    boolean o_valid;
    boolean o_isVal;
    String o_var;
    int o_val;

    public _5_ArrGetAddr_Q(String t, String n, String o) {
        temp = t;
        name = n;
        o_valid = true;
        o_isVal = false;
        o_var = o;
    }

    public _5_ArrGetAddr_Q(String t, String n, int o) {
        temp = t;
        name = n;
        o_valid = true;
        o_isVal = true;
        o_val = o;
    }

    public _5_ArrGetAddr_Q(String t, String n) {
        o_valid = false;
        temp = t;
        name = n;
    }

    @Override
    public String toString() {
        return o_valid ? String.format("%s = &%s\n", temp, name) : o_isVal ? String.format("%s = &%s[%d]\n", temp, name, o_val) : String.format("%s = &%s[%s]\n", temp, name, o_var);
    }
}
