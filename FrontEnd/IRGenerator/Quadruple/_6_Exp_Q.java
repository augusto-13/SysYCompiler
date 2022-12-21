package FrontEnd.IRGenerator.Quadruple;

public class _6_Exp_Q extends IRCode {

    private String res;
    private String op;
    private String arg1;
    private boolean isImm;
    private String arg2;
    private int imm;


    public _6_Exp_Q(String r, String a1, String o, String a2) {
        res = r;
        arg1 = a1;
        op = o;
        arg2 = a2;
        isImm = false;
    }

    public _6_Exp_Q(String r, String a1, String o, int i) {
        res = r;
        arg1 = a1;
        op = o;
        imm = i;
        isImm = true;
    }

    @Override
    public String toString() {
        return isImm ? String.format("%s = %s %s %d\n", res, arg1, op, imm) : String.format("%s = %s %s %s\n", res, arg1, op, arg2);
    }
}
