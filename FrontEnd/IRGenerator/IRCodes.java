package FrontEnd.IRGenerator;

import FrontEnd.IRGenerator.Quadruple.IRCode;
import FrontEnd.IRGenerator.Quadruple._14_StrDecl_Q;
import FrontEnd.IRGenerator.Quadruple._1_VarDecl_Q;
import FrontEnd.IRGenerator.Quadruple._2_ArrDecl_Q;
import FrontEnd.IRGenerator.Quadruple._8_FuncRet_Q;

import java.util.ArrayList;

// Container Class
public class IRCodes {
    // 优化前/优化后的'IR四元式列表'
    public static final ArrayList<IRCode> irCodes_main = new ArrayList<>();
    public static final ArrayList<ArrayList<IRCode>> irCodes_func = new ArrayList<>();
    public static ArrayList<IRCode> irCodes_curr_func;
    public static final ArrayList<IRCode> irCodes_global_decl = new ArrayList<>();
    public static final ArrayList<IRCode> irCodes_global_str = new ArrayList<>();
    public static final ArrayList<IRCode> irCodes_opt = new ArrayList<>();

    public static void addIRCode_ori(IRCode code) {
        if (IRContext.global_decl && (code instanceof _1_VarDecl_Q || code instanceof _2_ArrDecl_Q)) irCodes_global_decl.add(code);
        else if (code instanceof _14_StrDecl_Q) irCodes_global_str.add(code);
        else if (IRContext.in_func) irCodes_curr_func.add(code);
        else irCodes_main.add(code);
    }

    public static void init_irCodes_curr_func() {
        irCodes_curr_func = new ArrayList<>();
    }

    public static void merge_curr_func_into_irCodes_func() {
        int size = irCodes_curr_func.size();
        if ((irCodes_curr_func.get(size - 2)) instanceof _8_FuncRet_Q) {
            irCodes_curr_func.remove(size - 1);
        }
        irCodes_func.add(irCodes_curr_func);
    }

    public static void addIRCode_opt(IRCode code) {
        irCodes_opt.add(code);
    }



}
