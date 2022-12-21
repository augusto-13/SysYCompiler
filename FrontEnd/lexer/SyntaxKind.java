package FrontEnd.lexer;

import java.util.HashMap;
import java.util.HashSet;

public enum SyntaxKind {
    // 新添加语法类别，具体用途尚未知
    EOF("EOF"),
    LINE_COMMENT("LINE_COMMENT"),
    BLOCK_COMMENT("BLOCK_COMMENT"),
    ERROR("ERROR"),

    IDENT("IDENFR"),
    INT_CONST("INTCON"),
    STRING("STRCON"),

    MAIN_TK("MAINTK"),
    CONST_TK("CONSTTK"),
    INT_TK("INTTK"),
    BREAK_TK("BREAKTK"),
    CONTINUE_TK("CONTINUETK"),
    IF_TK("IFTK"),
    ELSE_TK("ELSETK"),
    WHILE_TK("WHILETK"),
    GETINT_TK("GETINTTK"),
    PRINTF_TK("PRINTFTK"),
    RETURN_TK("RETURNTK"),
    VOID_TK("VOIDTK"),

    NOT_LOGIC("NOT"),
    AND_LOGIC("AND"),
    OR_LOGIC("OR"),
    PLUS("PLUS"),
    MINUS("MINU"),
    MULT("MULT"),
    DIVIDE("DIV"),
    MOD("MOD"),

    LT("LSS"),
    LE("LEQ"),
    GT("GRE"),
    GE("GEQ"),
    EQ("EQL"),
    NE("NEQ"),

    ASSIGN("ASSIGN"),
    SEMICN("SEMICN"),
    COMMA("COMMA"),
    LPARENT("LPARENT"),
    RPARENT("RPARENT"),
    LBRACK("LBRACK"),
    RBRACK("RBRACK"),
    LBRACE("LBRACE"),
    RBRACE("RBRACE"),

    COMP_UNIT("<CompUnit>"),
    DECL("<Decl>"),
    MAIN_FUNC_DEF("<MainFuncDef>"),
    FUNC_DEF("<FuncDef>"),
    CONST_DECL("<ConstDecl>"),
    VAR_DECL("<VarDecl>"),
    BLOCK("<Block>"),
    FUNC_TYPE("<FuncType>"),
    FUNC_F_PARAMS("<FuncFParams>"),
    CONST_DEF("<ConstDef>"),
    VAR_DEF("<VarDef>"),
    BLOCK_ITEM("<BlockItem>"),
    FUNC_F_PARAM("<FuncFParam>"),
    CONST_EXP("<ConstExp>"),
    CONST_INIT_VAL("<ConstInitVal>"),
    INIT_VAL("<InitVal>"),
    STMT("<Stmt>"),
    ADD_EXP("<AddExp>"),
    EXP("<Exp>"),
    L_VAL("<LVal>"),
    COND("<Cond>"),
    MUL_EXP("<MulExp>"),
    UNARY_EXP("<UnaryExp>"),
    PRIMARY_EXP("<PrimaryExp>"),
    FUNC_R_PARAMS("<FuncRParams>"),
    L_OR_EXP("<LOrExp>"),
    L_AND_EXP("<LAndExp>"),
    EQ_EXP("<EqExp>"),
    REL_EXP("<RelExp>"),
    UNARY_OP("<UnaryOp>"),
    NUMBER("<Number>");

    private final String name;

    SyntaxKind(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    // HashMap's & HashSet's to be added!!!

    public static final HashMap<String, SyntaxKind> string2kind = new HashMap<String, SyntaxKind>() {
        {
            // 12 reserved keywords
            // 12 个保留关键字
            put("void", SyntaxKind.VOID_TK);
            put("main", SyntaxKind.MAIN_TK);
            put("const", SyntaxKind.CONST_TK);
            put("int", SyntaxKind.INT_TK);
            put("break", SyntaxKind.BREAK_TK);
            put("continue", SyntaxKind.CONTINUE_TK);
            put("if", SyntaxKind.IF_TK);
            put("else", SyntaxKind.ELSE_TK);
            put("while", SyntaxKind.WHILE_TK);
            put("getint", SyntaxKind.GETINT_TK);
            put("printf", SyntaxKind.PRINTF_TK);
            put("return", SyntaxKind.RETURN_TK);
        }
    };

    public static final HashSet<SyntaxKind> RelExpSymSet = new HashSet<SyntaxKind>() {
        {
            add(LT);
            add(GT);
            add(LE);
            add(GE);
        }
    };

    public static final HashSet<SyntaxKind> ExpFIRST = new HashSet<SyntaxKind>() {
        {
            add(IDENT);
            add(PLUS);
            add(MINUS);
            add(NOT_LOGIC);
            add(LPARENT);
            add(INT_CONST);
        }
    };
}
