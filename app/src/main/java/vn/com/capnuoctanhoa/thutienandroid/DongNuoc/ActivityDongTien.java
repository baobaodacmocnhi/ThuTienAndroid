package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ActivityDongTien extends AppCompatActivity {
    private EditText edtMaDN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dong_tien);

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

        try {
            String MaDN= getIntent().getStringExtra("MaDN");
            if (MaDN.equals("")==false) {
                fillDongNuoc(MaDN);
            }
        } catch (Exception ex) {
        }
    }

    private void fillDongNuoc(String MaDN) {
        try {
            for (int i = 0; i < CLocal.jsonDongNuoc.length(); i++) {
                JSONObject jsonObject = CLocal.jsonDongNuoc.getJSONObject(i);
                if (jsonObject.getString("ID").equals(MaDN) == true) {
                    edtMaDN.setText(MaDN);

                    break;
                }
            }
        }catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
