package FrontEnd.nodes;


import FrontEnd.IRGenerator.IRContext;
import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.IRGenerator.IRTbl.syms.Var;

import java.util.ArrayList;

public class InitValNode extends Node {
    public InitValNode() {
    }

    @Override
    public Var genIR() {
        // InitVal → Exp
        //         | '{' [ InitVal {',' InitVal} ] '}'
        ArrayList<Node> children = getChildren();
        int size = children.size();
        if (size == 1) {
            // dim == 0
            // InitVal → Exp
            // 如果是在`global_decl`中解析表达式，则全部计算使用init_value
            return children.get(0).genIR();
        } else if (children.get(1).getChildren().size() == 1) {
            // dim == 1
            // InitVal → '{' InitVal { ','  InitVal } '}'
            //      InitVal → Exp
            if (IRContext.global_decl) {
                ArrayList<Integer> values = new ArrayList<>();
                int d1 = 0;
                for (int i = 1; i < size - 1; i += 2) {
                    Var globalInitVal = children.get(i).genIR();
                    values.add(globalInitVal.getValidInitVal());
                    d1++;
                }
                return new Var("const", "temp", 1, d1, 0, values);
            }
            else {
                ArrayList<Var> values = new ArrayList<>();
                int d1 = 0;
                for (int i = 1; i < size - 1; i += 2) {
                    Var initValV = children.get(i).genIR();
                    values.add(initValV);
                    d1++;
                }
                return new Var("var", "temp", 1, d1, 0, values);
            }
        } else {
            // dim == 2
            // InitVal → '{' InitVal {  ',' InitVal } '}'
            //      InitVal → '{' InitVal {  ',' InitVal } '}'
            int d1 = (children.size() - 1) / 2;
            int d2 = (children.get(1).getChildren().size() - 1) / 2;
            if (IRContext.global_decl) {
                ArrayList<Integer> values = new ArrayList<>();
                for (int i = 1; i < size - 1; i += 2) {
                    Var globalInitVal = children.get(i).genIR();
                    d2 = globalInitVal.getD1();
                    values.addAll(globalInitVal.getConst_value_arr());
                }
                return new Var("const", "temp", 2, d1, d2, values);
            } else {
                ArrayList<Var> values = new ArrayList<>();
                for (int i = 1; i < size - 1; i += 2) {
                    Var initValV = children.get(i).genIR();
                    d2 = initValV.getD1();
                    values.addAll(initValV.getInit_value_arr());
                }
                return new Var("var", "temp", 2, d1, d2, values);
            }
        }
    }
}
