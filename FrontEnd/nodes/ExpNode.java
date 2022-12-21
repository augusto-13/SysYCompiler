package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.IRGenerator.IRTbl.syms.Var;

import java.util.ArrayList;

public class ExpNode extends Node {
    public ExpNode() {
    }

    @Override
    public Var genIR() {
        ArrayList<Node> children = getChildren();
        return children.get(0).genIR();
    }
}
