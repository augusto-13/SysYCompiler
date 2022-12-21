package FrontEnd.IRGenerator.Quadruple.Elements;

public class LVal {
    String name;
    boolean isArr;
    boolean o_isVal;
    String o_var;
    int o_val;

    public LVal(String n) {
        isArr = false;
        name = n;
    }

    public LVal(String n, String o) {
        isArr = true;
        o_isVal = false;
        name = n;
        o_var = o;
    }

    public LVal(String n, int o) {
        isArr = true;
        o_isVal = true;
        name = n;
        o_val = o;
    }

    @Override
    public String toString() {
        return !isArr ? name : o_isVal ? String.format("%s[%d]", name, o_val) : String.format("%s[%s]", name, o_var);
    }
}
