package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.CViewAdapter;
import vn.com.capnuoctanhoa.thutienandroid.CViewEntity;
import vn.com.capnuoctanhoa.thutienandroid.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class FragmentDanhSachDongNuoc extends Fragment {
    private View rootView;
    private EditText edtFromDate, edtToDate;
    private DatePickerDialog datePickerDialog;
    private Button btnDownload;
    private ListView lstView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_danh_sach_dong_nuoc, container, false);

        edtFromDate = (EditText) rootView.findViewById(R.id.edtFromDate);
        edtToDate = (EditText) rootView.findViewById(R.id.edtToDate);

        edtFromDate.setText("23/04/2018");
        edtToDate.setText("23/04/2018");

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

        btnDownload = (Button) rootView.findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.CheckNetworkAvailable(getContext()) == false) {
                    Toast.makeText(getActivity(), "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });

        lstView = (ListView) rootView.findViewById(R.id.lstView);
        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView MaDN = (TextView) view.findViewById(R.id.lvID);

                Bundle bundle = new Bundle();
                bundle.putString("MaDN", MaDN.getText().toString());

                FragmentDongNuoc fragmentDongNuoc = new FragmentDongNuoc();
                fragmentDongNuoc.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_dong_nuoc, fragmentDongNuoc);
                fragmentTransaction.commit();

                TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.tabs);
                tabhost.getTabAt(1).select();

                return false;
            }
        });

        return rootView;
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
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            publishProgress(ws.GetDSDongNuoc(CLocal.sharedPreferencesre.getString("MaNV", ""), edtFromDate.getText().toString(), edtToDate.getText().toString()));
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                try {
                    CLocal.jsonArray_DongNuoc = new JSONArray(values[0]);
                    ArrayList<CViewEntity> list = new ArrayList<CViewEntity>();
                    for (int i = 0; i < CLocal.jsonArray_DongNuoc.length(); i++) {
                        JSONObject jsonObject = CLocal.jsonArray_DongNuoc.getJSONObject(i);
                        CViewEntity entity = new CViewEntity();
                        entity.setSTT(String.valueOf(i+1));
                        entity.setID(jsonObject.getString("MaDN"));
                        entity.setName1(jsonObject.getString("DiaChi"));
//                        entity.setName2(jsonObject.getString("TONGCONG"));
                        entity.setContent(jsonObject.getString("DanhBo") + " " + jsonObject.getString("HoTen"));
//                        if (jsonObject.getString("NGAYGIAITRACH") != null || jsonObject.getString("NGAYGIAITRACH") != "")
//                            entity.setBackgroundColor(CLocal.Color_DaThu);
                        list.add(entity);
                    }
                    CViewAdapter adapter = new CViewAdapter(getActivity(), list);
                    lstView.setAdapter(adapter);
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
