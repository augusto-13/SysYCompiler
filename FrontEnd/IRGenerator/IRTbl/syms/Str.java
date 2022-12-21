package FrontEnd.IRGenerator.IRTbl.syms;

public class Str extends Sym {
    String name;
    String content;

    public Str(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return name;
    }

}
