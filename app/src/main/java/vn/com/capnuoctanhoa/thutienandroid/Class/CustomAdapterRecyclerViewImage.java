package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.app.Activity;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.R;

public class CustomAdapterRecyclerViewImage extends RecyclerView.Adapter<CustomAdapterRecyclerViewImage.RecyclerViewHolder> {
    private Activity activity;
    private ArrayList<Bitmap> mDisplayedValues;

    public CustomAdapterRecyclerViewImage(Activity activity, ArrayList<Bitmap> mDisplayedValues) {
        super();
        this.activity = activity;
        this.mDisplayedValues = mDisplayedValues;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_listview_image, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (getItemCount() > 0) {
            final Bitmap bitmap = mDisplayedValues.get(position);
            final int ID = position;
            holder.ID.setText(String.valueOf(position+1));
            holder.imageView.setImageBitmap(bitmap);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CLocal.showImgThumb(activity, bitmap);
                }
            });
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDisplayedValues.remove(ID);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDisplayedValues == null ? 0 : mDisplayedValues.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView ID;
        ImageView imageView;
        ImageButton imageButton;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ID = (TextView) itemView.findViewById(R.id.ID);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageButton = (ImageButton) itemView.findViewById(R.id.imageButton);
        }
    }
}
