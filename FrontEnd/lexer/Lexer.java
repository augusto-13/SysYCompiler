package FrontEnd.lexer;

import java.util.ArrayList;

public class Lexer {
    private FileSource src;
    private Cursor cursor;
    private ArrayList<LexerError> errors = new ArrayList<>();
    private ArrayList<Token> tokens = new ArrayList<>();

    private boolean isIdentFirst(char c) {
        return Character.isLetter(c) || c == '_';
    }

    private boolean isIdentLetter(char c) {
        return Character.isLetter(c) || Character.isDigit(c) || c == '_';
    }

    public Lexer(FileSource fileSource) {
        cursor = new Cursor(fileSource);
        src = fileSource;
    }

    public ArrayList<Token> tokenize() {
        while (!cursor.isEOF()) {
            Token token = nextToken();
            if (token.isComment()) continue;
            else if (token.isError()) errors.add(new LexerError(token));
            else tokens.add(token);
        }
        return tokens;
    }

    public Token nextToken() {
        cursor.eatSpace();
        int start = cursor.lengthConsumed();
        SyntaxKind kind = tokenKind();
        while (kind == SyntaxKind.LINE_COMMENT || kind == SyntaxKind.BLOCK_COMMENT) {
            cursor.eatSpace();
            start = cursor.lengthConsumed();
            kind = tokenKind();
        }
        int end = cursor.lengthConsumed();
        return new Token(src, start, end, kind);
    }

    private SyntaxKind tokenKind() {
        int start = cursor.lengthConsumed();
        char first = cursor.curr();
        cursor.bump();
        switch(first) {
            case (char)(-1): return SyntaxKind.EOF;
            case '&':
                if (cursor.curr() != '&') return SyntaxKind.ERROR;
                cursor.bump();
                return SyntaxKind.AND_LOGIC;
            case '|':
                if (cursor.curr() != '|') return SyntaxKind.ERROR;
                cursor.bump();
                return SyntaxKind.OR_LOGIC;
            case '+': return SyntaxKind.PLUS;
            case '-': return SyntaxKind.MINUS;
            case '*': return SyntaxKind.MULT;
            case '/':
                if (cursor.curr() == '/') {
                    cursor.eatLineComment();
                    return SyntaxKind.LINE_COMMENT;
                } else if (cursor.curr() == '*') {
                    cursor.eatBlockComment();
                    return SyntaxKind.BLOCK_COMMENT;
                } else {
                    return SyntaxKind.DIVIDE;
                }
            case '%': return SyntaxKind.MOD;
            case '<':
                if (cursor.curr() != '=') return SyntaxKind.LT;
                cursor.bump();
                return SyntaxKind.LE;
            case '>':
                if (cursor.curr() != '=') return SyntaxKind.GT;
                cursor.bump();
                return SyntaxKind.GE;
            case '=':
                if (cursor.curr() != '=') return SyntaxKind.ASSIGN;
                cursor.bump();
                return SyntaxKind.EQ;
            case '!':
                if (cursor.curr() != '=') return SyntaxKind.NOT_LOGIC;
                cursor.bump();
                return SyntaxKind.NE;
            case ';': return SyntaxKind.SEMICN;
            case ',': return SyntaxKind.COMMA;
            case '(': return SyntaxKind.LPARENT;
            case ')': return SyntaxKind.RPARENT;
            case '[': return SyntaxKind.LBRACK;
            case ']': return SyntaxKind.RBRACK;
            case '{': return SyntaxKind.LBRACE;
            case '}': return SyntaxKind.RBRACE;
            case '"':
                while (cursor.curr() != '"') cursor.bump();
                cursor.bump();
                return SyntaxKind.STRING;
            default:
                if (Character.isDigit(first)) {
                    while(Character.isDigit(cursor.curr())) {
                        cursor.bump();
                    }
                    return SyntaxKind.INT_CONST;
                }
                else if (isIdentFirst(first)) {
                    while (isIdentLetter(cursor.curr())) {
                        cursor.bump();
                    }
                    int end = cursor.lengthConsumed();
                    String ident = "";
                    for(;start < end; start++) ident += src.getNthChar(start);
                    if (ident.equals("bitand")) return SyntaxKind.BIT_AND;
                    return SyntaxKind.string2kind.getOrDefault(ident, SyntaxKind.IDENT);
                } else return SyntaxKind.ERROR;
        }
    }

    public boolean hasError() {
        return !errors.isEmpty();
    }

    public void printError() {
        for (LexerError error : errors) {
            error.print();
        }
    }
}
