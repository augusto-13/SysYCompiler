package FrontEnd.IRGenerator.Quadruple;

import FrontEnd.IRGenerator.IRTbl.syms.Str;

public class _14_Str_Q extends IRCode {

    Str str;

    public _14_Str_Q (Str str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return String.format("const str %s = \"%s\"\n", str, str.getContent());
    }

}
