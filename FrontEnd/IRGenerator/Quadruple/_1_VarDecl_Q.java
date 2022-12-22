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

    @Override
    public void toData(StringBuilder mips_data) {
        if (init) mips_data.append(String.format("%s: .word %d\n", name.substring(1), global_initVal));
        else mips_data.append(String.format("%s: .word 0\n", name.substring(1)));
    }
}
