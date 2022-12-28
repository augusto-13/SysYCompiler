package FrontEnd.parser;

import FrontEnd.errorChecker.Context;
import FrontEnd.errorChecker.ErrorKind;
import FrontEnd.errorChecker.ErrorPair;
import FrontEnd.lexer.*;
import FrontEnd.nodes.*;

public class Parser {
    private final TokenSource tokens;
    private final ASTBuilder builder;

    public Parser(TokenSource tokenSrc) {
        tokens = tokenSrc;
        builder = new ASTBuilder();
    }

    public Node parse() {
        CompileUnit_1();
        return builder.root();
    }

    private SyntaxKind currKind() {
        return tokens.curr().getKind();
    }

    private void error() {
        System.out.println("Something unexpected happened within FrontEnd.parser!");
    }

    private void checkToken(SyntaxKind sk) {
        if (currKind() == sk) {
            terminalSymbol();
        } else {
            if (sk == SyntaxKind.SEMICN) {
                builder.terminalSymbol(sk, ";", tokens.nthToken(-1).getLine());
                Context.addError(new ErrorPair(ErrorKind.NO_SEMICN, tokens.nthToken(-1).getLine()));
            } else if (sk == SyntaxKind.RPARENT) {
                builder.terminalSymbol(sk, ")", tokens.nthToken(-1).getLine());
                Context.addError(new ErrorPair(ErrorKind.NO_RPARENT, tokens.nthToken(-1).getLine()));
            } else if (sk == SyntaxKind.RBRACK) {
                builder.terminalSymbol(sk, "]", tokens.nthToken(-1).getLine());
                Context.addError(new ErrorPair(ErrorKind.NO_RBRACK, tokens.nthToken(-1).getLine()));
            }
        }
    }

    private boolean stmtHasASSIGN() {
        if (currKind() != SyntaxKind.IDENT) return false;
        for (int offset = 1; ; offset++) {
            /* BUG here!!! */
            if (tokens.nthToken(offset).getKind() == SyntaxKind.ASSIGN) return true;
            if (tokens.nthToken(offset).getKind() == SyntaxKind.SEMICN) return false;
        }
    }

    private void terminalSymbol() {
        Token token = tokens.curr();
        builder.terminalSymbol(token.getKind(), token.getContent(), token.getLine());
        tokens.bump();
    }

    private void CompileUnit_1() {
        builder.startNode(SyntaxKind.COMP_UNIT);
        SyntaxKind kind = currKind();
        while (kind != SyntaxKind.EOF) {
            if (kind == SyntaxKind.CONST_TK) {
                Decl_2();
            } else if (kind == SyntaxKind.VOID_TK) {
                FuncDef_4();
            } else if (kind == SyntaxKind.INT_TK) {
                if (tokens.nthToken(1).getKind() == SyntaxKind.MAIN_TK) {
                    MainFuncDef_3();
                } else if (tokens.nthToken(2).getKind() == SyntaxKind.LPARENT) {
                    FuncDef_4();
                } else {
                    Decl_2();
                }
            } else {
                error();
            }
            kind = currKind();
        }
        builder.finishNode(new CompileUnitNode());
    }

    private void Decl_2() {
        builder.startNode(SyntaxKind.DECL);
        SyntaxKind kind = currKind();
        if (kind == SyntaxKind.CONST_TK) {
            ConstDecl_5();
        } else if (kind == SyntaxKind.INT_TK) {
            VarDecl_6();
        } else {
            error();
        }
        builder.finishNode(new DeclNode());
    }

    private void MainFuncDef_3() {
        builder.startNode(SyntaxKind.MAIN_FUNC_DEF);
        checkToken(SyntaxKind.INT_TK);
        checkToken(SyntaxKind.MAIN_TK);
        checkToken(SyntaxKind.LPARENT);
        checkToken(SyntaxKind.RPARENT);
        if (currKind() == SyntaxKind.LBRACE) {
            Block_7();
        } else {
            error();
        }
        builder.finishNode(new MainFuncDefNode());
    }

    private void FuncDef_4() {
        builder.startNode(SyntaxKind.FUNC_DEF);
        FuncType_8();
        checkToken(SyntaxKind.IDENT);
        checkToken(SyntaxKind.LPARENT);
        if (currKind() == SyntaxKind.INT_TK) {
            FuncFParams_9();
        }
        checkToken(SyntaxKind.RPARENT);
        Block_7();
        builder.finishNode(new FuncDefNode());
    }

    private void ConstDecl_5() {
        builder.startNode(SyntaxKind.CONST_DECL);
        checkToken(SyntaxKind.CONST_TK);
        checkToken(SyntaxKind.INT_TK);
        ConstDef_10();
        while (currKind() == SyntaxKind.COMMA) {
            checkToken(SyntaxKind.COMMA);
            ConstDef_10();
        }
        checkToken(SyntaxKind.SEMICN);
        builder.finishNode(new ConstDeclNode());
    }

    private void VarDecl_6() {
        builder.startNode(SyntaxKind.VAR_DECL);
        checkToken(SyntaxKind.INT_TK);
        VarDef_11();
        while (currKind() == SyntaxKind.COMMA) {
            checkToken(SyntaxKind.COMMA);
            VarDef_11();
        }
        checkToken(SyntaxKind.SEMICN);
        builder.finishNode(new VarDeclNode());
    }

    private void Block_7() {
        builder.startNode(SyntaxKind.BLOCK);
        checkToken(SyntaxKind.LBRACE);
        while (currKind() != SyntaxKind.RBRACE) {
            BlockItem_12();
        }
        checkToken(SyntaxKind.RBRACE);
        builder.finishNode(new BlockNode());
    }

    private void FuncType_8() {
        builder.startNode(SyntaxKind.FUNC_TYPE);
        if (currKind() == SyntaxKind.INT_TK || currKind() == SyntaxKind.VOID_TK) {
            terminalSymbol();
        } else {
            error();
        }
        builder.finishNode(new FuncTypeNode());
    }

    private void FuncFParams_9() {
        builder.startNode(SyntaxKind.FUNC_F_PARAMS);
        if (currKind() == SyntaxKind.INT_TK) {
            FuncFParam_13();
        }
        while (currKind() == SyntaxKind.COMMA) {
            checkToken(SyntaxKind.COMMA);
            FuncFParam_13();
        }
        builder.finishNode(new FuncFParamsNode());
    }

    private void ConstDef_10() {
        builder.startNode(SyntaxKind.CONST_DEF);
        checkToken(SyntaxKind.IDENT);
        while (currKind() == SyntaxKind.LBRACK) {
            checkToken(SyntaxKind.LBRACK);
            ConstExp_14();
            checkToken(SyntaxKind.RBRACK);
        }
        checkToken(SyntaxKind.ASSIGN);
        ConstInitVal_15();
        builder.finishNode(new ConstDefNode());
    }

    private void VarDef_11() {
        builder.startNode(SyntaxKind.VAR_DEF);
        checkToken(SyntaxKind.IDENT);
        while (currKind() == SyntaxKind.LBRACK) {
            checkToken(SyntaxKind.LBRACK);
            ConstExp_14();
            checkToken(SyntaxKind.RBRACK);
        }
        if (currKind() == SyntaxKind.ASSIGN) {
            checkToken(SyntaxKind.ASSIGN);
            if (currKind() == SyntaxKind.GETINT_TK) {
                checkToken(SyntaxKind.GETINT_TK);
                checkToken(SyntaxKind.LPARENT);
                checkToken(SyntaxKind.RPARENT);
                checkToken(SyntaxKind.SEMICN);
            }
            else {
                InitVal_16();
            }
        }
        builder.finishNode(new VarDefNode());
    }

    private void BlockItem_12() {
        builder.startNode(SyntaxKind.BLOCK_ITEM);
        SyntaxKind kind = currKind();
        if (kind == SyntaxKind.CONST_TK || kind == SyntaxKind.INT_TK) {
            Decl_2();
        } else {
            Stmt_17();
        }
        builder.finishNode(new BlockItemNode());
    }

    private void FuncFParam_13() {
        builder.startNode(SyntaxKind.FUNC_F_PARAM);
        checkToken(SyntaxKind.INT_TK);
        checkToken(SyntaxKind.IDENT);
        if (currKind() == SyntaxKind.LBRACK) {
            checkToken(SyntaxKind.LBRACK);
            checkToken(SyntaxKind.RBRACK);
            while (currKind() == SyntaxKind.LBRACK) {
                checkToken(SyntaxKind.LBRACK);
                ConstExp_14();
                checkToken(SyntaxKind.RBRACK);
            }
        }
        builder.finishNode(new FuncFParamNode());
    }

    private void ConstExp_14() {
        builder.startNode(SyntaxKind.CONST_EXP);
        AddExp_18();
        builder.finishNode(new ConstExpNode());
    }

    private void ConstInitVal_15() {
        builder.startNode(SyntaxKind.CONST_INIT_VAL);
        if (currKind() == SyntaxKind.LBRACE) {
            checkToken(SyntaxKind.LBRACE);
            if (currKind() != SyntaxKind.RBRACE) {
                ConstInitVal_15();
                while (currKind() == SyntaxKind.COMMA) {
                    checkToken(SyntaxKind.COMMA);
                    ConstInitVal_15();
                }
            }
            checkToken(SyntaxKind.RBRACE);
        } else {
            ConstExp_14();
        }
        builder.finishNode(new ConstInitValNode());
    }

    private void InitVal_16() {
        builder.startNode(SyntaxKind.INIT_VAL);
        if (currKind() == SyntaxKind.LBRACE) {
            checkToken(SyntaxKind.LBRACE);
            if (currKind() != SyntaxKind.RBRACE) {
                InitVal_16();
                while (currKind() == SyntaxKind.COMMA) {
                    checkToken(SyntaxKind.COMMA);
                    InitVal_16();
                }
            }
            checkToken(SyntaxKind.RBRACE);

        } else {
            Exp_19();
        }
        builder.finishNode(new InitValNode());
    }

    private void Stmt_17() {
        builder.startNode(SyntaxKind.STMT);
        switch (currKind()) {
            case PRINTF_TK:
                checkToken(SyntaxKind.PRINTF_TK);
                checkToken(SyntaxKind.LPARENT);
                checkToken(SyntaxKind.STRING);
                while (currKind() == SyntaxKind.COMMA) {
                    checkToken(SyntaxKind.COMMA);
                    Exp_19();
                }
                checkToken(SyntaxKind.RPARENT);
                checkToken(SyntaxKind.SEMICN);
                break;
            case SEMICN:
                checkToken(SyntaxKind.SEMICN);
                break;
            case RETURN_TK:
                checkToken(SyntaxKind.RETURN_TK);
                if (SyntaxKind.ExpFIRST.contains(currKind())) Exp_19();
                checkToken(SyntaxKind.SEMICN);
                break;
            case CONTINUE_TK:
                checkToken(SyntaxKind.CONTINUE_TK);
                checkToken(SyntaxKind.SEMICN);
                break;
            case BREAK_TK:
                checkToken(SyntaxKind.BREAK_TK);
                checkToken(SyntaxKind.SEMICN);
                break;
            case WHILE_TK:
                checkToken(SyntaxKind.WHILE_TK);
                checkToken(SyntaxKind.LPARENT);
                Cond_21();
                checkToken(SyntaxKind.RPARENT);
                Stmt_17();
                break;
            case IF_TK:
                checkToken(SyntaxKind.IF_TK);
                checkToken(SyntaxKind.LPARENT);
                Cond_21();
                checkToken(SyntaxKind.RPARENT);
                Stmt_17();
                if (currKind() == SyntaxKind.ELSE_TK) {
                    checkToken(SyntaxKind.ELSE_TK);
                    Stmt_17();
                }
                break;
            case LBRACE:
                Block_7();
                break;
            default:
                if (!stmtHasASSIGN()) {
                    Exp_19();
                    checkToken(SyntaxKind.SEMICN);
                } else {
                    LVal_20();
                    checkToken(SyntaxKind.ASSIGN);
                    if (currKind() == SyntaxKind.GETINT_TK) {
                        checkToken(SyntaxKind.GETINT_TK);
                        checkToken(SyntaxKind.LPARENT);
                        checkToken(SyntaxKind.RPARENT);
                        checkToken(SyntaxKind.SEMICN);
                    } else {
                        Exp_19();
                        checkToken(SyntaxKind.SEMICN);
                    }
                }
                break;
        }
        builder.finishNode(new StmtNode());
    }

    private void AddExp_18() {
        // 左递归，需要特殊处理
        // e.g. A ::= c|Ab
        //      A ::= c{b}
        // 方法：1. 在开始向children栈中推入节点前，使用checkPoint()记录当前children栈的状态 (point: 数量)
        //      2. 处理好c，并封装为A
        //      3. 每遇到一次递归情况，就先处理b，再将栈中point后的所有节点封装为新的A
        //      4. 以此类推
        // 示意图：
        int point = builder.checkPoint();
        builder.startNode(SyntaxKind.ADD_EXP);
        MulExp_22();
        builder.finishNode(new AddExpNode());
        while (currKind() == SyntaxKind.PLUS || currKind() == SyntaxKind.MINUS) {
            builder.startNodeAt(point, SyntaxKind.ADD_EXP);
            if (currKind() == SyntaxKind.PLUS) checkToken(SyntaxKind.PLUS);
            else checkToken(SyntaxKind.MINUS);
            MulExp_22();
            builder.finishNode(new AddExpNode());
        }
    }

    private void Exp_19() {
        builder.startNode(SyntaxKind.EXP);
        AddExp_18();
        builder.finishNode(new ExpNode());
    }

    private void LVal_20() {
        builder.startNode(SyntaxKind.L_VAL);
        checkToken(SyntaxKind.IDENT);
        while (currKind() == SyntaxKind.LBRACK) {
            checkToken(SyntaxKind.LBRACK);
            Exp_19();
            checkToken(SyntaxKind.RBRACK);
        }
        builder.finishNode(new LValNode());
    }

    private void Cond_21() {
        builder.startNode(SyntaxKind.COND);
        LOrExp_26();
        builder.finishNode(new CondNode());
    }

    private void MulExp_22() {
        int point = builder.checkPoint();
        builder.startNode(SyntaxKind.MUL_EXP);
        UnaryExp_23();
        builder.finishNode(new MulExpNode());
        while (currKind() == SyntaxKind.MULT || currKind() == SyntaxKind.DIVIDE || currKind() == SyntaxKind.MOD || currKind() == SyntaxKind.BIT_AND) {
            builder.startNodeAt(point, SyntaxKind.MUL_EXP);
            checkToken(currKind());
            UnaryExp_23();
            builder.finishNode(new MulExpNode());
        }
    }

    private void UnaryExp_23() {
        builder.startNode(SyntaxKind.UNARY_EXP);
        if (currKind() == SyntaxKind.PLUS || currKind() == SyntaxKind.MINUS || currKind() == SyntaxKind.NOT_LOGIC) {
            UnaryOp_30();
            UnaryExp_23();
        } else if (currKind() == SyntaxKind.IDENT && tokens.nthToken(1).getKind() == SyntaxKind.LPARENT) {
            checkToken(SyntaxKind.IDENT);
            checkToken(SyntaxKind.LPARENT);
            if (SyntaxKind.ExpFIRST.contains(currKind())) FuncRParams_25();
            checkToken(SyntaxKind.RPARENT);
        } else {
            PrimaryExp_24();
        }
        builder.finishNode(new UnaryExpNode());
    }

    private void PrimaryExp_24() {
        builder.startNode(SyntaxKind.PRIMARY_EXP);
        if (currKind() == SyntaxKind.LPARENT) {
            checkToken(SyntaxKind.LPARENT);
            Exp_19();
            checkToken(SyntaxKind.RPARENT);
        } else if (currKind() == SyntaxKind.INT_CONST) {
            Number_31();
        } else if (currKind() == SyntaxKind.IDENT) {
            LVal_20();
        } else error();
        builder.finishNode(new PrimaryExpNode());
    }

    private void FuncRParams_25() {
        builder.startNode(SyntaxKind.FUNC_R_PARAMS);
        Exp_19();
        while (currKind() == SyntaxKind.COMMA) {
            checkToken(SyntaxKind.COMMA);
            Exp_19();
        }
        builder.finishNode(new FuncRParamsNode());
    }

    private void LOrExp_26() {
        int point = builder.checkPoint();
        builder.startNode(SyntaxKind.L_OR_EXP);
        LAndExp_27();
        builder.finishNode(new LOrExpNode());
        while (currKind() == SyntaxKind.OR_LOGIC) {
            builder.startNodeAt(point, SyntaxKind.L_OR_EXP);
            checkToken(SyntaxKind.OR_LOGIC);
            LAndExp_27();
            builder.finishNode(new LOrExpNode());
        }
    }

    private void LAndExp_27() {
        int point = builder.checkPoint();
        builder.startNode(SyntaxKind.L_AND_EXP);
        EqExp_28();
        builder.finishNode(new LAndExpNode());
        while (currKind() == SyntaxKind.AND_LOGIC) {
            builder.startNodeAt(point, SyntaxKind.L_AND_EXP);
            checkToken(SyntaxKind.AND_LOGIC);
            EqExp_28();
            builder.finishNode(new LAndExpNode());
        }
    }

    private void EqExp_28() {
        int point = builder.checkPoint();
        builder.startNode(SyntaxKind.EQ_EXP);
        RelExp_29();
        builder.finishNode(new EqExpNode());
        while (currKind() == SyntaxKind.EQ || currKind() == SyntaxKind.NE) {
            builder.startNodeAt(point, SyntaxKind.EQ_EXP);
            if (currKind() == SyntaxKind.EQ) checkToken(SyntaxKind.EQ);
            else checkToken(SyntaxKind.NE);
            RelExp_29();
            builder.finishNode(new EqExpNode());
        }
    }

    private void RelExp_29() {
        int point = builder.checkPoint();
        builder.startNode(SyntaxKind.REL_EXP);
        AddExp_18();
        builder.finishNode(new RelExpNode());
        while (SyntaxKind.RelExpSymSet.contains(currKind())) {
            builder.startNodeAt(point, SyntaxKind.REL_EXP);
            if (currKind() == SyntaxKind.LT) checkToken(SyntaxKind.LT);
            else if (currKind() == SyntaxKind.GT) checkToken(SyntaxKind.GT);
            else if (currKind() == SyntaxKind.LE) checkToken(SyntaxKind.LE);
            else checkToken(SyntaxKind.GE);
            AddExp_18();
            builder.finishNode(new RelExpNode());
        }
    }

    private void UnaryOp_30() {
        builder.startNode(SyntaxKind.UNARY_OP);
        if (currKind() == SyntaxKind.PLUS) {
            checkToken(SyntaxKind.PLUS);
        } else if (currKind() == SyntaxKind.MINUS) {
            checkToken(SyntaxKind.MINUS);
        } else if (currKind() == SyntaxKind.NOT_LOGIC) {
            checkToken(SyntaxKind.NOT_LOGIC);
        } else error();
        builder.finishNode(new UnaryOpNode());
    }

    private void Number_31() {
        builder.startNode(SyntaxKind.NUMBER);
        checkToken(SyntaxKind.INT_CONST);
        builder.finishNode(new NumberNode());
    }

}
