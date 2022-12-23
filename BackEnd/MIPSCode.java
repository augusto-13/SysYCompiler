package BackEnd;

public abstract class MIPSCode {
    public static class LI extends MIPSCode {
         final int reg;
         final int imm;

        public LI(int reg, int imm) {
            this.reg = reg;
            this.imm = imm;
        }

        @Override
        public String toString() {
            return "li $" + reg + ", " + imm + "\n";
        }
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

    public static class LW extends MIPSCode {
        int reg;
        int imm;
        int reg2;

        public LW(int reg, int imm, int reg2) {
            this.reg = reg;
            this.imm = imm;
            this.reg2 = reg2;
        }

        @Override
        public String toString() {
            return String.format("lw $%d, 0x%x($%d)\n", reg, imm, reg2);
        }
    }

    public static class Cal_RR extends MIPSCode {
        int res;
        int reg1;
        int reg2;
        String op;

        public Cal_RR(int res, int reg1, String op, int reg2) {
            this.res = res;
            this.reg1 = reg1;
            this.reg2 = reg2;
            this.op = op;
        }

        @Override
        public String toString() {
            switch (op) {
                case "+":
                    return
            }
        }
    }
}
