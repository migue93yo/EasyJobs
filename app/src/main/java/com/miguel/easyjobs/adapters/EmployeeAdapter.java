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
import com.miguel.easyjobs.domain.Employee;
import com.miguel.easyjobs.util.PictureSetting;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<Employee> employeesList;
    private Drawable drawable;
    private OnItemClickListener mItemClickListener;
    private Activity context;

    public EmployeeAdapter(Activity context, List<Employee> employeesList) {
        this.employeesList = employeesList;
        this.context = context;
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {

        protected ImageView image;
        protected TextView name;
        protected TextView province;
        protected TextView birthdate;
        protected View view;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.single_employee_image);
            name = (TextView) itemView.findViewById(R.id.single_employee_fullname);
            province = (TextView) itemView.findViewById(R.id.single_employee_province);
            birthdate = (TextView) itemView.findViewById(R.id.single_employee_birthdate);
            view = itemView.findViewById(R.id.single_employee_view);
        }

    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_employee_adapter, parent, false);
        drawable = v.getResources().getDrawable(R.drawable.buddyicon);
        return new EmployeeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, final int position) {
        final Employee employee = employeesList.get(position);

        ContentResolver cr = context.getContentResolver();
        Bitmap bitmap = null;

        File newFile = new File(PictureSetting.RUTE_IMAGE + File.separator + employee.getEmail() + ".png");
        Uri uri = Uri.fromFile(newFile);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
        } catch (IOException e) {
            Drawable drawable = context.getResources().getDrawable(R.drawable.buddyicon);
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }

        holder.image.setImageBitmap(bitmap);
        holder.name.setText(employee.getFullname());
        holder.province.setText(employee.getProvince());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        int myBirthday;
        int today;
        int year;

        myBirthday = Integer.valueOf(dateFormat.format((employee.getBirthdate())));
        today = Integer.parseInt(dateFormat.format(new Date()));
        year = (today - myBirthday) / 10000;
        holder.birthdate.setText(String.valueOf(year) + " " + context.getResources().getString(R.string.year));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, position);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, position);
                }
            }
        });

        if (position == employeesList.size() - 1)
            holder.view.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return employeesList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
