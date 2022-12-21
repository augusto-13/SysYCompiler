package FrontEnd.parser;

import FrontEnd.lexer.*;
import FrontEnd.nodes.*;

import java.util.Stack;

public class ASTBuilder {

    // size: 当前栈内元素个数
    class Parent {SyntaxKind kind; int size; Parent(SyntaxKind sk, int i) {kind = sk; size = i;}}

    private final Stack<Parent> ancestor = new Stack<>();
    private final Stack<Node> children = new Stack<>();

    public void startNode(SyntaxKind sk) {
        ancestor.push(new Parent(sk, children.size()));
    }

    public void finishNode(Node node) {
        // 这里需要倒一下顺序
        // Figure:
        // [children] || ... | LVal | '=' |  Exp
        //  -------->
        // [tmp] || Exp | '=' | LVal
        //  -------->
        // [node.children]:  add(LVal, '=', 'Exp')

        Parent p = ancestor.pop();
        Stack<Node> tmp = new Stack<>();
        while (children.size() > p.size) {
            tmp.push(children.pop());
        }
        while (!tmp.isEmpty()) {
            node.addChild(tmp.pop());
        }
        node.setNodeElement(p.kind);
        children.push(node);
    }

    public void startNodeAt(int point, SyntaxKind kind) {
        ancestor.push(new Parent(kind, point));
    }

    // Handle LeafNode
    public void terminalSymbol(SyntaxKind kind, String content, int line) {
        Node node = new LeafNode(kind, content, line);
        children.push(node);
    }

    public int checkPoint() {
        return children.size();
    }

    public Node root() {
        if (!ancestor.isEmpty()) {
            System.out.println("Error: ancestor stack should be empty!");
        }
        return children.peek();
    }
}
