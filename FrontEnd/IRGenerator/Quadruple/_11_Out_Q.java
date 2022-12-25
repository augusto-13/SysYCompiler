package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;
import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.IRGenerator.Quadruple.Elements.PrintElem;

import java.util.ArrayList;

import static BackEnd.MIPSTbl.a0;
import static BackEnd.MIPSTbl.v0;

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
        // OK!!!
        for (PrintElem out : outs) {
            if (out.is_num) {
                // print %d, 6
                mips_text.add(new MIPSCode.LI(a0, out.num));
                mips_text.add(new MIPSCode.LI(v0, 1));
                mips_text.add(new MIPSCode.Sys());
            }
            else if (out.is_var) {
                String var = out.var_name;
                if (var.charAt(0) == 't') {
                    if (MIPSTbl.regOrMem_trueIfReg(var)) {
                        mips_text.add(new MIPSCode.Move(a0, MIPSTbl.get_t_num(var)));
                    }
                    else {
                        int t_addr = MIPSTbl.get_t_addr(var);
                        mips_text.add(new MIPSCode.LW(a0, t_addr, 0));
                    }
                }
                else if (var.charAt(0) == '@') {
                    int g_addr = MIPSTbl.global_name2addr.get(var);
                    mips_text.add(new MIPSCode.LW(a0, g_addr, 0));
                }
                else if (var.charAt(0) == '%') {
                    if (MIPSTbl.regOrMem_trueIfReg(var)) {
                        mips_text.add(new MIPSCode.Move(a0, MIPSTbl.get_s_num(var)));
                    }
                    else {
                        int m_addr = MIPSTbl.main_name2addr.get(var);
                        mips_text.add(new MIPSCode.LW(a0, m_addr, 0));
                    }
                }
                else if (var.charAt(0) == '^' || var.charAt(0) == '!') {
                    int sp_offset = (var.charAt(0) == '^') ? MIPSTbl.func_name2offset.get(var) : MIPSTbl.get_para_name_2_sp_offset(var);
                    mips_text.add(new MIPSCode.LW(a0, sp_offset, MIPSTbl.sp));
                }
                else {
                    System.out.println("Something's wrong with _11_Q!!!");
                }
                mips_text.add(new MIPSCode.LI(v0, 1));
                mips_text.add(new MIPSCode.Sys());
            }
            else {
                // print %s
                mips_text.add(new MIPSCode.LA(a0, out.str_name));
                mips_text.add(new MIPSCode.LI(v0, 4));
                mips_text.add(new MIPSCode.Sys());
            }
        }
    }
}
