package FrontEnd.IRGenerator.Quadruple;

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



}
