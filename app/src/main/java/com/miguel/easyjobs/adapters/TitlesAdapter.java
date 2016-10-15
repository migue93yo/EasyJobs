package com.miguel.easyjobs.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.domain.Title;

import java.text.SimpleDateFormat;
import java.util.List;

public class TitlesAdapter extends RecyclerView.Adapter<TitlesAdapter.TitlesViewHolder> {

    private List<Title> titlesList;
    private Drawable drawable;
    private OnItemClickListener mItemClickListener;
    private Activity context;

    public TitlesAdapter(Activity context, List<Title> titlesList) {
        this.titlesList = titlesList;
        this.context = context;
    }

    @Override
    public TitlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_profile_adapter, parent, false);
        drawable = v.getResources().getDrawable(R.drawable.education);
        return new TitlesViewHolder(v);
    }

    public static class TitlesViewHolder extends RecyclerView.ViewHolder {

        protected ImageView image;
        protected TextView title;
        protected TextView name;
        protected TextView date;
        protected TextView loc;

        public TitlesViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.four_fields_image);
            title = (TextView) itemView.findViewById(R.id.four_fields_one);
            name = (TextView) itemView.findViewById(R.id.four_fields_two);
            date = (TextView) itemView.findViewById(R.id.four_fields_three);
            loc = (TextView) itemView.findViewById(R.id.four_fields_four);
        }

    }

    @Override
    public void onBindViewHolder(TitlesViewHolder holder, final int position) {
        Title title = titlesList.get(position);

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        holder.image.setImageBitmap(bitmap);
        holder.title.setText(title.getTitle());
        holder.name.setText(title.getCenter());
        holder.loc.setText(title.getProvince());

        SimpleDateFormat dateDormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStart = dateDormat.format(titlesList.get(position).getDateStart());

        String dateFinish = dateDormat.format(titlesList.get(position).getDateEnd());
        if (dateFinish.equals("11/11/1111")) {
            dateFinish = context.getResources().getString(R.string.actually);
        }

        holder.date.setText(dateStart + " - " + dateFinish);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return titlesList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
