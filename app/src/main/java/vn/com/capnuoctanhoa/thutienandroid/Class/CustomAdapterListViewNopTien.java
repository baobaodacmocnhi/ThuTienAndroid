package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;
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
        TextView Loai;
        TextView SoLuong;
        TextView TongCong;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_listview_noptien, null);
            holder = new ViewHolder();
            holder.ID = (TextView) convertView.findViewById(R.id.txtID);
            holder.Chot = (CheckBox) convertView.findViewById(R.id.chkChot);
            holder.Loai = (TextView) convertView.findViewById(R.id.txtLoai);
            holder.SoLuong = (TextView) convertView.findViewById(R.id.txtSoLuong);
            holder.TongCong = (TextView) convertView.findViewById(R.id.txtTongCong);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}
