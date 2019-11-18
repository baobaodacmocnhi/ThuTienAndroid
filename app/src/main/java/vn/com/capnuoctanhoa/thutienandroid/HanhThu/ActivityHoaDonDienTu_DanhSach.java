package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityChild;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewChild;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CSort;
import vn.com.capnuoctanhoa.thutienandroid.Class.CustomAdapterExpandableListView;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityHoaDonDienTu_DanhSach extends AppCompatActivity {
    private Spinner spnFilter, spnSort, spnNhanVien;
    private ExpandableListView listView;
    private CustomAdapterExpandableListView customAdapterExpandableListView;
    private TextView txtTongHD, txtTongCong;
    private long TongDC, TongCong, TongHD;
    private ArrayList<CViewParent> listParent;
    private ArrayList<CViewChild> listChild;
    private FloatingActionButton floatingActionButton;

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
        listView = (ExpandableListView) findViewById(R.id.listView);
        txtTongHD = (TextView) findViewById(R.id.txtTongHD);
        txtTongCong = (TextView) findViewById(R.id.txtTongCong);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                TextView STT = (TextView) view.findViewById(R.id.lvSTT);
                int i=Integer.parseInt(STT.getText().toString()) - 1;
                Intent intent;
                intent = new Intent(getApplicationContext(), ActivityHoaDonDienTu_ThuTien.class);
                intent.putExtra("STT", String.valueOf(i));
                startActivity(intent);
                return false;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollToPosition(0);
            }
        });
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
    protected void onStart() {
        super.onStart();
        loadListView();
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
                            if (CLocal.listHanhThu.get(i).isDangNgan_DienThoai() == false && CLocal.listHanhThu.get(i).isGiaiTrach() == false && CLocal.listHanhThu.get(i).isTamThu() == false && CLocal.listHanhThu.get(i).isThuHo() == false) {
                                CLocal.listHanhThuView.add(CLocal.listHanhThu.get(i));
                                addViewParent(CLocal.listHanhThu.get(i));
                            }
                        }
                    }
                    break;
                case "Đã Thu":
                    if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                        for (int i = 0; i < CLocal.listHanhThu.size(); i++) {
                            if (CLocal.listHanhThu.get(i).isDangNgan_DienThoai() == true && CLocal.listHanhThu.get(i).isGiaiTrach() == false && CLocal.listHanhThu.get(i).isTamThu() == false && CLocal.listHanhThu.get(i).isThuHo() == false) {
                                CLocal.listHanhThuView.add(CLocal.listHanhThu.get(i));
                                addViewParent(CLocal.listHanhThu.get(i));
                            }
                        }
                    }
                    break;
                case "Tạm Thu-Thu Hộ":
                    if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                        for (int i = 0; i < CLocal.listHanhThu.size(); i++) {
                            if (CLocal.listHanhThu.get(i).isGiaiTrach() == true || CLocal.listHanhThu.get(i).isTamThu() == true || CLocal.listHanhThu.get(i).isThuHo() == true) {
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
            listView.setAdapter(customAdapterExpandableListView);
            txtTongHD.setText("ĐC:" + CLocal.formatMoney(String.valueOf(TongDC), "") + "- HĐ:" + CLocal.formatMoney(String.valueOf(TongHD), ""));
            txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));

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
}
