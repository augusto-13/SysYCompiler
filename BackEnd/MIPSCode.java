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
        public Enter() {
        }

        @Override
        public String toString() {
            return "\n";
        }
    }

    public static class Sys extends MIPSCode {
        public Sys() {
        }

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
                    return String.format("addu $%d, $%d, $%d\n", res, reg1, reg2);
                case "-":
                    return String.format("subu $%d, $%d, $%d\n", res, reg1, reg2);
                case "*":
                    return String.format("mult $%d $%d\nmflo $%d\n", reg1, reg2, res);
                case "/":
                    return String.format("div $%d, $%d\nmflo $%d\n", reg1, reg2, res);
                case "%":
                    return String.format("div $%d, $%d\nmfhi $%d\n", reg1, reg2, res);
                default:
                    return String.format("%s $%d, $%d, $%d\n", op, res, reg1, reg2);
            }
        }
    }

    public static class Cal_RI extends MIPSCode {
        int res;
        int reg;
        int imm;
        String op;

        public Cal_RI(int res, int reg, String op, int imm) {
            this.res = res;
            this.reg = reg;
            this.imm = imm;
            this.op = op;
        }

        @Override
        public String toString() {
            switch (op) {
                case "+":
                    return String.format("addiu $%d, $%d, %d\n", res, reg, imm);
                case "-":
                    return String.format("subiu $%d, $%d, %d\n", res, reg, imm);
                case "<<":
                    return String.format("sll $%d, $%d, %d\n", res, reg, imm);
                default:
                    return String.format("%s $%d, $%d, %d\n", op, reg, imm, res);
            }
        }
    }

    public static class Move extends MIPSCode {
        int to;
        int from;

        public Move(int to, int from) {
            this.to = to;
            this.from = from;
        }

        @Override
        public String toString() {
            return String.format("move $%d, $%d\n", to, from);
        }
    }
}
