package vn.com.capnuoctanhoa.thutienandroid;

import android.graphics.Color;

public class CViewEntityChild {
    private String ID;
    private String Row1a;
    private String Row1b;
    private Boolean GiaiTrach;
    private Boolean TamThu;
    private Boolean ThuHo;

    public CViewEntityChild() {
        this.ID = "";
        Row1a = "";
        Row1b = "";
        GiaiTrach = false;
        TamThu = false;
        ThuHo = false;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRow1a() {
        return Row1a;
    }

    public void setRow1a(String row1a) {
        Row1a = row1a;
    }

    public String getRow1b() {
        return Row1b;
    }

    public void setRow1b(String row1b) {
        Row1b = row1b;
    }

    public Boolean getGiaiTrach() {
        return GiaiTrach;
    }

    public void setGiaiTrach(Boolean giaiTrach) {
        GiaiTrach = giaiTrach;
    }

    public Boolean getTamThu() {
        return TamThu;
    }

    public void setTamThu(Boolean tamThu) {
        TamThu = tamThu;
    }

    public Boolean getThuHo() {
        return ThuHo;
    }

    public void setThuHo(Boolean thuHo) {
        ThuHo = thuHo;
    }
}
