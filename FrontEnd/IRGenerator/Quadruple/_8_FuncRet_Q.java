package FrontEnd.IRGenerator.Quadruple;

public class _8_FuncRet_Q extends IRCode {
    boolean ret_void;
    boolean ret_isVal;
    String ret_var;
    int ret_val;

    public _8_FuncRet_Q() {
        ret_void = true;
    }

    public _8_FuncRet_Q(String ret_var) {
        ret_void = false;
        ret_isVal = false;
        this.ret_var = ret_var;
    }

    public _8_FuncRet_Q(int ret_val) {
        ret_void = false;
        ret_isVal = true;
        this.ret_val = ret_val;
    }

    @Override
    public String toString() {
        return ret_void ? "ret\n" : ret_isVal ? String.format("ret %d\n", ret_val) : String.format("ret %s\n", ret_var);
    }
}
