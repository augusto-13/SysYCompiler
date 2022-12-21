package FrontEnd.IRGenerator;

import FrontEnd.IRGenerator.Quadruple.IRCode;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.nodes.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class IRGenerator {
    public static int block_num = -1;
    public static int temp_num = -1;
    public static int str_num = -1;
    public static int label_num = -1;

    private final Node rootNode;
    private final ArrayList<IRCode> irCodes = IRCodes.irCodes_ori;

    // 生成一个"t%d"临时变量
    public static Var genNewTemp() {
        return new Var("var", String.format("t%d", ++temp_num));
    }

    // 生成一个临时常量，不打印
    public static Var genConstTemp(int value) {
        return new Var("const" , "temp" , value);
    }

    // 生成一个label
    public static String genLabel() {return String.format("label_%d", ++label_num); }

    public IRGenerator(Node root) {
        rootNode = root;
        visitAST();
        IRCodes.irCodes_ori.addAll(0, IRCodes.irCodes_str);
    }

    private void visitAST() {
        rootNode.genIR();
    }

    public void printTo(File ir) {
        try {
            FileWriter fw = new FileWriter(ir, true);
            for (IRCode irCode : irCodes) {
                fw.write(irCode.toString());
            }
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
