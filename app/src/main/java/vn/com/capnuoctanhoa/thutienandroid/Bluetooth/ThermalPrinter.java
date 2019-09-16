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
    private final byte[] ESC = {0x1b};
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

                stringBuilder.append(printLine("PHIẾU BÁO TIỀN NƯỚC", 4, toadoY, 60, 2, 1));
                //in dòng cuối
                stringBuilder.append(printLine(" ", 1, toadoY + 100, 10, 1, 1));
                stringBuilder.append("}\n");
                outputStream.write(stringBuilder.toString().getBytes());
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

                stringBuilder.append(printLine("BIÊN NHẬN THU TIỀN", 4, toadoY, 60, 2, 1));
                //in dòng cuối
                stringBuilder.append(printLine(" ", 1, toadoY + 100, 10, 1, 1));
                stringBuilder.append("}\n");
                outputStream.write(stringBuilder.toString().getBytes());
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

                stringBuilder.append(printLine("THÔNG BÁO TẠM", 4, toadoY, 80, 2, 1));
                stringBuilder.append(printLine("NGƯNG CUNG CẤP NƯỚC", 4, toadoY + 20, 40, 2, 1));

                //in dòng cuối
                stringBuilder.append(printLine(" ", 1, toadoY + 100, 10, 1, 1));
                stringBuilder.append("}\n");
                outputStream.write(stringBuilder.toString().getBytes());
                outputStream.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printTop() {
        try {
            this.toadoY = 20;
            outputStream.write((ESC));

            stringBuilder = new StringBuilder();
            stringBuilder.append("EZ\n");
            stringBuilder.append("{PRINT:\n");
            stringBuilder.append(printLine("CTY CP CẤP NƯỚC TÂN HÒA", 3, toadoY, 25, 1, 1));
            stringBuilder.append(printLine("95 PHẠM HỮU CHÍ, P12, Q5", 1, toadoY, 40, 1, 1));
            stringBuilder.append(printLine("Tổng đài: 1900.6489", 2, toadoY, 80, 1, 1));
            toadoY += 20;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String printLine(String data, int boldNumber, int toadoY, int toadoX, int widthFont, int heightFont) {
        String base = "@" + toadoY + "," + toadoX + ":TIMNR,HMULT" + widthFont + ",VMULT" + heightFont + "|" + data + "|\n";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < boldNumber; i++) {
            builder.append("@" + toadoY + "," + toadoX++ + ":TIMNR,HMULT" + widthFont + ",VMULT" + heightFont + "|" + data + "|\n");
        }
        this.toadoY = toadoY + 30;
        return builder.toString();
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
