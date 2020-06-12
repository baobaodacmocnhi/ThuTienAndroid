package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.Bluetooth.ThermalPrinter;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityChild;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CHoaDon;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDongTien extends AppCompatActivity {
    private TextView txtTongCong;
    private EditText edtMaDN, edtPhiMoNuoc, edtTienDu;
    private Button btnDongTien, btnIn, btnXoa, btnPhieuBao2;
    private ListView lstView;
    private CheckBox chkPhiMoNuoc, chkTienDu;
    private ArrayList<CHoaDon> lstHoaDon;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private long TongCong = 0, PhiMoNuoc = 0, TienDu = 0;
    private String selectedMaHDs = "";
    private String lstMaHD = "";
    private String danhBo = "";
    private JSONArray jsonArrayHoaDonTon = null;
    private int STT = -1;
//    private ThermalPrinter thermalPrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dong_tien);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTongCong = (TextView) findViewById(R.id.txtTongCong);
        edtMaDN = (EditText) findViewById(R.id.edtMaDN);
        btnDongTien = (Button) findViewById(R.id.btnDongTien);
        btnIn = (Button) findViewById(R.id.btnIn);
        btnXoa = (Button) findViewById(R.id.btnXoa);
        btnPhieuBao2 = (Button) findViewById(R.id.btnPhieuBao2);
        lstView = (ListView) findViewById(R.id.lstView);
        edtPhiMoNuoc = (EditText) findViewById(R.id.edtPhiMoNuoc);
        edtTienDu = (EditText) findViewById(R.id.edtTienDu);
        chkPhiMoNuoc = (CheckBox) findViewById(R.id.chkPhiMoNuoc);
        chkTienDu = (CheckBox) findViewById(R.id.chkTienDu);

//        final MyAsyncTask_Thermal myAsyncTask_thermal = new MyAsyncTask_Thermal();
//        myAsyncTask_thermal.execute();

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

        chkPhiMoNuoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chkPhiMoNuoc.isChecked() == true) {
                    edtPhiMoNuoc.setEnabled(true);
                    TongCong += PhiMoNuoc;
                    txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
                } else {
                    edtPhiMoNuoc.setEnabled(false);
                    TongCong -= PhiMoNuoc;
                    txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
                }
            }
        });

        chkTienDu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chkTienDu.isChecked() == true) {
                    edtTienDu.setEnabled(true);
                    TongCong -= TienDu;
                    txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
                } else {
                    edtTienDu.setEnabled(false);
                    TongCong += TienDu;
                    txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
                }
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
                            selectedMaHDs = hoadon.getMaHD();
                        else
                            selectedMaHDs += "," + hoadon.getMaHD();
                    }
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute("DongTien");
            }
        });

        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (thermalPrinter == null || thermalPrinter.getBluetoothDevice() == null)
//                    thermalPrinter = new ThermalPrinter(ActivityDongTien.this);
                if(CLocal.thermalPrinterService!=null) {
                    CLocal.thermalPrinterService.printThuTien(CLocal.listDongNuocView.get(STT));
                    if (CLocal.listDongNuocView.get(STT).isDongPhi() == true)
                        CLocal.thermalPrinterService.printPhiMoNuoc(CLocal.listDongNuocView.get(STT));
                }
            }
        });

        btnPhieuBao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (thermalPrinter == null || thermalPrinter.getBluetoothDevice() == null)
//                    thermalPrinter = new ThermalPrinter(ActivityDongTien.this);
//                thermalPrinter.printPhieuBao2(CLocal.listDongNuocView.get(STT));
                if(CLocal.thermalPrinterService!=null)
                CLocal.thermalPrinterService.printPhieuBao2(CLocal.listDongNuocView.get(STT));
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
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
                            selectedMaHDs = hoadon.getMaHD();
                        else
                            selectedMaHDs += "," + hoadon.getMaHD();
                    }
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute("XoaDongTien");
            }
        });

        try {
//            String MaDN = getIntent().getStringExtra("MaDN");
//            if (MaDN.equals("") == false) {
//                fillDongNuoc(MaDN);
//            }
            STT = Integer.parseInt(getIntent().getStringExtra("STT"));
            if (STT > -1) {
                fillDongNuoc(STT);
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

//    public class CHoaDon {
//        private String MaHD;
//        private String Ky;
//        private String TongCong;
//        private boolean Selected;
//
//        public CHoaDon() {
//            MaHD = "";
//            Ky = "";
//            TongCong = "";
//            Selected = false;
//        }
//
//        public String getMaHD() {
//            return MaHD;
//        }
//
//        public void setMaHD(String maHD) {
//            MaHD = maHD;
//        }
//
//        public String getKy() {
//            return Ky;
//        }
//
//        public void setKy(String ky) {
//            Ky = ky;
//        }
//
//        public String getTongCong() {
//            return TongCong;
//        }
//
//        public void setTongCong(String tongCong) {
//            TongCong = tongCong;
//        }
//
//        public boolean isSelected() {
//            return Selected;
//        }
//
//        public void setSelected(boolean selected) {
//            Selected = selected;
//        }
//
//        @Override
//        public String toString() {
//            return Ky + " : " + CLocal.formatMoney(TongCong, "đ");
//        }
//    }

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
            CLocal.showToastMessage(ActivityDongTien.this, ex.getMessage());
        }
    }

    private void fillDongNuoc(int STT) {
        try {
            if (CLocal.listDongNuocView != null && CLocal.listDongNuocView.size() > 0) {
                lstHoaDon = new ArrayList<CHoaDon>();
                arrayList = new ArrayList<String>();
                CEntityParent en = CLocal.listDongNuocView.get(STT);
                edtMaDN.setText(en.getID());
                danhBo = en.getDanhBo().replace(" ", "");
                for (int i = 0; i < en.getLstHoaDon().size(); i++) {
                    CHoaDon entity = new CHoaDon();
                    entity.setMaHD(en.getLstHoaDon().get(i).getMaHD());
                    entity.setKy(en.getLstHoaDon().get(i).getKy());
                    entity.setTongCong(String.valueOf(Integer.parseInt(en.getLstHoaDon().get(i).getTongCong()) + en.getLstHoaDon().get(i).getTienDuTruocDCHD()));
                    lstHoaDon.add(entity);

                    if (lstMaHD.isEmpty() == true)
                        lstMaHD = entity.getMaHD();
                    else
                        lstMaHD += "," + entity.getMaHD();

                    TongCong += Long.parseLong(entity.getTongCong());
                    arrayList.add(entity.getKy() + " : " + CLocal.formatMoney(entity.getTongCong(), "đ"));
                    PhiMoNuoc = Integer.parseInt(en.getLstHoaDon().get(i).getPhiMoNuoc());
                    TienDu = en.getLstHoaDon().get(i).getTienDuTruocDCHD();
                }
                if (PhiMoNuoc > 0) {
                    chkPhiMoNuoc.setChecked(true);
                    edtPhiMoNuoc.setEnabled(true);
                } else {
                    chkPhiMoNuoc.setChecked(false);
                    edtPhiMoNuoc.setEnabled(false);
                }
                if (TienDu > 0) {
                    chkTienDu.setChecked(true);
                    edtTienDu.setEnabled(true);
                } else {
                    chkTienDu.setChecked(false);
                    edtTienDu.setEnabled(false);
                }
                edtPhiMoNuoc.setText(CLocal.formatMoney(String.valueOf(PhiMoNuoc), "đ"));
                edtTienDu.setText(CLocal.formatMoney(String.valueOf(TienDu), "đ"));
                txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));

                arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, arrayList);
                lstView.setAdapter(arrayAdapter);

                for (int j = 0; j < arrayAdapter.getCount(); j++) {
                    lstView.setItemChecked(j, true);
                }
            }
        } catch (Exception ex) {
            CLocal.showToastMessage(ActivityDongTien.this, ex.getMessage());
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
                    int index = -1;
                    boolean exists = false;
                    for (int i = 0; i < CLocal.listDongNuocView.get(STT).getLstHoaDon().size(); i++)
                        if (CLocal.listDongNuocView.get(STT).getLstHoaDon().get(i).getMaHD().equals(jsonObjectHoaDonTon.getString("MaHD")) == true) {
                            index = i;
                            exists = true;
                            break;
                        }
                    if (exists == true) {
                        CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setMaHD(jsonObjectHoaDonTon.getString("MaHD"));
                        CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setKy(jsonObjectHoaDonTon.getString("Ky"));
                        if (jsonObjectHoaDonTon.has("GiaBan") == true)
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setGiaBan(jsonObjectHoaDonTon.getString("GiaBan"));
                        if (jsonObjectHoaDonTon.has("ThueGTGT") == true)
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setThueGTGT(jsonObjectHoaDonTon.getString("ThueGTGT"));
                        if (jsonObjectHoaDonTon.has("PhiBVMT") == true)
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setPhiBVMT(jsonObjectHoaDonTon.getString("PhiBVMT"));
                        CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setTongCong(jsonObjectHoaDonTon.getString("TongCong"));
                        if (jsonObjectHoaDonTon.has("GiaBieu") == true)
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setGiaBieu(jsonObjectHoaDonTon.getString("GiaBieu"));
                        if (jsonObjectHoaDonTon.has("DinhMuc") == true)
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setDinhMuc(jsonObjectHoaDonTon.getString("DinhMuc").replace("null", ""));
                        if (jsonObjectHoaDonTon.has("CSC") == true)
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setCSC(jsonObjectHoaDonTon.getString("CSC"));
                        if (jsonObjectHoaDonTon.has("CSM") == true)
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setCSM(jsonObjectHoaDonTon.getString("CSM"));
                        if (jsonObjectHoaDonTon.has("TieuThu") == true)
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setTieuThu(jsonObjectHoaDonTon.getString("TieuThu"));
                        if (jsonObjectHoaDonTon.has("TuNgay") == true)
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setTuNgay(jsonObjectHoaDonTon.getString("TuNgay"));
                        if (jsonObjectHoaDonTon.has("DenNgay") == true)
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setDenNgay(jsonObjectHoaDonTon.getString("DenNgay"));

                        CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setGiaiTrach(Boolean.parseBoolean(jsonObjectHoaDonTon.getString("GiaiTrach")));
                        CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setTamThu(Boolean.parseBoolean(jsonObjectHoaDonTon.getString("TamThu")));
                        CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setThuHo(Boolean.parseBoolean(jsonObjectHoaDonTon.getString("ThuHo")));
                        CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setPhiMoNuocThuHo(jsonObjectHoaDonTon.getString("PhiMoNuocThuHo").replace("null", ""));
                        CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setLenhHuy(Boolean.parseBoolean(jsonObjectHoaDonTon.getString("LenhHuy")));

                        if (jsonObjectHoaDonTon.has("DangNgan_DienThoai") == true)
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setDangNgan_DienThoai(Boolean.parseBoolean(jsonObjectHoaDonTon.getString("DangNgan_DienThoai")));
                        if (jsonObjectHoaDonTon.has("NgayGiaiTrach") == true)
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setNgayGiaiTrach(jsonObjectHoaDonTon.getString("NgayGiaiTrach").replace("null", ""));
                        if (jsonObjectHoaDonTon.has("XoaDangNgan_Ngay_DienThoai") == true)
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setXoaDangNgan_Ngay_DienThoai(jsonObjectHoaDonTon.getString("XoaDangNgan_Ngay_DienThoai").replace("null", ""));
                        if (jsonObjectHoaDonTon.has("PhiMoNuoc") == true)
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setPhiMoNuoc(jsonObjectHoaDonTon.getString("PhiMoNuoc"));
                        if (jsonObjectHoaDonTon.has("DCHD") == true) {
                            CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setDCHD(Boolean.parseBoolean(jsonObjectHoaDonTon.getString("DCHD")));
                            if (CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).isDCHD() == true) {
                                CLocal.listDongNuocView.get(STT).setDCHD(CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).isDCHD());
                                CLocal.listDongNuocView.get(STT).getLstHoaDon().get(index).setTienDuTruocDCHD(Integer.parseInt(jsonObjectHoaDonTon.getString("TienDuTruoc_DCHD")));
                            }
                        }
                    } else {
                        CEntityChild enChild = new CEntityChild();
                        enChild.setMaHD(jsonObjectHoaDonTon.getString("MaHD"));
                        enChild.setKy(jsonObjectHoaDonTon.getString("Ky"));
                        if (jsonObjectHoaDonTon.has("GiaBan") == true)
                            enChild.setGiaBan(jsonObjectHoaDonTon.getString("GiaBan"));
                        if (jsonObjectHoaDonTon.has("ThueGTGT") == true)
                            enChild.setThueGTGT(jsonObjectHoaDonTon.getString("ThueGTGT"));
                        if (jsonObjectHoaDonTon.has("PhiBVMT") == true)
                            enChild.setPhiBVMT(jsonObjectHoaDonTon.getString("PhiBVMT"));
                        enChild.setTongCong(jsonObjectHoaDonTon.getString("TongCong"));
                        if (jsonObjectHoaDonTon.has("GiaBieu") == true)
                            enChild.setGiaBieu(jsonObjectHoaDonTon.getString("GiaBieu"));
                        if (jsonObjectHoaDonTon.has("DinhMuc") == true)
                            enChild.setDinhMuc(jsonObjectHoaDonTon.getString("DinhMuc").replace("null", ""));
                        if (jsonObjectHoaDonTon.has("CSC") == true)
                            enChild.setCSC(jsonObjectHoaDonTon.getString("CSC"));
                        if (jsonObjectHoaDonTon.has("CSM") == true)
                            enChild.setCSM(jsonObjectHoaDonTon.getString("CSM"));
                        if (jsonObjectHoaDonTon.has("TieuThu") == true)
                            enChild.setTieuThu(jsonObjectHoaDonTon.getString("TieuThu"));
                        if (jsonObjectHoaDonTon.has("TuNgay") == true)
                            enChild.setTuNgay(jsonObjectHoaDonTon.getString("TuNgay"));
                        if (jsonObjectHoaDonTon.has("DenNgay") == true)
                            enChild.setDenNgay(jsonObjectHoaDonTon.getString("DenNgay"));

                        enChild.setGiaiTrach(Boolean.parseBoolean(jsonObjectHoaDonTon.getString("GiaiTrach")));
                        enChild.setTamThu(Boolean.parseBoolean(jsonObjectHoaDonTon.getString("TamThu")));
                        enChild.setThuHo(Boolean.parseBoolean(jsonObjectHoaDonTon.getString("ThuHo")));
                        enChild.setPhiMoNuocThuHo(jsonObjectHoaDonTon.getString("PhiMoNuocThuHo").replace("null", ""));
                        enChild.setLenhHuy(Boolean.parseBoolean(jsonObjectHoaDonTon.getString("LenhHuy")));

                        if (jsonObjectHoaDonTon.has("DangNgan_DienThoai") == true)
                            enChild.setDangNgan_DienThoai(Boolean.parseBoolean(jsonObjectHoaDonTon.getString("DangNgan_DienThoai")));
                        if (jsonObjectHoaDonTon.has("NgayGiaiTrach") == true)
                            enChild.setNgayGiaiTrach(jsonObjectHoaDonTon.getString("NgayGiaiTrach").replace("null", ""));
                        if (jsonObjectHoaDonTon.has("XoaDangNgan_Ngay_DienThoai") == true)
                            enChild.setXoaDangNgan_Ngay_DienThoai(jsonObjectHoaDonTon.getString("XoaDangNgan_Ngay_DienThoai").replace("null", ""));
                        if (jsonObjectHoaDonTon.has("PhiMoNuoc") == true)
                            enChild.setPhiMoNuoc(jsonObjectHoaDonTon.getString("PhiMoNuoc"));
                        if (jsonObjectHoaDonTon.has("DCHD") == true) {
                            enChild.setDCHD(Boolean.parseBoolean(jsonObjectHoaDonTon.getString("DCHD")));
                            if (enChild.isDCHD() == true) {
                                CLocal.listDongNuocView.get(STT).setDCHD(enChild.isDCHD());
                                enChild.setTienDuTruocDCHD(Integer.parseInt(jsonObjectHoaDonTon.getString("TienDuTruoc_DCHD")));
                            }
                        }
                        CLocal.listDongNuocView.get(STT).getLstHoaDon().add(enChild);
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    protected void onDestroy() {
//        if (thermalPrinter != null)
//            thermalPrinter.disconnectBluetoothDevice();
        super.onDestroy();
    }

    public class MyAsyncTask_Thermal extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
//                thermalPrinter = new ThermalPrinter(ActivityDongTien.this);
            } catch (Exception ex) {
                CLocal.showToastMessage(ActivityDongTien.this, ex.getMessage());
            }
            return null;
        }
    }

    public class MyAsyncTask extends AsyncTask<String, String, String[]> {
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
        protected String[] doInBackground(String... strings) {
            String result = "";
            String[] results = new String[]{};
            Boolean XoaDCHD = false;
            switch (strings[0]) {
                case "DongTien":
//                    if (selectedMaHDs.equals("") == true)
//                        return "CHƯA CHỌN HÓA ĐƠN";
//                    result = ws.dangNganDongNuoc(CLocal.MaNV, selectedMaHDs);
//                    if (Boolean.parseBoolean(result) == true) {
//                        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//                        Date dateCapNhat = new Date();
//                        for (int j = 0; j < CLocal.listDongNuocView.get(STT).getLstHoaDon().size(); j++) {
//                            if (CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
//                                    && CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isThuHo() == false
//                                    && CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isTamThu() == false
//                                    && CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false) {
//                                CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(true);
//                                CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).setGiaiTrach(true);
//                                CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).setNgayGiaiTrach(currentDate.format(dateCapNhat));
//                            }
//                        }
//                        CLocal.updateTinhTrangParent(CLocal.listDongNuocView, STT);
//                        return "THÀNH CÔNG";
//                    } else
//                        return "THẤT BẠI";
                    try {
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date dateCapNhat = new Date();
                        for (int j = 0; j < CLocal.listDongNuocView.get(STT).getLstHoaDon().size(); j++)
                            if (CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                    && CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                    && CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                    && selectedMaHDs.contains(CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                XoaDCHD = false;
                                if (CLocal.listDongNuocView.get(STT).isDCHD() == true && chkTienDu.isChecked() == false)
                                    XoaDCHD = true;
                                result = ws.XuLy_HoaDonDienTu("DangNgan", CLocal.MaNV, CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).getMaHD(), currentDate.format(dateCapNhat), "", CLocal.listDongNuocView.get(STT).getMaKQDN(), XoaDCHD.toString());
                                results = result.split(",");
                                if (Boolean.parseBoolean(results[0]) == true) {
                                    CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(true);
                                    CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).setGiaiTrach(true);
                                    CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).setNgayGiaiTrach(currentDate.format(dateCapNhat));
                                    CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).setXoaDangNgan_Ngay_DienThoai("");
//                                    if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null) {
//                                        thermalPrinter.printThuTien(CLocal.listDongNuocView.get(STT), CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j));
//                                    }
                                    CLocal.updateTinhTrangParent(CLocal.listDongNuocView, STT);
                                    CLocal.updateTinhTrangParent(CLocal.listDongNuoc, CLocal.listDongNuocView.get(STT));
                                } else {
                                    if (results.length == 5) {
                                        CLocal.updateValueChild(CLocal.listDongNuoc, results[2], results[3], results[4]);
                                        CLocal.updateValueChild(CLocal.listDongNuocView, results[2], results[3], results[4]);
                                        CLocal.updateTinhTrangParent(CLocal.listDongNuocView, STT);
                                    }
                                }

                            }
                        if (chkPhiMoNuoc.isChecked() == true) {
                            result = ws.XuLy_HoaDonDienTu("DongPhi", CLocal.MaNV, "", currentDate.format(dateCapNhat), "", CLocal.listDongNuocView.get(STT).getMaKQDN(), XoaDCHD.toString());
                            results = result.split(",");
                            if (Boolean.parseBoolean(results[0]) == true) {
                                CLocal.listDongNuocView.get(STT).setDongPhi(true);
                                CLocal.updateTinhTrangParent(CLocal.listDongNuocView, STT);
                                CLocal.updateTinhTrangParent(CLocal.listDongNuoc, CLocal.listDongNuocView.get(STT));
//                                if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null) {
//                                    thermalPrinter.printPhiMoNuoc(CLocal.listDongNuocView.get(STT));
//                                }
                            }
                        }
                        return results;
                    } catch (Exception ex) {
                        return new String[]{"false", ex.getMessage()};
                    }
                case "XoaDongTien":
                    try {
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date dateCapNhat = new Date();
                        for (int j = 0; j < CLocal.listDongNuocView.get(STT).getLstHoaDon().size(); j++)
                            if (CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == true
                                    && CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                    && selectedMaHDs.contains(CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                result = ws.XuLy_HoaDonDienTu("XoaDangNgan", CLocal.MaNV, CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).getMaHD(), currentDate.format(dateCapNhat), "", CLocal.listDongNuocView.get(STT).getMaKQDN(), String.valueOf(XoaDCHD));
                                results = result.split(",");
                                if (Boolean.parseBoolean(results[0]) == true) {
                                    CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(false);
                                    CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).setGiaiTrach(false);
                                    CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).setNgayGiaiTrach("");
                                    CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).setXoaDangNgan_Ngay_DienThoai(currentDate.format(dateCapNhat));
                                    CLocal.updateTinhTrangParent(CLocal.listDongNuocView, STT);
                                    CLocal.updateTinhTrangParent(CLocal.listDongNuoc, CLocal.listDongNuocView.get(STT));
                                }
                            }

                        if (chkPhiMoNuoc.isChecked() == true) {
                            result = ws.XuLy_HoaDonDienTu("XoaDongPhi", CLocal.MaNV, "", currentDate.format(dateCapNhat), "", CLocal.listDongNuocView.get(STT).getMaKQDN(), String.valueOf(XoaDCHD));
                            results = result.split(",");
                            if (Boolean.parseBoolean(results[0]) == true) {
                                CLocal.listDongNuocView.get(STT).setDongPhi(false);
                                CLocal.updateTinhTrangParent(CLocal.listDongNuocView, STT);
                                CLocal.updateTinhTrangParent(CLocal.listDongNuoc, CLocal.listDongNuocView.get(STT));
                            }
                        }
                        return results;
                    } catch (Exception ex) {
                        return new String[]{"false", ex.getMessage()};
                    }
                case "GetHoaDonTon":
                    result = ws.getDSHoaDonTon_DongNuoc(danhBo, lstMaHD);
                    publishProgress(new String[]{"GetHoaDonTon", result});
                    return new String[]{"", "tải hóa đơn tồn"};
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
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (strings[0].equals("") == false)
                if (Boolean.parseBoolean(strings[0]) == true) {
                    CLocal.showPopupMessage(ActivityDongTien.this, "THÀNH CÔNG", "center");
                } else if (Boolean.parseBoolean(strings[0]) == false)
                    CLocal.showPopupMessage(ActivityDongTien.this, "THẤT BẠI\n" + strings[1], "center");
        }

    }

}
