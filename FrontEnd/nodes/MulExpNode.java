package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRContext;
import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.Quadruple.Elements.LVal;
import FrontEnd.IRGenerator.Quadruple._3_Assign_Q;
import FrontEnd.IRGenerator.Quadruple._6_Exp_Q;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.IRGenerator.IRTbl.syms.Var;

import java.util.ArrayList;

public class MulExpNode extends Node {
    public MulExpNode() {
    }

    @Override
    public Var genIR() {
        // MulExp → UnaryExp
        //        | MulExp ('*'  |  '/'  |  '% ') UnaryExp
        ArrayList<Node> children = getChildren();
        int size = children.size();
        if (size == 1) return children.get(0).genIR();
        Node mulN = children.get(0);
        Node unaryN = children.get(2);
        Var mulV = mulN.genIR();
        Var unaryV = unaryN.genIR();
        String op = ((LeafNode) children.get(1)).getContent();
        if (IRContext.global_decl) {
            if (op.equals("*")) return IRGenerator.genConstTemp(mulV.getValidInitVal() * unaryV.getValidInitVal());
            else if (op.equals("/")) return IRGenerator.genConstTemp(mulV.getValidInitVal() / unaryV.getValidInitVal());
            else return IRGenerator.genConstTemp(mulV.getValidInitVal() % unaryV.getValidInitVal());
        }
        // addIRCodes
        // Cond-1. const +/- const
        // No IRCode to be added.
        if (mulV.isConst() && unaryV.isConst()) {
            if (op.equals("*")) {
                return IRGenerator.genConstTemp(mulV.getConst_value() * unaryV.getConst_value());
            } else if (op.equals("/")) {
                return IRGenerator.genConstTemp(mulV.getConst_value() / unaryV.getConst_value());
            } else {
                return IRGenerator.genConstTemp(mulV.getConst_value() % unaryV.getConst_value());
            }
        }
        Var resV = IRGenerator.genNewTemp();
        String res = resV.getName();
        String arg1 = mulV.getName();
        String arg2 = unaryV.getName();
        if (!mulV.isConst() && unaryV.isConst()) {
            // Cond-2. var '*'/'/'/'%' const
            arg2 = IRGenerator.genNewTemp().getName();
            IRCodes.addIRCode_ori(new _3_Assign_Q(new LVal(arg2), unaryV.getConst_value()));
            IRCodes.addIRCode_ori(new _6_Exp_Q(res, arg1, op, arg2));
        } else if (mulV.isConst() && !unaryV.isConst()) {
            // Cond-3. const +/- var
            arg1 = IRGenerator.genNewTemp().getName();
            IRCodes.addIRCode_ori(new _3_Assign_Q(new LVal(arg1), mulV.getConst_value()));
            IRCodes.addIRCode_ori(new _6_Exp_Q(res, arg1, op, arg2));
        } else {
            // Cond-4. var +/- var
            IRCodes.addIRCode_ori(new _6_Exp_Q(res, arg1, op, arg2));
        }
        return resV;
    }
}