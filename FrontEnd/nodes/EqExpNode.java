package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.IRGenerator.Quadruple._6_Exp_Q;

import java.util.ArrayList;

public class EqExpNode extends Node {
    public EqExpNode() {
    }

    @Override
    public Var genIR() {
        // EqExp  →  RelExp
        //        |  EqExp  ('==' | '!=')  RelExp
        ArrayList<Node> children = getChildren();
        int size = children.size();
        if (size == 1) {
            // EqExp  →  RelExp
            return children.get(0).genIR();
        }
        else if (size == 3) {
            // EqExp  →  EqExp  ('==' | '!=')  RelExp
            Var eqV = children.get(0).genIR();
            Var relV = children.get(2).genIR();
            String op = ((LeafNode) children.get(1)).getContent();
            if (eqV.isConst() && relV.isConst()) {
                int sub = eqV.getConst_value() - relV.getConst_value();
                if ((op.equals("==") && sub == 0) || (op.equals("!=") && sub != 0)) {
                    return IRGenerator.genConstTemp(1);
                } else return IRGenerator.genConstTemp(0);
            }
            Var subV = IRGenerator.genNewTemp();
            String sub = subV.getName();
            if (!eqV.isConst() && !relV.isConst()) {
                IRCodes.addIRCode_ori(new _6_Exp_Q(sub, eqV.getName(), "-", relV.getName()));
            } else if (eqV.isConst()) {
                IRCodes.addIRCode_ori(new _6_Exp_Q(sub, relV.getName(), "-", eqV.getConst_value()));
            } else {
                IRCodes.addIRCode_ori(new _6_Exp_Q(sub, eqV.getName(), "-", relV.getConst_value()));
            }
            if (op.equals("!=")) {
                /* TODO: Error ========== 0 ---> 0, others ---> 1*/
                IRCodes.addIRCode_ori(new _6_Exp_Q(sub, "$0", "sltu", sub));
            } else {
                IRCodes.addIRCode_ori(new _6_Exp_Q(sub, sub, "sltiu", 1));
            }
            return subV;
        }
        return null;
    }
}
