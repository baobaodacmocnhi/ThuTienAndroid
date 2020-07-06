package vn.com.capnuoctanhoa.thutienandroid.Doi;

import androidx.appcompat.app.AppCompatActivity;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.Class.CustomAdapterListViewNopTien;
import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityDownDataDongNuoc;
import vn.com.capnuoctanhoa.thutienandroid.R;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityNopTien extends AppCompatActivity {
    private EditText edtFromDate, edtToDate;
    private DatePickerDialog datePickerDialog;
    private Button btnXem, btnThem;
    private ListView lstView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nop_tien);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lstView = (ListView) findViewById(R.id.lstView);
        edtFromDate = (EditText) findViewById(R.id.edtFromDate);
        edtToDate = (EditText) findViewById(R.id.edtToDate);
        btnXem = (Button) findViewById(R.id.btnXem);
        btnThem = (Button) findViewById(R.id.btnThem);

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date dateCapNhat = new Date();
        edtFromDate.setText(currentDate.format(dateCapNhat));
        edtToDate.setText(currentDate.format(dateCapNhat));

        edtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                //hide soft keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ActivityNopTien.this,
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
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ActivityNopTien.this,
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
            public void onClick(View view) {
                if (edtFromDate.getText().equals("") == false && edtToDate.getText().equals("") == false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("get");
                }
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtFromDate.getText().equals("") == false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("them");
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

    public class MyAsyncTask extends AsyncTask<String, String, String[]> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityNopTien.this);
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
                        publishProgress(ws.getDS_ChotDangNgan(edtFromDate.getText().toString(), edtToDate.getText().toString()));
                        break;
                    case "them":
                        String result = ws.them_ChotDangNgan(edtFromDate.getText().toString(), CLocal.MaNV);
                        String[] results = result.split(";");
                        if (Boolean.parseBoolean(results[0]) == true)
                            publishProgress(ws.getDS_ChotDangNgan(edtFromDate.getText().toString(), edtToDate.getText().toString()));
                        else
                            return results;
                        break;
                }
                return new String[]{"true", ""};
            } catch (Exception ex) {
                return new String[]{"false", ex.getMessage()};
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                try {
                    JSONArray jsonArray = new JSONArray(values[0]);
                    ArrayList<CViewParent> lst = new ArrayList<CViewParent>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CViewParent cViewParent = new CViewParent();
                        cViewParent.setID(jsonObject.getString("ID"));
                        cViewParent.setChot(Boolean.parseBoolean(jsonObject.getString("Chot")));
                        cViewParent.setNgayChot(jsonObject.getString("NgayChot"));
                        String Loai = "", SoLuong = "", TongCong = "";
                        Loai = "Đăng Ngân\nCNKĐ\nGiấy\nHĐĐT\nNộp Tiền";
                        SoLuong = CLocal.formatMoney(jsonObject.getString("SLDangNgan").replace("null", "0"), "") + "\n"
                                + CLocal.formatMoney(jsonObject.getString("SLCNKD").replace("null", "0"), "") + "\n"
                                + CLocal.formatMoney(jsonObject.getString("SLGiay").replace("null", "0"), "") + "\n"
                                + CLocal.formatMoney(jsonObject.getString("SLHDDT").replace("null", "0"), "") + "\n"
                                + CLocal.formatMoney(jsonObject.getString("SLNopTien").replace("null", "0"), "");
                        TongCong = CLocal.formatMoney(jsonObject.getString("TCDangNgan").replace("null", "0"), "") + "\n"
                                + CLocal.formatMoney(jsonObject.getString("TCCNKD").replace("null", "0"), "") + "\n"
                                + CLocal.formatMoney(jsonObject.getString("TCGiay").replace("null", "0"), "") + "\n"
                                + CLocal.formatMoney(jsonObject.getString("TCHDDT").replace("null", "0"), "") + "\n"
                                + CLocal.formatMoney(jsonObject.getString("TCNopTien").replace("null", "0"), "");
                        cViewParent.setLoai(Loai);
                        cViewParent.setSoLuong(SoLuong);
                        cViewParent.setTongCong(TongCong);
                        lst.add(cViewParent);
                    }
                    CustomAdapterListViewNopTien customAdapterListViewNopTien = new CustomAdapterListViewNopTien(ActivityNopTien.this, lst);
                    lstView.setAdapter(customAdapterListViewNopTien);
                } catch (Exception ex) {
                    CLocal.showToastMessage(ActivityNopTien.this, ex.getMessage());
                }
            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (Boolean.parseBoolean(strings[0]) == true) {
//                CLocal.showPopupMessage(ActivityNopTien.this, "THÀNH CÔNG", "center");
            } else {
                CLocal.showPopupMessage(ActivityNopTien.this, "THẤT BẠI\n" + strings[1], "center");
            }
        }
    }
}
