package FrontEnd.IRGenerator.Quadruple;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.IRTbl.syms.Str;
import FrontEnd.IRGenerator.IRTbl.syms.Sym;
import FrontEnd.IRGenerator.IRTbl.syms.Var;

import java.util.ArrayList;

public class _11_Out_Q extends IRCode{
    ArrayList<Str> str_defs = new ArrayList<>();
    ArrayList<Sym> outs = new ArrayList<>();

    // str: without ""
    public _11_Out_Q(String str, ArrayList<Var> args) {
        int start = 0, arg_i = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '%' && str.charAt(i + 1) == 'd') {
                if (start != i) {
                    Str str_sym = new Str(String.format("str_%d", ++IRGenerator.str_num), str.substring(start, i));
                    IRCodes.addStrQ(new _14_Str_Q(str_sym));
                    outs.add(str_sym);
                }
                outs.add(args.get(arg_i++));
                start = i + 2;
                i++;
            }
        }
        if (start != str.length()) outs.add(new Str(String.format("str_%d", ++IRGenerator.str_num), str.substring(start)));
    }


    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (Sym out : outs) {
            ret.append(String.format("printf %s\n", out));
        }
        return ret.toString();
    }



}
