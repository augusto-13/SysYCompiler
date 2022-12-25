package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;

import java.util.ArrayList;

public class _12_Label_Q extends IRCode {
    private final String name;

    public _12_Label_Q(String name) {
        this.name = name;
    }

    // OK!!!
    @Override
    public String toString() {
        return name + ":\n";
    }

    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        mips_text.add(new MIPSCode.Label(name));
    }
}
