package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.CViewAdapter;
import vn.com.capnuoctanhoa.thutienandroid.CViewEntity;
import vn.com.capnuoctanhoa.thutienandroid.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDanhSachDongNuoc extends AppCompatActivity {
    private EditText edtFromDate, edtToDate;
    private DatePickerDialog datePickerDialog;
    private Button btnDownload;
    private ListView lstView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_dong_nuoc);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtFromDate = (EditText) findViewById(R.id.edtFromDate);
        edtToDate = (EditText) findViewById(R.id.edtToDate);

//        edtFromDate.setText("23/04/2018");
//        edtToDate.setText("23/04/2018");

        edtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ActivityDanhSachDongNuoc.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                edtFromDate.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        edtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ActivityDanhSachDongNuoc.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                edtToDate.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.CheckNetworkAvailable(ActivityDanhSachDongNuoc.this) == false) {
                    Toast.makeText(ActivityDanhSachDongNuoc.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });

        lstView = (ListView) findViewById(R.id.lstView);

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                PopupMenu popup = new PopupMenu(ActivityDanhSachDongNuoc.this, view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.popup_action_DongNuoc:
                                TextView MaDN = (TextView) view.findViewById(R.id.lvID);

                                Intent intent = new Intent(ActivityDanhSachDongNuoc.this, ActivityDongNuoc.class);
                                intent.putExtra("MaDN", MaDN.getText().toString());
                                startActivity(intent);
                                break;
                            case R.id.popup_action_MoNuoc:
                                TextView MaDN2 = (TextView) view.findViewById(R.id.lvID);

                                Intent intent2 = new Intent(ActivityDanhSachDongNuoc.this, ActivityMoNuoc.class);
                                intent2.putExtra("MaDN", MaDN2.getText().toString());
                                startActivity(intent2);
                                break;
                        }
                        return true;
                    }
                });
                popup.getMenuInflater().inflate(R.menu.popup_dong_nuoc, popup.getMenu());
                popup.show();
            }
        });

        LoadListView();
    }

    public class MyAsyncTask extends AsyncTask<Void, String, Void> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityDanhSachDongNuoc.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            publishProgress(ws.GetDSDongNuoc(CLocal.sharedPreferencesre.getString("MaNV", ""), edtFromDate.getText().toString(), edtToDate.getText().toString()));
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                try {
                    SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                    editor.putString("jsonDongNuoc", values[0]);
                    editor.commit();
                    CLocal.jsonDongNuoc = new JSONArray(values[0]);
                    LoadListView();
                } catch (Exception ex) {

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

    public void LoadListView() {
        try {
            if (CLocal.jsonDongNuoc != null && CLocal.jsonDongNuoc.length() > 0) {
                ArrayList<CViewEntity> list = new ArrayList<CViewEntity>();
                for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                    JSONObject jsonObject = CLocal.jsonDongNuoc.getJSONObject(i);
                    CViewEntity entity = new CViewEntity();
                    entity.setSTT(String.valueOf(i + 1));
                    entity.setID(jsonObject.getString("MaDN"));
                    entity.setName1(jsonObject.getString("MLT"));
                    entity.setName2(jsonObject.getString("DiaChi"));
                    entity.setContent1(jsonObject.getString("DanhBo"));
                    entity.setContent2(jsonObject.getString("HoTen"));
//                        if (jsonObject.getString("NGAYGIAITRACH") != null || jsonObject.getString("NGAYGIAITRACH") != "")
//                            entity.setBackgroundColor(CLocal.Color_DaThu);
                    list.add(entity);
                }
                CViewAdapter adapter = new CViewAdapter(ActivityDanhSachDongNuoc.this, list);
                lstView.setAdapter(adapter);
            }
        } catch (Exception e) {

        }
    }
}
