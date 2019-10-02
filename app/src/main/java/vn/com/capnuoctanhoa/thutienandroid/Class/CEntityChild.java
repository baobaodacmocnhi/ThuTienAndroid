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
    private String PhiMoNuocThuHo;
    private Boolean DangNgan_DienThoai;
    private String XoaDangNgan_Ngay_DienThoai;
    private String InPhieuBao_Ngay;
    private String InPhieuBao2_Ngay;
    private String InPhieuBao2_NgayHen;
    private String TBDongNuoc_Ngay;
    private String TBDongNuoc_NgayHen;

    public CEntityChild() {
        MaHD = "";
        Ky = "";
        TongCong = "";
        ModifyDate = "";
        ThuHo = false;
        TamThu = false;
        GiaiTrach = false;
        LenhHuy = false;
        TinhTrang = "";
        PhiMoNuoc = "";
        PhiMoNuocThuHo = "";
        DangNgan_DienThoai = false;
        XoaDangNgan_Ngay_DienThoai = "";
        InPhieuBao_Ngay = "";
        InPhieuBao2_Ngay = "";
        InPhieuBao2_NgayHen = "";
        TBDongNuoc = false;
        TBDongNuoc_Ngay = "";
        TBDongNuoc_NgayHen = "";
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

    public String getPhiMoNuocThuHo() {
        return PhiMoNuocThuHo;
    }

    public void setPhiMoNuocThuHo(String phiMoNuocThuHo) {
        PhiMoNuocThuHo = phiMoNuocThuHo;
    }

    public Boolean getDangNgan_DienThoai() {
        return DangNgan_DienThoai;
    }

    public void setDangNgan_DienThoai(Boolean dangNgan_DienThoai) {
        DangNgan_DienThoai = dangNgan_DienThoai;
    }

    public String getXoaDangNgan_Ngay_DienThoai() {
        return XoaDangNgan_Ngay_DienThoai;
    }

    public void setXoaDangNgan_Ngay_DienThoai(String xoaDangNgan_Ngay_DienThoai) {
        XoaDangNgan_Ngay_DienThoai = xoaDangNgan_Ngay_DienThoai;
    }

    public String getInPhieuBao_Ngay() {
        return InPhieuBao_Ngay;
    }

    public void setInPhieuBao_Ngay(String inPhieuBao_Ngay) {
        InPhieuBao_Ngay = inPhieuBao_Ngay;
    }

    public String getInPhieuBao2_Ngay() {
        return InPhieuBao2_Ngay;
    }

    public void setInPhieuBao2_Ngay(String inPhieuBao2_Ngay) {
        InPhieuBao2_Ngay = inPhieuBao2_Ngay;
    }

    public String getInPhieuBao2_NgayHen() {
        return InPhieuBao2_NgayHen;
    }

    public void setInPhieuBao2_NgayHen(String inPhieuBao2_NgayHen) {
        InPhieuBao2_NgayHen = inPhieuBao2_NgayHen;
    }

    public String getTBDongNuoc_Ngay() {
        return TBDongNuoc_Ngay;
    }

    public void setTBDongNuoc_Ngay(String TBDongNuoc_Ngay) {
        this.TBDongNuoc_Ngay = TBDongNuoc_Ngay;
    }

    public String getTBDongNuoc_NgayHen() {
        return TBDongNuoc_NgayHen;
    }

    public void setTBDongNuoc_NgayHen(String TBDongNuoc_NgayHen) {
        this.TBDongNuoc_NgayHen = TBDongNuoc_NgayHen;
    }

    public Boolean getTBDongNuoc() {
        return TBDongNuoc;
    }

    public void setTBDongNuoc(Boolean TBDongNuoc) {
        this.TBDongNuoc = TBDongNuoc;
    }


}