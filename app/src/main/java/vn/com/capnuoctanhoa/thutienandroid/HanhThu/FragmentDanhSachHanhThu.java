package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.CViewAdapter;
import vn.com.capnuoctanhoa.thutienandroid.CViewEntity;
import vn.com.capnuoctanhoa.thutienandroid.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class FragmentDanhSachHanhThu extends Fragment {
    private View rootView;
    private Spinner spnNam, spnKy, spnFromDot, spnToDot;
    private Button btnDownload;
    private ListView lstView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_danh_sach_hanh_thu, container, false);

        spnNam = (Spinner) rootView.findViewById(R.id.spnNam);
//        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, CLocal.arrayspnNam);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnNam.setAdapter(adapter);

        spnKy = (Spinner) rootView.findViewById(R.id.spnKy);
        spnFromDot = (Spinner) rootView.findViewById(R.id.spnFromDot);
        spnToDot = (Spinner) rootView.findViewById(R.id.spnToDot);

        btnDownload = (Button) rootView.findViewById(R.id.btnDownload);
        lstView = (ListView) rootView.findViewById(R.id.lstView);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CLocal.CheckNetworkAvailable(getContext()) == false) {
                    Toast.makeText(getActivity(), "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                /*String FileName = spnNam.getSelectedItem().toString() + "_" + spnKy.getSelectedItem().toString() + "_" + spnFromDot.getSelectedItem().toString() + "_" + spnToDot.getSelectedItem().toString();
                File file = new File(CLocal.Path + "/" + FileName);
                if (file.exists() == false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute();
                } else
                    Toast.makeText(getActivity(), "File đã có rồi", Toast.LENGTH_LONG).show();*/
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*Bundle bundle = new Bundle();
                bundle.putString("FileName", lstView.getItemAtPosition(i).toString());

                FragmentHanhThu hanhthu = new FragmentHanhThu();
                hanhthu.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.idHanhThuLayout, hanhthu);
                fragmentTransaction.commit();

                TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.tabs);
                tabhost.getTabAt(1).select();*/
            }
        });

        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               /* File file = new File(CLocal.Path + "/" + lstView.getItemAtPosition(i).toString());
                if (file.delete() == true)
                    LoadListView();*/
                return false;
            }
        });

/*        Intent intent= getActivity().getIntent();
        String SoHoaDon=intent.getStringExtra("SoHoaDon");
        if(SoHoaDon.equals("")==false)
        {
            Toast.makeText(getActivity(),SoHoaDon, Toast.LENGTH_LONG).show();
        }*/

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
            publishProgress(ws.GetDSHoaDon(spnNam.getSelectedItem().toString(), spnKy.getSelectedItem().toString(), spnFromDot.getSelectedItem().toString(), spnToDot.getSelectedItem().toString(), CLocal.sharedPreferencesre.getString("MaNV", "")));
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                try {
                    /*FileOutputStream out = getContext().openFileOutput(spnNam.getSelectedItem().toString() + "_" + spnKy.getSelectedItem().toString() + "_" + spnFromDot.getSelectedItem().toString() + "_" + spnToDot.getSelectedItem().toString(), Context.MODE_PRIVATE);
                    OutputStreamWriter writer = new OutputStreamWriter(out);
                    writer.write(values[0]);
                    writer.close();*/

                    CLocal.jsonArray_HanhThu = new JSONArray(values[0]);
                    LoadListView( CLocal.jsonArray_HanhThu);
                } catch (Exception e) {
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

    private void LoadListView(JSONArray jsonArray) {
        try {
            ArrayList<CViewEntity> list = new ArrayList<CViewEntity>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CViewEntity entity = new CViewEntity();

                entity.setID(jsonObject.getString("SOHOADON"));
                entity.setName1(jsonObject.getString("DANHBA"));
                entity.setName2(jsonObject.getString("TONGCONG"));
                entity.setContent(jsonObject.getString("SO") + " " + jsonObject.getString("DUONG"));
                if (jsonObject.getString("NGAYGIAITRACH") != null || jsonObject.getString("NGAYGIAITRACH") != "")
                    entity.setBackgroundColor(CLocal.Color_DaThu);
                list.add(entity);
            }
            CViewAdapter adapter = new CViewAdapter(getActivity(), list);
            lstView.setAdapter(adapter);
        } catch (Exception e) {
        }
    }



}
