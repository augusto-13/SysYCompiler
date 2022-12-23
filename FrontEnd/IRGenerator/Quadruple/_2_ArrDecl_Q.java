package FrontEnd.IRGenerator.Quadruple;


import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;

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

    @Override
    public void toData(StringBuilder mips_data) {
        if (global_initVal.isEmpty()) {
            mips_data.append(String.format("%s: .space %d\n", name.substring(1), d << 2));
        }
        else {
            mips_data.append(name.substring(1)).append(":\n");
            for (Integer i : global_initVal) {
                mips_data.append(String.format(".word %d\n", i));
            }
        }
        MIPSTbl.global_name2addr.put(name, MIPSTbl.global_address);
        MIPSTbl.global_address += (d << 2);
    }

    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        if (type.equals("main")) {
            MIPSTbl.main_name2addr.put(name, MIPSTbl.main_var_address);
            MIPSTbl.main_var_address += (d << 2);
        }
        else {
            MIPSTbl.func_name2offset.put(name, MIPSTbl.sp_offset - ((d - 1) << 2));
            MIPSTbl.sp_offset -= (d << 2);
        }
    }
}
