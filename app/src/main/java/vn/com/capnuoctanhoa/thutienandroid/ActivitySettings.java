package vn.com.capnuoctanhoa.thutienandroid;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.Bluetooth.ThermalPrinter;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;

public class ActivitySettings extends AppCompatActivity {
    private EditText edtMayInDaChon;
    private Button btnGetThermal;
    private ArrayAdapter<String> arrayBluetoothAdapter;
    private ListView lstView;
    private ThermalPrinter thermalPrinter;
    private RadioButton radTrucTiep, radGianTiep;
    private RadioGroup radGroupSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtMayInDaChon = (EditText) findViewById(R.id.edtMayInDaChon);
        btnGetThermal = (Button) findViewById(R.id.btnGetThermal);
        lstView = (ListView) findViewById(R.id.lstView);
        radTrucTiep = (RadioButton) findViewById(R.id.radTrucTiep);
        radGianTiep = (RadioButton) findViewById(R.id.radGianTiep);
        radGroupSync = (RadioGroup) findViewById(R.id.radGroupSync);

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

        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    thermalPrinter.setBluetoothDevice(thermalPrinter.getLstBluetoothDevice().get(position));
                    CLocal.ThermalPrinter = thermalPrinter.getBluetoothDevice().getName();
                    edtMayInDaChon.setText(CLocal.ThermalPrinter);
                    SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                    editor.putString("ThermalPrinter", thermalPrinter.getBluetoothDevice().getName());
                    editor.commit();
                } catch (Exception ex) {
                }
            }
        });

        if (CLocal.SyncTrucTiep == true)
            radTrucTiep.setChecked(true);
        else
            radGianTiep.setChecked(true);

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

