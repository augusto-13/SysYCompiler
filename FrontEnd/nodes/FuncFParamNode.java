package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRTbl.IRTbl;
import FrontEnd.IRGenerator.IRTbl.Var_tbl;
import FrontEnd.IRGenerator.IRTbl.syms.Sym;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.errorChecker.Context;
import FrontEnd.errorChecker.ErrorKind;
import FrontEnd.errorChecker.ErrorPair;
import FrontEnd.errorChecker.SymbolInfo;

import java.util.ArrayList;

public class FuncFParamNode extends Node {
    public FuncFParamNode() {
    }

    @Override
    public void checkError() {
        ArrayList<Node> children = getChildren();
        int dim = (children.size() == 2) ? 0 :
                (children.size() == 4) ? 1 : 2;
        int type = (dim == 0) ? SymbolInfo.INT :
                (dim == 1) ? SymbolInfo.ARRAY_1D : SymbolInfo.ARRAY_2D;
        LeafNode identNode = (LeafNode) children.get(1);
        String name = identNode.getContent();

        // 1. checkError()
        if (Context.checkRedefineError(name)) {
            Context.addError(new ErrorPair
                    (ErrorKind.REDEFINE_IDENT, identNode.getFinishLine()));
            return;
        }
        Context.addToParamStack(dim);

        // 2. 更新符号表
        Context.addSymbol(name, type);

        // 3. 对孩子们调用checkError()
        for (Node child : children) {
            child.checkError();
        }
    }

    @Override
    public Sym genIR() {
        ArrayList<Node> children = getChildren();
        int size = children.size();
        String ident = ((LeafNode) children.get(1)).getContent();
        Var fp = null;
        if (size == 2) fp = new Var("param", ident, 0, 0);
        if (size == 4) fp = new Var("param", ident, 1, 0);
        if (size == 7) fp = new Var("param", ident, 2, ((Var)children.get(5).genIR()).getConst_value());
        if (fp == null) {
            System.out.println("Something's wrong with `FuncVarNode` in `nodes`.");
            return null;
        }
        IRTbl.addEntry(new Var_tbl(ident, fp));
        return fp;
    }
}
