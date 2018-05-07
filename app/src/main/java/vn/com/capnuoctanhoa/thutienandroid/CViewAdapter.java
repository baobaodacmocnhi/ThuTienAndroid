package vn.com.capnuoctanhoa.thutienandroid;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class CViewAdapter extends BaseAdapter implements Filterable {
    private ArrayList<CViewEntity> mOriginalValues;
    private ArrayList<CViewEntity> mDisplayedValues;
   private Activity activity;

    public CViewAdapter(Activity activity, ArrayList<CViewEntity> list) {
        super();
        this.activity = activity;
        this.mDisplayedValues = list;
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
        TextView txtSTT;
        TextView txtID;
        TextView txtName1;
        TextView txtName2;
        TextView txtContent1;
        TextView txtContent2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_row_listview, null);
            holder = new ViewHolder();
            holder.txtSTT = (TextView) convertView.findViewById(R.id.lvSTT);
            holder.txtID = (TextView) convertView.findViewById(R.id.lvID);
            holder.txtName1 = (TextView) convertView.findViewById(R.id.lvName1);
            holder.txtName2 = (TextView) convertView.findViewById(R.id.lvName2);
            holder.txtContent1 = (TextView) convertView.findViewById(R.id.lvContent1);
            holder.txtContent2 = (TextView) convertView.findViewById(R.id.lvContent2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CViewEntity map = mDisplayedValues.get(position);
        holder.txtSTT.setText(map.getSTT());
        holder.txtID.setText(map.getID());
        holder.txtName1.setText(map.getName1());
        holder.txtName2.setText(map.getName2());
        holder.txtContent1.setText(map.getContent1());
        holder.txtContent2.setText(map.getContent2());

        convertView.setBackgroundColor(map.getBackgroundColor());

        return convertView;
    }

    @Override
    public Filter getFilter()
    {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

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
                        if ( mOriginalValues.get(i).getName1().toLowerCase().startsWith(constraint.toString())
                                ||mOriginalValues.get(i).getName2().toLowerCase().startsWith(constraint.toString())
                                ||mOriginalValues.get(i).getContent1().toLowerCase().startsWith(constraint.toString())
                                ||mOriginalValues.get(i).getContent2().toLowerCase().startsWith(constraint.toString())) {
                            CViewEntity entity=new CViewEntity();
                            entity.setSTT(mOriginalValues.get(i).getSTT());
                            entity.setID(mOriginalValues.get(i).getID());
                            entity.setName1(mOriginalValues.get(i).getName1());
                            entity.setName2(mOriginalValues.get(i).getName2());
                            entity.setContent1(mOriginalValues.get(i).getContent1());
                            entity.setContent2(mOriginalValues.get(i).getContent2());
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
