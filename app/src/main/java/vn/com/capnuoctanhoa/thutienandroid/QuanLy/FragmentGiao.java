package vn.com.capnuoctanhoa.thutienandroid.QuanLy;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewAdapter;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewEntity;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.HanhThu.ActivityDanhSachHanhThu;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class FragmentGiao extends Fragment {
    private View rootView;
    private Spinner spnNam, spnKy, spnFromDot, spnToDot, spnTo;
    private Button btnXem;
    private ListView lstView;
    private LinearLayout layoutTo;
    private ArrayList<String> spnID_To, spnName_To;
    private String selectedTo = "";
    private TextView txtTongHD, txtTongCong;
    private long TongHD, TongCong;
    private ArrayList<CViewEntity> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_giao, container, false);

        layoutTo = (LinearLayout) rootView.findViewById(R.id.layoutTo);
        spnNam = (Spinner) rootView.findViewById(R.id.spnNam);
        spnKy = (Spinner) rootView.findViewById(R.id.spnKy);
        spnFromDot = (Spinner) rootView.findViewById(R.id.spnFromDot);
        spnToDot = (Spinner) rootView.findViewById(R.id.spnToDot);
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
                    spnID_To.add("0");
                    spnName_To.add("Tất Cả");
                    for (int i = 0; i < CLocal.jsonTo.length(); i++) {
                        JSONObject jsonObject = CLocal.jsonTo.getJSONObject(i);
                        if (Boolean.parseBoolean(jsonObject.getString("HanhThu")) == true) {
                            spnID_To.add(jsonObject.getString("MaTo"));
                            spnName_To.add(jsonObject.getString("TenTo"));
                        }
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
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            lstView.setAdapter(null);
            list = new ArrayList<CViewEntity>();
            TongHD = TongCong = 0;
            if (CLocal.Doi == true) {
                if(Integer.parseInt(selectedTo)==0) {
                    for (int i = 0; i < spnID_To.size(); i++) {
                        publishProgress(ws.getTongGiaoHoaDon(spnID_To.get(i), spnNam.getSelectedItem().toString(), spnKy.getSelectedItem().toString(), spnFromDot.getSelectedItem().toString(), spnToDot.getSelectedItem().toString()));
                    }
                }
                else
                publishProgress(ws.getTongGiaoHoaDon(selectedTo, spnNam.getSelectedItem().toString(), spnKy.getSelectedItem().toString(), spnFromDot.getSelectedItem().toString(), spnToDot.getSelectedItem().toString()));
            }
            else
            publishProgress(ws.getTongGiaoHoaDon(CLocal.MaTo, spnNam.getSelectedItem().toString(), spnKy.getSelectedItem().toString(), spnFromDot.getSelectedItem().toString(), spnToDot.getSelectedItem().toString()));

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                try {
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