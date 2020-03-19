package vn.com.capnuoctanhoa.thutienandroid.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityChild;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;

public class ThermalPrinter {
    private Activity activity;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice = null;
    private ArrayList<BluetoothDevice> lstBluetoothDevice;
    private ArrayList<String> arrayList;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Thread thread;
    private byte[] readBuffer;
    private int readBufferPosition;
    private volatile boolean stopWorker;
    private final byte[] ESC = {0x1B};
    private StringBuilder stringBuilder;
    //    private CEntityParent entityParent;
    private int toadoX = 10;
    private int toadoY = 20;
    private int widthFont = 1;
    private int heightFont = 1;
    private int lengthPaper = 33;

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public ArrayList<BluetoothDevice> getLstBluetoothDevice() {
        return lstBluetoothDevice;
    }

    public void setLstBluetoothDevice(ArrayList<BluetoothDevice> lstBluetoothDevice) {
        this.lstBluetoothDevice = lstBluetoothDevice;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    public ThermalPrinter(Activity activity) {
        this.activity = activity;
        findBluetoothDevice();
        for (int i = 0; i < lstBluetoothDevice.size(); i++)
            if (lstBluetoothDevice.get(i).getAddress().equals(CLocal.ThermalPrinter)) {
                bluetoothDevice = lstBluetoothDevice.get(i);
                if (bluetoothDevice != null)
                    openBluetoothPrinter();
            }
//        bluetoothDevice = bluetoothAdapter.getRemoteDevice(CLocal.ThermalPrinter);
//        if (bluetoothDevice != null)
//            openBluetoothPrinter();
    }

    private void findBluetoothDevice() {
        try {
            lstBluetoothDevice = new ArrayList<BluetoothDevice>();
            arrayList = new ArrayList<String>();
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                arrayList.add("Chưa có kết nối nào");
            } else if (bluetoothAdapter.isEnabled() == false) {
                CLocal.setOnBluetooth(activity);
            } else {
                Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
                if (pairedDevice.size() > 0) {
                    for (BluetoothDevice pairedDev : pairedDevice) {
                        // My Bluetoth printer name is BTP_F09F1A
//                    if (pairedDev.getName().equals("BTP_F09F1A"))
                        {
                            lstBluetoothDevice.add(pairedDev);
                            arrayList.add(pairedDev.getName() + "\n" + pairedDev.getAddress());
//                        break;
                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openBluetoothPrinter() {
        try {
            if (bluetoothSocket == null) {
                //Standard uuid from string //
                UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                inputStream = bluetoothSocket.getInputStream();
//                beginListenData();
            } else if (bluetoothSocket.isConnected() == false)
                bluetoothSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void beginListenData() {
        try {
            final Handler handler = new Handler();
            final byte delimiter = 10;
            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                        try {
                            int byteAvailable = inputStream.available();
                            if (byteAvailable > 0) {
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for (int i = 0; i < byteAvailable; i++) {
                                    byte b = packetByte[i];
                                    if (b == delimiter) {
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedByte, 0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte, "US-ASCII");
                                        readBufferPosition = 0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
//                                                lblPrinterName.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            stopWorker = true;
                        }
                    }
                }
            });
            thread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void disconnectBluetoothDevice() {
        try {
            stopWorker = true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
        } catch (Exception ex) {
            Log.e("disconnectBluetooth", ex.getMessage());
        }
    }

    //region method printer

    public void printThuTien(CEntityParent entityParent) {
        try {
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printThuTien_EZ(entityParent);
                    break;
                case "ESC":
                    printThuTien_ESC(entityParent);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printThuTien(CEntityParent entityParent, CEntityChild entityChild) {
        try {
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printThuTien_EZ(entityParent, entityChild);
                    break;
                case "ESC":
                    printThuTien_ESC(entityParent, entityChild);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printPhieuBao(CEntityParent entityParent) {
        try {
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printPhieuBao_EZ(entityParent);
                    break;
                case "ESC":
                    printPhieuBao_ESC(entityParent);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printPhieuBao(CEntityParent entityParent, CEntityChild entityChild) {
        try {
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printPhieuBao_EZ(entityParent, entityChild);
                    break;
                case "ESC":
                    printPhieuBao_ESC(entityParent, entityChild);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printPhieuBao2(CEntityParent entityParent) {
        try {
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printPhieuBao2_EZ(entityParent);
                    break;
                case "ESC":
                    printPhieuBao2_ESC(entityParent);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printTBDongNuoc(CEntityParent entityParent) {
        try {
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printTBDongNuoc_EZ(entityParent);
                    break;
                case "ESC":
                    printTBDongNuoc_ESC(entityParent);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printDongNuoc(CEntityParent entityParent) {
        try {
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printDongNuoc_EZ(entityParent);
                    break;
                case "ESC":
                    printDongNuoc_ESC(entityParent);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printDongNuoc2(CEntityParent entityParent) {
        try {
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printDongNuoc2_EZ(entityParent);
                    break;
                case "ESC":
                    printDongNuoc2_ESC(entityParent);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printMoNuoc(CEntityParent entityParent) {
        try {
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printMoNuoc_EZ(entityParent);
                    break;
                case "ESC":
                    printMoNuoc_ESC(entityParent);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printPhiMoNuoc(CEntityParent entityParent) {
        try {
            switch (CLocal.MethodPrinter) {
                case "EZ":
                    printPhiMoNuoc_EZ(entityParent);
                    break;
                case "ESC":
                    printPhiMoNuoc_ESC(entityParent);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //endregion

    //region EZ method

    public void printThuTien_EZ(CEntityParent entityParent) {
        try {
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                if (entityParent.getLstHoaDon().get(i).isDangNgan_DienThoai() == true) {
                    printThuTien_EZ(entityParent, entityParent.getLstHoaDon().get(i));
                }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printThuTien_EZ(CEntityParent entityParent, CEntityChild entityChild) {
        try {
            printTop_EZ();
            printEZ("BIÊN NHẬN THU TIỀN", 4, toadoY, 40, 2, 1);
            printEZ("Kỳ: " + entityChild.getKy(), 1, toadoY, 100, 2, 1);
            printEZ("Ngày thu: " + entityChild.getNgayGiaiTrach(), 1, toadoY, 0, 1, 1);
            printEZ("Khách hàng: " + entityParent.getHoTen(), 1, toadoY, 0, 1, 1);
            printEZ("Địa chỉ: " + entityParent.getDiaChi(), 1, toadoY, 0, 1, 1);
            printEZ("Danh bộ: " + entityParent.getDanhBo(), 1, toadoY, 0, 1, 1);
            printEZ("Giá biểu: " + entityChild.getGiaBieu() + "   Định mức: " + entityChild.getDinhMuc(), 1, toadoY, 0, 1, 1);
            printEZ("CSC: " + entityChild.getCSC() + "  CSM: " + entityChild.getCSM() + "  Tiêu thụ: " + entityChild.getTieuThu() + "m3", 1, toadoY, 0, 1, 1);
            printEZ("Từ: " + entityChild.getTuNgay() + "  Đến: " + entityChild.getDenNgay(), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityChild.getGiaBan()), "đ"), 1, toadoY, 0, 1, 1);
            printEZ("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityChild.getThueGTGT()), "đ"), 1, toadoY, 0, 1, 1);
            printEZ("Phí BVMT: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT()), "đ"), 1, toadoY, 0, 1, 1);
            printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityChild.getTongCong()), "đ"), 3, toadoY, 0, 1, 1);
            printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityChild.getTongCong()), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
            printEZ("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Quý khách in hóa đơn vui lòng vào trang website Công ty: https://www.cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
            printEZ("XIN CẢM ƠN QUÝ KHÁCH", 1, toadoY, 0, 1, 1);
            printEZTheEnd();
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printPhieuBao_EZ(CEntityParent entityParent) {
        try {
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                if (entityParent.getLstHoaDon().get(i).getInPhieuBao_Ngay().equals("") == false) {
                    printPhieuBao_EZ(entityParent, entityParent.getLstHoaDon().get(i));
                }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printPhieuBao_EZ(CEntityParent entityParent, CEntityChild entityChild) {
        try {
            if (entityParent != null && entityChild != null) {
                if (entityChild.getInPhieuBao_Ngay().equals("") == false) {
                    printTop_EZ();
                    printEZ("GIẤY BÁO TIỀN NƯỚC", 4, toadoY, 30, 2, 1);
                    printEZ("Kỳ: " + entityChild.getKy(), 1, toadoY, 100, 2, 1);
                    printEZ("Khách hàng: " + entityParent.getHoTen(), 1, toadoY, 0, 1, 1);
                    printEZ("Địa chỉ: " + entityParent.getDiaChi(), 1, toadoY, 0, 1, 1);
                    printEZ("Danh bộ: " + entityParent.getDanhBo(), 1, toadoY, 0, 1, 1);
                    printEZ("Giá biểu: " + entityChild.getGiaBieu() + "   Định mức: " + entityChild.getDinhMuc(), 1, toadoY, 0, 1, 1);
                    printEZ("CSC: " + entityChild.getCSC() + "  CSM: " + entityChild.getCSM() + "  Tiêu thụ: " + entityChild.getTieuThu() + "m3", 1, toadoY, 0, 1, 1);
                    printEZ("Từ: " + entityChild.getTuNgay() + "  Đến: " + entityChild.getDenNgay(), 1, toadoY, 0, 1, 1);
                    printEZHangNgang();
                    printEZ("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityChild.getGiaBan()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityChild.getThueGTGT()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Phí BVMT: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityChild.getTongCong()), "đ"), 3, toadoY, 0, 1, 1);
                    printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityChild.getTongCong()), 1, toadoY, 0, 1, 1);
                    String[] str = entityChild.getInPhieuBao_Ngay().split(" ");
                    printEZ("Quý khách vui lòng thanh toán tiền nước trong 07 ngày kể từ ngày " + str[0], 1, toadoY, 0, 1, 1);
                    printEZ("Trân trọng kính chào.", 1, toadoY, 0, 1, 1);
                    printEZHangNgang();
                    printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
                    printEZ("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
                    printEZ("Ngày gửi: " + entityChild.getInPhieuBao_Ngay(), 1, toadoY, 0, 1, 1);
                    printEZHangNgang();
                    printEZ("Website Công ty: https://www.cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
                    printEZ("XIN CẢM ƠN QUÝ KHÁCH", 1, toadoY, 0, 1, 1);
                    printEZTheEnd();
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printPhieuBao2_EZ(CEntityParent entityParent) {
        try {
            printTop_EZ();
            printEZ("THÔNG BÁO TIỀN NƯỚC", 4, toadoY, 30, 2, 1);
            printEZ("CHƯA THANH TOÁN", 4, toadoY, 50, 2, 1);
            printEZ("Kính gửi: " + entityParent.getHoTen(), 1, toadoY, 0, 1, 1);
            printEZ("Địa chỉ: " + entityParent.getDiaChi(), 1, toadoY, 0, 1, 1);
            printEZ("Danh bộ: " + entityParent.getDanhBo(), 1, toadoY, 0, 1, 1);
            printEZ("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc(), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Hóa đơn:", 1, toadoY, 0, 1, 1);
            int TongCong = 0;
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                printEZ("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ"), 1, toadoY, 0, 1, 1);
                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
            }
            printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ"), 2, toadoY, 0, 1, 1);
            printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)), 1, toadoY, 0, 1, 1);
            String[] str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_NgayHen().split(" ");
            printEZ("Để được cung cấp nước liên tục đề nghị Quý khách vui lòng thanh toán trước ngày " + str[0], 1, toadoY, 0, 1, 1);
            printEZ("Nếu quá thời hạn trên khách hàng chưa thanh toán, Công ty sẽ tạm ngưng cung cấp nước theo quy định.", 1, toadoY, 0, 1, 1);
            printEZ("Mọi thắc mắc đề nghị Quý khách hàng liên hệ tại Công ty hoặc Tổng đài trước ngày cung cấp nước", 1, toadoY, 0, 1, 1);
            printEZ("Trân trọng kính chào.", 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
            printEZ("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
//                str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().split(" ");
            printEZ("Ngày gửi: " + entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay(), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Website Công ty: https://www.cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
            printEZ("XIN CẢM ƠN QUÝ KHÁCH", 1, toadoY, 0, 1, 1);
            printEZTheEnd();
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printTBDongNuoc_EZ(CEntityParent entityParent) {
        try {
            printTop_EZ();
            printEZ("THÔNG BÁO TẠM", 4, toadoY, 80, 2, 1);
            printEZ("NGƯNG CUNG CẤP NƯỚC", 4, toadoY, 30, 2, 1);
            printEZ(" ", 1, toadoY, 0, 1, 1);
            printEZ("Kính gửi: " + entityParent.getHoTen(), 1, toadoY, 0, 1, 1);
            printEZ("Địa chỉ: " + entityParent.getDiaChi(), 1, toadoY, 0, 1, 1);
            printEZ("Danh bộ: " + entityParent.getDanhBo(), 1, toadoY, 0, 1, 1);
            printEZ("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc(), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            String[] str = entityParent.getLstHoaDon().get(0).getTBDongNuoc_NgayHen().split(" ");
            printEZ("Công ty sẽ tạm ngưng cung cấp nước tại địa chỉ trên vào ngày: " + str[0], 1, toadoY, 0, 1, 1);
            printEZ("Lý do: Quý khách chưa thanh toán hóa đơn tiền nước:", 1, toadoY, 0, 1, 1);
            int TongCong = 0;
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                printEZ("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ"), 1, toadoY, 0, 1, 1);
                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
            }
            printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ"), 1, toadoY, 0, 1, 1);
            printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Công ty tiến hành mở nước khi Quý khách hàng đã thanh toán hết khoản nợ trên và chi phí mở nước là " + CLocal.getPhiMoNuoc(entityParent.getCo()) + "đ.", 1, toadoY, 0, 1, 1);
            printEZ("Trân trọng kính chào.", 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
            printEZ("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
//                str = entityParent.getLstHoaDon().get(0).getTBDongNuoc_Ngay().split(" ");
            printEZ("Ngày gửi: " + entityParent.getLstHoaDon().get(0).getTBDongNuoc_Ngay(), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Website Công ty: https://www.cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
            printEZ("XIN CẢM ƠN QUÝ KHÁCH", 1, toadoY, 0, 1, 1);
            printEZTheEnd();
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printDongNuoc_EZ(CEntityParent entityParent) {
        try {
            printTop_EZ();
            printEZ("BIÊN BẢN TẠM", 4, toadoY, 70, 2, 1);
            printEZ("NGƯNG CUNG CẤP NƯỚC", 4, toadoY, 30, 2, 1);
            printEZ(" ", 1, toadoY, 0, 1, 1);
            printEZ("Hôm nay: ngày " + entityParent.getNgayDN(), 1, toadoY, 0, 1, 1);
            printEZ("Tiến hành tạm ngưng cung cấp nước tại địa chỉ: " + entityParent.getDiaChi(), 1, toadoY, 0, 1, 1);
            printEZ("Khách hàng: " + entityParent.getHoTen(), 1, toadoY, 0, 1, 1);
            printEZ("Danh bộ: " + entityParent.getDanhBo(), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            int TongCong = 0;
            String Ky = "";
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                if (Ky.equals("") == true)
                    Ky += entityParent.getLstHoaDon().get(i).getKy();
                else
                    Ky += ", " + entityParent.getLstHoaDon().get(i).getKy();
                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
            }
            printEZ("Lý do: nợ tiền nước kỳ " + Ky, 1, toadoY, 0, 1, 1);
            printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ"), 1, toadoY, 0, 1, 1);
            printEZ("Chi phí mở nước là: " + CLocal.getPhiMoNuoc(entityParent.getCo()) + "đ/lần", 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Hiệu: " + entityParent.getHieu() + " Cỡ: " + entityParent.getCo(), 1, toadoY, 0, 1, 1);
            printEZ("Số thân: " + entityParent.getSoThan(), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
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
            printEZHangNgang();
            printEZ("Công ty chỉ mở nước khi khách hàng thanh toán hết nợ và chi phí mở nước.", 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
            printEZ("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Website Công ty: https://www.cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
            printEZ("XIN CẢM ƠN QUÝ KHÁCH", 1, toadoY, 0, 1, 1);
            printEZTheEnd();
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printDongNuoc2_EZ(CEntityParent entityParent) {
        try {
            printTop_EZ();
            printEZ("BIÊN BẢN", 4, toadoY, 80, 2, 1);
            printEZ("NGƯNG CUNG CẤP NƯỚC", 4, toadoY, 30, 2, 1);
            printEZ(" ", 1, toadoY, 0, 1, 1);
            printEZ("Kính gửi: " + entityParent.getHoTen(), 1, toadoY, 0, 1, 1);
            printEZ("Địa chỉ: " + entityParent.getDiaChi(), 1, toadoY, 0, 1, 1);
            printEZ("Danh bộ: " + entityParent.getDanhBo(), 1, toadoY, 0, 1, 1);
            printEZ("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc(), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            String[] str = entityParent.getLstHoaDon().get(0).getTBDongNuoc_NgayHen().split(" ");
            printEZ("Công ty sẽ tạm ngưng cung cấp nước tại địa chỉ trên vào ngày: " + str[0], 1, toadoY, 0, 1, 1);
            printEZ("Lý do: Quý khách chưa thanh toán hóa đơn tiền nước:" + entityParent.getHoTen(), 1, toadoY, 0, 1, 1);
            int TongCong = 0;
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                printEZ("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ"), 1, toadoY, 0, 1, 1);
                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
            }
            printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + entityParent.getDanhBo(), 1, toadoY, 0, 1, 1);
            printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + entityParent.getDanhBo(), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Công ty tiến hành mở nước khi Quý khách hàng đã thanh toán hết khoản nợ trên và chi phí mở nước là " + CLocal.getPhiMoNuoc(entityParent.getCo()) + "đ.", 1, toadoY, 0, 1, 1);
            printEZ("Trân trọng kính chào.", 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
            printEZ("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
            str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().split(" ");
            printEZ("Ngày gửi: " + str[0], 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Quý khách in hóa đơn vui lòng vào trang website Công ty: https://www.cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
            printEZ("XIN CẢM ƠN QUÝ KHÁCH", 1, toadoY, 0, 1, 1);
            printEZTheEnd();
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printMoNuoc_EZ(CEntityParent entityParent) {
        try {
            printTop_EZ();
            printEZ("BIÊN BẢN MỞ NƯỚC", 4, toadoY, 30, 2, 1);
            printEZ(" ", 1, toadoY, 0, 1, 1);
            printEZ("Hôm nay: ngày " + entityParent.getNgayMN(), 1, toadoY, 0, 1, 1);
            printEZ("Tiến hành mở nước tại địa chỉ: " + entityParent.getDiaChi(), 1, toadoY, 0, 1, 1);
            printEZ("Khách hàng: " + entityParent.getHoTen(), 1, toadoY, 0, 1, 1);
            printEZ("Danh bộ: " + entityParent.getDanhBo(), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Lý do:.........", 1, toadoY, 0, 1, 1);
            printEZ("Tổng cộng:.........", 1, toadoY, 0, 1, 1);
            printEZ("Chi phí mở nước:.........", 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Hiệu: " + entityParent.getHieu() + " Cỡ: " + entityParent.getCo(), 1, toadoY, 0, 1, 1);
            printEZ("Số thân: " + entityParent.getSoThan(), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
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
            printEZHangNgang();
            printEZ("Công ty chỉ mở nước khi khách hàng thanh toán hết nợ và chi phí mở nước.", 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
            printEZ("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Website Công ty: https://www.cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
            printEZ("XIN CẢM ƠN QUÝ KHÁCH", 1, toadoY, 0, 1, 1);
            printEZTheEnd();
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printPhiMoNuoc_EZ(CEntityParent entityParent) {
        try {
            printTop_EZ();
            printEZ("BIÊN NHẬN", 4, toadoY, 80, 2, 1);
            printEZ("THU TIỀN PHÍ MỞ NƯỚC", 4, toadoY, 20, 2, 1);
            printEZ(" ", 1, toadoY, 0, 1, 1);
            printEZ("Ngày thu: " + entityParent.getLstHoaDon().get(0).getNgayGiaiTrach(), 1, toadoY, 0, 1, 1);
            printEZ("Khách hàng: " + entityParent.getHoTen(), 1, toadoY, 0, 1, 1);
            printEZ("Địa chỉ: " + entityParent.getDiaChi(), 1, toadoY, 0, 1, 1);
            printEZ("Danh bộ: " + entityParent.getDanhBo(), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            if (entityParent.isDongNuoc2() == true) {
                printEZ("Ngày đóng nước lần 1: " + entityParent.getNgayDN1(), 1, toadoY, 0, 1, 1);
                printEZ("Ngày đóng nước lần 2: " + entityParent.getNgayDN(), 1, toadoY, 0, 1, 1);
            } else {
                printEZ("Ngày đóng nước lần 1: " + entityParent.getNgayDN(), 1, toadoY, 0, 1, 1);
            }
            printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(0).getPhiMoNuoc()), "đ"), 1, toadoY, 0, 1, 1);
            printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityParent.getLstHoaDon().get(0).getPhiMoNuoc()), 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
            printEZ("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
            printEZHangNgang();
            printEZ("Website Công ty: https://www.cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
            printEZ("XIN CẢM ƠN QUÝ KHÁCH", 1, toadoY, 0, 1, 1);
            printEZTheEnd();
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printTop_EZ() {
        try {
            this.toadoY = 0;
            resetPrinter();
            setLineSpacing();
            printEZ("CTY CP CẤP NƯỚC TÂN HÒA", 3, toadoY, 25, 1, 1);
            printEZ("95 PHẠM HỮU CHÍ, P12, Q5", 1, toadoY, 40, 1, 1);
            printEZ("Tổng đài: 1900.6489", 2, toadoY, 80, 1, 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printEZHangNgang() {
        printEZ("=================================================", 1, toadoY, 0, 1, 1);
    }

    private void printEZTheEnd() {
        printEZ("  ", 1, toadoY + 60, 0, 1, 1);
    }

    private String printEZAppend(String content, int boldNumber, int toadoY, int toadoX, int heightFont, int widthFont) {
//        String base = "@" + toadoY + "," + toadoX + ":TIMNR,HMULT" + widthFont + ",VMULT" + heightFont + "|" + content + "|\n";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < boldNumber; i++) {
            builder.append("@" + toadoY + "," + toadoX++ + ":TIMNR,HMULT" + heightFont + ",VMULT" + widthFont + "|" + content + "|\n");
        }
        this.toadoY = toadoY + 30;
        return builder.toString();
    }

    //print custom
    private void printEZ(String content, int boldNumber, int toadoY, int toadoX, int heightFont, int widthFont) {
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
            ex.printStackTrace();
        }
    }

    //endregion

    //region ESC/P Command
    ByteArrayOutputStream byteStream;

    public void printThuTien_ESC(CEntityParent entityParent) {
        try {
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                if (entityParent.getLstHoaDon().get(i).isDangNgan_DienThoai() == true) {
                    printThuTien_ESC(entityParent, entityParent.getLstHoaDon().get(i));
                }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printThuTien_ESC(CEntityParent entityParent, CEntityChild entityChild) {
        try {
            printTop_ESC();
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(true, 1, 2));
            byteStream.write("BIÊN NHẬN THU TIỀN\n".getBytes());
            byteStream.write(("Kỳ: " + entityChild.getKy() + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(setTextAlign(0));
            byteStream.write(("Ngày thu: " + entityChild.getNgayGiaiTrach() + "\n").getBytes());
            byteStream.write(("Khách hàng: " + entityParent.getHoTen() + "\n").getBytes());
            byteStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
            byteStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
            byteStream.write(("Giá biểu: " + entityChild.getGiaBieu() + "   Định mức: " + entityChild.getDinhMuc() + "\n").getBytes());
            byteStream.write(("CSC: " + entityChild.getCSC() + "  CSM: " + entityChild.getCSM() + "  Tiêu thụ: " + entityChild.getTieuThu() + "m3" + "\n").getBytes());
            byteStream.write(("Từ: " + entityChild.getTuNgay() + "  Đến: " + entityChild.getDenNgay() + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityChild.getGiaBan()), "đ") + "\n").getBytes());
            byteStream.write(("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityChild.getThueGTGT()), "đ") + "\n").getBytes());
            byteStream.write(("Phí BVMT: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT()), "đ") + "\n").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityChild.getTongCong()), "đ") + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityChild.getTongCong()) + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
            byteStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write("Quý khách in hóa đơn vui lòng vào trang website Công ty: https://www.cskhtanhoa.com.vn\n".getBytes());
            byteStream.write("XIN CẢM ƠN QUÝ KHÁCH\n".getBytes());
            byteStream.write(printLineFeed(3));
            outputStream.write(byteStream.toByteArray());
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printPhieuBao_ESC(CEntityParent entityParent) {
        try {
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++)
                if (entityParent.getLstHoaDon().get(i).getInPhieuBao_Ngay().equals("") == false) {
                    printPhieuBao_ESC(entityParent, entityParent.getLstHoaDon().get(i));
                }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printPhieuBao_ESC(CEntityParent entityParent, CEntityChild entityChild) {
        try {
            printTop_ESC();
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(true, 1, 2));
            byteStream.write("GIẤY BÁO TIỀN NƯỚC\n".getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(setTextAlign(0));
            byteStream.write(("Kỳ: " + entityChild.getKy() + "\n").getBytes());
            byteStream.write(("Khách hàng: " + entityParent.getHoTen() + "\n").getBytes());
            byteStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
            byteStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
            byteStream.write(("Giá biểu: " + entityChild.getGiaBieu() + "   Định mức: " + entityChild.getDinhMuc() + "\n").getBytes());
            byteStream.write(("CSC: " + entityChild.getCSC() + "  CSM: " + entityChild.getCSM() + "  Tiêu thụ: " + entityChild.getTieuThu() + "m3\n").getBytes());
            byteStream.write(("Từ: " + entityChild.getTuNgay() + "  Đến: " + entityChild.getDenNgay() + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityChild.getGiaBan()), "đ") + "\n").getBytes());
            byteStream.write(("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityChild.getThueGTGT()), "đ") + "\n").getBytes());
            byteStream.write(("Phí BVMT: " + CLocal.formatMoney(String.valueOf(entityChild.getPhiBVMT()), "đ") + "\n").getBytes());
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityChild.getTongCong()), "đ") + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityChild.getTongCong()) + "\n").getBytes());
            String[] str = entityChild.getInPhieuBao_Ngay().split(" ");
            byteStream.write(("Quý khách vui lòng thanh toán tiền nước trong 07 ngày kể từ ngày " + str[0] + "\n").getBytes());
            byteStream.write("Trân trọng kính chào.\n".getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
            byteStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
            byteStream.write(("Ngày gửi: " + entityChild.getInPhieuBao_Ngay() + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write("Website Công ty: https://www.cskhtanhoa.com.vn\n".getBytes());
            byteStream.write("XIN CẢM ƠN QUÝ KHÁCH\n".getBytes());
            byteStream.write(printLineFeed(3));
            outputStream.write(byteStream.toByteArray());
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printPhieuBao2_ESC(CEntityParent entityParent) {
        try {
            printTop_ESC();
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(true, 1, 2));
            byteStream.write(("THÔNG BÁO TIỀN NƯỚC\n").getBytes());
            byteStream.write(("CHƯA THANH TOÁN\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(setTextAlign(0));
            byteStream.write(("Kính gửi: " + entityParent.getHoTen() + "\n").getBytes());
            byteStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
            byteStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
            byteStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Hóa đơn:\n").getBytes());
            int TongCong = 0;
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                byteStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ") + "\n").getBytes());
                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
            }
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + "\n").getBytes());
            String[] str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_NgayHen().split(" ");
            byteStream.write(("Để được cung cấp nước liên tục đề nghị Quý khách vui lòng thanh toán trước ngày " + str[0] + "\n").getBytes());
            byteStream.write(("Nếu quá thời hạn trên khách hàng chưa thanh toán, Công ty sẽ tạm ngưng cung cấp nước theo quy định.\n").getBytes());
            byteStream.write(("Mọi thắc mắc đề nghị Quý khách hàng liên hệ tại Công ty hoặc Tổng đài trước ngày cung cấp nước\n").getBytes());
            byteStream.write(("Trân trọng kính chào.\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
            byteStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
//                str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().split(" ");
            byteStream.write(("Ngày gửi: " + entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay() + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Website Công ty: https://www.cskhtanhoa.com.vn\n").getBytes());
            byteStream.write(("XIN CẢM ƠN QUÝ KHÁCH\n").getBytes());
            byteStream.write(printLineFeed(3));
            outputStream.write(byteStream.toByteArray());
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printTBDongNuoc_ESC(CEntityParent entityParent) {
        try {
            printTop_ESC();
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(true, 1, 2));
            byteStream.write(("THÔNG BÁO TẠM\n").getBytes());
            byteStream.write(("NGƯNG CUNG CẤP NƯỚC\n").getBytes());
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(setTextAlign(0));
            byteStream.write(("Kính gửi: " + entityParent.getHoTen() + "\n").getBytes());
            byteStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
            byteStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
            byteStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
            byteStream.write(printDotFeed());
            String[] str = entityParent.getLstHoaDon().get(0).getTBDongNuoc_NgayHen().split(" ");
            byteStream.write(("Công ty sẽ tạm ngưng cung cấp nước tại địa chỉ trên vào ngày: " + str[0] + "\n").getBytes());
            byteStream.write(("Lý do: Quý khách chưa thanh toán hóa đơn tiền nước:\n").getBytes());
            int TongCong = 0;
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                byteStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ") + "\n").getBytes());
                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
            }
            byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
            byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Công ty tiến hành mở nước khi Quý khách hàng đã thanh toán hết khoản nợ trên và chi phí mở nước là " + CLocal.getPhiMoNuoc(entityParent.getCo()) + "đ.\n").getBytes());
            byteStream.write(("Trân trọng kính chào.\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
            byteStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
//                str = entityParent.getLstHoaDon().get(0).getTBDongNuoc_Ngay().split(" ");
            byteStream.write(("Ngày gửi: " + entityParent.getLstHoaDon().get(0).getTBDongNuoc_Ngay() + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Website Công ty: https://www.cskhtanhoa.com.vn\n").getBytes());
            byteStream.write(("XIN CẢM ƠN QUÝ KHÁCH\n").getBytes());
            byteStream.write(printLineFeed(3));
            outputStream.write(byteStream.toByteArray());
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printDongNuoc_ESC(CEntityParent entityParent) {
        try {
            printTop_ESC();
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(true, 1, 2));
            byteStream.write(("BIÊN BẢN TẠM\n").getBytes());
            byteStream.write(("NGƯNG CUNG CẤP NƯỚC\n").getBytes());
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(setTextAlign(0));
            byteStream.write(("Hôm nay: ngày " + entityParent.getNgayDN() + "\n").getBytes());
            byteStream.write(("Tiến hành tạm ngưng cung cấp nước tại địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
            byteStream.write(("Khách hàng: " + entityParent.getHoTen() + "\n").getBytes());
            byteStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
            byteStream.write(printDotFeed());
            int TongCong = 0;
            String Ky = "";
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                if (Ky.equals("") == true)
                    Ky += entityParent.getLstHoaDon().get(i).getKy();
                else
                    Ky += ", " + entityParent.getLstHoaDon().get(i).getKy();
                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
            }
            byteStream.write(("Lý do: nợ tiền nước kỳ " + Ky + "\n").getBytes());
            byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
            byteStream.write(("Chi phí mở nước là: " + CLocal.getPhiMoNuoc(entityParent.getCo()) + "đ/lần\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Hiệu: " + entityParent.getHieu() + " Cỡ: " + entityParent.getCo() + "\n").getBytes());
            byteStream.write(("Số thân: " + entityParent.getSoThan() + "\n").getBytes());
            byteStream.write(printDotFeed());
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
            byteStream.write(("Vị trí: " + entityParent.getViTri()).getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Công ty chỉ mở nước khi khách hàng thanh toán hết nợ và chi phí mở nước.\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
            byteStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Website Công ty: https://www.cskhtanhoa.com.vn\n").getBytes());
            byteStream.write(("XIN CẢM ƠN QUÝ KHÁCH\n").getBytes());
            byteStream.write(printLineFeed(3));
            outputStream.write(byteStream.toByteArray());
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printDongNuoc2_ESC(CEntityParent entityParent) {
        try {
            printTop_ESC();
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(true, 1, 2));
            byteStream.write(("BIÊN BẢN\n").getBytes());
            byteStream.write(("NGƯNG CUNG CẤP NƯỚC\n").getBytes());
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(setTextAlign(0));
            byteStream.write(("Kính gửi: " + entityParent.getHoTen() + "\n").getBytes());
            byteStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
            byteStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
            byteStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
            byteStream.write(printDotFeed());
            String[] str = entityParent.getLstHoaDon().get(0).getTBDongNuoc_NgayHen().split(" ");
            byteStream.write(("Công ty sẽ tạm ngưng cung cấp nước tại địa chỉ trên vào ngày: " + str[0] + "\n").getBytes());
            byteStream.write(("Lý do: Quý khách chưa thanh toán hóa đơn tiền nước:" + entityParent.getHoTen() + "\n").getBytes());
            int TongCong = 0;
            for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                byteStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ") + "\n").getBytes());
                TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
            }
            byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + entityParent.getDanhBo() + "\n").getBytes());
            byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + entityParent.getDanhBo() + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Công ty tiến hành mở nước khi Quý khách hàng đã thanh toán hết khoản nợ trên và chi phí mở nước là " + CLocal.getPhiMoNuoc(entityParent.getCo()) + "đ.\n").getBytes());
            byteStream.write(("Trân trọng kính chào.\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
            byteStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
            str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().split(" ");
            byteStream.write(("Ngày gửi: " + str[0] + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Quý khách in hóa đơn vui lòng vào trang website Công ty: https://www.cskhtanhoa.com.vn\n").getBytes());
            byteStream.write(("XIN CẢM ƠN QUÝ KHÁCH\n").getBytes());
            byteStream.write(printLineFeed(3));
            outputStream.write(byteStream.toByteArray());
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printMoNuoc_ESC(CEntityParent entityParent) {
        try {
            printTop_ESC();
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(true, 1, 2));
            byteStream.write(("BIÊN BẢN MỞ NƯỚC\n").getBytes());
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(setTextAlign(0));
            byteStream.write(("Hôm nay: ngày " + entityParent.getNgayMN() + "\n").getBytes());
            byteStream.write(("Tiến hành mở nước tại địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
            byteStream.write(("Khách hàng: " + entityParent.getHoTen() + "\n").getBytes());
            byteStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Lý do:.........\n").getBytes());
            byteStream.write(("Tổng cộng:.........\n").getBytes());
            byteStream.write(("Chi phí mở nước:.........\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Hiệu: " + entityParent.getHieu() + " Cỡ: " + entityParent.getCo() + "\n").getBytes());
            byteStream.write(("Số thân: " + entityParent.getSoThan() + "\n").getBytes());
            byteStream.write(printDotFeed());
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
            byteStream.write(printDotFeed());
            byteStream.write(("Công ty chỉ mở nước khi khách hàng thanh toán hết nợ và chi phí mở nước.\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
            byteStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Website Công ty: https://www.cskhtanhoa.com.vn\n").getBytes());
            byteStream.write(("XIN CẢM ƠN QUÝ KHÁCH\n").getBytes());
            byteStream.write(printLineFeed(3));
            outputStream.write(byteStream.toByteArray());
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printPhiMoNuoc_ESC(CEntityParent entityParent) {
        try {
            printTop_ESC();
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(true, 1, 2));
            byteStream.write(("BIÊN NHẬN\n").getBytes());
            byteStream.write(("THU TIỀN PHÍ MỞ NƯỚC\n").getBytes());
            byteStream.write(printLineFeed(1));
            byteStream.write(setTextStyle(false, 1, 1));
            byteStream.write(setTextAlign(0));
            byteStream.write(("Ngày thu: " + entityParent.getLstHoaDon().get(0).getNgayGiaiTrach() + "\n").getBytes());
            byteStream.write(("Khách hàng: " + entityParent.getHoTen() + "\n").getBytes());
            byteStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
            byteStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
            byteStream.write(printDotFeed());
            if (entityParent.isDongNuoc2() == true) {
                byteStream.write(("Ngày đóng nước lần 1: " + entityParent.getNgayDN1() + "\n").getBytes());
                byteStream.write(("Ngày đóng nước lần 2: " + entityParent.getNgayDN() + "\n").getBytes());
            } else {
                byteStream.write(("Ngày đóng nước lần 1: " + entityParent.getNgayDN() + "\n").getBytes());
            }
            byteStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(0).getPhiMoNuoc()), "đ") + "\n").getBytes());
            byteStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityParent.getLstHoaDon().get(0).getPhiMoNuoc()) + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
            byteStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
            byteStream.write(printDotFeed());
            byteStream.write(("Website Công ty: https://www.cskhtanhoa.com.vn\n").getBytes());
            byteStream.write(("XIN CẢM ƠN QUÝ KHÁCH\n").getBytes());
            byteStream.write(printLineFeed(3));
            outputStream.write(byteStream.toByteArray());
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printTop_ESC() {
        try {
            resetPrinter();
            byteStream = new ByteArrayOutputStream();
            byteStream.write(setTextStyle(true, 1, 1));
            byteStream.write(setTextAlign(1));
            byteStream.write("CTY CP CẤP NƯỚC TÂN HÒA\n".getBytes());
            byteStream.write("95 PHẠM HỮU CHÍ, P12, Q5\n".getBytes());
            byteStream.write("Tổng đài: 1900.6489\n".getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //endregion

    public static byte[] initPrinter() {
        return new byte[]{27, 64};
    }

    public static byte[] printNewLine() {
        return new byte[]{10};
    }

    public static byte[] printDotFeed() {
        return ".............................\n".getBytes();
    }

    public static byte[] printDotFeed(int n) {
        return new byte[]{27, 74, (byte) n};
    }

    public static byte[] printLineFeed(int n) {
        return new byte[]{27, 100, (byte) n};
    }

    public static byte[] setTextStyle(boolean bold, int extWidth, int extHeight) {
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

    public static byte[] setTextAlign(int align) {
        return new byte[]{27, 97, (byte) align};
    }

    //initial printer
    private void initialPrinter() {
        resetPrinter();
        setLineSpacing();
        setTimeNewRoman();
    }

    //reset printer
    private void resetPrinter() {
        try {
            outputStream.write(new byte[]{0x1B, 0x40});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //set font Times New Roman
    private void setTimeNewRoman() {
        try {
            outputStream.write(new byte[]{0x1B, 0x77, 0x35});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //set line spacing
    private void setLineSpacing() {
        try {
            //set line spacing using minimun units
            outputStream.write(new byte[]{0x1B, '2'});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printHangNgang() {
        try {
            outputStream.write(".............................\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print new line
    private void printNewLine(int numberLine) {
        try {
            for (int i = 0; i < numberLine; i++) {
                outputStream.write(new byte[]{0x0A});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printText(String content, int align) {
        try {
            switch (align) {
                case 0:
                    //left align
                    outputStream.write(new byte[]{0x1B, 'a', 0x00});
                    break;
                case 1:
                    //center align
                    outputStream.write(new byte[]{0x1B, 'a', 0x01});
                    break;
                case 2:
                    //right align
                    outputStream.write(new byte[]{0x1B, 'a', 0x02});
                    break;
            }
            outputStream.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s).replace(" ", "  ");
    }

    private int handlingYMoreThan450(int y, int delta) {
        if (y + delta > 450) {
            stringBuilder.append("}\n");
            try {
                outputStream.write(stringBuilder.toString().getBytes());
                outputStream.write(ESC);

                stringBuilder = new StringBuilder();
                stringBuilder.append("EZ\n");
                stringBuilder.append("{PRINT:\n");
            } catch (IOException e) {

            } finally {
                return 0;
            }
        } else
            return y + delta;
    }

    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime[] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        return dateTime;
    }


}
