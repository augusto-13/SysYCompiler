package FrontEnd.nodes;


import FrontEnd.IRGenerator.IRTbl.syms.Sym;
import FrontEnd.IRGenerator.IRTbl.syms.Var;

import java.util.ArrayList;

public class InitValNode extends Node {
    public InitValNode() {
    }

    @Override
    public Sym genIR() {
        // InitVal → Exp
        //         | '{' [ InitVal {',' InitVal} ] '}'
        // "棘手的小问题"：
        // InitVal可能是"表达式"
        ArrayList<Node> children = getChildren();
        int size = children.size();
        if (size == 1) {
            // dim == 0
            // InitVal → Exp
            Var expV = (Var) children.get(0).genIR();
            return expV;
        } else if (children.get(1).getChildren().size() == 1) {
            // dim == 1
            // InitVal → '{' InitVal { ','  InitVal } '}'
            //      InitVal → Exp
            ArrayList<Var> values = new ArrayList<>();
            int d1 = 0;
            for (int i = 1; i < size - 1; i += 2) {
                Var initValV = (Var) children.get(i).genIR();
                values.add(initValV);
                d1++;
            }
            return new Var("var", "temp", 1, d1, 0, values);
        } else {
            // dim == 2
            // InitVal → '{' InitVal {  ',' InitVal } '}'
            //      InitVal → '{' InitVal {  ',' InitVal } '}'
            ArrayList<Var> values = new ArrayList<>();
            int d1 = (children.size() - 1) / 2;
            int d2 = (children.get(1).getChildren().size() - 1) / 2;
            for (int i = 1; i < size - 1; i += 2) {
                Var initValV = (Var) children.get(i).genIR();
                d2 = initValV.getD1();
                values.addAll(initValV.getInit_value_arr());
            }
            return new Var("var", "temp", 2, d1, d2, values);
        }
    }
}
