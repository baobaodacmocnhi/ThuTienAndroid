package vn.com.capnuoctanhoa.thutienandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import vn.com.capnuoctanhoa.thutienandroid.HanhThu.ActivityHanhThu2;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ActivityDangNhap.class);
                startActivity(intent);
            }
        });

        Button btnHanhThu = (Button) findViewById(R.id.btnHanhThu);
        btnHanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SharedPreferences sharedPreferencesre= getSharedPreferences(CLocal.FileName_Local, MODE_PRIVATE);
                Toast.makeText(MainActivity.this, sharedPreferencesre.getString("UID", ""),
                        Toast.LENGTH_LONG).show();*/
                Intent intent = new Intent(MainActivity.this,ActivityHanhThu2.class);
                startActivity(intent);
            }
        });
    }
}
