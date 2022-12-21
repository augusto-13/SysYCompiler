package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRContext;
import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.IRTbl.IRTbl;
import FrontEnd.IRGenerator.Quadruple.Elements.LVal;
import FrontEnd.IRGenerator.Quadruple._12_Label_Q;
import FrontEnd.IRGenerator.Quadruple._13_Jump_Q;
import FrontEnd.IRGenerator.Quadruple._3_Assign_Q;
import FrontEnd.IRGenerator.Quadruple._8_FuncRet_Q;
import FrontEnd.IRGenerator.Quadruple._10_In_Q;
import FrontEnd.IRGenerator.Quadruple._11_Out_Q;
import FrontEnd.IRGenerator.IRTbl.syms.Sym;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.errorChecker.Context;
import FrontEnd.errorChecker.ErrorKind;
import FrontEnd.errorChecker.ErrorPair;
import FrontEnd.lexer.SyntaxKind;

import java.util.ArrayList;

public class StmtNode extends Node {
    public StmtNode() {}

    @Override
    public void checkError() {
        ArrayList<Node> children = getChildren();
        SyntaxKind firstKind = children.get(0).getKind();
        if (firstKind == SyntaxKind.RETURN_TK) {
            LeafNode returnNode = (LeafNode) children.get(0);
            if (Context.inFuncType == Context.VOID
                    && children.get(1).getKind() != SyntaxKind.SEMICN) {
                Context.addError(new ErrorPair(ErrorKind.VOID_FUNC_RETURN_INT, returnNode.getFinishLine()));
            }
        } else if (firstKind == SyntaxKind.PRINTF_TK) {
            int printfPDNum = 0;
            LeafNode stringNode = (LeafNode) children.get(2);
            String fs = stringNode.getContent();
            for (int i = 0; i < fs.length() - 1; i++) {
                if (fs.charAt(i) == '%' && fs.charAt(i + 1) == 'd') {
                    printfPDNum++;
                    i++;
                }
            }
            int printfArgNum = 0;
            for (int i = 3; i < children.size(); i += 2) {
                if (children.get(i).getKind() == SyntaxKind.COMMA) {
                    printfArgNum++;
                } else break;
            }
            if (printfArgNum != printfPDNum) {
                Context.addError(new ErrorPair(ErrorKind.UNMATCHED_PRINTF, children.get(0).getFinishLine()));
            }
        } else if (firstKind == SyntaxKind.WHILE_TK) {
            Context.inLoopNum++;
            for (Node child : children) {
                child.checkError();
            }
            Context.inLoopNum--;
            return;
        } else if (firstKind == SyntaxKind.BREAK_TK || firstKind == SyntaxKind.CONTINUE_TK) {
            if (Context.inLoopNum <= 0) {
                Context.addError(new ErrorPair(ErrorKind.ABUSE_BREAK_CONTINUE, children.get(0).getFinishLine()));
            }
        } else if (firstKind == SyntaxKind.L_VAL) {
            Context.lValInStmt = true;
            children.get(0).checkError();
            Context.lValInStmt = false;
            for (int i = 1; i < children.size(); i++) {
                children.get(i).checkError();
            }
            return;
        }

        for (Node child : children) {
            child.checkError();
        }
    }

    @Override
    public Sym genIR() {
        ArrayList<Node> children = getChildren();
        Node first = children.get(0);
        int size = children.size();
        if (first instanceof LValNode) {
            if (size == 4) {
                // Stmt → LVal '=' Exp ';'
                boolean prev_lVal_right = IRContext.lVal_right;
                IRContext.lVal_right = false;
                Var lVal_v = (Var) first.genIR();
                IRContext.lVal_right = prev_lVal_right;
                Var exp_v = (Var) children.get(2).genIR();
                LVal lVal = lVal_v.toLVal();
                if (exp_v.isConst()) {
                    IRCodes.addIRCode_ori(new _3_Assign_Q(lVal, exp_v.getConst_value()));
                } else {
                    IRCodes.addIRCode_ori(new _3_Assign_Q(lVal, exp_v.getName()));
                }
            }
            else if (size == 6) {
                // Stmt → LVal '=' 'getint' '(' ')' ';'
                boolean prev_lVal_right = IRContext.lVal_right;
                IRContext.lVal_right = false;
                Var lVal_v = (Var) first.genIR();
                IRContext.lVal_right = prev_lVal_right;
                IRCodes.addIRCode_ori(new _10_In_Q(lVal_v.toLVal()));
            }
        }
        else if (first instanceof ExpNode) {
            // Stmt -> Exp ';'
            first.genIR();
        }
        else if (first instanceof BlockNode) {
            // Stmt -> Block
            IRTbl.newFrame(IRTbl.FRAME_BLOCK, "block" + (++IRGenerator.block_num));
            IRContext.level++;
            first.genIR();
            IRContext.level--;
            IRTbl.removeCurrFrame();
        }
        else if (first instanceof LeafNode) {
            String leaf_1st = ((LeafNode) first).getContent();
            if (leaf_1st.equals("if")) {
                // Stmt -> 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
                if (size == 5) {
                    // Stmt -> 'if' '(' Cond ')' Stmt
                    // Cond: 传入条件不成立时跳转至标签
                    String if_end_label = IRGenerator.genLabel();
                    IRContext.else_label = if_end_label;
                    children.get(2).genIR();
                    children.get(4).genIR();
                    IRCodes.addIRCode_ori(new _12_Label_Q(if_end_label));
                }
                else if (size == 7) {
                    // Stmt -> 'if' '(' Cond ')' Stmt 'else' Stmt
                    String else_label = IRGenerator.genLabel();
                    IRContext.else_label = else_label;
                    children.get(2).genIR();
                    children.get(4).genIR();
                    IRCodes.addIRCode_ori(new _12_Label_Q(else_label));
                    children.get(6).genIR();
                }
            }
            else if (leaf_1st.equals("while")) {
                // Stmt -> 'while' '(' Cond ')' Stmt
                String prev_bez_label = IRContext.else_label;
                String prev_break_label = IRContext.break_label;
                String prev_continue_label = IRContext.continue_label;

                String while_cond = IRGenerator.genLabel();
                String while_stmt_end = IRGenerator.genLabel();
                IRContext.else_label = while_stmt_end;
                IRContext.break_label = while_stmt_end;
                IRContext.continue_label = while_cond;
                IRCodes.addIRCode_ori(new _12_Label_Q(while_cond));
                children.get(2).genIR();
                children.get(4).genIR();
                IRCodes.addIRCode_ori(new _12_Label_Q(while_stmt_end));

                IRContext.else_label = prev_bez_label;
                IRContext.break_label = prev_break_label;
                IRContext.continue_label = prev_continue_label;
            }
            else if (leaf_1st.equals("break")) {
                // Stmt -> 'break' ';'
                IRCodes.addIRCode_ori(new _13_Jump_Q("goto", IRContext.break_label));
            }
            else if (leaf_1st.equals("continue")) {
                // Stmt -> 'continue'  ';'
                IRCodes.addIRCode_ori(new _13_Jump_Q("goto", IRContext.continue_label));
            }
            else if (leaf_1st.equals("return")) {
                // Stmt -> 'return'  [Exp]  ';'
                if (size == 2) {
                    // Stmt -> 'return' ';'
                    IRCodes.addIRCode_ori(new _8_FuncRet_Q());
                }
                else {
                    // Stmt -> 'return'  Exp  ';'
                    Var ret_val_v = (Var)children.get(1).genIR();
                    if (ret_val_v.isConst()) {
                        IRCodes.addIRCode_ori(new _8_FuncRet_Q(ret_val_v.getConst_value()));
                    } else {
                        IRCodes.addIRCode_ori(new _8_FuncRet_Q(ret_val_v.getName()));
                    }
                }
            }
            else if (leaf_1st.equals(";")) {
                // Stmt -> ';'
            }
            else if (leaf_1st.equals("printf")) {
                // Stmt -> 'printf' '(' FormatString {','Exp} ')' ';'
                String str = ((LeafNode) children.get(2)).getContent();
                str = str.substring(1, str.length() - 1);
                ArrayList<Var> args = new ArrayList<>();
                if (size > 5) {
                    for (int i = 4; i < size; i += 2) {
                        args.add((Var)children.get(i).genIR());
                    }
                }
                IRCodes.addIRCode_ori(new _11_Out_Q(str, args));
            }
        }
        return null;
    }
}
