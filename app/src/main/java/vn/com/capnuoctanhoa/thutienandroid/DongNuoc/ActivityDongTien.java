package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDongTien extends AppCompatActivity {
    private TextView txtTongCong;
    private EditText edtMaDN;
    private Button btnDongTien,btnIn;
    private ListView lstView;
    private ArrayList<CHoaDon> lstHoaDon;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private long TongCong = 0;
    private String selectedMaHDs = "";
    private String lstMaHD = "";
    private String danhBo = "";
    private JSONArray jsonArrayHoaDonTon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dong_tien);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTongCong = (TextView) findViewById(R.id.txtTongCong);
        edtMaDN = (EditText) findViewById(R.id.edtMaDN);
        btnDongTien = (Button) findViewById(R.id.btnDongTien);
        btnIn = (Button) findViewById(R.id.btnIn);
        lstView = (ListView) findViewById(R.id.lstView);

        // CHOICE_MODE_NONE: Không cho phép lựa chọn (Mặc định).
        // ( listView.setItemChecked(..) không làm việc với CHOICE_MODE_NONE).
        // CHOICE_MODE_SINGLE: Cho phép một lựa chọn.
        // CHOICE_MODE_MULTIPLE: Cho phép nhiều lựa chọn.
        // CHOICE_MODE_MULTIPLE_MODAL: Cho phép nhiều lựa chọn trên Modal Selection Mode.
        lstView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView v = (CheckedTextView) view;
                boolean currentCheck = v.isChecked();
                CHoaDon hoadon = lstHoaDon.get(position);
                hoadon.setSelected(!currentCheck);
                if (currentCheck == true)
                    TongCong += Long.parseLong(hoadon.getTongCong());
                else
                    TongCong -= Long.parseLong(hoadon.getTongCong());
                txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
            }
        });

        btnDongTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray sp = lstView.getCheckedItemPositions();
//                StringBuilder sb = new StringBuilder();
                selectedMaHDs = "";
                for (int i = 0; i < sp.size(); i++) {
//                    int key = sp.keyAt(i);
//                    boolean value = sp.get(key);
                    if (sp.valueAt(i) == true) {
                        CHoaDon hoadon = lstHoaDon.get(sp.keyAt(i));
                        if (selectedMaHDs.equals("") == true)
                            selectedMaHDs = hoadon.MaHD;
                        else
                            selectedMaHDs += "," + hoadon.MaHD;
                    }
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute("DongTien");
            }
        });

        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        try {
            String MaDN = getIntent().getStringExtra("MaDN");
            if (MaDN.equals("") == false) {
                fillDongNuoc(MaDN);
            }
        } catch (Exception ex) {
        }

        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("GetHoaDonTon");

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

    public class CHoaDon {
        private String MaHD;
        private String Ky;
        private String TongCong;
        private boolean Selected;

        public CHoaDon() {
            MaHD = "";
            Ky = "";
            TongCong = "";
            Selected = false;
        }

        public String getMaHD() {
            return MaHD;
        }

        public void setMaHD(String maHD) {
            MaHD = maHD;
        }

        public String getKy() {
            return Ky;
        }

        public void setKy(String ky) {
            Ky = ky;
        }

        public String getTongCong() {
            return TongCong;
        }

        public void setTongCong(String tongCong) {
            TongCong = tongCong;
        }

        public boolean isSelected() {
            return Selected;
        }

        public void setSelected(boolean selected) {
            Selected = selected;
        }

        @Override
        public String toString() {
            return Ky + " : " + CLocal.formatMoney(TongCong, "đ");
        }
    }

    private void fillDongNuoc(String MaDN) {
        try {
            lstHoaDon = new ArrayList<CHoaDon>();
            arrayList = new ArrayList<String>();

            for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                JSONObject jsonObject = CLocal.jsonDongNuoc.getJSONObject(i);
                if (jsonObject.getString("ID").equals(MaDN) == true) {
                    edtMaDN.setText(MaDN);
                    danhBo = jsonObject.getString("DanhBo");
                    if (CLocal.jsonDongNuocChild != null && CLocal.jsonDongNuocChild.length() > 0)
                        for (int j = 0; j < CLocal.jsonDongNuocChild.length(); j++) {
                            JSONObject jsonObjectChild = CLocal.jsonDongNuocChild.getJSONObject(j);

                            if (jsonObjectChild.getString("ID").equals(MaDN) == true) {
                                CHoaDon entity = new CHoaDon();
                                entity.setMaHD(jsonObjectChild.getString("MaHD"));
                                entity.setKy(jsonObjectChild.getString("Ky"));
                                entity.setTongCong(jsonObjectChild.getString("TongCong"));
                                lstHoaDon.add(entity);

                                if (lstMaHD.isEmpty() == true)
                                    lstMaHD = entity.getMaHD();
                                else
                                    lstMaHD += "," + entity.getMaHD();

                                arrayList.add(jsonObjectChild.getString("Ky") + " : " + CLocal.formatMoney(jsonObjectChild.getString("TongCong"), "đ"));
                            }
                        }
                    break;
                }
            }

            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, arrayList);
            lstView.setAdapter(arrayAdapter);
            txtTongCong.setText(CLocal.formatMoney("0", "đ"));

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void fillHoaDonTon() {
        if (jsonArrayHoaDonTon != null)
            try {
                for (int k = 0; k < jsonArrayHoaDonTon.length(); k++) {
                    JSONObject jsonObjectHoaDonTon = jsonArrayHoaDonTon.getJSONObject(k);
                    CHoaDon entity = new CHoaDon();
                    entity.setMaHD(jsonObjectHoaDonTon.getString("MaHD"));
                    entity.setKy(jsonObjectHoaDonTon.getString("Ky"));
                    entity.setTongCong(jsonObjectHoaDonTon.getString("TongCong"));
                    lstHoaDon.add(entity);

                    arrayList.add(jsonObjectHoaDonTon.getString("Ky") + " : " + CLocal.formatMoney(jsonObjectHoaDonTon.getString("TongCong"), "đ"));
                }
                arrayAdapter.notifyDataSetChanged();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
            }
    }

    public class MyAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityDongTien.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            switch (strings[0]) {
                case "DongTien":
                    if (selectedMaHDs.equals("") == true)
                        return "CHƯA CHỌN HÓA ĐƠN";
                    result = ws.dangNganDongNuoc(CLocal.MaNV, selectedMaHDs);
                    if (Boolean.parseBoolean(result) == true)
                        return "THÀNH CÔNG";
                    else
                        return "THẤT BẠI";
                case "GetHoaDonTon":
                    result = ws.getDSHoaDonTon_DongNuoc(danhBo, lstMaHD);
                    publishProgress(new String[]{"GetHoaDonTon", result});
                    return "false";
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                switch (values[0]) {
                    case "GetHoaDonTon":
                        try {
                            jsonArrayHoaDonTon = new JSONArray(values[1]);
                            fillHoaDonTon();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
            if(s.equals("false")==false)
            CLocal.showPopupMessage(ActivityDongTien.this, s);
        }

    }

}
