package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRContext;
import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.IRTbl.IRTbl;
import FrontEnd.IRGenerator.Quadruple._15_T_Assign_Q;
import FrontEnd.IRGenerator.Quadruple._16_T_FuncCallRet_Q;
import FrontEnd.IRGenerator.Quadruple._3_Assign_Q;
import FrontEnd.IRGenerator.Quadruple._6_Exp_Q;
import FrontEnd.IRGenerator.Quadruple._7_FuncCall_Q;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.errorChecker.Context;
import FrontEnd.errorChecker.ErrorKind;
import FrontEnd.errorChecker.ErrorPair;
import FrontEnd.lexer.SyntaxKind;

import java.util.ArrayList;

public class UnaryExpNode extends Node {
    public UnaryExpNode() {
    }

    @Override
    public void checkError() {
        if (getChildren().get(0) instanceof LeafNode) {
            LeafNode identNode = (LeafNode) getChildren().get(0);
            String name = identNode.getContent();
            if (Context.checkUndefinedError(name)) {
                Context.addError(new ErrorPair(ErrorKind.UNDEFINED_IDENT, identNode.getFinishLine()));
                return;
            }
            ArrayList<Integer> rArgs = new ArrayList<>();
            if (getChildren().size() > 2
                    && getChildren().get(2).getKind() == SyntaxKind.FUNC_R_PARAMS) {
                ArrayList<Node> cdr = getChildren().get(2).getChildren();
                for (int i = 0; i < cdr.size(); i += 2) {
                    int dim = 0;
                    Node expNode = cdr.get(i);
                    Node addExpNode = expNode.getChildren().get(0);
                    if (addExpNode.getChildren().size() == 1) {
                        Node mulExpNode = addExpNode.getChildren().get(0);
                        if (mulExpNode.getChildren().size() == 1) {
                            Node unaryNode = mulExpNode.getChildren().get(0);
                            if (unaryNode.getChildren().size() == 1) {
                                Node primaryNode = unaryNode.getChildren().get(0);
                                if (primaryNode.getChildren().get(0).getKind() == SyntaxKind.L_VAL) {
                                    Node lValNode = primaryNode.getChildren().get(0);
                                    int brackNum = (lValNode.getChildren().size() - 1) / 3;
                                    String ident = ((LeafNode) lValNode.getChildren().get(0)).getContent();
                                    int dimOfIdent = Context.getDimOfIdent(ident);
                                    dim = dimOfIdent - brackNum;
                                }
                            } else if (unaryNode.getChildren().size() > 2) {
                                String ident = ((LeafNode) unaryNode.getChildren().get(0)).getContent();
                                dim = Context.checkFuncReturnDim(ident);
                            }
                        }
                    }
                    rArgs.add(dim);
                }
            }
            Context.checkArgsError(name, rArgs, identNode.getFinishLine());
        } else {
            for (Node child : getChildren()) {
                child.checkError();
            }
        }
    }

    @Override
    public Var genIR() {
        // UnaryExp →  PrimaryExp
        //          |  Ident '(' [FuncRParams] ')'
        //          |  UnaryOp UnaryExp
        ArrayList<Node> children = getChildren();
        int size = children.size();
        if (size == 1) {
            // UnaryExp → PrimaryExp
            return children.get(0).genIR();
        }
        else if (size == 2) {
            // addIRCode
            // UnaryExp → UnaryOp UnaryExp
            String op = ((LeafNode) children.get(0).getChildren().get(0)).getContent();
            Var unaryV = children.get(1).genIR();
            if (op.equals("+")) return unaryV;
            else if (op.equals("-")) {
                if (IRContext.global_decl) {
                    return IRGenerator.genConstTemp(-unaryV.getValidInitVal());
                }
                else if (unaryV.isConst()) {
                    unaryV.negative();
                    return unaryV;
                }
                else {
                    Var res = IRGenerator.genNewTemp();
                    IRCodes.addIRCode_ori(new _6_Exp_Q(res.getName(), "$0", op, unaryV.getName()));
                    return res;
                }
            }
            else {
                if (unaryV.isConst()) return IRGenerator.genConstTemp(unaryV.getConst_value() == 0 ? 1 : 0);
                Var res = IRGenerator.genNewTemp();
                IRCodes.addIRCode_ori(new _6_Exp_Q(res.getName(), unaryV.getName(), "sltiu", 1));
                return res;
            }
        }
        else if (size >= 3) {
            // UnaryExp → Ident '(' [FuncRParams] ')'
            String ident = ((LeafNode)children.get(0)).getContent();
            ArrayList<String> args = new ArrayList<>();
            if (size == 4) {
                // UnaryExp → Ident '(' FuncRParams ')'
                // 假定传进来的都是变量名/常量
                // 因此需要对LVal作进一步处理
                // 在IRContext中设置lValInRight以判断LVal在左侧还是右侧
                // 对于递归结构：由`各节点`负责维护前后状态，即在处理前保留lValInRight原状态，并在处理后恢复
                Node frpN = children.get(2);
                for (int i = 0; i < frpN.getChildren().size(); i += 2) {
                    Var argV = frpN.getChildren().get(i).genIR();
                    String arg_str;
                    if (argV.isConst()) {
                        arg_str = IRGenerator.genNewTemp().getName();
                        IRCodes.addIRCode_ori(new _15_T_Assign_Q(arg_str, argV.getConst_value()));
                    } else {
                        arg_str = argV.getName();
                    }
                    args.add(arg_str);
                }
            }
            IRCodes.addIRCode_ori(new _7_FuncCall_Q("func_" + ident, args));
            // if ret_void
            if (IRTbl.findFunc(ident).retVoid()) return null;
            // if ret_int
            else {
                Var tempT = IRGenerator.genNewTemp();
                IRCodes.addIRCode_ori(new _16_T_FuncCallRet_Q(tempT.getName()));
                return tempT;
            }
        }
        return null;
    }
}
