package FrontEnd.errorChecker;

import java.io.File;
import java.io.FileWriter;

public class ErrorPair {
    public final ErrorKind errorKind;
    public final int errorLine;

    public ErrorPair(ErrorKind kind, int i) {
        errorLine = i;
        errorKind = kind;
    }

    public void print() {
        System.out.println(errorLine + " " + errorKind.toString());
    }

    public void printTo(File out) {
        try {
            FileWriter fw = new FileWriter(out, true);
            fw.write(errorLine + " " + errorKind.toString() + "\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}