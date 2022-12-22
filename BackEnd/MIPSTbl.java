package BackEnd;

import java.util.HashMap;

public class MIPSTbl {

    public static int global_offset = 0;
    public static int main_var_offset = 0;
    // 全局变量、常量、字符串存储地址相对于`0x10010000`的偏移位(未*4)
    // (带@前缀)
    public static final HashMap<String, Integer> global_name2offset = new HashMap<>();
    // 主函数中局部变量、常量、字符串存储地址相对于`0x10040000`的偏移位(未*4)
    // (带%前缀)
    public static final HashMap<String, Integer> main_name2offset = new HashMap<>();
}
