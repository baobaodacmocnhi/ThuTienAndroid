package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class FragmentDongNuoc extends Fragment {
    private View rootView;
    private String MaDN;
    private BottomNavigationView bottomNavigationView;
    private ImageButton ibtnChupHinh;
    private ImageView imgThumb;
    private EditText edtMaDN, edtDanhBo, edtHoTen, edtDiaChi, edtNgayDN, edtChiSoDN, edtHieu, edtCo, edtSoThan,edtGhiChu;
    private Spinner spnChiMatSo, spnChiKhoaGoc;
    private Button btnKiemTra, btnDongNuoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dong_nuoc, container, false);

//        bottomNavigationView = (BottomNavigationView) rootView.findViewById(R.id.bottom_navigation);

        edtMaDN = (EditText) rootView.findViewById(R.id.edtMaDN);
        edtDanhBo = (EditText) rootView.findViewById(R.id.edtDanhBo);
        edtHoTen = (EditText) rootView.findViewById(R.id.edtHoTen);
        edtDiaChi = (EditText) rootView.findViewById(R.id.edtDiaChi);
        edtNgayDN = (EditText) rootView.findViewById(R.id.edtNgayDN);
        edtChiSoDN = (EditText) rootView.findViewById(R.id.edtChiSoDN);
        edtHieu = (EditText) rootView.findViewById(R.id.edtHieu);
        edtCo = (EditText) rootView.findViewById(R.id.edtCo);
        edtSoThan = (EditText) rootView.findViewById(R.id.edtSoThan);
        edtGhiChu = (EditText) rootView.findViewById(R.id.edtGhiChu);

        spnChiMatSo = (Spinner) rootView.findViewById(R.id.spnChiMatSo);
        spnChiKhoaGoc = (Spinner) rootView.findViewById(R.id.spnChiKhoaGoc);

        imgThumb = (ImageView) rootView.findViewById(R.id.imgThumb);
        ibtnChupHinh = (ImageButton) rootView.findViewById(R.id.ibtnChupHinh);

        btnKiemTra = (Button) rootView.findViewById(R.id.btnKiemTra);
        btnDongNuoc = (Button) rootView.findViewById(R.id.btnDongNuoc);

        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                MaDN = bundle.getString("MaDN");
                FillDongNuoc(MaDN);
            }
        } catch (Exception ex) {
        }

//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.bottom_btnKiemTra:
//                        Toast.makeText(getActivity(), "kiểm tra", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.bottom_btnThem:
//                        Toast.makeText(getActivity(), "đóng nước", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//                ;
//                return true;
//            }
//        });

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        edtNgayDN.setText(currentDate.format(new Date()));

        ibtnChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        btnKiemTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.CheckNetworkAvailable(getContext()) == false) {
                    Toast.makeText(getActivity(), "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });

        btnDongNuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.CheckNetworkAvailable(getContext()) == false) {
                    Toast.makeText(getActivity(), "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });

        return rootView;
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
            for (int i = 0; i < CLocal.jsonArray_DongNuoc.length(); i++) {
                JSONObject jsonObject = CLocal.jsonArray_DongNuoc.getJSONObject(i);
                if (jsonObject.getString("MaDN").equals(MaDN) == true) {
                    edtMaDN.setText(MaDN);
                    edtDanhBo.setText(jsonObject.getString("DanhBo"));
                    edtHoTen.setText(jsonObject.getString("HoTen"));
                    edtDiaChi.setText(jsonObject.getString("DiaChi"));
                    edtHieu.setText(jsonObject.getString("Hieu"));
                    edtCo.setText(jsonObject.getString("Co"));
                    edtSoThan.setText(jsonObject.getString("SoThan"));
                    break;
                }
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
