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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class ActivityDongNuoc extends AppCompatActivity {
    private ImageButton ibtnChupHinh;
    private ImageView imgviewUpdateDiaChiDHN;
    private EditText edtMaDN, edtDanhBo, edtMLT, edtHoTen, edtDiaChi, edtDiaChiDHN, edtNgayDN, edtChiSoDN, edtNiemChi, edtHieu, edtCo, edtSoThan, edtLyDo, edtKhoaKhac_GhiChu;
    private Spinner spnChiMatSo, spnChiKhoaGoc, spnViTri;
    private Button btnDongNuoc, btnIn;
    private CheckBox chkButChi, chkKhoaTu, chkKhoaKhac;
    private String imgPath;
    private Bitmap imgCapture;
    public ArrayList<Bitmap> lstCapture;
    private RecyclerView recyclerView;
    private CustomAdapterRecyclerViewImage customAdapterRecyclerViewImage;
    private CMarshMallowPermission CMarshMallowPermission = new CMarshMallowPermission(ActivityDongNuoc.this);
    private int STT = -1;
    private CWebservice ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dong_nuoc);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ws = new CWebservice();

        edtMaDN = (EditText) findViewById(R.id.edtMaDN);
        edtDanhBo = (EditText) findViewById(R.id.edtDanhBo);
        edtMLT = (EditText) findViewById(R.id.edtMLT);
        edtHoTen = (EditText) findViewById(R.id.edtHoTen);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtDiaChiDHN = (EditText) findViewById(R.id.edtDiaChiDHN);
        edtHieu = (EditText) findViewById(R.id.edtHieu);
        edtCo = (EditText) findViewById(R.id.edtCo);
        edtSoThan = (EditText) findViewById(R.id.edtSoThan);
        edtNgayDN = (EditText) findViewById(R.id.edtNgayDN);
        edtChiSoDN = (EditText) findViewById(R.id.edtChiSoDN);
        edtNiemChi = (EditText) findViewById(R.id.edtNiemChi);
        edtLyDo = (EditText) findViewById(R.id.edtLyDo);
        edtKhoaKhac_GhiChu = (EditText) findViewById(R.id.edtKhoaKhac_GhiChu);
        spnChiMatSo = (Spinner) findViewById(R.id.spnChiMatSo);
        spnChiKhoaGoc = (Spinner) findViewById(R.id.spnChiKhoaGoc);
        spnViTri = (Spinner) findViewById(R.id.spnViTri);

//        imgThumb = (ImageView) findViewById(R.id.imgThumb);
        ibtnChupHinh = (ImageButton) findViewById(R.id.ibtnChupHinh);
        lstCapture = new ArrayList<Bitmap>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        btnDongNuoc = (Button) findViewById(R.id.btnDongNuoc);
        btnIn = (Button) findViewById(R.id.btnIn);
        chkButChi = (CheckBox) findViewById(R.id.chkButChi);
        chkKhoaTu = (CheckBox) findViewById(R.id.chkKhoaTu);
        chkKhoaKhac = (CheckBox) findViewById(R.id.chkKhoaKhac);
        imgviewUpdateDiaChiDHN = (ImageView) findViewById(R.id.imgviewUpdateDiaChiDHN);

//        final MyAsyncTask_Thermal myAsyncTask_thermal = new MyAsyncTask_Thermal();
//        myAsyncTask_thermal.execute();

        imgviewUpdateDiaChiDHN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTask_updateDiaChiDHN myAsyncTask_updateDiaChiDHN = new MyAsyncTask_updateDiaChiDHN();
                myAsyncTask_updateDiaChiDHN.execute();
            }
        });

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
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDongNuoc.this);
                builder.setTitle("Thông Báo");
                builder.setMessage("Chọn lựa hành động");
                builder.setCancelable(false);
                builder.setPositiveButton("Chụp từ camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri imgUri = createImageUri();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(ActivityDongNuoc.this.getPackageManager()) != null) {
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
//                            Intent intent = new Intent();
//                            intent.setType("image/*");
//                            intent.setAction(Intent.ACTION_GET_CONTENT);
//                            intent.addCategory(Intent.CATEGORY_OPENABLE);
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

//        btnKiemTra.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (CLocal.checkNetworkAvailable(ActivityDongNuoc.this) == false) {
//                    Toast.makeText(ActivityDongNuoc.this, "Không có Internet", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                if (edtMaDN.getText().toString().equals("") == false) {
//                    MyAsyncTask myAsyncTask = new MyAsyncTask();
//                    myAsyncTask.execute("Kiểm Tra");
//                }
//            }
//        });

        btnDongNuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.checkNetworkAvailable(ActivityDongNuoc.this) == false) {
                    CLocal.showToastMessage(ActivityDongNuoc.this, "Không có Internet");
                    return;
                }
                if (edtMaDN.getText().toString().equals("") == false && edtChiSoDN.getText().toString().equals("") == false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("DongNuoc");
                }
            }
        });

        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                thermalPrinter = new ThermalPrinter(ActivityDongNuoc.this);
//                if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
//                    if (CLocal.listDongNuocView.get(STT).isDongNuoc() == true)
//                        thermalPrinter.printDongNuoc(CLocal.listDongNuocView.get(STT));
                if (CLocal.serviceThermalPrinter != null) {
                    try {
                        CLocal.serviceThermalPrinter.printDongNuoc(CLocal.listDongNuocView.get(STT));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        try {
//            String MaDN = getIntent().getStringExtra("MaDN");
//            if (MaDN.equals("") == false) {
//                fillDongNuoc(MaDN);
//            }
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
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                if (imgPath != null && imgPath != "") {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                    bitmap = CLocal.imageOreintationValidator(bitmap, imgPath);
                    imgCapture = bitmap;
                }
//            imgThumb.setImageBitmap(bitmap);

            } else if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                Uri uri = data.getData();
//            InputStream imageStream;
//            imageStream = getContentResolver().openInputStream(imageUri);
//            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

//            Bitmap bitmap = null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
                String strPath = CLocal.getPathFromUri(this, uri);
                Bitmap bitmap = BitmapFactory.decodeFile(strPath);
                bitmap = CLocal.imageOreintationValidator(bitmap, strPath);
                imgCapture = bitmap;
//            imgThumb.setImageBitmap(bitmap);
            }
            if (imgCapture != null) {
                lstCapture.add(imgCapture);
                customAdapterRecyclerViewImage = new CustomAdapterRecyclerViewImage(ActivityDongNuoc.this, lstCapture);
                recyclerView.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(customAdapterRecyclerViewImage);
            }
        } catch (Exception ex) {
            CLocal.showToastMessage(getApplicationContext(), ex.getMessage());
        }
    }

    public Uri createImageUri() {
        try {
            File filesDir = ActivityDongNuoc.this.getExternalFilesDir(CLocal.pathPicture);
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
                Uri photoURI = FileProvider.getUriForFile(ActivityDongNuoc.this, "thutien_file_provider", photoFile);
                uri = photoURI;
            }
            imgPath = photoFile.getAbsolutePath();
            return uri;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void fillDongNuoc(String MaDN) {
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
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    edtNgayDN.setText(currentDate.format(new Date()));
                    edtChiSoDN.setText(jsonObject.getString("ChiSoDN"));
                    setSpinnerSelection(spnChiMatSo, jsonObject.getString("ChiMatSo"));
                    setSpinnerSelection(spnChiKhoaGoc, jsonObject.getString("ChiKhoaGoc"));
                    edtLyDo.setText(jsonObject.getString("LyDo"));
                    break;
                }
            }
        } catch (Exception ex) {
            CLocal.showToastMessage(ActivityDongNuoc.this, ex.getMessage());
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
                edtDiaChiDHN.setText(en.getDiaChiDHN());
                edtHieu.setText(en.getHieu());
                edtCo.setText(en.getCo());
                edtSoThan.setText(en.getSoThan());

                setSpinnerSelection(spnChiMatSo, en.getChiMatSo());
                setSpinnerSelection(spnChiKhoaGoc, en.getChiKhoaGoc());
                setSpinnerSelection(spnViTri, en.getViTri());
                edtLyDo.setText(en.getLyDo());
                if (en.isDongNuoc2() == false) {
                    if (en.isDongNuoc() == true) {
                        edtNgayDN.setText(en.getNgayDN());
                        edtChiSoDN.setText(en.getChiSoDN());
                        edtNiemChi.setText(en.getNiemChi());
                    } else {
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        edtNgayDN.setText(currentDate.format(new Date()));
                    }
                } else {
                    edtNgayDN.setText(en.getNgayDN1());
                    edtChiSoDN.setText(en.getChiSoDN1());
                    edtNiemChi.setText(en.getNiemChi1());
                }
            }
        } catch (Exception ex) {
            CLocal.showToastMessage(ActivityDongNuoc.this, ex.getMessage());
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
//        Dialog builder = new Dialog(ActivityDongNuoc.this);
//        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                //nothing;
//            }
//        });
//
//        ImageView imageView = new ImageView(ActivityDongNuoc.this);
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
//                thermalPrinter = new ThermalPrinter(ActivityDongNuoc.this);
            } catch (Exception ex) {
                CLocal.showToastMessage(ActivityDongNuoc.this, ex.getMessage());
            }
            return null;
        }
    }

    public class MyAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityDongNuoc.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "DongNuoc":
                    if (Boolean.parseBoolean(ws.checkExist_DongNuoc(edtMaDN.getText().toString())) == true)
                        return "ĐÃ NHẬP RỒI";

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

                    String result = ws.themDongNuoc(edtMaDN.getText().toString(), edtDanhBo.getText().toString(), edtMLT.getText().toString(), edtHoTen.getText().toString(), edtDiaChi.getText().toString(), imgString, edtNgayDN.getText().toString(), edtChiSoDN.getText().toString(),
                            String.valueOf(chkButChi.isChecked()), String.valueOf(chkKhoaTu.isChecked()), edtNiemChi.getText().toString(), String.valueOf(chkKhoaKhac.isChecked()), edtKhoaKhac_GhiChu.getText().toString(),
                            edtHieu.getText().toString(), edtCo.getText().toString(), edtSoThan.getText().toString(), spnChiMatSo.getSelectedItem().toString(), spnChiKhoaGoc.getSelectedItem().toString(), spnViTri.getSelectedItem().toString(), edtLyDo.getText().toString(), CLocal.MaNV);
                    if (Boolean.parseBoolean(result) == true) {
                        CLocal.listDongNuocView.get(STT).setDongNuoc(true);
                        CLocal.listDongNuocView.get(STT).setNgayDN(edtNgayDN.getText().toString());
                        CLocal.listDongNuocView.get(STT).setChiSoDN(edtChiSoDN.getText().toString());
                        CLocal.listDongNuocView.get(STT).setButChi(chkButChi.isChecked());
                        CLocal.listDongNuocView.get(STT).setKhoaTu(chkKhoaTu.isChecked());
                        CLocal.listDongNuocView.get(STT).setNiemChi(edtNiemChi.getText().toString());
                        CLocal.listDongNuocView.get(STT).setKhoaKhac(chkKhoaKhac.isChecked());
                        CLocal.listDongNuocView.get(STT).setKhoaKhac_GhiChu(edtKhoaKhac_GhiChu.getText().toString());
                        CLocal.listDongNuocView.get(STT).setChiMatSo(spnChiMatSo.getSelectedItem().toString());
                        CLocal.listDongNuocView.get(STT).setChiKhoaGoc(spnChiKhoaGoc.getSelectedItem().toString());
                        CLocal.listDongNuocView.get(STT).setViTri(spnViTri.getSelectedItem().toString());
                        CLocal.listDongNuocView.get(STT).setLyDo(edtLyDo.getText().toString());

                        CLocal.updateTinhTrangParent(CLocal.listDongNuoc, CLocal.listDongNuocView.get(STT));
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
            CLocal.showPopupMessage(ActivityDongNuoc.this, s, "center");
        }

    }

    public class MyAsyncTask_updateDiaChiDHN extends AsyncTask<String, Void, String[]> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityDongNuoc.this);
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
                result = ws.update_DiaChiDHN(CLocal.MaNV, edtDanhBo.getText().toString().replace(" ", ""), edtDiaChiDHN.getText().toString());
                results = result.split(";");
                return results;
            } catch (Exception ex) {
                return new String[]{"false;" + ex.getMessage()};
            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (Boolean.parseBoolean(strings[0]) == true) {
                CLocal.showPopupMessage(ActivityDongNuoc.this, "THÀNH CÔNG", "center");
            } else
                CLocal.showPopupMessage(ActivityDongNuoc.this, "THẤT BẠI\n" + strings[1], "center");
        }
    }
}
