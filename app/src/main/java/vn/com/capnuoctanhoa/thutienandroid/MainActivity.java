package vn.com.capnuoctanhoa.thutienandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityDanhSachDongNuoc;
import vn.com.capnuoctanhoa.thutienandroid.HanhThu.ActivityDanhSachHanhThu;
import vn.com.capnuoctanhoa.thutienandroid.Service.ServiceAppKilled;

public class MainActivity extends AppCompatActivity {
    private ImageButton imgbtnDangNhap, imgbtnHanhThu, imgbtnDongNuoc;
    private TextView txtUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CLocal.sharedPreferencesre = getSharedPreferences(CLocal.FileName, MODE_PRIVATE);

        imgbtnDangNhap = (ImageButton) findViewById(R.id.imgbtnDangNhap);
        imgbtnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityDangNhap.class);
                startActivity(intent);
            }
        });

        imgbtnHanhThu = (ImageButton) findViewById(R.id.imgbtnHanhThu);
        imgbtnHanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityDanhSachHanhThu.class);
                startActivity(intent);
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

        txtUser = (TextView) findViewById(R.id.txtUser);

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

            if (CLocal.sharedPreferencesre.getString("jsonHanhThu", "").equals("") == false) {
                CLocal.jsonHanhThu = new JSONArray(CLocal.sharedPreferencesre.getString("jsonHanhThu", ""));
            }
            if (CLocal.sharedPreferencesre.getString("jsonDongNuoc", "").equals("") == false) {
                CLocal.jsonDongNuoc = new JSONArray(CLocal.sharedPreferencesre.getString("jsonDongNuoc", ""));
                CLocal.jsonDongNuocChild = new JSONArray(CLocal.sharedPreferencesre.getString("jsonDongNuocChild", ""));
            }
            if (CLocal.sharedPreferencesre.getString("jsonMessage", "").equals("") == false) {
                CLocal.jsonMessage = new JSONArray(CLocal.sharedPreferencesre.getString("jsonMessage", ""));
            }
            if (CLocal.checkNetworkAvailable(getApplicationContext()) == false) {
                Toast.makeText(MainActivity.this, "Không có Internet", Toast.LENGTH_LONG).show();
                return;
            }

            if (CLocal.sharedPreferencesre.getBoolean("Login", false) == true) {
                txtUser.setText("Xin chào " + CLocal.sharedPreferencesre.getString("HoTen", ""));
                txtUser.setTextColor(getResources().getColor(R.color.colorLogin));
                imgbtnDangNhap.setImageResource(R.drawable.ic_login);
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
        switch (item.getItemId()) {
            case R.id.action_search_khach_hang:
                Intent intent= new Intent(MainActivity.this, ActivitySearchKhachHang.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:

                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateApp(String versionServer) {
        try {
//            String versionServer=(String)myAsyncTask.execute("Version").get();
            PackageInfo packageInfo = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionDevice = packageInfo.versionName;
            if (versionServer.equals("") == false && versionServer.equals("False") == false && versionDevice.equals(versionServer) == false) {
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
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://113.161.88.180:1989/app/thutien.apk"));
                        startActivity(browserIntent);
//                        MyAsyncTask myAsyncTask = new MyAsyncTask();
//                        myAsyncTask.execute(new String[]{"Download","http://113.161.88.180:1989/app/thutien.apk"});
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
                    int count;
                    try {
                        String pathSave = Environment.getExternalStorageDirectory().toString() + "/DownLoad/";

                        URL url = new URL(strings[1]);

                        String[] path = url.getPath().split("/");
                        String fileName = path[path.length - 1];

                        URLConnection conection = url.openConnection();
                        conection.connect();
                        // getting file length
                        int lenghtOfFile = conection.getContentLength();

                        // input stream to read file - with 8k buffer
                        InputStream input = new BufferedInputStream(url.openStream(), 8192);

                        // Output stream to write file

                        OutputStream output = new FileOutputStream(pathSave + "/" + fileName);
                        byte data[] = new byte[1024];

                        long total = 0;
                        while ((count = input.read(data)) != -1) {
                            total += count;

                            // writing data to file
                            output.write(data, 0, count);

                        }

                        // flushing output
                        output.flush();

                        // closing streams
                        output.close();
                        input.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
            updateApp(s);
        }
    }
}
