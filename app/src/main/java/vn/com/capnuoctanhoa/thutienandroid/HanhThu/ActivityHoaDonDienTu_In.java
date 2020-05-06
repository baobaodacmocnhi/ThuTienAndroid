package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import androidx.appcompat.app.AppCompatActivity;
import vn.com.capnuoctanhoa.thutienandroid.Bluetooth.ThermalPrinter;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityHoaDonDienTu_In extends AppCompatActivity {
    private TextView txtSTT;
    private EditText edtTuSTT, edtDenSTT;
    private Button btnIn;
    private Integer STT = -1;
//    private ThermalPrinter thermalPrinter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_dien_tu_in);

        txtSTT = (TextView) findViewById(R.id.txtSTT);
        edtTuSTT = (EditText) findViewById(R.id.edtTuSTT);
        edtDenSTT = (EditText) findViewById(R.id.edtDenSTT);
        btnIn = (Button) findViewById(R.id.btnIn);

        try {
            if (CLocal.checkNetworkAvailable(ActivityHoaDonDienTu_In.this) == false) {
                Toast.makeText(ActivityHoaDonDienTu_In.this, "Không có Internet", Toast.LENGTH_LONG).show();
                return;
            }
            STT = Integer.parseInt(getIntent().getStringExtra("STT"));
            if (STT > -1)
                txtSTT.setText("STT hiện tại: " + (STT + 1));
        } catch (Exception ex) {
            CLocal.showToastMessage(ActivityHoaDonDienTu_In.this, ex.getMessage());
        }

        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (STT > -1) {
                    int tustt = -1, denstt = -1;
                    if (edtTuSTT.getText().toString().trim().equals("") == false)
                        tustt = Integer.parseInt(edtTuSTT.getText().toString()) - 1;
                    if (edtDenSTT.getText().toString().trim().equals("") == false)
                        denstt = Integer.parseInt(edtDenSTT.getText().toString()) - 1;
                    if (tustt != -1 && denstt != -1 && tustt <= denstt && denstt < CLocal.listHanhThuView.size())
                        for (int i = tustt; i <= denstt; i++)
                            for (int j = 0; j < CLocal.listHanhThuView.get(i).getLstHoaDon().size(); j++)
                                if (ThermalPrinter.getBluetoothDevice() != null)
                                    ThermalPrinter.printPhieuBao(CLocal.listHanhThuView.get(i), CLocal.listHanhThuView.get(i).getLstHoaDon().get(j));
                } else
                    CLocal.showPopupMessage(ActivityHoaDonDienTu_In.this, "Lỗi STT");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
