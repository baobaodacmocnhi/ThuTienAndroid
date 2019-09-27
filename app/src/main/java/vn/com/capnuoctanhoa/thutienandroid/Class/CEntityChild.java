package vn.com.capnuoctanhoa.thutienandroid.Class;

public class CEntityChild {
    private String MaHD;
    private String Ky;
    private String TongCong;
    private String ModifyDate;
    private Boolean ThuHo;
    private Boolean TamThu;
    private Boolean GiaiTrach;
    private Boolean TBDongNuoc;
    private Boolean LenhHuy;
    private String TinhTrang;
    private String PhiMoNuoc;
    private Boolean DangNgan_DienThoai;
    private Boolean InPhieuBao_DienThoai;
    private String InPhieuBao_Ngay_DienThoai;
    private Boolean XoaDangNgan_DienThoai;
    private String XoaDangNgan_Ngay_DienThoai;
    private String TBDongNuoc_Ngay;

    public CEntityChild() {
        MaHD = "";
        Ky = "";
        TongCong = "";
        ModifyDate="";
        ThuHo = false;
        TamThu =false;
        GiaiTrach = false;
        LenhHuy = false;
        TinhTrang="";
        PhiMoNuoc="";
        DangNgan_DienThoai = false;
        InPhieuBao_DienThoai=false;
        InPhieuBao_Ngay_DienThoai="";
        XoaDangNgan_DienThoai=false;
        XoaDangNgan_Ngay_DienThoai="";
        TBDongNuoc=false;
        TBDongNuoc_Ngay="";
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

    public Boolean getDangNgan_DienThoai() {
        return DangNgan_DienThoai;
    }

    public void setDangNgan_DienThoai(Boolean dangNgan_DienThoai) {
        DangNgan_DienThoai = dangNgan_DienThoai;
    }

    public Boolean getInPhieuBao_DienThoai() {
        return InPhieuBao_DienThoai;
    }

    public void setInPhieuBao_DienThoai(Boolean inPhieuBao_DienThoai) {
        InPhieuBao_DienThoai = inPhieuBao_DienThoai;
    }

    public String getInPhieuBao_Ngay_DienThoai() {
        return InPhieuBao_Ngay_DienThoai;
    }

    public void setInPhieuBao_Ngay_DienThoai(String inPhieuBao_Ngay_DienThoai) {
        InPhieuBao_Ngay_DienThoai = inPhieuBao_Ngay_DienThoai;
    }

    public Boolean getXoaDangNgan_DienThoai() {
        return XoaDangNgan_DienThoai;
    }

    public void setXoaDangNgan_DienThoai(Boolean xoaDangNgan_DienThoai) {
        XoaDangNgan_DienThoai = xoaDangNgan_DienThoai;
    }

    public String getXoaDangNgan_Ngay_DienThoai() {
        return XoaDangNgan_Ngay_DienThoai;
    }

    public void setXoaDangNgan_Ngay_DienThoai(String xoaDangNgan_Ngay_DienThoai) {
        XoaDangNgan_Ngay_DienThoai = xoaDangNgan_Ngay_DienThoai;
    }

    public Boolean getTBDongNuoc() {
        return TBDongNuoc;
    }

    public void setTBDongNuoc(Boolean TBDongNuoc) {
        this.TBDongNuoc = TBDongNuoc;
    }

    public String getTBDongNuoc_Ngay() {
        return TBDongNuoc_Ngay;
    }

    public void setTBDongNuoc_Ngay(String TBDongNuoc_Ngay) {
        this.TBDongNuoc_Ngay = TBDongNuoc_Ngay;
    }
}