package FrontEnd.IRGenerator;

import java.util.HashMap;

public class IRContext {
    public static boolean global_decl = false;
    public static boolean block_in_func_def = false;
    public static int level = 0;
    public static HashMap<String, Integer> name2d2 = new HashMap<>();
    public static boolean lVal_right = false;
    public static boolean jump_if;
    public static String if_label;
    public static String else_label;
    public static String continue_label;
    public static String break_label;
}
