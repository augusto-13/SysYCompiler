package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.IRGenerator.IRTbl.syms.Var;

import java.util.ArrayList;

public class ConstInitValNode extends Node {
    public ConstInitValNode() {
    }

    @Override
    public Var genIR() {
        // ConstInitVal → ConstExp
        //              |  '{' [ ConstInitVal {  ',' ConstInitVal } ]  '}'
        ArrayList<Node> children = getChildren();
        int size = children.size();
        if (size == 1) {
            // dim == 0
            // ConstInitVal → ConstExp
            return children.get(0).genIR();
        } else if (children.get(1).getChildren().size() == 1) {
            // dim == 1
            // ConstInitVal → '{' ConstInitVal {  ',' ConstInitVal } '}'
            //      ConstInitVal → ConstExp
            ArrayList<Integer> values = new ArrayList<>();
            int d1 = 0;
            for (int i = 1; i < size - 1; i += 2) {
                Var constInitVal = children.get(i).genIR();
                values.add(constInitVal.getConst_value());
                d1++;
            }
            return new Var("const", "temp", 1, d1, 0, values);
        } else {
            // dim == 2
            // ConstInitVal → '{' ConstInitVal {  ',' ConstInitVal } '}'
            //      ConstInitVal → '{' ConstInitVal {  ',' ConstInitVal } '}'
            ArrayList<Integer> values = new ArrayList<>();
            int d1 = (children.size() - 1) / 2;
            int d2 = (children.get(1).getChildren().size() - 1) / 2;
            for (int i = 1; i < size - 1; i += 2) {
                Var constInitVal = children.get(i).genIR();
                d2 = constInitVal.getD1();
                values.addAll(constInitVal.getConst_value_arr());
            }
            return new Var("const", "temp", 2, d1, d2, values);
        }
    }
}
