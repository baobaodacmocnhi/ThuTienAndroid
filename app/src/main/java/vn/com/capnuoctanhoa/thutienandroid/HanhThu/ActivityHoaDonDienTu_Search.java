package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CViewParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CustomAdapterListView;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityHoaDonDienTu_Search extends AppCompatActivity {
    private EditText edtNoiDung;
    private ListView lstView;
    private CustomAdapterListView customAdapterListView;
    private ArrayList<CViewParent> lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_dien_tu_search);

        edtNoiDung = (EditText) findViewById(R.id.edtNoiDung);
        lstView = (ListView) findViewById(R.id.lstView);

        edtNoiDung.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lst = new ArrayList<>();
                if (CLocal.listHanhThuView != null && CLocal.listHanhThuView.size() > 0) {
                    for (int i = 0; i < CLocal.listHanhThuView.size(); i++) {
                        if (CLocal.listHanhThuView.get(i).getDanhBo().replace(" ","").contains(s.toString().toUpperCase())
                                || CLocal.listHanhThuView.get(i).getHoTen().contains(s.toString().toUpperCase())
                                || CLocal.listHanhThuView.get(i).getDiaChi().contains(s.toString().toUpperCase())) {
                            CViewParent enViewParent = new CViewParent();
                            enViewParent.setRow1a(CLocal.listHanhThuView.get(i).getMLT());
                            enViewParent.setRow2a(CLocal.listHanhThuView.get(i).getDanhBo());
                            enViewParent.setRow3a(CLocal.listHanhThuView.get(i).getHoTen());
                            enViewParent.setRow4a(CLocal.listHanhThuView.get(i).getDiaChi());
                            lst.add(enViewParent);

                        }
                    }
                    loadListView();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView DanhBo = (TextView) view.findViewById(R.id.lvRow2a);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("DanhBo", DanhBo.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    public void loadListView() {
        customAdapterListView = new CustomAdapterListView(this, lst);
        lstView.setAdapter(customAdapterListView);
    }
}
