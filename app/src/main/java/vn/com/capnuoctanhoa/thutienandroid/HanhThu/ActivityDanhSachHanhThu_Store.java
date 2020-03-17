package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.ActivitySearchKhachHangWeb;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CSort;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.Class.CustomAdapterRecyclerViewParent_LoadMore;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDanhSachHanhThu_Store extends AppCompatActivity {
    private Button btnDownload, btnShowMess;
    private Spinner spnFilter, spnSort, spnFromDot, spnToDot, spnNhanVien, spnNam, spnKy;
    private RecyclerView recyclerView;
    private TextView txtTongHD, txtTongCong;
    private long TongHD, TongCong;
    private CustomAdapterRecyclerViewParent_LoadMore customAdapterRecyclerViewParent;
    private ArrayList<CViewParent> lstOriginal, lstDisplayed;
    private LinearLayout layoutNhanVien;
    private CardView layoutAutoHide;
    private NestedScrollView nestedScrollView;
    private ArrayList<String> spnID_NhanVien, spnName_NhanVien;
    private String selectedMaNV = "";
    private FloatingActionButton floatingActionButton;
    private int layoutAutoHide_Height;
    //    private loadRecyclerViewAysncTask loadRecyclerViewAysncTask;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_hanh_thu);

        ///clear notifications
        NotificationManager notificationManager = (NotificationManager) ActivityDanhSachHanhThu_Store.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnShowMess = (Button) findViewById(R.id.btnShowMess);
        spnFilter = (Spinner) findViewById(R.id.spnFilter);
        spnSort = (Spinner) findViewById(R.id.spnSort);
        spnFromDot = (Spinner) findViewById(R.id.spnFromDot);
        spnToDot = (Spinner) findViewById(R.id.spnToDot);
        spnNhanVien = (Spinner) findViewById(R.id.spnNhanVien);
        spnNam = (Spinner) findViewById(R.id.spnNam);
        spnKy = (Spinner) findViewById(R.id.spnKy);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        txtTongHD = (TextView) findViewById(R.id.txtTongHD);
        txtTongCong = (TextView) findViewById(R.id.txtTongCong);
        layoutNhanVien = (LinearLayout) findViewById(R.id.layoutNhanVien);
//        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        layoutAutoHide = (CardView) findViewById(R.id.layoutAutoHide);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        if (CLocal.ToTruong == true) {
            layoutNhanVien.setVisibility(View.VISIBLE);
            try {
                if (CLocal.jsonNhanVien != null && CLocal.jsonNhanVien.length() > 0) {
                    spnID_NhanVien = new ArrayList<>();
                    spnName_NhanVien = new ArrayList<>();
                    for (int i = 0; i < CLocal.jsonNhanVien.length(); i++) {
                        JSONObject jsonObject = CLocal.jsonNhanVien.getJSONObject(i);
                        if (Boolean.parseBoolean(jsonObject.getString("HanhThu")) == true) {
                            spnID_NhanVien.add(jsonObject.getString("MaND"));
                            spnName_NhanVien.add(jsonObject.getString("HoTen"));
                        }
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityDanhSachHanhThu_Store.this, android.R.layout.simple_spinner_item, spnName_NhanVien);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnNhanVien.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            layoutNhanVien.setVisibility(View.GONE);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CLocal.checkNetworkAvailable(ActivityDanhSachHanhThu_Store.this) == false) {
                    Toast.makeText(ActivityDanhSachHanhThu_Store.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });

        btnShowMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ActivityDanhSachHanhThu_Store.this);
                builderSingle.setIcon(R.mipmap.ic_launcher);
                builderSingle.setTitle("Tin nhắn đã nhận");
                builderSingle.setCancelable(false);

                ListView lstMessage = new ListView(ActivityDanhSachHanhThu_Store.this);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ActivityDanhSachHanhThu_Store.this, android.R.layout.select_dialog_singlechoice);

                try {
                    if (CLocal.jsonMessage != null && CLocal.jsonMessage.length() > 0) {
                        int stt = 0;
                        for (int i = 0; i < CLocal.jsonMessage.length(); i++) {
                            JSONObject jsonObject = CLocal.jsonMessage.getJSONObject(i);
                            arrayAdapter.add(jsonObject.getString("NgayNhan") + " - " + jsonObject.getString("Title") + " - " + jsonObject.getString("Content"));
                        }
                    }
                } catch (Exception ex) {
                }

                lstMessage.setAdapter(arrayAdapter);
                builderSingle.setView(lstMessage);

                builderSingle.setNegativeButton(
                        "Thoát",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setPositiveButton(
                        "Xóa Tất Cả",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CLocal.jsonMessage = new JSONArray();
                            }
                        });

                //hàm này khi click row sẽ bị ẩn
                /*builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(ActivityDanhSachHanhThu3.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });*/

                final Dialog dialog = builderSingle.create();
                builderSingle.show();
            }
        });

        spnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (loadRecyclerViewAysncTask != null && loadRecyclerViewAysncTask.getStatus() == AsyncTask.Status.FINISHED) {
//                    // My AsyncTask is currently doing work in doInBackground()
//                    loadRecyclerView();
//                }
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
                if (customAdapterRecyclerViewParent != null)
                    customAdapterRecyclerViewParent.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnNhanVien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMaNV = spnID_NhanVien.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        layoutAutoHide_Height = layoutAutoHide.getHeight();
//        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                int scrollY = nestedScrollView.getScrollY();
//                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layoutAutoHide.getLayoutParams();
//                int height = Math.max(0, layoutAutoHide_Height - scrollY);
//                lp.height = height;
//                layoutAutoHide.setLayoutParams(lp);
//            }
//        });

        //if you remove this part, the card would be shown in its minimum state at start
//        recyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                nestedScrollView.scrollTo(0, 0);
//            }
//        });
//
//        // The calculation for heights of views should be done after the view created
//        final View rootView = findViewById(R.id.layoutRoot);
//        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    public void onGlobalLayout() {
//                        //Remove the listener before proceeding
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                        } else {
//                            rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                        }
//                        // measure your views here
//                        layoutAutoHide_Height = layoutAutoHide.getHeight();
//                        nestedScrollView.scrollTo(0, 0);
//                    }
//                });
//
//        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                if (nestedScrollView.getScrollY() == 0 || nestedScrollView.getScrollY() < layoutAutoHide_Height) {
//                    floatingActionButton.hide();
//                } else {
//                    floatingActionButton.show();
//                }
//            }
//        });

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                    if(scrollY==0||scrollY<layoutAutoHide_Height) {
//                        floatingActionButton.hide();
//                    }
//                    else {
//                        floatingActionButton.show();
//                    }
//                }
//            });
//        }

//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nestedScrollView.fling(0);
//                nestedScrollView.smoothScrollTo(0, 0);
//            }
//        });

//    nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//        @Override
//        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//            if(v.getChildAt(v.getChildCount() - 1) != null) {
//                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
//                        scrollY > oldScrollY) {
//                  Toast.makeText(getApplicationContext(),"bottom",Toast.LENGTH_SHORT).show();
//                    //set load more listener for the RecyclerView adapter
//                    recyclerView.setNestedScrollingEnabled(true);
//                }
//            }
//        }
//    });

    }


    @Override
    protected void onStart() {
        super.onStart();
//        loadRecyclerView();
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
//                customAdapterRecyclerViewParent.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapterRecyclerViewParent.getFilter().filter(newText);
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
            case R.id.action_search_khach_hang:
                Intent intent = new Intent(ActivityDanhSachHanhThu_Store.this, ActivitySearchKhachHangWeb.class);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadListView() {
        try {
//            recyclerView.setAdapter(null);
            lstOriginal = new ArrayList<CViewParent>();
            lstDisplayed = new ArrayList<CViewParent>();
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

            for (int i = 0; i < lstOriginal.size(); i++) {
                lstDisplayed.add(lstOriginal.get(i));
                if (i == 9)
                    break;
            }

            customAdapterRecyclerViewParent = new CustomAdapterRecyclerViewParent_LoadMore(this, lstDisplayed);

            //set load more listener for the RecyclerView adapter
            customAdapterRecyclerViewParent.setOnLoadMoreListener(new CustomAdapterRecyclerViewParent_LoadMore.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            int index = lstDisplayed.size() ;
                            loadMore(index);
                        }
                    });
                    nestedScrollView.invalidate();
                    recyclerView.setNestedScrollingEnabled(false);
                }

            });
            recyclerView.setHasFixedSize(true);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            recyclerView.addItemDecoration(new CVerticalLineDecorator(2));
            recyclerView.setAdapter(customAdapterRecyclerViewParent);

        } catch (Exception e) {
        }
    }

    private void loadMore(final int index) {
        //add loading progress view
        lstDisplayed.add(null);
        customAdapterRecyclerViewParent.notifyItemInserted(lstDisplayed.size() - 1);
        //remove loading view
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 1000);
        lstDisplayed.remove(lstDisplayed.size() - 1);
        customAdapterRecyclerViewParent.notifyItemRemoved(lstDisplayed.size());
        if (lstDisplayed.size() == lstOriginal.size()) {
            customAdapterRecyclerViewParent.setMoreDataAvailable(false);
            //telling adapter to stop calling load more as no more server data available
            Toast.makeText(getApplicationContext(), "No More Data Available", Toast.LENGTH_LONG).show();
        } else {
            int end = index + 10;
            for (int i = index; i < end; i++) {
                lstDisplayed.add(lstOriginal.get(i));
            }
        }
        customAdapterRecyclerViewParent.notifyDataChanged();
    }

//    public void loadRecyclerView() {
//        loadRecyclerViewAysncTask = new loadRecyclerViewAysncTask();
//        loadRecyclerViewAysncTask.execute();
//    }

    public void addEntity(JSONObject jsonObject) {
        try {
            ///thiết lập khởi tạo 1 lần đầu để sort
            if (jsonObject.has("ModifyDate") == false)
                jsonObject.put("ModifyDate", CLocal.DateFormat.format(new Date()));
            CViewParent entity = new CViewParent();
            entity.setSTT(String.valueOf(lstOriginal.size() + 1));
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

            lstOriginal.add(entity);
        } catch (Exception e) {
        }
    }


    public class MyAsyncTask extends AsyncTask<Void, String, Void> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityDanhSachHanhThu_Store.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
//            publishProgress(ws.getDSHoaDonTon(CLocal.sharedPreferencesre.getString("selectedMaNV", ""), currentDate.format(new Date())));
            try {
                if (CLocal.ToTruong == false)
                    selectedMaNV = CLocal.MaNV;
                CLocal.jsonHanhThu = new JSONArray(ws.getDSHoaDonTon(selectedMaNV, spnNam.getSelectedItem().toString(), spnKy.getSelectedItem().toString(), spnFromDot.getSelectedItem().toString(), spnToDot.getSelectedItem().toString()));
                SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                if (CLocal.jsonHanhThu != null)
                    editor.putString("jsonHanhThu", CLocal.jsonHanhThu.toString());
                editor.commit();
                publishProgress("true");
            } catch (Exception ex) {
                publishProgress("false");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                try {
                    if (Boolean.parseBoolean(values[0]) == true) {
                        loadListView();
                    }
                } catch (Exception e) {
                    CLocal.jsonHanhThu = null;
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

//    public class loadRecyclerViewAysncTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
////            recyclerView.setAdapter(null);
//                lstDisplayed = new ArrayList<CViewParent>();
//                TongHD = TongCong = 0;
//                switch (spnFilter.getSelectedItem().toString()) {
//                    case "Chưa Thu":
//                    case "Đã Thu":
//                        if (CLocal.jsonHanhThu != null && CLocal.jsonHanhThu.length() > 0) {
//                            int stt = 0;
//                            for (int i = 0; i < CLocal.jsonHanhThu.length(); i++) {
//                                JSONObject jsonObject = CLocal.jsonHanhThu.getJSONObject(i);
//                                if (Boolean.parseBoolean(jsonObject.getString("GiaiTrach")) == false) {
//                                    addEntity(jsonObject);
//                                }
//                            }
//                        }
//                        break;
//                    case "Giải Trách":
//                        if (CLocal.jsonHanhThu != null && CLocal.jsonHanhThu.length() > 0) {
//                            int stt = 0;
//                            for (int i = 0; i < CLocal.jsonHanhThu.length(); i++) {
//                                JSONObject jsonObject = CLocal.jsonHanhThu.getJSONObject(i);
//                                if (Boolean.parseBoolean(jsonObject.getString("GiaiTrach")) == true) {
//                                    addEntity(jsonObject);
//                                }
//                            }
//                        }
//                        break;
//                    case "Tạm Thu-Thu Hộ":
//                        if (CLocal.jsonHanhThu != null && CLocal.jsonHanhThu.length() > 0) {
//                            int stt = 0;
//                            for (int i = 0; i < CLocal.jsonHanhThu.length(); i++) {
//                                JSONObject jsonObject = CLocal.jsonHanhThu.getJSONObject(i);
//                                if (Boolean.parseBoolean(jsonObject.getString("ThuHo")) == true || Boolean.parseBoolean(jsonObject.getString("TamThu")) == true) {
//                                    addEntity(jsonObject);
//                                }
//                            }
//                        }
//                        break;
//                    default:
//                        if (CLocal.jsonHanhThu != null && CLocal.jsonHanhThu.length() > 0) {
//                            for (int i = 0; i < CLocal.jsonHanhThu.length(); i++) {
//                                JSONObject jsonObject = CLocal.jsonHanhThu.getJSONObject(i);
//                                addEntity(jsonObject);
//                            }
//                        }
//                        break;
//                }
//            } catch (Exception e) {
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            customAdapterRecyclerViewParent = new CustomAdapterRecyclerViewParent_LoadMore(ActivityDanhSachHanhThu.this,recyclerView, lstDisplayed);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setHasFixedSize(true);
//            recyclerView.setAdapter(customAdapterRecyclerViewParent);
//            txtTongHD.setText(CLocal.formatMoney(String.valueOf(TongHD), ""));
//            txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
//        }
//    }
}
