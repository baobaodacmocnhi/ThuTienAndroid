package vn.com.capnuoctanhoa.thutienandroid.Class;

import java.util.ArrayList;

public class CEntityParent {
    private String ID;
    private String MLT;
    private String DanhBo;
    private String HoTen;
    private String DiaChi;
    private String ModifyDate;
    private Boolean ThuHo;
    private Boolean TamThu;
    private Boolean GiaiTrach;
    private Boolean TBDongNuoc;
    private Boolean LenhHuy;
    private String TinhTrang;
    private Boolean DangNgan_DienThoai;
    private Boolean DongNuoc;
    private Boolean DongNuoc2;
    private Boolean DongPhi;
    private Boolean MoNuoc;
    private String NgayDN;
    private String ChiSoDN;
    private Boolean ButChi;
    private Boolean KhoaTu;
    private String NiemChi;
    private Boolean KhoaKhac;
    private String KhoaKhac_GhiChu;
    private String Hieu;
    private String Co;
    private String SoThan;
    private String ChiMatSo;
    private String ChiKhoaGoc;
    private String ViTri;
    private String LyDo;
    private String NgayDN1;
    private String ChiSoDN1;
    private String NiemChi1;
    private String NgayMN;
    private String ChiSoMN;

    private ArrayList<CEntityChild> lstHoaDon;

    public CEntityParent() {
        ID = "";
        MLT = "";
        DanhBo = "";
        HoTen = "";
        DiaChi = "";
        ModifyDate = "";
        ThuHo = false;
        TamThu = false;
        GiaiTrach = false;
        LenhHuy = false;
        TinhTrang = "";
        DangNgan_DienThoai = false;
        TBDongNuoc = false;
        DongNuoc = false;
        DongNuoc2 = false;
        DongPhi = false;
        MoNuoc = false;
        NgayDN = "";
        ChiSoDN = "";
        ButChi = false;
        KhoaTu = false;
        NiemChi = "";
        KhoaKhac = false;
        KhoaKhac_GhiChu = "";
        Hieu = "";
        Co = "";
        SoThan = "";
        ChiMatSo = "";
        ChiKhoaGoc = "";
        ViTri="";
        LyDo = "";
        NgayDN1 = "";
        ChiSoDN1 = "";
        NiemChi1 = "";
        NgayMN="";
        ChiSoMN="";
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

    public Boolean getDangNgan_DienThoai() {
        return DangNgan_DienThoai;
    }

    public void setDangNgan_DienThoai(Boolean dangNgan_DienThoai) {
        DangNgan_DienThoai = dangNgan_DienThoai;
    }

    public Boolean getTBDongNuoc() {
        return TBDongNuoc;
    }

    public void setTBDongNuoc(Boolean TBDongNuoc) {
        this.TBDongNuoc = TBDongNuoc;
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

    public Boolean getDongNuoc2() {
        return DongNuoc2;
    }

    public void setDongNuoc2(Boolean dongNuoc2) {
        DongNuoc2 = dongNuoc2;
    }

    public String getNgayDN() {
        return NgayDN;
    }

    public void setNgayDN(String ngayDN) {
        NgayDN = ngayDN;
    }

    public String getChiSoDN() {
        return ChiSoDN;
    }

    public void setChiSoDN(String chiSoDN) {
        ChiSoDN = chiSoDN;
    }

    public Boolean getButChi() {
        return ButChi;
    }

    public void setButChi(Boolean butChi) {
        ButChi = butChi;
    }

    public Boolean getKhoaTu() {
        return KhoaTu;
    }

    public void setKhoaTu(Boolean khoaTu) {
        KhoaTu = khoaTu;
    }

    public String getNiemChi() {
        return NiemChi;
    }

    public void setNiemChi(String niemChi) {
        NiemChi = niemChi;
    }

    public Boolean getKhoaKhac() {
        return KhoaKhac;
    }

    public void setKhoaKhac(Boolean khoaKhac) {
        KhoaKhac = khoaKhac;
    }

    public String getKhoaKhac_GhiChu() {
        return KhoaKhac_GhiChu;
    }

    public void setKhoaKhac_GhiChu(String khoaKhac_GhiChu) {
        KhoaKhac_GhiChu = khoaKhac_GhiChu;
    }

    public String getHieu() {
        return Hieu;
    }

    public void setHieu(String hieu) {
        Hieu = hieu;
    }

    public String getCo() {
        return Co;
    }

    public void setCo(String co) {
        Co = co;
    }

    public String getSoThan() {
        return SoThan;
    }

    public void setSoThan(String soThan) {
        SoThan = soThan;
    }

    public String getChiMatSo() {
        return ChiMatSo;
    }

    public void setChiMatSo(String chiMatSo) {
        ChiMatSo = chiMatSo;
    }

    public String getChiKhoaGoc() {
        return ChiKhoaGoc;
    }

    public void setChiKhoaGoc(String chiKhoaGoc) {
        ChiKhoaGoc = chiKhoaGoc;
    }

    public String getViTri() {
        return ViTri;
    }

    public void setViTri(String viTri) {
        ViTri = viTri;
    }

    public String getLyDo() {
        return LyDo;
    }

    public void setLyDo(String lyDo) {
        LyDo = lyDo;
    }

    public String getNgayDN1() {
        return NgayDN1;
    }

    public void setNgayDN1(String ngayDN1) {
        NgayDN1 = ngayDN1;
    }

    public String getChiSoDN1() {
        return ChiSoDN1;
    }

    public void setChiSoDN1(String chiSoDN1) {
        ChiSoDN1 = chiSoDN1;
    }

    public String getNiemChi1() {
        return NiemChi1;
    }

    public void setNiemChi1(String niemChi1) {
        NiemChi1 = niemChi1;
    }

    public String getNgayMN() {
        return NgayMN;
    }

    public void setNgayMN(String ngayMN) {
        NgayMN = ngayMN;
    }

    public String getChiSoMN() {
        return ChiSoMN;
    }

    public void setChiSoMN(String chiSoMN) {
        ChiSoMN = chiSoMN;
    }

    public ArrayList<CEntityChild> getLstHoaDon() {
        return lstHoaDon;
    }

    public void setLstHoaDon(ArrayList<CEntityChild> lstHoaDon) {
        this.lstHoaDon = lstHoaDon;
    }
}
