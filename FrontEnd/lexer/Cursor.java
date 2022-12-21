package FrontEnd.lexer;

public class Cursor {
    private final FileSource fileSource;
    private final int start;
    private final int end;
    private int now;

    public Cursor(FileSource fileSource) {
        this.fileSource = fileSource;
        start = now = 0;
        end = fileSource.getEnd();
    }

    public char previous() {
        if (now == start) return (char)(-1);
        return fileSource.getNthChar(now - 1);
    }

    // 单纯为美观而保留的函数
    public char nthChar(int n) {
        if (now + n >= end) return (char)(-1);
        return fileSource.getNthChar(now + n);
    }

    public char curr() {
        return nthChar(0);
    }

    public boolean isEOF() {
        return now >= end;
    }

    public void bump() {
        if (now < end) now++;
    }

    /*
     * eatWhile实属优雅，在下不敢模仿
     * !='\n', !='/', isSpace()
     *
     * 约定：只有无用信息的eat封装在cursor中处理；
     * 对于eatString(), eatIdent(), eatInt()等有用信息的处理函数，
     * 暂时决定均在Lexer中单独实现。
     */
    public void eatSpace() {
        while (!isEOF() && Character.isWhitespace(curr())) bump();
    }

    public void eatLineComment() {
        while (!isEOF() && curr() != '\n') bump();
        bump();
    }

    // 妙极了！！！
    public void eatBlockComment() {
        bump();
        bump();
        while (!isEOF() && (previous() != '*' || curr() != '/')) {
            bump();
        }
        bump();
    }



    public int lengthConsumed() {
        return now;
    }


}
