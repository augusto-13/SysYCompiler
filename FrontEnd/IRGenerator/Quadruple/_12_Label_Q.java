package FrontEnd.IRGenerator.Quadruple;

public class _12_Label_Q extends IRCode {
    private String name;

    public _12_Label_Q(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + ":\n";
    }
}
