package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.R;

public class CustomAdapterRecyclerViewChild extends RecyclerView.Adapter<CustomAdapterRecyclerViewChild.RecyclerViewHolder>  {
    private ArrayList<CEntityChild> mDisplayedValues;

    public CustomAdapterRecyclerViewChild(ArrayList<CEntityChild> mDisplayedValues) {
        super();
        this.mDisplayedValues = mDisplayedValues;
    }

    @Override
    public int getItemCount() {
        return mDisplayedValues == null ? 0 : mDisplayedValues.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_child, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if(getItemCount()>0) {
            holder.ID.setText(mDisplayedValues.get(position).getID());
            holder.Row1a.setText(mDisplayedValues.get(position).getRow1a());
            holder.Row1b.setText(mDisplayedValues.get(position).getRow1b());
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView ID;
        TextView Row1a;
        TextView Row1b;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ID = (TextView) itemView.findViewById(R.id.lvID);
            Row1a = (TextView) itemView.findViewById(R.id.lvRow1a);
            Row1b = (TextView) itemView.findViewById(R.id.lvRow1b);
        }
    }
}
