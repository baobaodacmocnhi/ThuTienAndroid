package vn.com.capnuoctanhoa.thutienandroid;

import android.graphics.Color;

public class CViewEntity {
    private String STT;
    private String ID;
    private String Name1;
    private String Name2;
    private String Content;
    private int BackgroundColor=Color.TRANSPARENT;

    public String getSTT() {
        return STT;
    }

    public void setSTT(String STT) {
        this.STT = STT;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName1() {
        return Name1;
    }

    public void setName1(String name1) {
        Name1 = name1;
    }

    public String getName2() {
        return Name2;
    }

    public void setName2(String name2) {
        Name2 = name2;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getBackgroundColor() {
        return BackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        BackgroundColor = backgroundColor;
    }
}
