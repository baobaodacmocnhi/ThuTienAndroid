package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
    private EditText edtMLT, edtDanhBo, edtHoTen, edtDiaChi, edtInPhieuBao_Ngay, edtInPhieuBao2_Ngay, edtInPhieuBao2_NgayHen, edtInTBDongNuoc_Ngay, edtInTBDongNuoc_NgayHen, edtSoNgayHen, edtPhiMoNuoc, edtTongCong;
    private ListView listView;
    private Button btnTruoc, btnSau, btnThuTien, btnPhieuBao, btnPhieuBao2, btnTBDongNuoc, btnXoa;
    private Integer STT;
    private ThermalPrinter thermalPrinter;
    private CWebservice ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_dien_tu_thu_tien);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        btnThuTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                        if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date dateCapNhat = new Date();
                            for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++) {
                                if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                        && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                        && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                        && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(true);
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setNgayGiaiTrach(currentDate.format(dateCapNhat));
                                }
                            }
                            CLocal.listHanhThuView.get(STT).setSync(true);
                            CLocal.listHanhThuView.get(STT).setXuLy("DangNgan");
                            MyAsyncTask_XuLy myAsyncTask_xuLy = new MyAsyncTask_XuLy();
                            myAsyncTask_xuLy.execute(new String[]{"DangNgan", String.valueOf(STT)});
                            CLocal.updateTinhTrangParent(CLocal.listHanhThuView, STT);
                            CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(STT));

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    if (thermalPrinter != null)
                                        thermalPrinter.printThuTien(CLocal.listHanhThuView.get(STT));
                                }
                            }, 1000);
//                            btnSau.performClick();
                        }
                    }
                } catch (Exception ex) {

                }
            }
        });


        btnPhieuBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                        if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date dateCapNhat = new Date();
                            for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++) {
                                if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getInPhieuBao_Ngay().equals("") == true) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao_Ngay(currentDate.format(dateCapNhat));
                                }
                            }
                            CLocal.listHanhThuView.get(STT).setSync(true);
                            CLocal.listHanhThuView.get(STT).setXuLy("PhieuBao");
                            MyAsyncTask_XuLy myAsyncTask_xuLy = new MyAsyncTask_XuLy();
                            myAsyncTask_xuLy.execute(new String[]{"PhieuBao", String.valueOf(STT)});
                            CLocal.updateTinhTrangParent(CLocal.listHanhThuView, STT);
                            CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(STT));

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    if (thermalPrinter != null)
                                        thermalPrinter.printPhieuBao(CLocal.listHanhThuView.get(STT));
                                }
                            }, 1000);
//                            btnSau.performClick();
                        }
                    }
                } catch (Exception ex) {

                }
            }
        });

        btnPhieuBao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                        if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date dateCapNhat = new Date();
                            for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++) {
                                if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getInPhieuBao2_Ngay().equals("") == true) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao2_Ngay(currentDate.format(dateCapNhat));
                                    Date dt = dateCapNhat;
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(dt);
                                    c.add(Calendar.DATE, Integer.parseInt(edtSoNgayHen.getText().toString()));
                                    dt = c.getTime();
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setInPhieuBao2_NgayHen(currentDate.format(dt));
                                }
                            }
                            CLocal.listHanhThuView.get(STT).setSync(true);
                            CLocal.listHanhThuView.get(STT).setXuLy("PhieuBao2");
                            MyAsyncTask_XuLy myAsyncTask_xuLy = new MyAsyncTask_XuLy();
                            myAsyncTask_xuLy.execute(new String[]{"PhieuBao2", String.valueOf(STT)});
                            CLocal.updateTinhTrangParent(CLocal.listHanhThuView, STT);
                            CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(STT));

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    if (thermalPrinter != null)
                                        thermalPrinter.printPhieuBao2(CLocal.listHanhThuView.get(STT));
                                }
                            }, 1000);
//                            btnSau.performClick();
                        }
                    }
                } catch (Exception ex) {

                }
            }
        });

        btnTBDongNuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                        if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date dateCapNhat = new Date();
                            for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++) {
                                if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getTBDongNuoc_Ngay().equals("") == true) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setTBDongNuoc_Ngay(currentDate.format(dateCapNhat));
                                    Date dt = dateCapNhat;
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(dt);
                                    c.add(Calendar.DATE, Integer.parseInt(edtSoNgayHen.getText().toString()));
                                    dt = c.getTime();
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setTBDongNuoc_NgayHen(currentDate.format(dt));
                                }
                            }
                            CLocal.listHanhThuView.get(STT).setSync(true);
                            CLocal.listHanhThuView.get(STT).setXuLy("TBDongNuoc");
                            MyAsyncTask_XuLy myAsyncTask_xuLy = new MyAsyncTask_XuLy();
                            myAsyncTask_xuLy.execute(new String[]{"TBDongNuoc", String.valueOf(STT)});
                            CLocal.updateTinhTrangParent(CLocal.listHanhThuView, STT);
                            CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(STT));

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    if (thermalPrinter != null)
                                        thermalPrinter.printTBDongNuoc(CLocal.listHanhThuView.get(STT));
                                }
                            }, 1000);
//                            btnSau.performClick();
                        }
                    }
                } catch (Exception ex) {

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
                                            for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++) {
                                                CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setDangNgan_DienThoai(false);
                                            }
                                            CLocal.listHanhThuView.get(STT).setSync(true);
                                            CLocal.listHanhThuView.get(STT).setXuLy("XoaDangNgan");
                                            MyAsyncTask_XuLy myAsyncTask_xuLy = new MyAsyncTask_XuLy();
                                            myAsyncTask_xuLy.execute(new String[]{"XoaDangNgan", String.valueOf(STT)});
                                            CLocal.updateTinhTrangParent(CLocal.listHanhThuView, STT);
                                            CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(STT));
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
                    }
                }
            }
        } catch (Exception ex) {

        }
    }

    private void fillLayout(Integer STT) {
        try {
            if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                ArrayList<String> arrayList = new ArrayList<String>();
                if (STT >= 0 && STT < CLocal.listHanhThuView.size()) {
                    CEntityParent item = CLocal.listHanhThuView.get(STT);
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
        if (thermalPrinter != null)
            thermalPrinter.disconnectBluetoothDevice();
        super.onDestroy();
    }

    public class MyAsyncTask_Thermal extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                thermalPrinter = new ThermalPrinter(ActivityHoaDonDienTu_ThuTien.this);
            } catch (Exception ex) {
                CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
            }
            return null;
        }
    }

    public class MyAsyncTask_XuLy extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                boolean result = false;
                String MaHDs = "";
                int i = Integer.parseInt(strings[1]);
                for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                    if (MaHDs.equals("") == true)
                        MaHDs += CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD();
                    else
                        MaHDs += "," + CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD();
                switch (strings[0]) {
                    case "DangNgan":
                        result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("DangNgan", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(0).getNgayGiaiTrach(), ""));
                        break;
                    case "PhieuBao":
                        result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("PhieuBao", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(0).getInPhieuBao_Ngay(), ""));
                        break;
                    case "PhieuBao2":
                        result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("PhieuBao2", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(0).getInPhieuBao2_Ngay(), CLocal.listHanhThuView.get(i).getLstHoaDon().get(0).getInPhieuBao2_NgayHen()));
                        break;
                    case "TBDongNuoc":
                        result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("TBDongNuoc", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(0).getTBDongNuoc_Ngay(), CLocal.listHanhThuView.get(i).getLstHoaDon().get(0).getTBDongNuoc_NgayHen()));
                        break;
                    case "XoaDangNgan":
                        result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("XoaDangNgan", CLocal.MaNV, MaHDs, CLocal.listHanhThuView.get(i).getLstHoaDon().get(0).getXoaDangNgan_Ngay_DienThoai(), ""));
                        break;
                }
                if (result == true) {
                    CLocal.listHanhThuView.get(i).setSync(false);
                    CLocal.listHanhThuView.get(i).setXuLy("");
                    CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(i));
                }
                MyAsyncTask_XuLyTon myAsyncTask_xuLyTon = new MyAsyncTask_XuLyTon();
                myAsyncTask_xuLyTon.execute();
            } catch (Exception ex) {
                CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
            }
            return null;
        }
    }

    public class MyAsyncTask_XuLyTon extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = 0; i < CLocal.listHanhThuView.size(); i++)
                    if (CLocal.listHanhThuView.get(i).isSync() == true) {
                        MyAsyncTask_XuLy myAsyncTask_xuLy = new MyAsyncTask_XuLy();
                        myAsyncTask_xuLy.execute(new String[]{CLocal.listHanhThuView.get(i).getXuLy(), String.valueOf(i)});
                    }
            } catch (Exception ex) {
                CLocal.showToastMessage(ActivityHoaDonDienTu_ThuTien.this, ex.getMessage());
            }
            return null;
        }
    }

}
