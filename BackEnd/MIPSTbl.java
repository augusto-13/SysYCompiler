package BackEnd;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MIPSTbl {

    public static int global_address = 0x10010000;
    public static int main_var_address = 0x10040000;
    public static final HashMap<String, Integer> global_name2addr = new HashMap<>();
    public static final HashMap<String, Integer> main_name2addr = new HashMap<>();
    public static final HashMap<String, Integer> func_name2offset = new HashMap<>();
    public static final ArrayList<String> func_paraName = new ArrayList<>();
    public static final int[] t_reg_nums = new int[] {3, 5, 6, 7, 16, 17, 18, 19, 8, 20, 21, 22, 23, 24, };
    public static final int[] s_reg_nums = new int[] {9, 10, 11, 12, 13, 14, 15, 25, 30};
    public static final int ZERO = 0;
    public static final int AT = 1;
    public static final int v0 = 2;
    public static final int a0 = 4;
    public static final int t0 = 26;
    public static final int t1 = 27;
    public static final int t2 = 28;
    public static final int sp = 29;
    public static final int ra = 31;
    public static int sp_offset = 0;


    private static final int total_s_reg = s_reg_nums.length;
    private static int used_s_reg = 0;

    public static void initNewFuncDecl() {
        func_paraName.clear();
        func_name2offset.clear();
        sp_offset = 0;
    }

    public static int get_para_name_2_sp_offset(String name) {
        int k = func_paraName.size();
        int i = 0;
        for (; i < k; i++) {
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
    public static final HashMap<String, Integer> tName2tAddr = new HashMap<>();
    public static final ArrayList<Integer> t_allocated_addrs = new ArrayList<>();
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

    public static boolean regOrMem_trueIfReg(String name) {
        // 临时变量
        if (tName2tNum.containsKey(name)) return true;
        // 已分配寄存器
        else if (sName2sNum.containsKey(name)) return true;
        return false;
    }

    public static int get_s_num(String name) {
        return sName2sNum.get(name);
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

    public static int allocate_t_addr(String name) {
        int t_addr = global_address;
        while (true) {
            if (t_allocated_addrs.contains(t_addr)) {
                t_addr += 4;
                continue;
            }
            t_allocated_addrs.add(t_addr);
            tName2tAddr.put(name, t_addr);
            System.out.println(String.format("0x%x is allocated!!!", t_addr));
            return t_addr;
        }
    }

    public static int get_t_num(String name) {
        if (tName2tNum.containsKey(name)) {
            int ret_reg_num = tName2tNum.get(name);
            release_t(name);
            System.out.println(ret_reg_num + " is released!!!");
            return ret_reg_num;
        } else {
            return -1;
        }
    }

    public static int get_t_addr(String name) {
        int ret_addr = tName2tAddr.get(name);
        release_t(name);
        System.out.println(String.format("0x%x is free for other t's!!!"));
        return ret_addr;
    }

    public static void release_t(String name) {
        if (name.charAt(0) == 't') {
            if (tName2tNum.containsKey(name)) {
                int t_num = tName2tNum.get(name);
                tName2tNum.remove(name);
                t2occupied.put(t_num, false);
            }
            else {
                int t_addr = tName2tAddr.get(name);
                tName2tAddr.remove(name);
                t_allocated_addrs.remove(t_addr);
            }
        }
    }

    public static HashMap<String, Integer> tNum2push() {
        HashMap<String, Integer> ret = new HashMap<>();
        for (Map.Entry<String, Integer> tName_tNum : tName2tNum.entrySet()) {
            ret.put(tName_tNum.getKey(), tName_tNum.getValue());
            t2occupied.put(tName_tNum.getValue(), false);
        }
        tName2tNum.clear();
        return ret;
    }

    public static HashMap<String, Integer> tAddr2push() {
        HashMap<String, Integer> ret = new HashMap<>();
        ret.putAll(tName2tAddr);
        tName2tAddr.clear();
        t_allocated_addrs.clear();
        return ret;
    }

    public static void restoreAll_tNum(HashMap<String, Integer> tMap) {
        for (Map.Entry<String, Integer> tName_tNum : tMap.entrySet()) {
            t2occupied.put(tName_tNum.getValue(), true);
            tName2tNum.put(tName_tNum.getKey(), tName_tNum.getValue());
        }
    }

    public static void restoreAll_tAddr(HashMap<String, Integer> tMap) {
        for (Map.Entry<String, Integer> tName_tAddr : tMap.entrySet()) {
            tName2tAddr.put(tName_tAddr.getKey(), tName_tAddr.getValue());
            t_allocated_addrs.add(tName_tAddr.getValue());
        }
    }

}
