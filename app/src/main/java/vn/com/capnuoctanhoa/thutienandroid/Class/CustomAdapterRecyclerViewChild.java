package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.R;

public class CustomAdapterRecyclerViewChild extends RecyclerView.Adapter<CustomAdapterRecyclerViewChild.RecyclerViewHolder>  {
    private ArrayList<CViewChild> mDisplayedValues;

    public CustomAdapterRecyclerViewChild(ArrayList<CViewChild> mDisplayedValues) {
        super();
        this.mDisplayedValues = mDisplayedValues;
    }

    @Override
    public int getItemCount() {
        return mDisplayedValues == null ? 0 : mDisplayedValues.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_listview_child, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if(getItemCount()>0) {
            CViewChild entity = mDisplayedValues.get(position);
            holder.ID.setText(entity.getID());
            holder.Row1a.setText(entity.getRow1a());
            holder.Row1b.setText(entity.getRow1b());
            if(entity.getRow2a().isEmpty())
                holder.Row2a.setVisibility(View.GONE);
            else
                holder.Row2a.setText(entity.getRow2a());
            if(entity.getRow2b().isEmpty())
                holder.Row2b.setVisibility(View.GONE);
            else
                holder.Row2b.setText(entity.getRow2b());
            if(entity.getRow3a().isEmpty())
                holder.Row3a.setVisibility(View.GONE);
            else
                holder.Row3a.setText(entity.getRow3a());
            if(entity.getRow3b().isEmpty())
                holder.Row3b.setVisibility(View.GONE);
            else
                holder.Row3b.setText(entity.getRow3b());
            if(entity.getRow4a().isEmpty())
                holder.Row4a.setVisibility(View.GONE);
            else
                holder.Row4a.setText(entity.getRow4a());
            if(entity.getRow4b().isEmpty())
                holder.Row4b.setVisibility(View.GONE);
            else
                holder.Row4b.setText(entity.getRow4b());
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView ID;
        TextView Row1a;
        TextView Row1b;
        TextView Row2a;
        TextView Row2b;
        TextView Row3a;
        TextView Row3b;
        TextView Row4a;
        TextView Row4b;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ID = (TextView) itemView.findViewById(R.id.lvID);
            Row1a = (TextView) itemView.findViewById(R.id.lvRow1a);
            Row1b = (TextView) itemView.findViewById(R.id.lvRow1b);
            Row2a = (TextView) itemView.findViewById(R.id.lvRow2a);
            Row2b = (TextView) itemView.findViewById(R.id.lvRow2b);
            Row3a = (TextView) itemView.findViewById(R.id.lvRow3a);
            Row3b = (TextView) itemView.findViewById(R.id.lvRow3b);
            Row4a = (TextView) itemView.findViewById(R.id.lvRow4a);
            Row4b = (TextView) itemView.findViewById(R.id.lvRow4b);
        }
    }
}
