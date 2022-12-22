package FrontEnd.IRGenerator.Quadruple;

import BackEnd.MIPSCode;

import java.util.ArrayList;

// 四元式列表抽象类
public class IRCode {
    public void toData(StringBuilder mips_data) {}
    public void toText(String type, ArrayList<MIPSCode> mips_text) {}
}
