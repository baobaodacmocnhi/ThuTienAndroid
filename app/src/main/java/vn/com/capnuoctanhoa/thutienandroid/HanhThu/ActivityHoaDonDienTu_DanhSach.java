package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityChild;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewChild;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CSort;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.Class.CustomAdapterExpandableListView;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityHoaDonDienTu_DanhSach extends AppCompatActivity {
    private Spinner spnFilter, spnSort, spnNhanVien;
    private ExpandableListView lstView;
    private CustomAdapterExpandableListView customAdapterExpandableListView;
    private TextView txtTongHD, txtTongCong;
    private long TongDC, TongCong, TongHD;
    private ArrayList<CViewParent> listParent;
    private ArrayList<CViewChild> listChild;
    private FloatingActionButton floatingActionButton;
    private ImageView imgviewThuTienHD0, imgviewThongKe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_dien_tu_danh_sach);

        ///clear notifications
        NotificationManager notificationManager = (NotificationManager) ActivityHoaDonDienTu_DanhSach.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spnFilter = (Spinner) findViewById(R.id.spnFilter);
        spnSort = (Spinner) findViewById(R.id.spnSort);
        spnNhanVien = (Spinner) findViewById(R.id.spnNhanVien);
        lstView = (ExpandableListView) findViewById(R.id.lstView);
        txtTongHD = (TextView) findViewById(R.id.txtTongHD);
        txtTongCong = (TextView) findViewById(R.id.txtTongCong);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        imgviewThuTienHD0 = (ImageView) findViewById(R.id.imgviewThuTienHD0);
        imgviewThongKe = (ImageView) findViewById(R.id.imgviewThongKe);

        imgviewThuTienHD0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHoaDonDienTu_DanhSach.this);
                    builder.setMessage("Bạn có chắc chắn Thu Tiền HĐ=0?")
                            .setCancelable(false)
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                                        MyAsyncTask_XuLyTrucTiep_HD0 myAsyncTask_xuLyTrucTiep_hd0 = new MyAsyncTask_XuLyTrucTiep_HD0();
                                        myAsyncTask_xuLyTrucTiep_hd0.execute();
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
                    CLocal.showPopupMessage(ActivityHoaDonDienTu_DanhSach.this, ex.getMessage(), "left");
                }
            }
        });

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
                            if (CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == true && CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getMaNV_DangNgan() == CLocal.MaNV) {
                                TongHDDaThu++;
                                TongCongDaThu += Long.parseLong(CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getTongCong());
                            }
                            //tồn
                            if ((CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == false || CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getMaNV_DangNgan() != CLocal.MaNV) && CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isGiaiTrach() == false && CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isTamThu() == false && CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isThuHo() == false) {
                                TongHDTon++;
                                TongCongTon += Long.parseLong(CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getTongCong());
                            }
                        }
                    }
                }
                CLocal.showPopupMessage(ActivityHoaDonDienTu_DanhSach.this, "Tổng: " + CLocal.formatMoney(String.valueOf(TongHD), "hđ") + " = " + CLocal.formatMoney(String.valueOf(TongCong), "đ")
                        + "\n\nThu Hộ: " + CLocal.formatMoney(String.valueOf(TongHDThuHo), "hđ") + " = " + CLocal.formatMoney(String.valueOf(TongCongThuHo), "đ")
                        + "\n\nĐã Thu: " + CLocal.formatMoney(String.valueOf(TongHDDaThu), "hđ") + " = " + CLocal.formatMoney(String.valueOf(TongCongDaThu), "đ")
                        + "\n\nTồn: " + CLocal.formatMoney(String.valueOf(TongHDTon), "hđ") + " = " + CLocal.formatMoney(String.valueOf(TongCongTon), "đ"), "right");
            }
        });

        spnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (listParent != null && listParent.size() > 0)
                    switch (spnSort.getSelectedItem().toString()) {
                        case "Thời Gian Tăng":
                            Collections.sort(listParent, new CSort("ModifyDate", -1));
                            break;
                        case "Thời Gian Giảm":
                            Collections.sort(listParent, new CSort("ModifyDate", 1));
                            break;
                        default:
                            Collections.sort(listParent, new CSort("", -1));
                            break;
                    }
                if (customAdapterExpandableListView != null)
                    customAdapterExpandableListView.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lstView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            private int mLastFirstVisibleItem;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 1)
                    floatingActionButton.show();
                else
                    floatingActionButton.hide();
//                if(mLastFirstVisibleItem<firstVisibleItem)
//                {
//                    floatingActionButton.show();
//                }
//                if(mLastFirstVisibleItem>firstVisibleItem)
//                {
//                    floatingActionButton.hide();
//                }
//                mLastFirstVisibleItem=firstVisibleItem;
            }
        });

        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                TextView STT = (TextView) view.findViewById(R.id.lvSTT);
                int i = Integer.parseInt(STT.getText().toString()) - 1;
                CLocal.indexPosition = i;
                Intent intent;
                intent = new Intent(getApplicationContext(), ActivityHoaDonDienTu_ThuTien.class);
                intent.putExtra("STT", String.valueOf(i));
//                startActivity(intent);
                startActivityForResult(intent, 1);
                return false;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstView.smoothScrollToPosition(0);
            }
        });

        loadListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                cViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapterExpandableListView.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_down_data:
                Intent intent = new Intent(getApplicationContext(), ActivityDownDataHanhThu.class);
                intent.putExtra("LoaiDownData", "HoaDonDienTu");
                startActivityForResult(intent, 1);
                return true;
            case R.id.action_sync_data:
                if (CLocal.SyncTrucTiep == false) {
                    MyAsyncTask_XuLyTon myAsyncTask_xuLyTon = new MyAsyncTask_XuLyTon();
                } else
                    CLocal.showPopupMessage(ActivityHoaDonDienTu_DanhSach.this, "Tính Năng này không hoạt động khi Đồng Bộ Trực Tiếp", "center");
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadListView() {
        try {
            listParent = new ArrayList<CViewParent>();
            CLocal.listHanhThuView = new ArrayList<CEntityParent>();
            TongDC = TongCong = TongHD = 0;
            switch (spnFilter.getSelectedItem().toString()) {
                case "Chưa Thu":
                    if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                        for (int i = 0; i < CLocal.listHanhThu.size(); i++) {
                            if (CLocal.listHanhThu.get(i).isDangNgan_DienThoai() == false && CLocal.listHanhThu.get(i).isGiaiTrach() == false && CLocal.listHanhThu.get(i).isTamThu() == false && CLocal.listHanhThu.get(i).isThuHo() == false && CLocal.listHanhThu.get(i).isDongA() == false) {
                                CLocal.listHanhThuView.add(CLocal.listHanhThu.get(i));
                                addViewParent(CLocal.listHanhThu.get(i));
                            }
                        }
                    }
                    break;
                case "Đã Thu":
                    if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                        for (int i = 0; i < CLocal.listHanhThu.size(); i++) {
                            if (CLocal.listHanhThu.get(i).isDangNgan_DienThoai() == true) {
                                CLocal.listHanhThuView.add(CLocal.listHanhThu.get(i));
                                addViewParent(CLocal.listHanhThu.get(i));
                            }
                        }
                    }
                    break;
                case "Tạm Thu-Thu Hộ":
                    if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                        for (int i = 0; i < CLocal.listHanhThu.size(); i++) {
                            if ((CLocal.listHanhThu.get(i).isGiaiTrach() == true && CLocal.listHanhThu.get(i).isDangNgan_DienThoai() == false) || CLocal.listHanhThu.get(i).isTamThu() == true || CLocal.listHanhThu.get(i).isThuHo() == true || CLocal.listHanhThu.get(i).isDongA() == true) {
                                CLocal.listHanhThuView.add(CLocal.listHanhThu.get(i));
                                addViewParent(CLocal.listHanhThu.get(i));
                            }
                        }
                    }
                    break;
                case "In Phiếu Báo":
                    if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                        for (int i = 0; i < CLocal.listHanhThu.size(); i++) {
                            if (CLocal.listHanhThu.get(i).getLstHoaDon().get(0).getInPhieuBao_Ngay().equals("") == false || CLocal.listHanhThu.get(i).getLstHoaDon().get(0).getInPhieuBao2_Ngay().equals("") == false) {
                                CLocal.listHanhThuView.add(CLocal.listHanhThu.get(i));
                                addViewParent(CLocal.listHanhThu.get(i));
                            }
                        }
                    }
                    break;
                case "In TB Đóng Nước":
                    if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                        for (int i = 0; i < CLocal.listHanhThu.size(); i++) {
                            if (CLocal.listHanhThu.get(i).getLstHoaDon().get(0).getTBDongNuoc_Ngay().equals("") == false) {
                                CLocal.listHanhThuView.add(CLocal.listHanhThu.get(i));
                                addViewParent(CLocal.listHanhThu.get(i));
                            }
                        }
                    }
                    break;
                case "Xóa Đăng Ngân":
                    if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                        for (int i = 0; i < CLocal.listHanhThu.size(); i++) {
                            if (CLocal.listHanhThu.get(i).getLstHoaDon().get(0).getXoaDangNgan_Ngay_DienThoai().equals("") == false) {
                                CLocal.listHanhThuView.add(CLocal.listHanhThu.get(i));
                                addViewParent(CLocal.listHanhThu.get(i));
                            }
                        }
                    }
                    break;
                case ">= 2 HĐ":
                    if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                        for (int i = 0; i < CLocal.listHanhThu.size(); i++) {
                            if (CLocal.listHanhThu.get(i).isTamThu() == false && CLocal.listHanhThu.get(i).isThuHo() == false && CLocal.listHanhThu.get(i).getLstHoaDon().size() >= 2) {
                                CLocal.listHanhThuView.add(CLocal.listHanhThu.get(i));
                                addViewParent(CLocal.listHanhThu.get(i));
                            }
                        }
                    }
                    break;
                case "HĐ = 0":
                    if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                        for (int i = 0; i < CLocal.listHanhThu.size(); i++) {
                            boolean flag = false;
                            for (int j = 0; j < CLocal.listHanhThu.get(i).getLstHoaDon().size(); j++)
                                if (Integer.parseInt(CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getTieuThu()) == 0) {
                                    flag = true;
                                }
                            if (flag == true) {
                                CLocal.listHanhThuView.add(CLocal.listHanhThu.get(i));
                                addViewParent(CLocal.listHanhThu.get(i));
                            }
                        }
                    }
                    break;
                case "HĐ Giấy":
                    if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                        for (int i = 0; i < CLocal.listHanhThu.size(); i++) {
                            boolean flag = false;
                            for (int j = 0; j < CLocal.listHanhThu.get(i).getLstHoaDon().size(); j++) {
                                String[] Kys = CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getKy().split("/");
                                if (Integer.parseInt(Kys[1]) < 2020 || (Integer.parseInt(Kys[1]) == 2020 && Integer.parseInt(Kys[0]) <= 6))
                                    flag = true;
                            }
                            if (flag == true) {
                                CLocal.listHanhThuView.add(CLocal.listHanhThu.get(i));
                                addViewParent(CLocal.listHanhThu.get(i));
                            }
                        }
                    }
                    break;
                default:
                    if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                        for (int i = 0; i < CLocal.listHanhThu.size(); i++) {
                            CLocal.listHanhThuView.add(CLocal.listHanhThu.get(i));
                            addViewParent(CLocal.listHanhThu.get(i));
                        }
                    }
                    break;
            }
            customAdapterExpandableListView = new CustomAdapterExpandableListView(this, listParent);
            lstView.setAdapter(customAdapterExpandableListView);
            txtTongHD.setText("HĐ:" + CLocal.formatMoney(String.valueOf(TongHD), "") + "- ĐC:" + CLocal.formatMoney(String.valueOf(TongDC), ""));
            txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
            lstView.setSelection(CLocal.indexPosition);
        } catch (Exception ex) {
            CLocal.showToastMessage(ActivityHoaDonDienTu_DanhSach.this, ex.getMessage());
        }
    }

    public void addViewParent(CEntityParent enParent) {
        try {
            CViewParent enViewParent = new CViewParent();
            enViewParent.setModifyDate(enParent.getModifyDate());
            enViewParent.setSTT(String.valueOf(listParent.size() + 1));
            enViewParent.setID(String.valueOf(enParent.getID()));

            enViewParent.setRow1a(enParent.getMLT());
            enViewParent.setRow2a(enParent.getDanhBo());
            enViewParent.setRow3a(enParent.getHoTen());
            enViewParent.setRow4a(enParent.getDiaChi());

            enViewParent.setThuHo(enParent.isThuHo());
            enViewParent.setTamThu(enParent.isTamThu());
            enViewParent.setGiaiTrach(enParent.isGiaiTrach());
            enViewParent.setTBDongNuoc(enParent.isTBDongNuoc());
            enViewParent.setLenhHuy(enParent.isLenhHuy());

            ///////////////////////////

            listChild = new ArrayList<CViewChild>();
            Integer TongCongChild = 0;
            for (int i = 0; i < enParent.getLstHoaDon().size(); i++) {
                addViewChild(enParent.getLstHoaDon().get(i));
                ///cập nhật parent
                TongCongChild += Integer.parseInt(enParent.getLstHoaDon().get(i).getTongCong());
            }

            enViewParent.setListChild(listChild);
            enViewParent.setRow1b(String.valueOf(listChild.size()) + " HĐ: " + CLocal.formatMoney(TongCongChild.toString(), "đ"));
            enViewParent.setRow2b(enParent.getTinhTrang());
            TongDC++;

            listParent.add(enViewParent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addViewChild(CEntityChild enChild) {
        try {
            CViewChild enViewChild = new CViewChild();
            enViewChild.setID(enChild.getMaHD());
            enViewChild.setRow1a(enChild.getKy());
            enViewChild.setRow1b(CLocal.formatMoney(enChild.getTongCong(), "đ"));
            enViewChild.setGiaiTrach(enChild.isGiaiTrach());
            enViewChild.setTamThu(enChild.isTamThu());
            enViewChild.setThuHo(enChild.isThuHo());
            enViewChild.setTBDongNuoc(enChild.isTBDongNuoc());
            enViewChild.setLenhHuy(enChild.isLenhHuy());
            TongCong += Long.parseLong(enChild.getTongCong());
            TongHD++;

            listChild.add(enViewChild);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK)
                loadListView();
        }
    }

    public class MyAsyncTask_XuLyTrucTiep_HD0 extends AsyncTask<String, Void, String[]> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityHoaDonDienTu_DanhSach.this);
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
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date dateCapNhat = new Date();
                for (int i = 0; i < CLocal.listHanhThuView.size(); i++) {
                    for (int j = 0; j < CLocal.listHanhThuView.get(i).getLstHoaDon().size(); j++)
                        if (CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).isGiaiTrach() == false
                                && CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).isThuHo() == false
                                && CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).isTamThu() == false
                                && CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                && Integer.parseInt(CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getTieuThu()) == 0) {
                            result = ws.XuLy_HoaDonDienTu("DangNgan", CLocal.MaNV, CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD(), currentDate.format(dateCapNhat), "", CLocal.listHanhThu.get(i).getMaKQDN(), "false");
                            results = result.split(";");
                            if (Boolean.parseBoolean(results[0]) == true) {
                                CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).setDangNgan_DienThoai(true);
                                CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).setGiaiTrach(true);
                                CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).setNgayGiaiTrach(currentDate.format(dateCapNhat));
                                CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).setMaNV_DangNgan(CLocal.MaNV);
                                CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).setXoaDangNgan_Ngay_DienThoai("");
                            }
                        }

                    if (Boolean.parseBoolean(results[0]) == false && results.length == 5) {
                        CLocal.updateValueChild(CLocal.listHanhThu, results[2], results[3], results[4]);
                        CLocal.updateValueChild(CLocal.listHanhThuView, results[2], results[3], results[4]);
                    }
                    CLocal.updateTinhTrangParent(CLocal.listHanhThuView, i);
                    CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(i));
                }
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
                CLocal.showPopupMessage(ActivityHoaDonDienTu_DanhSach.this, "THÀNH CÔNG", "center");
            } else
                CLocal.showPopupMessage(ActivityHoaDonDienTu_DanhSach.this, "THẤT BẠI\n" + strings[1], "center");
        }
    }

    public class MyAsyncTask_XuLyTon extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityHoaDonDienTu_DanhSach.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = 0; i < CLocal.listHanhThu.size(); i++)
                    if (CLocal.listHanhThu.get(i).isSync() == true) {
                        boolean result = false;
                        String MaHDs = "";
                        Boolean XoaDCHD = false;
                        switch (CLocal.listHanhThu.get(i).getXuLy()) {
                            case "DangNgan":
                                for (int j = 0; j < CLocal.listHanhThu.get(i).getLstHoaDon().size(); j++)
                                    if (CLocal.listHanhThu.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == true) {
                                        if (MaHDs.equals("") == true)
                                            MaHDs += CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getMaHD();
                                        else
                                            MaHDs += "," + CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getMaHD();
                                    }
                                XoaDCHD = false;
                                if (CLocal.listHanhThuView.get(i).isDCHD() == true && CLocal.listHanhThuView.get(i).isXoaDCHD() == false)
                                    XoaDCHD = true;
                                result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("DangNgan", CLocal.MaNV, MaHDs, CLocal.listHanhThu.get(i).getLstHoaDon().get(CLocal.listHanhThu.get(i).getLstHoaDon().size() - 1).getNgayGiaiTrach(), "", CLocal.listHanhThu.get(i).getMaKQDN(), XoaDCHD.toString()));
                                break;
                            case "PhieuBao":
                                for (int j = 0; j < CLocal.listHanhThu.get(i).getLstHoaDon().size(); j++)
                                    if (CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getInPhieuBao_Ngay().equals("") == false) {
                                        if (MaHDs.equals("") == true)
                                            MaHDs += CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getMaHD();
                                        else
                                            MaHDs += "," + CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getMaHD();
                                    }
                                result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("PhieuBao", CLocal.MaNV, MaHDs, CLocal.listHanhThu.get(i).getLstHoaDon().get(CLocal.listHanhThu.get(i).getLstHoaDon().size() - 1).getInPhieuBao_Ngay(), "", CLocal.listHanhThu.get(i).getMaKQDN(), XoaDCHD.toString()));
                                break;
                            case "PhieuBao2":
                                for (int j = 0; j < CLocal.listHanhThu.get(i).getLstHoaDon().size(); j++)
                                    if (CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getInPhieuBao2_Ngay().equals("") == false) {
                                        if (MaHDs.equals("") == true)
                                            MaHDs += CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getMaHD();
                                        else
                                            MaHDs += "," + CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getMaHD();
                                    }
                                result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("PhieuBao2", CLocal.MaNV, MaHDs, CLocal.listHanhThu.get(i).getLstHoaDon().get(CLocal.listHanhThu.get(i).getLstHoaDon().size() - 1).getInPhieuBao2_Ngay(), CLocal.listHanhThu.get(i).getLstHoaDon().get(CLocal.listHanhThu.get(i).getLstHoaDon().size() - 1).getInPhieuBao2_NgayHen(), CLocal.listHanhThu.get(i).getMaKQDN(), XoaDCHD.toString()));
                                break;
                            case "TBDongNuoc":
                                for (int j = 0; j < CLocal.listHanhThu.get(i).getLstHoaDon().size(); j++)
                                    if (CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getTBDongNuoc_Ngay().equals("") == false) {
                                        if (MaHDs.equals("") == true)
                                            MaHDs += CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getMaHD();
                                        else
                                            MaHDs += "," + CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getMaHD();
                                    }
                                result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("TBDongNuoc", CLocal.MaNV, MaHDs, CLocal.listHanhThu.get(i).getLstHoaDon().get(CLocal.listHanhThu.get(i).getLstHoaDon().size() - 1).getTBDongNuoc_Ngay(), CLocal.listHanhThu.get(i).getLstHoaDon().get(CLocal.listHanhThu.get(i).getLstHoaDon().size() - 1).getTBDongNuoc_NgayHen(), CLocal.listHanhThu.get(i).getMaKQDN(), XoaDCHD.toString()));
                                break;
                            case "XoaDangNgan":
                                for (int j = 0; j < CLocal.listHanhThu.get(i).getLstHoaDon().size(); j++)
                                    if (CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getXoaDangNgan_Ngay_DienThoai().equals("") == false) {
                                        if (MaHDs.equals("") == true)
                                            MaHDs += CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getMaHD();
                                        else
                                            MaHDs += "," + CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getMaHD();
                                    }
                                result = Boolean.parseBoolean(ws.XuLy_HoaDonDienTu("XoaDangNgan", CLocal.MaNV, MaHDs, CLocal.listHanhThu.get(i).getLstHoaDon().get(CLocal.listHanhThu.get(i).getLstHoaDon().size() - 1).getXoaDangNgan_Ngay_DienThoai(), "", CLocal.listHanhThu.get(i).getMaKQDN(), XoaDCHD.toString()));
                                break;
                        }
                        if (result == true) {
                            CLocal.listHanhThu.get(i).setSync(false);
                            CLocal.listHanhThu.get(i).setXuLy("");
//                            CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(i));
                        }
                    }
            } catch (Exception ex) {
                CLocal.showToastMessage(ActivityHoaDonDienTu_DanhSach.this, ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            super.onPostExecute(aVoid);
        }
    }
}
