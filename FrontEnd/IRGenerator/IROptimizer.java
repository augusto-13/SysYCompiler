package FrontEnd.IRGenerator;

import FrontEnd.IRGenerator.Quadruple.IRCode;

import java.io.File;
import java.io.FileWriter;

public class IROptimizer {

    public IROptimizer() {
        IRCodes.irCodes_opt.addAll(IRCodes.irCodes_ori);
    }

    public void printTo(File ir_) {
        try {
            FileWriter fw = new FileWriter(ir_, true);
            for (IRCode irCode : IRCodes.irCodes_opt) {
                fw.write(irCode.toString());
            }
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
