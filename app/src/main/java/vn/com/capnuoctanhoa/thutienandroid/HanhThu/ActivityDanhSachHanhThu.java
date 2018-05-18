package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.CViewAdapter;
import vn.com.capnuoctanhoa.thutienandroid.CViewEntity;
import vn.com.capnuoctanhoa.thutienandroid.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDanhSachHanhThu extends AppCompatActivity {
    private Button btnDownload;
    private Spinner spnFilter;
    private ListView lstView;
    private CViewAdapter cViewAdapter;
private  ArrayList<CViewEntity> list = new ArrayList<CViewEntity>();

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

        btnDownload = (Button) findViewById(R.id.btnDownload);
        spnFilter = (Spinner) findViewById(R.id.spnFilter);
        lstView = (ListView) findViewById(R.id.lstView);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CLocal.checkNetworkAvailable(getApplicationContext()) == false) {
                    Toast.makeText(ActivityDanhSachHanhThu.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });

        spnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                cViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public void loadListView() {
        try {
            lstView.setAdapter(null);
            list = new ArrayList<CViewEntity>();
            switch (spnFilter.getSelectedItem().toString()) {
                case "Chưa Thu":
                case "Đã Thu":
                    if (CLocal.jsonHanhThu != null && CLocal.jsonHanhThu.length() > 0) {
                        int stt = 0;
                        for (int i = 0; i < CLocal.jsonHanhThu.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonHanhThu.getJSONObject(i);
                            if (Boolean.parseBoolean(jsonObject.getString("GiaiTrach")) == false) {
                                addEntity(jsonObject);
                            }
                        }
                    }
                    break;
                case "Giải Trách":
                    if (CLocal.jsonHanhThu != null && CLocal.jsonHanhThu.length() > 0) {
                        int stt = 0;
                        for (int i = 0; i < CLocal.jsonHanhThu.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonHanhThu.getJSONObject(i);
                            if (Boolean.parseBoolean(jsonObject.getString("GiaiTrach")) == true) {
                                addEntity(jsonObject);
                            }
                        }
                    }
                    break;
                case "Tạm Thu-Thu Hộ":
                    if (CLocal.jsonHanhThu != null && CLocal.jsonHanhThu.length() > 0) {
                        int stt = 0;
                        for (int i = 0; i < CLocal.jsonHanhThu.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonHanhThu.getJSONObject(i);
                            if (Boolean.parseBoolean(jsonObject.getString("ThuHo")) == true || Boolean.parseBoolean(jsonObject.getString("TamThu")) == true) {
                                addEntity(jsonObject);
                            }
                        }
                    }
                    break;
                default:
                    if (CLocal.jsonHanhThu != null && CLocal.jsonHanhThu.length() > 0) {
                        for (int i = 0; i < CLocal.jsonHanhThu.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonHanhThu.getJSONObject(i);
                            addEntity(jsonObject);
                        }
                    }
                    break;
            }
            cViewAdapter = new CViewAdapter(ActivityDanhSachHanhThu.this, list);
            lstView.setAdapter(cViewAdapter);
        } catch (Exception e) {
        }
    }

    public void addEntity(JSONObject jsonObject)
    {
        try
        {
        CViewEntity entity = new CViewEntity();
        entity.setSTT(String.valueOf(list.size() + 1));
        entity.setID(jsonObject.getString("ID"));
        entity.setRow1a(jsonObject.getString("DiaChi"));
        entity.setRow1b(jsonObject.getString("Ky")+": "+CLocal.formatMoney(jsonObject.getString("TongCong")));
        String strMLT = new StringBuffer(jsonObject.getString("MLT")).insert(4, " ").insert(2, " ").toString();
        entity.setRow2a(strMLT);
        String strDanhBo = new StringBuffer(jsonObject.getString("DanhBo")).insert(7, " ").insert(4, " ").toString();
        entity.setRow2b(strDanhBo);
        entity.setRow3a(jsonObject.getString("HoTen"));
        entity.setGiaiTrach(Boolean.parseBoolean(jsonObject.getString("GiaiTrach")));
        entity.setTamThu(Boolean.parseBoolean(jsonObject.getString("TamThu")));
        entity.setThuHo(Boolean.parseBoolean(jsonObject.getString("ThuHo")));

        list.add(entity);
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
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
            publishProgress(ws.getDSHoaDonTon(CLocal.sharedPreferencesre.getString("MaNV", ""), currentDate.format(new Date())));
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
                    loadListView();
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
