package vn.com.capnuoctanhoa.thutienandroid;

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

public class ActivitySettings extends AppCompatActivity {
    private EditText edtMayInDaChon, edtIDMobile;
    private Button btnGetThermal, btnGetIDMobile;
    private ArrayAdapter<String> arrayBluetoothAdapter;
    private ListView lstView;
    private ThermalPrinter thermalPrinter;
    private RadioButton radTrucTiep, radGianTiep, radEZ, radESC;
    private RadioGroup radGroupSync, radGroupMethodPrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtMayInDaChon = (EditText) findViewById(R.id.edtMayInDaChon);
        edtIDMobile = (EditText) findViewById(R.id.edtIDMobile);
        btnGetThermal = (Button) findViewById(R.id.btnGetThermal);
        btnGetIDMobile = (Button) findViewById(R.id.btnGetIDMobile);
        lstView = (ListView) findViewById(R.id.lstView);
        radTrucTiep = (RadioButton) findViewById(R.id.radTrucTiep);
        radGianTiep = (RadioButton) findViewById(R.id.radGianTiep);
        radGroupSync = (RadioGroup) findViewById(R.id.radGroupSync);
        radEZ = (RadioButton) findViewById(R.id.radEZ);
        radESC = (RadioButton) findViewById(R.id.radESC);
        radGroupMethodPrinter = (RadioGroup) findViewById(R.id.radGroupMethodPrinter);

        MyAsyncTask_Thermal myAsyncTask_thermal = new MyAsyncTask_Thermal();
        myAsyncTask_thermal.execute();
//        thermalPrinter=new ThermalPrinter(ActivitySettings.this);

        edtMayInDaChon.setText(CLocal.ThermalPrinter);
        btnGetThermal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadListView();

//                String str="Bằng chữ: một trăm năm mươi hai ng";
//                CLocal.showPopupMessage(ActivitySettings.this,String.valueOf(str.length()));

            }
        });

        btnGetIDMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtIDMobile.setText(CLocal.getAndroidID(ActivitySettings.this));
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
        else
            radGianTiep.setChecked(true);

        switch (CLocal.MethodPrinter) {
            case "EZ":
                radEZ.setChecked(true);
                break;
            case "ESC":
                radESC.setChecked(true);
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
                        CLocal.MethodPrinter = "ESC";
                        editor.putString("MethodPrinter", CLocal.MethodPrinter);
                        break;
                    case 1: // secondbutton
                        CLocal.MethodPrinter = "EZ";
                        editor.putString("MethodPrinter", CLocal.MethodPrinter);
                        break;
                }
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

    public class MyAsyncTask_Thermal extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            thermalPrinter = new ThermalPrinter(ActivitySettings.this);
            return null;
        }
    }
}

