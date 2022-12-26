package FrontEnd.IRGenerator.Quadruple;


import BackEnd.MIPSTbl;

public class _14_StrDecl_Q extends IRCode {

    String name;
    String content;

    public _14_StrDecl_Q(String name, String content) {
        this.name = name;
        this.content = content;
    }

    // OK!!!
    @Override
    public String toString() {
        return String.format("const str %s = \"%s\"\n", name, content);
    }

    @Override
    public void toData(StringBuilder mips_data) {
        mips_data.append(String.format("%s: .asciiz \"%s\"\n", name, content));
        MIPSTbl.global_address += (toLength() + 1);
    }

    private int toLength() {
        int l = 0;
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '\\') i++;
            l++;
        }
        return l;
    }
}

