package FrontEnd.nodes;

import FrontEnd.errorChecker.Context;
import FrontEnd.errorChecker.ErrorKind;
import FrontEnd.errorChecker.ErrorPair;
import FrontEnd.lexer.SyntaxKind;

public class BlockNode extends Node {
    public BlockNode() {
    }

    @Override
    public void checkError() {
        if (!Context.funcBlock) {
            // 新增层数
            Context.newScope();
            for (Node child : getChildren()) {
                child.checkError();
            }
            Context.clearCurrScope();
        } else {
            // FuncDef中的Block, 不更新层数
            // 但是对于FuncDef_Block 中的 "Stmt --> Block"文法：
            // 则需要更新层数。
            // 解决方案：如下，先记录所处函数类型，遍历所有孩子节点后，再还原函数类型。
            boolean noError = false;
            Context.funcBlock = false;
            for (Node child : getChildren()) {
                child.checkError();
            }
            Context.funcBlock = true;
            if (Context.inFuncType == Context.INT) {
                Node lastBlockItem = getChildren().get(getChildren().size() - 2);
                if (!(lastBlockItem instanceof LeafNode) && lastBlockItem.getChildren().get(0) instanceof StmtNode) {
                    StmtNode stmtNode = (StmtNode) lastBlockItem.getChildren().get(0);
                    if (stmtNode.getChildren().get(0).getKind() == SyntaxKind.RETURN_TK
                            && stmtNode.getChildren().get(1).getKind() == SyntaxKind.EXP) {
                        noError = true;
                    }
                }
                if (!noError) Context.addError(new ErrorPair(ErrorKind.INT_FUNC_NO_RETURN, getFinishLine()));
            }
        }
    }
}
