package BackEnd;

import sun.util.resources.cldr.zh.CalendarData_zh_Hans_HK;

import java.util.HashMap;

public class MIPSTbl {

    public static int global_offset = 0;
    public static int main_var_offset = 0;
    // 全局变量、常量、字符串存储地址相对于`0x10010000`的偏移位(未*4)
    // (带@前缀)
    public static final HashMap<String, Integer> global_name2offset = new HashMap<>();
    // 主函数中局部变量、常量存储地址相对于`0x10040000`的偏移位(未*4)
    // (带%前缀)
    public static final HashMap<String, Integer> main_name2offset = new HashMap<>();
    // 函数名=标签
    // public static final HashMap<String, String> funcName2label = new HashMap<>();

    public static final int[] t_reg_nums = new int[] {3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 24, 25, 26, 27, 28};
    public static final int[] s_reg_nums = new int[] {16, 17, 18, 19, 20, 21, 22, 23};
    public static final HashMap<Integer, Boolean> t2occupied = new HashMap<Integer, Boolean>() {
        {
            for (int t_reg_num : t_reg_nums) {
                put(t_reg_num, false);
            }
        }
    };
    public static final HashMap<String, Integer> tName2tNum = new HashMap<>();

    public static boolean regOrMem_trueIfReg(String name) {
        if (name.charAt(0) == 't') return true;
        return false;
    }

    public static int get_t_num(String name) {
        for (Integer i : t2occupied.keySet()) {
            if (!t2occupied.get(i)) {
                t2occupied.put(i, true);
                tName2tNum.put(name, i);
                return i;
            }
        }
        System.out.println("Something's wrong with t_reg allocation!");
        return -1;
    };

    public static void release_t(String name) {
        if (name.charAt(0) == 't') {
            int t_num = tName2tNum.get(name);
            tName2tNum.remove(name);
            t2occupied.put(t_num, false);
        }
    }



}
