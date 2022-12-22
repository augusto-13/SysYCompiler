package FrontEnd.IRGenerator.Quadruple.Elements;

import FrontEnd.IRGenerator.IRTbl.syms.Var;

public class PrintElem {
    public boolean is_num = false;
    public boolean is_var = false;
    public int num;
    public String str_name;
    public String var_name;

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
