package vn.com.capnuoctanhoa.thutienandroid.Admin;

import androidx.appcompat.app.AppCompatActivity;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityChild;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.HanhThu.ActivityDownDataHanhThu;
import vn.com.capnuoctanhoa.thutienandroid.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ActivityAdmin extends AppCompatActivity {
    private Button btnAction;
    private RadioButton radTruyVan, radCapNhat;
    private EditText edtSQL, edtResult;
    private CWebservice ws = new CWebservice();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnAction = (Button) findViewById(R.id.btnAction);
        radTruyVan = (RadioButton) findViewById(R.id.radTruyVan);
        radCapNhat = (RadioButton) findViewById(R.id.radCapNhat);
        edtSQL = (EditText) findViewById(R.id.edtSQL);
        edtResult = (EditText) findViewById(R.id.edtResult);

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });
    }

    public class MyAsyncTask extends AsyncTask<Void, String, String[]> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityAdmin.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            try {

                if (radTruyVan.isChecked() == true) {
                    String str = "";
                    JSONArray jsonResult = new JSONArray(ws.truyvan(edtSQL.getText().toString()));
                    for (int i = 0; i < jsonResult.length(); i++) {
                        JSONObject jsonObject = jsonResult.getJSONObject(i);
                        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                            String keyStr = it.next();
                            Object keyvalue = jsonObject.get(keyStr);

                            //Print key and value
                            str += keyStr + ": " + keyvalue + "\n";
                        }
                    }
                    String[] results = new String[]{"truyvan", str};
                    publishProgress(results);
                } else if (radCapNhat.isChecked() == true) {
                    String result = ws.capnhat(edtSQL.getText().toString());
                    String[] results = new String[]{"capnhat", result};
                    publishProgress(results);
                }
                return new String[]{"true", " "};
            } catch (Exception ex) {
                return new String[]{"false", ex.getMessage()};
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            try {
                if (values[0].equals("truyvan") == true) {
                    edtResult.setText(values[1].toString());
                } else if (values[0].equals("capnhat") == true) {
                    edtResult.setText(values[1].toString());
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
            if (Boolean.parseBoolean(strings[0]) == true) {

            } else {
                CLocal.showPopupMessage(ActivityAdmin.this, strings[1], "center");
            }
        }
    }
}