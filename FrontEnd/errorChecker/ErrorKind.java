package FrontEnd.errorChecker;


public enum ErrorKind {
    ILLEGAL_CHAR("a"),
    REDEFINE_IDENT("b"),
    UNDEFINED_IDENT("c"),
    UNMATCHED_PARAM_NUM("d"),
    UNMATCHED_PARAM_TYPE("e"),
    VOID_FUNC_RETURN_INT("f"),
    INT_FUNC_NO_RETURN("g"),
    MODIFY_CONST("h"),
    NO_SEMICN("i"),
    NO_RPARENT("j"),
    NO_RBRACK("k"),
    UNMATCHED_PRINTF("l"),
    ABUSE_BREAK_CONTINUE("m"),
    OTHER_ERROR("other");

    private final String type;

    ErrorKind(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }

}
