package vn.com.capnuoctanhoa.thutienandroid.Class;

import java.util.ArrayList;

public class CEntityParent {
    private String ID;
    private String MLT;
    private String DanhBo;
    private String HoTen;
    private String DiaChi;
    private String ModifyDate;
    private String TinhTrang;
    private Boolean GiaiTrach;
    private Boolean TamThu;
    private Boolean ThuHo;
    private Boolean LenhHuy;
    private Boolean DaThu;
    private Boolean DongNuoc;



    private Boolean DongPhi;
    private Boolean MoNuoc;
    private ArrayList<CEntityChild> lstHoaDon;

    public CEntityParent() {
        ID = "";
        MLT = "";
        DanhBo = "";
        HoTen ="";
        DiaChi = "";
        ModifyDate="";
        TinhTrang="";
        GiaiTrach = false;
        TamThu = false;
        ThuHo = false;
        LenhHuy = false;
        DaThu = false;
        DongNuoc=false;
        DongPhi=false;
        MoNuoc = false;
        lstHoaDon = new ArrayList<CEntityChild>();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMLT() {
        return MLT;
    }

    public void setMLT(String MLT) {
        this.MLT = MLT;
    }

    public String getDanhBo() {
        return DanhBo;
    }

    public void setDanhBo(String danhBo) {
        DanhBo = danhBo;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(String modifyDate) {
        ModifyDate = modifyDate;
    }

    public String getTinhTrang() {
        return TinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        TinhTrang = tinhTrang;
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

    public Boolean getLenhHuy() {
        return LenhHuy;
    }

    public void setLenhHuy(Boolean lenhHuy) {
        LenhHuy = lenhHuy;
    }

    public Boolean getDaThu() {
        return DaThu;
    }

    public void setDaThu(Boolean daThu) {
        DaThu = daThu;
    }

    public Boolean getDongNuoc() {
        return DongNuoc;
    }

    public void setDongNuoc(Boolean dongNuoc) {
        DongNuoc = dongNuoc;
    }

    public Boolean getDongPhi() {
        return DongPhi;
    }

    public void setDongPhi(Boolean dongPhi) {
        DongPhi = dongPhi;
    }

    public Boolean getMoNuoc() {
        return MoNuoc;
    }

    public void setMoNuoc(Boolean moNuoc) {
        MoNuoc = moNuoc;
    }

    public ArrayList<CEntityChild> getLstHoaDon() {
        return lstHoaDon;
    }

    public void setLstHoaDon(ArrayList<CEntityChild> lstHoaDon) {
        this.lstHoaDon = lstHoaDon;
    }
}
