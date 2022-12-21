package FrontEnd.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileSource {
    private final static int EOF = -1;
    private String sources = "";
    private final ArrayList<Integer> line2char = new ArrayList<>();

    public FileSource(File testFile) {
        try {
            InputStreamReader reader =
                    new InputStreamReader(new FileInputStream(testFile));
            BufferedReader bf = new BufferedReader(reader);
            line2char.add(0);
            String str;
            while ((str = bf.readLine()) != null) {
                sources += str;
                sources += "\n";
                line2char.add(sources.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获得待处理字符串sources总长度
    public int getEnd() {
        return sources.length();
    }

    // 给定char在文件中的位置，返回其所在行数
    public int getLineNum(int charIndex) {
        for (int line = 1; line < line2char.size(); line++) {
            if (charIndex < line2char.get(line))
                return line;
        }
        return EOF;
    }

    // 构造新的Token实例时使用，需要读取文件对应位置字符
    // new Token(**source**, kind, start, len);
    public char getNthChar(int n) {
        if (n >= sources.length()) return (char)(EOF);
        return sources.charAt(n);
    }

}
