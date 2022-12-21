package FrontEnd.nodes;

import java.io.File;

public class DeclNode extends Node{
    public DeclNode() { }

    @Override
    public void print() {
        for (Node child : super.getChildren()) {
            child.print();
        }
    }

    @Override
    public void printTo(File out) {
        try {
            for (Node child : super.getChildren()) {
                child.printTo(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
