package vn.com.capnuoctanhoa.thutienandroid;

import android.support.annotation.Nullable;
import android.widget.Toast;

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
    private String Excute(SoapObject request, String SOAP_ACTION) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
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
    private SoapObject ExcuteReturnTable(SoapObject request, String SOAP_ACTION) {
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

    public String DangNhap(String Username, String Password, String UID) {
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

        return Excute(request, SOAP_ACTION);
    }

    public String DangXuat(String Username) {
        String SOAP_ACTION = "http://tempuri.org/TT_DangXuat";
        String OPERATION_NAME = "TT_DangXuat";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("Username");
        pi.setValue(Username);
        pi.setType(String.class);
        request.addProperty(pi);

        return Excute(request, SOAP_ACTION);
    }

    public String UpdateUID(String MaNV, String UID) {
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

        return Excute(request, SOAP_ACTION);
    }

    public String GetVersion() {
        String SOAP_ACTION = "http://tempuri.org/TT_GetVersion";
        String OPERATION_NAME = "TT_GetVersion";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        return Excute(request, SOAP_ACTION);
    }

    public String GetDSHoaDon(String Nam, String Ky, String FromDot, String ToDot, String MaNV_HanhThu) {
        String SOAP_ACTION = "http://tempuri.org/TT_GetDSHoaDon";
        String OPERATION_NAME = "TT_GetDSHoaDon";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
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
        pi.setName("MaNV_HanhThu");
        pi.setValue(MaNV_HanhThu);
        pi.setType(String.class);
        request.addProperty(pi);

        return Excute(request, SOAP_ACTION);
    }

    public String GetDSDongNuoc(String MaNV_DongNuoc, String FromNgayGiao, String ToNgayGiao) {
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

        return Excute(request, SOAP_ACTION);
    }
}
