package FrontEnd.IRGenerator.Quadruple.Elements;

public class Param {
    boolean isArr;
    String name;
    boolean dim_is1;
    int d2;

    public Param(String name) {
        isArr = false;
        this.name = name;
    }

    public Param(String name, int d) {
        isArr = true;
        this.name = name;
        if (d == 0) {
            dim_is1 = true;
        } else {
            dim_is1 = false;
            d2 = d;
        }
    }

    @Override
    public String toString() {
        return !isArr ? String.format("int %s", name) : dim_is1 ? String.format("arr %s[]", name) : String.format("arr %s[][%d]", name, d2);
    }
}
