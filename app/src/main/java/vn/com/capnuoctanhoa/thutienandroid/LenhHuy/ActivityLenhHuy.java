package vn.com.capnuoctanhoa.thutienandroid.LenhHuy;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityDongTien;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityLenhHuy extends AppCompatActivity {
    private Button btnTimKiem, btnCapNhat, btnShowMess;
    private Spinner spnLoai;
    private CheckBox chkCat;
    private EditText edtMa, edtTinhTrang;
    private ListView lstView;
    private ArrayList<String> arrayList;
    private ArrayList<CLenhHuy> lstLenhHuy;
    private ArrayAdapter<String> arrayAdapter;
    private String lstMaHD = "",selectedMaHDs = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lenh_huy);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spnLoai = (Spinner) findViewById(R.id.spnLoai);
        chkCat = (CheckBox) findViewById(R.id.chkCat);
        edtMa = (EditText) findViewById(R.id.edtMa);
        edtTinhTrang = (EditText) findViewById(R.id.edtTinhTrang);
        lstView = (ListView) findViewById(R.id.lstView);
        btnTimKiem = (Button) findViewById(R.id.btnTimKiem);
        btnCapNhat = (Button) findViewById(R.id.btnCapNhat);
        btnShowMess = (Button) findViewById(R.id.btnShowMess);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Cắt Tạm");
        arrayList.add("Cắt Hủy");
        arrayList.add("Danh Bộ");
        arrayList.add("Tất Cả");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLoai.setAdapter(arrayAdapter);

        lstView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView v = (CheckedTextView) view;
                boolean currentCheck = v.isChecked();
                CLenhHuy lenhhuy = lstLenhHuy.get(position);
                lenhhuy.setSelected(!currentCheck);

                edtTinhTrang.setText(lenhhuy.getTinhTrang());
                chkCat.setChecked(lenhhuy.isCat());
            }
        });


        btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CLocal.checkNetworkAvailable(getApplicationContext()) == false) {
                    Toast.makeText(getApplicationContext(), "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                ActivityLenhHuy.MyAsyncTask myAsyncTask = new ActivityLenhHuy.MyAsyncTask();
                myAsyncTask.execute("TimKiem");
                CLocal.hideKeyboard(ActivityLenhHuy.this);
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CLocal.checkNetworkAvailable(getApplicationContext()) == false) {
                    Toast.makeText(getApplicationContext(), "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                SparseBooleanArray sp = lstView.getCheckedItemPositions();
                selectedMaHDs = "";
                for (int i = 0; i < sp.size(); i++) {
                    if (sp.valueAt(i) == true) {
                        CLenhHuy lenhhuy = lstLenhHuy.get(sp.keyAt(i));
                        if (selectedMaHDs.equals("") == true)
                            selectedMaHDs = lenhhuy.MaHD;
                        else
                            selectedMaHDs += "," + lenhhuy.MaHD;
                    }
                }
                ActivityLenhHuy.MyAsyncTask myAsyncTask = new ActivityLenhHuy.MyAsyncTask();
                myAsyncTask.execute("CapNhat");
                CLocal.hideKeyboard(ActivityLenhHuy.this);
            }
        });

        btnShowMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ActivityLenhHuy.this);
                builderSingle.setIcon(R.mipmap.ic_launcher);
                builderSingle.setTitle("Tin nhắn đã nhận");
                builderSingle.setCancelable(false);

                ListView lstMessage = new ListView(getApplicationContext());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ActivityLenhHuy.this, android.R.layout.select_dialog_singlechoice);

                try {
                    if (CLocal.jsonMessage != null && CLocal.jsonMessage.length() > 0) {
                        int stt = 0;
                        for (int i = 0; i < CLocal.jsonMessage.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonMessage.getJSONObject(i);
                            arrayAdapter.add(jsonObject.getString("NgayNhan") + " - " + jsonObject.getString("Title") + " - " + jsonObject.getString("Content"));
                        }
                    }
                } catch (Exception ex) {
                }

                lstMessage.setAdapter(arrayAdapter);
                builderSingle.setView(lstMessage);

                builderSingle.setNegativeButton(
                        "Thoát",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setPositiveButton(
                        "Xóa Tất Cả",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLenhHuy.this);
                                builder.setMessage("Bạn có chắc chắn xóa?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                CLocal.jsonMessage = new JSONArray();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });

                //hàm này khi click row sẽ bị ẩn
                /*builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(ActivityDanhSachHanhThu3.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });*/

                final Dialog dialog = builderSingle.create();
                builderSingle.show();
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

    public class CLenhHuy {
        private String MaHD;
        private String Ky;
        private String TinhTrang;
        private boolean Cat;
        private boolean Selected;

        public CLenhHuy() {
            MaHD = "";
            Ky = "";
            TinhTrang = "";
            Cat = false;
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

        public String getTinhTrang() {
            return TinhTrang;
        }

        public void setTinhTrang(String tinhTrang) {
            TinhTrang = tinhTrang;
        }

        public boolean isCat() {
            return Cat;
        }

        public void setCat(boolean cat) {
            Cat = cat;
        }

        public boolean isSelected() {
            return Selected;
        }

        public void setSelected(boolean selected) {
            Selected = selected;
        }

    }

    private void fillListView(String value) {
        try {
            JSONArray result = new JSONArray(value);
            lstLenhHuy=new ArrayList<CLenhHuy>();
            arrayList = new ArrayList<String>();

            for (int i = 0; i < result.length(); i++) {
                JSONObject jsonObject = result.getJSONObject(i);

                CLenhHuy entity = new CLenhHuy();
                entity.setMaHD(jsonObject.getString("MaHD"));
                entity.setKy(jsonObject.getString("Ky"));
                entity.setTinhTrang(jsonObject.getString("TinhTrang"));
                entity.setCat(Boolean.parseBoolean(jsonObject.getString("Cat")));
                lstLenhHuy.add(entity);

                if (lstMaHD.isEmpty() == true)
                    lstMaHD = jsonObject.getString("MaHD");
                else
                    lstMaHD += "," + jsonObject.getString("MaHD");
                if (Boolean.parseBoolean(jsonObject.getString("Cat")) == true)
                    arrayList.add(jsonObject.getString("DanhBo") + " ; " +jsonObject.getString("DiaChi") + " ; " +jsonObject.getString("Ky") + " ; " + jsonObject.getString("TinhTrang") + " ; Đã Cắt");
                else
                    arrayList.add(jsonObject.getString("DanhBo") + " ; " +jsonObject.getString("DiaChi") + " ; " +jsonObject.getString("Ky") + " ; " + jsonObject.getString("TinhTrang") + " ; Chưa Cắt");
            }

            arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_checked, arrayList);
            lstView.setAdapter(arrayAdapter);
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
            progressDialog = new ProgressDialog(ActivityLenhHuy.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... voids) {
            try {
                String result = "";
                switch (voids[0]) {
                    case "TimKiem":
                        result = ws.GetDSHoaDon_LenhHuy(spnLoai.getSelectedItem().toString(), edtMa.getText().toString().replace("-", "").replace(" ", ""));
                        if (result.equals("[]") == true)
                            return "Không có lệnh hủy";
                        else
                            publishProgress(new String[]{"TimKiem", result});
                        break;
                    case "CapNhat":
                        if (selectedMaHDs.equals("") == true)
                            return "CHƯA CHỌN HÓA ĐƠN";
                        result = ws.sua_LenhHuy(selectedMaHDs,String.valueOf(chkCat.isChecked()),edtTinhTrang.getText().toString(),CLocal.MaNV);
                        if (Boolean.parseBoolean(result) == true)
                            return "THÀNH CÔNG";
                        else
                            return "THẤT BẠI";
                }
                return "false";
            } catch (Exception ex) {
                return "false";
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                switch (values[0]) {
                    case "TimKiem":
                        try {
                            fillListView(values[1]);
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "CapNhat":

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
            if (s.equals("false") == false)
                CLocal.showPopupMessage(ActivityLenhHuy.this, s);
        }
    }

}

