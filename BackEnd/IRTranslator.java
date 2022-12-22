package BackEnd;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.Quadruple.IRCode;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class IRTranslator {

    ArrayList<IRCode> ir_data_global_decl = IRCodes.irCodes_global_decl;
    ArrayList<IRCode> ir_data_global_str = IRCodes.irCodes_global_str;
    ArrayList<IRCode> ir_text = IRCodes.irCodes_main;
    StringBuilder mips_data = new StringBuilder();
    ArrayList<MIPSCode> mips_text = new ArrayList<>();

    public IRTranslator() {
        data();
        text();
    }

    public void data() {
        if (ir_data_global_decl.isEmpty() && ir_data_global_str.isEmpty()) return;
        mips_data.append(".data\n");
        for (IRCode decl : ir_data_global_decl) {
            decl.toData(mips_data);
        }
        for (IRCode str : ir_data_global_str) {
            str.toData(mips_data);
        }
    }

    public void text() {

    }

    public void printTo(File mips) {
        try {
            FileWriter fw = new FileWriter(mips, true);
            fw.write(mips_data.toString());
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
