package vn.com.capnuoctanhoa.thutienandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityDongNuoc;
import vn.com.capnuoctanhoa.thutienandroid.HanhThu.ActivityHanhThu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Update();
        CLocal.sharedPreferencesre = getSharedPreferences(CLocal.FileName, MODE_PRIVATE);

        Button btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityDangNhap.class);
                startActivity(intent);
            }
        });

        Button btnHanhThu = (Button) findViewById(R.id.btnHanhThu);
        btnHanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityHanhThu.class);
                startActivity(intent);
            }
        });

        Button btnDongNuoc = (Button) findViewById(R.id.btnDongNuoc);
        btnDongNuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, version, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, ActivityDongNuoc.class);
                startActivity(intent);
            }
        });

    }

    private void Update()
    {
        if (CLocal.CheckNetworkAvailable(getApplicationContext()) == false) {
            Toast.makeText(MainActivity.this, "Không có Internet", Toast.LENGTH_LONG).show();
            return;
        }
        String versionServer = "";
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        try {
            versionServer = (String) myAsyncTask.execute(new String[]{"Version"}).get();
            PackageInfo packageInfo = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionDevice = packageInfo.versionName;
            if (versionDevice.equals(versionServer) == false) {
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
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
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyAsyncTask extends AsyncTask<String, String, String> {
        CWebservice ws = new CWebservice();

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "Version":
                    return ws.GetVersion();
                case "Download":
                    int count;
                    try {
                        String root = Environment.getExternalStorageDirectory().toString();

                        System.out.println("Downloading");
                        URL url = new URL(strings[1]);

                        URLConnection conection = url.openConnection();
                        conection.connect();
                        // getting file length
                        int lenghtOfFile = conection.getContentLength();

                        // input stream to read file - with 8k buffer
                        InputStream input = new BufferedInputStream(url.openStream(), 8192);

                        // Output stream to write file

                        OutputStream output = new FileOutputStream(root+"/downloadedfile.apk");
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
        }
    }
}
