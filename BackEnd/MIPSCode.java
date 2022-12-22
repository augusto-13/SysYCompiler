package BackEnd;

public abstract class MIPSCode {
    public static class LoadImm extends MIPSCode {
         final int reg;
         final int imm;

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

    public static class Sys extends MIPSCode {
        public Sys() {}

        @Override
        public String toString() {
            return "syscall\n";
        }
    }

    public static class LA extends MIPSCode {
        int reg;
        boolean add_str = false;
        int add;
        String add_;

        public LA(int reg, int add) {
            this.reg = reg;
            this.add = add;
        }

        public LA(int reg, String add) {
            add_str = true;
            this.reg = reg;
            this.add_ = add;
        }

        @Override
        public String toString() {
            return add_str ? String.format("la $%d, %s\n", reg, add_) : String.format("la $%d, 0x%x\n", reg, add);
        }
    }
}
