package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.R;

public class CustomAdapterExpandableListView extends BaseExpandableListAdapter implements Filterable {
    private Activity activity;
    private ArrayList<CViewParent> mOriginalValues;
    private ArrayList<CViewParent> mDisplayedValues;

    public CustomAdapterExpandableListView(Activity activity, ArrayList<CViewParent> mDisplayedValues) {
        this.activity = activity;
        this.mDisplayedValues = mDisplayedValues;
    }

    @Override
    public int getGroupCount() {
        return this.mDisplayedValues.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<CViewChild> lstChild = this.mDisplayedValues.get(groupPosition).getListChild();
        return lstChild.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mDisplayedValues.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<CViewChild> lstChild = this.mDisplayedValues.get(groupPosition).getListChild();
        return lstChild.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
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
        TextView Row4a;
        TextView Row4b;
        ConstraintLayout layoutParent;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.recyclerview_parent, null);
            holder = new ViewHolder();
            holder.STT = (TextView) convertView.findViewById(R.id.lvSTT);
            holder.ID = (TextView) convertView.findViewById(R.id.lvID);
            holder.Row1a = (TextView) convertView.findViewById(R.id.lvRow1a);
            holder.Row1b = (TextView) convertView.findViewById(R.id.lvRow1b);
            holder.Row2a = (TextView) convertView.findViewById(R.id.lvRow2a);
            holder.Row2b = (TextView) convertView.findViewById(R.id.lvRow2b);
            holder.Row3a = (TextView) convertView.findViewById(R.id.lvRow3a);
            holder.Row3b = (TextView) convertView.findViewById(R.id.lvRow3b);
            holder.Row4a = (TextView) convertView.findViewById(R.id.lvRow4a);
            holder.Row4b = (TextView) convertView.findViewById(R.id.lvRow4b);
            holder.layoutParent = (ConstraintLayout) convertView.findViewById(R.id.layoutParent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CViewParent map = (CViewParent) getGroup(groupPosition);
        holder.STT.setText(map.getSTT());
        holder.ID.setText(map.getID());
        if (map.getRow1a().isEmpty() == true && map.getRow1b().isEmpty() == true) {
            holder.Row1a.setVisibility(View.GONE);
            holder.Row1b.setVisibility(View.GONE);
        } else {
            holder.Row1a.setText(map.getRow1a());
            holder.Row1b.setText(map.getRow1b());
        }
        if (map.getRow2a().isEmpty() == true && map.getRow2b().isEmpty() == true) {
            holder.Row2a.setVisibility(View.GONE);
            holder.Row2b.setVisibility(View.GONE);
        } else {
            holder.Row2a.setText(map.getRow2a());
            holder.Row2b.setText(map.getRow2b());
        }
        if (map.getRow3a().isEmpty() == true && map.getRow3b().isEmpty() == true) {
            holder.Row3a.setVisibility(View.GONE);
            holder.Row3b.setVisibility(View.GONE);
        } else {
            holder.Row3a.setText(map.getRow3a());
            holder.Row3b.setText(map.getRow3b());
        }
        if (map.getRow4a().isEmpty() == true && map.getRow4b().isEmpty() == true) {
            holder.Row4a.setVisibility(View.GONE);
            holder.Row4b.setVisibility(View.GONE);
        } else {
            holder.Row4a.setText(map.getRow4a());
            holder.Row4b.setText(map.getRow4b());
        }

//        if (map.getGiaiTrach() == true)
//            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorGiaiTrach));
//        else if (map.getTamThu() == true || map.getThuHo() == true)
//            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorTamThu));
//        else
//            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorChuaThu));
        if (map.getTBDongNuoc() == true)
            holder.layoutParent.setBackgroundColor(activity.getResources().getColor(R.color.colorDongNuoc));
        else
            holder.layoutParent.setBackgroundColor(activity.getResources().getColor(R.color.colorChuaThu));
        if (map.getLenhHuy() == true)
            holder.layoutParent.setBackgroundColor(activity.getResources().getColor(R.color.colorLenhHuy));
        else
            holder.layoutParent.setBackgroundColor(activity.getResources().getColor(R.color.colorChuaThu));

        return convertView;
    }

    private class ViewHolderChild {
        TextView ID;
        TextView Row1a;
        TextView Row1b;
        TextView Row2a;
        TextView Row2b;
        TextView Row3a;
        TextView Row3b;
        TextView Row4a;
        TextView Row4b;
        ConstraintLayout layoutChild;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.recyclerview_child, null);
            holder = new ViewHolderChild();
            holder.ID = (TextView) convertView.findViewById(R.id.lvID);
            holder.Row1a = (TextView) convertView.findViewById(R.id.lvRow1a);
            holder.Row1b = (TextView) convertView.findViewById(R.id.lvRow1b);
            holder.Row2a = (TextView) convertView.findViewById(R.id.lvRow2a);
            holder.Row2b = (TextView) convertView.findViewById(R.id.lvRow2b);
            holder.Row3a = (TextView) convertView.findViewById(R.id.lvRow3a);
            holder.Row3b = (TextView) convertView.findViewById(R.id.lvRow3b);
            holder.Row4a = (TextView) convertView.findViewById(R.id.lvRow4a);
            holder.Row4b = (TextView) convertView.findViewById(R.id.lvRow4b);
            holder.layoutChild = (ConstraintLayout) convertView.findViewById(R.id.layoutChild);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderChild) convertView.getTag();
        }

        CViewChild map = (CViewChild) getChild(groupPosition, childPosition);
        holder.ID.setText(map.getID());
        holder.Row1a.setText(map.getRow1a());
        holder.Row1b.setText(map.getRow1b());
        if (map.getRow1a().isEmpty() == true && map.getRow1b().isEmpty() == true) {
            holder.Row1a.setVisibility(View.GONE);
            holder.Row1b.setVisibility(View.GONE);
        } else {
            holder.Row1a.setText(map.getRow1a());
            holder.Row1b.setText(map.getRow1b());
        }
        if (map.getRow2a().isEmpty() == true && map.getRow2b().isEmpty() == true) {
            holder.Row2a.setVisibility(View.GONE);
            holder.Row2b.setVisibility(View.GONE);
        } else {
            holder.Row2a.setText(map.getRow2a());
            holder.Row2b.setText(map.getRow2b());
        }
        if (map.getRow3a().isEmpty() == true && map.getRow3b().isEmpty() == true) {
            holder.Row3a.setVisibility(View.GONE);
            holder.Row3b.setVisibility(View.GONE);
        } else {
            holder.Row3a.setText(map.getRow3a());
            holder.Row3b.setText(map.getRow3b());
        }
        if (map.getRow4a().isEmpty() == true && map.getRow4b().isEmpty() == true) {
            holder.Row4a.setVisibility(View.GONE);
            holder.Row4b.setVisibility(View.GONE);
        } else {
            holder.Row4a.setText(map.getRow4a());
            holder.Row4b.setText(map.getRow4b());
        }
        if (map.getGiaiTrach() == true)
            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorGiaiTrach));
        else if (map.getTamThu() == true || map.getThuHo() == true)
            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorTamThu));
        else
            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorChuaThu));

        if (map.getTBDongNuoc() == true)
            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorDongNuoc));
        if (map.getLenhHuy() == true)
            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorLenhHuy));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDisplayedValues = (ArrayList<CViewParent>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<CViewParent> FilteredArrList = new ArrayList<CViewParent>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<CViewParent>(mDisplayedValues); // saves the original data in mOriginalValues
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
                                || mOriginalValues.get(i).getRow3b().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow4a().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow4b().toLowerCase().startsWith(constraint.toString())) {
                            CViewParent entity = new CViewParent();
                            entity.setSTT(mOriginalValues.get(i).getSTT());
                            entity.setID(mOriginalValues.get(i).getID());
                            entity.setRow1a(mOriginalValues.get(i).getRow1a());
                            entity.setRow1b(mOriginalValues.get(i).getRow1b());
                            entity.setRow2a(mOriginalValues.get(i).getRow2a());
                            entity.setRow2b(mOriginalValues.get(i).getRow2b());
                            entity.setRow3a(mOriginalValues.get(i).getRow3a());
                            entity.setRow3b(mOriginalValues.get(i).getRow3b());
                            entity.setRow4a(mOriginalValues.get(i).getRow4a());
                            entity.setRow4b(mOriginalValues.get(i).getRow4b());
                            entity.setGiaiTrach(mOriginalValues.get(i).getGiaiTrach());
                            entity.setTamThu(mOriginalValues.get(i).getTamThu());
                            entity.setThuHo(mOriginalValues.get(i).getThuHo());
                            entity.setLenhHuy(mOriginalValues.get(i).getLenhHuy());
                            entity.setListChild(mOriginalValues.get(i).getListChild());

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
