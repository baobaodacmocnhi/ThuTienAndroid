package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.constraintlayout.widget.ConstraintLayout;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityChild;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDownDataHanhThu extends AppCompatActivity {
    private Button btnDownload, btnShowMess;
    private Spinner spnFromDot, spnToDot, spnTo, spnNhanVien, spnNam, spnKy;
    private ArrayList<CViewParent> lstOriginal, lstDisplayed;
    private LinearLayout layoutTo, layoutNhanVien;
    private ConstraintLayout layoutMay;
    private ArrayList<String> spnID_To, spnName_To, spnID_NhanVien, spnName_NhanVien, spnName_Nam;
    private String selectedMaNV = "", LoaiDownData = "";
    private EditText edtTuMay, edtDenMay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_data_hanh_thu);

        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnShowMess = (Button) findViewById(R.id.btnShowMess);
        spnFromDot = (Spinner) findViewById(R.id.spnFromDot);
        spnToDot = (Spinner) findViewById(R.id.spnToDot);
        spnTo = (Spinner) findViewById(R.id.spnTo);
        spnNhanVien = (Spinner) findViewById(R.id.spnNhanVien);
        spnNam = (Spinner) findViewById(R.id.spnNam);
        spnKy = (Spinner) findViewById(R.id.spnKy);
        layoutTo = (LinearLayout) findViewById(R.id.layoutTo);
        layoutNhanVien = (LinearLayout) findViewById(R.id.layoutNhanVien);
        layoutMay = (ConstraintLayout) findViewById(R.id.layoutMay);
        edtTuMay = (EditText) findViewById(R.id.edtTuMay);
        edtDenMay = (EditText) findViewById(R.id.edtDenMay);

//        try {
//            LoaiDownData = getIntent().getStringExtra("LoaiDownData");
//        } catch (Exception ex) {
//        }

        try {
            if (CLocal.jsonNam != null && CLocal.jsonNam.length() > 0) {
                spnName_Nam = new ArrayList<>();
                for (int i = 0; i < CLocal.jsonNam.length(); i++) {
                    JSONObject jsonObject = CLocal.jsonNam.getJSONObject(i);
                    spnName_Nam.add(jsonObject.getString("NAM"));
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spnName_Nam);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnNam.setAdapter(adapter);
            //loaddata đợt
            if (CLocal.IDPhong.equals("1")) {
                //tân bình
                ArrayAdapter<CharSequence> adapterDot = ArrayAdapter.createFromResource(this, R.array.dotTB_array, android.R.layout.simple_spinner_item);
                adapterDot.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnFromDot.setAdapter(adapterDot);
                spnToDot.setAdapter(adapterDot);
            } else if (CLocal.IDPhong.equals("2")) {
                //tân phú
                ArrayAdapter<CharSequence> adapterDot = ArrayAdapter.createFromResource(this, R.array.dotTP_array, android.R.layout.simple_spinner_item);
                adapterDot.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnFromDot.setAdapter(adapterDot);
                spnToDot.setAdapter(adapterDot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //cast to an ArrayAdapter
        ArrayAdapter spnNamAdapter = (ArrayAdapter) spnNam.getAdapter();
        int spnNamPosition = spnNamAdapter.getPosition(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        //set the default according to value
        spnNam.setSelection(spnNamPosition);

        //cast to an ArrayAdapter
        ArrayAdapter spnKyAdapter = (ArrayAdapter) spnKy.getAdapter();
        int spnKyPosition = spnKyAdapter.getPosition(String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1));
        //set the default according to value
        spnKy.setSelection(spnKyPosition);

        if (CLocal.Doi == true) {
            layoutTo.setVisibility(View.VISIBLE);
            layoutMay.setVisibility(View.VISIBLE);
            try {
                if (CLocal.jsonTo != null && CLocal.jsonTo.length() > 0) {
                    spnID_To = new ArrayList<>();
                    spnName_To = new ArrayList<>();
                    for (int i = 0; i < CLocal.jsonTo.length(); i++) {
                        JSONObject jsonObject = CLocal.jsonTo.getJSONObject(i);
                        if (Boolean.parseBoolean(jsonObject.getString("HanhThu")) == true) {
                            spnID_To.add(jsonObject.getString("MaTo"));
                            spnName_To.add(jsonObject.getString("TenTo"));
                        }
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spnName_To);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnTo.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            layoutTo.setVisibility(View.GONE);
            if (CLocal.ToTruong == true || CLocal.DongNuoc == true) {
                layoutNhanVien.setVisibility(View.VISIBLE);
                layoutMay.setVisibility(View.VISIBLE);
                try {
                    if (CLocal.jsonNhanVien != null && CLocal.jsonNhanVien.length() > 0) {
                        spnID_NhanVien = new ArrayList<>();
                        spnName_NhanVien = new ArrayList<>();
                        spnID_NhanVien.add("0");
                        spnName_NhanVien.add("Tất Cả");
                        for (int i = 0; i < CLocal.jsonNhanVien.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonNhanVien.getJSONObject(i);
                            if (Boolean.parseBoolean(jsonObject.getString("HanhThu")) == true) {
                                spnID_NhanVien.add(jsonObject.getString("MaND"));
                                spnName_NhanVien.add(jsonObject.getString("HoTen"));
                            }
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spnName_NhanVien);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnNhanVien.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                layoutNhanVien.setVisibility(View.GONE);
        }

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CLocal.checkNetworkAvailable(ActivityDownDataHanhThu.this) == false) {
                    Toast.makeText(getApplicationContext(), "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });

        btnShowMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ActivityDownDataHanhThu.this);
                builderSingle.setIcon(R.mipmap.ic_launcher);
                builderSingle.setTitle("Tin nhắn đã nhận");
                builderSingle.setCancelable(false);

                ListView lstMessage = new ListView(getApplicationContext());

                //hiện thị k tô mầu
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ActivityDownDataHanhThu.this, android.R.layout.select_dialog_item);
//                try {
//                    if (CLocal.jsonMessage != null && CLocal.jsonMessage.length() > 0) {
//                        for (int i = 0; i < CLocal.jsonMessage.length(); i++) {
//                            JSONObject jsonObject = CLocal.jsonMessage.getJSONObject(i);
//                            arrayAdapter.add(jsonObject.getString("NgayNhan") + " - " + jsonObject.getString("Title") + " - " + jsonObject.getString("Content"));
//                        }
//                    }
//                } catch (Exception ex) {
//                }
//                lstMessage.setAdapter(arrayAdapter);

                //hiện thị tô mầu
                ArrayList<String> mylist = new ArrayList<String>();
                try {
                    if (CLocal.jsonMessage != null && CLocal.jsonMessage.length() > 0) {
                        for (int i = 0; i < CLocal.jsonMessage.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonMessage.getJSONObject(i);
                            mylist.add(jsonObject.getString("NgayNhan") + " - " + jsonObject.getString("Title") + " - " + jsonObject.getString("Content"));
                        }
                    }
                } catch (Exception ex) {
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ActivityDownDataHanhThu.this, android.R.layout.select_dialog_item, mylist) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        // Get the current item from ListView
                        View view = super.getView(position, convertView, parent);
                        if (position % 2 == 1) {
                            // Set a background color for ListView regular row/item
                            view.setBackgroundColor(Color.TRANSPARENT);
                        } else {
                            // Set the background color for alternate row/item
                            view.setBackgroundColor(getResources().getColor(R.color.colorListView));
                        }
                        return view;
                    }
                };
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDownDataHanhThu.this);
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

        spnTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (CLocal.jsonNhanVien != null && CLocal.jsonNhanVien.length() > 0) {
                        spnID_NhanVien = new ArrayList<>();
                        spnName_NhanVien = new ArrayList<>();
                        spnID_NhanVien.add("0");
                        spnName_NhanVien.add("Tất Cả");
                        for (int i = 0; i < CLocal.jsonNhanVien.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonNhanVien.getJSONObject(i);
                            if (jsonObject.getString("MaTo") == spnID_To.get(position) && Boolean.parseBoolean(jsonObject.getString("HanhThu")) == true) {
                                spnID_NhanVien.add(jsonObject.getString("MaND"));
                                spnName_NhanVien.add(jsonObject.getString("HoTen"));
                            }
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spnName_NhanVien);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnNhanVien.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnNhanVien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMaNV = spnID_NhanVien.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public class MyAsyncTask extends AsyncTask<Void, Void, String[]> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityDownDataHanhThu.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            try {
                if (CLocal.Doi == false && CLocal.ToTruong == false && CLocal.DongNuoc == false)
                    selectedMaNV = CLocal.MaNV;
                if (selectedMaNV.equals("0")) {
                    CLocal.jsonHanhThu = new JSONArray();
                    for (int i = 1; i < spnID_NhanVien.size(); i++) {
                        JSONArray jsonResult = new JSONArray(ws.getDSHoaDonTon_May(String.valueOf(spnID_NhanVien.get(i)), spnNam.getSelectedItem().toString(), spnKy.getSelectedItem().toString(), spnFromDot.getSelectedItem().toString(), spnToDot.getSelectedItem().toString(), edtTuMay.getText().toString(), edtDenMay.getText().toString()));
                        for (int j = 0; j < jsonResult.length(); j++) {
                            JSONObject jsonObject = jsonResult.getJSONObject(j);
                            CLocal.jsonHanhThu.put(jsonObject);
                        }
                    }
                } else {
                    CLocal.jsonHanhThu = new JSONArray(ws.getDSHoaDonTon_NhanVien(selectedMaNV, spnNam.getSelectedItem().toString(), spnKy.getSelectedItem().toString(), spnFromDot.getSelectedItem().toString(), spnToDot.getSelectedItem().toString()));
                }

                if (CLocal.jsonHanhThu != null) {
                    //khởi tạo ArrayList CEntityParent
                    CLocal.listHanhThu = new ArrayList<CEntityParent>();
                    for (int i = 0; i < CLocal.jsonHanhThu.length(); i++) {
                        JSONObject jsonObject = CLocal.jsonHanhThu.getJSONObject(i);
                        boolean flagExist = false;
                        for (int j = 0; j < CLocal.listHanhThu.size(); j++)
                            if (CLocal.listHanhThu.get(j).getID().equals(jsonObject.getString("DanhBo")) == true)
                                flagExist = true;
                        //không có mới thêm
                        if (flagExist == false) {
                            CEntityParent enParent = new CEntityParent();
                            ///thiết lập khởi tạo 1 lần đầu để sort
                            if (jsonObject.has("ModifyDate") == false)
                                enParent.setModifyDate(CLocal.DateFormat.format(new Date()));
                            else
                                enParent.setModifyDate(jsonObject.getString("ModifyDate"));
                            enParent.setID(jsonObject.getString("DanhBo"));

                            String strMLT = new StringBuffer(jsonObject.getString("MLT")).insert(4, " ").insert(2, " ").toString();
                            enParent.setMLT(strMLT);

                            String strDanhBo = new StringBuffer(jsonObject.getString("DanhBo")).insert(7, " ").insert(4, " ").toString();
                            enParent.setDanhBo(strDanhBo);

                            enParent.setHoTen(jsonObject.getString("HoTen"));
                            enParent.setDiaChi(jsonObject.getString("DiaChi"));
                            if (jsonObject.has("DiaChiDHN") == true)
                                enParent.setDiaChiDHN(jsonObject.getString("DiaChiDHN").replace("null", ""));
                            if (jsonObject.has("DongA") == true)
                                enParent.setDongA(Boolean.parseBoolean(jsonObject.getString("DongA")));
                            if (jsonObject.has("CuaHangThuHo1") == true)
                                enParent.setCuaHangThuHo1(jsonObject.getString("CuaHangThuHo1").replace("null", ""));
                            if (jsonObject.has("CuaHangThuHo2") == true)
                                enParent.setCuaHangThuHo2(jsonObject.getString("CuaHangThuHo2").replace("null", ""));

                            //khởi tạo ArrayList CEntityChild
                            ArrayList<CEntityChild> listChild = new ArrayList<CEntityChild>();
                            if (CLocal.jsonHanhThu != null && CLocal.jsonHanhThu.length() > 0)
                                for (int k = 0; k < CLocal.jsonHanhThu.length(); k++) {
                                    JSONObject jsonObjectChild = CLocal.jsonHanhThu.getJSONObject(k);
                                    if (jsonObjectChild.getString("DanhBo").equals(enParent.getID()) == true) {
                                        CEntityChild enChild = new CEntityChild();
                                        enChild.setModifyDate(enParent.getModifyDate());
                                        enChild.setMaHD(jsonObjectChild.getString("MaHD"));
                                        enChild.setKy(jsonObjectChild.getString("Ky"));
                                        enChild.setGiaBan(jsonObjectChild.getString("GiaBan"));
                                        enChild.setThueGTGT(jsonObjectChild.getString("ThueGTGT"));
                                        enChild.setPhiBVMT(jsonObjectChild.getString("PhiBVMT"));
                                        if (jsonObjectChild.has("PhiBVMT_Thue") == true)
                                            enChild.setPhiBVMT_Thue(jsonObjectChild.getString("PhiBVMT_Thue"));
                                        enChild.setTongCong(jsonObjectChild.getString("TongCong"));
                                        if (jsonObjectChild.has("ChiTietTienNuoc") == true)
                                            enChild.setChiTietTienNuoc(jsonObjectChild.getString("ChiTietTienNuoc"));
                                        enChild.setGiaBieu(jsonObjectChild.getString("GiaBieu"));
                                        enChild.setDinhMuc(jsonObjectChild.getString("DinhMuc").replace("null", ""));
                                        if (jsonObjectChild.has("Code") == true)
                                            enChild.setCode(jsonObjectChild.getString("Code"));
                                        if (jsonObjectChild.has("CoDH") == true)
                                            enChild.setCo(jsonObjectChild.getString("CoDH"));
                                        enChild.setCSC(jsonObjectChild.getString("CSC"));
                                        enChild.setCSM(jsonObjectChild.getString("CSM"));
                                        enChild.setTieuThu(jsonObjectChild.getString("TieuThu"));
                                        enChild.setTuNgay(jsonObjectChild.getString("TuNgay"));
                                        enChild.setDenNgay(jsonObjectChild.getString("DenNgay"));
                                        enChild.setGiaiTrach(Boolean.parseBoolean(jsonObjectChild.getString("GiaiTrach")));
                                        enChild.setTamThu(Boolean.parseBoolean(jsonObjectChild.getString("TamThu")));
                                        enChild.setThuHo(Boolean.parseBoolean(jsonObjectChild.getString("ThuHo")));
                                        if (jsonObjectChild.has("LenhHuy") == true)
                                            enChild.setLenhHuy(Boolean.parseBoolean(jsonObjectChild.getString("LenhHuy")));
                                        if (jsonObjectChild.has("LenhHuyCat") == true)
                                            enChild.setLenhHuyCat(Boolean.parseBoolean(jsonObjectChild.getString("LenhHuyCat")));
                                        if (jsonObjectChild.has("DangNgan_DienThoai") == true)
                                            enChild.setDangNgan_DienThoai(Boolean.parseBoolean(jsonObjectChild.getString("DangNgan_DienThoai")));
                                        if (jsonObjectChild.has("MaNV_DangNgan") == true)
                                            enChild.setMaNV_DangNgan(jsonObjectChild.getString("MaNV_DangNgan"));
                                        if (jsonObjectChild.has("NgayGiaiTrach") == true)
                                            if (jsonObjectChild.getString("NgayGiaiTrach").replace("null", "").equals("") == false)
                                                enChild.setNgayGiaiTrach(CLocal.convertTimestampToDate(Long.parseLong(jsonObjectChild.getString("NgayGiaiTrach").replace("null", "").replace("/Date(", "").replace(")/", ""))));
                                        if (jsonObjectChild.has("XoaDangNgan_Ngay_DienThoai") == true)
                                            if (jsonObjectChild.getString("XoaDangNgan_Ngay_DienThoai").replace("null", "").equals("") == false)
                                                enChild.setXoaDangNgan_Ngay_DienThoai(CLocal.convertTimestampToDate(Long.parseLong(jsonObjectChild.getString("XoaDangNgan_Ngay_DienThoai").replace("null", "").replace("/Date(", "").replace(")/", ""))));
                                        if (jsonObjectChild.has("InPhieuBao_Ngay") == true)
                                            if (jsonObjectChild.getString("InPhieuBao_Ngay").replace("null", "").equals("") == false)
                                                enChild.setInPhieuBao_Ngay(CLocal.convertTimestampToDate(Long.parseLong(jsonObjectChild.getString("InPhieuBao_Ngay").replace("null", "").replace("/Date(", "").replace(")/", ""))));
                                        if (jsonObjectChild.has("InPhieuBao2_Ngay") == true)
                                            if (jsonObjectChild.getString("InPhieuBao2_Ngay").replace("null", "").equals("") == false)
                                                enChild.setInPhieuBao2_Ngay(CLocal.convertTimestampToDate(Long.parseLong(jsonObjectChild.getString("InPhieuBao2_Ngay").replace("null", "").replace("/Date(", "").replace(")/", ""))));
                                        if (jsonObjectChild.has("InPhieuBao2_NgayHen") == true)
                                            if (jsonObjectChild.getString("InPhieuBao2_NgayHen").replace("null", "").equals("") == false)
                                                enChild.setInPhieuBao2_NgayHen(CLocal.convertTimestampToDate(Long.parseLong(jsonObjectChild.getString("InPhieuBao2_NgayHen").replace("null", "").replace("/Date(", "").replace(")/", ""))));
                                        if (jsonObjectChild.has("TBDongNuoc_Ngay") == true)
                                            if (jsonObjectChild.getString("TBDongNuoc_Ngay").replace("null", "").equals("") == false) {
                                                enChild.setTBDongNuoc_Ngay(CLocal.convertTimestampToDate(Long.parseLong(jsonObjectChild.getString("TBDongNuoc_Ngay").replace("null", "").replace("/Date(", "").replace(")/", ""))));
                                                enChild.setTBDongNuoc(true);
                                            } else
                                                enChild.setTBDongNuoc(false);
                                        if (jsonObjectChild.has("TBDongNuoc_NgayHen") == true)
                                            if (jsonObjectChild.getString("TBDongNuoc_NgayHen").replace("null", "").equals("") == false)
                                                enChild.setTBDongNuoc_NgayHen(CLocal.convertTimestampToDate(Long.parseLong(jsonObjectChild.getString("TBDongNuoc_NgayHen").replace("null", "").replace("/Date(", "").replace(")/", ""))));
                                        if (jsonObjectChild.has("PhiMoNuoc") == true)
                                            enChild.setPhiMoNuoc(jsonObjectChild.getString("PhiMoNuoc"));
                                        if (jsonObjectChild.has("PhiMoNuocThuHo") == true)
                                            enChild.setPhiMoNuocThuHo(jsonObjectChild.getString("PhiMoNuocThuHo"));
                                        if (jsonObjectChild.has("DCHD") == true) {
                                            enChild.setDCHD(Boolean.parseBoolean(jsonObjectChild.getString("DCHD")));
                                            if (enChild.isDCHD() == true) {
                                                enParent.setDCHD(enChild.isDCHD());
                                                enChild.setTienDuTruocDCHD(Integer.parseInt(jsonObjectChild.getString("TienDuTruoc_DCHD")));
                                            }
                                        }
                                        if (jsonObjectChild.has("ChoDCHD") == true)
                                            enChild.setChoDCHD(Boolean.parseBoolean(jsonObjectChild.getString("ChoDCHD")));

                                        //update parent
                                        if (jsonObjectChild.has("MaKQDN") == true)
                                            enParent.setMaKQDN(jsonObjectChild.getString("MaKQDN").replace("null", ""));
                                        if (jsonObjectChild.has("DongPhi") == true)
                                            enParent.setDongPhi(Boolean.parseBoolean(jsonObjectChild.getString("DongPhi")));
                                        listChild.add(enChild);
                                    }
                                }
//                            //update TinhTrang
//                            int ThuHo = 0, TamThu = 0, GiaiTrach = 0, DangNgan_DienThoai = 0, TBDongNuoc = 0, LenhHuy = 0;
//                            for (CEntityChild item : listChild) {
//                                if (item.getGiaiTrach() == true)
//                                    GiaiTrach++;
//                                else if (item.getTamThu() == true)
//                                    TamThu++;
//                                else if (item.getThuHo() == true)
//                                    ThuHo++;
//                                else if (item.getLenhHuy() == true)
//                                    LenhHuy++;
//                                else if (item.getTBDongNuoc() == true)
//                                    TBDongNuoc++;
//
//                                if (item.getDangNgan_DienThoai() == true)
//                                    DangNgan_DienThoai++;
//                            }
//
//                            if (GiaiTrach == listChild.size()) {
//                                enParent.setGiaiTrach(true);
//                                enParent.setTinhTrang("Giải Trách");
//                            } else if (TamThu == listChild.size()) {
//                                enParent.setTamThu(true);
//                                enParent.setTinhTrang("Tạm Thu");
//                            } else if (ThuHo == listChild.size()) {
//                                enParent.setThuHo(true);
//                                enParent.setTinhTrang("Thu Hộ");
//                            } else if (LenhHuy == listChild.size()) {
//                                enParent.setLenhHuy(true);
//                            } else if (TBDongNuoc == listChild.size()) {
//                                enParent.setTBDongNuoc(true);
//                            }
//
//                            if (DangNgan_DienThoai == listChild.size()) {
//                                enParent.setDangNgan_DienThoai(true);
//                                enParent.setTinhTrang("Đã Thu");
//                            }

                            enParent.setLstHoaDon(listChild);
                            enParent = CLocal.updateTinhTrangParent(enParent);
                            CLocal.listHanhThu.add(enParent);
                        }
                    }
                    SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                    editor.putString("jsonHanhThu", new Gson().toJsonTree(CLocal.listHanhThu).getAsJsonArray().toString());
                    editor.commit();
                }
                return new String[]{"true", ""};
            } catch (Exception ex) {
                return new String[]{"false", ex.getMessage()};
            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (Boolean.parseBoolean(strings[0]) == true) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                CLocal.showPopupMessage(ActivityDownDataHanhThu.this, strings[1], "center");
            }
        }
    }


}
