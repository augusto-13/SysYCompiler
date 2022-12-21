package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.IRTbl.syms.Sym;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.IRGenerator.Quadruple.Elements.LVal;
import FrontEnd.IRGenerator.Quadruple._3_Assign_Q;
import FrontEnd.IRGenerator.Quadruple._6_Exp_Q;

import java.util.ArrayList;

public class RelExpNode extends Node {
    public RelExpNode() {
    }

    @Override
    public Sym genIR() {
        ArrayList<Node> children = getChildren();
        int size = children.size();
        if (size == 1) {
            return children.get(0).genIR();
        } else if (size == 3) {
            Var relV = (Var) children.get(0).genIR();
            Var addV = (Var) children.get(2).genIR();
            String op = ((LeafNode) children.get(1)).getContent();
            if (relV.isConst() && addV.isConst()) {
                int sub = relV.getConst_value() - addV.getConst_value();
                if ((op.equals("<") && sub < 0) || (op.equals(">") && sub > 0) || (op.equals(">=") && sub >= 0) || (op.equals("<=") && sub <= 0)) {
                    return IRGenerator.genConstTemp(1);
                } else return IRGenerator.genConstTemp(0);
            }
            Var resV = IRGenerator.genNewTemp();
            String res = resV.getName();
            switch (op) {
                case "<":
                    if (addV.isConst()) {
                        IRCodes.addIRCode_ori(new _6_Exp_Q(res, relV.getName(), "slti", addV.getConst_value()));
                    } else {
                        String rel = relV.getName();
                        if (relV.isConst()) {
                            rel = IRGenerator.genNewTemp().getName();
                            IRCodes.addIRCode_ori(new _3_Assign_Q(new LVal(rel), relV.getConst_value()));
                        }
                        IRCodes.addIRCode_ori(new _6_Exp_Q(res, rel, "slt", addV.getName()));
                    }
                    break;
                case ">":
                    if (relV.isConst()) {
                        IRCodes.addIRCode_ori(new _6_Exp_Q(res, addV.getName(), "slti", relV.getConst_value()));
                    } else {
                        String add = addV.getName();
                        if (addV.isConst()) {
                            add = IRGenerator.genNewTemp().getName();
                            IRCodes.addIRCode_ori(new _3_Assign_Q(new LVal(add), addV.getConst_value()));
                        }
                        IRCodes.addIRCode_ori(new _6_Exp_Q(res, add, "slt", relV.getName()));
                    }
                    break;
                case ">=":
                    if (addV.isConst()) {
                        IRCodes.addIRCode_ori(new _6_Exp_Q(res, relV.getName(), "slti", addV.getConst_value()));
                    } else {
                        String rel = relV.getName();
                        if (relV.isConst()) {
                            rel = IRGenerator.genNewTemp().getName();
                            IRCodes.addIRCode_ori(new _3_Assign_Q(new LVal(rel), relV.getConst_value()));
                        }
                        IRCodes.addIRCode_ori(new _6_Exp_Q(res, rel, "slt", addV.getName()));
                    }
                    IRCodes.addIRCode_ori(new _6_Exp_Q(res, res, "sltiu", 1));
                    break;
                default:
                    if (relV.isConst()) {
                        IRCodes.addIRCode_ori(new _6_Exp_Q(res, addV.getName(), "slti", relV.getConst_value()));
                    } else {
                        String add = addV.getName();
                        if (addV.isConst()) {
                            add = IRGenerator.genNewTemp().getName();
                            IRCodes.addIRCode_ori(new _3_Assign_Q(new LVal(add), addV.getConst_value()));
                        }
                        IRCodes.addIRCode_ori(new _6_Exp_Q(res, add, "slt", relV.getName()));
                    }
                    IRCodes.addIRCode_ori(new _6_Exp_Q(res, res, "sltiu", 1));
                    break;
            }
            return resV;
        }
        return null;
    }
}
