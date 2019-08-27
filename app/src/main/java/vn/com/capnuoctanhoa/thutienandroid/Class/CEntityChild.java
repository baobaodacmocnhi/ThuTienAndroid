package vn.com.capnuoctanhoa.thutienandroid.Class;

public class CEntityChild {
    private String MaHD;
    private String Ky;
    private String TongCong;
    private String ModifyDate;
    private String TinhTrang;
    private Boolean GiaiTrach;
    private Boolean TamThu;
    private Boolean ThuHo;
    private Boolean LenhHuy;
    private String PhiMoNuoc;
    private Boolean DaThu;

    public CEntityChild() {
        MaHD = "";
        Ky = "";
        TongCong = "";
        ModifyDate="";
        TinhTrang="";
        GiaiTrach = false;
        TamThu =false;
        ThuHo = false;
        LenhHuy = false;
        PhiMoNuoc="";
        DaThu = false;
    }

    public String getMaHD() {
        return MaHD;
    }

    public void setMaHD(String maHD) {
        MaHD = maHD;
    }

    public String getKy() {
        return Ky;
    }

    public void setKy(String ky) {
        Ky = ky;
    }

    public String getTongCong() {
        return TongCong;
    }

    public void setTongCong(String tongCong) {
        TongCong = tongCong;
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

    public String getPhiMoNuoc() {
        return PhiMoNuoc;
    }

    public void setPhiMoNuoc(String phiMoNuoc) {
        PhiMoNuoc = phiMoNuoc;
    }

    public Boolean getDaThu() {
        return DaThu;
    }

    public void setDaThu(Boolean daThu) {
        DaThu = daThu;
    }
}