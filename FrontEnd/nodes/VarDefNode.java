package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRContext;
import FrontEnd.IRGenerator.IRTbl.IRTbl;
import FrontEnd.IRGenerator.IRTbl.Var_tbl;
import FrontEnd.IRGenerator.Quadruple.Elements.LVal;
import FrontEnd.IRGenerator.Quadruple._1_VarDecl_Q;
import FrontEnd.IRGenerator.Quadruple._2_ArrDecl_Q;
import FrontEnd.IRGenerator.Quadruple._3_Assign_Q;
import FrontEnd.IRGenerator.IRTbl.syms.Sym;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.errorChecker.Context;
import FrontEnd.errorChecker.ErrorKind;
import FrontEnd.errorChecker.ErrorPair;
import FrontEnd.errorChecker.SymbolInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class VarDefNode extends Node {
    public VarDefNode() {
    }

    @Override
    public void checkError() {
        ArrayList<Node> children = getChildren();
        String name = ((LeafNode) children.get(0)).getContent();
        int dim = (children.size() % 3 == 0) ? ((children.size() - 3) / 3) : ((children.size() - 1) / 3);
        int type  =  (dim == 0) ? SymbolInfo.INT :
                     (dim == 1) ? SymbolInfo.ARRAY_1D :
                     (dim == 2) ? SymbolInfo.ARRAY_2D : SymbolInfo.UNKNOWN;
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
        ArrayList<Node> children = getChildren();
        int size = children.size();
        String ident = ((LeafNode) children.get(0)).getContent();
        // 这里很重要，不同层同名变量要进行重命名
        String ident_ = (IRContext.global_decl) ? "@" + ident : (IRContext.level == 0) ? "%" + ident : "%" + ident + "_" + IRContext.level;
        Var var = null;
        if (size == 1) {
            // VarDef -> Ident
            var = new Var("var", ident_);
            if (IRContext.global_decl) {
                IRCodes.addIRCode_ori(new _1_VarDecl_Q(ident_, 0));
            } else {
                IRCodes.addIRCode_ori(new _1_VarDecl_Q(ident_));
            }

        }
        if (size == 3) {
            // VarDef -> Ident '=' InitVal
            Var initValV = (Var) children.get(2).genIR();
            var = new Var("var", ident_, initValV);
            if (IRContext.global_decl) {
                IRCodes.addIRCode_ori(new _1_VarDecl_Q(ident_, initValV.getConst_value()));
            } else {
                IRCodes.addIRCode_ori(new _1_VarDecl_Q(ident_));
                if (initValV.isConst()) {
                    IRCodes.addIRCode_ori(new _3_Assign_Q(new LVal(ident_), initValV.getConst_value()));
                } else {
                    IRCodes.addIRCode_ori(new _3_Assign_Q(new LVal(ident_), initValV.getName()));
                }
            }
        }
        if (size == 4) {
            // VarDef -> Ident '[' ConstExp ']'
            int d1_lenV = ((Var) children.get(2).genIR()).getConst_value();
            var = new Var("var", ident_, 1, d1_lenV, 0);
            ArrayList<Integer> initArr = new ArrayList<>();
            for (int i = 0; i < d1_lenV; i++) initArr.add(0);
            if (IRContext.global_decl) {
                IRCodes.addIRCode_ori(new _2_ArrDecl_Q(ident_, d1_lenV, initArr));
            } else {
                IRCodes.addIRCode_ori(new _2_ArrDecl_Q(ident_, d1_lenV));
            }

        }
        if (size == 6) {
            // VarDef -> Ident '[' ConstExp ']' '=' InitVal
            int d1_lenV = ((Var) children.get(2).genIR()).getConst_value();
            ArrayList<Var> value_arr = ((Var) children.get(5).genIR()).getInit_value_arr();
            var = new Var("var", ident_, 1, d1_lenV, 0, value_arr);
            if (IRContext.global_decl) {
                
            }
            else {
                IRCodes.addIRCode_ori(new _2_ArrDecl_Q(ident_, d1_lenV));
                for (int i = 0; i < d1_lenV; i++) {
                    if (value_arr.get(i).isConst()) {
                        IRCodes.addIRCode_ori(new _3_Assign_Q(new LVal(ident_, i), value_arr.get(i).getConst_value()));
                    } else {
                        IRCodes.addIRCode_ori(new _3_Assign_Q(new LVal(ident_, i), value_arr.get(i).getName()));
                    }
                }
            }

        }
        if (size == 7) {
            // VarDef -> Ident '[' ConstExp ']' '[' 'ConstExp' ']'
            int d1_lenV = ((Var) children.get(2).genIR()).getConst_value();
            int d2_lenV = ((Var) children.get(5).genIR()).getConst_value();
            var = new Var("var", ident_, 2, d1_lenV, d2_lenV);
            IRCodes.addIRCode_ori(new _2_ArrDecl_Q(ident_, d1_lenV * d2_lenV));
        }
        if (size == 9) {
            // VarDef -> Ident '[' ConstExp ']' '[' 'ConstExp' ']' '=' InitVal
            int d1_lenV = ((Var) children.get(2).genIR()).getConst_value();
            int d2_lenV = ((Var) children.get(5).genIR()).getConst_value();
            ArrayList<Var> value_arr = ((Var) children.get(8).genIR()).getInit_value_arr();
            var = new Var("var", ident_, 2, d1_lenV, d2_lenV, value_arr);
            IRCodes.addIRCode_ori(new _2_ArrDecl_Q(ident_, d1_lenV * d2_lenV));
            for (int i = 0; i < d1_lenV * d2_lenV; i++) {
                if (value_arr.get(i).isConst()) {
                    IRCodes.addIRCode_ori(new _3_Assign_Q(new LVal(ident_, i), value_arr.get(i).getConst_value()));
                } else {
                    IRCodes.addIRCode_ori(new _3_Assign_Q(new LVal(ident_, i), value_arr.get(i).getName()));
                }
            }
        }

        if (var == null) {
            System.out.println("The \"[genIR()]\" method within [VarDefNode] Node is wrong.");
            return null;
        }

        IRTbl.addEntry(new Var_tbl(ident, var));
        return null;
    }
}
