package FrontEnd.IRGenerator.Quadruple;


import java.util.ArrayList;

public class _7_FuncCall_Q extends IRCode {
    String name;
    ArrayList<String> args;

    public _7_FuncCall_Q(String name, ArrayList<String> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (String arg : args) {
            ret.append("push ").append(arg).append("\n");
        }
        ret.append("call ").append(name).append("\n");
        return ret.toString();
    }
}
