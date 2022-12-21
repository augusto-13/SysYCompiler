package FrontEnd.IRGenerator.Quadruple;

public class _1_VarDecl_Q extends IRCode {
    String name;
    boolean init = false;
    int global_initVal;

    public _1_VarDecl_Q(String name) {
        this.name = name;
    }

    public _1_VarDecl_Q(String name, int global_initVal) {
        this.name = name;
        this.init = true;
        this.global_initVal = global_initVal;
    }

    @Override
    public String toString() {
        return (init) ? String.format("var int %s = %d\n", name, global_initVal) : String.format("var int %s\n", name);
    }
}
