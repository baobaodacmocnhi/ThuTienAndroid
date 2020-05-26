package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.Bluetooth.ThermalPrinter;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CHoaDon;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityHoaDonDienTu_ThuTien extends AppCompatActivity {
    private TextView txtTinhTrang, txtTongHD;
    private EditText edtMLT, edtDanhBo, edtHoTen, edtDiaChi, edtInPhieuBao_Ngay, edtInPhieuBao2_Ngay, edtInPhieuBao2_NgayHen, edtInTBDongNuoc_Ngay, edtInTBDongNuoc_NgayHen, edtSoNgayHen, edtPhiMoNuoc, edtTienDu, edtTongCong;
    private ListView lstView;
    private Button btnTruoc, btnSau, btnThuTien, btnPhieuBao, btnPhieuBao2, btnTBDongNuoc, btnXoa;
    private CheckBox chkPhiMoNuoc, chkTienDu;
    private Integer STT = -1;
    private ThermalPrinter thermalPrinter = null;
    private CWebservice ws;
    private ArrayList<CHoaDon> lstHoaDon;
    private long TongCong = 0, PhiMoNuoc = 0, TienDu = 0;
    private String selectedMaHDs = "";
    private ImageView imgviewThongKe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_dien_tu_thu_tien);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTinhTrang = (TextView) findViewById(R.id.txtTinhTrang);
        txtTongHD = (TextView) findViewById(R.id.txtTongHD);
        edtMLT = (EditText) findViewById(R.id.edtMLT);
        edtDanhBo = (EditText) findViewById(R.id.edtDanhBo);
        edtHoTen = (EditText) findViewById(R.id.edtHoTen);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtInPhieuBao_Ngay = (EditText) findViewById(R.id.edtInPhieuBao_Ngay);
        edtInPhieuBao2_Ngay = (EditText) findViewById(R.id.edtInPhieuBao2_Ngay);
        edtInPhieuBao2_NgayHen = (EditText) findViewById(R.id.edtInPhieuBao2_NgayHen);
        edtInTBDongNuoc_Ngay = (EditText) findViewById(R.id.edtInTBDongNuoc_Ngay);
        edtInTBDongNuoc_NgayHen = (EditText) findViewById(R.id.edtInTBDongNuoc_NgayHen);
        edtSoNgayHen = (EditText) findViewById(R.id.edtSoNgayHen);
        edtPhiMoNuoc = (EditText) findViewById(R.id.edtPhiMoNuoc);
        edtTienDu = (EditText) findViewById(R.id.edtTienDu);
        edtTongCong = (EditText) findViewById(R.id.edtTongCong);
        lstView = (ListView) findViewById(R.id.lstView);
        btnTruoc = (Button) findViewById(R.id.btnTruoc);
        btnSau = (Button) findViewById(R.id.btnSau);
        btnThuTien = (Button) findViewById(R.id.btnThuTien);
        btnPhieuBao = (Button) findViewById(R.id.btnPhieuBao);
        btnPhieuBao2 = (Button) findViewById(R.id.btnPhieuBao2);
        btnTBDongNuoc = (Button) findViewById(R.id.btnTBDongNuoc);
        btnXoa = (Button) findViewById(R.id.btnXoa);
        chkPhiMoNuoc = (CheckBox) findViewById(R.id.chkPhiMoNuoc);
        chkTienDu = (CheckBox) findViewById(R.id.chkTienDu);

        lstView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final MyAsyncTask_Thermal myAsyncTask_thermal = new MyAsyncTask_Thermal();
        myAsyncTask_thermal.execute();
        ws = new CWebservice();

        try {
//            String DanhBo = getIntent().getStringExtra("DanhBo");
//            if (DanhBo.equals("") == false) {
//                fillLayout(DanhBo);
//            }
            STT = Integer.parseInt(getIntent().getStringExtra("STT"));
            if (STT > -1) {
                fillLayout(STT);
            }
        } catch (Exception ex) {
            CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
        }

        imgviewThongKe = (ImageView) findViewById(R.id.imgviewThongKe);
        imgviewThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int TongHD = 0, TongHDThuHo = 0, TongHDDaThu = 0, TongHDTon = 0;
                long TongCong = 0, TongCongThuHo = 0, TongCongDaThu = 0, TongCongTon = 0;
                if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                    for (int i = 0; i < CLocal.listHanhThu.size(); i++) {
                        for (int j = 0; j < CLocal.listHanhThu.get(i).getLstHoaDon().size(); j++) {
                            //tổng
                            TongHD++;
                            TongCong += Long.parseLong(CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getTongCong());
                            //thu hộ
                            if ((CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isGiaiTrach() == true && CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == false) || CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isTamThu() == true || CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isThuHo() == true) {
                                TongHDThuHo++;
                                TongCongThuHo += Long.parseLong(CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getTongCong());
                            }
                            //đã thu
                            if (CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == true) {
                                TongHDDaThu++;
                                TongCongDaThu += Long.parseLong(CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getTongCong());
                            }
                            //tồn
                            if (CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == false && CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isGiaiTrach() == false && CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isTamThu() == false && CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isThuHo() == false) {
                                TongHDTon++;
                                TongCongTon += Long.parseLong(CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getTongCong());
                            }
                        }
                    }
                }
                CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "Tổng: " + CLocal.formatMoney(String.valueOf(TongHD), "hđ") + " = " + CLocal.formatMoney(String.valueOf(TongCong), "đ")
                        + "\n\nThu Hộ: " + CLocal.formatMoney(String.valueOf(TongHDThuHo), "hđ") + " = " + CLocal.formatMoney(String.valueOf(TongCongThuHo), "đ")
                        + "\n\nĐã Thu: " + CLocal.formatMoney(String.valueOf(TongHDDaThu), "hđ") + " = " + CLocal.formatMoney(String.valueOf(TongCongDaThu), "đ")
                        + "\n\nTồn: " + CLocal.formatMoney(String.valueOf(TongHDTon), "hđ") + " = " + CLocal.formatMoney(String.valueOf(TongCongTon), "đ"), "right");
            }
        });

        btnTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (STT > 0) {
                    STT--;
                    fillLayout(STT);
                } else
                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Đầu Danh Sách");
            }
        });

        btnSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (STT < CLocal.listHanhThuView.size() - 1) {
                    STT++;
                    fillLayout(STT);
                } else
                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Cuối Danh Sách");
            }
        });

        //listview in scrollview not scroll, nên thêm code sau
        lstView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

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
                edtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));

                SparseBooleanArray sp = lstView.getCheckedItemPositions();
                selectedMaHDs = "";
                for (int i = 0; i < sp.size(); i++) {
                    if (sp.valueAt(i) == true) {
                        CHoaDon hoadon1 = lstHoaDon.get(sp.keyAt(i));
                        if (selectedMaHDs.equals("") == true)
                            selectedMaHDs = hoadon1.getMaHD();
                        else
                            selectedMaHDs += "," + hoadon1.getMaHD();
                    }
                }
            }
        });

        chkPhiMoNuoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chkPhiMoNuoc.isChecked() == true) {
                    edtPhiMoNuoc.setEnabled(true);
                    TongCong += PhiMoNuoc;
                    edtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
                } else {
                    edtPhiMoNuoc.setEnabled(false);
                    TongCong -= PhiMoNuoc;
                    edtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
                }
            }
        });

        chkTienDu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chkTienDu.isChecked() == true) {
                    edtTienDu.setEnabled(true);
                    TongCong -= TienDu;
                    edtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
                } else {
                    edtTienDu.setEnabled(false);
                    TongCong += TienDu;
                    edtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
                }
            }
        });

        btnThuTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHoaDonDienTu_ThuTien.this);
                    builder.setMessage("Bạn có chắc chắn Thu Tiền?")
                            .setCancelable(false)
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                                        if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                                            if (CLocal.SyncTrucTiep == true) {
                                                if (CLocal.listHanhThuView.get(STT).isGiaiTrach() == false
                                                        && CLocal.listHanhThuView.get(STT).isThuHo() == false
                                                        && CLocal.listHanhThuView.get(STT).isTamThu() == false
                                                        && CLocal.listHanhThuView.get(STT).isDangNgan_DienThoai() == false) {
                                                    MyAsyncTask_XuLyTrucTiep myAsyncTask_xuLyTrucTiep = new MyAsyncTask_XuLyTrucTiep();
                                                    myAsyncTask_xuLyTrucTiep.execute(new String[]{"DangNgan"});
                                                } else
                                                    reInBienNhan_Direct("DangNgan", CLocal.listHanhThuView.get(STT));
                                            } else {
                                                boolean flag = false;
                                                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                                Date dateCapNhat = new Date();
                                                for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++) {
                                                    if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false) {
                                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(true);
                                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setNgayGiaiTrach(currentDate.format(dateCapNhat));
                                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setXoaDangNgan_Ngay_DienThoai("");
                                                        if (CLocal.listHanhThuView.get(STT).isDCHD() == true && chkTienDu.isChecked() == false)
                                                            CLocal.listHanhThuView.get(STT).setXoaDCHD(true);
                                                        if (chkPhiMoNuoc.isChecked() == true)
                                                            CLocal.listHanhThuView.get(STT).setDongPhi(true);
                                                        flag = true;
                                                    }
                                                }
                                                if (flag == true) {
                                                    CLocal.listHanhThuView.get(STT).setSync(true);
                                                    CLocal.listHanhThuView.get(STT).setXuLy("DangNgan");
                                                    CLocal.updateTinhTrangParent(CLocal.listHanhThuView, STT);
                                                    CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(STT));
                                                    MyAsyncTask_XuLyGianTiep myAsyncTask_xuLyGianTiep = new MyAsyncTask_XuLyGianTiep();
                                                    myAsyncTask_xuLyGianTiep.execute(new String[]{CLocal.listHanhThuView.get(STT).getXuLy(), String.valueOf(STT)});
                                                    flag = false;
                                                    if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                                        thermalPrinter.printThuTien(CLocal.listHanhThuView.get(STT));
                                                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Thành Công");
                                                    btnSau.performClick();
                                                } else
                                                    CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "Đã Xử Lý Rồi");
                                            }
                                        }
                                    }
                                }
                            })
                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                } catch (Exception ex) {
                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
                }
            }
        });

        btnPhieuBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                        if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                            if (CLocal.SyncTrucTiep == true) {
                                if (CLocal.listHanhThuView.get(STT).isGiaiTrach() == false
                                        && CLocal.listHanhThuView.get(STT).isThuHo() == false
                                        && CLocal.listHanhThuView.get(STT).isTamThu() == false
                                        && CLocal.listHanhThuView.get(STT).isDangNgan_DienThoai() == false
                                        && CLocal.listHanhThuView.get(STT).isInPhieuBao() == false) {
                                    MyAsyncTask_XuLyTrucTiep myAsyncTask_xuLyTrucTiep = new MyAsyncTask_XuLyTrucTiep();
                                    myAsyncTask_xuLyTrucTiep.execute(new String[]{"PhieuBao"});
                                } else
                                    reInBienNhan_Direct("PhieuBao", CLocal.listHanhThuView.get(STT));
                            } else {
                                boolean flag = false;
                                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                Date dateCapNhat = new Date();
                                for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++) {
                                    if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getInPhieuBao_Ngay().equals("") == true) {
                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao_Ngay(currentDate.format(dateCapNhat));
                                        flag = true;
                                    }
                                }
                                if (flag == true) {
                                    CLocal.listHanhThuView.get(STT).setSync(true);
                                    CLocal.listHanhThuView.get(STT).setXuLy("PhieuBao");
                                    CLocal.updateTinhTrangParent(CLocal.listHanhThuView, STT);
                                    CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(STT));
                                    MyAsyncTask_XuLyGianTiep myAsyncTask_xuLyGianTiep = new MyAsyncTask_XuLyGianTiep();
                                    myAsyncTask_xuLyGianTiep.execute(new String[]{CLocal.listHanhThuView.get(STT).getXuLy(), String.valueOf(STT)});
                                    flag = false;
                                    if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                        thermalPrinter.printPhieuBao(CLocal.listHanhThuView.get(STT));
                                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Thành Công");
                                    btnSau.performClick();
                                } else
                                    CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "Đã Xử Lý Rồi");
                            }
                        }
                    }
                } catch (Exception ex) {
                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
                }
            }
        });

        btnPhieuBao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                        if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                            if (CLocal.SyncTrucTiep == true) {
                                if (CLocal.listHanhThuView.get(STT).isGiaiTrach() == false
                                        && CLocal.listHanhThuView.get(STT).isThuHo() == false
                                        && CLocal.listHanhThuView.get(STT).isTamThu() == false
                                        && CLocal.listHanhThuView.get(STT).isDangNgan_DienThoai() == false
                                        && CLocal.listHanhThuView.get(STT).isInPhieuBao2() == false) {
                                    MyAsyncTask_XuLyTrucTiep myAsyncTask_xuLyTrucTiep = new MyAsyncTask_XuLyTrucTiep();
                                    myAsyncTask_xuLyTrucTiep.execute(new String[]{"PhieuBao2", edtSoNgayHen.getText().toString()});
                                } else
                                    reInBienNhan_Direct("PhieuBao2", CLocal.listHanhThuView.get(STT));
                            } else {
                                boolean flag = false;
                                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                Date dateCapNhat = new Date();
                                for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++) {
                                    if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getInPhieuBao2_Ngay().equals("") == true) {
                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao2_Ngay(currentDate.format(dateCapNhat));
                                        Date dt = dateCapNhat;
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(dt);
                                        c.add(Calendar.DATE, Integer.parseInt(edtSoNgayHen.getText().toString()));
                                        dt = c.getTime();
                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao2_NgayHen(currentDate.format(dt));
                                        flag = true;
                                    }
                                }
                                if (flag == true) {
                                    CLocal.listHanhThuView.get(STT).setSync(true);
                                    CLocal.listHanhThuView.get(STT).setXuLy("PhieuBao2");
                                    CLocal.updateTinhTrangParent(CLocal.listHanhThuView, STT);
                                    CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(STT));
                                    MyAsyncTask_XuLyGianTiep myAsyncTask_xuLyGianTiep = new MyAsyncTask_XuLyGianTiep();
                                    myAsyncTask_xuLyGianTiep.execute(new String[]{CLocal.listHanhThuView.get(STT).getXuLy(), String.valueOf(STT)});
                                    flag = false;
                                    if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                        thermalPrinter.printPhieuBao2(CLocal.listHanhThuView.get(STT));
                                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Thành Công");
                                    btnSau.performClick();
                                } else
                                    CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "Đã Xử Lý Rồi");
                            }
                        }
                    }
                } catch (Exception ex) {
                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
                }
            }
        });

        btnTBDongNuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                        if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                            if (CLocal.SyncTrucTiep == true) {
                                if (CLocal.listHanhThuView.get(STT).isGiaiTrach() == false
                                        && CLocal.listHanhThuView.get(STT).isThuHo() == false
                                        && CLocal.listHanhThuView.get(STT).isTamThu() == false
                                        && CLocal.listHanhThuView.get(STT).isDangNgan_DienThoai() == false
                                        && CLocal.listHanhThuView.get(STT).isTBDongNuoc() == false) {
                                    MyAsyncTask_XuLyTrucTiep myAsyncTask_xuLyTrucTiep = new MyAsyncTask_XuLyTrucTiep();
                                    myAsyncTask_xuLyTrucTiep.execute(new String[]{"TBDongNuoc", edtSoNgayHen.getText().toString()});
                                } else
                                    reInBienNhan_Direct("TBDongNuoc", CLocal.listHanhThuView.get(STT));
                            } else {
                                boolean flag = false;
                                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                Date dateCapNhat = new Date();
                                for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++) {
                                    if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getTBDongNuoc_Ngay().equals("") == true) {
                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setTBDongNuoc_Ngay(currentDate.format(dateCapNhat));
                                        Date dt = dateCapNhat;
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(dt);
                                        c.add(Calendar.DATE, Integer.parseInt(edtSoNgayHen.getText().toString()));
                                        dt = c.getTime();
                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setTBDongNuoc_NgayHen(currentDate.format(dt));
                                        flag = true;
                                    }
                                }
                                if (flag == true) {
                                    CLocal.listHanhThuView.get(STT).setSync(true);
                                    CLocal.listHanhThuView.get(STT).setXuLy("TBDongNuoc");
                                    CLocal.updateTinhTrangParent(CLocal.listHanhThuView, STT);
                                    CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(STT));
                                    MyAsyncTask_XuLyGianTiep myAsyncTask_xuLyGianTiep = new MyAsyncTask_XuLyGianTiep();
                                    myAsyncTask_xuLyGianTiep.execute(new String[]{CLocal.listHanhThuView.get(STT).getXuLy(), String.valueOf(STT)});
                                    flag = false;
                                    if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                        thermalPrinter.printTBDongNuoc(CLocal.listHanhThuView.get(STT));
                                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Thành Công");
                                    btnSau.performClick();
                                } else
                                    CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "Đã Xử Lý Rồi");
                            }
                        }
                    }
                } catch (Exception ex) {
                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
                }
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHoaDonDienTu_ThuTien.this);
                    builder.setMessage("Bạn có chắc chắn xóa?")
                            .setCancelable(false)
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                                        if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                                            if (CLocal.SyncTrucTiep == true) {
                                                if (CLocal.listHanhThuView.get(STT).isGiaiTrach() == true
                                                        && CLocal.listHanhThuView.get(STT).isThuHo() == false
                                                        && CLocal.listHanhThuView.get(STT).isTamThu() == false
                                                        && CLocal.listHanhThuView.get(STT).isDangNgan_DienThoai() == true) {
                                                    MyAsyncTask_XuLyTrucTiep myAsyncTask_xuLyTrucTiep = new MyAsyncTask_XuLyTrucTiep();
                                                    myAsyncTask_xuLyTrucTiep.execute(new String[]{"XoaDangNgan"});
                                                }
                                            } else {
                                                boolean flag = false;
                                                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                                Date dateCapNhat = new Date();
                                                for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                                                    if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == true) {
                                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setXoaDangNgan_Ngay_DienThoai(currentDate.format(dateCapNhat));
                                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(false);
                                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setNgayGiaiTrach("");
                                                        if (chkPhiMoNuoc.isChecked() == true)
                                                            CLocal.listHanhThuView.get(STT).setDongPhi(false);
                                                        flag = true;
                                                    }
                                                if (flag == true) {
                                                    CLocal.listHanhThuView.get(STT).setSync(true);
                                                    CLocal.listHanhThuView.get(STT).setXuLy("XoaDangNgan");
                                                    CLocal.updateTinhTrangParent(CLocal.listHanhThuView, STT);
                                                    CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(STT));
                                                    MyAsyncTask_XuLyGianTiep myAsyncTask_xuLyGianTiep = new MyAsyncTask_XuLyGianTiep();
                                                    myAsyncTask_xuLyGianTiep.execute(new String[]{CLocal.listHanhThuView.get(STT).getXuLy(), String.valueOf(STT)});
                                                    flag = false;
                                                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Thành Công");
                                                } else
                                                    CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "Đã Xử Lý Rồi");
                                            }
                                        }
                                    }
                                }
                            })
                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                } catch (Exception ex) {
                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thutien, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_search_khach_hang:
                intent = new Intent(ActivityHoaDonDienTu_ThuTien.this, ActivityHoaDonDienTu_Search.class);
                startActivityForResult(intent, 1);
                return true;
            case R.id.action_ghichu:
                intent = new Intent(ActivityHoaDonDienTu_ThuTien.this, ActivityHoaDonDienTu_GhiChu.class);
                intent.putExtra("DanhBo", edtDanhBo.getText().toString().replace(" ", ""));
                startActivity(intent);
                return true;
            case R.id.action_print:
                intent = new Intent(ActivityHoaDonDienTu_ThuTien.this, ActivityHoaDonDienTu_In.class);
                intent.putExtra("STT", String.valueOf(STT));
                startActivity(intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillLayout(String DanhBo) {
        try {
            if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                ArrayList<String> arrayList = new ArrayList<String>();
                for (int i = 0; i < CLocal.listHanhThuView.size(); i++) {
                    if (CLocal.listHanhThuView.get(i).getDanhBo().equals(DanhBo) == true) {
                        STT = i;
                        fillLayout(STT);
                        break;
                    }
                }

            }
        } catch (Exception ex) {
            CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
        }
    }

    private void fillLayout(Integer STT) {
        try {
            if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                ArrayList<String> arrayList = new ArrayList<String>();
                if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                    CEntityParent item = CLocal.listHanhThuView.get(STT);
                    txtTinhTrang.setText(item.getTinhTrang());
                    txtTongHD.setText(String.valueOf(item.getLstHoaDon().size()) + " hđ");
                    edtMLT.setText(item.getMLT());
                    edtDanhBo.setText(item.getDanhBo());
                    edtHoTen.setText(item.getHoTen());
                    edtDiaChi.setText(item.getDiaChi());
                    edtInPhieuBao_Ngay.setText(item.getLstHoaDon().get(0).getInPhieuBao_Ngay());
                    edtInPhieuBao2_Ngay.setText(item.getLstHoaDon().get(0).getInPhieuBao2_Ngay());
                    edtInPhieuBao2_NgayHen.setText(item.getLstHoaDon().get(0).getInPhieuBao2_NgayHen());
                    edtInTBDongNuoc_Ngay.setText(item.getLstHoaDon().get(0).getTBDongNuoc_Ngay());
                    edtInTBDongNuoc_NgayHen.setText(item.getLstHoaDon().get(0).getTBDongNuoc_NgayHen());
                    lstHoaDon = new ArrayList<>();
                    TongCong = PhiMoNuoc = TienDu = 0;
                    selectedMaHDs = "";
                    for (int j = 0; j < item.getLstHoaDon().size(); j++) {
                        CHoaDon entity = new CHoaDon();
                        entity.setMaHD(item.getLstHoaDon().get(j).getMaHD());
                        entity.setKy(item.getLstHoaDon().get(j).getKy());
                        entity.setTongCong(item.getLstHoaDon().get(j).getTongCong());
                        entity.setSelected(true);
                        lstHoaDon.add(entity);

                        if (selectedMaHDs.equals("") == true)
                            selectedMaHDs = item.getLstHoaDon().get(j).getMaHD();
                        else
                            selectedMaHDs += "," + item.getLstHoaDon().get(j).getMaHD();

                        TongCong += Long.parseLong(item.getLstHoaDon().get(j).getTongCong());
                        arrayList.add(item.getLstHoaDon().get(j).getKy() + " : " + CLocal.formatMoney(item.getLstHoaDon().get(j).getTongCong(), "đ"));
                        PhiMoNuoc = Integer.parseInt(item.getLstHoaDon().get(j).getPhiMoNuoc());
                        TienDu = item.getLstHoaDon().get(j).getTienDuTruocDCHD();
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
                    edtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
                    if (item.isThuHo() == true || item.isTamThu() == true || (item.isGiaiTrach() == true && item.isDangNgan_DienThoai() == false)) {
                        btnThuTien.setEnabled(false);
                        btnPhieuBao.setEnabled(false);
                        btnPhieuBao2.setEnabled(false);
                        btnTBDongNuoc.setEnabled(false);
                        btnXoa.setEnabled(false);
                    } else {
                        btnThuTien.setEnabled(true);
                        btnPhieuBao.setEnabled(true);
                        btnPhieuBao2.setEnabled(true);
                        btnTBDongNuoc.setEnabled(true);
                        btnXoa.setEnabled(true);
                    }
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, arrayList);
                lstView.setAdapter(arrayAdapter);

                for (int j = 0; j < arrayAdapter.getCount(); j++) {
                    lstView.setItemChecked(j, true);
                }
            }
        } catch (Exception ex) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK)
                fillLayout(data.getStringExtra("DanhBo"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
            thermalPrinter.disconnectBluetoothDevice();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void reInBienNhan_Dialog(final String Loai, final CEntityParent entityParent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHoaDonDienTu_ThuTien.this);
        builder.setMessage("Đã Xử Lý Rồi")
                .setCancelable(false)
                .setNegativeButton("In Lại", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (Loai) {
                            case "DangNgan":
                                for (int j = 0; j < entityParent.getLstHoaDon().size(); j++)
                                    if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                        thermalPrinter.printThuTien(entityParent, entityParent.getLstHoaDon().get(j));
                                break;
                            case "PhieuBao":
                                for (int j = 0; j < entityParent.getLstHoaDon().size(); j++)
                                    if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                        thermalPrinter.printPhieuBao(entityParent, entityParent.getLstHoaDon().get(j));
                                break;
                            case "PhieuBao2":
                                if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                    thermalPrinter.printPhieuBao2(entityParent);
                                break;
                            case "TBDongNuoc":
                                if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                    thermalPrinter.printTBDongNuoc(entityParent);
                                break;
                        }
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void reInBienNhan_Direct(final String Loai, final CEntityParent entityParent) {
        switch (Loai) {
            case "DangNgan":
                for (int j = 0; j < entityParent.getLstHoaDon().size(); j++)
                    if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                        thermalPrinter.printThuTien(entityParent, entityParent.getLstHoaDon().get(j));
                break;
            case "PhieuBao":
                for (int j = 0; j < entityParent.getLstHoaDon().size(); j++)
                    if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                        thermalPrinter.printPhieuBao(entityParent, entityParent.getLstHoaDon().get(j));
                break;
            case "PhieuBao2":
                if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                    thermalPrinter.printPhieuBao2(entityParent);
                break;
            case "TBDongNuoc":
                if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                    thermalPrinter.printTBDongNuoc(entityParent);
                break;
        }
    }

    public class MyAsyncTask_Thermal extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityHoaDonDienTu_ThuTien.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang kết nối máy in");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (thermalPrinter == null || thermalPrinter.getBluetoothDevice() == null)
                    thermalPrinter = new ThermalPrinter(ActivityHoaDonDienTu_ThuTien.this);
            } catch (Exception ex) {
                CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

    }

    public class MyAsyncTask_XuLyTrucTiep extends AsyncTask<String, Void, String[]> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityHoaDonDienTu_ThuTien.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String[] doInBackground(String... strings) {
            try {
                String result = "";
                String[] results = new String[]{};
                String MaHDs = "";
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date dateCapNhat = new Date();
                boolean XoaDCHD = false;
                switch (strings[0]) {
                    case "DangNgan":
                        for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                    && selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                XoaDCHD = false;
                                if (CLocal.listHanhThuView.get(STT).isDCHD() == true && chkTienDu.isChecked() == false)
                                    XoaDCHD = true;
                                result = ws.XuLy_HoaDonDienTu("DangNgan", CLocal.MaNV, CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD(), currentDate.format(dateCapNhat), "", CLocal.listHanhThu.get(STT).getMaKQDN(), String.valueOf(XoaDCHD));
                                results = result.split(",");
                                if (Boolean.parseBoolean(results[0]) == true) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(true);
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setGiaiTrach(true);
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setNgayGiaiTrach(currentDate.format(dateCapNhat));
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setXoaDangNgan_Ngay_DienThoai("");
                                    if (CLocal.listHanhThuView.get(STT).isDCHD() == true && chkTienDu.isChecked() == false)
                                        CLocal.listHanhThuView.get(STT).setXoaDCHD(true);
                                    if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null) {
                                        thermalPrinter.printThuTien(CLocal.listHanhThuView.get(STT), CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j));
                                    }
                                }
                            }
                        if (chkPhiMoNuoc.isChecked() == true) {
                            result = ws.XuLy_HoaDonDienTu("DongPhi", CLocal.MaNV, "", currentDate.format(dateCapNhat), "", CLocal.listDongNuocView.get(STT).getMaKQDN(), String.valueOf(XoaDCHD));
                            results = result.split(",");
                            if (Boolean.parseBoolean(results[0]) == true) {
                                CLocal.listHanhThuView.get(STT).setDongPhi(true);
                                if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null) {
                                    thermalPrinter.printPhiMoNuoc(CLocal.listHanhThuView.get(STT));
                                }
                            }
                        }
                        break;
                    case "PhieuBao":
                        for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getInPhieuBao_Ngay().equals("") == true
                                    && selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                result = ws.XuLy_HoaDonDienTu("PhieuBao", CLocal.MaNV, CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD(), currentDate.format(dateCapNhat), "", CLocal.listHanhThu.get(STT).getMaKQDN(), String.valueOf(XoaDCHD));
                                results = result.split(",");
                                if (Boolean.parseBoolean(results[0]) == true) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao_Ngay(currentDate.format(dateCapNhat));
                                    if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                        thermalPrinter.printPhieuBao(CLocal.listHanhThuView.get(STT), CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j));
                                }
                            }

                        break;
                    case "PhieuBao2":
                        for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getInPhieuBao2_Ngay().equals("") == true
                                    && selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                if (MaHDs.equals("") == true)
                                    MaHDs += CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD();
                                else
                                    MaHDs += "," + CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD();
                            }
                        Date dt = dateCapNhat;
                        Calendar c = Calendar.getInstance();
                        c.setTime(dt);
                        c.add(Calendar.DATE, Integer.parseInt(strings[1]));
                        dt = c.getTime();
                        result = ws.XuLy_HoaDonDienTu("PhieuBao2", CLocal.MaNV, MaHDs, currentDate.format(dateCapNhat), currentDate.format(dt), CLocal.listHanhThu.get(STT).getMaKQDN(), String.valueOf(XoaDCHD));
                        results = result.split(",");
                        if (Boolean.parseBoolean(results[0]) == true) {
                            for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                                if (selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD())) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao2_Ngay(currentDate.format(dateCapNhat));
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao2_NgayHen(currentDate.format(dt));
                                }
                            if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                thermalPrinter.printPhieuBao2(CLocal.listHanhThuView.get(STT));
                        }
                        break;
                    case "TBDongNuoc":
                        for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getTBDongNuoc_Ngay().equals("") == true
                                    && selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                if (MaHDs.equals("") == true)
                                    MaHDs += CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD();
                                else
                                    MaHDs += "," + CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD();
                            }
                        Date dt2 = dateCapNhat;
                        Calendar c2 = Calendar.getInstance();
                        c2.setTime(dt2);
                        c2.add(Calendar.DATE, Integer.parseInt(strings[1]));
                        dt2 = c2.getTime();
                        result = ws.XuLy_HoaDonDienTu("TBDongNuoc", CLocal.MaNV, MaHDs, currentDate.format(dateCapNhat), currentDate.format(dt2), CLocal.listHanhThu.get(STT).getMaKQDN(), String.valueOf(XoaDCHD));
                        results = result.split(",");
                        if (Boolean.parseBoolean(results[0]) == true) {
                            for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                                if (selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD())) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setTBDongNuoc_Ngay(currentDate.format(dateCapNhat));
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setTBDongNuoc_NgayHen(currentDate.format(dt2));
                                }
                            if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                thermalPrinter.printTBDongNuoc(CLocal.listHanhThuView.get(STT));
                        }
                        break;
                    case "XoaDangNgan":
                        for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == true
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                    && selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                result = ws.XuLy_HoaDonDienTu("XoaDangNgan", CLocal.MaNV, CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD(), currentDate.format(dateCapNhat), "", CLocal.listHanhThu.get(STT).getMaKQDN(), String.valueOf(XoaDCHD));
                                results = result.split(",");
                                if (Boolean.parseBoolean(results[0]) == true) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(false);
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setGiaiTrach(false);
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setNgayGiaiTrach("");
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setXoaDangNgan_Ngay_DienThoai(currentDate.format(dateCapNhat));
                                }
                            }
                        if (chkPhiMoNuoc.isChecked() == true) {
                            result = ws.XuLy_HoaDonDienTu("XoaDongPhi", CLocal.MaNV, "", currentDate.format(dateCapNhat), "", CLocal.listDongNuocView.get(STT).getMaKQDN(), String.valueOf(XoaDCHD));
                            results = result.split(",");
                            if (Boolean.parseBoolean(results[0]) == true) {
                                CLocal.listHanhThuView.get(STT).setDongPhi(false);
                            }
                        }
                        break;
                }
                if (Boolean.parseBoolean(results[0]) == false && results.length == 5) {
                    CLocal.updateValueChild(CLocal.listHanhThu, results[2], results[3], results[4]);
                    CLocal.updateValueChild(CLocal.listHanhThuView, results[2], results[3], results[4]);
                }
                CLocal.updateTinhTrangParent(CLocal.listHanhThuView, STT);
                CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(STT));
                return results;

            } catch (Exception ex) {
                CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (Boolean.parseBoolean(strings[0]) == true) {
                CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "THÀNH CÔNG");
                btnSau.performClick();
            } else
                CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "THẤT BẠI\n" + strings[1]);
        }
    }

    public class MyAsyncTask_XuLyGianTiep extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String result = "";
                String MaHDs = "";
                boolean XoaDCHD = false;
                int i = Integer.parseInt(strings[1]);
                switch (strings[0]) {
                    case "DangNgan":
                        for (int j = 0; j < CLocal.listHanhThuView.get(i).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == true
                                    && selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                if (MaHDs.equals("") == true)
                                    MaHDs += CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                                else
                                    MaHDs += "," + CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                            }
                        XoaDCHD = false;
                        if (CLocal.listHanhThuView.get(STT).isDCHD() == true && chkTienDu.isChecked() == false)
                            XoaDCHD = true;
                        result = ws.XuLy_HoaDonDienTu("DangNgan", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getNgayGiaiTrach(), "", CLocal.listHanhThuView.get(i).getMaKQDN(), String.valueOf(XoaDCHD));
                        break;
                    case "PhieuBao":
                        for (int j = 0; j < CLocal.listHanhThuView.get(i).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getInPhieuBao_Ngay().equals("") == false
                                    && selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                if (MaHDs.equals("") == true)
                                    MaHDs += CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                                else
                                    MaHDs += "," + CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                            }
                        result = ws.XuLy_HoaDonDienTu("PhieuBao", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getInPhieuBao_Ngay(), "", CLocal.listHanhThuView.get(i).getMaKQDN(), String.valueOf(XoaDCHD));
                        break;
                    case "PhieuBao2":
                        for (int j = 0; j < CLocal.listHanhThuView.get(i).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getInPhieuBao2_Ngay().equals("") == false
                                    && selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                if (MaHDs.equals("") == true)
                                    MaHDs += CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                                else
                                    MaHDs += "," + CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                            }
                        result = ws.XuLy_HoaDonDienTu("PhieuBao2", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getInPhieuBao2_Ngay(), CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getInPhieuBao2_NgayHen(), CLocal.listHanhThuView.get(i).getMaKQDN(), String.valueOf(XoaDCHD));
                        break;
                    case "TBDongNuoc":
                        for (int j = 0; j < CLocal.listHanhThuView.get(i).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getTBDongNuoc_Ngay().equals("") == false
                                    && selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                if (MaHDs.equals("") == true)
                                    MaHDs += CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                                else
                                    MaHDs += "," + CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                            }
                        result = ws.XuLy_HoaDonDienTu("TBDongNuoc", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getTBDongNuoc_Ngay(), CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getTBDongNuoc_NgayHen(), CLocal.listHanhThuView.get(i).getMaKQDN(), String.valueOf(XoaDCHD));
                        break;
                    case "XoaDangNgan":
                        for (int j = 0; j < CLocal.listHanhThuView.get(i).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getXoaDangNgan_Ngay_DienThoai().equals("") == false
                                    && selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                if (MaHDs.equals("") == true)
                                    MaHDs += CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                                else
                                    MaHDs += "," + CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                            }
                        result = ws.XuLy_HoaDonDienTu("XoaDangNgan", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getXoaDangNgan_Ngay_DienThoai(), "", CLocal.listHanhThuView.get(i).getMaKQDN(), String.valueOf(XoaDCHD));
                        break;
                }

                String[] results = result.split(",");

                if (Boolean.parseBoolean(results[0]) == true) {
                    CLocal.listHanhThuView.get(i).setSync(false);
                    CLocal.listHanhThuView.get(i).setXuLy("");
                    CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(i));
                } else if (results.length == 5) {
                    CLocal.updateValueChild(CLocal.listHanhThu, results[2], results[3], results[4]);
                    CLocal.updateValueChild(CLocal.listHanhThuView, results[2], results[3], results[4]);
                }
            } catch (Exception ex) {
                CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for (int i = 0; i < CLocal.listHanhThuView.size(); i++)
                if (CLocal.listHanhThuView.get(i).isSync() == true) {
                    MyAsyncTask_XuLyGianTiep myAsyncTask_xuLyGianTiep = new MyAsyncTask_XuLyGianTiep();
                    myAsyncTask_xuLyGianTiep.execute(new String[]{CLocal.listHanhThuView.get(i).getXuLy(), String.valueOf(i)});
                }
            super.onPostExecute(aVoid);
        }
    }


}
