package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.CViewAdapterGroup;
import vn.com.capnuoctanhoa.thutienandroid.CViewEntity;
import vn.com.capnuoctanhoa.thutienandroid.CViewEntityChild;
import vn.com.capnuoctanhoa.thutienandroid.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDanhSachDongNuoc extends AppCompatActivity {
    private EditText edtFromDate, edtToDate;
    private DatePickerDialog datePickerDialog;
    private Button btnDownload;
    private Spinner spnFilter;
    private ExpandableListView lstView;
    private CViewAdapterGroup cViewAdapterGroup;
    private ArrayList<CViewEntity> listParent;
    private ArrayList<CViewEntityChild> listChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_dong_nuoc);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtFromDate = (EditText) findViewById(R.id.edtFromDate);
        edtToDate = (EditText) findViewById(R.id.edtToDate);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        spnFilter = (Spinner) findViewById(R.id.spnFilter);
        lstView = (ExpandableListView) findViewById(R.id.lstView);

        edtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ActivityDanhSachDongNuoc.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                edtFromDate.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        edtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ActivityDanhSachDongNuoc.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                edtToDate.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.checkNetworkAvailable(ActivityDanhSachDongNuoc.this) == false) {
                    Toast.makeText(ActivityDanhSachDongNuoc.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
                CLocal.hideKeyboard(ActivityDanhSachDongNuoc.this);
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

        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                PopupMenu popup = new PopupMenu(ActivityDanhSachDongNuoc.this, view);
                popup.getMenuInflater().inflate(R.menu.popup_dong_nuoc, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.popup_action_DongNuoc:
                                TextView MaDN = (TextView) view.findViewById(R.id.lvID);

                                Intent intent = new Intent(ActivityDanhSachDongNuoc.this, ActivityDongNuoc.class);
                                intent.putExtra("MaDN", MaDN.getText().toString());
                                startActivity(intent);
                                break;
                            case R.id.popup_action_MoNuoc:
                                TextView MaDN2 = (TextView) view.findViewById(R.id.lvID);

                                Intent intent2 = new Intent(ActivityDanhSachDongNuoc.this, ActivityMoNuoc.class);
                                intent2.putExtra("MaDN", MaDN2.getText().toString());
                                startActivity(intent2);
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
                return true;
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
        inflater.inflate(R.menu.menu_main, menu);
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
                cViewAdapterGroup.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public void loadListView() {
        try {
            lstView.setAdapter((BaseExpandableListAdapter) null);
            listParent = new ArrayList<CViewEntity>();
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
                            if (jsonObject.getString("MoNuoc") != "null" && Boolean.parseBoolean(jsonObject.getString("MoNuoc")) == false && Boolean.parseBoolean(jsonObject.getString("NgayGiaiTrach")) == true) {
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
                            if (Boolean.parseBoolean(jsonObject.getString("GiaiTrach")) == true) {
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
            cViewAdapterGroup = new CViewAdapterGroup(ActivityDanhSachDongNuoc.this, listParent);
            lstView.setAdapter(cViewAdapterGroup);
        } catch (Exception e) {

        }
    }

    public void addEntityParent(JSONObject jsonObject) {
        try {
            CViewEntity entity = new CViewEntity();
            entity.setSTT(String.valueOf(listParent.size() + 1));
            entity.setID(jsonObject.getString("ID"));
            entity.setRow1a(jsonObject.getString("DiaChi"));
            String strMLT = new StringBuffer(jsonObject.getString("MLT")).insert(4, " ").insert(2, " ").toString();
            entity.setRow2a(strMLT);
            String strDanhBo = new StringBuffer(jsonObject.getString("DanhBo")).insert(7, " ").insert(4, " ").toString();
            entity.setRow2b(strDanhBo);
            entity.setRow3a(jsonObject.getString("HoTen"));
            entity.setGiaiTrach(Boolean.parseBoolean(jsonObject.getString("GiaiTrach")));
            entity.setTamThu(Boolean.parseBoolean(jsonObject.getString("TamThu")));
            entity.setThuHo(Boolean.parseBoolean(jsonObject.getString("ThuHo")));

            ///////////////////////////
            listChild = new ArrayList<CViewEntityChild>();
            if (CLocal.jsonDongNuocChild != null && CLocal.jsonDongNuocChild.length() > 0)
                for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                    JSONObject jsonObjectChild = CLocal.jsonDongNuocChild.getJSONObject(i);
                    if (jsonObjectChild.getString("ID").equals(entity.getID())==true) {
                        addEntityChild(jsonObjectChild);
                    }
                }

//                listChildHashMap.put(listParent.get(listParent.size()-1),listChild);

            entity.setListChild(listChild);
            listParent.add(entity);
        } catch (Exception e) {
        }
    }

    public void addEntityChild(JSONObject jsonObject) {
        try {
            CViewEntityChild entity = new CViewEntityChild();
            entity.setID(jsonObject.getString("MaHD"));
            entity.setRow1a(jsonObject.getString("Ky"));
            entity.setRow1b(CLocal.formatMoney(jsonObject.getString("TongCong"), "đ"));
            entity.setGiaiTrach(Boolean.parseBoolean(jsonObject.getString("GiaiTrach")));
            entity.setTamThu(Boolean.parseBoolean(jsonObject.getString("TamThu")));
            entity.setThuHo(Boolean.parseBoolean(jsonObject.getString("ThuHo")));

            listChild.add(entity);
        } catch (Exception e) {
        }
    }

    public class MyAsyncTask extends AsyncTask<Void, String, Void> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityDanhSachDongNuoc.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                CLocal.jsonDongNuoc=new JSONArray(ws.getDSDongNuoc(CLocal.sharedPreferencesre.getString("MaNV", ""), edtFromDate.getText().toString(), edtToDate.getText().toString()));
                CLocal.jsonDongNuocChild=new JSONArray(ws.getDSCTDongNuoc(CLocal.sharedPreferencesre.getString("MaNV", ""), edtFromDate.getText().toString(), edtToDate.getText().toString()));
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
                    if(Boolean.parseBoolean(values[0])==true) {
                        SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                        editor.putString("jsonDongNuoc", CLocal.jsonDongNuoc.toString());
                        editor.putString("jsonDongNuocChild", CLocal.jsonDongNuocChild.toString());
                        editor.commit();
//                    CLocal.jsonDongNuoc = new JSONArray(array[0]);
//                    CLocal.jsonDongNuocChild = new JSONArray(array[1]);
                        loadListView();
                    }
                    else {
                        SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
                        editor.putString("jsonDongNuoc", "");
                        editor.putString("jsonDongNuocChild", "");
                        editor.commit();
                    }
                } catch (Exception ex) {

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
