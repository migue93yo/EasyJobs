package com.miguel.easyjobs.adapters;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.domain.Offer;
import com.miguel.easyjobs.model.CompanyTable;
import com.miguel.easyjobs.util.PictureSetting;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OffersViewHolder> {

    private List<Offer> OfferList;
    private OnItemClickListener mItemClickListener;
    private Activity context;

    public OffersAdapter(List<Offer> OfferList, Activity context) {
        this.OfferList = OfferList;
        this.context = context;
    }

    @Override
    public OffersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_offer_adapter, parent, false);
        return new OffersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OffersViewHolder holder, final int position) {
        Offer offer = OfferList.get(position);

        CompanyTable ct = new CompanyTable(context);

        ContentResolver cr = context.getContentResolver();
        Bitmap bitmap = null;

        File newFile = new File(PictureSetting.RUTE_IMAGE + offer.getEmail() + ".png");
        Uri uri = Uri.fromFile(newFile);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
        } catch (IOException e) {
            Drawable drawable = context.getResources().getDrawable(R.drawable.buddyicon);
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }

        holder.image.setImageBitmap(bitmap);

        holder.title.setText(offer.getTitle());
        holder.name.setText(ct.getName(offer.getEmail()));
        holder.loc.setText(offer.getProvince());

        SimpleDateFormat dateDormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStart = dateDormat.format(offer.getDuration());

        holder.date.setText(context.getResources().getString(R.string.date_for) + ": " + dateStart);

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
        return OfferList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public static class OffersViewHolder extends RecyclerView.ViewHolder {

        protected ImageView image;
        protected TextView title;
        protected TextView name;
        protected TextView date;
        protected TextView loc;

        public OffersViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.single_search_image);
            title = (TextView) itemView.findViewById(R.id.single_search_title);
            name = (TextView) itemView.findViewById(R.id.single_search_name);
            date = (TextView) itemView.findViewById(R.id.single_search_date);
            loc = (TextView) itemView.findViewById(R.id.single_search_loc);
        }

    }
}
