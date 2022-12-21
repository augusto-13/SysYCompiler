package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRContext;
import FrontEnd.IRGenerator.IRTbl.IRTbl;
import FrontEnd.IRGenerator.IRTbl.Var_tbl;
import FrontEnd.IRGenerator.IRTbl.syms.Sym;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.errorChecker.Context;
import FrontEnd.errorChecker.ErrorKind;
import FrontEnd.errorChecker.ErrorPair;
import FrontEnd.errorChecker.SymbolInfo;

import java.util.ArrayList;

public class ConstDefNode extends Node {
    public ConstDefNode() {
    }

    @Override
    public void checkError() {
        ArrayList<Node> children = getChildren();
        String name = ((LeafNode) children.get(0)).getContent();
        int dim = (children.size() - 3) / 3;
        int type = (dim == 0) ? SymbolInfo.CONST_INT :
                (dim == 1) ? SymbolInfo.CONST_ARRAY_1D :
                        (dim == 2) ? SymbolInfo.CONST_ARRAY_2D : SymbolInfo.UNKNOWN;

        // 1. checkError()
        if (Context.checkRedefineError(name)) {
            Context.addError(new ErrorPair
                    (ErrorKind.REDEFINE_IDENT, children.get(0).getFinishLine()));
            return;
        }
        // 2. 更新符号表
        Context.addSymbol(name, type);
        // 3. 对孩子调用checkError()
        for (Node child : children) {
            child.checkError();
        }
    }

    @Override
    public Sym genIR() {
        // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
        ArrayList<Node> children = getChildren();
        int size = children.size();
        int dim = size / 3 - 1;
        LeafNode identNode = (LeafNode) children.get(0);
        String ident = identNode.getContent();
        ident = (IRContext.global_decl)? "@" + ident : "%" + ident;
        Var const_init_value = (Var) children.get(size - 1).genIR();
        assert const_init_value.isConst();
        Var constVar;
        if (dim == 0) constVar = new Var("const", ident, const_init_value.getConst_value());
        else {
            ArrayList<Integer> value_arr = const_init_value.getConst_value_arr();
            constVar = new Var("const", ident, dim, const_init_value.getD1(), const_init_value.getD2(), value_arr);
        }
        // 填表
        IRTbl.addEntry(new Var_tbl(identNode.getContent(), constVar));
        return null;
    }
}
