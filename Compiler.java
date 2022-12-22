import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import BackEnd.IRTranslator;
import FrontEnd.IRGenerator.IRGenerator;
import FrontEnd.IRGenerator.IROptimizer;
import FrontEnd.IRGenerator.Quadruple.IRCode;
import FrontEnd.errorChecker.ErrorHandler;
import FrontEnd.lexer.*;
import FrontEnd.parser.*;
import FrontEnd.nodes.*;


public class Compiler {

    private static Node root;
    private static boolean noError = true;
    private static ArrayList<IRCode> irCodes;

    private static void init(File out) {
        try {
            if (!out.exists()) out.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileWriter fw = new FileWriter(out);
            fw.write("");
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void execFrontEnd(int level) {
        if (level < 1 || level > 5) {
            System.out.println("Illegal level value");
            return;
        }
        // level 1: FrontEnd.lexer
        File in = new File("testfile.txt");
        File out = new File("output.txt");
        init(out);
        FileSource fileSrc = new FileSource(in);
        Lexer lexer = new Lexer(fileSrc);
        ArrayList<Token> tokens = lexer.tokenize();
        if (lexer.hasError()) {
            lexer.printError();
            return;
        }
        if (level == 1) return;

        // level 2: FrontEnd.parser
        TokenSource tokenSrc = new TokenSource(tokens);
        Parser parser = new Parser(tokenSrc);
        root = parser.parse();
        root.printTo(out);
        if (level == 2) return;

        // level 3: FrontEnd.errorHandler
        File err = new File("error.txt");
        init(err);
        ErrorHandler errorHandler = new ErrorHandler(root, err);
        if (errorHandler.hasError()) {
            System.out.println("There's something wrong with your code.\nPlease check \"error.txt\".\nStop at level 3.");
            noError = false;
            return;
        }
        if (level == 3) return;

        // level 4: irGeneration
        File ir = new File("20373493_李逸卓_优化前中间代码.txt");
        init(ir);
        IRGenerator irGenerator = new IRGenerator(root);
        irGenerator.printTo(ir);
        if (level == 4) return;

        // level 5: irOptimization
        File ir_ = new File("20373493_李逸卓_优化后中间代码.txt");
        init(ir_);
        IROptimizer irOptimizer = new IROptimizer();
        irOptimizer.printTo(ir_);
    }

    private static void execBackEnd(int level) {
        if (level == 0) return;
        File mips = new File("mips.txt");
        init(mips);
        IRTranslator irTranslator = new IRTranslator();
        irTranslator.printTo(mips);
        if (level == 1) return;
    }

    public static void main(String[] args) {
        int frontLevel = 4;
        int backLevel = 1;
        execFrontEnd(frontLevel);
        if (!noError) return;
        execBackEnd(backLevel);
    }
}
