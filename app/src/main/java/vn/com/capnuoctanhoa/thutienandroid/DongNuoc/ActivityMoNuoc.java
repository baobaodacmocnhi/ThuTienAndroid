package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityMoNuoc extends AppCompatActivity {
    private ImageButton ibtnChupHinh;
    private ImageView imgThumb;
    private EditText edtMaDN, edtDanhBo, edtMLT,edtHoTen, edtDiaChi, edtNgayMN, edtChiSoMN, edtHieu, edtCo, edtSoThan, edtLyDo;
    private Spinner spnChiMatSo, spnChiKhoaGoc;
    private Button btnKiemTra, btnMoNuoc;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_nuoc);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtMaDN = (EditText) findViewById(R.id.edtMaDN);
        edtDanhBo = (EditText) findViewById(R.id.edtDanhBo);
        edtMLT = (EditText) findViewById(R.id.edtMLT);
        edtHoTen = (EditText) findViewById(R.id.edtHoTen);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtHieu = (EditText) findViewById(R.id.edtHieu);
        edtCo = (EditText) findViewById(R.id.edtCo);
        edtSoThan = (EditText) findViewById(R.id.edtSoThan);
        edtNgayMN = (EditText) findViewById(R.id.edtNgayMN);
        edtChiSoMN = (EditText) findViewById(R.id.edtChiSoMN);
        edtLyDo = (EditText) findViewById(R.id.edtLyDo);
        spnChiMatSo = (Spinner) findViewById(R.id.spnChiMatSo);
        spnChiKhoaGoc = (Spinner) findViewById(R.id.spnChiKhoaGoc);

        imgThumb = (ImageView) findViewById(R.id.imgThumb);
        ibtnChupHinh = (ImageButton) findViewById(R.id.ibtnChupHinh);

        btnKiemTra = (Button) findViewById(R.id.btnKiemTra);
        btnMoNuoc = (Button) findViewById(R.id.btnMoNuoc);

        ibtnChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        imgThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowImgThumb();
            }
        });

        btnKiemTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.CheckNetworkAvailable(ActivityMoNuoc.this) == false) {
                    Toast.makeText(ActivityMoNuoc.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                if(edtMaDN.getText().toString().equals("")==false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("Kiểm Tra");
                }
            }
        });

        btnMoNuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.CheckNetworkAvailable(ActivityMoNuoc.this) == false) {
                    Toast.makeText(ActivityMoNuoc.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                if(edtMaDN.getText().toString().equals("")==false&&edtChiSoMN.getText().toString().equals("")==false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("Mở Nước");
                }
            }
        });

        try {
            String MaDN= getIntent().getStringExtra("MaDN");
            if (MaDN.equals("")==false) {
                FillDongNuoc(MaDN);
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgThumb.setImageBitmap(bitmap);
        }
    }

    public void FillDongNuoc(String MaDN) {
        try {

            for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                JSONObject jsonObject = CLocal.jsonDongNuoc.getJSONObject(i);
                if (jsonObject.getString("MaDN").equals(MaDN) == true) {
                    edtMaDN.setText(MaDN);
                    edtDanhBo.setText(jsonObject.getString("DanhBo"));
                    edtMLT.setText(jsonObject.getString("MLT"));
                    edtHoTen.setText(jsonObject.getString("HoTen"));
                    edtDiaChi.setText(jsonObject.getString("DiaChi"));
                    edtHieu.setText(jsonObject.getString("Hieu"));
                    edtCo.setText(jsonObject.getString("Co"));
                    edtSoThan.setText(jsonObject.getString("SoThan"));
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                    edtNgayMN.setText(currentDate.format(new Date()));
                    edtChiSoMN.setText(jsonObject.getString("ChiSoMN").replace("null",""));
                    SetSpinnerSelection(spnChiMatSo,jsonObject.getString("ChiMatSo"));
                    SetSpinnerSelection(spnChiKhoaGoc,jsonObject.getString("ChiKhoaGoc"));
                    edtLyDo.setText(jsonObject.getString("LyDo").replace("null",""));
                    break;
                }
            }
        } catch (Exception ex) {
            Toast.makeText(ActivityMoNuoc.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void SetSpinnerSelection(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public void ShowImgThumb() {
        Dialog builder = new Dialog(ActivityMoNuoc.this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(ActivityMoNuoc.this);
        imageView.setImageBitmap(((BitmapDrawable)imgThumb.getDrawable()).getBitmap());
        builder.addContentView(imageView, new RelativeLayout.LayoutParams( 600,600));
        builder.show();
    }

    public byte[] ConvertBitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public class MyAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityMoNuoc.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "Kiểm Tra":
                    return ws.KiemTraHoaDon_DongNuoc(edtMaDN.getText().toString());
                case "Mở Nước":
                    if(Boolean.parseBoolean(ws.CheckExist_MoNuoc(edtMaDN.getText().toString()))==false) {
                        Bitmap reizeImage = Bitmap.createScaledBitmap(((BitmapDrawable) imgThumb.getDrawable()).getBitmap(), 1024, 1024, false);
                        String imgString = Base64.encodeToString(ConvertBitmapToBytes(reizeImage), Base64.NO_WRAP);
//                        imgString = "NULL";
                        return ws.ThemMoNuoc(edtMaDN.getText().toString(), imgString, edtNgayMN.getText().toString(), edtChiSoMN.getText().toString(), CLocal.sharedPreferencesre.getString("MaNV",""));
                    }
                    else
                        return "ĐÃ NHẬP RỒI";
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            Toast.makeText(ActivityMoNuoc.this, s, Toast.LENGTH_SHORT).show();
        }

    }
}
