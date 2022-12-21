package FrontEnd.errorChecker;

import java.util.ArrayList;

public class SymbolInfo {
    public static int VOID_FUNC = 0;
    public static int INT_FUNC = 1;
    public static int INT = 2;
    public static int CONST_INT = 3;
    public static int ARRAY_1D = 4;
    public static int CONST_ARRAY_1D = 5;
    public static int ARRAY_2D = 6;
    public static int CONST_ARRAY_2D = 7;
    public static int UNKNOWN = 8;

    public int type;
    public ArrayList<Integer> argTypes = new ArrayList<>();


    public SymbolInfo(int type) {
        this.type = type;
    }

    public SymbolInfo(int type, ArrayList<Integer> argTypes) {
        this.type = type;
        this.argTypes = argTypes;
    }
}
