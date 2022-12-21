package FrontEnd.IRGenerator.IRTbl;

import FrontEnd.IRGenerator.IRTbl.syms.Var;


public class Var_tbl extends Entry_tbl {
    String name;
    Var var;

    // 预留接口：寄存器编号
    int reg = -1;

    @Override
    public String getName() {
        return name;
    }

    public Var getVar() {
        return var;
    }

    public Var_tbl(String name, Var var) {
        /* TODO:
         * [Var_tbl]: 填表时应当使用原始标识符
         * [Var]: 应使用新标识符 */
        this.name = name;
        this.var = var;
    }
}