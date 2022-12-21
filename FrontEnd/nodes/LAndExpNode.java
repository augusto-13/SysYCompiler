package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRContext;
import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.IRGenerator.Quadruple._12_Label_Q;
import FrontEnd.IRGenerator.Quadruple._13_Jump_Q;

import java.util.ArrayList;

public class LAndExpNode extends Node {
    public LAndExpNode() {}

    @Override
    public Var genIR() {
        // LAndExp  →  EqExp  |
        //             LAndExp  '&&'  EqExp
        ArrayList<Node> children = getChildren();
        int size = children.size();
        boolean prev_jump_if = IRContext.jump_if;
        String prev_if_label = IRContext.if_label;
        if (size == 1) {
            // LAndExp  →  EqExp
            Var eqExpV = children.get(0).genIR();
            if (eqExpV.isConst()) {
                int val = eqExpV.getConst_value();
                if (IRContext.jump_if && val != 0) {
                    IRCodes.addIRCode_ori(new _13_Jump_Q("goto", IRContext.if_label));
                }
                else if (!IRContext.jump_if && val == 0){
                    IRCodes.addIRCode_ori(new _13_Jump_Q("goto", IRContext.else_label));
                }
            } else {
                String arg = eqExpV.getName();
                if (IRContext.jump_if) {
                    IRCodes.addIRCode_ori(new _13_Jump_Q("bnz", IRContext.if_label, arg));
                } else {
                    IRCodes.addIRCode_ori(new _13_Jump_Q("bez", IRContext.else_label, arg));
                }
            }
        }
        else if (size == 3) {
            // LAndExp  →  LAndExp  '&&' <1> EqExp
            String label1 = IRGenerator.genLabel();
            IRContext.jump_if = false;
            IRContext.if_label = label1;
            children.get(0).genIR();
            IRCodes.addIRCode_ori(new _12_Label_Q(label1));
            IRContext.if_label = prev_if_label;
            IRContext.jump_if = prev_jump_if;
            children.get(2).genIR();
        }
        return null;
    }
}
