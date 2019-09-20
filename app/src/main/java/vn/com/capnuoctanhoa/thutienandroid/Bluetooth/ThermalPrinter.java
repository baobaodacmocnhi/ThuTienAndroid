package vn.com.capnuoctanhoa.thutienandroid.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;

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
                        break;
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

    public void printPhieuBao(CEntityParent entityParent) {
        this.entityParent = entityParent;
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("PhieuBao");
    }

    public void printHoaDon(CEntityParent entityParent) {
        this.entityParent = entityParent;
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("HoaDon");
    }

    public void printDongNuoc(CEntityParent entityParent) {
        this.entityParent = entityParent;
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("DongNuoc");
    }

    private void printPhieuBao_Data() {
        try {
            if (entityParent != null) {
                printTop();

                printText("THÔNG BÁO TIỀN NƯỚC", 4, 0, 45, 1, 1);
                printText("CHƯA THANH TOÁN", 4, 0, 65, 1, 1);

                initialPrinter();
                printNewLine(1);
                outputStream.write(ESC);
                outputStream.write((" Kính gửi KH: " + entityParent.getHoTen() + "\n").getBytes());
                outputStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
                outputStream.write(("Danh bộ:").getBytes());

                printText(entityParent.getDanhBo(), 3, 0, 0, 2, 2);

                initialPrinter();
                printNewLine(1);
                outputStream.write(ESC);
                outputStream.write(("Hóa đơn:\n").getBytes());
                int TongCong=0;
                for (int i = 0; i < entityParent.getLstHoaDon().size(); i++) {
                    outputStream.write(("Kỳ : " + entityParent.getLstHoaDon().get(i).getKy() + "   " + CLocal.formatMoney(entityParent.getLstHoaDon().get(i).getTongCong(), "đ")+"\n").getBytes());
                    TongCong+=Integer.parseInt(entityParent.getLstHoaDon().get(i).getTongCong());
                }
                outputStream.write(("Tổng số tiền: " + CLocal.formatMoney(String.valueOf(TongCong), "đ") + " chưa thanh toán\n").getBytes());
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                outputStream.write(("Quý khách hàng vui lòng thanh toán trong 7 ngày\n").getBytes());
                outputStream.write(("Kể từ ngày: " + currentDate.format(new Date()).toString() + "\n").getBytes());
                outputStream.write(("Quá thời hạn trên, Công ty sẽ tạm ngưng dịch vụ cung cấp nước theo quy định. Mọi thắc mắc đề nghị quý khách hàng liên hệ tại Công ty hoặc Tổng đài trước ngày cung cấp nước.\n").getBytes());
                outputStream.write(("Trân trọng kính chào.\n").getBytes());
                outputStream.write(("Nhân viên: " + CLocal.HoTen + "\n").getBytes());
                outputStream.write(("Ngày gửi: " + currentDate.format(new Date()).toString() + "\n").getBytes());

                printNewLine(3);
                outputStream.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printHoaDon_Data() {
        try {
            if (entityParent != null) {
                printTop();

//                printText("BIÊN NHẬN THU TIỀN", 4, 0, 55, 2, 1);

//                outputStream.write(ESC);
//                printNewLine(1);
//                outputStream.write(("Kính gửi KH: " + entityParent.getHoTen() + "\n").getBytes());
//                outputStream.write(("Địa chỉ: " + entityParent.getDiaChi() + "\n").getBytes());
//                outputStream.write(("Danh bộ:\n").getBytes());
//                printText(entityParent.getDanhBo(), 3, 0, 0, 3, 1);

                printNewLine(3);
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

//                stringBuilder.append(printLine("THÔNG BÁO TẠM", 4, toadoY, 80, 2, 1));
//                stringBuilder.append(printLine("NGƯNG CUNG CẤP NƯỚC", 4, toadoY + 20, 40, 2, 1));
//
//                //in dòng cuối
//                stringBuilder.append(printLine(" ", 1, toadoY + 100, 10, 1, 1));
//                stringBuilder.append("}\n");
//                outputStream.write(stringBuilder.toString().getBytes());
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
            printText("CTY CP CẤP NƯỚC TÂN HÒA", 3, 0, 25, 1, 1);
            printText("95 PHẠM HỮU CHÍ, P12, Q5", 1, 0, 40, 1, 1);
            printText("Tổng đài: 1900.6489", 2, 0, 80, 1, 1);
            initialPrinter();
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


    private String printLine(String content, int boldNumber, int toadoY, int toadoX, int heightFont, int widthFont) {
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
                case "PhieuBao":
                    printPhieuBao_Data();
                    break;
                case "HoaDon":
                    printHoaDon_Data();
                    break;
                case "DongNuoc":
                    printDongNuoc_Data();
                    break;
            }
            return null;
        }
    }
}
