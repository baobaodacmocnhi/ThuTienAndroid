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

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class ActivityDangNhap extends AppCompatActivity {
    TextView txtUser;
    EditText edtUsername, edtPassword;
    Button btnDangNhap, btnDangXuat, btnThoat;
    SharedPreferences sharedPreferencesre;

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
        sharedPreferencesre = getSharedPreferences("my_data", MODE_PRIVATE);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });

        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferencesre.edit();
                editor.remove("Username");
                editor.remove("Password");
                editor.remove("MaNV");
                editor.remove("HoTen");
                editor.putBoolean("Login", false);
                editor.commit();
                Reload();
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

    public void Reload()
    {
        edtUsername.setText("");
        edtPassword.setText("");
        if (sharedPreferencesre.getBoolean("Login", false) == true) {
            txtUser.setText("Xin chào " + sharedPreferencesre.getString("HoTen", ""));
            btnDangNhap.setVisibility(View.INVISIBLE);
            btnDangXuat.setVisibility(View.VISIBLE);
        } else {
            txtUser.setText("Xin chào");
            btnDangNhap.setVisibility(View.VISIBLE);
            btnDangXuat.setVisibility(View.INVISIBLE);
        }
    }

    public class MyAsyncTask extends AsyncTask<Void, SoapObject, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityDangNhap.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.show();
        }

        CWebservice ws = new CWebservice();

        @Override
        protected Void doInBackground(Void... voids) {
            publishProgress(ws.DangNhap(edtUsername.getText().toString(), edtPassword.getText().toString()));
            return null;
        }

        @Override
        protected void onProgressUpdate(SoapObject... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                SoapObject user = values[0];
                for (int i = 0; i < user.getPropertyCount(); i++) {
                    SoapObject obj = (SoapObject) user.getProperty(i);

                    SharedPreferences.Editor editor = sharedPreferencesre.edit();
                    editor.putString("Username", obj.getProperty("TaiKhoan").toString());
                    editor.putString("Password", obj.getProperty("MatKhau").toString());
                    editor.putString("MaNV", obj.getProperty("MaND").toString());
                    editor.putString("HoTen", obj.getProperty("HoTen").toString());
                    editor.putBoolean("Login", true);
                    editor.commit();

                    Reload();
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
