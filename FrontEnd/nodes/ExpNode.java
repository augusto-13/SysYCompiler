package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRTbl.syms.Sym;
import FrontEnd.IRGenerator.IRTbl.syms.Var;

import java.util.ArrayList;

public class ExpNode extends Node {
    public ExpNode() {
    }

    @Override
    public Sym genIR() {
        ArrayList<Node> children = getChildren();
        Var var = (Var) children.get(0).genIR();
        return var;
    }
}
