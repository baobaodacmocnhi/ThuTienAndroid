package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityHoaDonDienTu_GhiChu extends AppCompatActivity {
private EditText edtDienThoai,edtGiaBieu,edtNiemChi,edtDiemBe;
private Button btnCapNhat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_dien_tu_ghi_chu);

        edtDienThoai=(EditText)findViewById(R.id.edtDienThoai);
        edtGiaBieu=(EditText)findViewById(R.id.edtGiaBieu);
        edtNiemChi=(EditText)findViewById(R.id.edtNiemChi);
        edtDiemBe=(EditText)findViewById(R.id.edtDiemBe);
        btnCapNhat=(Button) findViewById(R.id.btnCapNhat);

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
