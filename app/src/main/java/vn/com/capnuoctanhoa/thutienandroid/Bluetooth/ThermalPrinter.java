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
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;

public class ThermalPrinter {
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

    private void printThuTien_Data() {
        try {
            if (entityParent != null) {
                for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                    printTop();
                    outputStream.write("BIÊN NHẬN THU TIỀN\n".getBytes());
                    printNewLine(1);
                    outputStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "\n").getBytes());
                    outputStream.write(("Ngày thu: " + entityParent.getLstHoaDon().get(i).getNgayGiaiTrach() + "\n").getBytes());
                    outputStream.write(("Khách hàng: " + entityParent.getHoTen() + "\n").getBytes());
                    outputStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
                    outputStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
                    outputStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(i).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(i).getDinhMuc() + "\n").getBytes());
                    outputStream.write(("CSC: " + entityParent.getLstHoaDon().get(i).getCSC() + "  CSM: " + entityParent.getLstHoaDon().get(i).getCSM() + "  Tiêu thụ: " + entityParent.getLstHoaDon().get(i).getTieuThu() + " m3\n").getBytes());
                    outputStream.write(("Từ: " + entityParent.getLstHoaDon().get(i).getTuNgay() + " Đến: " + entityParent.getLstHoaDon().get(i).getDenNgay() + "\n").getBytes());
                    printHangNgang();
                    outputStream.write(("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getGiaBan()), "đ") + "\n").getBytes());
                    outputStream.write(("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getThueGTGT()), "đ") + "\n").getBytes());
                    outputStream.write(("Phí BVMT: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getPhiBVMT()), "đ") + "\n").getBytes());
                    outputStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getTongCong()), "đ") + "\n").getBytes());
                    outputStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityParent.getLstHoaDon().get(i).getTongCong()) + ".\n").getBytes());
                    printHangNgang();
                    outputStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
                    outputStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
                    printHangNgang();
                    outputStream.write("Quý khách in hóa đơn vui lòng vào trang website Công ty: https://www.cskhtanhoa.com.vn\n".getBytes());
                    outputStream.write("XIN CẢM ƠN QUÝ KHÁCH\n".getBytes());
                    printNewLine(3);
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
                    outputStream.write("GIẤY BÁO TIỀN NƯỚC\n".getBytes());
                    printNewLine(1);
                    outputStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "\n").getBytes());
                    outputStream.write(("Khách hàng: " + entityParent.getHoTen() + "\n").getBytes());
                    outputStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
                    outputStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
                    outputStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(i).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(i).getDinhMuc() + "\n").getBytes());
                    outputStream.write(("CSC: " + entityParent.getLstHoaDon().get(i).getCSC() + "  CSM: " + entityParent.getLstHoaDon().get(i).getCSM() + "  Tiêu thụ: " + entityParent.getLstHoaDon().get(i).getTieuThu() + " m3\n").getBytes());
                    outputStream.write(("Từ: " + entityParent.getLstHoaDon().get(i).getTuNgay() + " Đến: " + entityParent.getLstHoaDon().get(i).getDenNgay() + "\n").getBytes());
                    printHangNgang();
                    outputStream.write(("Tiền nước: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getGiaBan()), "đ") + "\n").getBytes());
                    outputStream.write(("Thuế GTGT: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getThueGTGT()), "đ") + "\n").getBytes());
                    outputStream.write(("Phí BVMT: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getPhiBVMT()), "đ") + "\n").getBytes());
                    outputStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(entityParent.getLstHoaDon().get(i).getTongCong()), "đ") + "\n").getBytes());
                    outputStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(entityParent.getLstHoaDon().get(i).getTongCong()) + ".\n").getBytes());
                    String[] str = entityParent.getLstHoaDon().get(i).getInPhieuBao_Ngay().split(" ");
                    outputStream.write(("Quý khách vui lòng thanh toán tiền nước trong 07 ngày kể từ ngày " + str[0] + "\n").getBytes());
                    outputStream.write("Trân trọng kính chào.\n".getBytes());
                    printHangNgang();
                    outputStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
                    outputStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
                    outputStream.write(("Ngày gửi: " + str[0] + "\n").getBytes());
                    printHangNgang();
                    outputStream.write("Trang website Công ty: https://www.cskhtanhoa.com.vn\n".getBytes());
                    outputStream.write("XIN CẢM ƠN QUÝ KHÁCH\n".getBytes());
                    printNewLine(3);
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
                outputStream.write("THÔNG BÁO TIỀN NƯỚC\n".getBytes());
                outputStream.write("CHƯA THANH TOÁN\n".getBytes());
                printNewLine(1);
                outputStream.write(("Kính gửi: " + entityParent.getHoTen() + "\n").getBytes());
                outputStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
                outputStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
                outputStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
                printHangNgang();
                outputStream.write(("Hóa đơn:\n").getBytes());
                int TongCong = 0;
                for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                    outputStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ") + "\n").getBytes());
                    TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
                }
                outputStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
                outputStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + ".\n").getBytes());
//                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                String[] str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_NgayHen().split(" ");
                outputStream.write(("Để được cung cấp nước liên tục đề nghị Quý khách vui lòng thanh toán trước ngày " + str[0] + "\n").getBytes());
                outputStream.write(("Nếu quá thời hạn trên khách hàng chưa thanh toán, Công ty sẽ tạm ngưng cung cấp nước theo quy định.\n").getBytes());
                outputStream.write(("Mọi thắc mắc đề nghị Quý khách hàng liên hệ tại Công ty hoặc Tổng đài trước ngày cung cấp nước\n").getBytes());
                outputStream.write(("Trân trọng kính chào.\n").getBytes());
                printHangNgang();
                outputStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
                outputStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
                 str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().split(" ");
                outputStream.write(("Ngày gửi: " + str[0] + "\n").getBytes());
                printHangNgang();
                outputStream.write("Trang website Công ty: https://www.cskhtanhoa.com.vn\n".getBytes());
                outputStream.write("XIN CẢM ƠN QUÝ KHÁCH\n".getBytes());
                printNewLine(3);
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
                outputStream.write("THÔNG BÁO TẠM\n".getBytes());
                outputStream.write("NGƯNG CUNG CẤP NƯỚC\n".getBytes());
                printNewLine(1);
                outputStream.write(("Kính gửi: " + entityParent.getHoTen() + "\n").getBytes());
                outputStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
                outputStream.write(("Danh bộ: " + entityParent.getDanhBo() + "\n").getBytes());
                outputStream.write(("Giá biểu: " + entityParent.getLstHoaDon().get(0).getGiaBieu() + "   Định mức: " + entityParent.getLstHoaDon().get(0).getDinhMuc() + "\n").getBytes());
                printHangNgang();
                String[] str = entityParent.getLstHoaDon().get(0).getTBDongNuoc_NgayHen().split(" ");
                outputStream.write(("Công ty sẽ tạm ngưng cung cấp nước tại địa chỉ trên vào ngày: " + str[0] + "\n").getBytes());
                outputStream.write(("Lý do: Quý khách chưa thanh toán hóa đơn tiền nước:\n").getBytes());
                int TongCong = 0;
                for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                    outputStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ") + "\n").getBytes());
                    TongCong += Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
                }
                outputStream.write(("Tổng cộng: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + "\n").getBytes());
                outputStream.write(("Bằng chữ: " + CLocal.ConvertMoneyToWord(String.valueOf(TongCong)) + ".\n").getBytes());
                printHangNgang();
                outputStream.write(("Công ty tiến hành mở nước khi Quý khách hàng đã thanh toán hết khoản nợ trên và chi phí mở nước là 168.000đ.\n").getBytes());
                outputStream.write(("Trân trọng kính chào.\n").getBytes());
                printHangNgang();
                outputStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
                outputStream.write(("Điện thoại: " + CLocal.DienThoai + "\n").getBytes());
                 str = entityParent.getLstHoaDon().get(0).getInPhieuBao2_Ngay().split(" ");
                outputStream.write(("Ngày gửi: " + str[0] + "\n").getBytes());
                printHangNgang();
                outputStream.write("Trang website Công ty: https://www.cskhtanhoa.com.vn\n".getBytes());
                outputStream.write("XIN CẢM ƠN QUÝ KHÁCH\n".getBytes());
                printNewLine(3);
                outputStream.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printTop() {
        try {
//            this.toadoY = 20;
//            outputStream.write(ESC);
//
//            stringBuilder = new StringBuilder();
//            stringBuilder.append("EZ\n");
//            stringBuilder.append("{PRINT:\n");
//            stringBuilder.append(printLine("CTY CP CẤP NƯỚC TÂN HÒA", 3, toadoY, 25, 1, 1));
//            stringBuilder.append(printLine("95 PHẠM HỮU CHÍ, P12, Q5", 1, toadoY, 40, 1, 1));
//            stringBuilder.append(printLine("Tổng đài: 1900.6489", 2, toadoY, 80, 1, 1));
//            toadoY += 20;

            resetPrinter();
            setLineSpacing();
            setTimeNewRoman();
            outputStream.write("CTY CP CẤP NƯỚC TÂN HÒA\n".getBytes());
            outputStream.write("95 PHẠM HỮU CHÍ, P12, Q5\n".getBytes());
            outputStream.write("Tổng đài: 1900.6489\n".getBytes());
            printNewLine(1);

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

    private String printEZ(String content, int boldNumber, int toadoY, int toadoX, int heightFont, int widthFont) {
//        String base = "@" + toadoY + "," + toadoX + ":TIMNR,HMULT" + widthFont + ",VMULT" + heightFont + "|" + content + "|\n";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < boldNumber; i++) {
            builder.append("@" + toadoY + "," + toadoX++ + ":TIMNR,HMULT" + widthFont + ",VMULT" + heightFont + "|" + content + "|\n");
        }
        this.toadoY = toadoY + 30;
        return builder.toString();
    }

    //print custom
    private void printText(String content, int boldNumber, int toadoY, int toadoX, int heightFont, int widthFont) {
        try {
            outputStream.write(ESC);
            stringBuilder = new StringBuilder();
            stringBuilder.append("EZ{PRINT:");
            for (int i = 0; i < boldNumber; i++) {
                stringBuilder.append("@" + toadoY + "," + toadoX++ + ":TIMNR,HMULT" + widthFont + ",VMULT" + heightFont + "|" + content + "|");
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
            }
            return null;
        }
    }


}
