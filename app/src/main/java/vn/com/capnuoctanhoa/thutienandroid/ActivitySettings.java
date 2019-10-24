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
import android.widget.TextView;

import vn.com.capnuoctanhoa.thutienandroid.Bluetooth.ThermalPrinter;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;

public class ActivitySettings extends AppCompatActivity {
    private Button btnGetThermal;
    private ArrayAdapter<String> arrayBluetoothAdapter;
    private ListView lstView;
    private ThermalPrinter thermalPrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnGetThermal=(Button) findViewById(R.id.btnGetThermal);
        lstView = (ListView) findViewById(R.id.lstView);

        MyAsyncTask_Thermal myAsyncTask_thermal=new MyAsyncTask_Thermal();
        myAsyncTask_thermal.execute();
//        thermalPrinter=new ThermalPrinter(ActivitySettings.this);

        btnGetThermal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadListView();
//                String []str="10/10/2019 12:12:10".split(" ");
//                CLocal.showPopupMessage(ActivitySettings.this,str[0]);
            }
        });

        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    thermalPrinter.setBluetoothDevice(thermalPrinter.getLstBluetoothDevice().get(position));
                    CLocal.ThermalPrinter = thermalPrinter.getBluetoothDevice().getName();
                    SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                    editor.putString("ThermalPrinter", thermalPrinter.getBluetoothDevice().getName());
                    editor.commit();
                }catch (Exception ex){}
            }
        });

    }

    public  void loadListView()
    {
        if(thermalPrinter!=null) {
            //add danh sách thiết bị vào listview
            arrayBluetoothAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, thermalPrinter.getArrayList());
            lstView.setAdapter(arrayBluetoothAdapter);
        }
        else
        {
            MyAsyncTask_Thermal myAsyncTask_thermal=new MyAsyncTask_Thermal();
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
        thermalPrinter.disconnectBluetoothDevice();
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

