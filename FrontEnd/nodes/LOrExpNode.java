package FrontEnd.nodes;

import FrontEnd.IRGenerator.IRCodes;
import FrontEnd.IRGenerator.IRContext;
import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.IRTbl.syms.Var;
import FrontEnd.IRGenerator.Quadruple._12_Label_Q;

import java.util.ArrayList;

public class LOrExpNode extends Node {
    public LOrExpNode() {}

    @Override
    public Var genIR() {
        // 有问题：
        // LOrExpNode内的genIR应该只处理逻辑关系
        // 也就是说，我只需要为其传入“跳转至标签”的继承属性
        // 而具体的跳转代码应由底层genIR函数来完成
        // 每一层语句中的已知量都是bnz_label, bez_label
        // 含义是该条件满足/不满足时的跳转至标签
        // 根据指令的具体上下文来决定是否跳转，以及跳转至哪个标签
        ArrayList<Node> children = getChildren();
        int size = children.size();
        boolean prev_jump_if = IRContext.jump_if;
        String prev_else_label = IRContext.else_label;
        if (size == 1) {
            // LOrExp  →  LAndExp
            children.get(0).genIR();
        }
        else if (size == 3) {
            // LOrExp  →  LOrExp  '||' <1> LAndExp
            // 对于右侧的LOrExp:
            // 如果其值为0，不跳转，进入下一分支<1>；成立，则跳转，进入if分支
            // LAndExp:
            // 如果其值为0，跳转，goto条件语句else分支；成立，则跳转，进入if分支
            // 如果考虑嵌套结构
            // LOrExp  →  LOrExp  '||'  LAndExp  '||'  LAndExp  '||'  LAndExp
            String label1 = IRGenerator.genLabel();
            IRContext.jump_if = true;
            IRContext.else_label = label1;
            children.get(0).genIR();
            IRCodes.addIRCode_ori(new _12_Label_Q(label1));
            IRContext.jump_if = prev_jump_if;
            IRContext.else_label = prev_else_label;
            children.get(2).genIR();
//            String prev_bnz_label = IRContext.bnz_label;
//            String end_label = IRGenerator.genLabel();
//            IRContext.bnz_label = end_label;
//            Var orV = children.get(0).genIR();
//            if (orV.isConst()) {
//                if (orV.getConst_value() == 0) {
//                    Var andV = children.get(2).genIR();
//                    if (andV.isConst()) {
//                        if (andV.getConst_value() == 0) {
//                            IRCodes.addIRCode_ori(new _13_Jump_Q("goto", IRContext.bez_label));
//                        }
//                    }
//                    else {
//                        IRCodes.addIRCode_ori(new _13_Jump_Q("bez", andV.getName(), IRContext.bez_label));
//                    }
//                }
//                else {
//                    IRCodes.addIRCode_ori(new _13_Jump_Q("goto", end_label));
//                }
//            }
//            else {
//                IRCodes.addIRCode_ori(new _13_Jump_Q("bnz", orV.getName(), end_label));
//                Var andV = children.get(2).genIR();
//                if (andV.isConst()) {
//                    if (andV.getConst_value() == 0) {
//                        IRCodes.addIRCode_ori(new _13_Jump_Q("goto", IRContext.bez_label));
//                    }
//                }
//                else {
//                    IRCodes.addIRCode_ori(new _13_Jump_Q("bez", andV.getName(), IRContext.bez_label));
//                }
//            }
//            IRCodes.addIRCode_ori(new _12_Label_Q(end_label));
//            IRContext.bnz_label = prev_bnz_label;
        }
        return null;
    }
}
