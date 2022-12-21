package FrontEnd.nodes;

import FrontEnd.errorChecker.Context;
import FrontEnd.errorChecker.ErrorKind;
import FrontEnd.errorChecker.ErrorPair;
import FrontEnd.lexer.SyntaxKind;

import java.io.File;
import java.io.FileWriter;

public class LeafNode extends Node {
    private String content;
    public LeafNode(SyntaxKind sk, String content, int line) {
        setNodeElement(sk, line);
        this.content = content;
    }

    @Override
    public void print() {
        System.out.println(getKind().toString() + " " + content);
    }

    // For Semantic Analysis
    public String getContent() {
        return content;
    }

    @Override
    public void printTo(File out){
        try {
            FileWriter fw = new FileWriter(out, true);
            fw.write(getKind().toString() + " " + content + "\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkError() {
        if (getKind() == SyntaxKind.STRING) {
            for (int i = 1; i < content.length() - 1; i++) {
                char ch = content.charAt(i);
                if (ch == 32 || ch == 33 || (ch >= 40 && ch <= 126 && ch != 92)) {
                    continue;
                } else if (ch == 92) {
                    if (i + 1 < content.length() - 1 && content.charAt(i + 1) == 'n') {
                        i++;
                        continue;
                    }
                } else if (ch == '%') {
                    if (i + 1 < content.length() - 1 && content.charAt(i + 1) == 'd') {
                        i++;
                        continue;
                    }
                }
                Context.errorList.add(new ErrorPair(ErrorKind.ILLEGAL_CHAR, getFinishLine()));
                return;
            }
        }
    }
}
