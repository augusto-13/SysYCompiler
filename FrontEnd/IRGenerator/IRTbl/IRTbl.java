package FrontEnd.IRGenerator.IRTbl;

import FrontEnd.IRGenerator.IRTbl.syms.Var;

import java.util.Stack;

public class IRTbl {

    public final static int FRAME_GLOBAL = 0;
    public final static int FRAME_FUNC_DEF_BLOCK = 1;
    public final static int FRAME_MAIN = 2;
    // 对于Block类型，内部执行完后要移除对应栈帧
    public final static int FRAME_BLOCK = 3;

    static Stack<Frame_tbl> stack_of_frames = new Stack<>();

    public static void newFrame(int type, String name) {
        stack_of_frames.push(new Frame_tbl(type, name));
    }

    public static void addEntry(Entry_tbl e) {stack_of_frames.peek().addEntry(e);}

    public static void addEntryToGlobalFrame(Entry_tbl e) {stack_of_frames.get(0).addEntry(e);}

    public static void removeCurrFrame() {
        if (stack_of_frames.peek().getType() == FRAME_BLOCK)
        stack_of_frames.pop();
        else System.out.println("You seem to have removed a frame that you're not supposed to operate on.");
    }

    private static Entry_tbl findInGlobalFrame(String name) {
        Frame_tbl globalFrame = stack_of_frames.get(0);
        return globalFrame.findName(name);
    }

    private static Entry_tbl findInCurrFrame(String name) {
        return stack_of_frames.peek().findName(name);
    }

//    public static Func_tbl findFunc(String funcName) {
//        return (Func_tbl) findInGlobalFrame(funcName);
//    }

    public static Var findVar(String name) {
        if (stack_of_frames.peek().getType() == FRAME_FUNC_DEF_BLOCK) {
            // FRAME_FUNC_DEF中查找Var
            // 遍历当前Func与Global两个Frame即可
            Entry_tbl entry = findInCurrFrame(name);
            if (entry != null) return ((Var_tbl) entry).getVar();
            entry = findInGlobalFrame(name);
            if (entry != null) return ((Var_tbl) entry).getVar();
            return null;
        }
        else {
            // FRAME_BLOCK与FRAME_MAIN中查找Var
            // 从后往前遍历GLOBAL, FUNC_DEF后的[MAIN与BLOCKs]
            for (int i = stack_of_frames.size() - 1; i >= 1; i--) {
                Frame_tbl frame = stack_of_frames.get(i);
                if (frame.getType() == FRAME_FUNC_DEF_BLOCK) break;
                if (frame.findName(name) != null)
                    return ((Var_tbl) frame.findName(name)).getVar();
            }
            return ((Var_tbl) findInGlobalFrame(name)).getVar();
        }
    }

    public static Func_tbl findFunc(String name) {
        return (Func_tbl) findInGlobalFrame(name);
    }

}
