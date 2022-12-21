package BackEnd;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.Quadruple.IRCode;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class IRTranslator {

    ArrayList<IRCode> ir_data_global_decl = IRCodes.irCodes_global_decl;
    ArrayList<IRCode> ir_data_global_str = IRCodes.irCodes_global_str;
    ArrayList<IRCode> ir_text = IRCodes.irCodes_ori;
    StringBuilder mips_data = new StringBuilder();
    ArrayList<MIPSCode> mips_text = new ArrayList<>();

    public IRTranslator() {
        data();
        text();
    }

    public void data() {

    }

    public void text() {

    }

    public void printTo(File mips) {
        try {
            FileWriter fw = new FileWriter(mips, true);
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
