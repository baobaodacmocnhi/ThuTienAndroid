package vn.com.capnuoctanhoa.thutienandroid;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class ActivityDangNhap extends AppCompatActivity {
    TextView txtUser;
    EditText edtUsername, edtPassword;
    Button btnDangNhap, btnDangXuat, btnThoat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        txtUser = (TextView) findViewById(R.id.txtUser);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        btnDangXuat = (Button) findViewById(R.id.btnDangXuat);
        btnThoat = (Button) findViewById(R.id.btnThoat);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.CheckNetworkAvailable(getApplicationContext()) == false) {
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
                if (CLocal.CheckNetworkAvailable(getApplicationContext()) == false) {
                    Toast.makeText(ActivityDangNhap.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                String Username = CLocal.sharedPreferencesre.getString("Username", "");
                SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                editor.remove("Username");
                editor.remove("Password");
                editor.remove("MaNV");
                editor.remove("HoTen");
                editor.putBoolean("Login", false);
                editor.commit();
                Reload();
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(new String[]{"DangXuat", Username});
            }
        });

        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Reload();
    }

    public void Reload() {
        edtUsername.setText("");
        edtPassword.setText("");
        if (CLocal.sharedPreferencesre.getBoolean("Login", false) == true) {
            txtUser.setText("Xin chào " + CLocal.sharedPreferencesre.getString("HoTen", ""));
            btnDangNhap.setVisibility(View.INVISIBLE);
            btnDangXuat.setVisibility(View.VISIBLE);
        } else {
            txtUser.setText("Xin chào");
            btnDangNhap.setVisibility(View.VISIBLE);
            btnDangXuat.setVisibility(View.INVISIBLE);
        }
    }

    public class MyAsyncTask extends AsyncTask<String, String, Void> {
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
        protected Void doInBackground(String... strings) {
            switch (strings[0]) {
                case "DangNhap":
                    publishProgress(ws.DangNhap(edtUsername.getText().toString(), edtPassword.getText().toString(), CLocal.sharedPreferencesre.getString("UID", "")));
                    break;
                case "DangXuat":
                    publishProgress(ws.DangXuat(strings[1]));
                    break;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... strings) {
            super.onProgressUpdate(strings);
            if (strings != null && (strings[0] != "true" || strings[0] != "false")) {
                try {
                    JSONArray jsonArray = new JSONArray(strings[0]);
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
