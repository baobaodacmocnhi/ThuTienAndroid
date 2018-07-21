package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CSort;
import vn.com.capnuoctanhoa.thutienandroid.Class.CustomAdapterListView;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDanhSachHanhThu extends AppCompatActivity {
    private Spinner spnFilter, spnSort;
    private ListView listView;
    private CustomAdapterListView customAdapterListView;
    private TextView txtTongHD, txtTongCong;
    private long TongHD, TongCong;
    private ArrayList<CEntityParent>  lstDisplayed;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_hanh_thu);

        ///clear notifications
        NotificationManager notificationManager = (NotificationManager) ActivityDanhSachHanhThu.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spnFilter = (Spinner) findViewById(R.id.spnFilter);
        spnSort = (Spinner) findViewById(R.id.spnSort);
        listView = (ListView) findViewById(R.id.listView);
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
                if (lstDisplayed != null && lstDisplayed.size() > 0)
                    switch (spnSort.getSelectedItem().toString()) {
                        case "Thời Gian Tăng":
                            Collections.sort(lstDisplayed, new CSort("ModifyDate", -1));
                            break;
                        case "Thời Gian Giảm":
                            Collections.sort(lstDisplayed, new CSort("ModifyDate", 1));
                            break;
                        default:
                            Collections.sort(lstDisplayed, new CSort("", -1));
                            break;
                    }
                if (customAdapterListView != null)
                    customAdapterListView.notifyDataSetChanged();
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
                if(firstVisibleItem>=1)
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

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollToPosition(0);
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
//                customAdapterListView.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapterListView.getFilter().filter(newText);
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
                Intent intent=new Intent(getApplicationContext(),ActivityDownDataHanhThu.class);
                startActivityForResult(intent,1);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadListView() {
        try {
            lstDisplayed = new ArrayList<CEntityParent>();
            TongHD = TongCong = 0;
            switch (spnFilter.getSelectedItem().toString()) {
                case "Chưa Thu":
                case "Đã Thu":
                    if (CLocal.jsonHanhThu != null && CLocal.jsonHanhThu.length() > 0) {
                        int stt = 0;
                        for (int i = 0; i < CLocal.jsonHanhThu.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonHanhThu.getJSONObject(i);
                            if (Boolean.parseBoolean(jsonObject.getString("GiaiTrach")) == false) {
                                addEntity(jsonObject);
                            }
                        }
                    }
                    break;
                case "Giải Trách":
                    if (CLocal.jsonHanhThu != null && CLocal.jsonHanhThu.length() > 0) {
                        int stt = 0;
                        for (int i = 0; i < CLocal.jsonHanhThu.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonHanhThu.getJSONObject(i);
                            if (Boolean.parseBoolean(jsonObject.getString("GiaiTrach")) == true) {
                                addEntity(jsonObject);
                            }
                        }
                    }
                    break;
                case "Tạm Thu-Thu Hộ":
                    if (CLocal.jsonHanhThu != null && CLocal.jsonHanhThu.length() > 0) {
                        int stt = 0;
                        for (int i = 0; i < CLocal.jsonHanhThu.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonHanhThu.getJSONObject(i);
                            if (Boolean.parseBoolean(jsonObject.getString("ThuHo")) == true || Boolean.parseBoolean(jsonObject.getString("TamThu")) == true) {
                                addEntity(jsonObject);
                            }
                        }
                    }
                    break;
                default:
                    if (CLocal.jsonHanhThu != null && CLocal.jsonHanhThu.length() > 0) {
                        for (int i = 0; i < CLocal.jsonHanhThu.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonHanhThu.getJSONObject(i);
                            addEntity(jsonObject);
                        }
                    }
                    break;
            }
            txtTongHD.setText(CLocal.formatMoney(String.valueOf(TongHD), ""));
            txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));

            customAdapterListView = new CustomAdapterListView(this, lstDisplayed);
            listView.setAdapter(customAdapterListView);

        } catch (Exception e) {
        }
    }

    public void addEntity(JSONObject jsonObject) {
        try {
            ///thiết lập khởi tạo 1 lần đầu để sort
            if (jsonObject.has("ModifyDate") == false)
                jsonObject.put("ModifyDate", CLocal.DateFormat.format(new Date()));
            CEntityParent entity = new CEntityParent();
            entity.setSTT(String.valueOf(lstDisplayed.size() + 1));
            entity.setID(jsonObject.getString("ID"));

            String strMLT = new StringBuffer(jsonObject.getString("MLT")).insert(4, " ").insert(2, " ").toString();
            entity.setRow1a(strMLT);
            entity.setRow1b(jsonObject.getString("Ky") + ": " + CLocal.formatMoney(jsonObject.getString("TongCong"), "đ"));

            String strDanhBo = new StringBuffer(jsonObject.getString("DanhBo")).insert(7, " ").insert(4, " ").toString();
            entity.setRow2a(strDanhBo);
            if (Boolean.parseBoolean(jsonObject.getString("GiaiTrach")) == true)
                entity.setRow2b("Giải Trách");
            else if (Boolean.parseBoolean(jsonObject.getString("TamThu")) == true)
                entity.setRow2b("Tạm Thu");
            else if (Boolean.parseBoolean(jsonObject.getString("ThuHo")) == true)
                entity.setRow2b("Thu Hộ");

            entity.setRow3a(jsonObject.getString("HoTen"));

            entity.setRow4a(jsonObject.getString("DiaChi"));

            entity.setGiaiTrach(Boolean.parseBoolean(jsonObject.getString("GiaiTrach")));
            entity.setTamThu(Boolean.parseBoolean(jsonObject.getString("TamThu")));
            entity.setThuHo(Boolean.parseBoolean(jsonObject.getString("ThuHo")));
            entity.setModifyDate(jsonObject.getString("ModifyDate"));
            TongHD++;
            TongCong += Long.parseLong(jsonObject.getString("TongCong"));

            lstDisplayed.add(entity);
        } catch (Exception e) {
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode== Activity.RESULT_OK)
                loadListView();
        }
    }
}
