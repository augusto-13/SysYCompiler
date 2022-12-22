package BackEnd;

public abstract class MIPSCode {

    public static class Comment extends MIPSCode {
        public final String s;

        public Comment(String s) {
            this.s = s;
        }

        @Override
        public String toString() {return "# " + s;}
    }

    public static class LoadImm extends MIPSCode {
        public final int reg;
        public final int imm;

        public LoadImm(int reg, int imm) {
            this.reg = reg;
            this.imm = imm;
        }

        @Override
        public String toString() {
            return "li $" + reg + ", " + imm + "\n";
        }
    }

    public static class RegImm extends MIPSCode {

    }
    
    public static class Label extends MIPSCode {
        final String l;

        public Label(String l) {
            this.l = l;
        }

        @Override
        public String toString() {
            return l + ":\n";
        }
    }

    public static class Enter extends MIPSCode {
        public Enter() {}

        @Override
        public String toString() {
            return "\n";
        }
    }
}
