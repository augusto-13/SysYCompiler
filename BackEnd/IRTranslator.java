package BackEnd;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.Quadruple.IRCode;
import FrontEnd.IRGenerator.Quadruple._9_FuncDecl_Q;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class IRTranslator {

    ArrayList<IRCode> ir_data_global_decl = IRCodes.irCodes_global_decl;
    ArrayList<IRCode> ir_data_global_str = IRCodes.irCodes_global_str;
    ArrayList<IRCode> ir_text_main = IRCodes.irCodes_main;
    ArrayList<ArrayList<IRCode>> ir_text_func = IRCodes.irCodes_func;
    StringBuilder mips_data = new StringBuilder();
    ArrayList<MIPSCode> mips_func_text = new ArrayList<>();
    ArrayList<MIPSCode> mips_main_text = new ArrayList<>();


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
        for (ArrayList<IRCode> irCodes : ir_text_func) {
            String func_name = ((_9_FuncDecl_Q) irCodes.get(0)).getFuncName();
            mips_func_text.add(new MIPSCode.Label(func_name));
            for (IRCode irCode : irCodes) {
                irCode.toText("func", mips_func_text);
            }
            mips_func_text.add(new MIPSCode.Enter());
        }
        for (IRCode irCode : ir_text_main) {
            irCode.toText("main", mips_main_text);
        }
    }

    public void printTo(File mips) {
        try {
            FileWriter fw = new FileWriter(mips, true);
            if (!mips_data.toString().isEmpty()) {
                fw.write(".data\n");
                fw.write(mips_data.toString());
                fw.write("\n");
            }
            for (MIPSCode code : mips_main_text) {
                fw.write(code.toString());
            }
            fw.write("\n");
            for (MIPSCode code : mips_func_text) {
                fw.write(code.toString());
            }
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
