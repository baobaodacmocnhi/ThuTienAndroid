package vn.com.capnuoctanhoa.thutienandroid.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;

public class ThermalPrinter_Store {
    private Activity activity;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;
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
    private CEntityParent entityParent;
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

    public ThermalPrinter_Store(Activity activity) {
        this.activity = activity;
        findBluetoothDevice();
        for (int i = 0; i < lstBluetoothDevice.size(); i++)
            if (lstBluetoothDevice.get(i).getName().equals(CLocal.ThermalPrinter)) {
                bluetoothDevice = lstBluetoothDevice.get(i);
                if (bluetoothDevice != null)
                    openBluetoothPrinter();
            }
    }

    private void findBluetoothDevice() {
        try {
            lstBluetoothDevice = new ArrayList<BluetoothDevice>();
            arrayList = new ArrayList<String>();
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                arrayList.add("Chưa có kết nối nào");
            }
            if (bluetoothAdapter.isEnabled() == false) {
                CLocal.setOnBluetooth(activity);
            }

            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

            if (pairedDevice.size() > 0) {
                for (BluetoothDevice pairedDev : pairedDevice) {
                    // My Bluetoth printer name is BTP_F09F1A
//                    if (pairedDev.getName().equals("BTP_F09F1A"))
                    {
                        lstBluetoothDevice.add(pairedDev);
                        arrayList.add(pairedDev.getName());
//                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void openBluetoothPrinter() {
        try {
            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();
            beginListenData();
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
            ex.printStackTrace();
        }
    }

    //

    public void printThuTien(CEntityParent entityParent) {
        this.entityParent = entityParent;
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("ThuTien");
    }

    public void printPhieuBao(CEntityParent entityParent) {
        this.entityParent = entityParent;
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("PhieuBao");
    }

    public void printPhieuBao2(CEntityParent entityParent) {
        this.entityParent = entityParent;
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("PhieuBao2");
    }

    public void printTBDongNuoc(CEntityParent entityParent) {
        this.entityParent = entityParent;
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("TBDongNuoc");
    }

    public void printDongNuoc(CEntityParent entityParent) {
        this.entityParent = entityParent;
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("DongNuoc");
    }

    public void printDongNuoc2(CEntityParent entityParent) {
        this.entityParent = entityParent;
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("DongNuoc2");
    }

    public void printMoNuoc(CEntityParent entityParent) {
        this.entityParent = entityParent;
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("MoNuoc");
    }

    public void printPhiMoNuoc(CEntityParent entityParent) {
        this.entityParent = entityParent;
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("PhiMoNuoc");
    }

    private void printThuTien_Data() {
        try {
            if (entityParent != null) {
                for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                    printTop();
//                    outputStream.write("BIÊN NHẬN THU TIỀN\n".getBytes());
//                    printNewLine(1);
//                    outputStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "\n").getBytes());
//                    outputStream.write(("Ngày thu: " + entityParent.getLstHoaDon().get(i).getNgayGiaiTrach() + "\n").getBytes());
//                    outputStream.write(("Khách hàng: " + entityParent.getHoTen() + "\n").getBytes());
//                    outputStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
//                    outputStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
//                    outputStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(i).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(i).getDinhMuc() + "\n").getBytes());
//                    outputStream.write(("CSC: " + entityParent.getLstHoaDon().get(i).getCSC() + "  CSM: " + entityParent.getLstHoaDon().get(i).getCSM() + "  Tiêu thụ: " + entityParent.getLstHoaDon().get(i).getTieuThu() + " m3\n").getBytes());
//                    outputStream.write(("Từ: " + entityParent.getLstHoaDon().get(i).getTuNgay() + " Đến: " + entityParent.getLstHoaDon().get(i).getDenNgay() + "\n").getBytes());
//                    printHangNgang();
//                    outputStream.write(("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getGiaBan()), "đ") + "\n").getBytes());
//                    outputStream.write(("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getThueGTGT()), "đ") + "\n").getBytes());
//                    outputStream.write(("Phí BVMT: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getPhiBVMT()), "đ") + "\n").getBytes());
//                    outputStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getTongCong()), "đ") + "\n").getBytes());
//                    outputStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityParent.getLstHoaDon().get(i).getTongCong()) + ".\n").getBytes());
//                    printHangNgang();
//                    outputStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
//                    outputStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
//                    printHangNgang();
//                    outputStream.write("Quý khách in hóa đơn vui lòng vào trang website Công ty: https://www.cskhtanhoa.com.vn\n".getBytes());
//                    outputStream.write("XIN CẢM ƠN QUÝ KHÁCH\n".getBytes());
//                    printNewLine(3);
                    printEZ("BIÊN NHẬN THU TIỀN", 4, toadoY, 40, 2, 1);
                    printEZ("Kỳ: " + entityParent.getLstHoaDon().get(i).getKy(), 1, toadoY, 100, 2, 1);
                    printEZ("Ngày thu: " + entityParent.getLstHoaDon().get(i).getNgayGiaiTrach(), 1, toadoY, 0, 1, 1);
                    printEZ("Khách hàng: " + entityParent.getHoTen(), 1, toadoY, 0, 1, 1);
                    printEZ("Địa chỉ: " + entityParent.getDiaChi(), 1, toadoY, 0, 1, 1);
                    printEZ("Danh bộ: " + entityParent.getDanhBo(), 1, toadoY, 0, 1, 1);
                    printEZ("Giá biểu: " + entityParent.getLstHoaDon().get(i).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(i).getDinhMuc(), 1, toadoY, 0, 1, 1);
                    printEZ("CSC: " + entityParent.getLstHoaDon().get(i).getCSC() + "  CSM: " + entityParent.getLstHoaDon().get(i).getCSM() + "  Tiêu thụ: " + entityParent.getLstHoaDon().get(i).getTieuThu() + "m3", 1, toadoY, 0, 1, 1);
                    printEZ("Từ: " + entityParent.getLstHoaDon().get(i).getTuNgay() + "  Đến: " + entityParent.getLstHoaDon().get(i).getDenNgay(), 1, toadoY, 0, 1, 1);
                    printEZHangNgang();
                    printEZ("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getGiaBan()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getThueGTGT()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Phí BVMT: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getPhiBVMT()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getTongCong()), "đ"), 3, toadoY, 0, 1, 1);
                    printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityParent.getLstHoaDon().get(i).getTongCong()), 1, toadoY, 0, 1, 1);
                    printEZHangNgang();
                    printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
                    printEZ("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
                    printEZHangNgang();
                    printEZ("Quý khách in hóa đơn vui lòng vào trang website Công ty: https://www.cskhtanhoa.com.vn", 1, toadoY, 0, 1, 1);
                    printEZ("XIN CẢM ƠN QUÝ KHÁCH", 1, toadoY, 0, 1, 1);
                    printEZTheEnd();
                    outputStream.flush();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printPhieuBao_Data() {
        try {
            if (entityParent != null) {
                for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                    printTop();
//                    outputStream.write("GIẤY BÁO TIỀN NƯỚC\n".getBytes());
//                    printNewLine(1);
//                    outputStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "\n").getBytes());
//                    outputStream.write(("Khách hàng: " + entityParent.getHoTen() + "\n").getBytes());
//                    outputStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
//                    outputStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
//                    outputStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(i).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(i).getDinhMuc() + "\n").getBytes());
//                    outputStream.write(("CSC: " + entityParent.getLstHoaDon().get(i).getCSC() + "  CSM: " + entityParent.getLstHoaDon().get(i).getCSM() + "  Tiêu thụ: " + entityParent.getLstHoaDon().get(i).getTieuThu() + " m3\n").getBytes());
//                    outputStream.write(("Từ: " + entityParent.getLstHoaDon().get(i).getTuNgay() + " Đến: " + entityParent.getLstHoaDon().get(i).getDenNgay() + "\n").getBytes());
//                    printHangNgang();
//                    outputStream.write(("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getGiaBan()), "đ") + "\n").getBytes());
//                    outputStream.write(("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getThueGTGT()), "đ") + "\n").getBytes());
//                    outputStream.write(("Phí BVMT: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getPhiBVMT()), "đ") + "\n").getBytes());
//                    outputStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getTongCong()), "đ") + "\n").getBytes());
//                    outputStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityParent.getLstHoaDon().get(i).getTongCong()) + ".\n").getBytes());
//                    String[] str = entityParent.getLstHoaDon().get(i).getInPhieuBao_Ngay().split(" ");
//                    outputStream.write(("Quý khách vui lòng thanh toán tiền nước trong 07 ngày kể từ ngày " + str[0] + "\n").getBytes());
//                    outputStream.write("Trân trọng kính chào.\n".getBytes());
//                    printHangNgang();
//                    outputStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
//                    outputStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
//                    outputStream.write(("Ngày gửi: " + str[0] + "\n").getBytes());
//                    printHangNgang();
//                    outputStream.write("Trang website Công ty: https://www.cskhtanhoa.com.vn\n".getBytes());
//                    outputStream.write("XIN CẢM ƠN QUÝ KHÁCH\n".getBytes());
//                    printNewLine(3);
                    printEZ("GIẤY BÁO TIỀN NƯỚC", 4, toadoY, 30, 2, 1);
                    printEZ("Kỳ: " + entityParent.getLstHoaDon().get(i).getKy(), 1, toadoY, 100, 2, 1);
                    printEZ("Khách hàng: " + entityParent.getHoTen(), 1, toadoY, 0, 1, 1);
                    printEZ("Địa chỉ: " + entityParent.getDiaChi(), 1, toadoY, 0, 1, 1);
                    printEZ("Danh bộ: " + entityParent.getDanhBo(), 1, toadoY, 0, 1, 1);
                    printEZ("Giá biểu: " + entityParent.getLstHoaDon().get(i).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(i).getDinhMuc(), 1, toadoY, 0, 1, 1);
                    printEZ("CSC: " + entityParent.getLstHoaDon().get(i).getCSC() + "  CSM: " + entityParent.getLstHoaDon().get(i).getCSM() + "  Tiêu thụ: " + entityParent.getLstHoaDon().get(i).getTieuThu() + "m3", 1, toadoY, 0, 1, 1);
                    printEZ("Từ: " + entityParent.getLstHoaDon().get(i).getTuNgay() + "  Đến: " + entityParent.getLstHoaDon().get(i).getDenNgay(), 1, toadoY, 0, 1, 1);
                    printEZHangNgang();
                    printEZ("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getGiaBan()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getThueGTGT()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Phí BVMT: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getPhiBVMT()), "đ"), 1, toadoY, 0, 1, 1);
                    printEZ("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getTongCong()), "đ"), 3, toadoY, 0, 1, 1);
                    printEZ("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityParent.getLstHoaDon().get(i).getTongCong()), 1, toadoY, 0, 1, 1);
                    String[] str = entityParent.getLstHoaDon().get(i).getInPhieuBao_Ngay().split(" ");
                    printEZ("Quý khách vui lòng thanh toán tiền nước trong 07 ngày kể từ ngày " + str[0], 1, toadoY, 0, 1, 1);
                    printEZ("Trân trọng kính chào.", 1, toadoY, 0, 1, 1);
                    printEZHangNgang();
                    printEZ("Nhân viên: " + CLocal.HoTen, 1, toadoY, 0, 1, 1);
                    printEZ("Điện thoại: " + CLocal.DienThoai, 1, toadoY, 0, 1, 1);
                    printEZ("Ngày gửi: " + entityParent.getLstHoaDon().get(i).getInPhieuBao_Ngay(), 1, toadoY, 0, 1, 1);
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

    private void printPhieuBao2_Data() {
        try {
            if (entityParent != null) {
                printTop();
//                outputStream.write("THÔNG BÁO TIỀN NƯỚC\n".getBytes());
//                outputStream.write("CHƯA THANH TOÁN\n".getBytes());
//                printNewLine(1);
//                outputStream.write(("Kính gửi: " + entityParent.getHoTen() + "\n").getBytes());
//                outputStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
//                outputStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
//                outputStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
//                printHangNgang();
//                outputStream.write(("Hóa đơn:\n").getBytes());
//                int TongCong = 0;
//                for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
//                    outputStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ") + "\n").getBytes());
//                    TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
//                }
//                outputStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
//                outputStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + ".\n").getBytes());
////                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
//                String[] str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_NgayHen().split(" ");
//                outputStream.write(("Để được cung cấp nước liên tục đề nghị Quý khách vui lòng thanh toán trước ngày " + str[0] + "\n").getBytes());
//                outputStream.write(("Nếu quá thời hạn trên khách hàng chưa thanh toán, Công ty sẽ tạm ngưng cung cấp nước theo quy định.\n").getBytes());
//                outputStream.write(("Mọi thắc mắc đề nghị Quý khách hàng liên hệ tại Công ty hoặc Tổng đài trước ngày cung cấp nước\n").getBytes());
//                outputStream.write(("Trân trọng kính chào.\n").getBytes());
//                printHangNgang();
//                outputStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
//                outputStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
//                str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().split(" ");
//                outputStream.write(("Ngày gửi: " + str[0] + "\n").getBytes());
//                printHangNgang();
//                outputStream.write("Trang website Công ty: https://www.cskhtanhoa.com.vn\n".getBytes());
//                outputStream.write("XIN CẢM ƠN QUÝ KHÁCH\n".getBytes());
//                printNewLine(3);
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
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printTBDongNuoc_Data() {
        try {
            if (entityParent != null) {
                printTop();
//                outputStream.write("THÔNG BÁO TẠM\n".getBytes());
//                outputStream.write("NGƯNG CUNG CẤP NƯỚC\n".getBytes());
//                printNewLine(1);
//                outputStream.write(("Kính gửi: " + entityParent.getHoTen() + "\n").getBytes());
//                outputStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
//                outputStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
//                outputStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
//                printHangNgang();
//                String[] str = entityParent.getLstHoaDon().get(0).getTBDongNuoc_NgayHen().split(" ");
//                outputStream.write(("Công ty sẽ tạm ngưng cung cấp nước tại địa chỉ trên vào ngày: " + str[0] + "\n").getBytes());
//                outputStream.write(("Lý do: Quý khách chưa thanh toán hóa đơn tiền nước:\n").getBytes());
//                int TongCong = 0;
//                for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
//                    outputStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ") + "\n").getBytes());
//                    TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
//                }
//                outputStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
//                outputStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + ".\n").getBytes());
//                printHangNgang();
//                outputStream.write(("Công ty tiến hành mở nước khi Quý khách hàng đã thanh toán hết khoản nợ trên và chi phí mở nước là 168.000đ.\n").getBytes());
//                outputStream.write(("Trân trọng kính chào.\n").getBytes());
//                printHangNgang();
//                outputStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
//                outputStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
//                str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().split(" ");
//                outputStream.write(("Ngày gửi: " + str[0] + "\n").getBytes());
//                printHangNgang();
//                outputStream.write("Trang website Công ty: https://www.cskhtanhoa.com.vn\n".getBytes());
//                outputStream.write("XIN CẢM ƠN QUÝ KHÁCH\n".getBytes());
//                printNewLine(3);
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
                printEZ("Công ty tiến hành mở nước khi Quý khách hàng đã thanh toán hết khoản nợ trên và chi phí mở nước là 168.000đ.", 1, toadoY, 0, 1, 1);
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
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printDongNuoc_Data() {
        try {
            if (entityParent != null) {
                printTop();
//                outputStream.write("THÔNG BÁO TẠM\n".getBytes());
//                outputStream.write("NGƯNG CUNG CẤP NƯỚC\n".getBytes());
//                printNewLine(1);
//                outputStream.write(("Kính gửi: " + entityParent.getHoTen() + "\n").getBytes());
//                outputStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
//                outputStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
//                outputStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
//                printHangNgang();
//                String[] str = entityParent.getLstHoaDon().get(0).getTBDongNuoc_NgayHen().split(" ");
//                outputStream.write(("Công ty sẽ tạm ngưng cung cấp nước tại địa chỉ trên vào ngày: " + str[0] + "\n").getBytes());
//                outputStream.write(("Lý do: Quý khách chưa thanh toán hóa đơn tiền nước:\n").getBytes());
//                int TongCong = 0;
//                for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
//                    outputStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ") + "\n").getBytes());
//                    TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
//                }
//                outputStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
//                outputStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + ".\n").getBytes());
//                printHangNgang();
//                outputStream.write(("Công ty tiến hành mở nước khi Quý khách hàng đã thanh toán hết khoản nợ trên và chi phí mở nước là 168.000đ.\n").getBytes());
//                outputStream.write(("Trân trọng kính chào.\n").getBytes());
//                printHangNgang();
//                outputStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
//                outputStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
//                str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().split(" ");
//                outputStream.write(("Ngày gửi: " + str[0] + "\n").getBytes());
//                printHangNgang();
//                outputStream.write("Trang website Công ty: https://www.cskhtanhoa.com.vn\n".getBytes());
//                outputStream.write("XIN CẢM ƠN QUÝ KHÁCH\n".getBytes());
//                printNewLine(3);
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
                printEZ("Chi phí mở nước là: 168.000đ/lần", 1, toadoY, 0, 1, 1);
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
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printDongNuoc2_Data() {
        try {
            if (entityParent != null) {
                printTop();
//                outputStream.write("THÔNG BÁO TẠM\n".getBytes());
//                outputStream.write("NGƯNG CUNG CẤP NƯỚC\n".getBytes());
//                printNewLine(1);
//                outputStream.write(("Kính gửi: " + entityParent.getHoTen() + "\n").getBytes());
//                outputStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
//                outputStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
//                outputStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
//                printHangNgang();
//                String[] str = entityParent.getLstHoaDon().get(0).getTBDongNuoc_NgayHen().split(" ");
//                outputStream.write(("Công ty sẽ tạm ngưng cung cấp nước tại địa chỉ trên vào ngày: " + str[0] + "\n").getBytes());
//                outputStream.write(("Lý do: Quý khách chưa thanh toán hóa đơn tiền nước:\n").getBytes());
//                int TongCong = 0;
//                for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
//                    outputStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ") + "\n").getBytes());
//                    TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
//                }
//                outputStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
//                outputStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + ".\n").getBytes());
//                printHangNgang();
//                outputStream.write(("Công ty tiến hành mở nước khi Quý khách hàng đã thanh toán hết khoản nợ trên và chi phí mở nước là 168.000đ.\n").getBytes());
//                outputStream.write(("Trân trọng kính chào.\n").getBytes());
//                printHangNgang();
//                outputStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
//                outputStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
//                str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().split(" ");
//                outputStream.write(("Ngày gửi: " + str[0] + "\n").getBytes());
//                printHangNgang();
//                outputStream.write("Trang website Công ty: https://www.cskhtanhoa.com.vn\n".getBytes());
//                outputStream.write("XIN CẢM ƠN QUÝ KHÁCH\n".getBytes());
//                printNewLine(3);
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
                printEZ("Công ty tiến hành mở nước khi Quý khách hàng đã thanh toán hết khoản nợ trên và chi phí mở nước là 168.000đ.", 1, toadoY, 0, 1, 1);
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
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printMoNuoc_Data() {
        try {
            if (entityParent != null) {
                printTop();
//                outputStream.write("THÔNG BÁO TẠM\n".getBytes());
//                outputStream.write("NGƯNG CUNG CẤP NƯỚC\n".getBytes());
//                printNewLine(1);
//                outputStream.write(("Kính gửi: " + entityParent.getHoTen() + "\n").getBytes());
//                outputStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
//                outputStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
//                outputStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
//                printHangNgang();
//                String[] str = entityParent.getLstHoaDon().get(0).getTBDongNuoc_NgayHen().split(" ");
//                outputStream.write(("Công ty sẽ tạm ngưng cung cấp nước tại địa chỉ trên vào ngày: " + str[0] + "\n").getBytes());
//                outputStream.write(("Lý do: Quý khách chưa thanh toán hóa đơn tiền nước:\n").getBytes());
//                int TongCong = 0;
//                for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
//                    outputStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ") + "\n").getBytes());
//                    TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
//                }
//                outputStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
//                outputStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + ".\n").getBytes());
//                printHangNgang();
//                outputStream.write(("Công ty tiến hành mở nước khi Quý khách hàng đã thanh toán hết khoản nợ trên và chi phí mở nước là 168.000đ.\n").getBytes());
//                outputStream.write(("Trân trọng kính chào.\n").getBytes());
//                printHangNgang();
//                outputStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
//                outputStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
//                str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().split(" ");
//                outputStream.write(("Ngày gửi: " + str[0] + "\n").getBytes());
//                printHangNgang();
//                outputStream.write("Trang website Công ty: https://www.cskhtanhoa.com.vn\n".getBytes());
//                outputStream.write("XIN CẢM ƠN QUÝ KHÁCH\n".getBytes());
//                printNewLine(3);
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
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printPhiMoNuoc_Data() {
        try {
            if (entityParent != null) {
                printTop();
//                outputStream.write("THÔNG BÁO TẠM\n".getBytes());
//                outputStream.write("NGƯNG CUNG CẤP NƯỚC\n".getBytes());
//                printNewLine(1);
//                outputStream.write(("Kính gửi: " + entityParent.getHoTen() + "\n").getBytes());
//                outputStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
//                outputStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
//                outputStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
//                printHangNgang();
//                String[] str = entityParent.getLstHoaDon().get(0).getTBDongNuoc_NgayHen().split(" ");
//                outputStream.write(("Công ty sẽ tạm ngưng cung cấp nước tại địa chỉ trên vào ngày: " + str[0] + "\n").getBytes());
//                outputStream.write(("Lý do: Quý khách chưa thanh toán hóa đơn tiền nước:\n").getBytes());
//                int TongCong = 0;
//                for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
//                    outputStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ") + "\n").getBytes());
//                    TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
//                }
//                outputStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
//                outputStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + ".\n").getBytes());
//                printHangNgang();
//                outputStream.write(("Công ty tiến hành mở nước khi Quý khách hàng đã thanh toán hết khoản nợ trên và chi phí mở nước là 168.000đ.\n").getBytes());
//                outputStream.write(("Trân trọng kính chào.\n").getBytes());
//                printHangNgang();
//                outputStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
//                outputStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
//                str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().split(" ");
//                outputStream.write(("Ngày gửi: " + str[0] + "\n").getBytes());
//                printHangNgang();
//                outputStream.write("Trang website Công ty: https://www.cskhtanhoa.com.vn\n".getBytes());
//                outputStream.write("XIN CẢM ƠN QUÝ KHÁCH\n".getBytes());
//                printNewLine(3);
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
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printTop() {
        try {
            this.toadoY = 0;
            resetPrinter();
            setLineSpacing();
//            outputStream.write(ESC);
//            stringBuilder = new StringBuilder();
//            stringBuilder.append("EZ\n");
//            stringBuilder.append("{PRINT:\n");
//            stringBuilder.append(printLine("CTY CP CẤP NƯỚC TÂN HÒA", 3, toadoY, 25, 1, 1));
//            stringBuilder.append(printLine("95 PHẠM HỮU CHÍ, P12, Q5", 1, toadoY, 40, 1, 1));
//            stringBuilder.append(printLine("Tổng đài: 1900.6489", 2, toadoY, 80, 1, 1));
//            stringBuilder.append("}");
            printEZ("CTY CP CẤP NƯỚC TÂN HÒA", 3, toadoY, 25, 1, 1);
            printEZ("95 PHẠM HỮU CHÍ, P12, Q5", 1, toadoY, 40, 1, 1);
            printEZ("Tổng đài: 1900.6489", 2, toadoY, 80, 1, 1);

//            resetPrinter();
//            setLineSpacing();
//            setTimeNewRoman();
//            outputStream.write("CTY CP CẤP NƯỚC TÂN HÒA\n".getBytes());
//            outputStream.write("95 PHẠM HỮU CHÍ, P12, Q5\n".getBytes());
//            outputStream.write("Tổng đài: 1900.6489\n".getBytes());
//            printNewLine(1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    private void setSizeNormal() {
        try {
            outputStream.write(new byte[]{0x1B, 0x21, 0x05});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setBold(Boolean bold) {
        try {
            if (bold == true)
                outputStream.write(new byte[]{0x1B, 0x45});
            else
                outputStream.write(new byte[]{0x1B, 0x46});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSizeDoubleHigh() {
        try {
            outputStream.write(new byte[]{0x1B, 0x21, 0x10});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSizeDoubleWidth() {
        try {
            outputStream.write(new byte[]{0x1B, 0x21, 0x20});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSizeDoubleHighWidth() {
        try {
            outputStream.write(new byte[]{0x1B, 0x21, 0x30});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printHangNgang() {
        try {
            outputStream.write(".....................\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printEZHangNgang() {
        printEZ("=================================================", 1, toadoY, 0, 1, 1);
    }

    private void printEZTheEnd() {
        printEZ("  ", 1, toadoY + 60, 0, 1, 1);
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

    //print custom
    private void printText(String content, int size, int align) {
        //Print config "mode"

//        [0x1b,0x21,0x00] //default
//        [0x1b,0x21,0x01] //small font
//        [0x1b,0x21,0x08] //bold
//        [0x1b,0x21,0x10] //doubleHeight
//        [0x1b,0x21,0x20] //doubleWidth
//        [0x1b,0x21,0x30] //doubleHeightAndWidth

        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x05};  // 1- bold
        byte[] bb1 = new byte[]{0x1B, 0x21, 0x10}; // 2- bold Double-high
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 3- bold Double-wide
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x30}; // 4- bold Double-high and double-wide
        try {
            switch (size) {
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb1);
                    break;
                case 3:
                    outputStream.write(bb2);
                    break;
                case 4:
                    outputStream.write(bb3);
                    break;
            }

            switch (align) {
                case 0:
                    //left align
                    outputStream.write(new byte[]{0x1B, 'a', 0});
                    break;
                case 1:
                    //center align
                    outputStream.write(new byte[]{0x1B, 'a', 1});
                    break;
                case 2:
                    //right align
                    outputStream.write(new byte[]{0x1B, 'a', 2});
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

    public class MyAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            switch (strings[0]) {
                case "ThuTien":
                    printThuTien_Data();
                    break;
                case "PhieuBao":
                    printPhieuBao_Data();
                    break;
                case "PhieuBao2":
                    printPhieuBao2_Data();
                    break;
                case "TBDongNuoc":
                    printTBDongNuoc_Data();
                    break;
                case "DongNuoc":
                    printDongNuoc_Data();
                    break;
                case "DongNuoc2":
                    printDongNuoc2_Data();
                    break;
                case "MoNuoc":
                    printMoNuoc_Data();
                    break;
                case "PhiMoNuoc":
                    printPhiMoNuoc_Data();
                    break;
            }
            return null;
        }
    }


}
