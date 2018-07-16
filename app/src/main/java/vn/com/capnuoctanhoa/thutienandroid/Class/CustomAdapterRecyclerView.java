package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.R;

public class CustomAdapterRecyclerView extends RecyclerView.Adapter<CustomAdapterRecyclerView.RecyclerViewHolder>  implements Filterable {
    private ArrayList<CViewEntity> mOriginalValues;
    private ArrayList<CViewEntity> mDisplayedValues;

    public CustomAdapterRecyclerView(ArrayList<CViewEntity> mDisplayedValues) {
        super();
        this.mDisplayedValues = mDisplayedValues;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_row_listview, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.STT.setText(mDisplayedValues.get(position).getSTT());
        holder.ID.setText(mDisplayedValues.get(position).getID());
        holder.Row1a.setText(mDisplayedValues.get(position).getRow1a());
        holder.Row1b.setText(mDisplayedValues.get(position).getRow1b());
        holder.Row2a.setText(mDisplayedValues.get(position).getRow2a());
        holder.Row2b.setText(mDisplayedValues.get(position).getRow2b());
        holder.Row3a.setText(mDisplayedValues.get(position).getRow3a());
        holder.Row3b.setText(mDisplayedValues.get(position).getRow3b());
        holder.Row4a.setText(mDisplayedValues.get(position).getRow4a());
        holder.Row4b.setText(mDisplayedValues.get(position).getRow4b());
    }

    @Override
    public int getItemCount() {
        return mDisplayedValues.size();
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
                                || mOriginalValues.get(i).getRow3b().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow4a().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow4b().toLowerCase().startsWith(constraint.toString())) {
                            CViewEntity entity = new CViewEntity();
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

    public class RecyclerViewHolder  extends RecyclerView.ViewHolder{
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
        ConstraintLayout layoutChild;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            STT = (TextView) itemView.findViewById(R.id.lvSTT);
            ID = (TextView) itemView.findViewById(R.id.lvID);
            Row1a = (TextView) itemView.findViewById(R.id.lvRow1a);
            Row1b = (TextView) itemView.findViewById(R.id.lvRow1b);
            Row2a = (TextView) itemView.findViewById(R.id.lvRow2a);
            Row2b = (TextView) itemView.findViewById(R.id.lvRow2b);
            Row3a = (TextView) itemView.findViewById(R.id.lvRow3a);
            Row3b = (TextView) itemView.findViewById(R.id.lvRow3b);
            Row4a = (TextView) itemView.findViewById(R.id.lvRow4a);
            Row4b = (TextView) itemView.findViewById(R.id.lvRow4b);
            layoutChild = (ConstraintLayout) itemView.findViewById(R.id.layoutChild);
        }
    }
}
