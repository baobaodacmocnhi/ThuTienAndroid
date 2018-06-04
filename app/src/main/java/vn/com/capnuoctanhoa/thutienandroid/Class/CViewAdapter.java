package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.R;

public class CViewAdapter extends BaseAdapter implements Filterable {
    private Activity activity;
    private ArrayList<CViewEntity> mOriginalValues;
    private ArrayList<CViewEntity> mDisplayedValues;

    public CViewAdapter(Activity activity, ArrayList<CViewEntity> mDisplayedValues) {
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
        TextView STT;
        TextView ID;
        TextView Row1a;
        TextView Row1b;
        TextView Row2a;
        TextView Row2b;
        TextView Row3a;
        TextView Row3b;
        ConstraintLayout layoutChild;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_row_listview, null);
            holder = new ViewHolder();
            holder.STT = (TextView) convertView.findViewById(R.id.lvSTT);
            holder.ID = (TextView) convertView.findViewById(R.id.lvID);
            holder.Row1a = (TextView) convertView.findViewById(R.id.lvRow1a);
            holder.Row1b = (TextView) convertView.findViewById(R.id.lvRow1b);
            holder.Row2a = (TextView) convertView.findViewById(R.id.lvRow2a);
            holder.Row2b = (TextView) convertView.findViewById(R.id.lvRow2b);
            holder.Row3a = (TextView) convertView.findViewById(R.id.lvRow3a);
            holder.Row3b = (TextView) convertView.findViewById(R.id.lvRow3b);
            holder.layoutChild = (ConstraintLayout) convertView.findViewById(R.id.layoutChild);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CViewEntity map = mDisplayedValues.get(position);
        holder.STT.setText(map.getSTT());
        holder.ID.setText(map.getID());
        holder.Row1a.setText(map.getRow1a());
        holder.Row1b.setText(map.getRow1b());
        holder.Row2a.setText(map.getRow2a());
        holder.Row2b.setText(map.getRow2b());
        holder.Row3a.setText(map.getRow3a());
        holder.Row3b.setText(map.getRow3b());

        if (map.getGiaiTrach() == true)
//            convertView.setBackgroundColor(CLocal.Color_GiaiTrach);
            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorGiaiTrach));
        else if (map.getTamThu() == true || map.getThuHo() == true)
//            convertView.setBackgroundColor(CLocal.Color_TamThu);
            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorTamThu));
        else
//            convertView.setBackgroundColor(CLocal.Color_ChuaThu);
            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorChuaThu));

//        if (map.getLenhHuy() == true)
//            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorLenhHuy));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<CViewEntity>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<CViewEntity> FilteredArrList = new ArrayList<CViewEntity>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<CViewEntity>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
//                        String data = mOriginalValues.get(i).getName1();
                        if (mOriginalValues.get(i).getRow1a().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow1b().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow2a().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow2b().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow3a().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow3b().toLowerCase().startsWith(constraint.toString())) {
                            CViewEntity entity = new CViewEntity();
                            entity.setSTT(mOriginalValues.get(i).getSTT());
                            entity.setID(mOriginalValues.get(i).getID());
                            entity.setRow1a(mOriginalValues.get(i).getRow1a());
                            entity.setRow1b(mOriginalValues.get(i).getRow1b());
                            entity.setRow2a(mOriginalValues.get(i).getRow2a());
                            entity.setRow2b(mOriginalValues.get(i).getRow2b());
                            entity.setRow3a(mOriginalValues.get(i).getRow3a());
                            entity.setRow3b(mOriginalValues.get(i).getRow3b());
                            entity.setGiaiTrach(mOriginalValues.get(i).getGiaiTrach());
                            entity.setTamThu(mOriginalValues.get(i).getTamThu());
                            entity.setThuHo(mOriginalValues.get(i).getThuHo());

                            FilteredArrList.add(entity);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
