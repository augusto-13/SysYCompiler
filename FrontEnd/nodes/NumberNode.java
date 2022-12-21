package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.IRTbl.syms.Var;

public class NumberNode extends Node {
    public NumberNode() {}

    @Override
    public Var genIR() {
        int value = Integer.parseInt(((LeafNode) getChildren().get(0)).getContent());
        return IRGenerator.genConstTemp(value);
    }

}
