package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.lexer.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public abstract class Node {
    private int startLine;
    private int finishLine;
    private SyntaxKind kind;
    private final ArrayList<Node> children = new ArrayList<>();

    // For Non-terminal
    public void setNodeElement(SyntaxKind sk) {
        kind = sk;
        startLine = children.get(0).startLine;
        finishLine = children.get(children.size() - 1).finishLine;
    }

    // For Error & Terminal
    public void setNodeElement(SyntaxKind sk, int line) {
        kind = sk;
        startLine = finishLine = line;
    }

    public int getFinishLine() {
        return finishLine;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void addChild(Node node) {
        children.add(node);
    }

    public SyntaxKind getKind() {
        return kind;
    }

    public void print() {
        for (Node child : children) {
            child.print();
        }
        System.out.println(kind.toString());
    }

    public void printTo(File out) {
        try {
            for (Node child : children) {
                child.printTo(out);
            }
            FileWriter fw = new FileWriter(out, true);
            fw.write(kind.toString() + "\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkError() {
        for (Node child : getChildren()) {
            child.checkError();
        }
    }

    public Var genIR() {
        for (Node child : getChildren()) {
            child.genIR();
        }
        return null;
    }

}
