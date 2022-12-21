package FrontEnd.IRGenerator;

import FrontEnd.IRGenerator.Quadruple.IRCode;
import FrontEnd.IRGenerator.Quadruple._14_StrDecl_Q;
import FrontEnd.IRGenerator.Quadruple._1_VarDecl_Q;
import FrontEnd.IRGenerator.Quadruple._2_ArrDecl_Q;

import java.util.ArrayList;

// Container Class
public class IRCodes {
    // 优化前/优化后的'IR四元式列表'
    public static final ArrayList<IRCode> irCodes_ori = new ArrayList<>();
    public static final ArrayList<IRCode> irCodes_global_decl = new ArrayList<>();
    public static final ArrayList<IRCode> irCodes_global_str = new ArrayList<>();
    public static final ArrayList<IRCode> irCodes_opt = new ArrayList<>();

    public static void addIRCode_ori(IRCode code) {
        if (IRContext.global_decl && (code instanceof _1_VarDecl_Q || code instanceof _2_ArrDecl_Q)) irCodes_global_decl.add(code);
        else if (code instanceof _14_StrDecl_Q) irCodes_global_str.add(code);
        else irCodes_ori.add(code);
    }

    public static void addIRCode_opt(IRCode code) {
        irCodes_opt.add(code);
    }



}
