package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDongTien extends AppCompatActivity {
    private TextView txtTongCong;
    private EditText edtMaDN;
    private Button btnDongTien;
    private ListView lstView;
    private ArrayList<CHoaDon> lstHoaDon;
    private long TongCong;
    private String MaHDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dong_tien);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TongCong = 0;
        MaHDs = "";

        txtTongCong = (TextView) findViewById(R.id.txtTongCong);

        edtMaDN = (EditText) findViewById(R.id.edtMaDN);

        btnDongTien = (Button) findViewById(R.id.btnDongTien);

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
                StringBuilder sb = new StringBuilder();
                MaHDs = "";
                for (int i = 0; i < sp.size(); i++) {
//                    int key = sp.keyAt(i);
//                    boolean value = sp.get(key);
                    if (sp.valueAt(i) == true) {
                        CHoaDon hoadon = lstHoaDon.get(sp.keyAt(i));
                        if (MaHDs.equals("") == true)
                            MaHDs = hoadon.MaHD;
                        else
                            MaHDs += "," + hoadon.MaHD;
                    }
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute("DongTien");
            }
        });

        try {
            String MaDN = getIntent().getStringExtra("MaDN");
            if (MaDN.equals("") == false) {
                fillDongNuoc(MaDN);
            }
        } catch (Exception ex) {
        }
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
            ArrayList<String> arrayList = new ArrayList<String>();

            for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                JSONObject jsonObject = CLocal.jsonDongNuoc.getJSONObject(i);
                if (jsonObject.getString("ID").equals(MaDN) == true) {
                    edtMaDN.setText(MaDN);

                    if (CLocal.jsonDongNuocChild != null && CLocal.jsonDongNuocChild.length() > 0)
                        for (int j = 0; j < CLocal.jsonDongNuocChild.length(); j++) {
                            JSONObject jsonObjectChild = CLocal.jsonDongNuocChild.getJSONObject(j);

                            if (jsonObjectChild.getString("ID").equals(MaDN) == true) {
                                CHoaDon entity = new CHoaDon();
                                entity.setMaHD(jsonObjectChild.getString("MaHD"));
                                entity.setKy(jsonObjectChild.getString("Ky"));
                                entity.setTongCong(jsonObjectChild.getString("TongCong"));
                                lstHoaDon.add(entity);

                                arrayList.add(jsonObjectChild.getString("Ky") + " : " + CLocal.formatMoney(jsonObjectChild.getString("TongCong"), "đ"));
                            }
                        }
                    break;
                }
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, arrayList);
            lstView.setAdapter(arrayAdapter);
            txtTongCong.setText(CLocal.formatMoney("0", "đ"));

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
            switch (strings[0]) {
                case "DongTien":
                    if(MaHDs.equals("")==true)
                        return "CHƯA CHỌN HÓA ĐƠN";
                    String result = ws.dangNganDongNuoc(CLocal.sharedPreferencesre.getString("MaNV", ""), MaHDs);
                    if (Boolean.parseBoolean(result) == true)
                        return "THÀNH CÔNG";
                    else
                        return "THẤT BẠI";
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            CLocal.showPopupMessage(ActivityDongTien.this, s);
        }

    }

}
