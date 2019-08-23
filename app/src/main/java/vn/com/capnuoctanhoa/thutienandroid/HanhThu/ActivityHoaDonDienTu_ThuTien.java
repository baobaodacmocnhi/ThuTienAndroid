package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONObject;

import vn.com.capnuoctanhoa.thutienandroid.Class.CViewParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityHoaDonDienTu_ThuTien extends AppCompatActivity {
    private EditText edtMLT, edtDanhBo, edtHoTen, edtDiaChi, edtTongCong;
    private ListView listView;
    private Button btnTruoc, btnSau, btnThuTien, btnInPhieuBao, btnXoa;
    private Integer index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_dien_tu_thu_tien);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtMLT = (EditText) findViewById(R.id.edtMLT);
        edtDanhBo = (EditText) findViewById(R.id.edtDanhBo);
        edtHoTen = (EditText) findViewById(R.id.edtHoTen);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtTongCong = (EditText) findViewById(R.id.edtTongCong);
        listView = (ListView) findViewById(R.id.listView);
        btnTruoc = (Button) findViewById(R.id.btnTruoc);
        btnSau = (Button) findViewById(R.id.btnSau);
        btnThuTien = (Button) findViewById(R.id.btnThuTien);
        btnInPhieuBao = (Button) findViewById(R.id.btnInPhieuBao);
        btnXoa = (Button) findViewById(R.id.btnXoa);

        try {
            String DanhBo = getIntent().getStringExtra("DanhBo");
            if (DanhBo.equals("") == false) {
                fillLayout(DanhBo);
            }
        } catch (Exception ex) {
        }

        btnTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index > 0) {
                    index--;
                }
                fillLayout(index);
            }
        });

        btnSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < CLocal.listHanhThu.size()-1) {
                    index++;
                }
                fillLayout(index);
            }
        });

        btnThuTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnInPhieuBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillLayout(String DanhBo) {
        try {
            if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                for (int i = 0; i < CLocal.listHanhThu.size(); i++) {
                    if (CLocal.listHanhThu.get(i).getDanhBo().equals(DanhBo) == true) {
                        index = i;
                        edtMLT.setText(CLocal.listHanhThu.get(i).getMLT());
                        edtDanhBo.setText(CLocal.listHanhThu.get(i).getDanhBo());
                        edtHoTen.setText(CLocal.listHanhThu.get(i).getHoTen());
                        edtDiaChi.setText(CLocal.listHanhThu.get(i).getDiaChi());
                        Integer TongCong = 0;
                        for (int j = 0; j < CLocal.listHanhThu.get(i).getLstHoaDon().size(); j++) {
                            TongCong += Integer.parseInt(CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getTongCong());
                        }
                        edtTongCong.setText(CLocal.formatMoney(TongCong.toString(), "đ"));
                    }
                }
            }
        } catch (Exception ex) {

        }
    }

    private void fillLayout(Integer i) {
        try {
            if (CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                if (i >= 0 && i < CLocal.listHanhThu.size()) {
                    edtMLT.setText(CLocal.listHanhThu.get(i).getMLT());
                    edtDanhBo.setText(CLocal.listHanhThu.get(i).getDanhBo());
                    edtHoTen.setText(CLocal.listHanhThu.get(i).getHoTen());
                    edtDiaChi.setText(CLocal.listHanhThu.get(i).getDiaChi());
                    Integer TongCong = 0;
                    for (int j = 0; j < CLocal.listHanhThu.get(i).getLstHoaDon().size(); j++) {
                        TongCong += Integer.parseInt(CLocal.listHanhThu.get(i).getLstHoaDon().get(j).getTongCong());
                    }
                    edtTongCong.setText(CLocal.formatMoney(TongCong.toString(), "đ"));
                }
            }
        } catch (Exception ex) {

        }
    }
}
