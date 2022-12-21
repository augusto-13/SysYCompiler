package FrontEnd.lexer;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class TokenSource {
    private final ArrayList<Token> tokens;
    private int curr;

    public TokenSource(ArrayList<Token> tokens) {
        this.tokens = tokens;
        curr = 0;
    }

    public Token curr() {
        return nthToken(0);
    }

    public Token nthToken(int n) {
        if (curr + n >= tokens.size()) {
            return null;
        }
        return tokens.get(curr + n);
    }

    public void bump() {
        if (curr < tokens.size())
        curr++;
    }

    public int lastLine() {
        if (curr() == null) return 0;
        return curr().getLine();
    }

    // for FrontEnd.lexer-test
    public void printTo(File out){
        try {
            FileWriter fw = new FileWriter(out, false);
            for (Token token : tokens) {
                System.out.print(token);
                fw.write(token.toString());
                fw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
