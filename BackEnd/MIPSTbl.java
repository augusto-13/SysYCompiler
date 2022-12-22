package BackEnd;

import java.util.HashMap;

public class MIPSTbl {

    public static int global_offset = 0;
    public static int main_var_offset = 0;
    // 全局变量、常量存储地址相对于`0x10010000`的偏移位(未*4)
    // (带@前缀)
    public static final HashMap<String, Integer> global_name2offset = new HashMap<>();
    // 主函数中局部变量、常量存储地址相对于`0x10040000`的偏移位(未*4)
    // (带%前缀)
    public static final HashMap<String, Integer> main_name2offset = new HashMap<>();
    // 函数名=标签
    // public static final HashMap<String, String> funcName2label = new HashMap<>();

    public static final int[] t_reg_nums = new int[] {3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 24, 25, 26, 27, 28};
    public static final int[] s_reg_nums = new int[] {16, 17, 18, 19, 20, 21, 22, 23};

    private static final int total_s_reg = s_reg_nums.length;
    private static int used_s_reg = 0;
    public static final HashMap<Integer, Boolean> t2occupied = new HashMap<Integer, Boolean>() {
        {
            for (int t_reg_num : t_reg_nums) {
                put(t_reg_num, false);
            }
        }
    };

    public static final HashMap<String, Integer> tName2tNum = new HashMap<>();
    public static final HashMap<String, Integer> sName2sNum = new HashMap<>();

    private static boolean isTemp(String name) {
        return name.charAt(0) == 't';
    }

    public static boolean regOrMem_trueIfReg(String name) {
        // 临时变量
        if (isTemp(name)) return true;
        // 已分配寄存器
        else if (sName2sNum.containsKey(name)) return true;
        // 尚未分配寄存器，但是仍有空闲寄存器
        else if (used_s_reg < total_s_reg) return true;
        return false;
    }

    public static int getRegNum(String name) {
        if (isTemp(name)) return get_t_num(name);
        else return get_s_num(name);
    }

    public static int get_t_num(String name) {
        if (tName2tNum.containsKey(name)) return tName2tNum.get(name);
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

    public static int get_s_num(String name) {
        if (sName2sNum.containsKey(name)) return sName2sNum.get(name);
        sName2sNum.put(name, s_reg_nums[used_s_reg]);
        used_s_reg++;
        return sName2sNum.get(name);
    }

    public static void release(String name) {
        if (name.charAt(0) == 't') {
            int t_num = tName2tNum.get(name);
            tName2tNum.remove(name);
            t2occupied.put(t_num, false);
        }
    }



}
