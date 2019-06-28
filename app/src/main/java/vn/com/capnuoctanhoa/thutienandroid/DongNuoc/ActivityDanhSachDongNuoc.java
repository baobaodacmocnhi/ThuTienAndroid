package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.ActivitySearchKhachHangWeb;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CSort;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityChild;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.Class.CustomAdapterExpandableListView;
import vn.com.capnuoctanhoa.thutienandroid.Class.CustomAdapterListView;
import vn.com.capnuoctanhoa.thutienandroid.Class.CustomAdapterRecyclerViewParent;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDanhSachDongNuoc extends AppCompatActivity {
    private Spinner spnFilter, spnSort, spnNhanVien;
    private ExpandableListView listView;
    private CustomAdapterExpandableListView customAdapterExpandableListView;
    private TextView txtTongHD, txtTongCong;
    private long TongDC, TongCong, TongHD;
    private ArrayList<CEntityParent> listParent;
    private ArrayList<CEntityChild> listChild;


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
                PopupMenu popup = new PopupMenu(getApplicationContext(), view);
                popup.getMenuInflater().inflate(R.menu.popup_dong_nuoc, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        TextView MaDN = (TextView) view.findViewById(R.id.lvID);
                        ;
                        Intent intent;
                        switch (id) {
                            case R.id.popup_action_DongNuoc:
                                intent = new Intent(getApplicationContext(), ActivityDongNuoc.class);
                                intent.putExtra("MaDN", MaDN.getText().toString());
                                startActivity(intent);
                                break;
                            case R.id.popup_action_DongNuoc2:
                                intent = new Intent(getApplicationContext(), ActivityDongNuoc2.class);
                                intent.putExtra("MaDN", MaDN.getText().toString());
                                startActivity(intent);
                                break;
                            case R.id.popup_action_MoNuoc:
                                intent = new Intent(getApplicationContext(), ActivityMoNuoc.class);
                                intent.putExtra("MaDN", MaDN.getText().toString());
                                startActivity(intent);
                                break;
                            case R.id.popup_action_DongTien:
                                intent = new Intent(getApplicationContext(), ActivityDongTien.class);
                                intent.putExtra("MaDN", MaDN.getText().toString());
                                startActivity(intent);
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
            listParent = new ArrayList<CEntityParent>();
            TongDC = TongCong = TongHD = 0;
            switch (spnFilter.getSelectedItem().toString()) {
                case "Chưa ĐN":
                    if (CLocal.jsonDongNuoc != null && CLocal.jsonDongNuoc.length() > 0) {
                        int stt = 0;
                        for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonDongNuoc.getJSONObject(i);
                            if (jsonObject.getString("DongNuoc") != "null" && Boolean.parseBoolean(jsonObject.getString("DongNuoc")) == false && Boolean.parseBoolean(jsonObject.getString("NgayGiaiTrach")) == false) {
                                addEntityParent(jsonObject);
                            }
                        }
                    }
                    break;
                case "Đã ĐN":
                    if (CLocal.jsonDongNuoc != null && CLocal.jsonDongNuoc.length() > 0) {
                        int stt = 0;
                        for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonDongNuoc.getJSONObject(i);
                            if (jsonObject.getString("DongNuoc") != "null" && Boolean.parseBoolean(jsonObject.getString("DongNuoc")) == true) {
                                addEntityParent(jsonObject);
                            }
                        }
                    }
                    break;
                case "Chưa MN":
                    if (CLocal.jsonDongNuoc != null && CLocal.jsonDongNuoc.length() > 0) {
                        int stt = 0;
                        for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonDongNuoc.getJSONObject(i);
                            if (jsonObject.getString("MoNuoc") != "null" && Boolean.parseBoolean(jsonObject.getString("MoNuoc")) == false && Boolean.parseBoolean(jsonObject.getString("DongPhi")) == true) {
                                addEntityParent(jsonObject);
                            }
                        }
                    }
                    break;
                case "Đã MN":
                    if (CLocal.jsonDongNuoc != null && CLocal.jsonDongNuoc.length() > 0) {
                        int stt = 0;
                        for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonDongNuoc.getJSONObject(i);
                            if (jsonObject.getString("MoNuoc") != "null" && Boolean.parseBoolean(jsonObject.getString("MoNuoc")) == true) {
                                addEntityParent(jsonObject);
                            }
                        }
                    }
                    break;
                case "Giải Trách":
                    if (CLocal.jsonDongNuoc != null && CLocal.jsonDongNuoc.length() > 0) {
                        int stt = 0;
                        for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonDongNuoc.getJSONObject(i);
                            if (Boolean.parseBoolean(jsonObject.getString("GiaiTrach")) == true && jsonObject.getString("DongPhi") != "null" && Boolean.parseBoolean(jsonObject.getString("DongPhi")) == false) {
                                addEntityParent(jsonObject);
                            }
                        }
                    }
                    break;
                case "Tạm Thu-Thu Hộ":
                    if (CLocal.jsonDongNuoc != null && CLocal.jsonDongNuoc.length() > 0) {
                        int stt = 0;
                        for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonDongNuoc.getJSONObject(i);
                            if (Boolean.parseBoolean(jsonObject.getString("ThuHo")) == true || Boolean.parseBoolean(jsonObject.getString("TamThu")) == true) {
                                addEntityParent(jsonObject);
                            }
                        }
                    }
                    break;
                default:
                    if (CLocal.jsonDongNuoc != null && CLocal.jsonDongNuoc.length() > 0) {
                        for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonDongNuoc.getJSONObject(i);
                            addEntityParent(jsonObject);
                        }
                    }
                    break;
            }
            customAdapterExpandableListView = new CustomAdapterExpandableListView(this, listParent);
            listView.setAdapter(customAdapterExpandableListView);
            txtTongHD.setText(CLocal.formatMoney(String.valueOf(TongDC), ""));
            txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongHD), "") + "HĐ: " + CLocal.formatMoney(String.valueOf(TongCong), "đ"));
        } catch (Exception e) {

        }
    }

    public void addEntityParent(JSONObject jsonObject) {
        try {
            ///thiết lập khởi tạo 1 lần đầu để sort
            if (jsonObject.has("ModifyDate") == false)
                jsonObject.put("ModifyDate", CLocal.DateFormat.format(new Date()));

            CEntityParent entity = new CEntityParent();
            entity.setSTT(String.valueOf(listParent.size() + 1));
            entity.setID(jsonObject.getString("ID"));

            String strMLT = new StringBuffer(jsonObject.getString("MLT")).insert(4, " ").insert(2, " ").toString();
            entity.setRow1a(strMLT);

            String strDanhBo = new StringBuffer(jsonObject.getString("DanhBo")).insert(7, " ").insert(4, " ").toString();
            entity.setRow2a(strDanhBo);

            entity.setRow3a(jsonObject.getString("HoTen"));

            entity.setRow4a(jsonObject.getString("DiaChi"));

            entity.setGiaiTrach(Boolean.parseBoolean(jsonObject.getString("GiaiTrach")));
            entity.setTamThu(Boolean.parseBoolean(jsonObject.getString("TamThu")));
            entity.setThuHo(Boolean.parseBoolean(jsonObject.getString("ThuHo")));
            entity.setLenhHuy(Boolean.parseBoolean(jsonObject.getString("LenhHuy")));
            entity.setModifyDate(jsonObject.getString("ModifyDate"));
            ///////////////////////////

            listChild = new ArrayList<CEntityChild>();

            if (CLocal.jsonDongNuocChild != null && CLocal.jsonDongNuocChild.length() > 0)
                for (int i = 0; i < CLocal.jsonDongNuocChild.length(); i++) {
                    JSONObject jsonObjectChild = CLocal.jsonDongNuocChild.getJSONObject(i);
                    Integer numRowChild = 0, numGiaiTrach = 0, numTamThu = 0, numThuHo = 0, numLenhHuy = 0;
                    Boolean flagPhiMoNuoc = false;
                    if (jsonObjectChild.getString("ID").equals(entity.getID()) == true) {
                        addEntityChild(jsonObjectChild);
                        ///cập nhật parent
                        numRowChild++;
                        if (Boolean.parseBoolean(jsonObjectChild.getString("GiaiTrach")) == true)
                            numGiaiTrach++;
                        else if (Boolean.parseBoolean(jsonObjectChild.getString("TamThu")) == true)
                            numTamThu++;
                        else if (Boolean.parseBoolean(jsonObjectChild.getString("ThuHo")) == true) {
                            numThuHo++;
                            if (jsonObjectChild.getString("PhiMoNuoc") != "null"&&jsonObjectChild.getString("PhiMoNuoc").equals("0")==false) {
                                flagPhiMoNuoc = true;
                            }
                        }
                        ///xét lệnh hủy riêng
                        if (Boolean.parseBoolean(jsonObjectChild.getString("LenhHuy")) == true)
                            numLenhHuy++;

                        if (numGiaiTrach == numRowChild) {
                            CLocal.updateJSON(CLocal.jsonDongNuoc, entity.getID(), "GiaiTrach", "true");
                            entity.setRow2b("Giải Trách");
                            entity.setGiaiTrach(true);
                        } else if (numTamThu == numRowChild) {
                            CLocal.updateJSON(CLocal.jsonDongNuoc, entity.getID(), "TamThu", "true");
                            entity.setRow2b("Tạm Thu");
                            entity.setTamThu(true);
                        } else if (numThuHo == numRowChild) {
                            CLocal.updateJSON(CLocal.jsonDongNuoc, entity.getID(), "ThuHo", "true");
                            String str = "Thu Hộ";
                            if (flagPhiMoNuoc == true)
                                str += " ("+jsonObjectChild.getString("PhiMoNuoc").substring(0,jsonObjectChild.getString("PhiMoNuoc").length()-3)+"k)";
                            entity.setRow2b(str);
                            entity.setThuHo(true);
                        }
                        ///xét lệnh hủy riêng
                        if (numLenhHuy == numRowChild) {
                            CLocal.updateJSON(CLocal.jsonDongNuoc, entity.getID(), "LenhHuy", "true");
                            entity.setLenhHuy(true);
                        }
                    }
                }
            entity.setListChild(listChild);
            entity.setRow1b(String.valueOf(listChild.size()) + " HĐ");
            TongDC++;

            listParent.add(entity);
        } catch (Exception e) {
        }
    }

    public void addEntityChild(JSONObject jsonObject) {
        try {
            ///thiết lập khởi tạo 1 lần đầu để sort
            if (jsonObject.has("ModifyDate") == false)
                jsonObject.put("ModifyDate", CLocal.DateFormat.format(new Date()));

            CEntityChild entity = new CEntityChild();
            entity.setID(jsonObject.getString("MaHD"));
            entity.setRow1a(jsonObject.getString("Ky"));
            entity.setRow1b(CLocal.formatMoney(jsonObject.getString("TongCong"), "đ"));
            entity.setGiaiTrach(Boolean.parseBoolean(jsonObject.getString("GiaiTrach")));
            entity.setTamThu(Boolean.parseBoolean(jsonObject.getString("TamThu")));
            entity.setThuHo(Boolean.parseBoolean(jsonObject.getString("ThuHo")));
            entity.setLenhHuy(Boolean.parseBoolean(jsonObject.getString("LenhHuy")));
            TongCong += Long.parseLong(jsonObject.getString("TongCong"));
            TongHD++;

            listChild.add(entity);
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
