package FrontEnd.IRGenerator.Quadruple;

import FrontEnd.IRGenerator.Quadruple.Elements.LVal;

public class _10_In_Q extends IRCode {
    LVal l;

    public _10_In_Q(LVal l) {
        this.l = l;
    }

    @Override
    public String toString() {
        return String.format("scanf %s\n", l);
    }
}
