package vn.com.capnuoctanhoa.thutienandroid.Class;

import java.util.ArrayList;

public class CViewParent {
    private String STT;
    private String ID;
    private String Row1a;
    private String Row1b;
    private String Row2a;
    private String Row2b;
    private String Row3a;
    private String Row3b;
    private String Row4a;
    private String Row4b;
    private Boolean GiaiTrach;
    private Boolean TamThu;
    private Boolean ThuHo;
    private Boolean TBDongNuoc;
    private Boolean LenhHuy;
    private Boolean DongNuoc;
    private Boolean DongNuoc2;
    private Boolean ToTrinh;
    private Boolean DCHD;
    private String ModifyDate;
    private ArrayList<CViewChild> listChild;
    //nộp tiền
    private boolean Chot;
    private String NgayChot;
    private String Loai;
    private String SoLuong;
    private String TongCong;

    public CViewParent() {
        this.STT = "";
        this.ID = "";
        Row1a = "";
        Row1b = "";
        Row2a = "";
        Row2b = "";
        Row3a = "";
        Row3b = "";
        Row4a = "";
        Row4b = "";
        GiaiTrach = false;
        TamThu = false;
        ThuHo = false;
        TBDongNuoc=false;
        LenhHuy = false;
        DongNuoc = false;
        DongNuoc2 = false;
        ToTrinh = false;
        DCHD = false;
        listChild = new ArrayList<CViewChild>();
        //nộp tiền
        Chot = false;
        NgayChot = "";
        Loai = "";
        SoLuong = "";
        TongCong = "";
    }

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

    public String getRow2a() {
        return Row2a;
    }

    public void setRow2a(String row2a) {
        Row2a = row2a;
    }

    public String getRow2b() {
        return Row2b;
    }

    public void setRow2b(String row2b) {
        Row2b = row2b;
    }

    public String getRow3a() {
        return Row3a;
    }

    public void setRow3a(String row3a) {
        Row3a = row3a;
    }

    public String getRow3b() {
        return Row3b;
    }

    public void setRow3b(String row3b) {
        Row3b = row3b;
    }

    public String getRow4a() {
        return Row4a;
    }

    public void setRow4a(String row4a) {
        Row4a = row4a;
    }

    public String getRow4b() {
        return Row4b;
    }

    public void setRow4b(String row4b) {
        Row4b = row4b;
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

    public Boolean getTBDongNuoc() {
        return TBDongNuoc;
    }

    public void setTBDongNuoc(Boolean TBDongNuoc) {
        this.TBDongNuoc = TBDongNuoc;
    }

    public Boolean getLenhHuy() {
        return LenhHuy;
    }

    public void setLenhHuy(Boolean lenhHuy) {
        LenhHuy = lenhHuy;
    }

    public Boolean getDongNuoc() {
        return DongNuoc;
    }

    public void setDongNuoc(Boolean dongNuoc) {
        DongNuoc = dongNuoc;
    }

    public Boolean getDongNuoc2() {
        return DongNuoc2;
    }

    public void setDongNuoc2(Boolean dongNuoc2) {
        DongNuoc2 = dongNuoc2;
    }

    public Boolean getToTrinh() {
        return ToTrinh;
    }

    public void setToTrinh(Boolean toTrinh) {
        ToTrinh = toTrinh;
    }

    public Boolean getDCHD() {
        return DCHD;
    }

    public void setDCHD(Boolean DCHD) {
        this.DCHD = DCHD;
    }

    public String getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(String modifyDate) {
        ModifyDate = modifyDate;
    }

    public ArrayList<CViewChild> getListChild() {
        return listChild;
    }

    public void setListChild(ArrayList<CViewChild> listChild) {
        this.listChild = listChild;
    }

    public int getItemChildCount() {
        return listChild.size();
    }

    //nộp tiền
    public boolean isChot() {
        return Chot;
    }

    public void setChot(boolean chot) {
        Chot = chot;
    }

    public String getNgayChot() {
        return NgayChot;
    }

    public void setNgayChot(String ngayChot) {
        NgayChot = ngayChot;
    }

    public String getLoai() {
        return Loai;
    }

    public void setLoai(String loai) {
        Loai = loai;
    }

    public String getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(String soLuong) {
        SoLuong = soLuong;
    }

    public String getTongCong() {
        return TongCong;
    }

    public void setTongCong(String tongCong) {
        TongCong = tongCong;
    }
}
