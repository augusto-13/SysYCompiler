package FrontEnd.IRGenerator.Quadruple.Elements;

import FrontEnd.IRGenerator.IRTbl.syms.Var;

public class PrintElem {
    boolean is_num = false;
    boolean is_var = false;
    int num;
    String str_name;
    String var_name;

    public PrintElem(Var var) {
        if (var.isConst()) {
            is_num = true;
            num = var.getConst_value();
        }
        else {
            is_var = true;
            var_name = var.getName();
        }
    }

    public PrintElem(String str) {
        str_name = str;
    }

    @Override
    public String toString() {
        return is_num ? Integer.toString(num) : is_var ? var_name : str_name;
    }
}
