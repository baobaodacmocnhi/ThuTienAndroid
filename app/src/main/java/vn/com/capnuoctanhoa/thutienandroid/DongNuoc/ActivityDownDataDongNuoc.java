package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityChild;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDownDataDongNuoc extends AppCompatActivity {
    private EditText edtFromDate, edtToDate;
    private DatePickerDialog datePickerDialog;
    private Button btnDownload, btnShowMess;
    private Spinner spnTo, spnNhanVien;
    private LinearLayout layoutTo, layoutNhanVien;
    private ArrayList<String> spnID_To, spnName_To, spnID_NhanVien, spnName_NhanVien;
    private String selectedMaNV = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_data_dong_nuoc);

        edtFromDate = (EditText) findViewById(R.id.edtFromDate);
        edtToDate = (EditText) findViewById(R.id.edtToDate);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnShowMess = (Button) findViewById(R.id.btnShowMess);
        spnTo = (Spinner) findViewById(R.id.spnTo);
        spnNhanVien = (Spinner) findViewById(R.id.spnNhanVien);
        layoutTo = (LinearLayout) findViewById(R.id.layoutTo);
        layoutNhanVien = (LinearLayout) findViewById(R.id.layoutNhanVien);

        if (CLocal.Doi == true) {
            layoutTo.setVisibility(View.VISIBLE);
            try {
                if (CLocal.jsonTo != null && CLocal.jsonTo.length() > 0) {
                    spnID_To = new ArrayList<>();
                    spnName_To = new ArrayList<>();
                    for (int i = 0; i < CLocal.jsonTo.length(); i++) {
                        JSONObject jsonObject = CLocal.jsonTo.getJSONObject(i);
                        if (Boolean.parseBoolean(jsonObject.getString("DongNuoc")) == true) {
                            spnID_To.add(jsonObject.getString("MaTo"));
                            spnName_To.add(jsonObject.getString("TenTo"));
                        }
//                        spnID_To.add(jsonObject.getString("MaTo"));
//                        spnName_To.add(jsonObject.getString("TenTo"));
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
            if (CLocal.ToTruong == true) {
                layoutNhanVien.setVisibility(View.VISIBLE);
                try {
                    if (CLocal.jsonNhanVien != null && CLocal.jsonNhanVien.length() > 0) {
                        spnID_NhanVien = new ArrayList<>();
                        spnName_NhanVien = new ArrayList<>();
                        spnID_NhanVien.add("0");
                        spnName_NhanVien.add("Tất Cả");
                        for (int i = 0; i < CLocal.jsonNhanVien.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonNhanVien.getJSONObject(i);
                            if (Boolean.parseBoolean(jsonObject.getString("DongNuoc")) == true) {
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
                datePickerDialog = new DatePickerDialog(ActivityDownDataDongNuoc.this,
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
                datePickerDialog = new DatePickerDialog(ActivityDownDataDongNuoc.this,
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

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.checkNetworkAvailable(ActivityDownDataDongNuoc.this) == false) {
                    Toast.makeText(getApplicationContext(), "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
                CLocal.hideKeyboard(ActivityDownDataDongNuoc.this);
            }
        });

        btnShowMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ActivityDownDataDongNuoc.this);
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

                //hiện thị tô màu
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
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ActivityDownDataDongNuoc.this, android.R.layout.select_dialog_item, mylist) {
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDownDataDongNuoc.this);
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
                            if (jsonObject.getString("MaTo") == spnID_To.get(position) && Boolean.parseBoolean(jsonObject.getString("DongNuoc")) == true) {
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

    public class MyAsyncTask extends AsyncTask<Void, String, String[]> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityDownDataDongNuoc.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            try {
                if (CLocal.Doi == false && CLocal.ToTruong == false)
                    selectedMaNV = CLocal.MaNV;
                if (selectedMaNV.equals("0")) {
                    CLocal.jsonDongNuoc = new JSONArray();
                    CLocal.jsonDongNuocChild = new JSONArray();
                    for (int i = 1; i < spnID_NhanVien.size(); i++) {
                        JSONArray jsonResult = new JSONArray(ws.getDSDongNuoc(String.valueOf(spnID_NhanVien.get(i))));
//                        JSONArray jsonResult = new JSONArray(ws.getDSDongNuoc(String.valueOf(spnID_NhanVien.get(i)), edtFromDate.getText().toString(), edtToDate.getText().toString()));
                        for (int j = 0; j < jsonResult.length(); j++) {
                            JSONObject jsonObject = jsonResult.getJSONObject(j);
                            CLocal.jsonDongNuoc.put(jsonObject);
                        }
                        JSONArray jsonResult_Child = new JSONArray(ws.getDSCTDongNuoc(String.valueOf(spnID_NhanVien.get(i))));
//                        JSONArray jsonResult_Child = new JSONArray(ws.getDSCTDongNuoc(String.valueOf(spnID_NhanVien.get(i)), edtFromDate.getText().toString(), edtToDate.getText().toString()));
                        for (int j = 0; j < jsonResult_Child.length(); j++) {
                            JSONObject jsonObject = jsonResult_Child.getJSONObject(j);
                            CLocal.jsonDongNuocChild.put(jsonObject);
                        }
                    }
                } else {
                    CLocal.jsonDongNuoc = new JSONArray(ws.getDSDongNuoc(selectedMaNV));
                    CLocal.jsonDongNuocChild = new JSONArray(ws.getDSCTDongNuoc(selectedMaNV));
//                    CLocal.jsonDongNuoc = new JSONArray(ws.getDSDongNuoc(selectedMaNV, edtFromDate.getText().toString(), edtToDate.getText().toString()));
//                    CLocal.jsonDongNuocChild = new JSONArray(ws.getDSCTDongNuoc(selectedMaNV, edtFromDate.getText().toString(), edtToDate.getText().toString()));
                }
                if (CLocal.jsonDongNuoc != null) {
                    //khởi tạo ArrayList CEntityParent
                    CLocal.listDongNuoc = new ArrayList<CEntityParent>();
                    for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                        JSONObject jsonObject = CLocal.jsonDongNuoc.getJSONObject(i);
                        CEntityParent enParent = new CEntityParent();
                        ///thiết lập khởi tạo 1 lần đầu để sort
                        if (jsonObject.has("ModifyDate") == false)
                            enParent.setModifyDate(CLocal.DateFormat.format(new Date()));
                        else
                            enParent.setModifyDate(jsonObject.getString("ModifyDate"));
                        enParent.setID(jsonObject.getString("MaDN"));

                        String strMLT = new StringBuffer(jsonObject.getString("MLT")).insert(4, " ").insert(2, " ").toString();
                        enParent.setMLT(strMLT);

                        String strDanhBo = new StringBuffer(jsonObject.getString("DanhBo")).insert(7, " ").insert(4, " ").toString();
                        enParent.setDanhBo(strDanhBo);

                        enParent.setHoTen(jsonObject.getString("HoTen"));
                        enParent.setDiaChi(jsonObject.getString("DiaChi"));
                        enParent.setDongNuoc(Boolean.parseBoolean(jsonObject.getString("DongNuoc")));
                        enParent.setDongNuoc2(Boolean.parseBoolean(jsonObject.getString("DongNuoc2")));
                        enParent.setDongPhi(Boolean.parseBoolean(jsonObject.getString("DongPhi")));
                        enParent.setMoNuoc(Boolean.parseBoolean(jsonObject.getString("MoNuoc")));
                        enParent.setButChi(Boolean.parseBoolean(jsonObject.getString("ButChi")));
                        enParent.setKhoaTu(Boolean.parseBoolean(jsonObject.getString("KhoaTu")));
                        enParent.setKhoaKhac(Boolean.parseBoolean(jsonObject.getString("KhoaKhac")));
                        if (jsonObject.getString("NgayDN").replace("null", "").equals("") == false)
                            enParent.setNgayDN(CLocal.convertTimestampToDate(Long.parseLong(jsonObject.getString("NgayDN").replace("null", "").replace("/Date(", "").replace(")/", ""))));
                        enParent.setChiSoDN(jsonObject.getString("ChiSoDN").replace("null", ""));
                        enParent.setNiemChi(jsonObject.getString("NiemChi").replace("null", ""));
                        enParent.setMauSac(jsonObject.getString("MauSac").replace("null", ""));
                        enParent.setKhoaKhac_GhiChu(jsonObject.getString("KhoaKhac_GhiChu").replace("null", ""));
                        enParent.setHieu(jsonObject.getString("Hieu"));
                        enParent.setCo(jsonObject.getString("Co"));
                        enParent.setSoThan(jsonObject.getString("SoThan"));
                        enParent.setChiMatSo(jsonObject.getString("ChiMatSo").replace("null", ""));
                        enParent.setChiKhoaGoc(jsonObject.getString("ChiKhoaGoc").replace("null", ""));
                        enParent.setViTri(jsonObject.getString("ViTri").replace("null", ""));
                        enParent.setLyDo(jsonObject.getString("LyDo").replace("null", ""));
                        if (jsonObject.getString("NgayDN1").replace("null", "").equals("") == false)
                            enParent.setNgayDN1(CLocal.convertTimestampToDate(Long.parseLong(jsonObject.getString("NgayDN1").replace("null", "").replace("/Date(", "").replace(")/", ""))));
                        enParent.setChiSoDN1(jsonObject.getString("ChiSoDN1").replace("null", ""));
                        enParent.setNiemChi1(jsonObject.getString("NiemChi1").replace("null", ""));
                        enParent.setMauSac1(jsonObject.getString("MauSac1").replace("null", ""));
                        if (jsonObject.getString("NgayMN").replace("null", "").equals("") == false)
                            enParent.setNgayMN(CLocal.convertTimestampToDate(Long.parseLong(jsonObject.getString("NgayMN").replace("null", "").replace("/Date(", "").replace(")/", ""))));
                        enParent.setChiSoMN(jsonObject.getString("ChiSoMN").replace("null", ""));
                        enParent.setNiemChiMN(jsonObject.getString("NiemChiMN").replace("null", ""));
                        enParent.setMauSacMN(jsonObject.getString("MauSacMN").replace("null", ""));
                        if (jsonObject.has("MaKQDN") == true)
                            enParent.setMaKQDN(jsonObject.getString("MaKQDN"));
                        if (jsonObject.has("DiaChiDHN") == true)
                            enParent.setDiaChiDHN(jsonObject.getString("DiaChiDHN").replace("null", ""));

                        if (jsonObject.has("CuaHangThuHo1") == true)
                            enParent.setCuaHangThuHo1(jsonObject.getString("CuaHangThuHo1").replace("null", ""));
                        if (jsonObject.has("CuaHangThuHo2") == true)
                            enParent.setCuaHangThuHo2(jsonObject.getString("CuaHangThuHo2").replace("null", ""));
                        if (jsonObject.getString("CreateDate").replace("null", "").equals("") == false)
                            enParent.setCreateDate(CLocal.convertTimestampToDate(Long.parseLong(jsonObject.getString("CreateDate").replace("null", "").replace("/Date(", "").replace(")/", ""))));
                        //khởi tạo ArrayList CEntityChild
                        ArrayList<CEntityChild> listChild = new ArrayList<CEntityChild>();
                        if (CLocal.jsonDongNuocChild != null && CLocal.jsonDongNuocChild.length() > 0)
                            for (int k = 0; k < CLocal.jsonDongNuocChild.length(); k++) {
                                JSONObject jsonObjectChild = CLocal.jsonDongNuocChild.getJSONObject(k);
                                if (jsonObjectChild.getString("MaDN").equals(enParent.getID()) == true) {
                                    CEntityChild enChild = new CEntityChild();
                                    enChild.setModifyDate(enParent.getModifyDate());
                                    enChild.setMaHD(jsonObjectChild.getString("MaHD"));
                                    enChild.setKy(jsonObjectChild.getString("Ky"));
                                    if (jsonObjectChild.has("GiaBan") == true)
                                        enChild.setGiaBan(jsonObjectChild.getString("GiaBan"));
                                    if (jsonObjectChild.has("ThueGTGT") == true)
                                        enChild.setThueGTGT(jsonObjectChild.getString("ThueGTGT"));
                                    if (jsonObjectChild.has("PhiBVMT") == true)
                                        enChild.setPhiBVMT(jsonObjectChild.getString("PhiBVMT"));
                                    if (jsonObjectChild.has("PhiBVMT_Thue") == true)
                                        enChild.setPhiBVMT_Thue(jsonObjectChild.getString("PhiBVMT_Thue"));
                                    enChild.setTongCong(jsonObjectChild.getString("TongCong"));
                                    if (jsonObjectChild.has("ChiTietTienNuoc") == true)
                                        enChild.setChiTietTienNuoc(jsonObjectChild.getString("ChiTietTienNuoc"));
                                    if (jsonObjectChild.has("GiaBieu") == true)
                                        enChild.setGiaBieu(jsonObjectChild.getString("GiaBieu"));
                                    if (jsonObjectChild.has("DinhMuc") == true)
                                        enChild.setDinhMuc(jsonObjectChild.getString("DinhMuc").replace("null", ""));
                                    if (jsonObjectChild.has("CSC") == true)
                                        enChild.setCSC(jsonObjectChild.getString("CSC"));
                                    if (jsonObjectChild.has("CSM") == true)
                                        enChild.setCSM(jsonObjectChild.getString("CSM"));
                                    if (jsonObjectChild.has("Code") == true)
                                        enChild.setCode(jsonObjectChild.getString("Code"));
                                    if (jsonObjectChild.has("TieuThu") == true)
                                        enChild.setTieuThu(jsonObjectChild.getString("TieuThu"));
                                    if (jsonObjectChild.has("TuNgay") == true)
                                        enChild.setTuNgay(jsonObjectChild.getString("TuNgay").replace("null", ""));
                                    if (jsonObjectChild.has("DenNgay") == true)
                                        enChild.setDenNgay(jsonObjectChild.getString("DenNgay").replace("null", ""));

                                    enChild.setGiaiTrach(Boolean.parseBoolean(jsonObjectChild.getString("GiaiTrach")));
                                    enChild.setTamThu(Boolean.parseBoolean(jsonObjectChild.getString("TamThu")));
                                    enChild.setThuHo(Boolean.parseBoolean(jsonObjectChild.getString("ThuHo")));
                                    enChild.setPhiMoNuocThuHo(jsonObjectChild.getString("PhiMoNuocThuHo").replace("null", ""));
                                    enChild.setLenhHuy(Boolean.parseBoolean(jsonObjectChild.getString("LenhHuy")));

                                    if (jsonObjectChild.has("DangNgan_DienThoai") == true)
                                        enChild.setDangNgan_DienThoai(Boolean.parseBoolean(jsonObjectChild.getString("DangNgan_DienThoai")));
                                    if (jsonObjectChild.has("MaNV_DangNgan") == true)
                                        enChild.setMaNV_DangNgan(jsonObjectChild.getString("MaNV_DangNgan"));
                                    if (jsonObjectChild.has("NgayGiaiTrach") == true)
                                        enChild.setNgayGiaiTrach(jsonObjectChild.getString("NgayGiaiTrach").replace("null", ""));
                                    if (jsonObjectChild.has("XoaDangNgan_Ngay_DienThoai") == true)
                                        enChild.setXoaDangNgan_Ngay_DienThoai(jsonObjectChild.getString("XoaDangNgan_Ngay_DienThoai").replace("null", ""));
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

                                    listChild.add(enChild);
                                }
                            }
                        //update TinhTrang
//                        int ThuHo = 0, TamThu = 0, GiaiTrach = 0, LenhHuy = 0, PhiMoNuocThuHo = 0;
//                        for (CEntityChild item : listChild) {
//                            if (item.getGiaiTrach() == true)
//                                GiaiTrach++;
//                            else if (item.getTamThu() == true)
//                                TamThu++;
//                            else if (item.getThuHo() == true) {
//                                ThuHo++;
//                                if (Integer.parseInt(item.getPhiMoNuocThuHo()) > 0)
//                                    PhiMoNuocThuHo++;
//                            } else if (item.getLenhHuy() == true)
//                                LenhHuy++;
//                        }
//
//                        if (GiaiTrach == listChild.size()) {
//                            enParent.setGiaiTrach(true);
//                            enParent.setTinhTrang("Giải Trách");
//                        } else if (TamThu == listChild.size()) {
//                            enParent.setTamThu(true);
//                            enParent.setTinhTrang("Tạm Thu");
//                        } else if (ThuHo == listChild.size()) {
//                            enParent.setThuHo(true);
//                            String str = "Thu Hộ";
//                            if (PhiMoNuocThuHo == listChild.size())
//                                str += " (" + listChild.get(0).getPhiMoNuocThuHo().substring(0, listChild.get(0).getPhiMoNuocThuHo().length() - 3) + "k)";
//                            enParent.setTinhTrang(str);
//                        } else if (LenhHuy == listChild.size()) {
//                            enParent.setLenhHuy(true);
//                        }

                        enParent.setLstHoaDon(listChild);
                        enParent = CLocal.updateTinhTrangParent(enParent);
                        CLocal.listDongNuoc.add(enParent);
                    }
                    SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
//                    editor.putString("jsonDongNuoc", CLocal.jsonDongNuoc.toString());
//                    editor.putString("jsonDongNuocChild", CLocal.jsonDongNuocChild.toString());
                    editor.putString("jsonDongNuoc", new Gson().toJsonTree(CLocal.listDongNuoc).getAsJsonArray().toString());
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
                CLocal.showPopupMessage(ActivityDownDataDongNuoc.this, strings[1], "center");
            }
        }
    }
}
