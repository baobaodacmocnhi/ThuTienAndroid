package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CHoaDon;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocation;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityHoaDonDienTu_ThuTien extends AppCompatActivity {
    private TextView txtTinhTrang, txtLenhHuy, txtTongHD;
    private EditText edtMLT, edtDanhBo, edtHoTen, edtDiaChi, edtDiaChiDHN, edtInPhieuBao_Ngay, edtInPhieuBao2_Ngay, edtInPhieuBao2_NgayHen, edtInTBDongNuoc_Ngay, edtInTBDongNuoc_NgayHen, edtSoNgayHen, edtPhiMoNuoc, edtTienDu, edtTongCong;
    private ListView lstView;
    private Button btnTruoc, btnSau, btnThuTien, btnPhieuBao, btnPhieuBaoCT, btnPhieuBao2, btnTBDongNuoc, btnXoa;
    private CheckBox chkPhiMoNuoc, chkTienDu;
    private Integer STT = -1;
    private CWebservice ws;
    private ArrayList<CHoaDon> lstHoaDon;
    private long TongCong = 0, PhiMoNuoc = 0, TienDu = 0;
    private String selectedMaHDs = "";
    private ImageView imgviewThongKe, imgviewUpdateDiaChiDHN;
    private CLocation cLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_dien_tu_thu_tien);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cLocation = new CLocation(ActivityHoaDonDienTu_ThuTien.this);
        ws = new CWebservice();

        txtTinhTrang = (TextView) findViewById(R.id.txtTinhTrang);
        txtLenhHuy = (TextView) findViewById(R.id.txtLenhHuy);
        txtTongHD = (TextView) findViewById(R.id.txtTongHD);
        edtMLT = (EditText) findViewById(R.id.edtMLT);
        edtDanhBo = (EditText) findViewById(R.id.edtDanhBo);
        edtHoTen = (EditText) findViewById(R.id.edtHoTen);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtDiaChiDHN = (EditText) findViewById(R.id.edtDiaChiDHN);
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
        btnPhieuBaoCT = (Button) findViewById(R.id.btnPhieuBaoCT);
        btnPhieuBao2 = (Button) findViewById(R.id.btnPhieuBao2);
        btnTBDongNuoc = (Button) findViewById(R.id.btnTBDongNuoc);
        btnXoa = (Button) findViewById(R.id.btnXoa);
        chkPhiMoNuoc = (CheckBox) findViewById(R.id.chkPhiMoNuoc);
        chkTienDu = (CheckBox) findViewById(R.id.chkTienDu);
        imgviewThongKe = (ImageView) findViewById(R.id.imgviewThongKe);
        imgviewUpdateDiaChiDHN = (ImageView) findViewById(R.id.imgviewUpdateDiaChiDHN);

        lstView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        final MyAsyncTask_Thermal myAsyncTask_thermal = new MyAsyncTask_Thermal();
//        myAsyncTask_thermal.execute();

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
                            if ((CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == true && CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getMaNV_DangNgan().equals(CLocal.MaNV) == false)
                                    || (CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isGiaiTrach() == true && CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == false)
                                    || CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isTamThu() == true || CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isThuHo() == true) {
                                TongHDThuHo++;
                                TongCongThuHo += Long.parseLong(CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getTongCong());
                            }
                            //đã thu
                            if (CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == true && CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getMaNV_DangNgan().equals(CLocal.MaNV) == true) {
                                TongHDDaThu++;
                                TongCongDaThu += Long.parseLong(CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getTongCong());
                            }
                            //tồn
                            if ((CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == false)
                                    && CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isGiaiTrach() == false && CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isTamThu() == false && CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isThuHo() == false) {
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

        imgviewUpdateDiaChiDHN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTask_updateDiaChiDHN myAsyncTask_updateDiaChiDHN = new MyAsyncTask_updateDiaChiDHN();
                myAsyncTask_updateDiaChiDHN.execute();
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
                                                if ((CLocal.listHanhThuView.get(STT).isGiaiTrach() == false
                                                        && CLocal.listHanhThuView.get(STT).isThuHo() == false
                                                        && CLocal.listHanhThuView.get(STT).isTamThu() == false
                                                        && CLocal.listHanhThuView.get(STT).isDangNgan_DienThoai() == false)) {
                                                    MyAsyncTask_XuLyTrucTiep myAsyncTask_xuLyTrucTiep = new MyAsyncTask_XuLyTrucTiep();
                                                    myAsyncTask_xuLyTrucTiep.execute(new String[]{"DangNgan"});
                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHoaDonDienTu_ThuTien.this);
                                                    builder.setMessage("Đã Xử Lý, Bạn muốn In lại?")
                                                            .setCancelable(false)
                                                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    try {
                                                                        reInBienNhan_Direct("DangNgan", CLocal.listHanhThuView.get(STT));
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    } catch (ParseException e) {
                                                                        e.printStackTrace();
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
                                                    Button btnPositive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                                                    Button btnNegative = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                                                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                                                    layoutParams.weight = 10;
                                                    layoutParams.gravity = Gravity.CENTER;
                                                    btnPositive.setLayoutParams(layoutParams);
                                                    btnNegative.setLayoutParams(layoutParams);
                                                }
                                            } else {
                                                boolean flag = false;
//                                                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                                Date dateCapNhat = new Date();
                                                for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++) {
                                                    if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false) {
                                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(true);
                                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setNgayGiaiTrach(CLocal.DateFormat.format(dateCapNhat));
                                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setMaNV_DangNgan(CLocal.MaNV);
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
                                                    if (CLocal.serviceThermalPrinter != null) {
                                                        try {
                                                            CLocal.serviceThermalPrinter.printThuTien(CLocal.listHanhThuView.get(STT));
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Thành Công");
                                                    btnSau.performClick();
                                                } else
                                                    CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "Đã Xử Lý Rồi", "center");
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
                    Button btnPositive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button btnNegative = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                    layoutParams.weight = 10;
                    layoutParams.gravity = Gravity.CENTER;
                    btnPositive.setLayoutParams(layoutParams);
                    btnNegative.setLayoutParams(layoutParams);
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
                                if ((CLocal.InPhieuBao == true || (CLocal.listHanhThuView.get(STT).isGiaiTrach() == false
                                        && CLocal.listHanhThuView.get(STT).isThuHo() == false
                                        && CLocal.listHanhThuView.get(STT).isTamThu() == false
                                        && CLocal.listHanhThuView.get(STT).isDangNgan_DienThoai() == false))
                                        && CLocal.listHanhThuView.get(STT).isInPhieuBao() == false) {
                                    MyAsyncTask_XuLyTrucTiep myAsyncTask_xuLyTrucTiep = new MyAsyncTask_XuLyTrucTiep();
                                    myAsyncTask_xuLyTrucTiep.execute(new String[]{"PhieuBao"});
                                } else
                                    reInBienNhan_Direct("PhieuBao", CLocal.listHanhThuView.get(STT));
                            } else {
                                boolean flag = false;
//                                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                Date dateCapNhat = new Date();
                                for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++) {
                                    if ((CLocal.InPhieuBao == true || (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false))
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getInPhieuBao_Ngay().equals("") == true) {
                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao_Ngay(CLocal.DateFormat.format(dateCapNhat));
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
                                    if (CLocal.serviceThermalPrinter != null)
                                        CLocal.serviceThermalPrinter.printPhieuBao(CLocal.listHanhThuView.get(STT));
                                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Thành Công");
                                    btnSau.performClick();
                                } else
                                    CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "Đã Xử Lý Rồi", "center");
                            }
                        }
                    }
                } catch (Exception ex) {
                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
                }
            }
        });

        btnPhieuBaoCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                        if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                            if (CLocal.SyncTrucTiep == true) {
                                if ((CLocal.InPhieuBao == true || (CLocal.listHanhThuView.get(STT).isGiaiTrach() == false
                                        && CLocal.listHanhThuView.get(STT).isThuHo() == false
                                        && CLocal.listHanhThuView.get(STT).isTamThu() == false
                                        && CLocal.listHanhThuView.get(STT).isDangNgan_DienThoai() == false))
                                        && CLocal.listHanhThuView.get(STT).isInPhieuBao() == false) {
                                    MyAsyncTask_XuLyTrucTiep myAsyncTask_xuLyTrucTiep = new MyAsyncTask_XuLyTrucTiep();
                                    myAsyncTask_xuLyTrucTiep.execute(new String[]{"PhieuBaoCT"});
                                } else
                                    reInBienNhan_Direct("PhieuBaoCT", CLocal.listHanhThuView.get(STT));
                            } else {
                                boolean flag = false;
//                                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                Date dateCapNhat = new Date();
                                for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++) {
                                    if ((CLocal.InPhieuBao == true || (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false))
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getInPhieuBao_Ngay().equals("") == true) {
                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao_Ngay(CLocal.DateFormat.format(dateCapNhat));
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
                                    if (CLocal.serviceThermalPrinter != null)
                                        CLocal.serviceThermalPrinter.printPhieuBaoCT(CLocal.listHanhThuView.get(STT));
                                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Thành Công");
                                    btnSau.performClick();
                                } else
                                    CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "Đã Xử Lý Rồi", "center");
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
                                if ((CLocal.InPhieuBao == true || (CLocal.listHanhThuView.get(STT).isGiaiTrach() == false
                                        && CLocal.listHanhThuView.get(STT).isThuHo() == false
                                        && CLocal.listHanhThuView.get(STT).isTamThu() == false
                                        && CLocal.listHanhThuView.get(STT).isDangNgan_DienThoai() == false))
                                        && CLocal.listHanhThuView.get(STT).isInPhieuBao2() == false) {
                                    MyAsyncTask_XuLyTrucTiep myAsyncTask_xuLyTrucTiep = new MyAsyncTask_XuLyTrucTiep();
                                    myAsyncTask_xuLyTrucTiep.execute(new String[]{"PhieuBao2", edtSoNgayHen.getText().toString()});
                                } else
                                    reInBienNhan_Direct("PhieuBao2", CLocal.listHanhThuView.get(STT));
                            } else {
                                boolean flag = false;
//                                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                Date dateCapNhat = new Date();
                                for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++) {
                                    if ((CLocal.InPhieuBao == true || (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false))
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getInPhieuBao2_Ngay().equals("") == true) {
                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao2_Ngay(CLocal.DateFormat.format(dateCapNhat));
                                        Date dt = dateCapNhat;
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(dt);
                                        c.add(Calendar.DATE, Integer.parseInt(edtSoNgayHen.getText().toString()));
                                        dt = c.getTime();
                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao2_NgayHen(CLocal.DateFormat.format(dt));
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
                                    if (CLocal.serviceThermalPrinter != null)
                                        CLocal.serviceThermalPrinter.printPhieuBao2(CLocal.listHanhThuView.get(STT));
                                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Thành Công");
                                    btnSau.performClick();
                                } else
                                    CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "Đã Xử Lý Rồi", "center");
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
//                                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                Date dateCapNhat = new Date();
                                for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++) {
                                    if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getTBDongNuoc_Ngay().equals("") == true) {
                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setTBDongNuoc_Ngay(CLocal.DateFormat.format(dateCapNhat));
                                        Date dt = dateCapNhat;
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(dt);
//                                        c.add(Calendar.DATE, Integer.parseInt(edtSoNgayHen.getText().toString()));
                                        c = CLocal.GetToDate(c, Integer.parseInt(edtSoNgayHen.getText().toString()));
                                        dt = c.getTime();
                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setTBDongNuoc_NgayHen(CLocal.DateFormat.format(dt));
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
                                    if (CLocal.serviceThermalPrinter != null)
                                        CLocal.serviceThermalPrinter.printTBDongNuoc(CLocal.listHanhThuView.get(STT));
                                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Thành Công");
                                    btnSau.performClick();
                                } else
                                    CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "Đã Xử Lý Rồi", "center");
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
                                                if ((CLocal.listHanhThuView.get(STT).isGiaiTrach() == true
                                                        && CLocal.listHanhThuView.get(STT).isThuHo() == false
                                                        && CLocal.listHanhThuView.get(STT).isTamThu() == false
                                                        && CLocal.listHanhThuView.get(STT).isDangNgan_DienThoai() == true)
                                                        || (CLocal.checkDangNganChild(CLocal.listHanhThuView.get(STT)) == true)) {
                                                    MyAsyncTask_XuLyTrucTiep myAsyncTask_xuLyTrucTiep = new MyAsyncTask_XuLyTrucTiep();
                                                    myAsyncTask_xuLyTrucTiep.execute(new String[]{"XoaDangNgan"});
                                                }
                                            } else {
                                                boolean flag = false;
//                                                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                                Date dateCapNhat = new Date();
                                                for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                                                    if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                                            && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == true) {
                                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setXoaDangNgan_Ngay_DienThoai(CLocal.DateFormat.format(dateCapNhat));
                                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(false);
                                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setNgayGiaiTrach("");
                                                        CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setMaNV_DangNgan("");
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
                                                    CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "Đã Xử Lý Rồi", "center");
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
                    Button btnPositive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button btnNegative = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                    layoutParams.weight = 10;
                    layoutParams.gravity = Gravity.CENTER;
                    btnPositive.setLayoutParams(layoutParams);
                    btnNegative.setLayoutParams(layoutParams);
                } catch (Exception ex) {
                    CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            STT = Integer.parseInt(getIntent().getStringExtra("STT"));
            if (STT > -1) {
                fillLayout(STT);
            }
        } catch (Exception ex) {
            CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menubar_thutien, menu);
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

    private void fillLayout(final Integer STT) {
        try {
            if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                ArrayList<String> arrayList = new ArrayList<String>();
                if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                    CEntityParent item = CLocal.listHanhThuView.get(STT);
                    txtTinhTrang.setText(item.getTinhTrang());
                    if (item.isLenhHuy() == true) {
                        if (item.isLenhHuyCat() == true)
                            txtLenhHuy.setText("Lệnh Hủy có Cắt");
                        else
                            txtLenhHuy.setText("Lệnh Hủy");
                    } else
                        txtLenhHuy.setText("");
                    txtTongHD.setText(String.valueOf(item.getLstHoaDon().size()) + " hđ");
                    edtMLT.setText(item.getMLT());
                    edtDanhBo.setText(item.getDanhBo());
                    edtHoTen.setText(item.getHoTen());
                    edtDiaChi.setText(item.getDiaChi());
                    edtDiaChiDHN.setText(item.getDiaChiDHN());
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
                        entity.setTongCong(String.valueOf(Integer.parseInt(item.getLstHoaDon().get(j).getTongCong()) + item.getLstHoaDon().get(j).getTienDuTruocDCHD()));
                        entity.setSelected(true);
                        lstHoaDon.add(entity);

                        if (selectedMaHDs.equals("") == true)
                            selectedMaHDs = item.getLstHoaDon().get(j).getMaHD();
                        else
                            selectedMaHDs += "," + item.getLstHoaDon().get(j).getMaHD();

                        TongCong += Long.parseLong(entity.getTongCong());
                        arrayList.add(entity.getKy() + " : " + CLocal.formatMoney(entity.getTongCong(), "đ") + " (Code: " + item.getLstHoaDon().get(j).getCode() + ")");
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
//                    TongCong += TienDu;
                    edtTienDu.setText(CLocal.formatMoney(String.valueOf(TienDu), "đ"));
                    edtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
                    if (item.isThuHo() == true || item.isTamThu() == true || (item.isGiaiTrach() == true && item.isDangNgan_DienThoai() == false)) {
                        btnThuTien.setEnabled(false);
                        btnPhieuBao.setEnabled(false);
                        btnPhieuBao2.setEnabled(false);
                        btnTBDongNuoc.setEnabled(false);
                        btnXoa.setEnabled(false);
                        if (CLocal.InPhieuBao == true) {
                            btnPhieuBao.setEnabled(true);
                            btnPhieuBao2.setEnabled(true);
                        }
                    } else {
                        btnThuTien.setEnabled(true);
                        btnPhieuBao.setEnabled(true);
                        btnPhieuBao2.setEnabled(true);
                        btnTBDongNuoc.setEnabled(true);
                        btnXoa.setEnabled(true);
                    }
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, arrayList) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        // Get the current item from ListView
                        View view = super.getView(position, convertView, parent);
                        if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(position).isGiaiTrach() == true)
                            view.setBackgroundColor(getResources().getColor(R.color.colorGiaiTrach));
                        return view;
                    }
                };
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

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        super.onBackPressed();
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
                                    if (CLocal.serviceThermalPrinter != null) {
                                        try {
                                            CLocal.serviceThermalPrinter.printThuTien(entityParent, entityParent.getLstHoaDon().get(j));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                break;
                            case "PhieuBao":
                                for (int j = 0; j < entityParent.getLstHoaDon().size(); j++)
                                    if (CLocal.serviceThermalPrinter != null) {
                                        try {
                                            CLocal.serviceThermalPrinter.printPhieuBao(entityParent, entityParent.getLstHoaDon().get(j));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                break;
                            case "PhieuBao2":
                                if (CLocal.serviceThermalPrinter != null) {
                                    try {
                                        CLocal.serviceThermalPrinter.printPhieuBao2(entityParent);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case "TBDongNuoc":
                                if (CLocal.serviceThermalPrinter != null) {
                                    try {
                                        CLocal.serviceThermalPrinter.printTBDongNuoc(entityParent);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
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

    private void reInBienNhan_Direct(final String Loai, final CEntityParent entityParent) throws IOException, ParseException {
        switch (Loai) {
            case "DangNgan":
                for (int j = 0; j < entityParent.getLstHoaDon().size(); j++)
                    if (CLocal.serviceThermalPrinter != null)
                        CLocal.serviceThermalPrinter.printThuTien(entityParent, entityParent.getLstHoaDon().get(j));
                break;
            case "PhieuBao":
                for (int j = 0; j < entityParent.getLstHoaDon().size(); j++)
                    if (CLocal.serviceThermalPrinter != null)
                        CLocal.serviceThermalPrinter.printPhieuBao(entityParent, entityParent.getLstHoaDon().get(j));
                break;
            case "PhieuBaoCT":
                for (int j = 0; j < entityParent.getLstHoaDon().size(); j++)
                    if (CLocal.serviceThermalPrinter != null)
                        CLocal.serviceThermalPrinter.printPhieuBaoCT(entityParent, entityParent.getLstHoaDon().get(j));
                break;
            case "PhieuBao2":
                if (CLocal.serviceThermalPrinter != null)
                    CLocal.serviceThermalPrinter.printPhieuBao2(entityParent);
                break;
            case "TBDongNuoc":
                if (CLocal.serviceThermalPrinter != null)
                    CLocal.serviceThermalPrinter.printTBDongNuoc(entityParent);
                break;
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
//                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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
                                result = ws.XuLy_HoaDonDienTu("DangNgan", CLocal.MaNV, CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD(), CLocal.DateFormat.format(dateCapNhat), "", CLocal.listHanhThu.get(STT).getMaKQDN(), String.valueOf(XoaDCHD), cLocation.getCurrentLocation());
                                results = result.split(";");
                                if (Boolean.parseBoolean(results[0]) == true) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(true);
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setGiaiTrach(true);
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setNgayGiaiTrach(CLocal.DateFormat.format(dateCapNhat));
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setMaNV_DangNgan(CLocal.MaNV);
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setXoaDangNgan_Ngay_DienThoai("");
                                    if (CLocal.listHanhThuView.get(STT).isDCHD() == true && chkTienDu.isChecked() == false)
                                        CLocal.listHanhThuView.get(STT).setXoaDCHD(true);
                                    if (CLocal.serviceThermalPrinter != null)
                                        CLocal.serviceThermalPrinter.printThuTien(CLocal.listHanhThuView.get(STT), CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j));
                                }
                            } else
                                results = new String[]{"false", CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getKy() + " đã Thu Hộ hoặc Tạm Thu"};
                        if (chkPhiMoNuoc.isChecked() == true) {
                            result = ws.XuLy_HoaDonDienTu("DongPhi", CLocal.MaNV, "", CLocal.DateFormat.format(dateCapNhat), "", CLocal.listDongNuocView.get(STT).getMaKQDN(), String.valueOf(XoaDCHD), cLocation.getCurrentLocation());
                            results = result.split(";");
                            if (Boolean.parseBoolean(results[0]) == true) {
                                CLocal.listHanhThuView.get(STT).setDongPhi(true);
                                if (CLocal.serviceThermalPrinter != null)
                                    CLocal.serviceThermalPrinter.printPhiMoNuoc(CLocal.listHanhThuView.get(STT));
                            }
                        }
                        break;
                    case "PhieuBao":
                    case "PhieuBaoCT":
                        for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                            if ((CLocal.InPhieuBao == true || (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false))
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getInPhieuBao_Ngay().equals("") == true
                                    && selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                result = ws.XuLy_HoaDonDienTu("PhieuBao", CLocal.MaNV, CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD(), CLocal.DateFormat.format(dateCapNhat), "", CLocal.listHanhThuView.get(STT).getMaKQDN(), String.valueOf(XoaDCHD), cLocation.getCurrentLocation());
                                results = result.split(";");
                                if (Boolean.parseBoolean(results[0]) == true) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao_Ngay(CLocal.DateFormat.format(dateCapNhat));
                                    if (CLocal.serviceThermalPrinter != null)
                                        if (strings[0].equals("PhieuBao") == true)
                                            CLocal.serviceThermalPrinter.printPhieuBao(CLocal.listHanhThuView.get(STT), CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j));
                                        else if (strings[0].equals("PhieuBaoCT") == true)
                                            CLocal.serviceThermalPrinter.printPhieuBaoCT(CLocal.listHanhThuView.get(STT), CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j));
                                }
                            } else if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getInPhieuBao_Ngay().equals("") == false
                                    && selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                if (CLocal.serviceThermalPrinter != null)
                                    if (strings[0].equals("PhieuBao") == true)
                                        CLocal.serviceThermalPrinter.printPhieuBao(CLocal.listHanhThuView.get(STT), CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j));
                                    else if (strings[0].equals("PhieuBaoCT") == true)
                                        CLocal.serviceThermalPrinter.printPhieuBaoCT(CLocal.listHanhThuView.get(STT), CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j));
                            }
                        break;
                    case "PhieuBao2":
                        for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                            if ((CLocal.InPhieuBao == true || (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false))
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
                        result = ws.XuLy_HoaDonDienTu("PhieuBao2", CLocal.MaNV, MaHDs, CLocal.DateFormat.format(dateCapNhat), CLocal.DateFormat.format(dt), CLocal.listHanhThuView.get(STT).getMaKQDN(), String.valueOf(XoaDCHD), cLocation.getCurrentLocation());
                        results = result.split(";");
                        if (Boolean.parseBoolean(results[0]) == true) {
                            for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                                if (selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD())) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao2_Ngay(CLocal.DateFormat.format(dateCapNhat));
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao2_NgayHen(CLocal.DateFormat.format(dt));
                                }
                            if (CLocal.serviceThermalPrinter != null)
                                CLocal.serviceThermalPrinter.printPhieuBao2(CLocal.listHanhThuView.get(STT));
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
//                        c2.add(Calendar.DATE, Integer.parseInt(strings[1]));
                        c2 = CLocal.GetToDate(c2, Integer.parseInt(strings[1]));
                        dt2 = c2.getTime();
                        result = ws.XuLy_HoaDonDienTu("TBDongNuoc", CLocal.MaNV, MaHDs, CLocal.DateFormat.format(dateCapNhat), CLocal.DateFormat.format(dt2), CLocal.listHanhThuView.get(STT).getMaKQDN(), String.valueOf(XoaDCHD), cLocation.getCurrentLocation());
                        results = result.split(";");
                        if (Boolean.parseBoolean(results[0]) == true) {
                            for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                                if (selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD())) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setTBDongNuoc_Ngay(CLocal.DateFormat.format(dateCapNhat));
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setTBDongNuoc_NgayHen(CLocal.DateFormat.format(dt2));
                                }
//                            if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
//                                thermalPrinter.printTBDongNuoc(CLocal.listHanhThuView.get(STT));
                            if (CLocal.serviceThermalPrinter != null)
                                CLocal.serviceThermalPrinter.printTBDongNuoc(CLocal.listHanhThuView.get(STT));
                        }
                        break;
                    case "XoaDangNgan":
                        for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == true
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                    && selectedMaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD()) == true) {
                                result = ws.XuLy_HoaDonDienTu("XoaDangNgan", CLocal.MaNV, CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD(), CLocal.DateFormat.format(dateCapNhat), "", CLocal.listHanhThuView.get(STT).getMaKQDN(), String.valueOf(XoaDCHD), cLocation.getCurrentLocation());
                                results = result.split(";");
                                if (Boolean.parseBoolean(results[0]) == true) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(false);
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setGiaiTrach(false);
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setNgayGiaiTrach("");
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setMaNV_DangNgan("");
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setXoaDangNgan_Ngay_DienThoai(CLocal.DateFormat.format(dateCapNhat));
                                }
                            }
                        if (chkPhiMoNuoc.isChecked() == true) {
                            result = ws.XuLy_HoaDonDienTu("XoaDongPhi", CLocal.MaNV, "", CLocal.DateFormat.format(dateCapNhat), "", CLocal.listDongNuocView.get(STT).getMaKQDN(), String.valueOf(XoaDCHD), cLocation.getCurrentLocation());
                            results = result.split(";");
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
//                CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "THÀNH CÔNG","center");
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHoaDonDienTu_ThuTien.this);
                builder.setTitle("Thông Báo");
                builder.setMessage("THÀNH CÔNG")
                        .setCancelable(false)
                        .setNegativeButton("TRƯỚC", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                btnTruoc.performClick();
                            }
                        })
                        .setPositiveButton("SAU", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                btnSau.performClick();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                TextView textView = (TextView) alert.findViewById(android.R.id.message);
                textView.setTextSize(20);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setGravity(Gravity.CENTER);

                Button btnPositive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                layoutParams.gravity = Gravity.CENTER;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);
            } else
                CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "THẤT BẠI\n" + strings[1], "center");
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
                        result = ws.XuLy_HoaDonDienTu("DangNgan", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getNgayGiaiTrach(), "", CLocal.listHanhThuView.get(i).getMaKQDN(), String.valueOf(XoaDCHD), cLocation.getCurrentLocation());
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
                        result = ws.XuLy_HoaDonDienTu("PhieuBao", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getInPhieuBao_Ngay(), "", CLocal.listHanhThuView.get(i).getMaKQDN(), String.valueOf(XoaDCHD), cLocation.getCurrentLocation());
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
                        result = ws.XuLy_HoaDonDienTu("PhieuBao2", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getInPhieuBao2_Ngay(), CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getInPhieuBao2_NgayHen(), CLocal.listHanhThuView.get(i).getMaKQDN(), String.valueOf(XoaDCHD), cLocation.getCurrentLocation());
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
                        result = ws.XuLy_HoaDonDienTu("TBDongNuoc", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getTBDongNuoc_Ngay(), CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getTBDongNuoc_NgayHen(), CLocal.listHanhThuView.get(i).getMaKQDN(), String.valueOf(XoaDCHD), cLocation.getCurrentLocation());
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
                        result = ws.XuLy_HoaDonDienTu("XoaDangNgan", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getXoaDangNgan_Ngay_DienThoai(), "", CLocal.listHanhThuView.get(i).getMaKQDN(), String.valueOf(XoaDCHD), cLocation.getCurrentLocation());
                        break;
                }

                String[] results = result.split(";");

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

    public class MyAsyncTask_updateDiaChiDHN extends AsyncTask<String, Void, String[]> {
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
                result = ws.update_DiaChiDHN(CLocal.MaNV, edtDanhBo.getText().toString().replace(" ", ""), edtDiaChiDHN.getText().toString());
                results = result.split(";");
                return results;
            } catch (Exception ex) {
                return new String[]{"false;" + ex.getMessage()};
            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (Boolean.parseBoolean(strings[0]) == true) {
                CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "THÀNH CÔNG", "center");
            } else
                CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "THẤT BẠI\n" + strings[1], "center");
        }
    }
}
