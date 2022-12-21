package FrontEnd.IRGenerator.IRTbl;

import FrontEnd.IRGenerator.IRTbl.syms.Var;

import java.util.ArrayList;

public class Func_tbl extends Entry_tbl {
    String name;
    boolean retVoid;
    final ArrayList<Var> params = new ArrayList<>();

    public Func_tbl(String name, String type, ArrayList<Var> params) {
        this.name = name;
        this.retVoid = (type.equals("void"));
        this.params.addAll(params);
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean retVoid() {
        return retVoid;
    }

}