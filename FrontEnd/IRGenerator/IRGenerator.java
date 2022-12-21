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
    }

    private void visitAST() {
        rootNode.genIR();
    }

    public void printTo(File ir) {
        try {
            FileWriter fw = new FileWriter(ir, true);
            for (IRCode irCode : IRCodes.irCodes_global_str) {
                fw.write(irCode.toString());
            }
            for (IRCode irCode : IRCodes.irCodes_global_decl) {
                fw.write(irCode.toString());
            }
            for (IRCode irCode : IRCodes.irCodes_ori) {
                fw.write(irCode.toString());
            }
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
