package FrontEnd.IRGenerator.Quadruple;

import FrontEnd.IRGenerator.Quadruple.Elements.Param;

import java.util.ArrayList;

public class _9_FuncDecl_Q extends IRCode {
    String name;
    boolean retVoid;
    ArrayList<Param> params = new ArrayList<>();

    public _9_FuncDecl_Q(String name, String type, ArrayList<Param> params) {
        this.name = name;
        this.retVoid = (type.equals("void"));
        this.params.addAll(params);
    }

    @Override
    public String toString() {
        // e.g.
        // int foo()
        // para int a
        // para int bd
        StringBuilder ret = new StringBuilder();
        ret.append(retVoid ? String.format("void %s()\n", name) : String.format("int %s()\n", name));
        for (Param param : params) {
            ret.append(String.format("para %s\n", param));
        }
        return ret.toString();
    }
}
