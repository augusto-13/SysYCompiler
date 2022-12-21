package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRTbl.syms.Sym;
import FrontEnd.IRGenerator.IRTbl.syms.Var;

import java.util.ArrayList;

public class ConstExpNode extends Node{
    public ConstExpNode() {
    }

    @Override
    public Sym genIR() {
        ArrayList<Node> children = getChildren();
        Var var = (Var) children.get(0).genIR();
        assert var.isConst();
        return var;
    }
}
