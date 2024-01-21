package vn.com.capnuoctanhoa.thutienandroid.Service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.Vector;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityChild;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;

public class ServiceThermalPrinter extends Service {
    public ServiceThermalPrinter() {
    }

    private BluetoothAdapter mBluetoothAdapter;
    public static final String B_UUID = "00001101-0000-1000-8000-00805f9b34fb";
    public static final int STATE_NONE = 0;
    //    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    private ConnectBtThread mConnectThread;
    private ConnectedBtThread mConnectedThread;
    private static Handler mHandler = null;
    public static int mState = STATE_NONE;
    public static BluetoothSocket mSocket = null;
    public static BluetoothDevice mDevice = null;
    //    public Vector<Byte> packData = new Vector<>(2048);
    private final IBinder mBinder = new LocalBinder();
    public InputStream inS;
    public static OutputStream outputStream;

    //IBinder mIBinder = new LocalBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //mHandler = getApplication().getHandler();
        return mBinder;
    }

    public void toast(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }

    public void toastThread(final String mess) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class LocalBinder extends Binder {
        public ServiceThermalPrinter getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServiceThermalPrinter.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        B_DEVICE = intent.getStringExtra("ThermalPrinter");
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        connectToDevice(CLocal.ThermalPrinter);
        return START_STICKY;
    }

    private synchronized void connectToDevice(String macAddress) {
        mDevice = mBluetoothAdapter.getRemoteDevice(macAddress);
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        mConnectThread = new ConnectBtThread(mDevice);
        toast("Đang Kết Nối Máy In");
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    private void setState(int state) {
        mState = state;
        if (mHandler != null) {
            // mHandler.obtainMessage();
        }
    }

    public int getState() {
        return mState;
    }

    public synchronized void stop() {
        setState(STATE_NONE);
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        stopSelf();
    }

    public void sendData(String message) {
        if (mConnectedThread != null) {
            mConnectedThread.write(message.getBytes());
            toast("sent data");
        } else {
            Toast.makeText(ServiceThermalPrinter.this, "Failed to send data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean stopService(Intent name) {
        setState(STATE_NONE);

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        mBluetoothAdapter.cancelDiscovery();
        return super.stopService(name);
    }

    private synchronized void connected(BluetoothSocket mmSocket) {

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        mConnectedThread = new ConnectedBtThread(mmSocket);
        mConnectedThread.start();

        setState(STATE_CONNECTED);
    }

    private class ConnectBtThread extends Thread {
//        private final BluetoothSocket mSocket;
//        private final BluetoothDevice mDevice;

        public ConnectBtThread(BluetoothDevice device) {
//            mDevice = device;
            BluetoothSocket socket = null;
            try {
                socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(B_UUID));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mSocket = socket;

        }

        @Override
        public void run() {
            mBluetoothAdapter.cancelDiscovery();

            try {
                mSocket.connect();
//                Log.d("service","connect thread run method (connected)");
                toastThread("Đã Kết Nối Máy In");
                setState(STATE_CONNECTED);
//                SharedPreferences pre = getSharedPreferences("BT_NAME",0);
//                pre.edit().putString("bluetooth_connected",mDevice.getName()).apply();

            } catch (IOException e) {

                try {
                    mSocket.close();
//                    Log.d("service","connect thread run method ( close function)");
                    toastThread("Lỗi Kết Nối Máy In");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
            //connected(mSocket);
            mConnectedThread = new ConnectedBtThread(mSocket);
            mConnectedThread.start();
        }

        public void cancel() {

            try {
                mSocket.close();
//                Log.d("service","connect thread cancel method");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ConnectedBtThread extends Thread {
        private BluetoothSocket cSocket;
        //        private  InputStream inS;
//        private  OutputStream outS;
        private byte[] buffer;

        public ConnectedBtThread() {

        }

        public ConnectedBtThread(BluetoothSocket socket) {
            cSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inS = tmpIn;
            outputStream = tmpOut;
        }

        @Override
        public void run() {
            buffer = new byte[1024];
            int mByte;
            try {
                mByte = inS.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Log.d("service","connected thread run method");
//            toastThread("connected thread run method");
//            setState(STATE_LISTEN);
        }


        public void write(byte[] buff) {
            try {
                outputStream.write(buff);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void cancel() {
            try {
                cSocket.close();
//                Log.d("service","connected thread cancel method");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        this.stop();
        super.onDestroy();
    }

    /////////////////////////////////////////////////////////////
    private static final byte[] ESC = {0x1B};
    private static int toadoY = 0;
    private static StringBuilder stringBuilder;

    //region Method printer

    public void printThuTien(CEntityParent entityParent) throws IOException {
        try {
            if (mConnectedThread == null || mState != STATE_CONNECTED)
                connected(mSocket);
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printThuTien_EZ(entityParent);
                    break;
                case "ESC":
                    printThuTien_ESC(entityParent);
                    break;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printThuTien(CEntityParent entityParent, CEntityChild entityChild) throws IOException {
        try {
            if (mConnectedThread == null || mState != STATE_CONNECTED)
                connected(mSocket);
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printThuTien_EZ(entityParent, entityChild);
                    break;
                case "ESC":
                    printThuTien_ESC(entityParent, entityChild);
                    break;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void printPhieuBao(CEntityParent entityParent) throws IOException, ParseException {
        try {
            if (mConnectedThread == null || mState != STATE_CONNECTED)
                connectToDevice(CLocal.ThermalPrinter);
            switch (CLocal.MethodPrinter) {
                case "Honeywell31":
                case "ER58":
                    printPhieuBao_ESC(entityParent, 31);
                    break;
                case "Honeywell45":
                    printPhieuBao_ESC(entityParent, 45);
                    break;
                case "Intermec":
                    printPhieuBao_EZ(entityParent);
                    break;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printPhieuBaoCT(CEntityParent entityParent) throws IOException, ParseException {
        try {
            if (mConnectedThread == null || mState != STATE_CONNECTED)
                connectToDevice(CLocal.ThermalPrinter);
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printPhieuBaoCT_EZ(entityParent);
                    break;
                case "ESC":
                    printPhieuBaoCT_ESC(entityParent);
                    break;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printPhieuBao(CEntityParent entityParent, CEntityChild entityChild) throws IOException, ParseException {
        try {
            if (mConnectedThread == null || mState != STATE_CONNECTED)
                connectToDevice(CLocal.ThermalPrinter);
            switch (CLocal.MethodPrinter) {
                case "Honeywell31":
                case "Honeywell45":
                case "ER58":
                    printPhieuBao_ESC(entityParent, entityChild);
                    break;
                case "Intermec":
                    printPhieuBao_EZ(entityParent, entityChild);
                    break;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printPhieuBaoCT(CEntityParent entityParent, CEntityChild entityChild) throws IOException, ParseException {
        try {
            if (mConnectedThread == null || mState != STATE_CONNECTED)
                connectToDevice(CLocal.ThermalPrinter);
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printPhieuBaoCT_EZ(entityParent, entityChild);
                    break;
                case "ESC":
                    printPhieuBaoCT_ESC(entityParent, entityChild);
                    break;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void printPhieuBao2(CEntityParent entityParent) throws IOException {
        try {
            if (mConnectedThread == null || mState != STATE_CONNECTED)
                connectToDevice(CLocal.ThermalPrinter);
            switch (CLocal.MethodPrinter) {
                case "Honeywell31":
                case "ER58":
                    printPhieuBao2_ESC(entityParent, 31);
                    break;
                case "Honeywell45":
                    printPhieuBao2_ESC(entityParent, 45);
                    break;
                case "Intermec":
                    printPhieuBao2_EZ(entityParent);
                    break;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void printTBDongNuoc(CEntityParent entityParent) throws IOException {
        try {
            if (mConnectedThread == null || mState != STATE_CONNECTED)
                connectToDevice(CLocal.ThermalPrinter);
            switch (CLocal.MethodPrinter) {
                case "Honeywell31":
                case "ER58":
                    printTBDongNuoc_ESC(entityParent,31);
                    break;
                case "Honeywell45":
                    printTBDongNuoc_ESC(entityParent,45);
                    break;
                case "Intermec":
                    printTBDongNuoc_EZ(entityParent);
                    break;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printDongNuoc(CEntityParent entityParent) throws IOException {
        try {
            if (mConnectedThread == null || mState != STATE_CONNECTED)
                connectToDevice(CLocal.ThermalPrinter);
            switch (CLocal.MethodPrinter) {
                case "Honeywell31":
                case "Honeywell45":
                case "ER58":
                    printDongNuoc_ESC(entityParent);
                    break;
                case "Intermec":
                    printDongNuoc_EZ(entityParent);
                    break;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printDongNuoc2(CEntityParent entityParent) throws IOException {
        try {
            if (mConnectedThread == null || mState != STATE_CONNECTED)
                connectToDevice(CLocal.ThermalPrinter);
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printDongNuoc2_EZ(entityParent);
                    break;
                case "ESC":
                    printDongNuoc2_ESC(entityParent);
                    break;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printMoNuoc(CEntityParent entityParent) throws IOException {
        try {
            if (mConnectedThread == null || mState != STATE_CONNECTED)
                connectToDevice(CLocal.ThermalPrinter);
            switch (CLocal.MethodPrinter) {
                case "Honeywell31":
                case "Honeywell45":
                case "ER58":
                    printMoNuoc_ESC(entityParent);
                    break;
                case "Intermec":
                    printMoNuoc_EZ(entityParent);
                    break;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printPhiMoNuoc(CEntityParent entityParent) throws IOException {
        try {
            if (mConnectedThread == null || mState != STATE_CONNECTED)
                connectToDevice(CLocal.ThermalPrinter);
            switch (CLocal.MethodPrinter) {
                case "Honeywell31":
                case "Honeywell45":
                case "ER58":
                    printPhiMoNuoc_ESC(entityParent);
                    break;
                case "Intermec":
                    printPhiMoNuoc_EZ(entityParent);
                    break;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    //endregion

    //region EZ method

    public void printThuTien_EZ(CEntityParent entityParent) throws IOException {
        try {
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                if (entityParent.getLstHoaDon().get(i).isDangNgan_DienThoai() == true) {
                    printThuTien_EZ(entityParent, entityParent.getLstHoaDon().get(i));
                }
        } catch (Exception ex) {
            throw ex;
        }
    }

//    public void printThuTien_EZAppend(CEntityParent entityParent) {
//        try {
//            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
//                if (entityParent.getLstHoaDon().get(i).isDangNgan_DienThoai() == true) {
//                    printThuTien_EZAppend(entityParent, entityParent.getLstHoaDon().get(i));
//                }
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }

    public void printThuTien_EZ(CEntityParent entityParent, CEntityChild entityChild) throws IOException {
        try {
            if (entityParent != null && entityChild != null) {
                if (entityChild.isDangNgan_DienThoai() == true) {
                    printTop_EZ();
                    printEZ("BIÊN NHẬN", 4, toadoY, 130, 2, 1);
                    printEZ("THU TIỀN NƯỚC", 4, toadoY, 100, 2, 1);
//            printEZ("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1);
                    printEZ("Kỳ: " + entityChild.getKy(), 4, toadoY, 130, 2, 1);
                    printEZ("Ngày thu: " + entityChild.getNgayGiaiTrach(), 3, toadoY, 0, 1, 1);
                    printEZ("Khách hàng: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1);
                    printEZ("Địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1);
                    printEZ("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1);
                    printEZ("MLT: " + entityParent.getMLT() + " Code: " + entityChild.getCode(), 3, toadoY, 0, 1, 1);
                    printEZ("Giá biểu: " + entityChild.getGiaBieu() + "   Định mức: " + entityChild.getDinhMuc(), 1, toadoY, 0, 1, 1);
                    printEZ("Từ: " + entityChild.getTuNgay() + "  Đến: " + entityChild.getDenNgay(), 1, toadoY, 0, 1, 1);
                    if (entityChild.getCode().equals("F") == false)
                        printEZ("CSC: " + entityChild.getCSC() + "  CSM: " + entityChild.getCSM(), 1, toadoY, 0, 1, 1);
                    else
                        printEZ("Code F Tạm Tính", 1, toadoY, 0, 1, 1);
                    printEZ("Tiêu thụ: " + entityChild.getTieuThu() + "m3", 3, toadoY, 0, 2, 1);
                    printDotFeed_EZ();
                    printEZ("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityChild.getGiaBan()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityChild.getThueGTGT()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Phí BVMT: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(Integer.parseInt(entityChild.getTongCong()) + entityChild.getTienDuTruocDCHD()), "đ"), 3, toadoY, 0, 2, 1);
                    if (entityChild.getTienDuTruocDCHD() > 0 && entityParent.isXoaDCHD() == false) {
                        printEZ("Tiền dư: " + CLocal.formatMoney(String.valueOf(entityChild.getTienDuTruocDCHD()), "đ"), 3, toadoY, 0, 2, 1);
                        printEZ("Tổng cộng tiền thanh toán:\n " + CLocal.formatMoney(entityChild.getTongCong(), "đ"), 3, toadoY, 0, 2, 1);
                        printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityChild.getTongCong()), 1, toadoY, 0, 1, 1);
                    } else {
                        printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(Integer.parseInt(entityChild.getTongCong()) + entityChild.getTienDuTruocDCHD())), 1, toadoY, 0, 1, 1);
                    }
                    printDotFeed_EZ();
                    printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
                    printEZ("Điện thoại/Zalo: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
//                    printEZ("Zalo: " + CLocal.Zalo, 1, toadoY, 0, 1, 1);
                    printEZ("Ngày in: " + CLocal.getTime(), 3, toadoY, 0, 1, 1);
                    printDotFeed_EZ();
                    printEZ("In hóa đơn https://cskhtanhoa.com.vn/hddt", 1, toadoY, 0, 1, 1);
//                    printEZ("XIN CẢM ƠN", 1, toadoY, 50, 1, 1);
//                    printEZ("Từ kỳ 04/2022 không thu tiền nước tại nhà", 3, toadoY, 0, 1, 1);
                    printEnd_EZ();
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

//    public void printThuTien_EZAppend(CEntityParent entityParent, CEntityChild entityChild) {
//        try {
//            if (entityParent != null && entityChild != null) {
//                if (entityChild.isDangNgan_DienThoai() == true) {
//                    printTop_EZAppend();
//                    stringBuilder.append(printEZAppend("BIÊN NHẬN", 4, toadoY, 130, 2, 1));
//                    stringBuilder.append(printEZAppend("THU TIỀN NƯỚC", 4, toadoY, 100, 2, 1));
////            stringBuilder.append(printEZAppend("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1));
//                    stringBuilder.append(printEZAppend("Kỳ: " + entityChild.getKy(), 4, toadoY, 130, 2, 1));
//                    stringBuilder.append(printEZAppend("Ngày thu: " + entityChild.getNgayGiaiTrach(), 3, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Khách hàng: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("MLT: " + entityParent.getMLT() + " Code: " + entityChild.getCode(), 3, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Giá biểu: " + entityChild.getGiaBieu() + "   Định mức: " + entityChild.getDinhMuc(), 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Từ: " + entityChild.getTuNgay() + "  Đến: " + entityChild.getDenNgay(), 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("CSC: " + entityChild.getCSC() + "  CSM: " + entityChild.getCSM(), 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Tiêu thụ: " + entityChild.getTieuThu() + "m3", 3, toadoY, 0, 2, 1));
//                    stringBuilder.append(printDotFeed_EZAppend());
//                    stringBuilder.append(printEZAppend("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityChild.getGiaBan()), "đ"), 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityChild.getThueGTGT()), "đ"), 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Phí BVMT: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT()), "đ"), 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Tổng cộng: " + CLocal.formatMoney(String.valueOf(Integer.parseInt(entityChild.getTongCong()) + entityChild.getTienDuTruocDCHD()), "đ"), 3, toadoY, 0, 2, 1));
//                    if (entityChild.getTienDuTruocDCHD() > 0 && entityParent.isXoaDCHD() == false) {
//                        stringBuilder.append(printEZAppend("Tiền dư: " + CLocal.formatMoney(String.valueOf(entityChild.getTienDuTruocDCHD()), "đ"), 3, toadoY, 0, 2, 1));
//                        stringBuilder.append(printEZAppend("Tổng cộng tiền thanh toán:\n " + CLocal.formatMoney(entityChild.getTongCong(), "đ"), 3, toadoY, 0, 2, 1));
//                        stringBuilder.append(printEZAppend("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityChild.getTongCong()), 1, toadoY, 0, 1, 1));
//                    } else {
//                        stringBuilder.append(printEZAppend("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(Integer.parseInt(entityChild.getTongCong()) + entityChild.getTienDuTruocDCHD())), 1, toadoY, 0, 1, 1));
//                    }
//                    stringBuilder.append(printDotFeed_EZAppend());
//                    stringBuilder.append(printEZAppend("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Ngày in: " + CLocal.getTime(), 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printDotFeed_EZAppend());
//                    stringBuilder.append(printEZAppend("Quý khách muốn in hóa đơn vui lòng vào trang website Công ty: https://cskhtanhoa.com.vn/hddt", 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("XIN CẢM ƠN", 1, toadoY, 50, 1, 1));
//                    stringBuilder.append(printEnd_EZAppend());
//                    stringBuilder.append("}");
//                    outputStream.write(stringBuilder.toString().getBytes());
//                    outputStream.flush();
//                }
//            }
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }

    public void printPhieuBao_EZ(CEntityParent entityParent) throws IOException, ParseException {
        try {
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                if (entityParent.getLstHoaDon().get(i).getInPhieuBao_Ngay().equals("") == false) {
                    printPhieuBao_EZ(entityParent, entityParent.getLstHoaDon().get(i));
                }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printPhieuBaoCT_EZ(CEntityParent entityParent) throws IOException, ParseException {
        try {
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                if (entityParent.getLstHoaDon().get(i).getInPhieuBao_Ngay().equals("") == false) {
                    printPhieuBaoCT_EZ(entityParent, entityParent.getLstHoaDon().get(i));
                }
        } catch (Exception ex) {
            throw ex;
        }
    }

//    public void printPhieuBao_EZAppend(CEntityParent entityParent) {
//        try {
//            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
//                if (entityParent.getLstHoaDon().get(i).getInPhieuBao_Ngay().equals("") == false) {
//                    printPhieuBao_EZAppend(entityParent, entityParent.getLstHoaDon().get(i));
//                }
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }

    public void printPhieuBao_EZ(CEntityParent entityParent, CEntityChild entityChild) throws IOException, ParseException {
        try {
            if (entityParent != null && entityChild != null) {
                if (entityChild.getInPhieuBao_Ngay().equals("") == false && entityChild.isTamThu() == false && entityChild.isThuHo() == false) {
                    printTop_EZ();
                    printEZ("GIẤY BÁO TIỀN NƯỚC", 4, toadoY, 60, 2, 1);
                    printEZ("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1);

                    printEZ("Kỳ: " + entityChild.getKy(), 4, toadoY, 130, 2, 1);
                    printEZ("Khách hàng: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1);
                    printEZ("Địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1);
                    printEZ("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1);
                    printEZ("MLT: " + entityParent.getMLT() + " Code: " + entityChild.getCode(), 3, toadoY, 0, 1, 1);
                    printEZ("Giá biểu: " + entityChild.getGiaBieu() + "   Định mức: " + entityChild.getDinhMuc(), 1, toadoY, 0, 1, 1);
                    printEZ("Từ: " + entityChild.getTuNgay() + "  Đến: " + entityChild.getDenNgay(), 1, toadoY, 0, 1, 1);
                    if (entityChild.getCode().equals("F") == false)
                        printEZ("CSC: " + entityChild.getCSC() + "  CSM: " + entityChild.getCSM(), 1, toadoY, 0, 1, 1);
                    else
                        printEZ("Code F Tạm Tính", 1, toadoY, 0, 1, 1);
                    printEZ("Tiêu thụ: " + entityChild.getTieuThu() + "m3", 3, toadoY, 0, 2, 1);
                    printDotFeed_EZ();
                    printEZ("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityChild.getGiaBan()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityChild.getThueGTGT()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Tiền DV thoát nước: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Thuế DV thoát nước: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT_Thue()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(Integer.parseInt(entityChild.getTongCong()) + entityChild.getTienDuTruocDCHD()), "đ"), 3, toadoY, 0, 2, 1);
                    if (entityChild.getTienDuTruocDCHD() > 0) {
                        printEZ("Tiền dư: " + CLocal.formatMoney(String.valueOf(entityChild.getTienDuTruocDCHD()), "đ"), 3, toadoY, 0, 2, 1);
                        printEZ("Tổng cộng tiền thanh toán:\n " + CLocal.formatMoney(entityChild.getTongCong(), "đ"), 3, toadoY, 0, 2, 1);
                    }
                    printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityChild.getTongCong()), 1, toadoY, 0, 1, 1);
                    //
                    String[] str = entityChild.getInPhieuBao_Ngay().split(" ");
                    Date dateLap = CLocal.DateFormat.parse(entityChild.getInPhieuBao_Ngay());
                    Calendar c = Calendar.getInstance();
                    c.setTime(dateLap);
                    c.add(Calendar.DATE, 3);
                    dateLap = c.getTime();
                    printEZ("Qúy Khách vui lòng thanh toán tiền nước trước ngày " + CLocal.DateFormatShort.format(dateLap), 1, toadoY, 0, 1, 1);
                    //
                    if (entityParent.getCuaHangThuHo1().equals("") == false) {
                        printEZ("Dịch vụ Thu Hộ:", 3, toadoY, 0, 1, 1);
                        printEZ(entityParent.getCuaHangThuHo1(), 3, toadoY, 0, 1, 1);
                        printEZ(entityParent.getCuaHangThuHo2(), 3, toadoY, 0, 1, 1);
                    }
                    printDotFeed_EZ();
                    printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
                    printEZ("Điện thoại/Zalo: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
//                    printEZ("Zalo: " + CLocal.Zalo, 1, toadoY, 0, 1, 1);
                    printEZ("Ngày lập: " + entityChild.getInPhieuBao_Ngay(), 3, toadoY, 0, 1, 1);
                    printEZ("Ngày in: " + CLocal.getTime(), 3, toadoY, 0, 1, 1);
                    printDotFeed_EZ();
                    printEZ("https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
//                    printEZ("XIN CẢM ƠN", 1, toadoY, 50, 1, 1);
//                    printEZ("Từ kỳ 04/2022 không thu tiền nước tại nhà", 3, toadoY, 0, 1, 1);
                    printEnd_EZ();
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printPhieuBaoCT_EZ(CEntityParent entityParent, CEntityChild entityChild) throws IOException, ParseException {
        try {
            if (entityParent != null && entityChild != null) {
                if (entityChild.getInPhieuBao_Ngay().equals("") == false && entityChild.isTamThu() == false && entityChild.isThuHo() == false) {
                    printTop_EZ();
                    printEZ("GIẤY BÁO TIỀN NƯỚC", 4, toadoY, 60, 2, 1);
                    printEZ("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1);

                    printEZ("Kỳ: " + entityChild.getKy(), 4, toadoY, 130, 2, 1);
                    printEZ("Khách hàng: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1);
                    printEZ("Địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1);
                    printEZ("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1);
                    printEZ("MLT: " + entityParent.getMLT() + " Code: " + entityChild.getCode(), 3, toadoY, 0, 1, 1);
                    printEZ("Giá biểu: " + entityChild.getGiaBieu() + "   Định mức: " + entityChild.getDinhMuc(), 1, toadoY, 0, 1, 1);
                    printEZ("Từ: " + entityChild.getTuNgay() + "  Đến: " + entityChild.getDenNgay(), 1, toadoY, 0, 1, 1);
                    if (entityChild.getCode().equals("F") == false)
                        printEZ("CSC: " + entityChild.getCSC() + "  CSM: " + entityChild.getCSM(), 1, toadoY, 0, 1, 1);
                    else
                        printEZ("Code F Tạm Tính", 1, toadoY, 0, 1, 1);
                    printEZ("Tiêu thụ: " + entityChild.getTieuThu() + "m3", 3, toadoY, 0, 2, 1);
                    printDotFeed_EZ();
                    String[] ChiTiets = entityChild.getChiTietTienNuoc().split("\r\n");
                    for (String chitiet : ChiTiets) {
                        printEZ(chitiet, 1, toadoY, 0, 1, 1);
                    }
                    printEZ("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityChild.getGiaBan()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityChild.getThueGTGT()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Tiền DV thoát nước: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Thuế DV thoát nước: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT_Thue()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(Integer.parseInt(entityChild.getTongCong()) + entityChild.getTienDuTruocDCHD()), "đ"), 3, toadoY, 0, 2, 1);
                    if (entityChild.getTienDuTruocDCHD() > 0) {
                        printEZ("Tiền dư: " + CLocal.formatMoney(String.valueOf(entityChild.getTienDuTruocDCHD()), "đ"), 3, toadoY, 0, 2, 1);
                        printEZ("Tổng cộng tiền thanh toán:\n " + CLocal.formatMoney(entityChild.getTongCong(), "đ"), 3, toadoY, 0, 2, 1);
                    }
                    printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityChild.getTongCong()), 1, toadoY, 0, 1, 1);
                    //
                    String[] str = entityChild.getInPhieuBao_Ngay().split(" ");
                    Date dateLap = CLocal.DateFormat.parse(entityChild.getInPhieuBao_Ngay());
                    Calendar c = Calendar.getInstance();
                    c.setTime(dateLap);
                    c.add(Calendar.DATE, 7);
                    dateLap = c.getTime();
                    printEZ("Qúy Khách vui lòng thanh toán tiền nước trước ngày " + CLocal.DateFormatShort.format(dateLap), 1, toadoY, 0, 1, 1);
                    //
                    if (entityParent.getCuaHangThuHo1().equals("") == false) {
                        printEZ("Dịch vụ Thu Hộ:", 3, toadoY, 0, 1, 1);
                        printEZ(entityParent.getCuaHangThuHo1(), 3, toadoY, 0, 1, 1);
                        printEZ(entityParent.getCuaHangThuHo2(), 3, toadoY, 0, 1, 1);
                    }
                    printDotFeed_EZ();
                    printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
                    printEZ("Điện thoại/Zalo: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
//                    printEZ("Zalo: " + CLocal.Zalo, 1, toadoY, 0, 1, 1);
                    printEZ("Ngày lập: " + entityChild.getInPhieuBao_Ngay(), 3, toadoY, 0, 1, 1);
                    printEZ("Ngày in: " + CLocal.getTime(), 3, toadoY, 0, 1, 1);
                    printDotFeed_EZ();
                    printEZ("https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
//                    printEZ("XIN CẢM ƠN", 1, toadoY, 50, 1, 1);
//                    printEZ("Từ kỳ 04/2022 không thu tiền nước tại nhà", 3, toadoY, 0, 1, 1);
                    printEnd_EZ();
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

//    public void printPhieuBao_EZAppend(CEntityParent entityParent, CEntityChild entityChild) {
//        try {
//            if (entityParent != null && entityChild != null) {
//                if (entityChild.getInPhieuBao_Ngay().equals("") == false) {
//                    printTop_EZAppend();
//                    stringBuilder.append(printEZAppend("GIẤY BÁO TIỀN NƯỚC", 4, toadoY, 60, 2, 1));
//                    stringBuilder.append(printEZAppend("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1));
//                    stringBuilder.append(printEZAppend("Kỳ: " + entityChild.getKy(), 4, toadoY, 130, 2, 1));
//                    stringBuilder.append(printEZAppend("Khách hàng: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("MLT: " + entityParent.getMLT() + " Code: " + entityChild.getCode(), 3, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Giá biểu: " + entityChild.getGiaBieu() + "   Định mức: " + entityChild.getDinhMuc(), 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Từ: " + entityChild.getTuNgay() + "  Đến: " + entityChild.getDenNgay(), 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("CSC: " + entityChild.getCSC() + "  CSM: " + entityChild.getCSM(), 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Tiêu thụ: " + entityChild.getTieuThu() + "m3", 3, toadoY, 0, 2, 1));
//                    stringBuilder.append(printDotFeed_EZAppend());
//                    stringBuilder.append(printEZAppend("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityChild.getGiaBan()), "đ"), 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityChild.getThueGTGT()), "đ"), 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Tiền DV thoát nước: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT()), "đ"), 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Tổng cộng: " + CLocal.formatMoney(String.valueOf(Integer.parseInt(entityChild.getTongCong()) + entityChild.getTienDuTruocDCHD()), "đ"), 3, toadoY, 0, 2, 1));
//                    if (entityChild.getTienDuTruocDCHD() > 0) {
//                        stringBuilder.append(printEZAppend("Tiền dư: " + CLocal.formatMoney(String.valueOf(entityChild.getTienDuTruocDCHD()), "đ"), 3, toadoY, 0, 2, 1));
//                        stringBuilder.append(printEZAppend("Tổng cộng tiền thanh toán:\n " + CLocal.formatMoney(entityChild.getTongCong(), "đ"), 3, toadoY, 0, 2, 1));
//                    }
//                    stringBuilder.append(printEZAppend("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityChild.getTongCong()), 1, toadoY, 0, 1, 1));
//                    String[] str = entityChild.getInPhieuBao_Ngay().split(" ");
//                    stringBuilder.append(printEZAppend("Quý khách vui lòng thanh toán tiền nước trong 07 ngày kể từ ngày " + str[0], 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Trân trọng kính chào.", 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printDotFeed_EZAppend());
//                    stringBuilder.append(printEZAppend("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Ngày lập: " + entityChild.getInPhieuBao_Ngay(), 3, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("Ngày in: " + CLocal.getTime(), 3, toadoY, 0, 1, 1));
//                    stringBuilder.append(printDotFeed_EZAppend());
//                    stringBuilder.append(printEZAppend("Website Công ty: https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1));
//                    stringBuilder.append(printEZAppend("XIN CẢM ƠN", 1, toadoY, 50, 1, 1));
//                    stringBuilder.append(printEnd_EZAppend());
//                    stringBuilder.append("}");
//                    outputStream.write(stringBuilder.toString().getBytes());
//                    outputStream.flush();
//                }
//            }
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }

    public void printPhieuBao2_EZ(CEntityParent entityParent) throws IOException {
        try {
            if (entityParent != null) {
                if (entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().equals("") == false) {
                    printTop_EZ();
                    printEZ("THÔNG BÁO TIỀN NƯỚC", 4, toadoY, 60, 2, 1);
                    printEZ("CHƯA THANH TOÁN", 4, toadoY, 80, 2, 1);
                    printEZ("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1);
                    printEZ("Kính gửi: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1);
                    printEZ("Địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1);
                    printEZ("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1);
                    printEZ("MLT: " + entityParent.getMLT() + " Code: " + entityParent.getLstHoaDon().get(0).getCode(), 3, toadoY, 0, 1, 1);
//                    printEZ("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc(), 1, toadoY, 0, 1, 1);
//                    if (entityParent.getLstHoaDon().get(0).getCode().equals("F") == false)
//                        printEZ("CSC: " + entityParent.getLstHoaDon().get(0).getCSC() + "  CSM: " + entityParent.getLstHoaDon().get(0).getCSM() + "  Tiêu thụ: " + entityParent.getLstHoaDon().get(0).getTieuThu() + "m3", 1, toadoY, 0, 1, 1);
//                    else
//                        printEZ("Code F Tạm Tính", 1, toadoY, 0, 1, 1);
                    printDotFeed_EZ();
                    printEZ("Hóa đơn:", 1, toadoY, 0, 1, 1);
                    int TongCong = 0, TienDu = 0;
                    for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                        if (entityParent.getLstHoaDon().get(i).isTamThu() == false && entityParent.getLstHoaDon().get(i).isThuHo() == false) {
                            printEZ("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(String.valueOf(Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong()) + entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD()), "đ"), 3, toadoY, 0, 1, 1);
                            TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong()) + entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
                            TienDu += entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
                        }
                    printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ"), 3, toadoY, 0, 2, 1);
                    if (TienDu > 0) {
                        printEZ("Tiền dư: " + CLocal.formatMoney(String.valueOf(TienDu), "đ"), 3, toadoY, 0, 2, 1);
                        printEZ("Tổng cộng tiền thanh toán:\n " + CLocal.formatMoney(String.valueOf(TongCong - TienDu), "đ"), 3, toadoY, 0, 2, 1);
                        printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong - TienDu)), 1, toadoY, 0, 1, 1);
                    } else {
                        printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)), 1, toadoY, 0, 1, 1);
                    }
//                    String[] str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_NgayHen().split(" ");
                    Date dt = new Date();
                    Calendar c = Calendar.getInstance();
                    c.setTime(dt);
                    c.add(Calendar.DATE, 3);
                    dt = c.getTime();
                    printEZ("Ghi chú: Quý khách thanh toán tiền nước trước ngày " + CLocal.DateFormatShort.format(dt) + ". Nếu đã thanh toán vui lòng bỏ qua thông báo này", 3, toadoY, 0, 1, 1);
                    if (entityParent.getCuaHangThuHo1().equals("") == false) {
                        printEZ("Dịch vụ Thu Hộ:", 3, toadoY, 0, 1, 1);
                        printEZ(entityParent.getCuaHangThuHo1(), 3, toadoY, 0, 1, 1);
                        printEZ(entityParent.getCuaHangThuHo2(), 3, toadoY, 0, 1, 1);
                    }
                    printDotFeed_EZ();
                    printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
                    printEZ("Điện thoại/Zalo: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
//                    printEZ("Zalo: " + CLocal.Zalo, 1, toadoY, 0, 1, 1);
                    printEZ("Ngày lập: " + entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay(), 3, toadoY, 0, 1, 1);
                    printEZ("Ngày in: " + CLocal.getTime(), 3, toadoY, 0, 1, 1);
                    printDotFeed_EZ();
                    printEZ("https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
//                    printEZ("XIN CẢM ƠN", 1, toadoY, 50, 1, 1);
//                    printEZ("Từ kỳ 04/2022 không thu tiền nước tại nhà", 3, toadoY, 0, 1, 1);
                    printEnd_EZ();
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

//    public void printPhieuBao2_EZAppend(CEntityParent entityParent) {
//        try {
//            printTop_EZAppend();
//            stringBuilder.append(printEZAppend("THÔNG BÁO TIỀN NƯỚC", 4, toadoY, 60, 2, 1));
//            stringBuilder.append(printEZAppend("CHƯA THANH TOÁN", 4, toadoY, 80, 2, 1));
//            stringBuilder.append(printEZAppend("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1));
//            stringBuilder.append(printEZAppend("Kính gửi: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("MLT: " + entityParent.getMLT() + " Code: " + entityParent.getLstHoaDon().get(0).getCode(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("CSC: " + entityParent.getLstHoaDon().get(0).getCSC() + "  CSM: " + entityParent.getLstHoaDon().get(0).getCSM() + "  Tiêu thụ: " + entityParent.getLstHoaDon().get(0).getTieuThu() + "m3", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Hóa đơn:", 1, toadoY, 0, 1, 1));
//            int TongCong = 0, TienDu = 0;
//            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
//                stringBuilder.append(printEZAppend("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ"), 3, toadoY, 0, 1, 1));
//                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong()) + entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
//                TienDu += entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
//            }
//            stringBuilder.append(printEZAppend("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ"), 3, toadoY, 0, 2, 1));
//            if (TienDu > 0) {
//                stringBuilder.append(printEZAppend("Tiền dư: " + CLocal.formatMoney(String.valueOf(TienDu), "đ"), 3, toadoY, 0, 2, 1));
//                stringBuilder.append(printEZAppend("Tổng cộng tiền thanh toán:\n " + CLocal.formatMoney(String.valueOf(TongCong - TienDu), "đ"), 3, toadoY, 0, 2, 1));
//                stringBuilder.append(printEZAppend("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong - TienDu)), 1, toadoY, 0, 1, 1));
//            } else {
//                stringBuilder.append(printEZAppend("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)), 1, toadoY, 0, 1, 1));
//            }
//            String[] str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_NgayHen().split(" ");
//            stringBuilder.append(printEZAppend("Để được cung cấp nước liên tục đề nghị Quý khách vui lòng thanh toán trước ngày " + str[0], 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Nếu quá thời hạn trên khách hàng chưa thanh toán, Công ty sẽ tạm ngưng cung cấp nước theo quy định.", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Mọi thắc mắc đề nghị Quý khách hàng liên hệ tại Công ty hoặc Tổng đài trước ngày ngưng cung cấp nước", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Trân trọng kính chào.", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Ngày lập: " + entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Ngày in: " + CLocal.getTime(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Website Công ty: https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("XIN CẢM ƠN", 1, toadoY, 50, 1, 1));
//            stringBuilder.append(printEnd_EZAppend());
//            stringBuilder.append("}");
//            outputStream.write(stringBuilder.toString().getBytes());
//            outputStream.flush();
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }

    public void printTBDongNuoc_EZ(CEntityParent entityParent) throws IOException {
        try {
            if (entityParent != null) {
                if (entityParent.getLstHoaDon().get(entityParent.getLstHoaDon().size() - 1).getTBDongNuoc_Ngay().equals("") == false) {
                    printTop_EZ();
                    printEZ("THÔNG BÁO", 4, toadoY, 120, 2, 1);
                    printEZ("NGƯNG CUNG CẤP NƯỚC", 4, toadoY, 20, 2, 1);
                    printEZ("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1);
                    printEZ("Kính gửi: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1);
                    printEZ("Địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1);
                    printEZ("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1);
                    printEZ("MLT: " + entityParent.getMLT() + " Code: " + entityParent.getLstHoaDon().get(0).getCode(), 3, toadoY, 0, 1, 1);
//                    printEZ("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc(), 1, toadoY, 0, 1, 1);
//                    if (entityParent.getLstHoaDon().get(0).getCode().equals("F") == false)
//                        printEZ("CSC: " + entityParent.getLstHoaDon().get(0).getCSC() + "  CSM: " + entityParent.getLstHoaDon().get(0).getCSM() + "  Tiêu thụ: " + entityParent.getLstHoaDon().get(0).getTieuThu() + "m3", 1, toadoY, 0, 1, 1);
//                    else
//                        printEZ("Code F Tạm Tính", 1, toadoY, 0, 1, 1);
                    printDotFeed_EZ();
                    String[] str = entityParent.getLstHoaDon().get(entityParent.getLstHoaDon().size() - 1).getTBDongNuoc_NgayHen().split(" ");
                    printEZ("Công ty sẽ tạm ngưng cung cấp nước tại địa chỉ trên từ ngày " + str[0], 3, toadoY, 0, 1, 1);
                    printEZ("Lý do: Quý khách chưa thanh toán hóa đơn tiền nước:", 1, toadoY, 0, 1, 1);
                    int TongCong = 0, TienDu = 0;
                    for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                        if (entityParent.getLstHoaDon().get(i).isTamThu() == false && entityParent.getLstHoaDon().get(i).isThuHo() == false) {
                            printEZ("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(String.valueOf(Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong()) + entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD()), "đ"), 3, toadoY, 0, 1, 1);
                            TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong()) + entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
                            TienDu += entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
                        }
                    printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ"), 3, toadoY, 0, 1, 1);
                    if (TienDu > 0) {
                        printEZ("Tiền dư: " + CLocal.formatMoney(String.valueOf(TienDu), "đ"), 3, toadoY, 0, 1, 1);
                        printEZ("Tổng cộng tiền thanh toán:\n " + CLocal.formatMoney(String.valueOf(TongCong - TienDu), "đ"), 3, toadoY, 0, 1, 1);
                        printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong - TienDu)), 1, toadoY, 0, 1, 1);
                    } else {
                        printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)), 1, toadoY, 0, 1, 1);
                    }
                    printDotFeed_EZ();
                    String Co = "";
                    if (entityParent.getLstHoaDon().get(0).getCo().equals("") == true)
                        Co = entityParent.getCo();
                    else
                        Co = entityParent.getLstHoaDon().get(0).getCo();
                    printEZ("Phí mở nước: " + CLocal.getPhiMoNuoc(Co) + "đ. (nếu khách hàng bị khóa nước)", 1, toadoY, 0, 1, 1);
                    printEZ("Ghi chú: Quý khách thanh toán tiền nước trước ngày " + str[0] + ". Nếu đã thanh toán vui lòng bỏ qua thông báo này.", 1, toadoY, 0, 1, 1);
                    if (entityParent.getCuaHangThuHo1().equals("") == false) {
                        printEZ("Dịch vụ Thu Hộ:", 3, toadoY, 0, 1, 1);
                        printEZ(entityParent.getCuaHangThuHo1(), 3, toadoY, 0, 1, 1);
                        printEZ(entityParent.getCuaHangThuHo2(), 3, toadoY, 0, 1, 1);
                    }
                    printDotFeed_EZ();
                    printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
                    printEZ("Điện thoại/Zalo: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
//                    printEZ("Zalo: " + CLocal.Zalo, 1, toadoY, 0, 1, 1);
                    printEZ("Ngày lập: " + entityParent.getLstHoaDon().get(entityParent.getLstHoaDon().size() - 1).getTBDongNuoc_Ngay(), 3, toadoY, 0, 1, 1);
                    printEZ("Ngày in: " + CLocal.getTime(), 3, toadoY, 0, 1, 1);
                    printDotFeed_EZ();
                    printEZ("https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
//                    printEZ("XIN CẢM ƠN", 1, toadoY, 50, 1, 1);
//                    printEZ("Từ kỳ 04/2022 không thu tiền nước tại nhà", 3, toadoY, 0, 1, 1);
                    printEnd_EZ();
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

//    public void printTBDongNuoc_EZAppend(CEntityParent entityParent) {
//        try {
//            printTop_EZAppend();
//            stringBuilder.append(printEZAppend("THÔNG BÁO", 4, toadoY, 120, 2, 1));
//            stringBuilder.append(printEZAppend("TẠM NGƯNG CUNG CẤP NƯỚC", 4, toadoY, 30, 2, 1));
//            stringBuilder.append(printEZAppend("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1));
//            stringBuilder.append(printEZAppend("Kính gửi: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("MLT: " + entityParent.getMLT() + " Code: " + entityParent.getLstHoaDon().get(0).getCode(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("CSC: " + entityParent.getLstHoaDon().get(0).getCSC() + "  CSM: " + entityParent.getLstHoaDon().get(0).getCSM() + "  Tiêu thụ: " + entityParent.getLstHoaDon().get(0).getTieuThu() + "m3", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            String[] str = entityParent.getLstHoaDon().get(0).getTBDongNuoc_NgayHen().split(" ");
//            stringBuilder.append(printEZAppend("Công ty sẽ tạm ngưng cung cấp nước tại địa chỉ trên vào ngày " + str[0], 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Lý do: Quý khách chưa thanh toán hóa đơn tiền nước:", 1, toadoY, 0, 1, 1));
//            int TongCong = 0, TienDu = 0;
//            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
//                stringBuilder.append(printEZAppend("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ"), 3, toadoY, 0, 1, 1));
//                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong()) + entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
//                TienDu += entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
//            }
//            stringBuilder.append(printEZAppend("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ"), 3, toadoY, 0, 1, 1));
//            if (TienDu > 0) {
//                stringBuilder.append(printEZAppend("Tiền dư: " + CLocal.formatMoney(String.valueOf(TienDu), "đ"), 3, toadoY, 0, 1, 1));
//                stringBuilder.append(printEZAppend("Tổng cộng tiền thanh toán:\n " + CLocal.formatMoney(String.valueOf(TongCong - TienDu), "đ"), 3, toadoY, 0, 1, 1));
//                stringBuilder.append(printEZAppend("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong - TienDu)), 1, toadoY, 0, 1, 1));
//            } else {
//                stringBuilder.append(printEZAppend("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)), 1, toadoY, 0, 1, 1));
//            }
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Công ty tiến hành mở nước khi Quý khách hàng đã thanh toán hết khoản nợ trên và chi phí mở nước là " + CLocal.getPhiMoNuoc(entityParent.getLstHoaDon().get(0).getCo()) + "đ.", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Trân trọng kính chào.", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Ngày lập: " + entityParent.getLstHoaDon().get(0).getTBDongNuoc_Ngay(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Ngày in: " + CLocal.getTime(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Website Công ty: https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("XIN CẢM ƠN", 1, toadoY, 50, 1, 1));
//            stringBuilder.append(printEnd_EZAppend());
//            stringBuilder.append("}");
//            outputStream.write(stringBuilder.toString().getBytes());
//            outputStream.flush();
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }

    public void printDongNuoc_EZ(CEntityParent entityParent) throws IOException {
        try {
            printTop_EZ();
            printEZ("BIÊN BẢN TẠM", 4, toadoY, 80, 2, 1);
            printEZ("NGƯNG CUNG CẤP NƯỚC LẦN 1", 4, toadoY, 40, 2, 1);
            printEZ("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1);
            printEZ("Hôm nay: ngày " + entityParent.getNgayDN(), 3, toadoY, 0, 1, 1);
            printEZ("Tiến hành tạm ngưng cung cấp nước tại địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1);
            printEZ("Khách hàng: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1);
            printEZ("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1);
            printEZ("MLT: " + entityParent.getMLT(), 3, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            printEZ("Lý do: nợ tiền nước kỳ", 1, toadoY, 0, 1, 1);
            int TongCong = 0;
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                printEZ("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ"), 3, toadoY, 0, 1, 1);
                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
            }
            printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ"), 3, toadoY, 0, 1, 1);
            printEZ("Chi phí mở nước là: " + CLocal.getPhiMoNuoc(entityParent.getCo()) + "đ/lần", 3, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            printEZ("Hiệu: " + entityParent.getHieu() + " Cỡ: " + entityParent.getCo(), 1, toadoY, 0, 1, 1);
            printEZ("Số thân: " + entityParent.getSoThan(), 1, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            printEZ("Chỉ số: " + entityParent.getChiSoDN(), 1, toadoY, 0, 1, 1);
            printEZ("Mã chì: " + entityParent.getNiemChi(), 1, toadoY, 0, 1, 1);
            printEZ("Chì thân: " + entityParent.getChiMatSo(), 1, toadoY, 0, 1, 1);
            printEZ("Chì gốc: " + entityParent.getChiKhoaGoc(), 1, toadoY, 0, 1, 1);
            String str = "";
            if (entityParent.isKhoaTu() == true)
                str = "Khóa Từ";
            else
                str = "Khóa Chì";
            printEZ("Khóa nước: " + str, 1, toadoY, 0, 1, 1);
            printEZ("Vị trí: " + entityParent.getViTri(), 1, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            printEZ("Công ty chỉ mở nước khi khách hàng thanh toán hết nợ và chi phí mở nước.", 1, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
            printEZ("Điện thoại/Zalo: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
//            printEZ("Zalo: " + CLocal.Zalo, 1, toadoY, 0, 1, 1);
            printEZ("Ngày in: " + CLocal.getTime(), 3, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            printEZ("https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
//            printEZ("XIN CẢM ƠN", 1, toadoY, 50, 1, 1);
            printEnd_EZ();
            outputStream.flush();
        } catch (Exception ex) {
            throw ex;
        }
    }

//    public void printDongNuoc_EZAppend(CEntityParent entityParent) {
//        try {
//            printTop_EZAppend();
//            stringBuilder.append(printEZAppend("BIÊN BẢN TẠM", 4, toadoY, 80, 2, 1));
//            stringBuilder.append(printEZAppend("NGƯNG CUNG CẤP NƯỚC LẦN 1", 4, toadoY, 40, 2, 1));
//            stringBuilder.append(printEZAppend("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1));
//            stringBuilder.append(printEZAppend("Hôm nay: ngày " + entityParent.getNgayDN(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Tiến hành tạm ngưng cung cấp nước tại địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Khách hàng: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("MLT: " + entityParent.getMLT(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Lý do: nợ tiền nước kỳ", 1, toadoY, 0, 1, 1));
//            int TongCong = 0;
//            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
//                stringBuilder.append(printEZAppend("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ"), 3, toadoY, 0, 1, 1));
//                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
//            }
//            stringBuilder.append(printEZAppend("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ"), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Chi phí mở nước là: " + CLocal.getPhiMoNuoc(entityParent.getCo()) + "đ/lần", 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Hiệu: " + entityParent.getHieu() + " Cỡ: " + entityParent.getCo(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Số thân: " + entityParent.getSoThan(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Chỉ số: " + entityParent.getChiSoDN(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Mã chì: " + entityParent.getNiemChi(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Chì thân: " + entityParent.getChiMatSo(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Chì gốc: " + entityParent.getChiKhoaGoc(), 1, toadoY, 0, 1, 1));
//            String str = "";
//            if (entityParent.isKhoaTu() == true)
//                str = "Khóa Từ";
//            else
//                str = "Khóa Chì";
//            stringBuilder.append(printEZAppend("Khóa nước: " + str, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Vị trí: " + entityParent.getViTri(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Công ty chỉ mở nước khi khách hàng thanh toán hết nợ và chi phí mở nước.", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Ngày in: " + CLocal.getTime(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Website Công ty: https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("XIN CẢM ƠN", 1, toadoY, 50, 1, 1));
//            stringBuilder.append(printEnd_EZAppend());
//            stringBuilder.append("}");
//            outputStream.write(stringBuilder.toString().getBytes());
//            outputStream.flush();
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }

    public void printDongNuoc2_EZ(CEntityParent entityParent) throws IOException {
        try {
            printTop_EZ();
            printEZ("BIÊN BẢN TẠM", 4, toadoY, 80, 2, 1);
            printEZ("NGƯNG CUNG CẤP NƯỚC LẦN 2", 4, toadoY, 40, 2, 1);
            printEZ("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1);
            printEZ("Hôm nay: ngày " + entityParent.getNgayDN(), 3, toadoY, 0, 1, 1);
            printEZ("Tiến hành tạm ngưng cung cấp nước tại địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1);
            printEZ("Khách hàng: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1);
            printEZ("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1);
            printEZ("MLT: " + entityParent.getMLT(), 3, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            printEZ("Lý do: nợ tiền nước kỳ", 1, toadoY, 0, 1, 1);
            int TongCong = 0;
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                printEZ("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ"), 3, toadoY, 0, 1, 1);
                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
            }
            printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ"), 3, toadoY, 0, 1, 1);
            printEZ("Chi phí mở nước là: " + (Integer.parseInt(CLocal.getPhiMoNuoc(entityParent.getCo())) * 2) + "đ/lần", 3, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            printEZ("Hiệu: " + entityParent.getHieu() + " Cỡ: " + entityParent.getCo(), 1, toadoY, 0, 1, 1);
            printEZ("Số thân: " + entityParent.getSoThan(), 1, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            printEZ("Chỉ số: " + entityParent.getChiSoDN(), 1, toadoY, 0, 1, 1);
            printEZ("Mã chì: " + entityParent.getNiemChi(), 1, toadoY, 0, 1, 1);
            printEZ("Chì thân: " + entityParent.getChiMatSo(), 1, toadoY, 0, 1, 1);
            printEZ("Chì gốc: " + entityParent.getChiKhoaGoc(), 1, toadoY, 0, 1, 1);
            String str = "";
            if (entityParent.isKhoaTu() == true)
                str = "Khóa Từ";
            else
                str = "Khóa Chì";
            printEZ("Khóa nước: " + str, 1, toadoY, 0, 1, 1);
            printEZ("Vị trí: " + entityParent.getViTri(), 1, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            printEZ("Công ty chỉ mở nước khi khách hàng thanh toán hết nợ và chi phí mở nước.", 1, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
            printEZ("Điện thoại/Zalo: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
//            printEZ("Zalo: " + CLocal.Zalo, 1, toadoY, 0, 1, 1);
            printEZ("Ngày in: " + CLocal.getTime(), 3, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            printEZ("https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
//            printEZ("XIN CẢM ƠN", 1, toadoY, 50, 1, 1);
            printEnd_EZ();
            outputStream.flush();
        } catch (Exception ex) {
            throw ex;
        }
    }

//    public void printDongNuoc2_EZAppend(CEntityParent entityParent) {
//        try {
//            printTop_EZAppend();
//            stringBuilder.append(printEZAppend("BIÊN BẢN TẠM", 4, toadoY, 80, 2, 1));
//            stringBuilder.append(printEZAppend("NGƯNG CUNG CẤP NƯỚC LẦN 2", 4, toadoY, 40, 2, 1));
//            stringBuilder.append(printEZAppend("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1));
//            stringBuilder.append(printEZAppend("Hôm nay: ngày " + entityParent.getNgayDN(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Tiến hành tạm ngưng cung cấp nước tại địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Khách hàng: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("MLT: " + entityParent.getMLT(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Lý do: nợ tiền nước kỳ", 1, toadoY, 0, 1, 1));
//            int TongCong = 0;
//            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
//                stringBuilder.append(printEZAppend("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ"), 3, toadoY, 0, 1, 1));
//                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
//            }
//            stringBuilder.append(printEZAppend("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ"), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Chi phí mở nước là: " + (Integer.parseInt(CLocal.getPhiMoNuoc(entityParent.getCo())) * 2) + "đ/lần", 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Hiệu: " + entityParent.getHieu() + " Cỡ: " + entityParent.getCo(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Số thân: " + entityParent.getSoThan(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Chỉ số: " + entityParent.getChiSoDN(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Mã chì: " + entityParent.getNiemChi(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Chì thân: " + entityParent.getChiMatSo(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Chì gốc: " + entityParent.getChiKhoaGoc(), 1, toadoY, 0, 1, 1));
//            String str = "";
//            if (entityParent.isKhoaTu() == true)
//                str = "Khóa Từ";
//            else
//                str = "Khóa Chì";
//            stringBuilder.append(printEZAppend("Khóa nước: " + str, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Vị trí: " + entityParent.getViTri(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Công ty chỉ mở nước khi khách hàng thanh toán hết nợ và chi phí mở nước.", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Ngày in: " + CLocal.getTime(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Website Công ty: https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("XIN CẢM ƠN", 1, toadoY, 50, 1, 1));
//            stringBuilder.append(printEnd_EZAppend());
//            stringBuilder.append("}");
//            outputStream.write(stringBuilder.toString().getBytes());
//            outputStream.flush();
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }

    public void printMoNuoc_EZ(CEntityParent entityParent) throws IOException {
        try {
            printTop_EZ();
            printEZ("BIÊN BẢN MỞ NƯỚC", 4, toadoY, 60, 2, 1);
            printEZ("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1);
            printEZ("Hôm nay: ngày " + entityParent.getNgayMN(), 3, toadoY, 0, 1, 1);
            printEZ("Tiến hành mở nước tại địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1);
            printEZ("Hôm nay: ngày " + entityParent.getNgayMN(), 3, toadoY, 0, 1, 1);
            printEZ("Khách hàng: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1);
            printEZ("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1);
            printEZ("MLT: " + entityParent.getMLT(), 3, toadoY, 0, 1, 1);
            printEnd_EZ();
            printEZ("Hiệu: " + entityParent.getHieu() + " Cỡ: " + entityParent.getCo(), 1, toadoY, 0, 1, 1);
            printEZ("Số thân: " + entityParent.getSoThan(), 1, toadoY, 0, 1, 1);
            printEnd_EZ();
            printEZ("Chỉ số: " + entityParent.getChiSoMN(), 1, toadoY, 0, 1, 1);
            printEZ("Chì thân: " + entityParent.getChiMatSo(), 1, toadoY, 0, 1, 1);
            printEZ("Chì gốc: " + entityParent.getChiKhoaGoc(), 1, toadoY, 0, 1, 1);
            String str = "";
            if (entityParent.isKhoaTu() == true)
                str = "Khóa Từ";
            else
                str = "Khóa Chì";
            printEZ("Khóa nước: " + str, 1, toadoY, 0, 1, 1);
            printEZ("Vị trí: " + entityParent.getViTri(), 1, toadoY, 0, 1, 1);
            printEnd_EZ();
            printEZ("Công ty chỉ mở nước khi khách hàng thanh toán hết nợ và chi phí mở nước.", 1, toadoY, 0, 1, 1);
            printEnd_EZ();
            printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
            printEZ("Điện thoại/Zalo: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
//            printEZ("Zalo: " + CLocal.Zalo, 1, toadoY, 0, 1, 1);
            printEZ("Ngày in: " + CLocal.getTime(), 3, toadoY, 0, 1, 1);
            printEnd_EZ();
            printEZ("https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
//            printEZ("XIN CẢM ƠN", 1, toadoY, 50, 1, 1);
            printEnd_EZ();
            outputStream.flush();
        } catch (Exception ex) {
            throw ex;
        }
    }

//    public void printMoNuoc_EZAppend(CEntityParent entityParent) {
//        try {
//            printTop_EZAppend();
//            stringBuilder.append(printEZAppend("BIÊN BẢN MỞ NƯỚC", 4, toadoY, 60, 2, 1));
//            stringBuilder.append(printEZAppend("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1));
//            stringBuilder.append(printEZAppend("Hôm nay: ngày " + entityParent.getNgayMN(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Tiến hành mở nước tại địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Hôm nay: ngày " + entityParent.getNgayMN(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Khách hàng: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("MLT: " + entityParent.getMLT(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Hiệu: " + entityParent.getHieu() + " Cỡ: " + entityParent.getCo(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Số thân: " + entityParent.getSoThan(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Chỉ số: " + entityParent.getChiSoMN(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Chì thân: " + entityParent.getChiMatSo(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Chì gốc: " + entityParent.getChiKhoaGoc(), 1, toadoY, 0, 1, 1));
//            String str = "";
//            if (entityParent.isKhoaTu() == true)
//                str = "Khóa Từ";
//            else
//                str = "Khóa Chì";
//            stringBuilder.append(printEZAppend("Khóa nước: " + str, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Vị trí: " + entityParent.getViTri(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Công ty chỉ mở nước khi khách hàng thanh toán hết nợ và chi phí mở nước.", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Ngày in: " + CLocal.getTime(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Website Công ty: https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("XIN CẢM ƠN", 1, toadoY, 50, 1, 1));
//            stringBuilder.append(printEnd_EZAppend());
//            stringBuilder.append("}");
//            outputStream.write(stringBuilder.toString().getBytes());
//            outputStream.flush();
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }

    public void printPhiMoNuoc_EZ(CEntityParent entityParent) throws IOException {
        try {
            printTop_EZ();
            printEZ("BIÊN NHẬN", 4, toadoY, 130, 2, 1);
            printEZ("THU TIỀN PHÍ MỞ NƯỚC", 4, toadoY, 40, 2, 1);
            printEZ("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1);
            printEZ("Ngày thu: " + entityParent.getLstHoaDon().get(0).getNgayGiaiTrach(), 3, toadoY, 20, 1, 1);
            printEZ("Khách hàng: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1);
            printEZ("Địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1);
            printEZ("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1);
            printEZ("MLT: " + entityParent.getMLT(), 3, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            if (entityParent.isDongNuoc2() == true) {
                printEZ("Ngày đóng nước lần 1: " + entityParent.getNgayDN1(), 1, toadoY, 0, 1, 1);
                printEZ("Ngày đóng nước lần 2: " + entityParent.getNgayDN(), 1, toadoY, 0, 1, 1);
            } else {
                printEZ("Ngày đóng nước lần 1: " + entityParent.getNgayDN(), 1, toadoY, 0, 1, 1);
            }
            printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(0).getPhiMoNuoc()), "đ"), 3, toadoY, 0, 1, 1);
            printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityParent.getLstHoaDon().get(0).getPhiMoNuoc()), 1, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
            printEZ("Điện thoại/Zalo: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
//            printEZ("Zalo: " + CLocal.Zalo, 1, toadoY, 0, 1, 1);
            printEZ("Ngày in: " + CLocal.getTime(), 3, toadoY, 0, 1, 1);
            printDotFeed_EZ();
            printEZ("https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
//            printEZ("XIN CẢM ƠN", 1, toadoY, 50, 1, 1);
            printEnd_EZ();
            outputStream.flush();
        } catch (Exception ex) {

        }
    }

//    public void printPhiMoNuoc_EZAppend(CEntityParent entityParent) {
//        try {
//            printTop_EZAppend();
//            stringBuilder.append(printEZAppend("BIÊN NHẬN", 4, toadoY, 130, 2, 1));
//            stringBuilder.append(printEZAppend("THU TIỀN PHÍ MỞ NƯỚC", 4, toadoY, 40, 2, 1));
//            stringBuilder.append(printEZAppend("(KHÔNG THAY THẾ HÓA ĐƠN)", 4, toadoY, 20, 2, 1));
//            stringBuilder.append(printEZAppend("Ngày thu: " + entityParent.getLstHoaDon().get(0).getNgayGiaiTrach(), 3, toadoY, 20, 1, 1));
//            stringBuilder.append(printEZAppend("Khách hàng: " + entityParent.getHoTen(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Địa chỉ: " + entityParent.getDiaChi(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Danh bộ (Mã KH): " + entityParent.getDanhBo(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("MLT: " + entityParent.getMLT(), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            if (entityParent.isDongNuoc2() == true) {
//                stringBuilder.append(printEZAppend("Ngày đóng nước lần 1: " + entityParent.getNgayDN1(), 1, toadoY, 0, 1, 1));
//                stringBuilder.append(printEZAppend("Ngày đóng nước lần 2: " + entityParent.getNgayDN(), 1, toadoY, 0, 1, 1));
//            } else {
//                stringBuilder.append(printEZAppend("Ngày đóng nước lần 1: " + entityParent.getNgayDN(), 1, toadoY, 0, 1, 1));
//            }
//            stringBuilder.append(printEZAppend("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(0).getPhiMoNuoc()), "đ"), 3, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityParent.getLstHoaDon().get(0).getPhiMoNuoc()), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("Ngày in: " + CLocal.getTime(), 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printDotFeed_EZAppend());
//            stringBuilder.append(printEZAppend("Website Công ty: https://cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1));
//            stringBuilder.append(printEZAppend("XIN CẢM ƠN", 1, toadoY, 50, 1, 1));
//            stringBuilder.append(printEnd_EZAppend());
//            stringBuilder.append("}");
//            outputStream.write(stringBuilder.toString().getBytes());
//            outputStream.flush();
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }

    private void printTop_EZ() throws IOException {
        try {
            toadoY = 0;
            resetPrinter();
//            setLineSpacing();
            printEZ("CTY CP CẤP NƯỚC TÂN HÒA", 3, toadoY, 25, 1, 1);
            printEZ("VĂN PHÒNG GIAO DỊCH", 3, toadoY, 80, 1, 1);
            printEZ("95 PHẠM HỮU CHÍ, P12, Q5", 3, toadoY, 40, 1, 1);
            printEZ("Tổng đài: 1900.6489", 3, toadoY, 80, 1, 1);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void printTop_EZAppend() throws IOException {
        try {
            toadoY = 0;
            resetPrinter();
            outputStream.write(ESC);
            stringBuilder = new StringBuilder();
            stringBuilder.append("EZ{PRINT:");
            stringBuilder.append(printEZAppend("CTY CP CẤP NƯỚC TÂN HÒA", 3, toadoY, 25, 1, 1));
            stringBuilder.append(printEZAppend("VĂN PHÒNG GIAO DỊCH", 3, toadoY, 80, 1, 1));
            stringBuilder.append(printEZAppend("95 PHẠM HỮU CHÍ, P12, Q5", 3, toadoY, 40, 1, 1));
            stringBuilder.append(printEZAppend("Tổng đài: 1900.6489", 3, toadoY, 80, 1, 1));
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void printDotFeed_EZ() {
//        printEZ("------------------------------------------------------", 1, toadoY, 0, 1, 1);
    }

    private String printDotFeed_EZAppend() {
        toadoY -= 40;
        return printEZAppend("------------------------------------------------------", 1, toadoY, 0, 1, 1);
    }

    private void printEnd_EZ() throws IOException {
        printEZ("  ", 1, toadoY + 60, 0, 1, 1);
    }

    private String printEnd_EZAppend() {
        return printEZAppend("  ", 1, toadoY + 60, 0, 1, 1);
    }

    private String printEZAppend(String content, int boldNumber, int toadoY, int toadoX, int heightFont, int widthFont) {
        ArrayList<String> valuesOutput = new ArrayList();
        String valueOutput = "";
        if (content.indexOf(" ") == 0)
            valuesOutput.add(content);
        else {
            String[] valuesInput = content.split(" ");
            valueOutput = "";
            for (String valueInput : valuesInput) {
                String temp = valueOutput;
                if (temp.equals("") == true)
                    temp += valueInput;
                else
                    temp += " " + valueInput;
                if (temp.length() <= 34)
                    valueOutput = temp;
                else {
                    valuesOutput.add(valueOutput);
                    valueOutput = valueInput;
                }
            }
            valuesOutput.add(valueOutput);
        }
        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < boldNumber; i++) {
//            builder.append("@" + toadoY + "," + toadoX++ + ":FONTP,HMULT" + heightFont + ",VMULT" + widthFont + "|" + content + "|\n");
//        }
        for (int j = 0; j < valuesOutput.size(); j++) {
            for (int i = 0; i < boldNumber; i++) {
                stringBuilder.append("@" + toadoY + "," + toadoX++ + ":FONTP,HMULT" + heightFont + ",VMULT" + widthFont + "|" + valuesOutput.get(j) + "|");
            }
            if (valuesOutput.size() > 1 && j < valuesOutput.size() - 1)
                switch (heightFont) {
                    case 1:
                        toadoY = toadoY + 30;
                        break;
                    case 2:
                        toadoY = toadoY + 60;
                        break;
                }
        }
        switch (heightFont) {
            case 1:
                ServiceThermalPrinter.toadoY = toadoY + 30;
                break;
            case 2:
                ServiceThermalPrinter.toadoY = toadoY + 60;
                break;
        }

        return builder.toString();
    }

    //print custom
    private void printEZ(String content, int boldNumber, int toadoY, int toadoX, int heightFont, int widthFont) throws IOException {
        try {
            ArrayList<String> valuesOutput = new ArrayList();
            String valueOutput = "";
            if (content.indexOf(" ") == 0)
                valuesOutput.add(content);
            else {
                String[] valuesInput = content.split(" ");
                valueOutput = "";
                for (String valueInput : valuesInput) {
                    String temp = valueOutput;
                    if (temp.equals("") == true)
                        temp += valueInput;
                    else
                        temp += " " + valueInput;
                    if (temp.length() <= 34)
                        valueOutput = temp;
                    else {
                        valuesOutput.add(valueOutput);
                        valueOutput = valueInput;
                    }
                }
                valuesOutput.add(valueOutput);
            }
            outputStream.write(ESC);
            stringBuilder = new StringBuilder();
            stringBuilder.append("EZ{PRINT:");
            for (int j = 0; j < valuesOutput.size(); j++) {
                for (int i = 0; i < boldNumber; i++) {
                    stringBuilder.append("@" + toadoY + "," + toadoX++ + ":TIMNR,HMULT" + heightFont + ",VMULT" + widthFont + "|" + valuesOutput.get(j) + "|");
                }
                toadoY += 30;
            }
            stringBuilder.append("}");
            outputStream.write(stringBuilder.toString().getBytes());
        } catch (Exception ex) {
            throw ex;
        }
    }

    //endregion

    //region ESC/P Command
    ByteArrayOutputStream byteStream;

    public void printThuTien_ESC(CEntityParent entityParent) throws IOException {
        try {
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                if (entityParent.getLstHoaDon().get(i).isDangNgan_DienThoai() == true) {
                    printThuTien_ESC(entityParent, entityParent.getLstHoaDon().get(i));
                }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printThuTien_ESC(CEntityParent entityParent, CEntityChild entityChild) throws IOException {
        try {
            if (entityParent != null && entityChild != null) {
                if (entityChild.isDangNgan_DienThoai() == true) {
                    printTop_ESC();
                    byteStream.write(printLineFeed(1));
                    byteStream.write(setTextStyle(true, 1, 2));
                    byteStream.write("BIÊN NHẬN\n".getBytes());
                    byteStream.write("THU TIỀN NƯỚC\n".getBytes());
//            byteStream.write("(KHÔNG THAY THẾ HÓA ĐƠN)\n".getBytes());
                    byteStream.write(("Kỳ: " + entityChild.getKy() + "\n").getBytes());
                    byteStream.write(printLineFeed(1));
                    byteStream.write(setTextAlign(0));
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write(("Ngày thu: " + entityChild.getNgayGiaiTrach() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Khách hàng: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getHoTen() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Địa chỉ: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getDiaChi() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Danh bộ (Mã KH): ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getDanhBo() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("MLT: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getMLT()).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Code: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityChild.getCode() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Giá biểu: " + entityChild.getGiaBieu() + "   Định mức: " + entityChild.getDinhMuc() + "\n").getBytes());
                    byteStream.write(("Từ: " + entityChild.getTuNgay() + "  Đến: " + entityChild.getDenNgay() + "\n").getBytes());
                    if (entityChild.getCode().equals("F") == false)
                        byteStream.write(("CSC: " + entityChild.getCSC() + "  CSM: " + entityChild.getCSM() + "\n").getBytes());
                    else
                        byteStream.write(("Code F Tạm Tính\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 2));
                    byteStream.write(("Tiêu thụ: " + entityChild.getTieuThu() + "m3" + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityChild.getGiaBan()), "đ") + "\n").getBytes());
                    byteStream.write(("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityChild.getThueGTGT()), "đ") + "\n").getBytes());
                    byteStream.write(("Tiền DV thoát nước: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT()), "đ") + "\n").getBytes());
                    byteStream.write(("Thuế DV thoát nước: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT_Thue()), "đ") + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 2));
                    byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(Integer.parseInt(entityChild.getTongCong()) + entityChild.getTienDuTruocDCHD()), "đ") + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    if (entityChild.getTienDuTruocDCHD() > 0 && entityParent.isXoaDCHD() == false) {
                        byteStream.write(("Tiền dư: " + CLocal.formatMoney(String.valueOf(entityChild.getTienDuTruocDCHD()), "đ") + "\n").getBytes());
                        byteStream.write(("Tộng cộng tiền thanh toán:\n " + CLocal.formatMoney(String.valueOf(entityChild.getTongCong()), "đ") + "\n").getBytes());
                        byteStream.write(setTextStyle(false, 1, 1));
                        byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(entityChild.getTongCong())) + "\n").getBytes());
                    } else {
                        byteStream.write(setTextStyle(false, 1, 1));
                        byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(Integer.parseInt(entityChild.getTongCong()) + entityChild.getTienDuTruocDCHD())) + "\n").getBytes());
                    }
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
                    byteStream.write(("Điện thoại/Zalo: " + CLocal.DienThoai + "\n").getBytes());
//                    byteStream.write(("Zalo: " + CLocal.Zalo + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write(("Ngày in: " + CLocal.getTime() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write("In hóa đơn https://cskhtanhoa.com.vn/hddt\n".getBytes());
//                    byteStream.write(setTextAlign(1));
//                    byteStream.write("XIN CẢM ƠN\n".getBytes());
//                    byteStream.write(setTextStyle(true, 1, 1));
//                    byteStream.write(("Từ kỳ 04/2022 không thu tiền nước tại nhà\n").getBytes());
                    byteStream.write(printLineFeed(3));
                    outputStream.write(byteStream.toByteArray());
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printPhieuBao_ESC(CEntityParent entityParent) throws IOException, ParseException {
        try {
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                if (entityParent.getLstHoaDon().get(i).getInPhieuBao_Ngay().equals("") == false) {
                    printPhieuBao_ESC(entityParent, entityParent.getLstHoaDon().get(i));
                }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void printPhieuBao_ESC(CEntityParent entityParent, int charWidth) throws IOException, ParseException {
        try {
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                if (entityParent.getLstHoaDon().get(i).getInPhieuBao_Ngay().equals("") == false) {
                    printPhieuBao_ESC(entityParent, entityParent.getLstHoaDon().get(i), charWidth);
                }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printPhieuBaoCT_ESC(CEntityParent entityParent) throws IOException, ParseException {
        try {
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                if (entityParent.getLstHoaDon().get(i).getInPhieuBao_Ngay().equals("") == false) {
                    printPhieuBaoCT_ESC(entityParent, entityParent.getLstHoaDon().get(i));
                }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printPhieuBao_ESC(CEntityParent entityParent, CEntityChild entityChild) throws IOException, ParseException {
        try {
            if (entityParent != null && entityChild != null) {
                if (entityChild.getInPhieuBao_Ngay().equals("") == false && entityChild.isTamThu() == false && entityChild.isThuHo() == false) {
                    printTop_ESC();
                    byteStream.write(printLineFeed(1));
                    byteStream.write(setTextStyle(true, 1, 2));
                    byteStream.write("GIẤY BÁO TIỀN NƯỚC\n".getBytes());
                    byteStream.write("(KHÔNG THAY THẾ HÓA ĐƠN)\n".getBytes());

                    byteStream.write(("Kỳ: " + entityChild.getKy() + "\n").getBytes());
                    byteStream.write(printLineFeed(1));
                    byteStream.write(setTextAlign(0));
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Khách hàng: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getHoTen() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Địa chỉ: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getDiaChi() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Danh bộ (Mã KH): ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getDanhBo() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("MLT: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getMLT()).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Code: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityChild.getCode() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Giá biểu: " + entityChild.getGiaBieu() + "   Định mức: " + entityChild.getDinhMuc() + "\n").getBytes());
                    byteStream.write(("Từ: " + entityChild.getTuNgay() + "  Đến: " + entityChild.getDenNgay() + "\n").getBytes());
                    if (entityChild.getCode().equals("F") == false)
                        byteStream.write(("CSC: " + entityChild.getCSC() + "  CSM: " + entityChild.getCSM() + "\n").getBytes());
                    else
                        byteStream.write(("Code F Tạm Tính\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 2));
                    byteStream.write(("Tiêu thụ: " + entityChild.getTieuThu() + "m3\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityChild.getGiaBan()), "đ") + "\n").getBytes());
                    byteStream.write(("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityChild.getThueGTGT()), "đ") + "\n").getBytes());
                    byteStream.write(("Tiền DV thoát nước: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT()), "đ") + "\n").getBytes());
                    byteStream.write(("Thuế DV thoát nước: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT_Thue()), "đ") + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 2));
                    byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(Integer.parseInt(entityChild.getTongCong()) + entityChild.getTienDuTruocDCHD()), "đ") + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    if (entityChild.getTienDuTruocDCHD() > 0) {
                        byteStream.write(("Tiền dư: " + CLocal.formatMoney(String.valueOf(entityChild.getTienDuTruocDCHD()), "đ") + "\n").getBytes());
                        byteStream.write(("Tổng cộng tiền thanh toán:\n " + CLocal.formatMoney(entityChild.getTongCong(), "đ") + "\n").getBytes());
                    }
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityChild.getTongCong()) + "\n").getBytes());
                    String[] str = entityChild.getInPhieuBao_Ngay().split(" ");
                    Date dateLap = CLocal.DateFormat.parse(entityChild.getInPhieuBao_Ngay());
                    Calendar c = Calendar.getInstance();
                    c.setTime(dateLap);
                    c.add(Calendar.DATE, 3);
                    dateLap = c.getTime();
                    byteStream.write(("QK vui lòng thanh toán tiền nước trước ngày " + CLocal.DateFormat.format(dateLap) + "\n").getBytes());
                    //
                    if (entityParent.getCuaHangThuHo1().equals("") == false) {
                        byteStream.write(setTextStyle(true, 1, 1));
                        byteStream.write(("Dịch vụ Thu Hộ:\n").getBytes());
                        byteStream.write((entityParent.getCuaHangThuHo1() + "\n").getBytes());
                        byteStream.write((entityParent.getCuaHangThuHo2() + "\n").getBytes());
                    }
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
                    byteStream.write(("Điện thoại/Zalo: " + CLocal.DienThoai + "\n").getBytes());
//                    byteStream.write(("Zalo: " + CLocal.Zalo + "\n").getBytes());
                    byteStream.write(("Ngày in: " + CLocal.getTime() + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write(("Ngày gửi: " + entityChild.getInPhieuBao_Ngay() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write("https://cskhtanhoa.com.vn\n".getBytes());
//                    byteStream.write(setTextAlign(1));
//                    byteStream.write("XIN CẢM ƠN\n".getBytes());
//                    byteStream.write(setTextStyle(true, 1, 1));
//                    byteStream.write(("Từ kỳ 04/2022 không thu tiền nước tại nhà\n").getBytes());
                    byteStream.write(printLineFeed(3));
                    outputStream.write(byteStream.toByteArray());
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void printPhieuBao_ESC(CEntityParent entityParent, CEntityChild entityChild, int charWidth) throws IOException, ParseException {
        try {
            if (entityParent != null && entityChild != null) {
                if (entityChild.getInPhieuBao_Ngay().equals("") == false && entityChild.isTamThu() == false && entityChild.isThuHo() == false) {
                    printTop_ESC();
                    byteStream.write(printLineFeed(1));
                    byteStream.write(breakLine(escpStyle("GIẤY BÁO TIỀN NƯỚC (KHÔNG THAY THẾ HÓA ĐƠN)\n", 0b11000), charWidth).getBytes());
                    byteStream.write(breakLine(escpStyle("Kỳ: " + entityChild.getKy() + "\n", 0b11000), charWidth).getBytes());
                    byteStream.write(printLineFeed(1));
                    byteStream.write(setTextAlign(0));
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Khách hàng: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getHoTen() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Địa chỉ: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getDiaChi() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Danh bộ (Mã KH): ").getBytes());
//                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write(escpStyle(entityParent.getDanhBo() + "\n", 0b11000).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("MLT: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getMLT()).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("   Code: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityChild.getCode() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Giá biểu: " + entityChild.getGiaBieu() + "   Định mức: " + entityChild.getDinhMuc() + "\n").getBytes());
                    byteStream.write(("Từ: " + entityChild.getTuNgay() + "  Đến: " + entityChild.getDenNgay() + "\n").getBytes());
                    if (entityChild.getCode().equals("F") == false)
                        byteStream.write(("CSC: " + entityChild.getCSC() + "  CSM: " + entityChild.getCSM() + "\n").getBytes());
                    else
                        byteStream.write(("Code F Tạm Tính\n").getBytes());
//                    byteStream.write(setTextStyle(true, 1, 2));
                    byteStream.write(escpStyle(pad("Tiêu thụ: ", entityChild.getTieuThu() + "m3\n", ' ', charWidth), 0b11000).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(pad("Tiền nước: ", CLocal.formatMoney(String.valueOf(entityChild.getGiaBan()), "đ") + "\n", ' ', charWidth).getBytes());
                    byteStream.write(pad("Thuế GTGT: ", CLocal.formatMoney(String.valueOf(entityChild.getThueGTGT()), "đ") + "\n", ' ', charWidth).getBytes());
                    byteStream.write(pad("Tiền DV thoát nước: ", CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT()), "đ") + "\n", ' ', charWidth).getBytes());
                    byteStream.write(pad("Thuế DV thoát nước: ", CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT_Thue()), "đ") + "\n", ' ', charWidth).getBytes());
                    byteStream.write(escpStyle(pad("Tổng cộng: ", CLocal.formatMoney(String.valueOf(Integer.parseInt(entityChild.getTongCong()) + entityChild.getTienDuTruocDCHD()), "đ") + "\n", ' ', charWidth), 0b11000).getBytes());
                    if (entityChild.getTienDuTruocDCHD() > 0) {
                        byteStream.write(escpStyle(pad("Tiền dư: ", CLocal.formatMoney(String.valueOf(entityChild.getTienDuTruocDCHD()), "đ") + "\n", ' ', charWidth), 0b11000).getBytes());
                        byteStream.write(escpStyle(pad("Tổng cộng tiền thanh toán: ", CLocal.formatMoney(entityChild.getTongCong(), "đ") + "\n", ' ', charWidth), 0b11000).getBytes());
                    }
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(breakLine("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityChild.getTongCong()) + "\n", charWidth).getBytes());
                    String[] str = entityChild.getInPhieuBao_Ngay().split(" ");
                    Date dateLap = CLocal.DateFormat.parse(entityChild.getInPhieuBao_Ngay());
                    Calendar c = Calendar.getInstance();
                    c.setTime(dateLap);
                    c.add(Calendar.DATE, 3);
                    dateLap = c.getTime();
                    byteStream.write(("Qúy khách vui lòng thanh toán tiền nước trước ngày " + escpStyle(CLocal.DateFormatShort.format(dateLap), 0b01000) + "\n").getBytes());
                    //
                    if (entityParent.getCuaHangThuHo1().equals("") == false) {
                        byteStream.write(setTextStyle(true, 1, 1));
                        byteStream.write(("Dịch vụ Thu Hộ:\n").getBytes());
                        byteStream.write(breakLine(entityParent.getCuaHangThuHo1() + "\n", charWidth).getBytes());
                        byteStream.write(breakLine(entityParent.getCuaHangThuHo2() + "\n", charWidth).getBytes());
                    }
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
                    byteStream.write(("Điện thoại/Zalo: " + CLocal.DienThoai + "\n").getBytes());
//                    byteStream.write(("Zalo: " + CLocal.Zalo + "\n").getBytes());
                    byteStream.write(("Ngày in: " + CLocal.getTime() + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write(("Ngày gửi: " + entityChild.getInPhieuBao_Ngay() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write("Website: https://cskhtanhoa.com.vn\n".getBytes());
//                    byteStream.write(setTextAlign(1));
//                    byteStream.write("XIN CẢM ƠN\n".getBytes());
//                    byteStream.write(setTextStyle(true, 1, 1));
//                    byteStream.write(("Từ kỳ 04/2022 không thu tiền nước tại nhà\n").getBytes());
                    byteStream.write(printLineFeed(3));
                    outputStream.write(byteStream.toByteArray());
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printPhieuBaoCT_ESC(CEntityParent entityParent, CEntityChild entityChild) throws IOException, ParseException {
        try {
            if (entityParent != null && entityChild != null) {
                if (entityChild.getInPhieuBao_Ngay().equals("") == false && entityChild.isTamThu() == false && entityChild.isThuHo() == false) {
                    printTop_ESC();
                    byteStream.write(printLineFeed(1));
                    byteStream.write(setTextStyle(true, 1, 2));
                    byteStream.write("GIẤY BÁO TIỀN NƯỚC\n".getBytes());
                    byteStream.write("(KHÔNG THAY THẾ HÓA ĐƠN)\n".getBytes());

                    byteStream.write(("Kỳ: " + entityChild.getKy() + "\n").getBytes());
                    byteStream.write(printLineFeed(1));
                    byteStream.write(setTextAlign(0));
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Khách hàng: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getHoTen() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Địa chỉ: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getDiaChi() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Danh bộ (Mã KH): ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getDanhBo() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("MLT: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getMLT()).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Code: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityChild.getCode() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Giá biểu: " + entityChild.getGiaBieu() + "   Định mức: " + entityChild.getDinhMuc() + "\n").getBytes());
                    byteStream.write(("Từ: " + entityChild.getTuNgay() + "  Đến: " + entityChild.getDenNgay() + "\n").getBytes());
                    if (entityChild.getCode().equals("F") == false)
                        byteStream.write(("CSC: " + entityChild.getCSC() + "  CSM: " + entityChild.getCSM() + "\n").getBytes());
                    else
                        byteStream.write(("Code F Tạm Tính\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 2));
                    byteStream.write(("Tiêu thụ: " + entityChild.getTieuThu() + "m3\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    String[] ChiTiets = entityChild.getChiTietTienNuoc().split("\r\n");
                    for (String chitiet : ChiTiets) {
                        byteStream.write((chitiet + "\n").getBytes());
                    }
                    byteStream.write(("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityChild.getGiaBan()), "đ") + "\n").getBytes());
                    byteStream.write(("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityChild.getThueGTGT()), "đ") + "\n").getBytes());
                    byteStream.write(("Tiền DV thoát nước: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT()), "đ") + "\n").getBytes());
                    byteStream.write(("Thuế DV thoát nước: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT_Thue()), "đ") + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 2));
                    byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(Integer.parseInt(entityChild.getTongCong()) + entityChild.getTienDuTruocDCHD()), "đ") + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    if (entityChild.getTienDuTruocDCHD() > 0) {
                        byteStream.write(("Tiền dư: " + CLocal.formatMoney(String.valueOf(entityChild.getTienDuTruocDCHD()), "đ") + "\n").getBytes());
                        byteStream.write(("Tổng cộng tiền thanh toán:\n " + CLocal.formatMoney(entityChild.getTongCong(), "đ") + "\n").getBytes());
                    }
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityChild.getTongCong()) + "\n").getBytes());
                    String[] str = entityChild.getInPhieuBao_Ngay().split(" ");
                    Date dateLap = CLocal.DateFormat.parse(entityChild.getInPhieuBao_Ngay());
                    Calendar c = Calendar.getInstance();
                    c.setTime(dateLap);
                    c.add(Calendar.DATE, 7);
                    dateLap = c.getTime();
                    byteStream.write(("QK vui lòng thanh toán tiền nước trước ngày " + CLocal.DateFormat.format(dateLap) + "\n").getBytes());
                    //
                    if (entityParent.getCuaHangThuHo1().equals("") == false) {
                        byteStream.write(setTextStyle(true, 1, 1));
                        byteStream.write(("Dịch vụ Thu Hộ:\n").getBytes());
                        byteStream.write((entityParent.getCuaHangThuHo1() + "\n").getBytes());
                        byteStream.write((entityParent.getCuaHangThuHo2() + "\n").getBytes());
                    }
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
                    byteStream.write(("Điện thoại/Zalo: " + CLocal.DienThoai + "\n").getBytes());
//                    byteStream.write(("Zalo: " + CLocal.Zalo + "\n").getBytes());
                    byteStream.write(("Ngày in: " + CLocal.getTime() + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write(("Ngày gửi: " + entityChild.getInPhieuBao_Ngay() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write("https://cskhtanhoa.com.vn\n".getBytes());
//                    byteStream.write(setTextAlign(1));
//                    byteStream.write("XIN CẢM ƠN\n".getBytes());
//                    byteStream.write(setTextStyle(true, 1, 1));
//                    byteStream.write(("Từ kỳ 04/2022 không thu tiền nước tại nhà\n").getBytes());
                    byteStream.write(printLineFeed(3));
                    outputStream.write(byteStream.toByteArray());
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printPhieuBao2_ESC(CEntityParent entityParent) throws IOException {
        try {
            if (entityParent != null) {
                if (entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().equals("") == false) {
                    printTop_ESC();
                    byteStream.write(printLineFeed(1));
                    byteStream.write(setTextStyle(true, 1, 2));
                    byteStream.write(("THÔNG BÁO TIỀN NƯỚC\n").getBytes());
                    byteStream.write(("CHƯA THANH TOÁN\n").getBytes());
                    byteStream.write("(KHÔNG THAY THẾ HÓA ĐƠN)\n".getBytes());
                    byteStream.write(printLineFeed(1));
                    byteStream.write(setTextAlign(0));
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Kính gửi: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getHoTen() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Địa chỉ: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getDiaChi() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Danh bộ (Mã KH): ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getDanhBo() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("MLT: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getMLT()).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("   Code: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getLstHoaDon().get(0).getCode() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
//                    byteStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
//                    if (entityParent.getLstHoaDon().get(0).getCode().equals("F") == false)
//                        byteStream.write(("CSC: " + entityParent.getLstHoaDon().get(0).getCSC() + "  CSM: " + entityParent.getLstHoaDon().get(0).getCSM() + "  Tiêu thụ: " + entityParent.getLstHoaDon().get(0).getTieuThu() + "m3\n").getBytes());
//                    else
//                        byteStream.write(("Code F Tạm Tính\n").getBytes());
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("Hóa đơn:\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    int TongCong = 0, TienDu = 0;
                    for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                        if (entityParent.getLstHoaDon().get(i).isTamThu() == false && entityParent.getLstHoaDon().get(i).isThuHo() == false) {
                            byteStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(String.valueOf(Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong()) + entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD()), "đ") + "\n").getBytes());
                            TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong()) + entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
                            TienDu += entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
                        }
                    byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    if (TienDu > 0) {
                        byteStream.write(("Tiền dư: " + CLocal.formatMoney(String.valueOf(TienDu), "đ") + "\n").getBytes());
                        byteStream.write(("Tổng cộng tiền thanh toán:\n " + CLocal.formatMoney(String.valueOf(TongCong - TienDu), "đ") + "\n").getBytes());
                        byteStream.write(setTextStyle(false, 1, 1));
                        byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong - TienDu)) + "\n").getBytes());
                    } else {
                        byteStream.write(setTextStyle(false, 1, 1));
                        byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + "\n").getBytes());
                    }
//                    String[] str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_NgayHen().split(" ");
                    Date dt = new Date();
                    Calendar c = Calendar.getInstance();
                    c.setTime(dt);
                    c.add(Calendar.DATE, 3);
                    dt = c.getTime();
                    byteStream.write(("Ghi chú: Quý khách vui lòng thanh toán tiền nước trước ngày ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((CLocal.DateFormatShort.format(dt)).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write((". Nếu Quý khách đã thanh toán vui lòng bỏ qua thông báo này.\n").getBytes());
                    if (entityParent.getCuaHangThuHo1().equals("") == false) {
                        byteStream.write(setTextStyle(true, 1, 1));
                        byteStream.write(("Dịch vụ Thu Hộ:\n").getBytes());
                        byteStream.write((entityParent.getCuaHangThuHo1() + "\n").getBytes());
                        byteStream.write((entityParent.getCuaHangThuHo2() + "\n").getBytes());
                    }
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
                    byteStream.write(("Điện thoại/Zalo: " + CLocal.DienThoai + "\n").getBytes());
//                    byteStream.write(("Zalo: " + CLocal.Zalo + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write(("Ngày lập: " + entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay() + "\n").getBytes());
                    byteStream.write(("Ngày in: " + CLocal.getTime() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("https://cskhtanhoa.com.vn\n").getBytes());
//                    byteStream.write(setTextAlign(1));
//                    byteStream.write(("XIN CẢM ƠN\n").getBytes());
//                    byteStream.write(setTextStyle(true, 1, 1));
//                    byteStream.write(("Từ kỳ 04/2022 không thu tiền nước tại nhà\n").getBytes());
                    byteStream.write(printLineFeed(3));
                    outputStream.write(byteStream.toByteArray());
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void printPhieuBao2_ESC(CEntityParent entityParent, int charWidth) throws IOException {
        try {
            if (entityParent != null) {
                if (entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().equals("") == false) {
                    printTop_ESC();
                    byteStream.write(printLineFeed(1));
                    byteStream.write(setTextStyle(true, 1, 2));
                    byteStream.write(breakLine("THÔNG BÁO TIỀN NƯỚC CHƯA THANH TOÁN (KHÔNG THAY THẾ HÓA ĐƠN)", charWidth).getBytes());
                    byteStream.write(printLineFeed(1));
                    byteStream.write(setTextAlign(0));
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Kính gửi: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getHoTen() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Địa chỉ: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getDiaChi() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Danh bộ (Mã KH): ").getBytes());
//                    byteStream.write(setTextStyle(true, 1, 2));
                    byteStream.write(escpStyle(entityParent.getDanhBo() + "\n", 0b11000).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("MLT: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getMLT()).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("   Code: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getLstHoaDon().get(0).getCode() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
//                    byteStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
//                    if (entityParent.getLstHoaDon().get(0).getCode().equals("F") == false)
//                        byteStream.write(("CSC: " + entityParent.getLstHoaDon().get(0).getCSC() + "  CSM: " + entityParent.getLstHoaDon().get(0).getCSM() + "  Tiêu thụ: " + entityParent.getLstHoaDon().get(0).getTieuThu() + "m3\n").getBytes());
//                    else
//                        byteStream.write(("Code F Tạm Tính\n").getBytes());
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("Hóa đơn:\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    int TongCong = 0, TienDu = 0;
                    for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                        if (entityParent.getLstHoaDon().get(i).isTamThu() == false && entityParent.getLstHoaDon().get(i).isThuHo() == false) {
                            byteStream.write(pad("Kỳ: " + entityParent.getLstHoaDon().get(i).getKy(), CLocal.formatMoney(String.valueOf(Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong()) + entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD()), "đ"), ' ', charWidth).getBytes());
                            TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong()) + entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
                            TienDu += entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
                        }
                    byteStream.write(pad("Tổng cộng: ", CLocal.formatMoney(String.valueOf(TongCong), "đ"), ' ', charWidth).getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    if (TienDu > 0) {
                        byteStream.write(pad("Tiền dư: ", CLocal.formatMoney(String.valueOf(TienDu), "đ"), ' ', charWidth).getBytes());
                        byteStream.write(pad("Tổng cộng tiền thanh toán: ", CLocal.formatMoney(String.valueOf(TongCong - TienDu), "đ"), ' ', charWidth).getBytes());
                        byteStream.write(setTextStyle(false, 1, 1));
                        byteStream.write(breakLine("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong - TienDu)) + "\n", charWidth).getBytes());
                    } else {
                        byteStream.write(setTextStyle(false, 1, 1));
                        byteStream.write(breakLine("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + "\n", charWidth).getBytes());
                    }
//                    String[] str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_NgayHen().split(" ");
                    byteStream.write(printDotFeed_ESC());
                    Date dt = new Date();
                    Calendar c = Calendar.getInstance();
                    c.setTime(dt);
                    c.add(Calendar.DATE, 3);
                    dt = c.getTime();
                    byteStream.write(breakLine(escpStyle("Ghi chú:", 0b01000) + " Quý khách vui lòng thanh toán tiền nước trước ngày " + escpStyle(CLocal.DateFormatShort.format(dt), 0b01000) + ". Nếu Quý khách đã thanh toán vui lòng bỏ qua thông báo này.\n", charWidth).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    if (entityParent.getCuaHangThuHo1().equals("") == false) {
                        byteStream.write(setTextStyle(true, 1, 1));
                        byteStream.write(("Dịch vụ Thu Hộ:\n").getBytes());
                        byteStream.write(breakLine(entityParent.getCuaHangThuHo1() + "\n", charWidth).getBytes());
                        byteStream.write(breakLine(entityParent.getCuaHangThuHo2() + "\n", charWidth).getBytes());
                    }
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
                    byteStream.write(("Điện thoại/Zalo: " + CLocal.DienThoai + "\n").getBytes());
//                    byteStream.write(("Zalo: " + CLocal.Zalo + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write(("Ngày lập: " + entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay() + "\n").getBytes());
                    byteStream.write(("Ngày in: " + CLocal.getTime() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("Website: https://cskhtanhoa.com.vn\n").getBytes());
//                    byteStream.write(setTextAlign(1));
//                    byteStream.write(("XIN CẢM ƠN\n").getBytes());
//                    byteStream.write(setTextStyle(true, 1, 1));
//                    byteStream.write(("Từ kỳ 04/2022 không thu tiền nước tại nhà\n").getBytes());
                    byteStream.write(printLineFeed(3));
                    outputStream.write(byteStream.toByteArray());
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printTBDongNuoc_ESC(CEntityParent entityParent) throws IOException {
        try {
            if (entityParent != null) {
                if (entityParent.getLstHoaDon().get(entityParent.getLstHoaDon().size() - 1).getTBDongNuoc_Ngay().equals("") == false) {
                    printTop_ESC();
                    byteStream.write(printLineFeed(1));
                    byteStream.write(setTextStyle(true, 1, 2));
                    byteStream.write(("THÔNG BÁO\n").getBytes());
                    byteStream.write(("NGƯNG CUNG CẤP NƯỚC\n").getBytes());
                    byteStream.write("(KHÔNG THAY THẾ HÓA ĐƠN)\n".getBytes());
                    byteStream.write(printLineFeed(1));
                    byteStream.write(setTextAlign(0));
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Kính gửi: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getHoTen() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Địa chỉ: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getDiaChi() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Danh bộ (Mã KH): ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getDanhBo() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("MLT: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getMLT()).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Code: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getLstHoaDon().get(0).getCode() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
//                    byteStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
//                    if (entityParent.getLstHoaDon().get(0).getCode().equals("F") == false)
//                        byteStream.write(("CSC: " + entityParent.getLstHoaDon().get(0).getCSC() + "  CSM: " + entityParent.getLstHoaDon().get(0).getCSM() + "  Tiêu thụ: " + entityParent.getLstHoaDon().get(0).getTieuThu() + "m3\n").getBytes());
//                    else
//                        byteStream.write(("Code F Tạm Tính\n").getBytes());
                    byteStream.write(printDotFeed_ESC());
                    String[] str = entityParent.getLstHoaDon().get(entityParent.getLstHoaDon().size() - 1).getTBDongNuoc_NgayHen().split(" ");
                    byteStream.write(("Công ty sẽ tạm ngưng cung cấp nước tại địa chỉ trên từ ngày ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((str[0] + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Lý do: Quý khách chưa thanh toán hóa đơn tiền nước:\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    int TongCong = 0, TienDu = 0;
                    for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                        if (entityParent.getLstHoaDon().get(i).isTamThu() == false && entityParent.getLstHoaDon().get(i).isThuHo() == false) {
                            byteStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(String.valueOf(Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong()) + entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD()), "đ") + "\n").getBytes());
                            TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong()) + entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
                            TienDu += entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
                        }
                    byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
                    if (TienDu > 0) {
                        byteStream.write(("Tiền dư: " + CLocal.formatMoney(String.valueOf(TienDu), "đ") + "\n").getBytes());
                        byteStream.write(("Tổng cộng tiền thanh toán:\n " + CLocal.formatMoney(String.valueOf(TongCong - TienDu), "đ") + "\n").getBytes());
                        byteStream.write(setTextStyle(false, 1, 1));
                        byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong - TienDu)) + "\n").getBytes());
                    } else {
                        byteStream.write(setTextStyle(false, 1, 1));
                        byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + "\n").getBytes());
                    }
                    byteStream.write(printDotFeed_ESC());
                    String Co = "";
                    if (entityParent.getLstHoaDon().get(0).getCo().equals("") == true)
                        Co = entityParent.getCo();
                    else
                        Co = entityParent.getLstHoaDon().get(0).getCo();
                    byteStream.write(("Phí mở nước: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((CLocal.getPhiMoNuoc(Co) + "đ. (nếu khách hàng bị khóa nước)\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Ghi chú: Quý khách thanh toán tiền nước trước ngày " + str[0] + ". Nếu đã thanh toán vui lòng bỏ qua thông báo này\n").getBytes());
                    if (entityParent.getCuaHangThuHo1().equals("") == false) {
                        byteStream.write(setTextStyle(true, 1, 1));
                        byteStream.write(("Dịch vụ Thu Hộ:\n").getBytes());
                        byteStream.write((entityParent.getCuaHangThuHo1() + "\n").getBytes());
                        byteStream.write((entityParent.getCuaHangThuHo2() + "\n").getBytes());
                    }
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
                    byteStream.write(("Điện thoại/Zalo: " + CLocal.DienThoai + "\n").getBytes());
//                    byteStream.write(("Zalo: " + CLocal.Zalo + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write(("Ngày lập: " + entityParent.getLstHoaDon().get(entityParent.getLstHoaDon().size() - 1).getTBDongNuoc_Ngay() + "\n").getBytes());
                    byteStream.write(("Ngày in: " + CLocal.getTime() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("https://cskhtanhoa.com.vn\n").getBytes());
//                    byteStream.write(setTextAlign(1));
//                    byteStream.write(("XIN CẢM ƠN\n").getBytes());
//                    byteStream.write(setTextStyle(true, 1, 1));
//                    byteStream.write(("Từ kỳ 04/2022 không thu tiền nước tại nhà\n").getBytes());
                    byteStream.write(printLineFeed(3));
                    outputStream.write(byteStream.toByteArray());
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void printTBDongNuoc_ESC(CEntityParent entityParent, int charWidth) throws IOException {
        try {
            if (entityParent != null) {
                if (entityParent.getLstHoaDon().get(entityParent.getLstHoaDon().size() - 1).getTBDongNuoc_Ngay().equals("") == false) {
                    printTop_ESC();
                    byteStream.write(printLineFeed(1));
                    byteStream.write(breakLine(escpStyle("THÔNG BÁO NGƯNG CUNG CẤP NƯỚC (KHÔNG THAY THẾ HÓA ĐƠN)\n", 0b11000), charWidth).getBytes());
                    byteStream.write(printLineFeed(1));
                    byteStream.write(setTextAlign(0));
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Kính gửi: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getHoTen() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Địa chỉ: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getDiaChi() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Danh bộ (Mã KH): ").getBytes());
                    byteStream.write(escpStyle(entityParent.getDanhBo() + "\n", 0b11000).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("MLT: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getMLT()).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(("Code: ").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write((entityParent.getLstHoaDon().get(0).getCode() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
//                    byteStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
//                    if (entityParent.getLstHoaDon().get(0).getCode().equals("F") == false)
//                        byteStream.write(("CSC: " + entityParent.getLstHoaDon().get(0).getCSC() + "  CSM: " + entityParent.getLstHoaDon().get(0).getCSM() + "  Tiêu thụ: " + entityParent.getLstHoaDon().get(0).getTieuThu() + "m3\n").getBytes());
//                    else
//                        byteStream.write(("Code F Tạm Tính\n").getBytes());
                    byteStream.write(printDotFeed_ESC());
                    String[] str = entityParent.getLstHoaDon().get(entityParent.getLstHoaDon().size() - 1).getTBDongNuoc_NgayHen().split(" ");
                    byteStream.write(breakLine("Công ty sẽ tạm ngưng cung cấp nước tại địa chỉ trên từ ngày " + escpStyle(str[0], 0b01000), charWidth).getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(breakLine("Lý do: Quý khách chưa thanh toán hóa đơn tiền nước:\n", charWidth).getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    int TongCong = 0, TienDu = 0;
                    for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                        if (entityParent.getLstHoaDon().get(i).isTamThu() == false && entityParent.getLstHoaDon().get(i).isThuHo() == false) {
                            byteStream.write(pad("Kỳ: " + entityParent.getLstHoaDon().get(i).getKy(), CLocal.formatMoney(String.valueOf(Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong()) + entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD()), "đ") + "\n", ' ', charWidth).getBytes());
                            TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong()) + entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
                            TienDu += entityParent.getLstHoaDon().get(i).getTienDuTruocDCHD();
                        }
                    byteStream.write(escpStyle(pad("Tổng cộng: ", CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n", ' ', charWidth), 0b11000).getBytes());
                    if (TienDu > 0) {
                        byteStream.write(escpStyle(pad("Tiền dư: ", CLocal.formatMoney(String.valueOf(TienDu), "đ") + "\n", ' ', charWidth), 0b11000).getBytes());
                        byteStream.write(escpStyle(pad("Tổng cộng tiền thanh toán: ", CLocal.formatMoney(String.valueOf(TongCong - TienDu), "đ") + "\n", ' ', charWidth), 0b11000).getBytes());
                        byteStream.write(setTextStyle(false, 1, 1));
                        byteStream.write(breakLine("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong - TienDu)) + "\n", charWidth).getBytes());
                    } else {
                        byteStream.write(setTextStyle(false, 1, 1));
                        byteStream.write(breakLine("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + "\n", charWidth).getBytes());
                    }
                    byteStream.write(printDotFeed_ESC());
                    String Co = "";
                    if (entityParent.getLstHoaDon().get(0).getCo().equals("") == true)
                        Co = entityParent.getCo();
                    else
                        Co = entityParent.getLstHoaDon().get(0).getCo();
                    byteStream.write(escpStyle(pad("Phí mở nước: ", CLocal.getPhiMoNuoc(Co) + "đ", ' ', charWidth), 0b01000).getBytes());
                    byteStream.write(escpStyle("(nếu khách hàng bị khóa nước)\n", 0b01000).getBytes());
                    byteStream.write((escpStyle("Ghi chú:", 0b01000) + " Quý khách thanh toán tiền nước trước ngày " + escpStyle(str[0], 0b01000) + ". Nếu đã thanh toán vui lòng bỏ qua thông báo này\n").getBytes());
                    if (entityParent.getCuaHangThuHo1().equals("") == false) {
                        byteStream.write(setTextStyle(true, 1, 1));
                        byteStream.write(("Dịch vụ Thu Hộ:\n").getBytes());
                        byteStream.write(breakLine(entityParent.getCuaHangThuHo1() + "\n", charWidth).getBytes());
                        byteStream.write(breakLine(entityParent.getCuaHangThuHo2() + "\n", charWidth).getBytes());
                    }
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
                    byteStream.write(("Điện thoại/Zalo: " + CLocal.DienThoai + "\n").getBytes());
//                    byteStream.write(("Zalo: " + CLocal.Zalo + "\n").getBytes());
                    byteStream.write(setTextStyle(true, 1, 1));
                    byteStream.write(("Ngày lập: " + entityParent.getLstHoaDon().get(entityParent.getLstHoaDon().size() - 1).getTBDongNuoc_Ngay() + "\n").getBytes());
                    byteStream.write(("Ngày in: " + CLocal.getTime() + "\n").getBytes());
                    byteStream.write(setTextStyle(false, 1, 1));
                    byteStream.write(printDotFeed_ESC());
                    byteStream.write(("Website: https://cskhtanhoa.com.vn\n").getBytes());
//                    byteStream.write(setTextAlign(1));
//                    byteStream.write(("XIN CẢM ƠN\n").getBytes());
//                    byteStream.write(setTextStyle(true, 1, 1));
//                    byteStream.write(("Từ kỳ 04/2022 không thu tiền nước tại nhà\n").getBytes());
                    byteStream.write(printLineFeed(3));
                    outputStream.write(byteStream.toByteArray());
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printDongNuoc_ESC(CEntityParent entityParent) throws IOException {
        try {
            printTop_ESC();
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(true, 1, 2));
            byteStream.write(("BIÊN BẢN TẠM\n").getBytes());
            byteStream.write(("NGƯNG CUNG CẤP NƯỚC LẦN 1\n").getBytes());
            byteStream.write("(KHÔNG THAY THẾ HÓA ĐƠN)\n".getBytes());
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextAlign(0));
            byteStream.write(("Hôm nay: ngày " + entityParent.getNgayDN() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Tiến hành tạm ngưng cung cấp nước tại địa chỉ: ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getDiaChi() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Khách hàng: ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getHoTen() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Danh bộ (Mã KH): ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getDanhBo() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("MLT: ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getMLT() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Lý do: nợ tiền nước kỳ\n").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            int TongCong = 0;
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
//                if (Ky.equals("") == true)
//                    Ky += entityParent.getLstHoaDon().get(i).getKy();
//                else
//                    Ky += ", " + entityParent.getLstHoaDon().get(i).getKy();
                byteStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ") + "\n").getBytes());
                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
            }
            byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
            byteStream.write(("Chi phí mở nước là: " + CLocal.getPhiMoNuoc(entityParent.getCo()) + "đ/lần\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Hiệu: " + entityParent.getHieu() + " Cỡ: " + entityParent.getCo() + "\n").getBytes());
            byteStream.write(("Số thân: " + entityParent.getSoThan() + "\n").getBytes());
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Chỉ số: " + entityParent.getChiSoDN() + "\n").getBytes());
            byteStream.write(("Mã chì: " + entityParent.getNiemChi() + "\n").getBytes());
            byteStream.write(("Chì thân: " + entityParent.getChiMatSo() + "\n").getBytes());
            byteStream.write(("Chì gốc: " + entityParent.getChiKhoaGoc() + "\n").getBytes());
            String str = "";
            if (entityParent.isKhoaTu() == true)
                str = "Khóa Từ";
            else
                str = "Khóa Chì";
            byteStream.write(("Khóa nước: " + str + "\n").getBytes());
            byteStream.write(("Vị trí: " + entityParent.getViTri() + "\n").getBytes());
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Công ty chỉ mở nước khi khách hàng thanh toán hết nợ và chi phí mở nước.\n").getBytes());
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
            byteStream.write(("Điện thoại/Zalo: " + CLocal.DienThoai + "\n").getBytes());
//            byteStream.write(("Zalo: " + CLocal.Zalo + "\n").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write(("Ngày in: " + CLocal.getTime() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("https://cskhtanhoa.com.vn\n").getBytes());
//            byteStream.write(setTextAlign(1));
//            byteStream.write(("XIN CẢM ƠN\n").getBytes());
            byteStream.write(printLineFeed(3));
            outputStream.write(byteStream.toByteArray());
            outputStream.flush();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printDongNuoc2_ESC(CEntityParent entityParent) throws IOException {
        try {
            printTop_ESC();
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(true, 1, 2));
            byteStream.write(("BIÊN BẢN TẠM\n").getBytes());
            byteStream.write(("NGƯNG CUNG CẤP NƯỚC LẦN 2\n").getBytes());
            byteStream.write("(KHÔNG THAY THẾ HÓA ĐƠN)\n".getBytes());
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextAlign(0));
            byteStream.write(("Hôm nay: ngày " + entityParent.getNgayDN() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Tiến hành tạm ngưng cung cấp nước tại địa chỉ: ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getDiaChi() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Khách hàng: ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getHoTen() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Danh bộ (Mã KH): ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getDanhBo() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("MLT: ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getMLT() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Lý do: nợ tiền nước kỳ\n").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            int TongCong = 0;
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
//                if (Ky.equals("") == true)
//                    Ky += entityParent.getLstHoaDon().get(i).getKy();
//                else
//                    Ky += ", " + entityParent.getLstHoaDon().get(i).getKy();
                byteStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ") + "\n").getBytes());
                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
            }
            byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
            byteStream.write(("Chi phí mở nước là: " + (Integer.parseInt(CLocal.getPhiMoNuoc(entityParent.getCo())) * 2) + "đ/lần\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Hiệu: " + entityParent.getHieu() + " Cỡ: " + entityParent.getCo() + "\n").getBytes());
            byteStream.write(("Số thân: " + entityParent.getSoThan() + "\n").getBytes());
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Chỉ số: " + entityParent.getChiSoDN() + "\n").getBytes());
            byteStream.write(("Mã chì: " + entityParent.getNiemChi() + "\n").getBytes());
            byteStream.write(("Chì thân: " + entityParent.getChiMatSo() + "\n").getBytes());
            byteStream.write(("Chì gốc: " + entityParent.getChiKhoaGoc() + "\n").getBytes());
            String str = "";
            if (entityParent.isKhoaTu() == true)
                str = "Khóa Từ";
            else
                str = "Khóa Chì";
            byteStream.write(("Khóa nước: " + str + "\n").getBytes());
            byteStream.write(("Vị trí: " + entityParent.getViTri() + "\n").getBytes());
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Công ty chỉ mở nước khi khách hàng thanh toán hết nợ và chi phí mở nước.\n").getBytes());
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
            byteStream.write(("Điện thoại/Zalo: " + CLocal.DienThoai + "\n").getBytes());
//            byteStream.write(("Zalo: " + CLocal.Zalo + "\n").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write(("Ngày in: " + CLocal.getTime() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("https://cskhtanhoa.com.vn\n").getBytes());
//            byteStream.write(setTextAlign(1));
//            byteStream.write(("XIN CẢM ƠN\n").getBytes());
            byteStream.write(printLineFeed(3));
            outputStream.write(byteStream.toByteArray());
            outputStream.flush();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printMoNuoc_ESC(CEntityParent entityParent) throws IOException {
        try {
            printTop_ESC();
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(true, 1, 2));
            byteStream.write(("BIÊN BẢN MỞ NƯỚC\n").getBytes());
            byteStream.write("(KHÔNG THAY THẾ HÓA ĐƠN)\n".getBytes());
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextAlign(0));
            byteStream.write(("Hôm nay: ngày " + entityParent.getNgayMN() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Tiến hành mở nước tại địa chỉ: ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getDiaChi() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Khách hàng: ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getHoTen() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Danh bộ (Mã KH): ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getDanhBo() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("MLT: ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getMLT() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Hiệu: " + entityParent.getHieu() + " Cỡ: " + entityParent.getCo() + "\n").getBytes());
            byteStream.write(("Số thân: " + entityParent.getSoThan() + "\n").getBytes());
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Chỉ số: " + entityParent.getChiSoMN() + "\n").getBytes());
            byteStream.write(("Chì thân: " + entityParent.getChiMatSo() + "\n").getBytes());
            byteStream.write(("Chì gốc: " + entityParent.getChiKhoaGoc() + "\n").getBytes());
            String str = "";
            if (entityParent.isKhoaTu() == true)
                str = "Khóa Từ";
            else
                str = "Khóa Chì";
            byteStream.write(("Khóa nước: " + str + "\n").getBytes());
            byteStream.write(("Vị trí: " + entityParent.getViTri() + "\n").getBytes());
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Công ty chỉ mở nước khi khách hàng thanh toán hết nợ và chi phí mở nước.\n").getBytes());
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
            byteStream.write(("Điện thoại/Zalo: " + CLocal.DienThoai + "\n").getBytes());
//            byteStream.write(("Zalo: " + CLocal.Zalo + "\n").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write(("Ngày in: " + CLocal.getTime() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("https://cskhtanhoa.com.vn\n").getBytes());
//            byteStream.write(setTextAlign(1));
//            byteStream.write(("XIN CẢM ƠN\n").getBytes());
            byteStream.write(printLineFeed(3));
            outputStream.write(byteStream.toByteArray());
            outputStream.flush();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void printPhiMoNuoc_ESC(CEntityParent entityParent) throws IOException {
        try {
            printTop_ESC();
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(true, 1, 2));
            byteStream.write(("BIÊN NHẬN\n").getBytes());
            byteStream.write(("THU TIỀN PHÍ MỞ NƯỚC\n").getBytes());
            byteStream.write("(KHÔNG THAY THẾ HÓA ĐƠN)\n".getBytes());
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextAlign(0));
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write(("Ngày thu: " + entityParent.getLstHoaDon().get(0).getNgayGiaiTrach() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Khách hàng: ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getHoTen() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Địa chỉ: ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getDiaChi() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Danh bộ (Mã KH): ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getDanhBo() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("MLT: ").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write((entityParent.getMLT() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(printDotFeed_ESC());
            if (entityParent.isDongNuoc2() == true) {
                byteStream.write(("Ngày đóng nước lần 1: " + entityParent.getNgayDN1() + "\n").getBytes());
                byteStream.write(("Ngày đóng nước lần 2: " + entityParent.getNgayDN() + "\n").getBytes());
            } else {
                byteStream.write(("Ngày đóng nước lần 1: " + entityParent.getNgayDN() + "\n").getBytes());
            }
            byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(0).getPhiMoNuoc()), "đ") + "\n").getBytes());
            byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityParent.getLstHoaDon().get(0).getPhiMoNuoc()) + "\n").getBytes());
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
            byteStream.write(("Điện thoại/Zalo: " + CLocal.DienThoai + "\n").getBytes());
//            byteStream.write(("Zalo: " + CLocal.Zalo + "\n").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write(("Ngày in: " + CLocal.getTime() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(printDotFeed_ESC());
            byteStream.write(("https://cskhtanhoa.com.vn\n").getBytes());
//            byteStream.write(setTextAlign(1));
//            byteStream.write(("XIN CẢM ƠN\n").getBytes());
            byteStream.write(printLineFeed(3));
            outputStream.write(byteStream.toByteArray());
            outputStream.flush();
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void printTop_ESC() throws IOException {
        try {
            resetPrinter();
            byteStream = new ByteArrayOutputStream();
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write(setTextAlign(1));
            byteStream.write("CTY CP CẤP NƯỚC TÂN HÒA\n".getBytes());
            byteStream.write("VĂN PHÒNG GIAO DỊCH\n".getBytes());
            byteStream.write("95 PHẠM HỮU CHÍ, P12, Q5\n".getBytes());
            byteStream.write("Tổng đài: 1900.6489\n".getBytes());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int escpLength(String str) {
        if (str == null)
            return 0;
        return str.length() - (int) str.chars().filter(ch -> ch == 0x1B).count() * 3;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String pad(String left, String right, char pad, int space) {
        if (left == null)
            left = "";
        if (right == null)
            right = "";
        else
            right += "  ";
        int leftLength = escpLength(left),
                rightLength = escpLength(right);
        int padLength = space - leftLength - rightLength;
        if (padLength > 0) {
            char[] padChar = new char[padLength];
            Arrays.fill(padChar, pad);
            return left + (new String(padChar)) + right;
        } else if (padLength < 0) {
            return breakLine(left + right, space);
        } else
            return left + right;
    }

    private String breakLine(String text, int lineLimit) {
        try {
            char ESC = 0x1B;
            if (text == null || text.trim().isEmpty())
                return "";
            StringBuilder sb = new StringBuilder(text);
            for (int i = 0, lineLenth = 0, breakIdx = 0; i < sb.length(); i++) {
                if (sb.charAt(i) == ESC) {
                    i += 2;
                    continue;
                } else if (sb.charAt(i) == ' ')
                    breakIdx = i;
                else if (sb.charAt(i) == '\n')
                    lineLenth = 0;
                if (lineLenth > lineLimit) {
                    lineLenth = i - breakIdx;
                    sb.replace(breakIdx, breakIdx + 1, "\n");
                }
                lineLenth++;
            }
            return sb.toString();
        } catch (Exception ignored) {
            return "";
        }
    }

    private String escpStyle(String text, int style) {
        //00000000: thường
        //00000001: nhỏ
        //00000010: nền đen
        //00000100: đảo ngược
        //00001000: đậm
        //00010000: 2 x cao
        //00100000: 2 x rộng
        //01000000: gạch ngang chữ
        //10000000: nền đen gạch ngang chữ
        char ESC = 0x1B, CMD_STYLE = '!';
        return "" + ESC + CMD_STYLE + (char) style + text + ESC + CMD_STYLE + (char) 0;
    }

    //endregion

    public byte[] printDotFeed_ESC() {
//        return "------------------------------\n".getBytes();
        return "".getBytes();
    }

    public byte[] printLineFeed(int n) {
        return new byte[]{27, 100, (byte) n};
    }

    public byte[] setTextStyle(boolean bold, int extWidth, int extHeight) {
        int n1 = extWidth - 1;
        int n2 = extHeight - 1;
        if (n1 < 0) {
            n1 = 0;
        }
        if (n1 > 7) {
            n1 = 7;
        }
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 > 7) {
            n2 = 7;
        }
        byte extension = (byte) (n1 & 15 | n2 << 4 & 240);
        return new byte[]{27, 69, (byte) (bold ? 1 : 0), 29, 33, extension};
    }

    public byte[] setTextAlign(int align) {
        return new byte[]{27, 97, (byte) align};
    }

    //reset printer
    private void resetPrinter() {
        try {
            outputStream.write(new byte[]{0x1B, 0x40});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
