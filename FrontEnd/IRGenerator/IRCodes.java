package FrontEnd.IRGenerator;

import FrontEnd.IRGenerator.Quadruple.IRCode;
import FrontEnd.IRGenerator.Quadruple._14_Str_Q;

import java.util.ArrayList;

// Container Class
public class IRCodes {
    // 优化前/优化后的'IR四元式列表'
    public static final ArrayList<IRCode> irCodes_ori = new ArrayList<>();
    public static final ArrayList<IRCode> irCodes_opt = new ArrayList<>();
    public static final ArrayList<IRCode> irCodes_str = new ArrayList<>();

    public static void addStrQ(_14_Str_Q str_Q) {irCodes_str.add(str_Q);}

    public static void addIRCode_ori(IRCode code) {
        irCodes_ori.add(code);
    }

    public static void addIRCode_opt(IRCode code) {
        irCodes_opt.add(code);
    }



}
