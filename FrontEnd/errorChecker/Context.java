package FrontEnd.errorChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Context {

    public final static int NOT = 0;
    public final static int VOID = 1;
    public final static int INT = 2;

    public static int currLevel = 0;
    public static int inLoopNum = 0;
    public static boolean funcBlock = false;
    public static int inFuncType = NOT;
    public static boolean lValInStmt = false;

    public static Stack<HashMap<String, SymbolInfo>> symT = new Stack<>();
    public static HashMap<String, Stack<Integer>> content2levels = new HashMap<>();

    public final static ArrayList<Integer> funcParamDimStack = new ArrayList<>();

    public final static ArrayList<ErrorPair> errorList = new ArrayList<>();

    public static void init() {
        currLevel = 0;
        symT.push(new HashMap<>());
    }

    public static void newScope() {
        symT.push(new HashMap<>());
        currLevel++;
    }

    public static void clearCurrScope() {
        for (String content : symT.peek().keySet()) {
            content2levels.get(content).pop();
            if (content2levels.get(content).size() == 0)
                content2levels.remove(content);
        }
        symT.pop();
        currLevel--;
    }

    public static void addSymbol(String name, int type) {
        symT.peek().put(name, (new SymbolInfo(type)));
        if (!content2levels.containsKey(name)) content2levels.put(name, new Stack<>());
        content2levels.get(name).push(currLevel);
    }

    public static void addSymbol_func(String name, int type, ArrayList<Integer> argTypes) {
        symT.peek().put(name, (new SymbolInfo(type, argTypes)));
        if (!content2levels.containsKey(name)) content2levels.put(name, new Stack<>());
        content2levels.get(name).push(currLevel);
    }

    public static void addError(ErrorPair pair) {
        errorList.add(pair);
    }

    public static void addToParamStack(int paramDim) {
        funcParamDimStack.add(paramDim);
    }

    public static void pourStack(ArrayList<Integer> argTypes) {
        argTypes.addAll(funcParamDimStack);
        funcParamDimStack.clear();
    }

    public static boolean checkRedefineError(String name) {
        return Context.content2levels.containsKey(name)
                && Context.content2levels.get(name).peek() >= Context.currLevel;
    }

    public static boolean checkUndefinedError(String name) {
        return !Context.content2levels.containsKey(name);
    }

    public static boolean checkModifyConstError(String name) {
        int level = content2levels.get(name).peek();
        for (String key : symT.get(level).keySet()) {
            if (key.equals(name)) {
                return symT.get(level).get(key).type == SymbolInfo.CONST_INT ||
                        symT.get(level).get(key).type == SymbolInfo.CONST_ARRAY_1D ||
                        symT.get(level).get(key).type == SymbolInfo.CONST_ARRAY_2D;
            }
        }
        return false;
    }

    public static void checkArgsError(String name, ArrayList<Integer> rArgs, int line) {
        int level = content2levels.get(name).peek();
        ArrayList<Integer> fArgs = symT.get(level).get(name).argTypes;
        if (fArgs.size() != rArgs.size()) addError(new ErrorPair(ErrorKind.UNMATCHED_PARAM_NUM, line));
        else if (!fArgs.equals(rArgs)) {
            addError(new ErrorPair(ErrorKind.UNMATCHED_PARAM_TYPE, line));
        }
    }

    public static int getDimOfIdent(String name) {
        if (checkUndefinedError(name)) return -1;
        int level = content2levels.get(name).peek();
        int type = symT.get(level).get(name).type;
        int dim = (type == SymbolInfo.INT || type == SymbolInfo.CONST_INT) ? 0 :
                (type == SymbolInfo.ARRAY_1D || type == SymbolInfo.CONST_ARRAY_1D) ? 1 : 2;
        return dim;
    }

    public static int checkFuncReturnDim(String name) {
        if (checkUndefinedError(name)) return -1;
        int level = content2levels.get(name).peek();
        int type = symT.get(level).get(name).type;
        return type == SymbolInfo.INT_FUNC ? 0 : -1;
    }

}
