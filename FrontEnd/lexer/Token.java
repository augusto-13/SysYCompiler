package FrontEnd.lexer;

public class Token {
    private final int line;
    private final SyntaxKind kind;
    private final String content;

    public Token(FileSource src, int start, int end, SyntaxKind kind) {
        String tmp = "";
        for (int i = start; i < end; i++) {
            tmp += src.getNthChar(i);
        }
        this.content = tmp;
        this.kind = kind;
        this.line = src.getLineNum(start);
    }

    public boolean isEOF() {
        return kind.equals(SyntaxKind.EOF);
    }

    public boolean isError() {
        return kind.equals(SyntaxKind.ERROR);
    }

    public boolean isComment() {
        return kind.equals(SyntaxKind.BLOCK_COMMENT)
                || kind.equals(SyntaxKind.LINE_COMMENT);
    }

    public int getLine() {
        return line;
    }

    public SyntaxKind getKind() {
        return kind;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return kind.toString() + " " + content + "\n";
    }

}
