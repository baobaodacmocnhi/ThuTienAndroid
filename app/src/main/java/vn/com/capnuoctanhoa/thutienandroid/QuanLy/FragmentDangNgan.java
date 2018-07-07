package vn.com.capnuoctanhoa.thutienandroid.QuanLy;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewAdapter;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewEntity;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class FragmentDangNgan extends Fragment {
    private View rootView;
    private DatePickerDialog datePickerDialog;
    private EditText edtFromDate, edtToDate;
    private Spinner  spnTo;
    private Button btnXem;
    private ListView lstView;
    private LinearLayout layoutTo;
    private ArrayList<String> spnID_To, spnName_To;
    private String selectedTo = "";
    private TextView txtTongHD, txtTongCong;
    private long TongHD, TongCong;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dang_ngan, container, false);

        layoutTo = (LinearLayout) rootView.findViewById(R.id.layoutTo);
        edtFromDate = (EditText) rootView.findViewById(R.id.edtFromDate);
        edtToDate = (EditText) rootView.findViewById(R.id.edtToDate);
        spnTo = (Spinner) rootView.findViewById(R.id.spnTo);
        btnXem = (Button) rootView.findViewById(R.id.btnXem);
        lstView = (ListView) rootView.findViewById(R.id.lstView);
        txtTongHD = (TextView) rootView.findViewById(R.id.txtTongHD);
        txtTongCong = (TextView) rootView.findViewById(R.id.txtTongCong);

        if (CLocal.Doi == true) {
            layoutTo.setVisibility(View.VISIBLE);
            try {
                if (CLocal.jsonTo != null && CLocal.jsonTo.length() > 0) {
                    spnID_To = new ArrayList<>();
                    spnName_To = new ArrayList<>();
                    for (int i = 0; i < CLocal.jsonTo.length(); i++) {
                        JSONObject jsonObject = CLocal.jsonTo.getJSONObject(i);

                            spnID_To.add(jsonObject.getString("MaTo"));
                            spnName_To.add(jsonObject.getString("TenTo"));

                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spnName_To);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnTo.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            layoutTo.setVisibility(View.GONE);
        }

        spnTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTo = spnID_To.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getActivity(),
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
                datePickerDialog = new DatePickerDialog(getActivity(),
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
                if (CLocal.checkNetworkAvailable(getActivity().getApplicationContext()) == false) {
                    Toast.makeText(getActivity(), "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });

        return  rootView;
    }

    public class MyAsyncTask extends AsyncTask<Void, String, Void> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (CLocal.Doi == false)
                selectedTo = CLocal.MaTo;
            publishProgress(ws.getTongDangNgan(selectedTo, edtFromDate.getText().toString(), edtToDate.getText().toString()));

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                try {
                    lstView.setAdapter(null);
                    ArrayList<CViewEntity> list = new ArrayList<CViewEntity>();
                    TongHD = TongCong = 0;
                    JSONArray jsonArray=new JSONArray(values[0]);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CViewEntity entity = new CViewEntity();
                        entity.setRow1a(jsonObject.getString("HoTen"));
                        entity.setRow2a(jsonObject.getString("TongHD"));
                        entity.setRow2b(CLocal.formatMoney(jsonObject.getString("TongCong"), "đ"));
                        list.add(entity);
                        TongHD += Long.parseLong(jsonObject.getString("TongHD"));
                        TongCong += Long.parseLong(jsonObject.getString("TongCong"));
                    }
                    CViewAdapter cViewAdapter = new CViewAdapter(getActivity(), list);
                    lstView.setAdapter(cViewAdapter);
                    txtTongHD.setText(CLocal.formatMoney(String.valueOf(TongHD), ""));
                    txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
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
