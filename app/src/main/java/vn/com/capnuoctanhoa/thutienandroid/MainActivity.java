package vn.com.capnuoctanhoa.thutienandroid;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import vn.com.capnuoctanhoa.thutienandroid.HanhThu.ActivityHanhThu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                if(CLocal.CheckNetworkAvailable(getApplicationContext())==false)
                {
                    Toast.makeText(MainActivity.this,"Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });

    }

    public class MyAsyncTask extends AsyncTask<Void, String, Void>
    {
        CWebservice ws = new CWebservice();

        @Override
        protected Void doInBackground(Void... voids) {
            publishProgress(ws.GetVersion());
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            try {
                PackageInfo packageInfo = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
                String version = packageInfo.versionName;
                if(values[0].equals(version)==false)
                {
                    Toast.makeText(MainActivity.this,"Update", Toast.LENGTH_LONG).show();
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
