package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.MarshMallowPermission;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDongNuoc extends AppCompatActivity {
    private ImageButton ibtnChupHinh;
    private ImageView imgThumb;
    private TextView txtHoaDon;
    private EditText edtMaDN, edtDanhBo, edtMLT, edtHoTen, edtDiaChi, edtNgayDN, edtChiSoDN, edtHieu, edtCo, edtSoThan, edtLyDo;
    private Spinner spnChiMatSo, spnChiKhoaGoc;
    private Button btnKiemTra, btnDongNuoc;
    private String imgPath;
    private MarshMallowPermission marshMallowPermission = new MarshMallowPermission(ActivityDongNuoc.this);

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

        txtHoaDon=(TextView)  findViewById(R.id.txtHoaDon);
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (marshMallowPermission.checkPermissionForExternalStorage() == false) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    }
                    if (marshMallowPermission.checkPermissionForExternalStorage() == false)
                        return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDongNuoc.this);
                builder.setTitle("Thông Báo");
                builder.setMessage("Chọn lựa hành động");
                builder.setCancelable(false);
                builder.setPositiveButton("Chụp từ camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       Uri  imgUri = createImageUri();
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

        imgThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImgThumb();
            }
        });

        btnKiemTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.checkNetworkAvailable(ActivityDongNuoc.this) == false) {
                    Toast.makeText(ActivityDongNuoc.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                if (edtMaDN.getText().toString().equals("") == false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("Kiểm Tra");
                }
            }
        });

        btnDongNuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.checkNetworkAvailable(ActivityDongNuoc.this) == false) {
                    Toast.makeText(ActivityDongNuoc.this, "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                if (edtMaDN.getText().toString().equals("") == false && edtChiSoDN.getText().toString().equals("") == false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("Đóng Nước");
                }
            }
        });

        try {
            String MaDN = getIntent().getStringExtra("MaDN");
            if (MaDN.equals("") == false) {
                fillDongNuoc(MaDN);
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            bitmap = imageOreintationValidator(bitmap, imgPath);
            imgThumb.setImageBitmap(bitmap);
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
            bitmap = imageOreintationValidator(bitmap, strPath);
            imgThumb.setImageBitmap(bitmap);
        }
    }

    public Uri createImageUri() {
        try {
            Uri uri;
            File photoFile = createFile();
            if (Build.VERSION.SDK_INT < 21) {
                // Từ android 5.0 trở xuống. khi ta sử dụng FileProvider.getUriForFile() sẽ trả về ngoại lệ FileUriExposedException
                // Vì vậy mình sử dụng Uri.fromFile đề lấy ra uri cho file ảnh
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                photoFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "" + timeStamp + ".jpg");
                uri = Uri.fromFile(photoFile);
            } else {
                // từ android 5.0 trở lên ta có thể sử dụng Uri.fromFile() và FileProvider.getUriForFile() để trả về uri file sau khi chụp.
                // Nhưng bắt buộc từ Android 7.0 trở lên ta phải sử dụng FileProvider.getUriForFile() để trả về uri cho file đó.
                Uri photoURI = FileProvider.getUriForFile(ActivityDongNuoc.this, "thutien_camera_fullsize", photoFile);
                uri = photoURI;
            }
            imgPath = photoFile.getAbsolutePath();
            return uri;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public File createFile() {
        File filesDir = ActivityDongNuoc.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            file = File.createTempFile(timeStamp, ".jpg", filesDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public Bitmap imageOreintationValidator(Bitmap bitmap, String path) {
        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

    public String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        String str = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
        return str;
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
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                    edtNgayDN.setText(currentDate.format(new Date()));
                    edtChiSoDN.setText(jsonObject.getString("ChiSoDN").replace("null", ""));
                    setSpinnerSelection(spnChiMatSo, jsonObject.getString("ChiMatSo"));
                    setSpinnerSelection(spnChiKhoaGoc, jsonObject.getString("ChiKhoaGoc"));
                    edtLyDo.setText(jsonObject.getString("LyDo").replace("null", ""));
                    txtHoaDon.setText(jsonObject.getString("HoaDon"));
                    break;
                }
            }
        } catch (Exception ex) {
            Toast.makeText(ActivityDongNuoc.this, ex.toString(), Toast.LENGTH_SHORT).show();
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

    public void showImgThumb() {
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
        imageView.setImageBitmap(((BitmapDrawable) imgThumb.getDrawable()).getBitmap());
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(1000, 1000));
        builder.show();
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
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "Kiểm Tra":
                    if (Boolean.parseBoolean(ws.checkHoaDon_DongNuoc(edtMaDN.getText().toString())) == true)
                        return "ĐÃ GIẢI TRÁCH";
                    else
                        return "CÒN TỒN";
                case "Đóng Nước":
                    if (Boolean.parseBoolean(ws.checkExist_DongNuoc(edtMaDN.getText().toString())) == false) {
                        Bitmap reizeImage = Bitmap.createScaledBitmap(((BitmapDrawable) imgThumb.getDrawable()).getBitmap(), 1024, 1024, false);
                        String imgString = convertBitmapToString(reizeImage);

                        return ws.themDongNuoc(edtMaDN.getText().toString(), edtDanhBo.getText().toString(), edtMLT.getText().toString(), edtHoTen.getText().toString(), edtDiaChi.getText().toString(),
                                imgString, edtNgayDN.getText().toString(), edtChiSoDN.getText().toString(), edtHieu.getText().toString(), edtCo.getText().toString(), edtSoThan.getText().toString(),
                                spnChiMatSo.getSelectedItem().toString(), spnChiKhoaGoc.getSelectedItem().toString(), edtLyDo.getText().toString(), CLocal.sharedPreferencesre.getString("MaNV", ""));
                    } else
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
            if(Boolean.parseBoolean(s)==true)
            CLocal.showPopupMessage(ActivityDongNuoc.this, "THÀNH CÔNG");
            else
                CLocal.showPopupMessage(ActivityDongNuoc.this, "THẤT BẠI");
        }

    }
}
