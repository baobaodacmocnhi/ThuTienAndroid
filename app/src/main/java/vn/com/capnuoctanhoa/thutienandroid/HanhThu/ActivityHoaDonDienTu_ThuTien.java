package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.ActivitySearchKhachHangWeb;
import vn.com.capnuoctanhoa.thutienandroid.ActivitySettings;
import vn.com.capnuoctanhoa.thutienandroid.Bluetooth.ThermalPrinter;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.MainActivity;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityHoaDonDienTu_ThuTien extends AppCompatActivity {
    private TextView lbTinhTrang;
    private EditText edtMLT, edtDanhBo, edtHoTen, edtDiaChi, edtInPhieuBao_Ngay, edtInPhieuBao2_Ngay, edtInPhieuBao2_NgayHen, edtInTBDongNuoc_Ngay, edtInTBDongNuoc_NgayHen, edtSoNgayHen, edtPhiMoNuoc, edtTongCong;
    private ListView listView;
    private Button btnTruoc, btnSau, btnThuTien, btnPhieuBao, btnPhieuBao2, btnTBDongNuoc, btnXoa;
    private Integer STT;
    private ThermalPrinter thermalPrinter = null;
    private CWebservice ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_dien_tu_thu_tien);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lbTinhTrang = (TextView) findViewById(R.id.lbTinhTrang);
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
        edtTongCong = (EditText) findViewById(R.id.edtTongCong);
        listView = (ListView) findViewById(R.id.listView);
        btnTruoc = (Button) findViewById(R.id.btnTruoc);
        btnSau = (Button) findViewById(R.id.btnSau);
        btnThuTien = (Button) findViewById(R.id.btnThuTien);
        btnPhieuBao = (Button) findViewById(R.id.btnPhieuBao);
        btnPhieuBao2 = (Button) findViewById(R.id.btnPhieuBao2);
        btnTBDongNuoc = (Button) findViewById(R.id.btnTBDongNuoc);
        btnXoa = (Button) findViewById(R.id.btnXoa);

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
        }

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
        listView.setOnTouchListener(new View.OnTouchListener() {
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

        btnThuTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                        if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                            if (CLocal.SyncTrucTiep == true) {
                                if (CLocal.listHanhThuView.get(STT).isGiaiTrach() == false
                                        && CLocal.listHanhThuView.get(STT).isThuHo() == false
                                        && CLocal.listHanhThuView.get(STT).isTamThu() == false
                                        && CLocal.listHanhThuView.get(STT).isDangNgan_DienThoai() == false) {
                                    MyAsyncTask_XuLyTrucTiep myAsyncTask_xuLyTrucTiep = new MyAsyncTask_XuLyTrucTiep();
                                    myAsyncTask_xuLyTrucTiep.execute(new String[]{"DangNgan"});
                                }
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
                                }
                                CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Thành Công");
                                if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                    thermalPrinter.printThuTien(CLocal.listHanhThuView.get(STT));
                                btnSau.performClick();
                            }
                        }
                    }
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
                                }
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
                                }
                                CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Thành Công");
                                if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                    thermalPrinter.printPhieuBao(CLocal.listHanhThuView.get(STT));
                                btnSau.performClick();
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
                                    myAsyncTask_xuLyTrucTiep.execute(new String[]{"PhieuBao2",edtSoNgayHen.getText().toString()});
                                }
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
                                }
                                CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Thành Công");
                                if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                    thermalPrinter.printPhieuBao2(CLocal.listHanhThuView.get(STT));
                                btnSau.performClick();
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
                                    myAsyncTask_xuLyTrucTiep.execute(new String[]{"TBDongNuoc",edtSoNgayHen.getText().toString()});
                                }
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
                                }
                                CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, "Thành Công");
                                if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                    thermalPrinter.printTBDongNuoc(CLocal.listHanhThuView.get(STT));
                                btnSau.performClick();
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
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                                        if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                                            if (CLocal.SyncTrucTiep == true) {
                                                if (CLocal.listHanhThuView.get(STT).isGiaiTrach() == false
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
                                                }
                                            }
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
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
                    lbTinhTrang.setText(item.getTinhTrang());
                    edtMLT.setText(item.getMLT());
                    edtDanhBo.setText(item.getDanhBo());
                    edtHoTen.setText(item.getHoTen());
                    edtDiaChi.setText(item.getDiaChi());
                    edtInPhieuBao_Ngay.setText(item.getLstHoaDon().get(0).getInPhieuBao_Ngay());
                    edtInPhieuBao2_Ngay.setText(item.getLstHoaDon().get(0).getInPhieuBao2_Ngay());
                    edtInPhieuBao2_NgayHen.setText(item.getLstHoaDon().get(0).getInPhieuBao2_NgayHen());
                    edtInTBDongNuoc_Ngay.setText(item.getLstHoaDon().get(0).getTBDongNuoc_Ngay());
                    edtInTBDongNuoc_NgayHen.setText(item.getLstHoaDon().get(0).getTBDongNuoc_NgayHen());
                    Integer PhiMoNuoc = 0, TongCong = 0;
                    for (int j = 0; j < item.getLstHoaDon().size(); j++) {
                        TongCong += Integer.parseInt(item.getLstHoaDon().get(j).getTongCong());
                        arrayList.add(item.getLstHoaDon().get(j).getKy() + " : " + CLocal.formatMoney(item.getLstHoaDon().get(j).getTongCong(), "đ"));
                        PhiMoNuoc = Integer.parseInt(item.getLstHoaDon().get(j).getPhiMoNuoc());
                    }
                    TongCong += PhiMoNuoc;
                    edtPhiMoNuoc.setText(CLocal.formatMoney(PhiMoNuoc.toString(), "đ"));
                    edtTongCong.setText(CLocal.formatMoney(TongCong.toString(), "đ"));
                    if (item.isThuHo() == true || item.isTamThu() == true || item.isGiaiTrach() == true) {
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
                listView.setAdapter(arrayAdapter);
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
//        if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
//            thermalPrinter.disconnectBluetoothDevice();
        super.onDestroy();
    }

    public class MyAsyncTask_Thermal extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (thermalPrinter == null)
                    thermalPrinter = new ThermalPrinter(ActivityHoaDonDienTu_ThuTien.this);
            } catch (Exception ex) {
                CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
            }
            return null;
        }
    }

    public class MyAsyncTask_XuLyTrucTiep extends AsyncTask<String, Void, Boolean> {
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
        protected Boolean doInBackground(String... strings) {
            try {
                boolean result = false;
                String MaHDs = "";
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date dateCapNhat = new Date();
                switch (strings[0]) {
                    case "DangNgan":
                        for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false) {
                                result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("DangNgan", CLocal.MaNV, CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD(), currentDate.format(dateCapNhat), ""));
                                if (result == true) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(true);
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setNgayGiaiTrach(currentDate.format(dateCapNhat));
                                    if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                        thermalPrinter.printThuTien(CLocal.listHanhThuView.get(STT), CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j));
                                }
                            }
                        break;
                    case "PhieuBao":
                        for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getInPhieuBao_Ngay().equals("") == true) {
                                result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("PhieuBao", CLocal.MaNV, CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD(), currentDate.format(dateCapNhat), ""));
                                if (result == true) {
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
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getInPhieuBao2_Ngay().equals("") == true) {
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
                        result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("PhieuBao2", CLocal.MaNV, MaHDs, currentDate.format(dateCapNhat), currentDate.format(dt)));
                        if (result == true) {
                            CLocal.listHanhThuView.get(STT).getLstHoaDon().get(CLocal.listHanhThuView.get(STT).getLstHoaDon().size() - 1).setInPhieuBao2_Ngay(currentDate.format(dateCapNhat));
                            CLocal.listHanhThuView.get(STT).getLstHoaDon().get(CLocal.listHanhThuView.get(STT).getLstHoaDon().size() - 1).setInPhieuBao2_NgayHen(currentDate.format(dt));
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
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getTBDongNuoc_Ngay().equals("") == true) {
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
                        result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("TBDongNuoc", CLocal.MaNV, MaHDs, currentDate.format(dateCapNhat), currentDate.format(dt2)));
                        if (result == true) {
                            CLocal.listHanhThuView.get(STT).getLstHoaDon().get(CLocal.listHanhThuView.get(STT).getLstHoaDon().size() - 1).setTBDongNuoc_Ngay(currentDate.format(dateCapNhat));
                            CLocal.listHanhThuView.get(STT).getLstHoaDon().get(CLocal.listHanhThuView.get(STT).getLstHoaDon().size() - 1).setTBDongNuoc_NgayHen(currentDate.format(dt2));
                            if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
                                thermalPrinter.printTBDongNuoc(CLocal.listHanhThuView.get(STT));
                        }
                        break;
                    case "XoaDangNgan":
                        for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false) {
                                result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("XoaDangNgan", CLocal.MaNV, CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD(), currentDate.format(dateCapNhat), ""));
                                if (result == true) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(false);
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setNgayGiaiTrach("");
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setXoaDangNgan_Ngay_DienThoai(currentDate.format(dateCapNhat));
                                }
                            }
                        break;
                }
                CLocal.updateTinhTrangParent(CLocal.listHanhThuView, STT);
                CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(STT));
                return true;

            } catch (Exception ex) {
                CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (aBoolean == true) {
                CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "THÀNH CÔNG");
            } else
                CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "THẤT BẠI");
        }
    }

    public class MyAsyncTask_XuLyGianTiep extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                boolean result = false;
                String MaHDs = "";
                int i = Integer.parseInt(strings[1]);

                switch (strings[0]) {
                    case "DangNgan":
                        for (int j = 0; j < CLocal.listHanhThuView.get(i).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == true) {
                                if (MaHDs.equals("") == true)
                                    MaHDs += CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                                else
                                    MaHDs += "," + CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                            }
                        result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("DangNgan", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getNgayGiaiTrach(), ""));
                        break;
                    case "PhieuBao":
                        for (int j = 0; j < CLocal.listHanhThuView.get(i).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getInPhieuBao_Ngay().equals("") == false) {
                                if (MaHDs.equals("") == true)
                                    MaHDs += CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                                else
                                    MaHDs += "," + CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                            }
                        result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("PhieuBao", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getInPhieuBao_Ngay(), ""));
                        break;
                    case "PhieuBao2":
                        for (int j = 0; j < CLocal.listHanhThuView.get(i).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getInPhieuBao2_Ngay().equals("") == false) {
                                if (MaHDs.equals("") == true)
                                    MaHDs += CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                                else
                                    MaHDs += "," + CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                            }
                        result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("PhieuBao2", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getInPhieuBao2_Ngay(), CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getInPhieuBao2_NgayHen()));
                        break;
                    case "TBDongNuoc":
                        for (int j = 0; j < CLocal.listHanhThuView.get(i).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getTBDongNuoc_Ngay().equals("") == false) {
                                if (MaHDs.equals("") == true)
                                    MaHDs += CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                                else
                                    MaHDs += "," + CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                            }
                        result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("TBDongNuoc", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getTBDongNuoc_Ngay(), CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getTBDongNuoc_NgayHen()));
                        break;
                    case "XoaDangNgan":
                        for (int j = 0; j < CLocal.listHanhThuView.get(i).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getXoaDangNgan_Ngay_DienThoai().equals("") == false) {
                                if (MaHDs.equals("") == true)
                                    MaHDs += CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                                else
                                    MaHDs += "," + CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD();
                            }
                        result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("XoaDangNgan", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(CLocal.listHanhThuView.get(i).getLstHoaDon().size() - 1).getXoaDangNgan_Ngay_DienThoai(), ""));
                        break;
                }
                if (result == true) {
                    CLocal.listHanhThuView.get(i).setSync(false);
                    CLocal.listHanhThuView.get(i).setXuLy("");
                    CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(i));
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
