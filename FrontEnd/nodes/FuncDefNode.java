package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRContext;
import FrontEnd.IRGenerator.IRTbl.Func_tbl;
import FrontEnd.IRGenerator.IRTbl.IRTbl;
import FrontEnd.IRGenerator.Quadruple.Elements.Param;
import FrontEnd.IRGenerator.Quadruple._9_FuncDecl_Q;
import FrontEnd.IRGenerator.Quadruple._12_Label_Q;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.errorChecker.Context;
import FrontEnd.errorChecker.ErrorKind;
import FrontEnd.errorChecker.ErrorPair;
import FrontEnd.errorChecker.SymbolInfo;
import FrontEnd.lexer.SyntaxKind;

import java.util.ArrayList;


public class FuncDefNode extends Node {
    public FuncDefNode() {
    }

    @Override
    public void checkError() {
        ArrayList<Node> children = getChildren();
        LeafNode identNode = (LeafNode) children.get(1);
        LeafNode IntVoidNode = (LeafNode) children.get(0).getChildren().get(0);
        int type = (IntVoidNode.getKind() == SyntaxKind.INT_TK) ? SymbolInfo.INT_FUNC : SymbolInfo.VOID_FUNC;
        String name = identNode.getContent();
        // 1. checkError()
        if (Context.checkRedefineError(name)) {
            Context.addError(new ErrorPair
                    (ErrorKind.REDEFINE_IDENT, identNode.getFinishLine()));
            return;
        }

        // 2. Renew: funcBlock
        Context.inFuncType = (type == SymbolInfo.INT_FUNC) ? Context.INT : Context.VOID;
        Context.funcBlock = true;

        // 3. 更新维护 *符号表* & *map*
        // Maintain symT, ident2levels
        // 需要注意：先添加函数标识符到原作用域，再扩充作用域。
        ArrayList<Integer> argTypes = new ArrayList<>();
        Context.addSymbol_func(name, type, argTypes);
        Context.newScope();

        // BUG: 此处递归调用会出错
        // Solution: 分析完参数后立即将参数信息加入符号表, 然后再对Block使用checkError()

        // 4. 对孩子们调用checkError()函数
        // children.for.checkError()
        for (int i = 0; i < children.size() - 1; i++) {
            Node child = children.get(i);
            child.checkError();
        }
        Context.pourStack(argTypes);

        children.get(children.size() - 1).checkError();

        // 5. 更新 符号表 & 当前层数 & 所在函数类型
        // Out Of FuncDefNode
        Context.clearCurrScope();
        Context.funcBlock = false;
    }

    @Override
    public Var genIR() {
        //
        // FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
        //
        // 1. 将ident填入global_frame
        // === [一定格外注意：是填入globalFrame]
        // 2. 生成FuncDecl_Q_IRCode
        // === 2-1. param采用Var结构封装，函数不需要
        // === 2-2. 将Func_Decl_Q传递给Func_tbl构造函数做参数，多少有点不入流，但也可以接受
        // =======================================================================
        // 3. IRContext中`in_func_def_block` 信号 -> true
        // 4. generate开始标签
        // 5. 外部新建func_block_frame
        // 6. 对Block调用genIR（无需返回综合属性值）
        // 7. generate结束标签
        // 8. IRContext中`in_func_def_block`信号 -> false
        ArrayList<Node> children = getChildren();
        int size = children.size();
        ArrayList<Var> paramVs = new ArrayList<>();
        ArrayList<Param> params = new ArrayList<>();
        String funcType = ((LeafNode)children.get(0).getChildren().get(0)).getContent();
        String funcName = ((LeafNode)children.get(1)).getContent();
        if (size == 6) {
            // FuncDef → FuncType Ident '(' FuncFParams ')' Block
            paramVs = ((FuncFParamsNode) children.get(3)).genIRs();
        }
        for (Var paramV : paramVs) {
            if (paramV.getDim() == 0) params.add(new Param(paramV.getName()));
            else params.add(new Param(paramV.getName(), paramV.getD2()));
        }
        IRContext.in_func = true;
        IRCodes.init_irCodes_curr_func();
        IRCodes.addIRCode_ori(new _9_FuncDecl_Q(funcName, funcType, params));
        IRTbl.addEntryToGlobalFrame(new Func_tbl(funcName, funcType, paramVs));
        IRTbl.newFrame(IRTbl.FRAME_FUNC_DEF_BLOCK, funcName);
        children.get(children.size() - 1).genIR();
        IRTbl.removeCurrFrame();
        IRContext.in_func = false;
        IRCodes.merge_curr_func_into_irCodes_func();
        return null;
    }
}
