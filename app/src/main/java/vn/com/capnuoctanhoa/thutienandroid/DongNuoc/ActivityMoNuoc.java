package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.Class.CMarshMallowPermission;
import vn.com.capnuoctanhoa.thutienandroid.Class.CustomAdapterRecyclerViewImage;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityMoNuoc extends AppCompatActivity {
    private ImageButton ibtnChupHinh;
    //    private ImageView imgThumb;
    private EditText edtMaDN, edtDanhBo, edtMLT, edtHoTen, edtDiaChi, edtNgayMN, edtChiSoMN, edtNiemChi, edtHieu, edtCo, edtSoThan, edtLyDo;
    private Spinner spnChiMatSo, spnChiKhoaGoc, spnViTri, spnMauSac;
    private Button btnMoNuoc, btnIn;
    private String imgPath;
    private Bitmap imgCapture;
    public ArrayList<Bitmap> lstCapture;
    private RecyclerView recyclerView;
    private CustomAdapterRecyclerViewImage customAdapterRecyclerViewImage;
    private CMarshMallowPermission CMarshMallowPermission = new CMarshMallowPermission(ActivityMoNuoc.this);
    private int STT = -1;
    private CWebservice ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_nuoc);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ws = new CWebservice();

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
        edtNiemChi = (EditText) findViewById(R.id.edtNiemChi);
        edtLyDo = (EditText) findViewById(R.id.edtLyDo);
        spnChiMatSo = (Spinner) findViewById(R.id.spnChiMatSo);
        spnChiKhoaGoc = (Spinner) findViewById(R.id.spnChiKhoaGoc);
        spnViTri = (Spinner) findViewById(R.id.spnViTri);
        spnMauSac = (Spinner) findViewById(R.id.spnMauSac);
//        imgThumb = (ImageView) findViewById(R.id.imgThumb);
        ibtnChupHinh = (ImageButton) findViewById(R.id.ibtnChupHinh);
        lstCapture = new ArrayList<Bitmap>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        btnMoNuoc = (Button) findViewById(R.id.btnMoNuoc);
        btnIn = (Button) findViewById(R.id.btnIn);

//        final MyAsyncTask_Thermal myAsyncTask_thermal = new MyAsyncTask_Thermal();
//        myAsyncTask_thermal.execute();

        ibtnChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CMarshMallowPermission.checkPermissionForExternalStorage() == false) {
                        CMarshMallowPermission.requestPermissionForExternalStorage();
                    }
                    if (CMarshMallowPermission.checkPermissionForExternalStorage() == false)
                        return;
                }
                imgCapture = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMoNuoc.this);
                builder.setTitle("Thông Báo");
                builder.setMessage("Chọn lựa hành động");
                builder.setCancelable(false);
                builder.setPositiveButton("Chụp từ camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri imgUri = createImageUri();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(ActivityMoNuoc.this.getPackageManager()) != null) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri); // put uri file khi mà mình muốn lưu ảnh sau khi chụp như thế nào  ?
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            startActivityForResult(intent, 1);
                        }
                    }
                });
                builder.setNegativeButton("Chọn từ thư viện", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT <= 19) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            startActivityForResult(intent, 2);
                        } else if (Build.VERSION.SDK_INT > 19) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

//        imgThumb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showImgThumb();
//            }
//        });

        btnMoNuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.checkNetworkAvailable(ActivityMoNuoc.this) == false) {
                    Toast.makeText(ActivityMoNuoc.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                if (edtMaDN.getText().toString().equals("") == false && edtChiSoMN.getText().toString().equals("") == false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("MoNuoc");
                }
            }
        });

        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CLocal.listDongNuocView.get(STT).isMoNuoc() == true)
                    if (CLocal.serviceThermalPrinter != null) {
                        try {
                            CLocal.serviceThermalPrinter.printMoNuoc(CLocal.listDongNuocView.get(STT));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }
        });

        try {
            STT = Integer.parseInt(getIntent().getStringExtra("STT"));
            if (STT > -1) {
                fillDongNuoc(STT);
            }
        } catch (Exception ex) {
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (imgPath != null && imgPath != "") {
                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                bitmap = CLocal.imageOreintationValidator(bitmap, imgPath);
                imgCapture = bitmap;
            }
//            imgThumb.setImageBitmap(bitmap);
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String strPath = CLocal.getPathFromUri(this, uri);
            Bitmap bitmap = BitmapFactory.decodeFile(strPath);
            bitmap = CLocal.imageOreintationValidator(bitmap, strPath);
            imgCapture = bitmap;
//            imgThumb.setImageBitmap(bitmap);
        }
        if (imgCapture != null) {
            lstCapture.add(imgCapture);
            customAdapterRecyclerViewImage = new CustomAdapterRecyclerViewImage(ActivityMoNuoc.this, lstCapture);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(customAdapterRecyclerViewImage);
        }
    }

    private Uri createImageUri() {
        try {
            File filesDir = ActivityMoNuoc.this.getExternalFilesDir(CLocal.pathPicture);
            File photoFile = null;
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                photoFile = File.createTempFile(timeStamp, ".jpg", filesDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri uri;

            if (Build.VERSION.SDK_INT < 21) {
                // Từ android 5.0 trở xuống. khi ta sử dụng FileProvider.getUriForFile() sẽ trả về ngoại lệ FileUriExposedException
                // Vì vậy mình sử dụng Uri.fromFile đề lấy ra uri cho file ảnh
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                photoFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "" + timeStamp + ".jpg");
                uri = Uri.fromFile(photoFile);
            } else {
                // từ android 5.0 trở lên ta có thể sử dụng Uri.fromFile() và FileProvider.getUriForFile() để trả về uri file sau khi chụp.
                // Nhưng bắt buộc từ Android 7.0 trở lên ta phải sử dụng FileProvider.getUriForFile() để trả về uri cho file đó.
                Uri photoURI = FileProvider.getUriForFile(ActivityMoNuoc.this, "thutien_file_provider", photoFile);
                uri = photoURI;
            }
            imgPath = photoFile.getAbsolutePath();
            return uri;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void fillDongNuoc(String MaDN) {
        try {

            for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                JSONObject jsonObject = CLocal.jsonDongNuoc.getJSONObject(i);
                if (jsonObject.getString("ID").equals(MaDN) == true) {
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
                    edtChiSoMN.setText(jsonObject.getString("ChiSoMN").replace("null", ""));
                    setSpinnerSelection(spnChiMatSo, jsonObject.getString("ChiMatSo"));
                    setSpinnerSelection(spnChiKhoaGoc, jsonObject.getString("ChiKhoaGoc"));
                    edtLyDo.setText(jsonObject.getString("LyDo").replace("null", ""));
                    break;
                }
            }
        } catch (Exception ex) {
            CLocal.showToastMessage(ActivityMoNuoc.this, ex.getMessage());
        }
    }

    public void fillDongNuoc(int STT) {
        try {
            if (CLocal.listDongNuocView != null && CLocal.listDongNuocView.size() > 0) {
                CEntityParent en = CLocal.listDongNuocView.get(STT);
                edtMaDN.setText(en.getID());
                edtDanhBo.setText(en.getDanhBo());
                edtMLT.setText(en.getMLT());
                edtHoTen.setText(en.getHoTen());
                edtDiaChi.setText(en.getDiaChi());
                edtHieu.setText(en.getHieu());
                edtCo.setText(en.getCo());
                edtSoThan.setText(en.getSoThan());

                setSpinnerSelection(spnChiMatSo, en.getChiMatSo());
                setSpinnerSelection(spnChiKhoaGoc, en.getChiKhoaGoc());
                setSpinnerSelection(spnViTri, en.getViTri());
                edtLyDo.setText(en.getLyDo());
                if (en.isMoNuoc() == false) {
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    edtNgayMN.setText(currentDate.format(new Date()));
                } else {
                    edtNgayMN.setText(en.getNgayMN());
                    edtChiSoMN.setText(en.getChiSoMN());
                    edtNiemChi.setText(en.getChiSoMN());
                    setSpinnerSelection(spnMauSac, en.getMauSacMN());
                }
            }
        } catch (Exception ex) {
            CLocal.showToastMessage(ActivityMoNuoc.this, ex.getMessage());
        }
    }

    private void setSpinnerSelection(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

//    public void showImgThumb() {
//        Dialog builder = new Dialog(ActivityMoNuoc.this);
//        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                //nothing;
//            }
//        });
//
//        ImageView imageView = new ImageView(ActivityMoNuoc.this);
//        imageView.setImageBitmap(((BitmapDrawable) imgThumb.getDrawable()).getBitmap());
//        builder.addContentView(imageView, new RelativeLayout.LayoutParams(1000, 1000));
//        builder.show();
//    }

    @Override
    protected void onDestroy() {
//        if (thermalPrinter != null)
//            thermalPrinter.disconnectBluetoothDevice();
        super.onDestroy();
    }

    public class MyAsyncTask_Thermal extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
//                thermalPrinter = new ThermalPrinter(ActivityMoNuoc.this);
            } catch (Exception ex) {
                CLocal.showToastMessage(ActivityMoNuoc.this, ex.getMessage());
            }
            return null;
        }
    }

    public class MyAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityMoNuoc.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "MoNuoc":
//                    if (Boolean.parseBoolean(ws.checkExist_DongNuoc(edtMaDN.getText().toString())) == false)
//                        return "CHƯA NHẬP ĐÓNG NƯỚC";
//                    if (Boolean.parseBoolean(ws.checkExist_MoNuoc(edtMaDN.getText().toString())) == true)
//                        return "ĐÃ NHẬP RỒI";

                    String imgString = "";
                    if (lstCapture.size() > 0) {
//                        Bitmap reizeImage = Bitmap.createScaledBitmap(((BitmapDrawable) imgThumb.getDrawable()).getBitmap(), 1024, 1024, false);
                        for (int i = 0; i < lstCapture.size(); i++) {
                            Bitmap reizeImage = Bitmap.createScaledBitmap(lstCapture.get(i), 1024, 1024, false);
                            if (imgString.equals("") == true)
                                imgString += CLocal.convertBitmapToString(reizeImage);
                            else
                                imgString += ";" + CLocal.convertBitmapToString(reizeImage);
                        }
//                        Bitmap reizeImage = Bitmap.createScaledBitmap(imgCapture, 1024, 1024, false);
//                        imgString = CLocal.convertBitmapToString(reizeImage);
                    }

                    String result = ws.themMoNuoc(edtMaDN.getText().toString(), imgString, edtNgayMN.getText().toString(), edtChiSoMN.getText().toString(), edtNiemChi.getText().toString(), spnMauSac.getSelectedItem().toString(), CLocal.MaNV);
                    if (Boolean.parseBoolean(result) == true) {
                        CLocal.listDongNuocView.get(STT).setMoNuoc(true);
                        CLocal.listDongNuocView.get(STT).setNgayMN(edtNgayMN.getText().toString());
                        CLocal.listDongNuocView.get(STT).setChiSoMN(edtChiSoMN.getText().toString());
                        CLocal.listDongNuocView.get(STT).setNiemChiMN(edtNiemChi.getText().toString());
                        CLocal.listDongNuocView.get(STT).setMauSacMN(spnMauSac.getSelectedItem().toString());
                        return "THÀNH CÔNG";
                    } else
                        return "THẤT BẠI";
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
            CLocal.showPopupMessage(ActivityMoNuoc.this, s, "center");
        }

    }
}
