package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityDongNuoc;
import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityDongNuoc2;
import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityDongTien;
import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityMoNuoc;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class CustomAdapterRecyclerViewParent extends RecyclerView.Adapter<CustomAdapterRecyclerViewParent.RecyclerViewHolder>  implements Filterable {
    private Activity activity;
    private ArrayList<CEntityParent> mOriginalValues;
    private ArrayList<CEntityParent> mDisplayedValues;

    public CustomAdapterRecyclerViewParent(Activity activity, ArrayList<CEntityParent> mDisplayedValues) {
        super();
        this.activity=activity;
        this.mDisplayedValues = mDisplayedValues;
    }

    @Override
    public int getItemCount() {
        return mDisplayedValues == null ? 0 : mDisplayedValues.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_parent, parent, false);
        return new RecyclerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        if(getItemCount()>0) {
            CEntityParent entity = mDisplayedValues.get(position);

            holder.STT.setText(entity.getSTT());
            holder.ID.setText(entity.getID());
            holder.Row1a.setText(entity.getRow1a());
            holder.Row1b.setText(entity.getRow1b());
            if (entity.getRow2a().isEmpty() == true && entity.getRow2b().isEmpty() == true) {
                holder.Row2a.setVisibility(View.GONE);
                holder.Row2b.setVisibility(View.GONE);
            } else {
                holder.Row2a.setText(entity.getRow2a());
                holder.Row2b.setText(entity.getRow2b());
            }
            if (entity.getRow3a().isEmpty() == true && entity.getRow3b().isEmpty() == true) {
                holder.Row3a.setVisibility(View.GONE);
                holder.Row3b.setVisibility(View.GONE);
            } else {
                holder.Row3a.setText(entity.getRow3a());
                holder.Row3b.setText(entity.getRow3b());
            }
            if (entity.getRow4a().isEmpty() == true && entity.getRow4b().isEmpty() == true) {
                holder.Row4a.setVisibility(View.GONE);
                holder.Row4b.setVisibility(View.GONE);
            } else {
                holder.Row4a.setText(entity.getRow4a());
                holder.Row4b.setText(entity.getRow4b());
            }
            ///
            ///thêm ItemChild
            if(entity.getItemChildCount()>0) {
                CustomAdapterRecyclerViewChild customAdapterRecyclerViewChild = new CustomAdapterRecyclerViewChild(entity.getListChild());
                LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                holder.recyclerView.setLayoutManager(layoutManager);
                holder.recyclerView.setAdapter(customAdapterRecyclerViewChild);

                holder.layoutChild.setVisibility(View.GONE);

                holder.layoutParent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.layoutParent) {
                            if ( holder.layoutChild.getVisibility() == View.VISIBLE) {
                                holder.layoutChild.setVisibility(View.GONE);
                            } else {
                                holder.layoutChild.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

//                holder.layoutParent.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        PopupMenu popup = new PopupMenu(activity, v);
//                        popup.getMenuInflater().inflate(R.menu.popup_dong_nuoc, popup.getMenu());
//                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem menuItem) {
//                                int id = menuItem.getItemId();
//                                TextView MaDN =  holder.ID;
//                                Intent intent;
//                                switch (id) {
//                                    case R.id.popup_action_DongNuoc:
//                                        intent = new Intent(activity, ActivityDongNuoc.class);
//                                        intent.putExtra("MaDN", MaDN.getText().toString());
//                                        activity.startActivity(intent);
//                                        break;
//                                    case R.id.popup_action_DongNuoc2:
//                                        intent = new Intent(activity, ActivityDongNuoc2.class);
//                                        intent.putExtra("MaDN", MaDN.getText().toString());
//                                        activity.startActivity(intent);
//                                        break;
//                                    case R.id.popup_action_MoNuoc:
//                                        intent = new Intent(activity, ActivityMoNuoc.class);
//                                        intent.putExtra("MaDN", MaDN.getText().toString());
//                                        activity.startActivity(intent);
//                                        break;
//                                    case R.id.popup_action_DongTien:
//                                        intent = new Intent(activity, ActivityDongTien.class);
//                                        intent.putExtra("MaDN", MaDN.getText().toString());
//                                        activity.startActivity(intent);
//                                        break;
//                                }
//                                return true;
//                            }
//                        });
//                        popup.show();
//                        return false;
//                    }
//                });
            }
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
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
        LinearLayout layoutChild;
        RecyclerView recyclerView;

        public RecyclerViewHolder(final View itemView) {
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
            layoutParent = (ConstraintLayout) itemView.findViewById(R.id.layoutParent);
            layoutChild = (LinearLayout) itemView.findViewById(R.id.layoutChild);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);

            }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDisplayedValues = (ArrayList<CEntityParent>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<CEntityParent> FilteredArrList = new ArrayList<CEntityParent>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<CEntityParent>(mDisplayedValues); // saves the original data in mOriginalValues
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
                            CEntityParent entity = new CEntityParent();
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
