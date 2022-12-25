package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;
import BackEnd.MIPSTbl;

import java.util.ArrayList;
import java.util.HashMap;

public class _13_Jump_Q extends IRCode {
    final static HashMap<String, Integer> type_str2int = new HashMap<String, Integer>() {
        {
            put("goto", 0);
            put("bez", 1);
            put("bnz", 2);
        }
    };
    final static HashMap<Integer, String> type_int2str = new HashMap<Integer, String>() {
        {
            put(0, "goto");
            put(1, "bez");
            put(2, "bnz");
        }
    };
    int type;
    String label;
    String arg;

    public _13_Jump_Q(String t, String l) {
        type = type_str2int.get(t);
        label = l;
    }

    public _13_Jump_Q(String t, String l, String a) {
        type = type_str2int.get(t);
        label = l;
        arg = a;
    }

    // OK!!!
    @Override
    public String toString() {
        return type == 0 ? String.format("goto %s\n", label) : String.format("%s %s %s\n", type_int2str.get(type), arg, label);
    }

    @Override
    public void toText(String type, ArrayList<MIPSCode> mips_text) {
        if (this.type == 0) {
            mips_text.add(new MIPSCode.J(label));
        }
        else {
            // gist: 将arg存入寄存器
            if (arg.charAt(0) == 't') {
                if (MIPSTbl.regOrMem_trueIfReg(arg)) {
                    write(MIPSTbl.get_t_num(arg), mips_text);
                }
                else {
                    int t_addr = MIPSTbl.get_t_addr(arg);
                    mips_text.add(new MIPSCode.LW(MIPSTbl.t0, t_addr, 0));
                    write(MIPSTbl.t0, mips_text);
                }
            }
            else if (arg.charAt(0) == '@') {
                int g_addr = MIPSTbl.global_name2addr.get(arg);
                mips_text.add(new MIPSCode.LW(MIPSTbl.t0, g_addr, 0));
                write(MIPSTbl.t0, mips_text);
            }
            else if (arg.charAt(0) == '%') {
                if (MIPSTbl.regOrMem_trueIfReg(arg)) {
                    write(MIPSTbl.get_s_num(arg), mips_text);
                }
                else {
                    int m_addr = MIPSTbl.main_name2addr.get(arg);
                    mips_text.add(new MIPSCode.LW(MIPSTbl.t0, m_addr, 0));
                    write(MIPSTbl.t0, mips_text);
                }
            }
            else if (arg.charAt(0) == '^' || arg.charAt(0) == '!') {
                int sp_offset = (arg.charAt(0) == '^') ? MIPSTbl.func_name2offset.get(arg) : MIPSTbl.get_para_name_2_sp_offset(arg);
                mips_text.add(new MIPSCode.LW(MIPSTbl.t0, sp_offset, MIPSTbl.sp));
                write(MIPSTbl.t0, mips_text);
            }
            else {
                System.out.println("Something's wrong with _13_Q!!!");
            }
        }
    }

    private void write(int reg_num, ArrayList<MIPSCode> mips_text) {
        if (this.type == 1) mips_text.add(new MIPSCode.BEZ(reg_num, label));
        else mips_text.add(new MIPSCode.BNZ(reg_num, label));
    }
}
