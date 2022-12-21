package FrontEnd.IRGenerator.Quadruple;


import java.util.ArrayList;

public class _2_ArrDecl_Q extends IRCode {
    String name;
    int d;
    // 只有为全局变量时不为空
    ArrayList<Integer> global_initVal = new ArrayList<>();

    public _2_ArrDecl_Q(String n, int d_) {
        name = n;
        d = d_;
    }

    public _2_ArrDecl_Q(String n, int d_, ArrayList<Integer> i) {
        name = n;
        d = d_;
        global_initVal.addAll(i);
    }

    @Override
    public String toString() {
        if (global_initVal.isEmpty()) return String.format("arr int %s[%d]\n", name, d);
        StringBuilder ret = new StringBuilder();
        ret.append(String.format("arr int %s[%d]\n", name, d));
        for (int i = 0; i < d; i++) ret.append(String.format("%s[%d] = %d\n", name, i, global_initVal.get(i)));
        return ret.toString();
    }
}
