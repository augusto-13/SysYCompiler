package FrontEnd.IRGenerator.Quadruple;

import java.util.HashMap;

public class _13_Jump_Q extends IRCode {
    // bez, bnz, goto, bgt, bge, blt, ble
    final static HashMap<String, Integer> type_str2int = new HashMap<String, Integer>() {
        {
            put("goto", 0);
            put("bez", 1);
            put("bnz", 2);
            put("bgt", 3);
            put("blt", 4);
            put("bge", 5);
            put("ble", 6);
        }
    };
    final static HashMap<Integer, String> type_int2str = new HashMap<Integer, String>() {
        {
            put(0, "goto");
            put(1, "bez");
            put(2, "bnz");
            put(3, "bgt");
            put(4, "blt");
            put(5, "bge");
            put(6, "ble");
        }
    };
    int type;
    String label;
    String arg1;
    String arg2;

    public _13_Jump_Q(String t, String l) {
        type = type_str2int.get(t);
        label = l;
    }

    public _13_Jump_Q(String t, String l, String a) {
        type = type_str2int.get(t);
        label = l;
        arg1 = a;
    }

    public _13_Jump_Q(String t, String l, String a1, String a2) {
        type = type_str2int.get(t);
        label = l;
        arg1 = a1;
        arg2 = a2;
    }

    @Override
    public String toString() {
        return type == 0 ? String.format("goto %s\n", label) :
                type <= 2 ? String.format("%s %s %s\n", type_int2str.get(type), arg1, label)
                : String.format("%s %s %s %s\n", type_int2str.get(type), arg1, arg2, label);
    }
}
