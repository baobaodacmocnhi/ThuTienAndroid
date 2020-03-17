package vn.com.capnuoctanhoa.thutienandroid;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.com.capnuoctanhoa.thutienandroid.Bluetooth.ThermalPrinter;
import vn.com.capnuoctanhoa.thutienandroid.Class.CEntityParent;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CMarshMallowPermission;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityDanhSachDongNuoc;
import vn.com.capnuoctanhoa.thutienandroid.HanhThu.ActivityDanhSachHanhThu;
import vn.com.capnuoctanhoa.thutienandroid.HanhThu.ActivityHoaDonDienTu_DanhSach;
import vn.com.capnuoctanhoa.thutienandroid.LenhHuy.ActivityLenhHuy;
import vn.com.capnuoctanhoa.thutienandroid.QuanLy.ActivityQuanLy;
import vn.com.capnuoctanhoa.thutienandroid.Service.ServiceAppKilled;
import vn.com.capnuoctanhoa.thutienandroid.Service.ServiceFirebaseInstanceID;
import vn.com.capnuoctanhoa.thutienandroid.TamThu.ActivityTamThu;

public class MainActivity extends AppCompatActivity {
    private ImageButton imgbtnDangNhap, imgbtnHanhThu,imgbtnTamThu, imgbtnDongNuoc, imgbtnQuanLy, imgbtnTimKiem, imgbtnLenhHuy, imgbtnHoaDonDienTu;
    private TextView txtUser, txtQuanLy, txtLenhHuy, txtHoaDonDienTu, txtVersion;
    private CMarshMallowPermission cMarshMallowPermission = new CMarshMallowPermission(MainActivity.this);
    private String pathdownloaded;
    PackageInfo packageInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (cMarshMallowPermission.checkAllPermissionForAPP()) {

        }

//        File folder = new File(CLocal.pathRoot);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//        folder = new File(CLocal.pathPicture);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//        folder = new File(CLocal.pathFile);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }

        CLocal.sharedPreferencesre = getSharedPreferences(CLocal.fileName_SharedPreferences, MODE_PRIVATE);

        imgbtnDangNhap = (ImageButton) findViewById(R.id.imgbtnDangNhap);
        imgbtnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityDangNhap.class);
                startActivity(intent);
//                CLocal.showPopupMessage(MainActivity.this,"abcadfasdf");

            }
        });

        imgbtnHanhThu = (ImageButton) findViewById(R.id.imgbtnHanhThu);
        imgbtnHanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityDanhSachHanhThu.class);
                startActivity(intent);
//                ThermalPrinter thermalPrinter = new ThermalPrinter(MainActivity.this);
//                if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
//                    thermalPrinter.printPhieuBao(CLocal.listHanhThu.get(0), CLocal.listHanhThu.get(0).getLstHoaDon().get(0));
            }
        });

        imgbtnTamThu = (ImageButton) findViewById(R.id.imgbtnTamThu);
        imgbtnTamThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityTamThu.class);
                startActivity(intent);
//                ThermalPrinter thermalPrinter = new ThermalPrinter(MainActivity.this);
//                if (thermalPrinter != null && thermalPrinter.getBluetoothDevice() != null)
//                    thermalPrinter.printPhieuBao_ESC(CLocal.listHanhThu.get(0), CLocal.listHanhThu.get(0).getLstHoaDon().get(0));
            }
        });

        imgbtnDongNuoc = (ImageButton) findViewById(R.id.imgbtnDongNuoc);
        imgbtnDongNuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityDanhSachDongNuoc.class);
                startActivity(intent);
            }
        });

        imgbtnQuanLy = (ImageButton) findViewById(R.id.imgbtnQuanLy);
        imgbtnQuanLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityQuanLy.class);
                startActivity(intent);
            }
        });

        imgbtnTimKiem = (ImageButton) findViewById(R.id.imgbtnTimKiem);
        imgbtnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivitySearchKhachHang.class);
                startActivity(intent);
            }
        });

        imgbtnLenhHuy = (ImageButton) findViewById(R.id.imgbtnLenhHuy);
        imgbtnLenhHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityLenhHuy.class);
                startActivity(intent);
            }
        });

        imgbtnHoaDonDienTu = (ImageButton) findViewById(R.id.imgbtnHoaDonDienTu);
        imgbtnHoaDonDienTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityHoaDonDienTu_DanhSach.class);
                startActivity(intent);
            }
        });

        txtUser = (TextView) findViewById(R.id.txtUser);
        txtQuanLy = (TextView) findViewById(R.id.txtQuanLy);
        txtLenhHuy = (TextView) findViewById(R.id.txtLenhHuy);
        txtHoaDonDienTu = (TextView) findViewById(R.id.txtHoaDonDienTu);
        txtVersion = (TextView) findViewById(R.id.txtVersion);
        try {
            packageInfo = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        txtVersion.setText("V" + packageInfo.versionName);
        if (CLocal.checkNetworkAvailable(MainActivity.this) == true) {
            MyAsyncTask myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute("Version");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Intent intent = new Intent(this, ServiceAppKilled.class);
            startService(intent);

            if (CLocal.sharedPreferencesre.getString("UID", "").equals("") == true) {
                ServiceFirebaseInstanceID serviceFirebaseInstanceID = new ServiceFirebaseInstanceID();
                serviceFirebaseInstanceID.onTokenRefresh();
            }
            if (CLocal.sharedPreferencesre.getString("jsonHanhThu", "").equals("") == false) {
//                CLocal.jsonHanhThu = new JSONArray(CLocal.sharedPreferencesre.getString("jsonHanhThu", ""));
//                if (CLocal.jsonHanhThu.length() > 1000)
//                    CLocal.jsonHanhThu = null;
                CLocal.listHanhThu = new Gson().fromJson(CLocal.sharedPreferencesre.getString("jsonHanhThu", ""), new TypeToken<ArrayList<CEntityParent>>() {
                }.getType());
                if (CLocal.listHanhThu.size() > 2000)
                    CLocal.listHanhThu = null;
            }
            if (CLocal.sharedPreferencesre.getString("jsonDongNuoc", "").equals("") == false) {
//                CLocal.jsonDongNuoc = new JSONArray(CLocal.sharedPreferencesre.getString("jsonDongNuoc", ""));
//                CLocal.jsonDongNuocChild = new JSONArray(CLocal.sharedPreferencesre.getString("jsonDongNuocChild", ""));
//                if(CLocal.jsonDongNuoc.length()>1000)
//                    CLocal.jsonDongNuoc=null;
                CLocal.listDongNuoc = new Gson().fromJson(CLocal.sharedPreferencesre.getString("jsonDongNuoc", ""), new TypeToken<ArrayList<CEntityParent>>() {
                }.getType());
                if (CLocal.listDongNuoc.size() > 2000)
                    CLocal.listDongNuoc = null;
            }
            if (CLocal.sharedPreferencesre.getString("jsonMessage", "").equals("") == false) {
                CLocal.jsonMessage = new JSONArray(CLocal.sharedPreferencesre.getString("jsonMessage", ""));
            }
            if (CLocal.sharedPreferencesre.getString("ThermalPrinter", "").equals("") == false) {
                CLocal.ThermalPrinter = CLocal.sharedPreferencesre.getString("ThermalPrinter", "");
            }
            if (CLocal.sharedPreferencesre.getString("MethodPrinter", "").equals("") == false) {
                CLocal.MethodPrinter = CLocal.sharedPreferencesre.getString("MethodPrinter", "");
            }

            imgbtnQuanLy.setVisibility(View.GONE);
            txtQuanLy.setVisibility(View.GONE);
            imgbtnLenhHuy.setVisibility(View.GONE);
            txtLenhHuy.setVisibility(View.GONE);
            imgbtnHoaDonDienTu.setVisibility(View.GONE);
            txtHoaDonDienTu.setVisibility(View.GONE);
            if (CLocal.sharedPreferencesre.getBoolean("Login", false) == true) {
                CLocal.MaNV = CLocal.sharedPreferencesre.getString("MaNV", "");
                CLocal.HoTen = CLocal.sharedPreferencesre.getString("HoTen", "");
                CLocal.DienThoai = CLocal.sharedPreferencesre.getString("DienThoai", "");
                CLocal.SyncTrucTiep = CLocal.sharedPreferencesre.getBoolean("SyncTrucTiep", true);
                CLocal.MethodPrinter = CLocal.sharedPreferencesre.getString("MethodPrinter", "ESC");
                txtUser.setText("Xin chào " + CLocal.HoTen);
                txtUser.setTextColor(getResources().getColor(R.color.colorLogin));
                imgbtnDangNhap.setImageResource(R.drawable.ic_login);
                if (CLocal.sharedPreferencesre.getBoolean("Doi", false) == true && CLocal.sharedPreferencesre.getString("jsonTo", "").equals("") == false) {
                    CLocal.Doi = CLocal.sharedPreferencesre.getBoolean("Doi", false);
                    CLocal.jsonTo = new JSONArray(CLocal.sharedPreferencesre.getString("jsonTo", ""));
                    CLocal.jsonNhanVien = new JSONArray(CLocal.sharedPreferencesre.getString("jsonNhanVien", ""));
                    imgbtnQuanLy.setVisibility(View.VISIBLE);
                    txtQuanLy.setVisibility(View.VISIBLE);
                    imgbtnLenhHuy.setVisibility(View.VISIBLE);
                    txtLenhHuy.setVisibility(View.VISIBLE);
                    imgbtnHoaDonDienTu.setVisibility(View.VISIBLE);
                    txtHoaDonDienTu.setVisibility(View.VISIBLE);
                } else if (CLocal.sharedPreferencesre.getBoolean("ToTruong", false) == true && CLocal.sharedPreferencesre.getString("jsonNhanVien", "").equals("") == false) {
                    CLocal.ToTruong = CLocal.sharedPreferencesre.getBoolean("ToTruong", false);
                    CLocal.jsonNhanVien = new JSONArray(CLocal.sharedPreferencesre.getString("jsonNhanVien", ""));
                    CLocal.MaTo = CLocal.sharedPreferencesre.getString("MaTo", "");
                    imgbtnQuanLy.setVisibility(View.VISIBLE);
                    txtQuanLy.setVisibility(View.VISIBLE);
                    imgbtnHoaDonDienTu.setVisibility(View.VISIBLE);
                    txtHoaDonDienTu.setVisibility(View.VISIBLE);
                } else if (CLocal.sharedPreferencesre.getBoolean("TestApp", false) == true) {
                    imgbtnHoaDonDienTu.setVisibility(View.VISIBLE);
                    txtHoaDonDienTu.setVisibility(View.VISIBLE);
                }
            } else {
                txtUser.setText("Xin hãy đăng nhập");
                txtUser.setTextColor(getResources().getColor(R.color.colorLogout));
                imgbtnDangNhap.setImageResource(R.drawable.ic_logout);
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_search_khach_hang:
                intent = new Intent(MainActivity.this, ActivitySearchKhachHangWeb.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                intent = new Intent(MainActivity.this, ActivitySettings.class);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateApp(String versionServer) {
        try {
            String versionDevice = packageInfo.versionName;
            if (versionServer.equals("") == false && versionServer.equals("False") == false && versionServer.equals("java.net.ConnectException: Connection refused") == false && versionDevice.equals(versionServer) == false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Cập nhật");
                builder.setMessage("Đã có phiên bản mới, Bạn hãy cập nhật");
                builder.setCancelable(false);
                builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("Cập Nhật", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://113.161.88.180:1989/app/thutien.apk"));
//                        startActivity(browserIntent);
//                        if (CLocal.checkNetworkAvailable(MainActivity.this) == true) {
//                            MyAsyncTask myAsyncTask = new MyAsyncTask();
//                            myAsyncTask.execute("Version");
//                        }
                        if (cMarshMallowPermission.checkPermissionForExternalStorage() == true) {
                            MyAsyncTaskDownload myAsyncTask = new MyAsyncTaskDownload();
                            myAsyncTask.execute("http://113.161.88.180:1989/app/thutien.apk");
                        } else
                            CLocal.showPopupMessage(MainActivity.this, "Bạn chưa cấp quyền cho App");
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyAsyncTask extends AsyncTask<String, String, String> {
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "Version":
                    return ws.getVersion();
                case "Download":
                    break;
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
            if (s.contains("Connection refused") == false)
                updateApp(s);
        }
    }

    public class MyAsyncTaskDownload extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            int count;
            try {
                File folder = new File(CLocal.pathRoot);
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdirs();
                }
                if (success) {
                    // Do something on success
                } else {
                    // Do something else on failure
                }
                URL url = new URL(strings[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                String[] path = url.getPath().split("/");
                String[] fileName = path[path.length - 1].split("\\.");

                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                //extension must change (mp3,mp4,zip,apk etc.)
                pathdownloaded = CLocal.pathRoot + fileName[0] + "." + fileName[1];
                OutputStream output = new FileOutputStream(pathdownloaded);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            installapk();
        }
    }

    public void installapk() {
        try {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                File file = new File( pathdownloaded);
//                Uri data = FileProvider.getUriForFile(getBaseContext(), "thutien_file_provider", file);
//                intent.setDataAndType(data, "application/vnd.android.package-archive");
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(intent);
            Intent intent;
            File file = new File(pathdownloaded);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkUri = FileProvider.getUriForFile(MainActivity.this, "thutien_file_provider", file);
                intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.setData(apkUri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                Uri apkUri = Uri.fromFile(file);
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            startActivity(intent);
            finish();

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CMarshMallowPermission.READ_EXTERNAL_STORAGE_REQUEST_CODE:
            case CMarshMallowPermission.WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MyAsyncTaskDownload myAsyncTask = new MyAsyncTaskDownload();
                    myAsyncTask.execute("http://113.161.88.180:1989/app/thutien.apk");
                } else {
                    //not granted
                }
                break;
            case CMarshMallowPermission.REQUEST_INSTALL_PACKAGES_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    installapk();
                } else {
                    //not granted
                }
                break;
            case CMarshMallowPermission.APP_PERMISSIONS_REQUEST_CODE:
                HashMap<String, Integer> permissionResults = new HashMap<>();
                int deniedCount = 0;

                //gather permission grant results
                for (int i = 0; i < grantResults.length; i++)
                    //add only permissions which are denied
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        permissionResults.put(permissions[i], grantResults[i]);
                        deniedCount++;
                    }
                //check if all permissions are granted
                if (deniedCount == 0) {
                    //proceed ahead with the app
                }
                //alleast one or all permissions are denied
                else {
                    for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
                        String permName = entry.getKey();
                        int permResult = entry.getValue();
                        //permissions is denied (this is the first time, when "never ask again" is not checked)
                        //so ask again explaining to usage of permission
                        //shouldShowRequestPermissionRationale will return true
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                            //show dialog of explanation
                            showDialog("", "App cần cấp Tất Cả Quyền để hoạt động ổn định", "Yes, Cấp Quyền", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    cMarshMallowPermission.checkAllPermissionForAPP();
                                }
                            }, "No, Đóng App", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }, false);
                        }
                        //permissions is denied (and never ask again is checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            //ask user to go to settings and manually allow permissions
                            showDialog("", "Bạn đã Từ Chối Cấp Quyền. Đồng ý Tất Cả Quyền tại [Cài Đặt] > [Quyền Ứng Dụng]", "Đi đến Cài Đặt", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    //go to app settings
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }, "No, Đóng App", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }, false);
                        }
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public AlertDialog showDialog(String title, String msg, String positiveLabel, DialogInterface.OnClickListener positiveOnClick, String negativeLabel, DialogInterface.OnClickListener negativeOnClick, boolean isCancelAble) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(isCancelAble);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, positiveOnClick);
        builder.setNegativeButton(negativeLabel, negativeOnClick);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Intent startApp = new Intent("vn.com.capnuoctanhoa.thutienandroid");
//        startActivity(startApp);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
