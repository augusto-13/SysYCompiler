package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;
import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.IRGenerator.Quadruple.Elements.PrintElem;

import java.util.ArrayList;

public class _11_Out_Q extends IRCode{
    ArrayList<PrintElem> outs = new ArrayList<>();

    // str: without ""
    public _11_Out_Q(String str, ArrayList<Var> args) {
        int start = 0, arg_i = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '%' && str.charAt(i + 1) == 'd') {
                if (start != i) {
                    String name = String.format("str_%d", ++IRGenerator.str_num);
                    String content = str.substring(start, i);
                    IRCodes.addIRCode_ori(new _14_StrDecl_Q(name, content));
                    outs.add(new PrintElem(name));
                }
                outs.add(new PrintElem(args.get(arg_i++)));
                start = i + 2;
                i++;
            }
        }
        if (start != str.length()) {
            String name = String.format("str_%d", ++IRGenerator.str_num);
            String content = str.substring(start);
            IRCodes.addIRCode_ori(new _14_StrDecl_Q(name, content));
            outs.add(new PrintElem(name));
        }
    }


    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (PrintElem out : outs) {
            ret.append(String.format("printf %s\n", out));
        }
        return ret.toString();
    }

    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        for (PrintElem out : outs) {
            if (out.is_num) {
                // print %d, 6
                mips_text.add(new MIPSCode.LoadImm(4, out.num));
                mips_text.add(new MIPSCode.LoadImm(2, 1));
                mips_text.add(new MIPSCode.Sys());
            }
            else if (out.is_var) {
                // TODO
                String var = out.var_name;
                if (MIPSTbl.regOrMem_trueIfReg(var)) {

                }
            }
            else {
                // print %s
                mips_text.add(new MIPSCode.LA(4, out.str_name));
                mips_text.add(new MIPSCode.LoadImm(2, 4));
                mips_text.add(new MIPSCode.Sys());
            }
        }
    }
}
