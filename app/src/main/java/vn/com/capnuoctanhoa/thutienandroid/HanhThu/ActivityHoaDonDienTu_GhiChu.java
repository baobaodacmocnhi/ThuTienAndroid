package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityHoaDonDienTu_GhiChu extends AppCompatActivity {
    private EditText edtDienThoai, edtGiaBieu, edtNiemChi, edtDiemBe;
    private Button btnCapNhat;
    private String DanhBo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_dien_tu_ghi_chu);

        edtDienThoai = (EditText) findViewById(R.id.edtDienThoai);
        edtGiaBieu = (EditText) findViewById(R.id.edtGiaBieu);
        edtNiemChi = (EditText) findViewById(R.id.edtNiemChi);
        edtDiemBe = (EditText) findViewById(R.id.edtDiemBe);
        btnCapNhat = (Button) findViewById(R.id.btnCapNhat);

        try {
            if (CLocal.checkNetworkAvailable(ActivityHoaDonDienTu_GhiChu.this) == false) {
                Toast.makeText(ActivityHoaDonDienTu_GhiChu.this, "Không có Internet", Toast.LENGTH_LONG).show();
                return;
            }
            DanhBo = getIntent().getStringExtra("DanhBo");
            MyAsyncTask myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute("get");
        } catch (Exception ex) {
            CLocal.showToastMessage(ActivityHoaDonDienTu_GhiChu.this, ex.getMessage());
        }

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DanhBo != "") {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("update");
                } else
                    CLocal.showPopupMessage(ActivityHoaDonDienTu_GhiChu.this, "Lỗi Danh Bộ", "center");
            }
        });
    }

    public class MyAsyncTask extends AsyncTask<String, JSONArray, String[]> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityHoaDonDienTu_GhiChu.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String[] doInBackground(String... strings) {
            try {
                switch (strings[0]) {
                    case "get":
                        if (DanhBo.equals("") == false) {
                            JSONArray jsonResult = new JSONArray(ws.get_GhiChu(DanhBo));
                            if (jsonResult != null && jsonResult.length() > 0) {
                                publishProgress(jsonResult);
                            }
                            return new String[]{"", ""};
                        }
                        break;
                    case "update":
                        if (DanhBo.equals("") == false) {
                            String result = ws.update_GhiChu(CLocal.MaNV, DanhBo, edtDienThoai.getText().toString(), edtGiaBieu.getText().toString(), edtNiemChi.getText().toString(), edtDiemBe.getText().toString());
                            String[] results = result.split(";");
                            if (Boolean.parseBoolean(results[0]) == true)
                                return new String[]{"update", "THÀNH CÔNG"};
                            else
                                return new String[]{"update", "THẤT BẠI\n" + results[1]};
                        }
                        break;
                }
            } catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            try {
                for (int i = 0; i < values[0].length(); i++) {
                    JSONObject jsonObject = values[0].getJSONObject(i);
                    edtDienThoai.setText(jsonObject.getString("DienThoai"));
                    edtGiaBieu.setText(jsonObject.getString("GiaBieu"));
                    edtNiemChi.setText(jsonObject.getString("NiemChi"));
                    edtDiemBe.setText(jsonObject.getString("DiemBe"));
                }
            } catch (Exception ex) {

            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (strings[0].equals("update") == true) {
                CLocal.showPopupMessage(ActivityHoaDonDienTu_GhiChu.this, strings[1], "center");
            }
        }
    }
}
