package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRContext;
import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.Quadruple.Elements.LVal;
import FrontEnd.IRGenerator.Quadruple._3_Assign_Q;
import FrontEnd.IRGenerator.Quadruple._6_Exp_Q;
import FrontEnd.IRGenerator.IRTbl.syms.Sym;
import FrontEnd.IRGenerator.IRTbl.syms.Var;

import java.util.ArrayList;

public class AddExpNode extends Node {
    public AddExpNode() {
    }

    @Override
    public Sym genIR() {
        // AddExp → MulExp
        //        | AddExp ('+' | '−') MulExp
        ArrayList<Node> children = getChildren();
        int size = children.size();
        if (size == 1) return children.get(0).genIR();
        Node addN = children.get(0);
        Node mulN = children.get(2);
        Var addV = (Var) addN.genIR();
        Var mulV = (Var) mulN.genIR();
        String op = ((LeafNode) children.get(1)).getContent();
        // addIRCodes
        if (IRContext.global_decl) {
            if (op.equals("+")) return IRGenerator.genConstTemp(addV.getValidInitVal() + mulV.getValidInitVal());
            else return IRGenerator.genConstTemp(addV.getValidInitVal() - mulV.getValidInitVal());
        }
        if (addV.isConst() && mulV.isConst()) {
            // Cond-1. const +/- const
            // No IRCode to be added.
            if (op.equals("+")) {
                return IRGenerator.genConstTemp(addV.getConst_value() + mulV.getConst_value());
            } else {
                return IRGenerator.genConstTemp(addV.getConst_value() - mulV.getConst_value());
            }
        }
        Var resV = IRGenerator.genNewTemp();
        String res = resV.getName();
        String arg1 = addV.getName();
        String arg2 = mulV.getName();
        if (!addV.isConst() && mulV.isConst()) {
            // Cond-2. var +/- const
            IRCodes.addIRCode_ori(new _6_Exp_Q(res, arg1, op, mulV.getConst_value()));
        } else if (addV.isConst() && !mulV.isConst()) {
            // Cond-3. const +/- var
            if (op.equals("+")) {
                IRCodes.addIRCode_ori(new _6_Exp_Q(res, arg2, op, addV.getConst_value()));
            } else {
                arg1 = IRGenerator.genNewTemp().getName();
                IRCodes.addIRCode_ori(new _3_Assign_Q(new LVal(arg1), addV.getConst_value()));
                IRCodes.addIRCode_ori(new _6_Exp_Q(res, arg1, op, arg2));
            }

        } else {
            // Cond-4. var +/- var
            IRCodes.addIRCode_ori(new _6_Exp_Q(res, arg1, op, arg2));
        }
        return resV;
    }
}
