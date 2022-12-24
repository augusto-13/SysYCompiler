package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRContext;
import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.IRTbl.IRTbl;
import FrontEnd.IRGenerator.Quadruple._15_T_Assign_Q;
import FrontEnd.IRGenerator.Quadruple._4_ArrGetVal_Q;
import FrontEnd.IRGenerator.Quadruple._5_ArrGetAddr_Q;
import FrontEnd.IRGenerator.Quadruple._6_Exp_Q;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.errorChecker.Context;
import FrontEnd.errorChecker.ErrorKind;
import FrontEnd.errorChecker.ErrorPair;

import java.util.ArrayList;

public class LValNode extends Node {
    public LValNode() {
    }

    @Override
    public void checkError() {
        LeafNode identNode = (LeafNode) getChildren().get(0);
        String name = identNode.getContent();
        if (Context.checkUndefinedError(name)) {
            Context.addError(new ErrorPair(ErrorKind.UNDEFINED_IDENT, identNode.getFinishLine()));
            return;
        }
        if (Context.lValInStmt && Context.checkModifyConstError(name)) {
            Context.addError(new ErrorPair(ErrorKind.MODIFY_CONST, identNode.getFinishLine()));
        }
        for (Node child : getChildren()) {
            child.checkError();
        }
    }

    @Override
    public Var genIR() {
        // LVal → Ident {'[' Exp ']'}
        boolean lVal_right = IRContext.lVal_right;
        ArrayList<Node> children = getChildren();
        int size = children.size();
        // LVal → Ident
        if (size == 1) {
            Var ret = IRTbl.findVar(((LeafNode) children.get(0)).getContent());
            assert ret != null;
            if (ret.getDim() == 0) return ret;
            else {
                Var temp = IRGenerator.genNewTemp();
                IRCodes.addIRCode_ori(new _5_ArrGetAddr_Q(temp.getName(), ret.getName()));
                return temp;
            }
        }
        // LVal → Ident '[' Exp ']'
        if (size == 4) {
            Node expN = children.get(2);
            Var expV = expN.genIR();
            Var arrV = IRTbl.findVar(((LeafNode) children.get(0)).getContent());
            assert arrV != null;
            if (arrV.getDim() == 1) {
                // a[index_1]
                if (lVal_right) {
                    if (IRContext.global_decl) {
                        return IRGenerator.genConstTemp(arrV.getValidInitArr().get(expV.getValidInitVal()));
                    }
                    else if (expV.isConst()) {
                        if (arrV.isConst()) return IRGenerator.genConstTemp(arrV.getConst_value_arr().get(expV.getConst_value()));
                        Var temp = IRGenerator.genNewTemp();
                        IRCodes.addIRCode_ori(new _4_ArrGetVal_Q(temp.getName(), arrV.getName(), expV.getConst_value()));
                        return temp;
                    } else {
                        Var temp = IRGenerator.genNewTemp();
                        IRCodes.addIRCode_ori(new _4_ArrGetVal_Q(temp.getName(), arrV.getName(), expV.getName()));
                        return temp;
                    }
                } else {
                    return new Var("arr_val", arrV, 1, expV);
                }
            } else {
                // &a[index_1]
                // lVal_right === true
                int d2_len = arrV.getD2();
                Var temp = IRGenerator.genNewTemp();
                if (expV.isConst()) {
                    int index = expV.getConst_value() * d2_len;
                    IRCodes.addIRCode_ori(new _5_ArrGetAddr_Q(temp.getName(), arrV.getName(), index));
                } else {
                    Var d2_lenV = IRGenerator.genNewTemp();
                    IRCodes.addIRCode_ori(new _15_T_Assign_Q(d2_lenV.getName(), d2_len));
                    Var index = IRGenerator.genNewTemp();
                    IRCodes.addIRCode_ori(new _6_Exp_Q(index.getName(), expV.getName(), "*", d2_lenV.getName()));
                    IRCodes.addIRCode_ori(new _5_ArrGetAddr_Q(temp.getName(), arrV.getName(), index.getName()));
                }
                return temp;
            }
        }
        // LVal → Ident '[' Exp ']' '[' Exp ']'
        if (size == 7) {
            Var arrV = IRTbl.findVar(((LeafNode) children.get(0)).getContent());
            assert arrV != null;
            int d2_len = arrV.getD2();
            Var index1_expV = children.get(2).genIR();
            Var index2_expV = children.get(5).genIR();
            if (IRContext.global_decl) {
                return IRGenerator.genConstTemp(arrV.getValidInitArr().get(d2_len * index1_expV.getValidInitVal() + index2_expV.getValidInitVal()));
            }
            if (index1_expV.isConst() && index2_expV.isConst()) {
                int index = d2_len * index1_expV.getConst_value() + index2_expV.getConst_value();
                if (lVal_right) {
                    if (arrV.isConst()) return IRGenerator.genConstTemp(arrV.getConst_value_arr().get(index));
                    Var temp = IRGenerator.genNewTemp();
                    IRCodes.addIRCode_ori(new _4_ArrGetVal_Q(temp.getName(), arrV.getName(), index));
                    return temp;
                } else return new Var("arr_val", arrV, 2, IRGenerator.genConstTemp(index));
            } else if (index1_expV.isConst()) {
                Var _1 = IRGenerator.genConstTemp(d2_len * index1_expV.getConst_value());
                Var index = IRGenerator.genNewTemp();
                IRCodes.addIRCode_ori(new _6_Exp_Q(index.getName(), index2_expV.getName(), "+", _1.getConst_value()));
                if (lVal_right) {
                    Var temp = IRGenerator.genNewTemp();
                    IRCodes.addIRCode_ori(new _4_ArrGetVal_Q(temp.getName(), arrV.getName(), index.getName()));
                    return temp;
                } else return new Var("arr_val", arrV, 2, index);
            } else {
                Var _1 = IRGenerator.genNewTemp();
                Var d2_lenV = IRGenerator.genNewTemp();
                Var index = IRGenerator.genNewTemp();
                IRCodes.addIRCode_ori(new _15_T_Assign_Q(d2_lenV.getName(), d2_len));
                IRCodes.addIRCode_ori(new _6_Exp_Q(_1.getName(), index1_expV.getName(), "*", d2_lenV.getName()));
                if (index2_expV.isConst())
                    IRCodes.addIRCode_ori(new _6_Exp_Q(index.getName(), _1.getName(), "+", index2_expV.getConst_value()));
                else IRCodes.addIRCode_ori(new _6_Exp_Q(index.getName(), _1.getName(), "+", index2_expV.getName()));
                if (lVal_right) {
                    Var temp = IRGenerator.genNewTemp();
                    IRCodes.addIRCode_ori(new _4_ArrGetVal_Q(temp.getName(), arrV.getName(), index.getName()));
                    return temp;
                } else return new Var("arr_val", arrV, 2, index);
            }
        }
        return null;
    }
}
