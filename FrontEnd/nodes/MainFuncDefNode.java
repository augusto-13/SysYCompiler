package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRTbl.IRTbl;
import FrontEnd.IRGenerator.Quadruple._12_Label_Q;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.errorChecker.Context;

public class MainFuncDefNode extends Node {
    public MainFuncDefNode() {}

    @Override
    public void checkError() {
        Context.inFuncType = Context.INT;
        Context.funcBlock = true;
        Context.newScope();
        for (Node child : getChildren()) {
            child.checkError();
        }
        Context.clearCurrScope();
        Context.inFuncType = Context.NOT;
        Context.funcBlock = false;
    }

    @Override
    public Var genIR() {
        // MainFuncDef →  'int'  'main'  '('  ')' Block
        IRTbl.newFrame(IRTbl.FRAME_MAIN, "main");
        IRCodes.addIRCode_ori(new _12_Label_Q("main_func_begin"));
        getChildren().get(4).genIR();
        IRCodes.addIRCode_ori(new _12_Label_Q("main_func_end"));
        return null;
    }
}
