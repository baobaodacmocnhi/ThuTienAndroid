package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.app.Activity;
import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class CustomAdapterRecyclerViewParent_LoadMore extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements Filterable {

    private Activity activity;
    private ArrayList<CViewParent> mOriginalValues;
    private ArrayList<CViewParent> mDisplayedValues;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading=false,isMoreDataAvailable = true;

    public CustomAdapterRecyclerViewParent_LoadMore(Activity activity, ArrayList<CViewParent> mDisplayedValues) {
        super();
        this.activity=activity;
        this.mDisplayedValues = mDisplayedValues;
    }

    @Override
    public int getItemCount() {
        return mDisplayedValues == null ? 0 : mDisplayedValues.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDisplayedValues.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_listview_parent, parent, false);
            return new RecyclerViewHolder(view);
        }
        else if (viewType==VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_listview_loading, parent, false);
            return new RecyclerViewHolderLoading(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && onLoadMoreListener!=null){
            isLoading = true;
            onLoadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==VIEW_TYPE_ITEM){
            CViewParent entity = mDisplayedValues.get(position);
            final RecyclerViewHolder recyclerViewHolder=(RecyclerViewHolder)holder;

            recyclerViewHolder.STT.setText(entity.getSTT());
            recyclerViewHolder.ID.setText(entity.getID());
            recyclerViewHolder.Row1a.setText(entity.getRow1a());
            recyclerViewHolder.Row1b.setText(entity.getRow1b());
            if (entity.getRow2a().isEmpty() == true && entity.getRow2b().isEmpty() == true) {
                recyclerViewHolder.Row2a.setVisibility(View.GONE);
                recyclerViewHolder.Row2b.setVisibility(View.GONE);
            } else {
                recyclerViewHolder.Row2a.setText(entity.getRow2a());
                recyclerViewHolder.Row2b.setText(entity.getRow2b());
            }
            if (entity.getRow3a().isEmpty() == true && entity.getRow3b().isEmpty() == true) {
                recyclerViewHolder.Row3a.setVisibility(View.GONE);
                recyclerViewHolder.Row3b.setVisibility(View.GONE);
            } else {
                recyclerViewHolder.Row3a.setText(entity.getRow3a());
                recyclerViewHolder.Row3b.setText(entity.getRow3b());
            }
            if (entity.getRow4a().isEmpty() == true && entity.getRow4b().isEmpty() == true) {
                recyclerViewHolder.Row4a.setVisibility(View.GONE);
                recyclerViewHolder.Row4b.setVisibility(View.GONE);
            } else {
                recyclerViewHolder.Row4a.setText(entity.getRow4a());
                recyclerViewHolder.Row4b.setText(entity.getRow4b());
            }
            ///
            ///thêm ItemChild
            if(entity.getItemChildCount()>0) {
                CustomAdapterRecyclerViewChild customAdapterRecyclerViewChild = new CustomAdapterRecyclerViewChild(entity.getListChild());
                LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewHolder.recyclerView.setLayoutManager(layoutManager);
                recyclerViewHolder.recyclerView.setAdapter(customAdapterRecyclerViewChild);

                recyclerViewHolder.layoutChild.setVisibility(View.GONE);

                recyclerViewHolder.layoutParent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.layoutParent) {
                            if ( recyclerViewHolder.layoutChild.getVisibility() == View.VISIBLE) {
                                recyclerViewHolder.layoutChild.setVisibility(View.GONE);
                            } else {
                                recyclerViewHolder.layoutChild.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

                recyclerViewHolder.layoutParent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        PopupMenu popup = new PopupMenu(activity, v);
                        popup.getMenuInflater().inflate(R.menu.menu_dong_nuoc, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                int id = menuItem.getItemId();
                                TextView MaDN =  recyclerViewHolder.ID;
                                Intent intent;
                                switch (id) {
                                    case R.id.action_DongNuoc1:
                                        intent = new Intent(activity, ActivityDongNuoc.class);
                                        intent.putExtra("MaDN", MaDN.getText().toString());
                                        activity.startActivity(intent);
                                        break;
                                    case R.id.action_DongNuoc2:
                                        intent = new Intent(activity, ActivityDongNuoc2.class);
                                        intent.putExtra("MaDN", MaDN.getText().toString());
                                        activity.startActivity(intent);
                                        break;
                                    case R.id.action_MoNuoc:
                                        intent = new Intent(activity, ActivityMoNuoc.class);
                                        intent.putExtra("MaDN", MaDN.getText().toString());
                                        activity.startActivity(intent);
                                        break;
                                    case R.id.action_DongTien:
                                        intent = new Intent(activity, ActivityDongTien.class);
                                        intent.putExtra("MaDN", MaDN.getText().toString());
                                        activity.startActivity(intent);
                                        break;
                                }
                                return true;
                            }
                        });
                        popup.show();
                        return false;
                    }
                });
            }
        }
        //No else part needed as load holder doesn't bind any data

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

    private class RecyclerViewHolderLoading extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public RecyclerViewHolderLoading(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
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
