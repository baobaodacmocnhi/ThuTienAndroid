package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.support.annotation.Nullable;

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
    private final String SOAP_ADDRESS = "http://113.161.88.180:1989/service.asmx";

    @Nullable
    private String excute(SoapObject request, String SOAP_ACTION) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS,1000*60*2);
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

    public String dangNhap(String Username, String Password, String UID) {
        String SOAP_ACTION = "http://tempuri.org/TT_DangNhap";
        String OPERATION_NAME = "TT_DangNhap";
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
        pi.setName("UID");
        pi.setValue(UID);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String dangNhaps(String Username, String Password, String UID) {
        String SOAP_ACTION = "http://tempuri.org/TT_DangNhaps";
        String OPERATION_NAME = "TT_DangNhaps";
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
        pi.setName("UID");
        pi.setValue(UID);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String dangXuat(String Username) {
        String SOAP_ACTION = "http://tempuri.org/TT_DangXuat";
        String OPERATION_NAME = "TT_DangXuat";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("Username");
        pi.setValue(Username);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String dangXuats(String Username, String UID) {
        String SOAP_ACTION = "http://tempuri.org/TT_DangXuats";
        String OPERATION_NAME = "TT_DangXuats";
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
        String SOAP_ACTION = "http://tempuri.org/TT_UpdateUID";
        String OPERATION_NAME = "TT_UpdateUID";
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
        String SOAP_ACTION = "http://tempuri.org/TT_GetVersion";
        String OPERATION_NAME = "TT_GetVersion";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        return excute(request, SOAP_ACTION);
    }

    public String getDSHoaDonTon(String MaNV, String NgayDi) {
        String SOAP_ACTION = "http://tempuri.org/TT_GetDSHoaDonTon";
        String OPERATION_NAME = "TT_GetDSHoaDonTon";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaNV");
        pi.setValue(MaNV);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("NgayDi");
        pi.setValue(NgayDi);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getDSHoaDonTon(String MaNV, String FromDot,String ToDot) {
        String SOAP_ACTION = "http://tempuri.org/TT_GetDSHoaDonTon_Dot";
        String OPERATION_NAME = "TT_GetDSHoaDonTon_Dot";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaNV");
        pi.setValue(MaNV);
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

    public String getDSDongNuoc(String MaNV_DongNuoc, String FromNgayGiao, String ToNgayGiao) {
        String SOAP_ACTION = "http://tempuri.org/TT_GetDSDongNuoc";
        String OPERATION_NAME = "TT_GetDSDongNuoc";
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

    public String getDSCTDongNuoc(String MaNV_DongNuoc, String FromNgayGiao, String ToNgayGiao) {
        String SOAP_ACTION = "http://tempuri.org/TT_GetDSCTDongNuoc";
        String OPERATION_NAME = "TT_GetDSCTDongNuoc";
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
        String SOAP_ACTION = "http://tempuri.org/TT_CheckExist_DongNuoc";
        String OPERATION_NAME = "TT_CheckExist_DongNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaDN");
        pi.setValue(MaDN);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String checkExist_DongNuoc2(String MaDN) {
        String SOAP_ACTION = "http://tempuri.org/TT_CheckExist_DongNuoc2";
        String OPERATION_NAME = "TT_CheckExist_DongNuoc2";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaDN");
        pi.setValue(MaDN);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String themDongNuoc(String MaDN, String DanhBo, String MLT, String HoTen, String DiaChi, String HinhDN, String NgayDN, String ChiSoDN,
                               String Hieu, String Co, String SoThan, String ChiMatSo, String ChiKhoaGoc, String LyDo, String CreateBy) {
        String SOAP_ACTION = "http://tempuri.org/TT_ThemDongNuoc";
        String OPERATION_NAME = "TT_ThemDongNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaDN");
        pi.setValue(MaDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("DanhBo");
        pi.setValue(DanhBo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("MLT");
        pi.setValue(MLT);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("HoTen");
        pi.setValue(HoTen);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("DiaChi");
        pi.setValue(DiaChi);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("HinhDN");
        pi.setValue(HinhDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("NgayDN");
        pi.setValue(NgayDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("ChiSoDN");
        pi.setValue(ChiSoDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("Hieu");
        pi.setValue(Hieu);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("Co");
        pi.setValue(Co);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("SoThan");
        pi.setValue(SoThan);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("ChiMatSo");
        pi.setValue(ChiMatSo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("ChiKhoaGoc");
        pi.setValue(ChiKhoaGoc);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("LyDo");
        pi.setValue(LyDo);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("CreateBy");
        pi.setValue(CreateBy);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String themDongNuoc2(String MaDN, String HinhDN, String NgayDN, String ChiSoDN, String CreateBy) {
        String SOAP_ACTION = "http://tempuri.org/TT_ThemDongNuoc2";
        String OPERATION_NAME = "TT_ThemDongNuoc2";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaDN");
        pi.setValue(MaDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("HinhDN");
        pi.setValue(HinhDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("NgayDN");
        pi.setValue(NgayDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("ChiSoDN");
        pi.setValue(ChiSoDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("CreateBy");
        pi.setValue(CreateBy);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String checkExist_MoNuoc(String MaDN) {
        String SOAP_ACTION = "http://tempuri.org/TT_CheckExist_MoNuoc";
        String OPERATION_NAME = "TT_CheckExist_MoNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaDN");
        pi.setValue(MaDN);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String themMoNuoc(String MaDN, String HinhMN, String NgayMN, String ChiSoMN, String CreateBy) {
        String SOAP_ACTION = "http://tempuri.org/TT_ThemMoNuoc";
        String OPERATION_NAME = "TT_ThemMoNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaDN");
        pi.setValue(MaDN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("HinhMN");
        pi.setValue(HinhMN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("NgayMN");
        pi.setValue(NgayMN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("ChiSoMN");
        pi.setValue(ChiSoMN);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("CreateBy");
        pi.setValue(CreateBy);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String dangNganDongNuoc(String MaNV, String MaHDs)
    {
        String SOAP_ACTION = "http://tempuri.org/TT_DangNganDongNuoc";
        String OPERATION_NAME = "TT_DangNganDongNuoc";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MaNV");
        pi.setValue(MaNV);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("MaHDs");
        pi.setValue(MaHDs);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getDSHoaDon(String DanhBo)
    {
        String SOAP_ACTION = "http://tempuri.org/TT_GetDSHoaDon_DanhBo";
        String OPERATION_NAME = "TT_GetDSHoaDon_DanhBo";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("DanhBo");
        pi.setValue(DanhBo);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

    public String getDSHoaDon(String HoTen,String SoNha,String TenDuong)
    {
        String SOAP_ACTION = "http://tempuri.org/TT_GetDSHoaDon_TTKH";
        String OPERATION_NAME = "TT_GetDSHoaDon_TTKH";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("HoTen");
        pi.setValue(HoTen);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("SoNha");
        pi.setValue(SoNha);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("TenDuong");
        pi.setValue(TenDuong);
        pi.setType(String.class);
        request.addProperty(pi);

        return excute(request, SOAP_ACTION);
    }

}
