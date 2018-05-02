package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class FragmentDongNuoc extends Fragment {
    private View rootView;
    private ImageButton ibtnChupHinh;
    private ImageView imgThumb;
    private EditText edtMaDN, edtDanhBo, edtMLT,edtHoTen, edtDiaChi, edtNgayDN, edtChiSoDN, edtHieu, edtCo, edtSoThan, edtLyDo;
    private Spinner spnChiMatSo, spnChiKhoaGoc;
    private Button btnKiemTra, btnDongNuoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dong_nuoc, container, false);

        edtMaDN = (EditText) rootView.findViewById(R.id.edtMaDN);
        edtDanhBo = (EditText) rootView.findViewById(R.id.edtDanhBo);
        edtMLT = (EditText) rootView.findViewById(R.id.edtMLT);
        edtHoTen = (EditText) rootView.findViewById(R.id.edtHoTen);
        edtDiaChi = (EditText) rootView.findViewById(R.id.edtDiaChi);
        edtHieu = (EditText) rootView.findViewById(R.id.edtHieu);
        edtCo = (EditText) rootView.findViewById(R.id.edtCo);
        edtSoThan = (EditText) rootView.findViewById(R.id.edtSoThan);
        edtNgayDN = (EditText) rootView.findViewById(R.id.edtNgayDN);
        edtChiSoDN = (EditText) rootView.findViewById(R.id.edtChiSoDN);
        edtLyDo = (EditText) rootView.findViewById(R.id.edtLyDo);
        spnChiMatSo = (Spinner) rootView.findViewById(R.id.spnChiMatSo);
        spnChiKhoaGoc = (Spinner) rootView.findViewById(R.id.spnChiKhoaGoc);

        imgThumb = (ImageView) rootView.findViewById(R.id.imgThumb);
        ibtnChupHinh = (ImageButton) rootView.findViewById(R.id.ibtnChupHinh);

        btnKiemTra = (Button) rootView.findViewById(R.id.btnKiemTra);
        btnDongNuoc = (Button) rootView.findViewById(R.id.btnDongNuoc);

        ibtnChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        imgThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowImgThumb();
            }
        });

        btnKiemTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CLocal.CheckNetworkAvailable(getContext()) == false) {
                    Toast.makeText(getActivity(), "Không có Internet", Toast.LENGTH_LONG).show();
                    return;
                }
                if(edtMaDN.getText().toString().equals("")==false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("Kiểm Tra");
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
                if(edtMaDN.getText().toString().equals("")==false&&edtChiSoDN.getText().toString().equals("")==false) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute("Đóng Nước");
                }
            }
        });

        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                String MaDN = bundle.getString("MaDN");
                FillDongNuoc(MaDN);
            }
        } catch (Exception ex) {
        }

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
                    edtMLT.setText(jsonObject.getString("MLT"));
                    edtHoTen.setText(jsonObject.getString("HoTen"));
                    edtDiaChi.setText(jsonObject.getString("DiaChi"));
                    edtHieu.setText(jsonObject.getString("Hieu"));
                    edtCo.setText(jsonObject.getString("Co"));
                    edtSoThan.setText(jsonObject.getString("SoThan"));
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                    edtNgayDN.setText(currentDate.format(new Date()));
                    edtChiSoDN.setText(jsonObject.getString("ChiSoDN").replace("null",""));
                    SetSpinnerSelection(spnChiMatSo,jsonObject.getString("ChiMatSo"));
                    SetSpinnerSelection(spnChiKhoaGoc,jsonObject.getString("ChiKhoaGoc"));
                    edtLyDo.setText(jsonObject.getString("LyDo").replace("null",""));
                    break;
                }
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void SetSpinnerSelection(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public void ShowImgThumb() {
        Dialog builder = new Dialog(getContext());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(((BitmapDrawable)imgThumb.getDrawable()).getBitmap());
        builder.addContentView(imageView, new RelativeLayout.LayoutParams( 600,600));
        builder.show();
    }

    public byte[] ConvertBitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public class MyAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "Kiểm Tra":
                    return ws.KiemTraHoaDon_DongNuoc(edtMaDN.getText().toString());
                case "Đóng Nước":
                    if(Boolean.parseBoolean(ws.CheckExist_KQDongNuoc(edtMaDN.getText().toString()))==false) {
                        Bitmap reizeImage = Bitmap.createScaledBitmap(((BitmapDrawable) imgThumb.getDrawable()).getBitmap(), 1024, 1024, false);
                        String imgString = Base64.encodeToString(ConvertBitmapToBytes(reizeImage), Base64.NO_WRAP);
//                        imgString = "NULL";
                        return ws.ThemDongNuoc(edtMaDN.getText().toString(), edtDanhBo.getText().toString(), edtMLT.getText().toString(), edtHoTen.getText().toString(), edtDiaChi.getText().toString(),
                                imgString, edtNgayDN.getText().toString(), edtChiSoDN.getText().toString(), edtHieu.getText().toString(),edtCo.getText().toString(), edtSoThan.getText().toString(),
                                spnChiMatSo.getSelectedItem().toString(), spnChiKhoaGoc.getSelectedItem().toString(), edtLyDo.getText().toString(),CLocal.sharedPreferencesre.getString("MaNV",""));
                    }
                    else
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
            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        }

    }
}
