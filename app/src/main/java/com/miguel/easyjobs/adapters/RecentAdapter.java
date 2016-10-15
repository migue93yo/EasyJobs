package com.miguel.easyjobs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.domain.SearchCriteria;

import java.util.List;

public class RecentAdapter extends BaseAdapter {

    private Context context;
    private List<SearchCriteria> list;

    public RecentAdapter(List<SearchCriteria> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.single_recent_adapter, null);
        }

        SearchCriteria criteria = list.get(position);

        TextView keyword = (TextView) view.findViewById(R.id.recent_keyword);
        TextView province = (TextView) view.findViewById(R.id.recent_province);
        TextView job = (TextView) view.findViewById(R.id.recent_job);

        keyword.setText(criteria.getKeyword());

        String separador = "";
        if (!criteria.getJob().equals("")) {
            job.setText(criteria.getJob());
            separador = " " + context.getResources().getString(R.string.in) + " ";
        }

        if (!criteria.getProvince().equals("")) {
            province.setText(separador + criteria.getProvince());
        }

        return view;
    }
}
