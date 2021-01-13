package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.text.Editable;

import androidx.annotation.Nullable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by user on 22/03/2018.
 */

public class CWebservice {
    private final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    private final String SOAP_ADDRESS = "http://113.161.88.180:1989/wsThuTien.asmx";
//  private final String SOAP_ADDRESS = "http://192.168.90.11:81/wsThuTien_test.asmx";

    @Nullable
    private String excute(SoapObject request, String SOAP_ACTION) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS, 1000 * 60 * 3);
        Object response = null;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }
        return response.toString();
    }

    @Nullable
    private SoapObject excuteReturnTable(SoapObject request, String SOAP_ACTION) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        SoapObject response = null;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            response = (SoapObject) envelope.bodyIn;
        } catch (Exception exception) {
            return null;
        }
        response = (SoapObject) response.getProperty(0);
        response = (SoapObject) response.getProperty(1);
        try {
            response = (SoapObject) response.getProperty(0);
        } catch (Exception ex) {
            return null;
        }
        return response;
    }

    public String dangNhaps(String Username, String Password, String IDMobile, String UID) {
        String SOAP_ACTION = "http://tempuri.org/DangNhaps";
        String OPERATION_NAME = "DangNhaps";
//        String SOAP_ACTION = "http://tempuri.org/DangNhaps_Admin";
//        String OPERATION_NAME = "DangNhaps_Admin";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("Username");
        pi.setValue(Username);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Password");
        pi.setValue(Password);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("IDMobile");
        pi.setValue(IDMobile);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("UID");
        pi.setValue(UID);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String dangXuats(String Username, String UID) {
        String SOAP_ACTION = "http://tempuri.org/DangXuats";
        String OPERATION_NAME = "DangXuats";
//        String SOAP_ACTION = "http://tempuri.org/DangXuats_Admin";
//        String OPERATION_NAME = "DangXuats_Admin";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("Username");
        pi.setValue(Username);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("UID");
        pi.setValue(UID);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String dangXuats_Person(String Username, String UID) {
        String SOAP_ACTION = "http://tempuri.org/DangXuats_Person";
        String OPERATION_NAME = "DangXuats_Person";
//        String SOAP_ACTION = "http://tempuri.org/DangXuats_Admin";
//        String OPERATION_NAME = "DangXuats_Admin";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("Username");
        pi.setValue(Username);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("UID");
        pi.setValue(UID);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String updateUID(String MaNV, String UID) {
        String SOAP_ACTION = "http://tempuri.org/UpdateUID";
        String OPERATION_NAME = "UpdateUID";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaNV");
        pi.setValue(MaNV);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("UID");
        pi.setValue(UID);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String updateLogin(String MaNV, String UID) {
        String SOAP_ACTION = "http://tempuri.org/updateLogin";
        String OPERATION_NAME = "updateLogin";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaNV");
        pi.setValue(MaNV);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("UID");
        pi.setValue(UID);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getVersion() {
        String SOAP_ACTION = "http://tempuri.org/GetVersion";
        String OPERATION_NAME = "GetVersion";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        return excute(request, SOAP_ACTION);
    }

    public String getDSTo() {
        String SOAP_ACTION = "http://tempuri.org/GetDSTo";
        String OPERATION_NAME = "GetDSTo";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        return excute(request, SOAP_ACTION);
    }

    public String getDS_NhanVien_HanhThu() {
        String SOAP_ACTION = "http://tempuri.org/getDS_NhanVien_HanhThu";
        String OPERATION_NAME = "getDS_NhanVien_HanhThu";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        return excute(request, SOAP_ACTION);
    }

    public String getDSNhanVienTo(String MaTo) {
        String SOAP_ACTION = "http://tempuri.org/GetDSNhanVienTo";
        String OPERATION_NAME = "GetDSNhanVienTo";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaTo");
        pi.setValue(MaTo);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getDS_NhanVien() {
        String SOAP_ACTION = "http://tempuri.org/getDS_NhanVien";
        String OPERATION_NAME = "getDS_NhanVien";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        return excute(request, SOAP_ACTION);
    }

    //hành thu
    public String getDSHoaDonTon_NhanVien(String MaNV, String Nam, String Ky, String FromDot, String ToDot) {
        String SOAP_ACTION = "http://tempuri.org/getDSHoaDonTon_NhanVien";
        String OPERATION_NAME = "getDSHoaDonTon_NhanVien";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaNV");
        pi.setValue(MaNV);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Nam");
        pi.setValue(Nam);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Ky");
        pi.setValue(Ky);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("FromDot");
        pi.setValue(FromDot);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToDot");
        pi.setValue(ToDot);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getDSHoaDonTon_May(String MaNV, String Nam, String Ky, String FromDot, String ToDot,String TuMay,String DenMay) {
        String SOAP_ACTION = "http://tempuri.org/getDSHoaDonTon_May";
        String OPERATION_NAME = "getDSHoaDonTon_May";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaNV");
        pi.setValue(MaNV);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Nam");
        pi.setValue(Nam);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Ky");
        pi.setValue(Ky);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("FromDot");
        pi.setValue(FromDot);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToDot");
        pi.setValue(ToDot);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("TuMay");
        pi.setValue(TuMay);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("DenMay");
        pi.setValue(DenMay);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String XuLy_HoaDonDienTu(String LoaiXuLy, String MaNV, String MaHDs, String Ngay, String NgayHen, String MaKQDN, String XoaDCHD, String Location) {
        String SOAP_ACTION = "http://tempuri.org/XuLy_HoaDonDienTu";
        String OPERATION_NAME = "XuLy_HoaDonDienTu";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("LoaiXuLy");
        pi.setValue(LoaiXuLy);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("MaNV");
        pi.setValue(MaNV);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("MaHDs");
        pi.setValue(MaHDs);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Ngay");
        pi.setValue(Ngay);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("NgayHen");
        pi.setValue(NgayHen);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("MaKQDN");
        pi.setValue(MaKQDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("XoaDCHD");
        pi.setValue(XoaDCHD);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Location");
        pi.setValue(Location);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String get_GhiChu(String DanhBo) {
        String SOAP_ACTION = "http://tempuri.org/get_GhiChu";
        String OPERATION_NAME = "get_GhiChu";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("DanhBo");
        pi.setValue(DanhBo);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String update_GhiChu(String MaNV, String DanhBo, String DienThoai, String GiaBieu, String NiemChi, String DiemBe) {
        String SOAP_ACTION = "http://tempuri.org/update_GhiChu";
        String OPERATION_NAME = "update_GhiChu";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaNV");
        pi.setValue(MaNV);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("DanhBo");
        pi.setValue(DanhBo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("DienThoai");
        pi.setValue(DienThoai);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("GiaBieu");
        pi.setValue(GiaBieu);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("NiemChi");
        pi.setValue(NiemChi);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("DiemBe");
        pi.setValue(DiemBe);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String update_DiaChiDHN(String MaNV, String DanhBo, String DiaChiDHN) {
        String SOAP_ACTION = "http://tempuri.org/update_DiaChiDHN";
        String OPERATION_NAME = "update_DiaChiDHN";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaNV");
        pi.setValue(MaNV);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("DanhBo");
        pi.setValue(DanhBo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("DiaChiDHN");
        pi.setValue(DiaChiDHN);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    //tạm thu
    public String getDSTamThu(String RutSot, String MaNV, String FromCreateDate, String ToCreateDate) {
        String SOAP_ACTION = "http://tempuri.org/GetDSTamThu";
        String OPERATION_NAME = "GetDSTamThu";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("RutSot");
        pi.setValue(RutSot);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("MaNV");
        pi.setValue(MaNV);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("FromCreateDate");
        pi.setValue(FromCreateDate);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToCreateDate");
        pi.setValue(ToCreateDate);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    //đóng nước
    public String getDSDongNuoc(String MaNV_DongNuoc) {
        String SOAP_ACTION = "http://tempuri.org/GetDSDongNuoc";
        String OPERATION_NAME = "GetDSDongNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaNV_DongNuoc");
        pi.setValue(MaNV_DongNuoc);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getDSDongNuoc(String MaNV_DongNuoc, String FromNgayGiao, String ToNgayGiao) {
        String SOAP_ACTION = "http://tempuri.org/GetDSDongNuoc";
        String OPERATION_NAME = "GetDSDongNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaNV_DongNuoc");
        pi.setValue(MaNV_DongNuoc);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("FromNgayGiao");
        pi.setValue(FromNgayGiao);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToNgayGiao");
        pi.setValue(ToNgayGiao);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getDSCTDongNuoc(String MaNV_DongNuoc) {
        String SOAP_ACTION = "http://tempuri.org/GetDSCTDongNuoc";
        String OPERATION_NAME = "GetDSCTDongNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaNV_DongNuoc");
        pi.setValue(MaNV_DongNuoc);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getDSCTDongNuoc(String MaNV_DongNuoc, String FromNgayGiao, String ToNgayGiao) {
        String SOAP_ACTION = "http://tempuri.org/GetDSCTDongNuoc";
        String OPERATION_NAME = "GetDSCTDongNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaNV_DongNuoc");
        pi.setValue(MaNV_DongNuoc);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("FromNgayGiao");
        pi.setValue(FromNgayGiao);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToNgayGiao");
        pi.setValue(ToNgayGiao);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String checkExist_DongNuoc(String MaDN) {
        String SOAP_ACTION = "http://tempuri.org/CheckExist_DongNuoc";
        String OPERATION_NAME = "CheckExist_DongNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaDN");
        pi.setValue(MaDN);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String checkExist_DongNuoc2(String MaDN) {
        String SOAP_ACTION = "http://tempuri.org/CheckExist_DongNuoc2";
        String OPERATION_NAME = "CheckExist_DongNuoc2";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaDN");
        pi.setValue(MaDN);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String themDongNuoc(String MaDN, String DanhBo, String MLT, String HoTen, String DiaChi, String HinhDN, String NgayDN, String ChiSoDN,
                               String ButChi, String KhoaTu, String NiemChi, String KhoaKhac, String KhoaKhac_GhiChu,
                               String Hieu, String Co, String SoThan, String ChiMatSo, String ChiKhoaGoc, String ViTri, String LyDo, String CreateBy) {
        String SOAP_ACTION = "http://tempuri.org/ThemDongNuoc";
        String OPERATION_NAME = "ThemDongNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaDN");
        pi.setValue(MaDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("DanhBo");
        pi.setValue(DanhBo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("MLT");
        pi.setValue(MLT);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("HoTen");
        pi.setValue(HoTen);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("DiaChi");
        pi.setValue(DiaChi);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("HinhDN");
        pi.setValue(HinhDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("NgayDN");
        pi.setValue(NgayDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ChiSoDN");
        pi.setValue(ChiSoDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ButChi");
        pi.setValue(ButChi);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("KhoaTu");
        pi.setValue(KhoaTu);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("KhoaKhac");
        pi.setValue(KhoaKhac);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("KhoaKhac_GhiChu");
        pi.setValue(KhoaKhac_GhiChu);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("NiemChi");
        pi.setValue(NiemChi);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Hieu");
        pi.setValue(Hieu);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Co");
        pi.setValue(Co);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("SoThan");
        pi.setValue(SoThan);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ChiMatSo");
        pi.setValue(ChiMatSo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ChiKhoaGoc");
        pi.setValue(ChiKhoaGoc);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ViTri");
        pi.setValue(ViTri);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("LyDo");
        pi.setValue(LyDo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("CreateBy");
        pi.setValue(CreateBy);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String themDongNuoc2(String MaDN, String HinhDN, String NgayDN, String ChiSoDN,
                                String ButChi, String KhoaTu, String NiemChi, String KhoaKhac, String KhoaKhac_GhiChu, String CreateBy) {
        String SOAP_ACTION = "http://tempuri.org/ThemDongNuoc2";
        String OPERATION_NAME = "ThemDongNuoc2";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaDN");
        pi.setValue(MaDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("HinhDN");
        pi.setValue(HinhDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("NgayDN");
        pi.setValue(NgayDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ChiSoDN");
        pi.setValue(ChiSoDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ButChi");
        pi.setValue(ButChi);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("KhoaTu");
        pi.setValue(KhoaTu);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("NiemChi");
        pi.setValue(NiemChi);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("KhoaKhac");
        pi.setValue(KhoaKhac);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("KhoaKhac_GhiChu");
        pi.setValue(KhoaKhac_GhiChu);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("CreateBy");
        pi.setValue(CreateBy);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String checkExist_MoNuoc(String MaDN) {
        String SOAP_ACTION = "http://tempuri.org/CheckExist_MoNuoc";
        String OPERATION_NAME = "CheckExist_MoNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaDN");
        pi.setValue(MaDN);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String themMoNuoc(String MaDN, String HinhMN, String NgayMN, String ChiSoMN, String CreateBy) {
        String SOAP_ACTION = "http://tempuri.org/ThemMoNuoc";
        String OPERATION_NAME = "ThemMoNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaDN");
        pi.setValue(MaDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("HinhMN");
        pi.setValue(HinhMN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("NgayMN");
        pi.setValue(NgayMN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ChiSoMN");
        pi.setValue(ChiSoMN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("CreateBy");
        pi.setValue(CreateBy);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String dangNganDongNuoc(String MaNV, String MaHDs) {
        String SOAP_ACTION = "http://tempuri.org/DangNganDongNuoc";
        String OPERATION_NAME = "DangNganDongNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaNV");
        pi.setValue(MaNV);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("MaHDs");
        pi.setValue(MaHDs);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getDSHoaDonTon_DongNuoc(String DanhBo, String MaHDs) {
        String SOAP_ACTION = "http://tempuri.org/GetDSHoaDonTon_DongNuoc";
        String OPERATION_NAME = "GetDSHoaDonTon_DongNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("DanhBo");
        pi.setValue(DanhBo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("MaHDs");
        pi.setValue(MaHDs);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    //tìm kiếm
    public String getDSTimKiem(String DanhBo) {
        String SOAP_ACTION = "http://tempuri.org/GetDSTimKiem";
        String OPERATION_NAME = "GetDSTimKiem";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("DanhBo");
        pi.setValue(DanhBo);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getDSTimKiemTTKH(String HoTen, String SoNha, String TenDuong) {
        String SOAP_ACTION = "http://tempuri.org/GetDSTimKiemTTKH";
        String OPERATION_NAME = "GetDSTimKiemTTKH";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("HoTen");
        pi.setValue(HoTen);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("SoNha");
        pi.setValue(SoNha);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("TenDuong");
        pi.setValue(TenDuong);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    //quản lý
    public String getTongGiaoHoaDon(String MaTo, String Nam, String Ky, String FromDot, String ToDot) {
        String SOAP_ACTION = "http://tempuri.org/GetTongGiaoHoaDon";
        String OPERATION_NAME = "GetTongGiaoHoaDon";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaTo");
        pi.setValue(MaTo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Nam");
        pi.setValue(Nam);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Ky");
        pi.setValue(Ky);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("FromDot");
        pi.setValue(FromDot);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToDot");
        pi.setValue(ToDot);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getTongDangNgan(String MaTo, String FromNgayGiaiTrach, String ToNgayGiaiTrach) {
        String SOAP_ACTION = "http://tempuri.org/GetTongDangNgan";
        String OPERATION_NAME = "GetTongDangNgan";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaTo");
        pi.setValue(MaTo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("FromNgayGiaiTrach");
        pi.setValue(FromNgayGiaiTrach);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToNgayGiaiTrach");
        pi.setValue(ToNgayGiaiTrach);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getTongTon_DenKy(String MaTo, String Nam, String Ky, String FromDot, String ToDot) {
        String SOAP_ACTION = "http://tempuri.org/GetTongTon_DenKy";
        String OPERATION_NAME = "GetTongTon_DenKy";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaTo");
        pi.setValue(MaTo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Nam");
        pi.setValue(Nam);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Ky");
        pi.setValue(Ky);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("FromDot");
        pi.setValue(FromDot);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToDot");
        pi.setValue(ToDot);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getTongTon_TrongKy(String MaTo, String Nam, String Ky, String FromDot, String ToDot) {
        String SOAP_ACTION = "http://tempuri.org/GetTongTon_TrongKy";
        String OPERATION_NAME = "GetTongTon_TrongKy";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaTo");
        pi.setValue(MaTo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Nam");
        pi.setValue(Nam);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Ky");
        pi.setValue(Ky);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("FromDot");
        pi.setValue(FromDot);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToDot");
        pi.setValue(ToDot);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getTongDongMoNuoc(String DongNuoc, String MaTo, String FromNgayDN, String ToNgayDN) {
        String SOAP_ACTION = "http://tempuri.org/GetTongDongMoNuoc";
        String OPERATION_NAME = "GetTongDongMoNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("DongNuoc");
        pi.setValue(DongNuoc);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("MaTo");
        pi.setValue(MaTo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("FromNgayDN");
        pi.setValue(FromNgayDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToNgayDN");
        pi.setValue(ToNgayDN);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getTongDongMoNuoc_Tong(String DongNuoc, String MaTo, String FromNgayDN, String ToNgayDN) {
        String SOAP_ACTION = "http://tempuri.org/GetTongDongMoNuoc_Tong";
        String OPERATION_NAME = "GetTongDongMoNuoc_Tong";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("DongNuoc");
        pi.setValue(DongNuoc);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("MaTo");
        pi.setValue(MaTo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("FromNgayDN");
        pi.setValue(FromNgayDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToNgayDN");
        pi.setValue(ToNgayDN);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getTongDongMoNuoc_ChiTiet(String DongNuoc, String MaTo, String FromNgayDN, String ToNgayDN) {
        String SOAP_ACTION = "http://tempuri.org/GetTongDongMoNuoc_ChiTiet";
        String OPERATION_NAME = "GetTongDongMoNuoc_ChiTiet";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("DongNuoc");
        pi.setValue(DongNuoc);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("MaTo");
        pi.setValue(MaTo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("FromNgayDN");
        pi.setValue(FromNgayDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToNgayDN");
        pi.setValue(ToNgayDN);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getTongThuHo_Tong(String MaTo, String FromCreateDate, String ToCreateDate, String Loai) {
        String SOAP_ACTION = "http://tempuri.org/GetTongThuHo_Tong";
        String OPERATION_NAME = "GetTongThuHo_Tong";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaTo");
        pi.setValue(MaTo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("FromCreateDate");
        pi.setValue(FromCreateDate);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToCreateDate");
        pi.setValue(ToCreateDate);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Loai");
        pi.setValue(Loai);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getTongThuHo_ChiTiet(String MaTo, String FromCreateDate, String ToCreateDate, String Loai) {
        String SOAP_ACTION = "http://tempuri.org/GetTongThuHo_ChiTiet";
        String OPERATION_NAME = "GetTongThuHo_ChiTiet";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaTo");
        pi.setValue(MaTo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("FromCreateDate");
        pi.setValue(FromCreateDate);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToCreateDate");
        pi.setValue(ToCreateDate);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Loai");
        pi.setValue(Loai);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    //admin
    public String truyvan(String sql) {
        String SOAP_ACTION = "http://tempuri.org/truyvan";
        String OPERATION_NAME = "truyvan";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("sql");
        pi.setValue(sql);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String capnhat(String sql) {
        String SOAP_ACTION = "http://tempuri.org/capnhat";
        String OPERATION_NAME = "capnhat";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("sql");
        pi.setValue(sql);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    //lệnh hủy
    public String GetDSHoaDon_LenhHuy(String LoaiCat, String ID) {
        String SOAP_ACTION = "http://tempuri.org/GetDSHoaDon_LenhHuy";
        String OPERATION_NAME = "GetDSHoaDon_LenhHuy";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("LoaiCat");
        pi.setValue(LoaiCat);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ID");
        pi.setValue(ID);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String sua_LenhHuy(String MaHDs, String Cat, String TinhTrang, String CreateBy) {
        String SOAP_ACTION = "http://tempuri.org/Sua_LenhHuy";
        String OPERATION_NAME = "Sua_LenhHuy";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaHDs");
        pi.setValue(MaHDs);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Cat");
        pi.setValue(Cat);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("TinhTrang");
        pi.setValue(TinhTrang);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("CreateBy");
        pi.setValue(CreateBy);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    //nộp tiền
    public String getDS_ChotDangNgan(String FromNgayGiaiTrach, String ToNgayGiaiTrach) {
        String SOAP_ACTION = "http://tempuri.org/getDS_ChotDangNgan";
        String OPERATION_NAME = "getDS_ChotDangNgan";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("FromNgayGiaiTrach");
        pi.setValue(FromNgayGiaiTrach);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("ToNgayGiaiTrach");
        pi.setValue(ToNgayGiaiTrach);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String them_ChotDangNgan(String NgayGiaiTrach, String CreateBy) {
        String SOAP_ACTION = "http://tempuri.org/them_ChotDangNgan";
        String OPERATION_NAME = "them_ChotDangNgan";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("NgayGiaiTrach");
        pi.setValue(NgayGiaiTrach);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("CreateBy");
        pi.setValue(CreateBy);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String chotDangNgan(String ID, String Chot, String CreateBy) {
        String SOAP_ACTION = "http://tempuri.org/chotDangNgan";
        String OPERATION_NAME = "chotDangNgan";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("ID");
        pi.setValue(ID);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Chot");
        pi.setValue(Chot);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("CreateBy");
        pi.setValue(CreateBy);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String nopTien(String NgayGiaiTrach) {
        String SOAP_ACTION = "http://tempuri.org/syncNopTienLo";
        String OPERATION_NAME = "syncNopTienLo";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("NgayGiaiTrach");
        pi.setValue(NgayGiaiTrach);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String showError_NopTien(String NgayGiaiTrach) {
        String SOAP_ACTION = "http://tempuri.org/showError_NopTien";
        String OPERATION_NAME = "showError_NopTien";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("NgayGiaiTrach");
        pi.setValue(NgayGiaiTrach);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }
}
