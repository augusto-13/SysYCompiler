package FrontEnd.lexer;

public class LexerError {
    private final int lineNum;

    public LexerError(Token token) {
        lineNum = token.getLine();
    }

    public void print() {
        System.out.println("Lexer Error on line " + lineNum);
    }

}
