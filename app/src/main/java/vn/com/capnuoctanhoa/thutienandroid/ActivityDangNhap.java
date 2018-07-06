package vn.com.capnuoctanhoa.thutienandroid;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;

public class ActivityDangNhap extends AppCompatActivity {
    TextView txtUser;
    EditText edtUsername, edtPassword;
    Button btnDangNhap, btnDangXuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtUser = (TextView) findViewById(R.id.txtUser);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        btnDangXuat = (Button) findViewById(R.id.btnDangXuat);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.checkNetworkAvailable(getApplicationContext()) == false) {
                    Toast.makeText(ActivityDangNhap.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute("DangNhap");
            }
        });

        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.checkNetworkAvailable(getApplicationContext()) == false) {
                    Toast.makeText(ActivityDangNhap.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(new String[]{"DangXuat"});

                Reload();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Reload();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Reload() {
        edtUsername.setText("");
        edtPassword.setText("");
        if (CLocal.sharedPreferencesre.getBoolean("Login", false) == true) {
            txtUser.setText("Xin chào " + CLocal.sharedPreferencesre.getString("HoTen", ""));
            txtUser.setTextColor(getResources().getColor(R.color.colorLogin));
            edtUsername.setVisibility(View.INVISIBLE);
            edtPassword.setVisibility(View.INVISIBLE);
            btnDangNhap.setVisibility(View.INVISIBLE);
            btnDangXuat.setVisibility(View.VISIBLE);
        } else {
            txtUser.setText("Xin hãy đăng nhập");
            txtUser.setTextColor(getResources().getColor(R.color.colorLogout));
            edtUsername.setVisibility(View.VISIBLE);
            edtPassword.setVisibility(View.VISIBLE);
            btnDangNhap.setVisibility(View.VISIBLE);
            btnDangXuat.setVisibility(View.INVISIBLE);
        }
    }

    public class MyAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityDangNhap.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            switch (strings[0]) {
                case "DangNhap":
                    try {
                    result = ws.dangNhaps(edtUsername.getText().toString(), edtPassword.getText().toString(), CLocal.sharedPreferencesre.getString("UID", ""));
                    if (result.isEmpty() == false&&result.equals("[]")==false) {
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                        editor.putString("Username", jsonObject.getString("TaiKhoan"));
                        editor.putString("Password", jsonObject.getString("MatKhau"));
                        editor.putString("MaNV", jsonObject.getString("MaND"));
                        editor.putString("HoTen", jsonObject.getString("HoTen"));
                        editor.putString("jsonHanhThu", "");
                        editor.putString("jsonDongNuoc", "");
                        editor.putString("jsonDongNuocChild", "");
                        editor.putString("jsonMessage", "");
                        if(Boolean.parseBoolean(jsonObject.getString("Doi"))==true)
                        {
                            editor.putString("jsonTo", ws.getDSTo());
//                            editor.putString("jsonNhanVien", ws.getDSNhanVienDoi());
                            editor.putBoolean("Doi",Boolean.parseBoolean(jsonObject.getString("Doi")));
                        }
                        if(Boolean.parseBoolean(jsonObject.getString("ToTruong"))==true)
                        {
                            editor.putString("jsonNhanVien", ws.getDSNhanVienTo(jsonObject.getString("MaTo")));
                            editor.putBoolean("ToTruong",Boolean.parseBoolean(jsonObject.getString("ToTruong")));
                        }
                        editor.putBoolean("Login", true);
                        editor.commit();

                        publishProgress("DangNhap");
                        return "true";
                    } else {
                        return "false";
                    }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                case "DangXuat":
                    try {
                    result=ws.dangXuats(CLocal.sharedPreferencesre.getString("Username", ""),CLocal.sharedPreferencesre.getString("UID", ""));
                    if (result.isEmpty() == false) {
                        SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                        editor.putString("Username", "");
                        editor.putString("Password", "");
                        editor.putString("MaNV", "");
                        editor.putString("HoTen", "");
                        editor.putString("jsonHanhThu", "");
                        editor.putString("jsonDongNuoc", "");
                        editor.putString("jsonDongNuocChild", "");
                        editor.putString("jsonMessage", "");
                        editor.putString("jsonTo", "");
                        editor.putString("jsonNhanVien", "");
                        editor.putBoolean("Doi", false);
                        editor.putBoolean("ToTruong", false);
                        editor.putBoolean("Login", false);
                        editor.commit();

                        publishProgress("DangXuat");
                        return "true";
                    } else {
                        return "false";
                    }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                switch (values[0]) {
                    case "DangNhap":
                            Reload();
                        break;
                    case "DangXuat":
                        CLocal.initialCLocal();
                        break;
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (Boolean.parseBoolean(s) == true) {
//                CLocal.showPopupMessage(ActivityDangNhap.this, "THÀNH CÔNG");
                finish();
            } else
                CLocal.showPopupMessage(ActivityDangNhap.this, "THẤT BẠI");
        }
    }
}
