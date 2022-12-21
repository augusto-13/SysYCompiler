package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRContext;
import FrontEnd.IRGenerator.IRTbl.syms.Sym;

import java.util.ArrayList;

public class PrimaryExpNode extends Node {
    public PrimaryExpNode() {
    }

    @Override
    public Sym genIR() {
        // PrimaryExp  → '(' Exp ')'
        //             | LVal
        //             | Number
        ArrayList<Node> children = getChildren();
        int size = children.size();
        if (size == 3) {
            // PrimaryExp → '(' Exp ')'
            return children.get(1).genIR();
        } else {
            // PrimaryExp →  Number | LVal
            boolean pre_lVal_right = IRContext.lVal_right;
            IRContext.lVal_right = true;
            Sym ret = children.get(0).genIR();
            IRContext.lVal_right = pre_lVal_right;
            return ret;
        }
    }
}
