package BackEnd;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.Quadruple.IRCode;
import FrontEnd.IRGenerator.Quadruple._1_VarDecl_Q;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class IRTranslator {

    ArrayList<IRCode> ir_data_global_decl = IRCodes.irCodes_global_decl;
    ArrayList<IRCode> ir_data_global_str = IRCodes.irCodes_global_str;
    ArrayList<IRCode> ir_text_main = IRCodes.irCodes_main;
    ArrayList<ArrayList<IRCode>> ir_text_func = IRCodes.irCodes_func;
    StringBuilder mips_data = new StringBuilder();
    ArrayList<MIPSCode> mips_text = new ArrayList<>();

    public IRTranslator() {
        data();
        text();
    }

    public void data() {
        for (IRCode decl : ir_data_global_decl) {
            decl.toData(mips_data);
        }
        for (IRCode str : ir_data_global_str) {
            str.toData(mips_data);
        }
    }

    public void text() {
        for (IRCode irCode : ir_text_main) {
            irCode.toText(mips_text);
        }
    }

    public void printTo(File mips) {
        try {
            FileWriter fw = new FileWriter(mips, true);
            if (!mips_data.toString().isEmpty()) {
                fw.write(".data\n");
                fw.write(mips_data.toString());
            }
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
