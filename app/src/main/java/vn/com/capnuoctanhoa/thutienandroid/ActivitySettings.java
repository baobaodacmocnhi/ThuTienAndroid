package vn.com.capnuoctanhoa.thutienandroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentProvider;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;

public class ActivitySettings extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;
    private ArrayList<BluetoothDevice> lstBluetoothDevice;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Thread thread;
    private byte[] readBuffer;
    private int readBufferPosition;
    private volatile boolean stopWorker;
    private ArrayAdapter<String> arrayBluetoothAdapter;
    private StringBuilder stringBuilder;
    private final byte[] stringChuoi = {0x1b};
    private ListView lstView;
    private TextView lblPrinterName;
    private  EditText editText;
    private Button btnTimKiem, btnKetNoi, btnHuyKetNoi, btnIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lstView = (ListView) findViewById(R.id.lstView);
        lblPrinterName = (TextView) findViewById(R.id.lblPrinterName);
        editText = (EditText) findViewById(R.id.editText);
        btnTimKiem = (Button) findViewById(R.id.btnTimKiem);
        btnKetNoi = (Button) findViewById(R.id.btnKetNoi);
        btnHuyKetNoi = (Button) findViewById(R.id.btnHuyKetNoi);
        btnIn = (Button) findViewById(R.id.btnIn);

        findBluetoothDevice();
        for (int i = 0; i < lstBluetoothDevice.size(); i++)
            if (lstBluetoothDevice.get(i).getName().equals(CLocal.ThermalPrinter)) {
                bluetoothDevice = lstBluetoothDevice.get(i);
                if (bluetoothDevice != null)
                    openBluetoothPrinter();
            }

        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                bluetoothDevice = lstBluetoothDevice.get(position);
                lblPrinterName.setText(bluetoothDevice.getName());
                CLocal.ThermalPrinter = bluetoothDevice.getName();
                SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                editor.putString("ThermalPrinter", bluetoothDevice.getName());
                editor.commit();
            }
        });

        btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    findBluetoothDevice();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnKetNoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openBluetoothPrinter();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnHuyKetNoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    disconnectBluetoothDevice();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    printData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findBluetoothDevice() {
        try {
            lstBluetoothDevice = new ArrayList<BluetoothDevice>();
            ArrayList<String> arrayList = new ArrayList<String>();
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                arrayList.add("Chưa có kết nối nào");
                lblPrinterName.setText("Chưa có kết nối nào");
            }
            if (bluetoothAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, 0);
            }

            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

            if (pairedDevice.size() > 0) {
                for (BluetoothDevice pairedDev : pairedDevice) {
                    // My Bluetoth printer name is BTP_F09F1A
//                    if (pairedDev.getName().equals("BTP_F09F1A"))
                    {
                        lstBluetoothDevice.add(pairedDev);
//                        bluetoothDevice = pairedDev;
                        arrayList.add(pairedDev.getName());
                        break;
                    }
                }
            }
            arrayBluetoothAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, arrayList);
            lstView.setAdapter(arrayBluetoothAdapter);
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
                                                lblPrinterName.setText(data);
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

    private void printData() throws IOException {
        try {
            outputStream.write((stringChuoi));
            int y = 5;
            stringBuilder = new StringBuilder();
            stringBuilder.append("EZ\n");
            stringBuilder.append("{PRINT:\n");
            stringBuilder.append(printLine("CTY CP CẤP NƯỚC TÂN HÒA", 3, y, 25, 1, 1));
            stringBuilder.append("}\n");
            outputStream.write(stringBuilder.toString().getBytes());
            outputStream.flush();
            lblPrinterName.setText("Printing Text...");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void disconnectBluetoothDevice() throws IOException {
        try {
            stopWorker = true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            lblPrinterName.setText("Printer Disconnected.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String printLine(String data, int boldNumber, java.lang.Object... args) {
        String base = "@%d,%d:TIMNR,HMULT%d,VMULT%d|" + data + "|\n";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < boldNumber; i++) {
            builder.append(String.format(base, args));
            args[1] = (int) args[1] + 1;
        }
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
                outputStream.write(stringChuoi);

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

    //print custom
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
        try {
            switch (size) {
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
            }

            switch (align) {
                case 0:
                    //left align
                    outputStream.write(CLocal.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(CLocal.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(CLocal.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(CLocal.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print unicode
    public void printUnicode() {
        try {
            outputStream.write(CLocal.ESC_ALIGN_CENTER);
            printText(CLocal.UNICODE_TEXT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print new line
    private void printNewLine() {
        try {
            outputStream.write(CLocal.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String leftRightAlign(String str1, String str2) {
        String ans = str1 + str2;
        if (ans.length() < 31) {
            int n = (31 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }

    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime[] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        return dateTime;
    }
}

