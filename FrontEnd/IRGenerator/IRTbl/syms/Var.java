package FrontEnd.IRGenerator.IRTbl.syms;

import FrontEnd.IRGenerator.Quadruple.Elements.LVal;
import java.util.ArrayList;

// 对于数字：也作为isConst变量存储在Var类中
// 对于临时变量：只需要isConst, name,  value三个域
// 对于常量：
// 对于普通变量：
// 对于形式参数：

public class Var {

    String name;
    boolean isConst = false;


    // 因为只有当'定义'或'形参'时，'[]'的含义是定义数组，其余情况'[]'含义均为取值。
    boolean isFParam = false;
    boolean init = false;

    // isFParam || isDecl
    // dim
    int dim;
    int d1;
    int d2;

    // isConst
    int const_value;
    final ArrayList<Integer> const_value_arr = new ArrayList<>();

    // global
    boolean isGlobal;
    int g_init_val;
    final ArrayList<Integer> g_init_arr = new ArrayList<>();

    // !isConst & init
    Var init_value;
    final ArrayList<Var> init_value_arr = new ArrayList<>();

    // !isConst & dim != 0 & !FParam
    // i, j: 取值位置实参
    // i * d2 + j: 最终取值位置实参
    // 对于a[i][j], "i * d2 + j" 外部提前计算好，传入`最终取值位置实参` index ( Var中保存 )
    // index_1, index_2两个布尔类型变量表示: 是否存在`取值位置实参`
    boolean index_1 = false;
    boolean index_2 = false;
    Var index;

    // Constructors
    // 1.
    // dim-1, dim-2 const
    // dim-1, dim-2 var with init
    public Var(String type, String name, int dim, int d1, int d2, ArrayList value_arr) {
        if (type.equals("const")) {
            this.name = name;
            this.isConst = true;
            this.dim = dim;
            this.d1 = d1;
            this.d2 = d2;
            this.const_value_arr.addAll(value_arr);
            this.init = true;
        }
        if (type.equals("var")) {
            this.name = name;
            this.dim = dim;
            this.d1 = d1;
            this.d2 = d2;
            this.init_value_arr.addAll(value_arr);
            this.init = true;
        }
        if (type.equals("global")) {
            this.name = name;
            this.isGlobal = true;
            this.dim = dim;
            this.d1 = d1;
            this.d2 = d2;
            this.g_init_arr.addAll(value_arr);
            this.init = true;
        }
    }

    // 2.
    // dim-0 const
    public Var(String type, String name, int value) {
        if (type.equals("const")) {
            this.name = name;
            this.isConst = true;
            this.dim = 0;
            this.const_value = value;
            this.init = true;
        }
        if (type.equals("global")) {
            this.name = name;
            this.isGlobal = true;
            this.dim = 0;
            this.g_init_val = value;
            this.init = true;
        }
    }

    // 3.
    // dim-0 var with init
    public Var(String type, String name, Var value) {
        if (type.equals("var")) {
            this.name = name;
            this.dim = 0;
            this.init = true;
            this.init_value = value;
        }
    }

    // 4.
    // dim-0 var without init
    // RET
    public Var(String type, String name) {
        if (type.equals("var")) {
            this.name = name;
            this.dim = 0;
        }
    }

    // 5.
    // dim-1, dim-2 var without init
    public Var(String type, String name, int dim, int d1, int d2) {
        if (type.equals("var")) {
            this.name = name;
            this.isConst = false;
            this.dim = dim;
            this.d1 = d1;
            this.d2 = d2;
        }
    }

    // 6.
    // arr[index1] / arr[index1][index_2]
    // ATTENTION: "name" 是数组本身的标识符
    public Var(String type, Var arr, int index_dim, Var index_cal) {
        if (type.equals("arr_val")) {
            this.name = arr.getName();
            this.isConst = arr.isConst;
            this.dim = arr.dim;
            this.d1 = arr.d1;
            this.d2 = arr.d2;
            if (isConst) this.const_value_arr.addAll(arr.const_value_arr);
            assert index_dim == 1 || index_dim == 2;
            if (index_dim == 2) index_2 = true;
            index_1 = true;
            index = index_cal;
        }
    }

    // 7.
    // fparam: a, a[], a[][7]
    public Var(String type, String name, int dim, int d2) {
        if (type.equals("param")) {
            this.name = name;
            this.dim = dim;
            this.d2 = d2;
            this.isFParam = true;
        }
    }

    // "IR->String" key methods
    // 只有"数组本身"与"序号"均为const时，才能直接返回值
    private String getValueStr() {
        assert isConst;
        if (dim == 0) return String.valueOf(const_value);
        if (dim == 1) {
            if (!index_1) return name;
        }
        if (dim == 2) {
            if (!index_1) return name;
            if (!index_2) {
                if (index.isConst) return String.format("&%s[%d]", name, index.const_value);
                return String.format("&%s[%s]", name, index.name);
            }
        }
        if (index.isConst) return String.valueOf(const_value_arr.get(index.const_value));
        return String.format("%s[%s]", name, index.name);
    }

    private String getStr() {
        if (dim == 0) return name;
        if (dim == 1) {
            if (!index_1) return name;
            return name + "[" + index + "]";
        }
        if (dim == 2) {
            if (!index_1) return name;
            if (!index_2) return "&" + name + "[" + index + "]";
            return name + "[" + index + "]";
        }
        return null;
    }

    @Override
    public String toString() {
        if (!isFParam) {
            if (isConst) {
                return getValueStr();
            } else {
                return getStr();
            }
        }
        // if FParam: Won't use it.
        // if Decl:
        return name;
    }

    // isConst
    // UnaryExp → UnaryOp(-)
    /* TODO==构建"DAG"图时需要特别注意这步操作 */
    public void negative() {
        assert isConst;
        const_value = -const_value;
    }

    // getter methods
    public int getD1() {
        return d1;
    }

    public int getD2() {
        return d2;
    }

    public ArrayList<Integer> getConst_value_arr() {
        return const_value_arr;
    }

    public ArrayList<Var> getInit_value_arr() {
        return init_value_arr;
    }

    public int getConst_value() {
        return const_value;
    }

    public Var getInit_value() {
        return init_value;
    }

    public int getDim() {
        return dim;
    }

    public String getName() {
        return name;
    }

    public boolean isConst() {
        return isConst;
    }

    public boolean isFParam() {
        return isFParam;
    }

    public boolean isTemp() {
        return name.equals("temp");
    }

    public LVal toLVal() {
        if (dim == 0) return new LVal(name);
        else if (index.isConst) {
            return new LVal(name, index.getConst_value());
        } else {
            return new LVal(name, index.getName());
        }
    }

    public int getValidInitVal() {
        return isConst ? const_value : g_init_val;
    }

    public ArrayList<Integer> getValidInitArr() {
        return isConst ? const_value_arr : g_init_arr;
    }

}
