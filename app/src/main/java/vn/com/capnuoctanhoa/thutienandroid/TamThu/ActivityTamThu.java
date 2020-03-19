package vn.com.capnuoctanhoa.thutienandroid.TamThu;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CSort;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.Class.CustomAdapterListView;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityTamThu extends AppCompatActivity {
    private EditText edtFromDate, edtToDate;
    private Button btnXem;
    private TextView txtTongHD, txtTongCong;
    private ListView lstView;
    private CheckBox chkRutSot;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tam_thu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtFromDate = (EditText) findViewById(R.id.edtFromDate);
        edtToDate = (EditText) findViewById(R.id.edtToDate);
        chkRutSot = (CheckBox) findViewById(R.id.chkRutSot);
        btnXem = (Button) findViewById(R.id.btnXem);
        lstView = (ListView) findViewById(R.id.lstView);
        txtTongHD = (TextView) findViewById(R.id.txtTongHD);
        txtTongCong = (TextView) findViewById(R.id.txtTongCong);

        edtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                //hide soft keyboard
                InputMethodManager imm = (InputMethodManager) ActivityTamThu.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ActivityTamThu.this,
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
                //hide soft keyboard
                InputMethodManager imm = (InputMethodManager) ActivityTamThu.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ActivityTamThu.this,
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

        btnXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CLocal.checkNetworkAvailable(ActivityTamThu.this) == false) {
                    Toast.makeText(getApplicationContext(), "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
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

    public class MyAsyncTask extends AsyncTask<Void, String, Void> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityTamThu.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            publishProgress(ws.getDSTamThu(String.valueOf(chkRutSot.isChecked()), CLocal.MaNV, edtFromDate.getText().toString(), edtToDate.getText().toString()));
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                try {
                    ArrayList<CViewParent> lstDisplayed = new ArrayList<CViewParent>();
                    ArrayList<CViewParent> lstTG = new ArrayList<CViewParent>();
                    ArrayList<CViewParent> lstCQ = new ArrayList<CViewParent>();
                    int TongHD = 0, TongCong = 0;
                    JSONArray jsonArray = new JSONArray(values[0]);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CViewParent enViewParent = new CViewParent();
//                        enViewParent.setSTT(String.valueOf(lstDisplayed.size() + 1));
                        enViewParent.setID(jsonObject.getString("ID"));
                        String strMLT = new StringBuffer(jsonObject.getString("MLT")).insert(4, " ").insert(2, " ").toString();
                        enViewParent.setRow1a(strMLT);
                        enViewParent.setRow1b(jsonObject.getString("Ky") + ": " + CLocal.formatMoney(jsonObject.getString("TongCong"), "đ"));
                        String strDanhBo = new StringBuffer(jsonObject.getString("DanhBo")).insert(7, " ").insert(4, " ").toString();
                        enViewParent.setRow2a(strDanhBo);
                        if (Integer.parseInt(jsonObject.getString("GiaBieu")) <= 20)
                            lstTG.add(enViewParent);
                        else
                            lstCQ.add(enViewParent);
                        TongHD++;
                        TongCong += Long.parseLong(jsonObject.getString("TongCong"));
                    }
                    txtTongHD.setText(CLocal.formatMoney(String.valueOf(TongHD), ""));
                    txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));

                    Collections.sort(lstTG, new CSort("", -1));
                    Collections.sort(lstCQ, new CSort("", -1));
                    lstDisplayed.addAll(lstCQ);
                    lstDisplayed.addAll(lstTG);
                    for (int i = 0; i < lstDisplayed.size(); i++) {
                        lstDisplayed.get(i).setSTT(String.valueOf(i + 1));
                    }

                    CustomAdapterListView customAdapterListView = new CustomAdapterListView(ActivityTamThu.this, lstDisplayed);
                    lstView.setAdapter(customAdapterListView);
                    customAdapterListView.notifyDataSetChanged();
                } catch (Exception e) {
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
