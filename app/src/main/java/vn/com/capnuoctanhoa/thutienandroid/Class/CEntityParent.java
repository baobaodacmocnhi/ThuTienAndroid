package vn.com.capnuoctanhoa.thutienandroid.Class;

import java.util.ArrayList;

public class CEntityParent {
    private String ID;
    private String MLT;
    private String DanhBo;
    private String HoTen;
    private String DiaChi;
    private String DiaChiDHN;
    private String ModifyDate;
    private boolean ThuHo;
    private boolean TamThu;
    private boolean GiaiTrach;
    private boolean TBDongNuoc;
    private boolean LenhHuy;
    private boolean LenhHuyCat;
    private String TinhTrang;
    private boolean DangNgan_DienThoai;
    private boolean InPhieuBao;
    private boolean InPhieuBao2;
    private boolean DongNuoc;
    private boolean DongNuoc2;
    private String MaKQDN;
    private boolean DongPhi;
    private boolean MoNuoc;
    private String NgayDN;
    private String ChiSoDN;
    private boolean ButChi;
    private boolean KhoaTu;
    private String NiemChi;
    private boolean KhoaKhac;
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
    private boolean Sync;
    private String XuLy;
    private ArrayList<CEntityChild> lstHoaDon;
    private boolean DCHD;
    private boolean XoaDCHD;
    private boolean DongA;
    private boolean UpdatedHDDT;

    public CEntityParent() {
        ID = "";
        MLT = "";
        DanhBo = "";
        HoTen = "";
        DiaChiDHN="";
        DiaChi = "";
        ModifyDate = "";
        ThuHo = false;
        TamThu = false;
        GiaiTrach = false;
        LenhHuy = false;
        LenhHuyCat = false;
        TinhTrang = "";
        DangNgan_DienThoai = false;
        InPhieuBao = false;
        InPhieuBao2 = false;
        TBDongNuoc = false;
        DongNuoc = false;
        DongNuoc2 = false;
        MaKQDN = "";
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
        ViTri = "";
        LyDo = "";
        NgayDN1 = "";
        ChiSoDN1 = "";
        NiemChi1 = "";
        NgayMN = "";
        ChiSoMN = "";
        Sync = false;
        XuLy = "";
        lstHoaDon = new ArrayList<CEntityChild>();
        DCHD = false;
        XoaDCHD = false;
        DongA = false;
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

    public String getDiaChiDHN() {
        return DiaChiDHN;
    }

    public void setDiaChiDHN(String diaChiDHN) {
        DiaChiDHN = diaChiDHN;
    }

    public String getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(String modifyDate) {
        ModifyDate = modifyDate;
    }

    public boolean isThuHo() {
        return ThuHo;
    }

    public void setThuHo(boolean thuHo) {
        ThuHo = thuHo;
    }

    public boolean isTamThu() {
        return TamThu;
    }

    public void setTamThu(boolean tamThu) {
        TamThu = tamThu;
    }

    public boolean isGiaiTrach() {
        return GiaiTrach;
    }

    public void setGiaiTrach(boolean giaiTrach) {
        GiaiTrach = giaiTrach;
    }

    public boolean isTBDongNuoc() {
        return TBDongNuoc;
    }

    public void setTBDongNuoc(boolean TBDongNuoc) {
        this.TBDongNuoc = TBDongNuoc;
    }

    public boolean isLenhHuy() {
        return LenhHuy;
    }

    public void setLenhHuy(boolean lenhHuy) {
        LenhHuy = lenhHuy;
    }

    public boolean isLenhHuyCat() {
        return LenhHuyCat;
    }

    public void setLenhHuyCat(boolean lenhHuyCat) {
        LenhHuyCat = lenhHuyCat;
    }

    public boolean isDangNgan_DienThoai() {
        return DangNgan_DienThoai;
    }

    public void setDangNgan_DienThoai(boolean dangNgan_DienThoai) {
        DangNgan_DienThoai = dangNgan_DienThoai;
    }

    public boolean isInPhieuBao() {
        return InPhieuBao;
    }

    public void setInPhieuBao(boolean inPhieuBao) {
        InPhieuBao = inPhieuBao;
    }

    public boolean isInPhieuBao2() {
        return InPhieuBao2;
    }

    public void setInPhieuBao2(boolean inPhieuBao2) {
        InPhieuBao2 = inPhieuBao2;
    }

    public boolean isDongNuoc() {
        return DongNuoc;
    }

    public void setDongNuoc(boolean dongNuoc) {
        DongNuoc = dongNuoc;
    }

    public boolean isDongNuoc2() {
        return DongNuoc2;
    }

    public void setDongNuoc2(boolean dongNuoc2) {
        DongNuoc2 = dongNuoc2;
    }

    public String getMaKQDN() {
        return MaKQDN;
    }

    public void setMaKQDN(String maKQDN) {
        MaKQDN = maKQDN;
    }

    public boolean isDongPhi() {
        return DongPhi;
    }

    public void setDongPhi(boolean dongPhi) {
        DongPhi = dongPhi;
    }

    public boolean isMoNuoc() {
        return MoNuoc;
    }

    public void setMoNuoc(boolean moNuoc) {
        MoNuoc = moNuoc;
    }

    public boolean isButChi() {
        return ButChi;
    }

    public void setButChi(boolean butChi) {
        ButChi = butChi;
    }

    public boolean isKhoaTu() {
        return KhoaTu;
    }

    public void setKhoaTu(boolean khoaTu) {
        KhoaTu = khoaTu;
    }

    public boolean isKhoaKhac() {
        return KhoaKhac;
    }

    public void setKhoaKhac(boolean khoaKhac) {
        KhoaKhac = khoaKhac;
    }

    public String getTinhTrang() {
        return TinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        TinhTrang = tinhTrang;
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

    public String getNiemChi() {
        return NiemChi;
    }

    public void setNiemChi(String niemChi) {
        NiemChi = niemChi;
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

    public boolean isSync() {
        return Sync;
    }

    public void setSync(boolean sync) {
        Sync = sync;
    }

    public String getXuLy() {
        return XuLy;
    }

    public void setXuLy(String xuLy) {
        XuLy = xuLy;
    }

    public ArrayList<CEntityChild> getLstHoaDon() {
        return lstHoaDon;
    }

    public void setLstHoaDon(ArrayList<CEntityChild> lstHoaDon) {
        this.lstHoaDon = lstHoaDon;
    }

    public boolean isDCHD() {
        return DCHD;
    }

    public void setDCHD(boolean DCHD) {
        this.DCHD = DCHD;
    }

    public boolean isXoaDCHD() {
        return XoaDCHD;
    }

    public void setXoaDCHD(boolean checkTienDu) {
        this.XoaDCHD = checkTienDu;
    }

    public boolean isDongA() {
        return DongA;
    }

    public void setDongA(boolean dongA) {
        DongA = dongA;
    }

    public void setCEntityParent(CEntityParent entityParent) {
        ID = entityParent.getID();
        MLT = entityParent.getMLT();
        DanhBo = entityParent.getDanhBo();
        HoTen = entityParent.getHoTen();
        DiaChi = entityParent.getDiaChi();
        DiaChiDHN = entityParent.getDiaChiDHN();
        ModifyDate = entityParent.getModifyDate();
        ThuHo = entityParent.isThuHo();
        TamThu = entityParent.isTamThu();
        GiaiTrach = entityParent.isGiaiTrach();
        LenhHuy = entityParent.isLenhHuy();
        LenhHuyCat = entityParent.isLenhHuyCat();
        TinhTrang = entityParent.getTinhTrang();
        DangNgan_DienThoai = entityParent.isDangNgan_DienThoai();
        TBDongNuoc = entityParent.isTBDongNuoc();
        DongNuoc = entityParent.isDongNuoc();
        DongNuoc2 = entityParent.isDongNuoc2();
        DongPhi = entityParent.isDongPhi();
        MoNuoc = entityParent.isMoNuoc();
        NgayDN = entityParent.getNgayDN();
        ChiSoDN = entityParent.getChiSoDN();
        ButChi = entityParent.isButChi();
        KhoaTu = entityParent.isKhoaTu();
        NiemChi = entityParent.getNiemChi();
        KhoaKhac = entityParent.isKhoaKhac();
        KhoaKhac_GhiChu = entityParent.getKhoaKhac_GhiChu();
        Hieu = entityParent.getHieu();
        Co = entityParent.getCo();
        SoThan = entityParent.getSoThan();
        ChiMatSo = entityParent.getChiMatSo();
        ChiKhoaGoc = entityParent.getChiKhoaGoc();
        ViTri = entityParent.getViTri();
        LyDo = entityParent.getLyDo();
        NgayDN1 = entityParent.getNgayDN1();
        ChiSoDN1 = entityParent.getChiSoDN1();
        NiemChi1 = entityParent.getNiemChi1();
        NgayMN = entityParent.getNgayMN();
        ChiSoMN = entityParent.getChiSoMN();
        Sync = entityParent.isSync();
        XuLy = entityParent.getXuLy();
        lstHoaDon = entityParent.getLstHoaDon();
        DCHD = entityParent.isDCHD();
        XoaDCHD = entityParent.isXoaDCHD();
        DongA = entityParent.isDongA();
    }

}
