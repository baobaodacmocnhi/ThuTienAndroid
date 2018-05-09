package vn.com.capnuoctanhoa.thutienandroid;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityDangNhap extends AppCompatActivity {
    TextView txtUser;
    EditText edtUsername, edtPassword;
    Button btnDangNhap, btnDangXuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                String Username = CLocal.sharedPreferencesre.getString("Username", "");
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(new String[]{"DangXuat", Username});

                Reload();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Reload();
    }

    public void Reload() {
        edtUsername.setText("");
        edtPassword.setText("");
        if (CLocal.sharedPreferencesre.getBoolean("Login", false) == true) {
            txtUser.setText("Xin chào " + CLocal.sharedPreferencesre.getString("HoTen", ""));
            edtUsername.setVisibility(View.INVISIBLE);
            edtPassword.setVisibility(View.INVISIBLE);
            btnDangNhap.setVisibility(View.INVISIBLE);
            btnDangXuat.setVisibility(View.VISIBLE);
        } else {
            txtUser.setText("Xin chào");
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
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "DangNhap":
                    String str = "";
                    str = ws.dangNhap(edtUsername.getText().toString(), edtPassword.getText().toString(), CLocal.sharedPreferencesre.getString("UID", ""));
                    if (str.isEmpty() == false) {
                        publishProgress(new String[]{"DangNhap",str});
                        return "true";
                    } else {
                        return "false";
                    }
                case "DangXuat":
                    String str2 = "";
                    str2=ws.dangXuat(strings[1]);
                    if (str2.isEmpty() == false) {
                        publishProgress(new String[]{"DangXuat",str2});
                        return "true";
                    } else {
                        return "false";
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
                        try {
                            JSONArray jsonArray = new JSONArray(values[1]);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                            editor.putString("Username", jsonObject.getString("TaiKhoan"));
                            editor.putString("Password", jsonObject.getString("MatKhau"));
                            editor.putString("MaNV", jsonObject.getString("MaND"));
                            editor.putString("HoTen", jsonObject.getString("HoTen"));
                            editor.putBoolean("Login", true);
                            editor.commit();

                            Reload();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "DangXuat":
                        SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                        editor.remove("Username");
                        editor.remove("Password");
                        editor.remove("MaNV");
                        editor.remove("HoTen");
                        editor.remove("jsonHanhThu");
                        editor.remove("jsonDongNuoc");
                        editor.putBoolean("Login", false);
                        editor.commit();
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
                CLocal.showPopupMessage(ActivityDangNhap.this, "THÀNH CÔNG");
                finish();
            } else
                CLocal.showPopupMessage(ActivityDangNhap.this, "THẤT BẠI");
        }
    }
}
