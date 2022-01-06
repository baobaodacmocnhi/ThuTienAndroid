package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import androidx.appcompat.app.AppCompatActivity;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocation;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityHoaDonDienTu_In extends AppCompatActivity {
    private TextView txtSTT;
    private EditText edtTuSTT, edtDenSTT;
    private Button btnIn;
    private int STT = -1, tustt = -1, denstt = -1;
    private CWebservice ws;
    //    private ThermalPrinter thermalPrinter = null;
    private CLocation cLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_dien_tu_in);

        ws = new CWebservice();
        cLocation = new CLocation(ActivityHoaDonDienTu_In.this);

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
                    tustt = -1;
                    denstt = -1;
                    if (edtTuSTT.getText().toString().trim().equals("") == false)
                        tustt = Integer.parseInt(edtTuSTT.getText().toString()) - 1;
                    if (edtDenSTT.getText().toString().trim().equals("") == false)
                        denstt = Integer.parseInt(edtDenSTT.getText().toString()) - 1;
                    if (tustt != -1 && denstt != -1 && tustt <= denstt && denstt < CLocal.listHanhThuView.size()) {
                        MyAsyncTask_XuLyTrucTiep myAsyncTask_xuLyTrucTiep = new MyAsyncTask_XuLyTrucTiep();
                        myAsyncTask_xuLyTrucTiep.execute(new String[]{"PhieuBao"});
                    }
                } else
                    CLocal.showPopupMessage(ActivityHoaDonDienTu_In.this, "Lỗi STT", "center");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void reInBienNhan_Direct(final String Loai, final CEntityParent entityParent) throws IOException, ParseException {
        switch (Loai) {

            case "PhieuBao":
                for (int j = 0; j < entityParent.getLstHoaDon().size(); j++)
                    if (CLocal.serviceThermalPrinter != null)
                        CLocal.serviceThermalPrinter.printPhieuBao(entityParent, entityParent.getLstHoaDon().get(j));
                break;
        }
    }

    public class MyAsyncTask_XuLyTrucTiep extends AsyncTask<String, Void, String[]> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityHoaDonDienTu_In.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String[] doInBackground(String... strings) {
            try {
                String result = "";
                String[] results = new String[]{};
                String MaHDs = "";
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date dateCapNhat = new Date();
                boolean XoaDCHD = false;
                switch (strings[0]) {
                    case "PhieuBao":
                        for (int i = tustt; i <= denstt; i++) {
                            if (CLocal.SyncTrucTiep == true) {
                                if (CLocal.listHanhThuView.get(i).isGiaiTrach() == false
                                        && CLocal.listHanhThuView.get(i).isThuHo() == false
                                        && CLocal.listHanhThuView.get(i).isTamThu() == false
                                        && CLocal.listHanhThuView.get(i).isDangNgan_DienThoai() == false
                                        && CLocal.listHanhThuView.get(i).isInPhieuBao() == false) {
                                    for (int j = 0; j < CLocal.listHanhThuView.get(i).getLstHoaDon().size(); j++)
                                        if (CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).isGiaiTrach() == false
                                                && CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).isThuHo() == false
                                                && CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).isTamThu() == false
                                                && CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                                && CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getInPhieuBao_Ngay().equals("") == true) {
                                            result = ws.XuLy_HoaDonDienTu("PhieuBao", CLocal.MaNV, CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD(), currentDate.format(dateCapNhat), "", CLocal.listHanhThuView.get(i).getMaKQDN(), String.valueOf(XoaDCHD), cLocation.getLocation());
                                            results = result.split(",");
                                            if (Boolean.parseBoolean(results[0]) == true) {
                                                CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).setInPhieuBao_Ngay(currentDate.format(dateCapNhat));
                                                if (CLocal.serviceThermalPrinter != null)
                                                    CLocal.serviceThermalPrinter.printPhieuBao(CLocal.listHanhThuView.get(i), CLocal.listHanhThuView.get(i).getLstHoaDon().get(j));
                                            } else if (Boolean.parseBoolean(results[0]) == false && results.length == 5) {
                                                CLocal.updateValueChild(CLocal.listHanhThu, results[2], results[3], results[4]);
                                                CLocal.updateValueChild(CLocal.listHanhThuView, results[2], results[3], results[4]);
                                            }
                                            CLocal.updateTinhTrangParent(CLocal.listHanhThuView, i);
                                            CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(i));
                                        }
                                } else
                                    reInBienNhan_Direct("PhieuBao", CLocal.listHanhThuView.get(i));
                            } else {

                            }
                        }
                        break;
                }
                return new String[]{"true"};

            } catch (Exception ex) {
                CLocal.showToastMessage(ActivityHoaDonDienTu_In.this, ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (Boolean.parseBoolean(strings[0]) == true) {
//                CLocal.showPopupMessage(ActivityHoaDonDienTu_ThuTien.this, "THÀNH CÔNG","center");
            } else
                CLocal.showPopupMessage(ActivityHoaDonDienTu_In.this, "THẤT BẠI\n" + strings[1], "center");
        }
    }

}
