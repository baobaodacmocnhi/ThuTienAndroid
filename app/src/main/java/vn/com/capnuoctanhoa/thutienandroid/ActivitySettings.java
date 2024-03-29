package vn.com.capnuoctanhoa.thutienandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import vn.com.capnuoctanhoa.thutienandroid.Bluetooth.ThermalPrinter;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocation;

public class ActivitySettings extends AppCompatActivity {
    private EditText edtMayInDaChon, edtIDMobile, edtSoTien;
    private Button btnGetThermal, btnGetIDMobile, btnSave;
    private ArrayAdapter<String> arrayBluetoothAdapter;
    private ListView lstView;
    private ThermalPrinter thermalPrinter;
    private RadioButton radTrucTiep, radGianTiep, radIntermec, radHoneywell3l, radHoneywell45, radER58;
    private RadioGroup radGroupSync, radGroupMethodPrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtMayInDaChon = (EditText) findViewById(R.id.edtMayInDaChon);
        edtIDMobile = (EditText) findViewById(R.id.edtIDMobile);
        edtSoTien = (EditText) findViewById(R.id.edtSoTien);
        btnGetThermal = (Button) findViewById(R.id.btnGetThermal);
        btnGetIDMobile = (Button) findViewById(R.id.btnGetIDMobile);
        btnSave = (Button) findViewById(R.id.btnSave);
        lstView = (ListView) findViewById(R.id.lstView);
        radTrucTiep = (RadioButton) findViewById(R.id.radTrucTiep);
        radGianTiep = (RadioButton) findViewById(R.id.radGianTiep);
        radGroupSync = (RadioGroup) findViewById(R.id.radGroupSync);
        radIntermec = (RadioButton) findViewById(R.id.radIntermec);
        radHoneywell3l = (RadioButton) findViewById(R.id.radHoneywell31);
        radHoneywell45 = (RadioButton) findViewById(R.id.radHoneywell45);
        radER58 = (RadioButton) findViewById(R.id.radER58);
        radGroupMethodPrinter = (RadioGroup) findViewById(R.id.radGroupMethodPrinter);
        edtMayInDaChon.setText(CLocal.ThermalPrinter);

        edtIDMobile.setText(CLocal.getAndroidID(ActivitySettings.this));
        edtSoTien.setText(String.valueOf(CLocal.SoTien));
        btnGetThermal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTask_Thermal myAsyncTask_thermal = new MyAsyncTask_Thermal();
                myAsyncTask_thermal.execute();


//                String str="Bằng chữ: một trăm năm mươi hai ng";
//                CLocal.showPopupMessage(ActivitySettings.this,String.valueOf(str.length()));

            }
        });

        btnGetIDMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                edtIDMobile.setText(CLocal.getAndroidID(ActivitySettings.this));
                setClipboard(getApplicationContext(), edtIDMobile.getText().toString());
                CLocal.showToastMessage(getApplicationContext(), "Copied");
//                CLocation cLocation = new CLocation(ActivitySettings.this);
//                CLocal.showPopupMessage(ActivitySettings.this, cLocation.getCurrentLocation(), "center");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                editor.putInt("SoTien", Integer.parseInt(edtSoTien.getText().toString()));
                editor.commit();
            }
        });

        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    thermalPrinter.setBluetoothDevice(thermalPrinter.getLstBluetoothDevice().get(position));
                    CLocal.ThermalPrinter = thermalPrinter.getBluetoothDevice().getAddress();
                    edtMayInDaChon.setText(CLocal.ThermalPrinter);
                    SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                    editor.putString("ThermalPrinter", CLocal.ThermalPrinter);
                    editor.commit();
                } catch (Exception ex) {
                }
            }
        });

        if (CLocal.SyncTrucTiep == true)
            radTrucTiep.setChecked(true);
//        else
//            radGianTiep.setChecked(true);

        switch (CLocal.MethodPrinter) {
            case "Intermec":
                radIntermec.setChecked(true);
                break;
            case "Honeywell31":
                radHoneywell3l.setChecked(true);
                break;
            case "Honeywell45":
                radHoneywell45.setChecked(true);
                break;
            case "ER58":
                radER58.setChecked(true);
                break;
        }

        radGroupSync.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radGroupSync.findViewById(checkedId);
                int index = radGroupSync.indexOfChild(radioButton);
                SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                switch (index) {
                    case 0: // first button
                        CLocal.SyncTrucTiep = true;
                        editor.putBoolean("SyncTrucTiep", true);
                        break;
                    case 1: // secondbutton
                        CLocal.SyncTrucTiep = false;
                        editor.putBoolean("SyncTrucTiep", false);
                        break;
                }
                editor.commit();
            }
        });

        radGroupMethodPrinter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radGroupMethodPrinter.findViewById(checkedId);
                int index = radGroupMethodPrinter.indexOfChild(radioButton);
                SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                switch (index) {
                    case 0: // first button
                        CLocal.MethodPrinter = "Honeywell31";
                        break;
                    case 1: // secondbutton
                        CLocal.MethodPrinter = "Honeywell45";
                        break;
                    case 2: // thirdbutton
                        CLocal.MethodPrinter = "ER58";
                        break;
                    case 3: // thirdbutton
                        CLocal.MethodPrinter = "Intermec";
                        break;
                }
                editor.putString("MethodPrinter", CLocal.MethodPrinter);
                editor.commit();
            }
        });
    }

    public void loadListView() {
        if (thermalPrinter != null) {
            //add danh sách thiết bị vào listview
            arrayBluetoothAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, thermalPrinter.getArrayList());
            lstView.setAdapter(arrayBluetoothAdapter);
        } else {
            MyAsyncTask_Thermal myAsyncTask_thermal = new MyAsyncTask_Thermal();
            myAsyncTask_thermal.execute();
        }
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

    @Override
    protected void onDestroy() {
//        thermalPrinter.disconnectBluetoothDevice();
        super.onDestroy();
    }

    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    public class MyAsyncTask_Thermal extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivitySettings.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang kết nối máy in");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (thermalPrinter == null || thermalPrinter.getBluetoothDevice() == null)
                thermalPrinter = new ThermalPrinter(ActivitySettings.this);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            loadListView();
        }
    }
}

