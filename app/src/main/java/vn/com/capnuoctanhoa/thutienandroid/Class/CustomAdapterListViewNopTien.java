package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;
import vn.com.capnuoctanhoa.thutienandroid.Doi.ActivityNopTien;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class CustomAdapterListViewNopTien extends BaseAdapter {
    private Activity activity;
    private ArrayList<CViewParent> mDisplayedValues;

    public CustomAdapterListViewNopTien(Activity activity, ArrayList<CViewParent> mDisplayedValues) {
        super();
        this.activity = activity;
        this.mDisplayedValues = mDisplayedValues;
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return mDisplayedValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView ID;
        CheckBox Chot;
        TextView NgayChot;
        Button NopTien;
        TextView Loai;
        TextView SoLuong;
        TextView TongCong;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_listview_noptien, null);
            holder = new ViewHolder();
            holder.ID = (TextView) convertView.findViewById(R.id.txtID);
            holder.Chot = (CheckBox) convertView.findViewById(R.id.chkChot);
            holder.NgayChot = (TextView) convertView.findViewById(R.id.txtNgayChot);
            holder.NopTien = (Button) convertView.findViewById(R.id.btnNopTien);
            holder.Loai = (TextView) convertView.findViewById(R.id.txtLoai);
            holder.SoLuong = (TextView) convertView.findViewById(R.id.txtSoLuong);
            holder.TongCong = (TextView) convertView.findViewById(R.id.txtTongCong);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CViewParent map = mDisplayedValues.get(position);
        holder.ID.setText(map.getSTT());
        holder.Chot.setChecked(map.isChot());
        holder.NgayChot.setText(map.getNgayChot());
        holder.Loai.setText(map.getLoai());
        holder.SoLuong.setText(map.getSoLuong());
        holder.TongCong.setText(map.getTongCong());

        holder.Chot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(new String[]{"chot", map.getID(), String.valueOf(isChecked), CLocal.MaNV});
            }
        });

        holder.NopTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Bạn có chắc chắn Nộp Tiền?")
                        .setCancelable(false)
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MyAsyncTask myAsyncTask = new MyAsyncTask();
                                myAsyncTask.execute(new String[]{"noptien", map.getNgayChot()});
                            }
                        })
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                Button btnPositive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                layoutParams.gravity = Gravity.CENTER;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);
            }
        });

        return convertView;
    }

    public class MyAsyncTask extends AsyncTask<String, String, String[]> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String[] doInBackground(String... strings) {
            try {
                String result = "";
                switch (strings[0]) {
                    case "chot":
                        result = ws.chotDangNgan(strings[1], strings[2], strings[3]);
                        break;
                    case "noptien":
                        result = ws.nopTien(strings[1]);
                        break;
                }
                return result.split(";");
            } catch (Exception ex) {
                return new String[]{"false", ex.getMessage()};
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                try {

                } catch (Exception ex) {
                    CLocal.showToastMessage(activity, ex.getMessage());
                }
            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (Boolean.parseBoolean(strings[0]) == true) {
                CLocal.showPopupMessage(activity, "THÀNH CÔNG", "center");
            } else {
                CLocal.showPopupMessage(activity, "THẤT BẠI\n" + strings[1], "center");
            }
        }
    }
}
