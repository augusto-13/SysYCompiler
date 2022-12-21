package FrontEnd.IRGenerator.IRTbl;

import java.util.ArrayList;

public class Frame_tbl {
    int type;
    String name;
    ArrayList<Entry_tbl> frame;


    public Frame_tbl(int type, String name) {
        this.type = type;
        this.name = name;
        frame = new ArrayList<>();
    }

    public int getType() {return type;}

    public Entry_tbl findName(String name) {
        for (Entry_tbl entry : frame) {
            if (entry.getName().equals(name)) return entry;
        }
        return null;
    }

    public void addEntry(Entry_tbl e) {frame.add(e);}
}