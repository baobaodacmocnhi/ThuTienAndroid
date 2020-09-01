package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityChild;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CSort;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewChild;
import vn.com.capnuoctanhoa.thutienandroid.Class.CustomAdapterExpandableListView;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDanhSachDongNuoc extends AppCompatActivity {
    private Spinner spnFilter, spnSort, spnNhanVien;
    private ExpandableListView lstView;
    private CustomAdapterExpandableListView customAdapterExpandableListView;
    private TextView txtTongHD, txtTongCong;
    private long TongDC, TongCong, TongHD;
    private ArrayList<CViewParent> listParent;
    private ArrayList<CViewChild> listChild;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_dong_nuoc);

        ///clear notifications
        NotificationManager notificationManager = (NotificationManager) ActivityDanhSachDongNuoc.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spnFilter = (Spinner) findViewById(R.id.spnFilter);
        spnSort = (Spinner) findViewById(R.id.spnSort);
        spnNhanVien = (Spinner) findViewById(R.id.spnNhanVien);
        lstView = (ExpandableListView) findViewById(R.id.lstView);
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
                PopupMenu popup = new PopupMenu(getApplicationContext(), view);
                popup.getMenuInflater().inflate(R.menu.menu_dongnuoc, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        TextView STT = (TextView) view.findViewById(R.id.lvSTT);
                        int i=Integer.parseInt(STT.getText().toString()) - 1;
                        CLocal.indexPosition = i;
                        Intent intent;
                        switch (id) {
                            case R.id.action_DongNuoc1:
                                intent = new Intent(getApplicationContext(), ActivityDongNuoc.class);
                                intent.putExtra("STT", String.valueOf(i));
                                startActivity(intent);
                                break;
                            case R.id.action_DongNuoc2:
                                intent = new Intent(getApplicationContext(), ActivityDongNuoc2.class);
                                intent.putExtra("STT", String.valueOf(i));
                                startActivity(intent);
                                break;
                            case R.id.action_MoNuoc:
                                intent = new Intent(getApplicationContext(), ActivityMoNuoc.class);
                                intent.putExtra("STT", String.valueOf(i));
                                startActivity(intent);
                                break;
                            case R.id.action_DongTien:
                                intent = new Intent(getApplicationContext(), ActivityDongTien.class);
                                intent.putExtra("STT",String.valueOf(i));
                                startActivity(intent);
                                break;
                            case R.id.action_PhieuBao2:
                                if (CLocal.serviceThermalPrinter != null)
                                    CLocal.serviceThermalPrinter.printPhieuBao2(CLocal.listDongNuocView.get(i));
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
                return false;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                Intent intent = new Intent(getApplicationContext(), ActivityDownDataDongNuoc.class);
                startActivityForResult(intent, 1);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadListView() {
        try {
//            recyclerView.setAdapter(null);
            listParent = new ArrayList<CViewParent>();
            CLocal.listDongNuocView = new ArrayList<CEntityParent>();
            TongDC = TongCong = TongHD = 0;
            switch (spnFilter.getSelectedItem().toString()) {
                case "Chưa ĐN":
                    if (CLocal.listDongNuoc != null && CLocal.listDongNuoc.size() > 0) {
                        for (int i = 0; i < CLocal.listDongNuoc.size(); i++) {
                            if (CLocal.listDongNuoc.get(i).isDongNuoc() == false && CLocal.listDongNuoc.get(i).isGiaiTrach() == false) {
                                CLocal.listDongNuocView.add(CLocal.listDongNuoc.get(i));
                                addEntityParent(CLocal.listDongNuoc.get(i));
                            }
                        }
                    }
                    break;
                case "Đã ĐN":
                    if (CLocal.listDongNuoc != null && CLocal.listDongNuoc.size() > 0) {
                        for (int i = 0; i < CLocal.listDongNuoc.size(); i++) {
                            if (CLocal.listDongNuoc.get(i).isDongNuoc() == true) {
                                CLocal.listDongNuocView.add(CLocal.listDongNuoc.get(i));
                                addEntityParent(CLocal.listDongNuoc.get(i));
                            }
                        }
                    }
                    break;
                case "Chưa MN":
                    if (CLocal.listDongNuoc != null && CLocal.listDongNuoc.size() > 0) {
                        for (int i = 0; i < CLocal.listDongNuoc.size(); i++) {
                            if (CLocal.listDongNuoc.get(i).isMoNuoc() == false && CLocal.listDongNuoc.get(i).getLstHoaDon().get(0).getPhiMoNuocThuHo().equals("") == true) {
                                CLocal.listDongNuocView.add(CLocal.listDongNuoc.get(i));
                                addEntityParent(CLocal.listDongNuoc.get(i));
                            }
                        }
                    }
                    break;
                case "Đã MN":
                    if (CLocal.listDongNuoc != null && CLocal.listDongNuoc.size() > 0) {
                        for (int i = 0; i < CLocal.listDongNuoc.size(); i++) {
                            if (CLocal.listDongNuoc.get(i).isMoNuoc() == true) {
                                CLocal.listDongNuocView.add(CLocal.listDongNuoc.get(i));
                                addEntityParent(CLocal.listDongNuoc.get(i));
                            }
                        }
                    }
                    break;
                case "Chưa Thu":
                    if (CLocal.listDongNuoc != null && CLocal.listDongNuoc.size() > 0) {
                        for (int i = 0; i < CLocal.listDongNuoc.size(); i++) {
                            if (CLocal.listDongNuoc.get(i).isGiaiTrach() == false) {
                                CLocal.listDongNuocView.add(CLocal.listDongNuoc.get(i));
                                addEntityParent(CLocal.listDongNuoc.get(i));
                            }
                        }
                    }
                    break;
                case "Đã Thu":
                    if (CLocal.listDongNuoc != null && CLocal.listDongNuoc.size() > 0) {
                        for (int i = 0; i < CLocal.listDongNuoc.size(); i++) {
                            if (CLocal.listDongNuoc.get(i).isDangNgan_DienThoai() == true) {
                                CLocal.listDongNuocView.add(CLocal.listDongNuoc.get(i));
                                addEntityParent(CLocal.listDongNuoc.get(i));
                            }
                        }
                    }
                    break;
                case "Giải Trách":
                    if (CLocal.listDongNuoc != null && CLocal.listDongNuoc.size() > 0) {
                        for (int i = 0; i < CLocal.listDongNuoc.size(); i++) {
                            if (CLocal.listDongNuoc.get(i).isGiaiTrach() == true) {
                                CLocal.listDongNuocView.add(CLocal.listDongNuoc.get(i));
                                addEntityParent(CLocal.listDongNuoc.get(i));
                            }
                        }
                    }
                    break;
                case "Tạm Thu-Thu Hộ":
                    if (CLocal.listDongNuoc != null && CLocal.listDongNuoc.size() > 0) {
                        for (int i = 0; i < CLocal.listDongNuoc.size(); i++) {
                            if (CLocal.listDongNuoc.get(i).isThuHo() == true || CLocal.listDongNuoc.get(i).isTamThu() == true) {
                                CLocal.listDongNuocView.add(CLocal.listDongNuoc.get(i));
                                addEntityParent(CLocal.listDongNuoc.get(i));
                            }
                        }
                    }
                    break;
                default:
                    if (CLocal.listDongNuoc != null && CLocal.listDongNuoc.size() > 0) {
                        for (int i = 0; i < CLocal.listDongNuoc.size(); i++) {
                            CLocal.listDongNuocView.add(CLocal.listDongNuoc.get(i));
                            addEntityParent(CLocal.listDongNuoc.get(i));
                        }
                    }
                    break;
            }
            customAdapterExpandableListView = new CustomAdapterExpandableListView(this, listParent,"DongNuoc");
            lstView.setAdapter(customAdapterExpandableListView);
            txtTongHD.setText("HĐ:" + CLocal.formatMoney(String.valueOf(TongHD), "")+"- ĐC:" + CLocal.formatMoney(String.valueOf(TongDC), ""));
            txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
            lstView.setSelection(CLocal.indexPosition);
        } catch (Exception e) {

        }
    }

    public void addEntityParent(CEntityParent enParent) {
        try {
            CViewParent enViewParent = new CViewParent();
            enViewParent.setModifyDate(enParent.getModifyDate());
            enViewParent.setSTT(String.valueOf(listParent.size() + 1));
            enViewParent.setID(String.valueOf(enParent.getID()));

            enViewParent.setRow1a(enParent.getMLT());
            enViewParent.setRow2a(enParent.getDanhBo());
            enViewParent.setRow3a(enParent.getHoTen());
            enViewParent.setRow4a(enParent.getDiaChi());

            enViewParent.setGiaiTrach(enParent.isGiaiTrach());
            enViewParent.setTamThu(enParent.isTamThu());
            enViewParent.setThuHo(enParent.isThuHo());
            enViewParent.setLenhHuy(enParent.isLenhHuy());

            ///////////////////////////

            listChild = new ArrayList<CViewChild>();
            Integer TongCongChild = 0, numGiaiTrach = 0, numTamThu = 0, numThuHo = 0, numLenhHuy = 0;
            Boolean flagPhiMoNuoc = false;
            for (int i = 0; i < enParent.getLstHoaDon().size(); i++) {
                addEntityChild(enParent.getLstHoaDon().get(i));
                ///cập nhật parent
                if(enParent.getLstHoaDon().get(i).getTongCong().equals("null")==false)
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

    public void addEntityChild(CEntityChild enChild) {
        try {
            CViewChild enViewChild = new CViewChild();
            enViewChild.setID(enChild.getMaHD());
            enViewChild.setRow1a(enChild.getKy());
            enViewChild.setRow1b(CLocal.formatMoney(enChild.getTongCong(), "đ"));
            enViewChild.setGiaiTrach(enChild.isGiaiTrach());
            enViewChild.setTamThu(enChild.isTamThu());
            enViewChild.setThuHo(enChild.isThuHo());
            enViewChild.setLenhHuy(enChild.isLenhHuy());
            TongCong += Long.parseLong(enChild.getTongCong());
            TongHD++;

            listChild.add(enViewChild);
        } catch (Exception e) {
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
