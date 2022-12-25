package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;

import java.util.ArrayList;

import static BackEnd.MIPSTbl.v0;

public class _8_FuncRet_Q extends IRCode {
    boolean ret_void;
    boolean ret_isVal;
    String ret_var;
    int ret_val;

    public _8_FuncRet_Q() {
        ret_void = true;
    }

    public _8_FuncRet_Q(String ret_var) {
        ret_void = false;
        ret_isVal = false;
        this.ret_var = ret_var;
    }

    public _8_FuncRet_Q(int ret_val) {
        ret_void = false;
        ret_isVal = true;
        this.ret_val = ret_val;
    }

    @Override
    public String toString() {
        return ret_void ? "ret\n" : ret_isVal ? String.format("ret %d\n", ret_val) : String.format("ret %s\n", ret_var);
    }

    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        // OK!!!
        if (type.equals("func")) {
            if (!ret_void) {
                if (ret_isVal) {
                    mips_text.add(new MIPSCode.LI(v0, ret_val));
                }
                else {
                    if (ret_var.charAt(0) == 't') {
                        if (MIPSTbl.regOrMem_trueIfReg(ret_var)) {
                            mips_text.add(new MIPSCode.Move(v0, MIPSTbl.get_t_num(ret_var)));
                        }
                        else {
                            int t_addr = MIPSTbl.get_t_addr(ret_var);
                            mips_text.add(new MIPSCode.LW(v0, t_addr, 0));
                        }
                    }
                    else if (ret_var.charAt(0) == '@') {
                        int g_addr = MIPSTbl.global_name2addr.get(ret_var);
                        mips_text.add(new MIPSCode.LW(v0, g_addr, 0));
                    }
                    else if (ret_var.charAt(0) == '%') {
                        if (MIPSTbl.regOrMem_trueIfReg(ret_var)) {
                            mips_text.add(new MIPSCode.Move(v0, MIPSTbl.get_s_num(ret_var)));
                        }
                        else {
                            int m_addr = MIPSTbl.main_name2addr.get(ret_var);
                            mips_text.add(new MIPSCode.LW(v0, m_addr, 0));
                        }
                    }
                    else if (ret_var.charAt(0) == '^' || ret_var.charAt(0) == '!') {
                        int sp_offset = (ret_var.charAt(0) == '^') ? MIPSTbl.func_name2offset.get(ret_var) : MIPSTbl.get_para_name_2_sp_offset(ret_var);
                        mips_text.add(new MIPSCode.LW(v0, sp_offset, MIPSTbl.sp));
                    }
                    else {
                        System.out.println("Something's wrong with _8_Q!!!");
                    }
                }
            }
            mips_text.add(new MIPSCode.JR());
        }
        else {
            mips_text.add(new MIPSCode.LI(v0, 10));
            mips_text.add(new MIPSCode.Sys());
        }
    }
}
