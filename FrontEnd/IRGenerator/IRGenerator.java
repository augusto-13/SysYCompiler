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
            int func_num = -1;
            FileWriter fw = new FileWriter(ir, true);
            if (!IRCodes.irCodes_global_str.isEmpty())
                fw.write("**global_str**\n");
            for (IRCode irCode : IRCodes.irCodes_global_str) {
                fw.write(irCode.toString());
            }
            if (!IRCodes.irCodes_global_decl.isEmpty())
                fw.write("\n**global_decl**\n");
            for (IRCode irCode : IRCodes.irCodes_global_decl) {
                fw.write(irCode.toString());
            }
            fw.write("\n**main**\n");
            for (IRCode irCode : IRCodes.irCodes_main) {
                fw.write(irCode.toString());
            }
            fw.write("\n");
            for (ArrayList<IRCode> irCodes : IRCodes.irCodes_func) {
                fw.write(String.format("**func_%d**\n", ++func_num));
                for (IRCode irCode : irCodes) {
                    fw.write(irCode.toString());
                }
                fw.write("\n");
            }
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
