package vn.com.capnuoctanhoa.thutienandroid.QuanLy;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.Class.CustomAdapterListView;
import vn.com.capnuoctanhoa.thutienandroid.Class.CustomAdapterRecyclerViewParent;
import vn.com.capnuoctanhoa.thutienandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDongMoNuoc extends Fragment {
    private View rootView;
    private DatePickerDialog datePickerDialog;
    private EditText edtFromDate, edtToDate;
    private Spinner spnTo;
    private Button btnXem;
    private RadioButton radDongNuoc;
    private RecyclerView recyclerView;
    private LinearLayout layoutTo;
    private CardView layoutAutoHide;
    private NestedScrollView nestedScrollView;
    private FloatingActionButton floatingActionButton;
    private TextView txtTongHD, txtTongCong;

    private int layoutAutoHide_Height;
    private ArrayList<String> spnID_To, spnName_To;
    private String selectedTo = "";
    private long TongHD, TongCong;
    private ArrayList<CEntityParent> list;
    private CustomAdapterRecyclerViewParent customAdapterRecyclerViewParent;

    public FragmentDongMoNuoc() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_dong_mo_nuoc, container, false);

        layoutTo = (LinearLayout) rootView.findViewById(R.id.layoutTo);
        edtFromDate = (EditText) rootView.findViewById(R.id.edtFromDate);
        edtToDate = (EditText) rootView.findViewById(R.id.edtToDate);
        spnTo = (Spinner) rootView.findViewById(R.id.spnTo);
        btnXem = (Button) rootView.findViewById(R.id.btnXem);
        radDongNuoc=(RadioButton) rootView.findViewById(R.id.radDongNuoc);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        txtTongHD = (TextView) rootView.findViewById(R.id.txtTongHD);
        txtTongCong = (TextView) rootView.findViewById(R.id.txtTongCong);
        nestedScrollView = (NestedScrollView) rootView.findViewById(R.id.nestedScrollView);
        layoutAutoHide = (CardView) rootView.findViewById(R.id.layoutAutoHide);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);

        if (CLocal.Doi == true) {
            layoutTo.setVisibility(View.VISIBLE);
            try {
                if (CLocal.jsonTo != null && CLocal.jsonTo.length() > 0) {
                    spnID_To = new ArrayList<>();
                    spnName_To = new ArrayList<>();
                    spnID_To.add("0");
                    spnName_To.add("Tất Cả");
                    for (int i = 0; i < CLocal.jsonTo.length(); i++) {
                        JSONObject jsonObject = CLocal.jsonTo.getJSONObject(i);

                        spnID_To.add(jsonObject.getString("MaTo"));
                        spnName_To.add(jsonObject.getString("TenTo"));

                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spnName_To);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnTo.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            layoutTo.setVisibility(View.GONE);
        }

        spnTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTo = spnID_To.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                //hide soft keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getActivity(),
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
                //hide soft keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getActivity(),
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

        btnXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CLocal.checkNetworkAvailable(getActivity().getApplicationContext()) == false) {
                    Toast.makeText(getActivity(), "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });

        layoutAutoHide_Height = layoutAutoHide.getHeight();
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = nestedScrollView.getScrollY();
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layoutAutoHide.getLayoutParams();
                int height = Math.max(0, layoutAutoHide_Height - scrollY);
                lp.height = height;
                layoutAutoHide.setLayoutParams(lp);
            }
        });

        //if you remove this part, the card would be shown in its minimum state at start
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.scrollTo(0, 0);
            }
        });

        // The calculation for heights of views should be done after the view created
        final View view = rootView.findViewById(R.id.layoutRoot);
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        //Remove the listener before proceeding
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                        // measure your views here
                        layoutAutoHide_Height = layoutAutoHide.getHeight();
                        nestedScrollView.scrollTo(0, 0);
                    }
                });

        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (nestedScrollView.getScrollY() == 0 || nestedScrollView.getScrollY() < layoutAutoHide_Height) {
                    floatingActionButton.hide();
                } else {
                    floatingActionButton.show();
                }
            }
        });

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

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollView.fling(0);
                nestedScrollView.smoothScrollTo(0, 0);
            }
        });

        return rootView;
    }

    public class MyAsyncTask extends AsyncTask<Void, String, Void> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            recyclerView.setAdapter(null);
            list = new ArrayList<CEntityParent>();
            TongHD = TongCong = 0;
            Boolean DongNuoc=false;
            if(radDongNuoc.isChecked()==true)
                DongNuoc=true;
            if (CLocal.Doi == true) {
                if (Integer.parseInt(selectedTo) == 0) {
                    for (int i = 0; i < spnID_To.size(); i++) {
                        publishProgress(ws.getTongDongMoNuoc(String.valueOf(DongNuoc),spnID_To.get(i), edtFromDate.getText().toString(), edtToDate.getText().toString()));
                    }
                } else
                    publishProgress(ws.getTongDongMoNuoc(String.valueOf(DongNuoc),selectedTo, edtFromDate.getText().toString(), edtToDate.getText().toString()));
            } else
                publishProgress(ws.getTongDongMoNuoc(String.valueOf(DongNuoc),CLocal.MaTo, edtFromDate.getText().toString(), edtToDate.getText().toString()));
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                try {
                    JSONArray jsonArray = new JSONArray(values[0]);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CEntityParent entity = new CEntityParent();
                        entity.setRow1a(jsonObject.getString("HoTen"));
                        entity.setRow2a(jsonObject.getString("TongHD"));
//                        entity.setRow2b(CLocal.formatMoney(jsonObject.getString("TongCong"), "đ"));
                        list.add(entity);
                        TongHD += Long.parseLong(jsonObject.getString("TongHD"));
//                        TongCong += Long.parseLong(jsonObject.getString("TongCong"));
                    }
                    CustomAdapterListView customAdapterListView = new CustomAdapterListView(getActivity(), list);
                    customAdapterRecyclerViewParent = new CustomAdapterRecyclerViewParent(getActivity(), list);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(customAdapterRecyclerViewParent);
                    txtTongHD.setText(CLocal.formatMoney(String.valueOf(TongHD), ""));
//                    txtTongCong.setText(CLocal.formatMoney(String.valueOf(TongCong), "đ"));
                } catch (Exception e) {
                    e.printStackTrace();
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
