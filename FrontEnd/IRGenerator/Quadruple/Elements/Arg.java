package FrontEnd.IRGenerator.Quadruple.Elements;

public class Arg {
    boolean a_isVal;
    String a_var;
    int a_val;

    public Arg(int a_val) {
        a_isVal = true;
        this.a_val = a_val;
    }

    public Arg(String a_var) {
        a_isVal = false;
        this.a_var = a_var;
    }

    @Override
    public String toString() {
        return  a_isVal ? String.valueOf(a_val) : a_var;
    }
}
