package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

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
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CSort;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewAdapter;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewEntity;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDanhSachHanhThu extends AppCompatActivity {
    private Button btnDownload, btnShowMess;
    private Spinner spnFilter, spnSort,spnFromDot, spnToDot,spnNhanVien,spnNam,spnKy;
    private ListView lstView;
    private TextView txtTongHD, txtTongCong;
    private long TongHD, TongCong;
    private CViewAdapter cViewAdapter;
    private ArrayList<CViewEntity> list;
    private LinearLayout layoutNhanVien;
    private ConstraintLayout layoutAutoHide;
    private ArrayList<String> spnID_NhanVien,spnName_NhanVien;
    private String selectedMaNV ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_hanh_thu);

        ///clear notifications
        NotificationManager notificationManager= (NotificationManager) ActivityDanhSachHanhThu.this.getSystemService(Context.NOTIFICATION_SERVICE);
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
        lstView = (ListView) findViewById(R.id.lstView);
        txtTongHD = (TextView) findViewById(R.id.txtTongHD);
        txtTongCong = (TextView) findViewById(R.id.txtTongCong);
        layoutNhanVien = (LinearLayout) findViewById(R.id.layoutNhanVien);
        layoutAutoHide=(ConstraintLayout) findViewById(R.id.layoutAutoHide);

        if (CLocal.ToTruong == true) {
            layoutNhanVien.setVisibility(View.VISIBLE);
            try {
                if (CLocal.jsonNhanVien != null && CLocal.jsonNhanVien.length() > 0) {
                    spnID_NhanVien=new ArrayList<>();
                    spnName_NhanVien=new ArrayList<>();
                    for (int i = 0; i < CLocal.jsonNhanVien.length(); i++) {
                        JSONObject jsonObject = CLocal.jsonNhanVien.getJSONObject(i);
                        if(Boolean.parseBoolean(jsonObject.getString("HanhThu"))==true) {
                            spnID_NhanVien.add(jsonObject.getString("MaND"));
                            spnName_NhanVien.add(jsonObject.getString("HoTen"));
                        }
                    }
                }
                ArrayAdapter<String> adapter =new ArrayAdapter<String>(ActivityDanhSachHanhThu.this,android.R.layout.simple_spinner_item, spnName_NhanVien);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnNhanVien.setAdapter(adapter);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
            layoutNhanVien.setVisibility(View.GONE);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CLocal.checkNetworkAvailable(getApplicationContext()) == false) {
                    Toast.makeText(ActivityDanhSachHanhThu.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });

        btnShowMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ActivityDanhSachHanhThu.this);
                builderSingle.setIcon(R.mipmap.ic_launcher);
                builderSingle.setTitle("Tin nhắn đã nhận");
                builderSingle.setCancelable(false);

                ListView lstMessage = new ListView(ActivityDanhSachHanhThu.this);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ActivityDanhSachHanhThu.this, android.R.layout.select_dialog_singlechoice);

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
                        "Xóa Tất Cả",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CLocal.jsonMessage = new JSONArray();
                            }
                        });

                builderSingle.setPositiveButton(
                        "Thoát",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });


                //hàm này khi click row sẽ bị ẩn
                /*builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(ActivityDanhSachHanhThu.this);
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
                loadListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (spnSort.getSelectedItem().toString()) {
                    case "Thời Gian Tăng":
                        Collections.sort(list, new CSort("ModifyDate", -1));
                        break;
                    case "Thời Gian Giảm":
                        Collections.sort(list, new CSort("ModifyDate", 1));
                        break;
                    default:
                        Collections.sort(list, new CSort("", -1));
                        break;
                }
                cViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnNhanVien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMaNV =spnID_NhanVien.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lstView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    // check if we reached the top or bottom of the list
                    View v = lstView.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        layoutAutoHide.setVisibility(View.VISIBLE);
                    }
                } else if (totalItemCount - visibleItemCount == firstVisibleItem) {
                    View v = lstView.getChildAt(totalItemCount - 1);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        // reached the bottom: visible header and footer
//                        layoutAutoHide.setVisibility(View.VISIBLE);
                    }
                } else if (totalItemCount - visibleItemCount > firstVisibleItem){
                    // on scrolling
                    layoutAutoHide.setVisibility(View.GONE);
                }
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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                cViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_search_khach_hang:
                Intent intent= new Intent(ActivityDanhSachHanhThu.this, ActivitySearchKhachHangWeb.class);
                startActivity(intent);
                return true;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadListView() {
        try {
            lstView.setAdapter(null);
            list = new ArrayList<CViewEntity>();
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
            cViewAdapter = new CViewAdapter(ActivityDanhSachHanhThu.this, list);
            lstView.setAdapter(cViewAdapter);
            txtTongHD.setText(CLocal.formatMoney(String.valueOf(TongHD), ""));
            txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
        } catch (Exception e) {
        }
    }

    public void addEntity(JSONObject jsonObject) {
        try {
            ///thiết lập khởi tạo 1 lần đầu để sort
            if(jsonObject.has("ModifyDate")==false)
                jsonObject.put("ModifyDate",CLocal.DateFormat.format(new Date()));
            CViewEntity entity = new CViewEntity();
            entity.setSTT(String.valueOf(list.size() + 1));
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

            list.add(entity);
        } catch (Exception e) {
        }
    }

    public class MyAsyncTask extends AsyncTask<Void, String, Void> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityDanhSachHanhThu.this);
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
                    selectedMaNV =CLocal.MaNV;
                CLocal.jsonHanhThu = new JSONArray(ws.getDSHoaDonTon(selectedMaNV,spnNam.getSelectedItem().toString(),spnKy.getSelectedItem().toString(), spnFromDot.getSelectedItem().toString(), spnToDot.getSelectedItem().toString()));
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
                    if (Boolean.parseBoolean(values[0]) == true){
                        loadListView();
                    }
                } catch (Exception e) {
                    CLocal.jsonHanhThu=null;
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
}
