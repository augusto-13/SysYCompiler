package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.IRGenerator.IRTbl.syms.Var;

import java.util.ArrayList;

public class ConstExpNode extends Node{
    public ConstExpNode() {
    }

    @Override
    public Var genIR() {
        ArrayList<Node> children = getChildren();
        Var var = children.get(0).genIR();
        assert var.isConst();
        return var;
    }
}
