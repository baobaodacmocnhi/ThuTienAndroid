package vn.com.capnuoctanhoa.thutienandroid;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;

public class ActivitySearchKhachHang extends AppCompatActivity {
    EditText edtDanhBo, edtHoTen, edtSoNha, edtTenDuong;
    Button btnTimKiem;
    ListView lstView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_khach_hang);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .8));

        edtDanhBo = (EditText) findViewById(R.id.edtDanhBo);
        edtHoTen = (EditText) findViewById(R.id.edtHoTen);
        edtSoNha = (EditText) findViewById(R.id.edtSoNha);
        edtTenDuong = (EditText) findViewById(R.id.edtTenDuong);
        btnTimKiem = (Button) findViewById(R.id.btnTimKiem);
        lstView = (ListView) findViewById(R.id.lstView);

        btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtDanhBo.getText().toString().isEmpty() == false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("DanhBo");
                } else if (edtHoTen.getText().toString().isEmpty() == false || edtSoNha.getText().toString().isEmpty() == false || edtTenDuong.getText().toString().isEmpty() == false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("TTKH");
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

    private void loadListView(String[] values) {
        try {
            ArrayList<String> arrayList = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(values[1]);
            switch (values[0]) {
                case "DanhBo":
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String str = "Danh Bộ: " + new StringBuffer(jsonObject.getString("DanhBo")).insert(7, " ").insert(4, " ").toString()
                                + "\nKhách Hàng: " + jsonObject.getString("HoTen")
                                + "\nĐịa Chỉ: " + jsonObject.getString("DiaChi")
                                + "\nGB: " + jsonObject.getString("GiaBieu") + " ĐM: " + jsonObject.getString("DinhMuc")
                                + "\nKỳ: " + jsonObject.getString("Ky") + " Tiêu Thụ: " + jsonObject.getString("TieuThu")
                                + "\nTổng Cộng: " + CLocal.formatMoney(jsonObject.getString("TongCong"), "đ")
                                + "\nNgày Giải Trách: " + jsonObject.getString("NgayGiaiTrach");
                        arrayList.add(str);
                    }
                    break;
                case "TTKH":
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String str = "Danh Bộ: " + new StringBuffer(jsonObject.getString("DanhBo")).insert(7, " ").insert(4, " ").toString()
                                + "\nKhách Hàng: " + jsonObject.getString("HoTen")
                                + "\nĐịa Chỉ: " + jsonObject.getString("DiaChi");
                        arrayList.add(str);
                    }
                    break;
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
            lstView.setAdapter(arrayAdapter);
        } catch (Exception ex) {
        }

    }

    public class MyAsyncTask extends AsyncTask<String, String, Void> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivitySearchKhachHang.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            switch (strings[0]) {
                case "DanhBo":
                    String str = "";
                    str = ws.getDSHoaDon(edtDanhBo.getText().toString());
                    if (str.isEmpty() == false && str.equals("[]") == false) {
                        publishProgress(new String[]{"DanhBo", str});
                    }
                case "TTKH":
                    String str2 = "";
                    str2 = ws.getDSHoaDon(edtHoTen.getText().toString(), edtSoNha.getText().toString(), edtTenDuong.getText().toString());
                    if (str2.isEmpty() == false && str2.equals("[]") == false) {
                        publishProgress(new String[]{"TTKH", str2});
                    }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                loadListView(values);
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
