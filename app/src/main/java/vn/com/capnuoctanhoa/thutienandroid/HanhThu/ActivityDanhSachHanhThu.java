package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.CViewAdapter;
import vn.com.capnuoctanhoa.thutienandroid.CViewEntity;
import vn.com.capnuoctanhoa.thutienandroid.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDanhSachHanhThu extends AppCompatActivity {
    private Spinner spnNam, spnKy, spnFromDot, spnToDot;
    private Button btnDownload;
    private ListView lstView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_hanh_thu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spnNam = (Spinner) findViewById(R.id.spnNam);
        spnKy = (Spinner) findViewById(R.id.spnKy);
        spnFromDot = (Spinner) findViewById(R.id.spnFromDot);
        spnToDot = (Spinner) findViewById(R.id.spnToDot);

        btnDownload = (Button) findViewById(R.id.btnDownload);
        lstView = (ListView) findViewById(R.id.lstView);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CLocal.CheckNetworkAvailable(getApplicationContext()) == false) {
                    Toast.makeText(ActivityDanhSachHanhThu.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });

    }

    public void LoadListView() {
        try {
            if (CLocal.jsonHanhThu != null && CLocal.jsonHanhThu.length() > 0) {
                ArrayList<CViewEntity> list = new ArrayList<CViewEntity>();
                for (int i = 0; i < CLocal.jsonHanhThu.length(); i++) {
                    JSONObject jsonObject = CLocal.jsonHanhThu.getJSONObject(i);
                    CViewEntity entity = new CViewEntity();
                    entity.setSTT(String.valueOf(i + 1));
                    entity.setID(jsonObject.getString("SOHOADON"));
                    entity.setName1(jsonObject.getString("DANHBA"));
                    entity.setName2(jsonObject.getString("TONGCONG"));
                    entity.setContent1(jsonObject.getString("SO") + " " + jsonObject.getString("DUONG"));
                    if (jsonObject.getString("NGAYGIAITRACH") != null || jsonObject.getString("NGAYGIAITRACH") != "")
                        entity.setBackgroundColor(CLocal.Color_DaThu);
                    list.add(entity);
                }
                CViewAdapter adapter = new CViewAdapter(ActivityDanhSachHanhThu.this, list);
                lstView.setAdapter(adapter);
            }
        } catch (Exception e) {
        }
    }

    public class MyAsyncTask extends AsyncTask<Void, String, Void> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityDanhSachHanhThu.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            publishProgress(ws.GetDSHoaDon(spnNam.getSelectedItem().toString(), spnKy.getSelectedItem().toString(), spnFromDot.getSelectedItem().toString(), spnToDot.getSelectedItem().toString(), CLocal.sharedPreferencesre.getString("MaNV", "")));
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                try {
                    SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                    editor.putString("jsonHanhThu", values[0]);
                    editor.commit();
                    CLocal.jsonHanhThu = new JSONArray(values[0]);
                    LoadListView();
                } catch (Exception e) {
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }
}
