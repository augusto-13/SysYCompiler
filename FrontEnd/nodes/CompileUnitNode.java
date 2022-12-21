package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRContext;
import FrontEnd.IRGenerator.IRTbl.IRTbl;
import FrontEnd.IRGenerator.Quadruple._12_Label_Q;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.errorChecker.Context;

import java.util.ArrayList;

public class CompileUnitNode extends Node {

    public CompileUnitNode() {
    }

    @Override
    public void checkError() {
        Context.init();
        for (Node child : getChildren()) {
            child.checkError();
        }
    }

    @Override
    public Var genIR() {
        IRTbl.newFrame(IRTbl.FRAME_GLOBAL, "global");
        boolean global_decl_a = false;
        boolean global_decl_o = false;
        ArrayList<Node> children = getChildren();
        for (Node child : children) {
            if (child instanceof DeclNode) {
                if (!global_decl_a) {
                    global_decl_a = true;
                    IRContext.global_decl = true;
                }
            } else {
                if (!global_decl_o) {
                    global_decl_o = true;
                    IRContext.global_decl = false;
                }
            }
            child.genIR();
        }
        return null;
    }


}
