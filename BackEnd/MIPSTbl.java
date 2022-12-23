package BackEnd;

import java.util.ArrayList;
import java.util.HashMap;

public class MIPSTbl {

    public static int global_address = 0x10010000;
    public static int main_var_address = 0x10040000;
    public static final HashMap<String, Integer> global_name2addr = new HashMap<>();
    public static final HashMap<String, Integer> main_name2addr = new HashMap<>();
    public static final HashMap<String, Integer> func_name2offset = new HashMap<>();
    public static final ArrayList<String> func_paraName = new ArrayList<>();
    public static final int[] t_reg_nums = new int[] {3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 24, 25, 26};
    public static final int[] s_reg_nums = new int[] {16, 17, 18, 19, 20, 21, 22, 23, 30};
    public static int sp_offset = 0;
    public static final int sp = 29;
    public static final int ra = 31;
    public static final int t0 = 27;
    public static final int t1 = 28;


    private static final int total_s_reg = s_reg_nums.length;
    private static int used_s_reg = 0;

    public static int get_para_name_2_sp_offset(String name) {
        int k = func_paraName.size();
        for (int i = 0; i < k; i++) {
            if (func_paraName.get(i).equals(name)) break;
        }
        assert i < k;
        return (k - i) << 2;
    }

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
    public static boolean allocate_s_reg(String name) {
        if (used_s_reg >= total_s_reg) return false;
        sName2sNum.put(name, s_reg_nums[used_s_reg]);
        used_s_reg++;
        return true;
    }
    public static int allocate_t_reg(String name) {
        for (Integer i : t2occupied.keySet()) {
            if (!t2occupied.get(i)) {
                t2occupied.put(i, true);
                tName2tNum.put(name, i);
                System.out.println(i + " is occupied!!!");
                return i;
            }
        }
        System.out.println("Something's wrong with t_reg allocation!");
        return -1;
    }
    public static boolean regOrMem_trueIfReg(String name) {
        // 临时变量
        if (isTemp(name)) return true;
        // 已分配寄存器
        else if (sName2sNum.containsKey(name)) return true;
        return false;
    }

    public static int get_t_num(String name) {
        int ret_reg_num = tName2tNum.get(name);
        release_t(name);
        return ret_reg_num;
    }

    public static int get_s_num(String name) {
        return sName2sNum.get(name);
    }

    public static void release_t(String name) {
        if (name.charAt(0) == 't') {
            int t_num = tName2tNum.get(name);
            tName2tNum.remove(name);
            t2occupied.put(t_num, false);
        }
    }



}
