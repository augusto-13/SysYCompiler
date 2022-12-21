package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRTbl.syms.Var;

import java.util.ArrayList;

public class FuncFParamsNode extends Node {
    public FuncFParamsNode() {
    }

    public ArrayList<Var> genIRs() {
        // FuncFParams → FuncFParam {  ',' FuncFParam }
        ArrayList<Node> children = getChildren();
        ArrayList<Var> ret = new ArrayList<>();
        int size = children.size();
        for (int i = 0; i < size; i += 2) {
            ret.add((Var) children.get(i).genIR());
        }
        return ret;
    }
}
