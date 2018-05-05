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

public class ActivityDongNuoc extends AppCompatActivity {
    private ImageButton ibtnChupHinh;
    private ImageView imgThumb;
    private EditText edtMaDN, edtDanhBo, edtMLT,edtHoTen, edtDiaChi, edtNgayDN, edtChiSoDN, edtHieu, edtCo, edtSoThan, edtLyDo;
    private Spinner spnChiMatSo, spnChiKhoaGoc;
    private Button btnKiemTra, btnDongNuoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dong_nuoc);

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
        edtNgayDN = (EditText) findViewById(R.id.edtNgayDN);
        edtChiSoDN = (EditText) findViewById(R.id.edtChiSoDN);
        edtLyDo = (EditText) findViewById(R.id.edtLyDo);
        spnChiMatSo = (Spinner) findViewById(R.id.spnChiMatSo);
        spnChiKhoaGoc = (Spinner) findViewById(R.id.spnChiKhoaGoc);

        imgThumb = (ImageView) findViewById(R.id.imgThumb);
        ibtnChupHinh = (ImageButton) findViewById(R.id.ibtnChupHinh);

        btnKiemTra = (Button) findViewById(R.id.btnKiemTra);
        btnDongNuoc = (Button) findViewById(R.id.btnDongNuoc);

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
                if (CLocal.CheckNetworkAvailable(ActivityDongNuoc.this) == false) {
                    Toast.makeText(ActivityDongNuoc.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                if(edtMaDN.getText().toString().equals("")==false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("Kiểm Tra");
                }
            }
        });

        btnDongNuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.CheckNetworkAvailable(ActivityDongNuoc.this) == false) {
                    Toast.makeText(ActivityDongNuoc.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                if(edtMaDN.getText().toString().equals("")==false&&edtChiSoDN.getText().toString().equals("")==false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("Đóng Nước");
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
                    edtNgayDN.setText(currentDate.format(new Date()));
                    edtChiSoDN.setText(jsonObject.getString("ChiSoDN").replace("null",""));
                    SetSpinnerSelection(spnChiMatSo,jsonObject.getString("ChiMatSo"));
                    SetSpinnerSelection(spnChiKhoaGoc,jsonObject.getString("ChiKhoaGoc"));
                    edtLyDo.setText(jsonObject.getString("LyDo").replace("null",""));
                    break;
                }
            }
        } catch (Exception ex) {
            Toast.makeText(ActivityDongNuoc.this, ex.toString(), Toast.LENGTH_SHORT).show();
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
        Dialog builder = new Dialog(ActivityDongNuoc.this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(ActivityDongNuoc.this);
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
            progressDialog = new ProgressDialog(ActivityDongNuoc.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "Kiểm Tra":
                    return ws.KiemTraHoaDon_DongNuoc(edtMaDN.getText().toString());
                case "Đóng Nước":
                    if(Boolean.parseBoolean(ws.CheckExist_DongNuoc(edtMaDN.getText().toString()))==false) {
                        Bitmap reizeImage = Bitmap.createScaledBitmap(((BitmapDrawable) imgThumb.getDrawable()).getBitmap(), 1024, 1024, false);
                        String imgString = Base64.encodeToString(ConvertBitmapToBytes(reizeImage), Base64.NO_WRAP);
//                        imgString = "NULL";
                        return ws.ThemDongNuoc(edtMaDN.getText().toString(), edtDanhBo.getText().toString(), edtMLT.getText().toString(), edtHoTen.getText().toString(), edtDiaChi.getText().toString(),
                                imgString, edtNgayDN.getText().toString(), edtChiSoDN.getText().toString(), edtHieu.getText().toString(),edtCo.getText().toString(), edtSoThan.getText().toString(),
                                spnChiMatSo.getSelectedItem().toString(), spnChiKhoaGoc.getSelectedItem().toString(), edtLyDo.getText().toString(),CLocal.sharedPreferencesre.getString("MaNV",""));
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
            Toast.makeText(ActivityDongNuoc.this, s, Toast.LENGTH_SHORT).show();
        }

    }
}
