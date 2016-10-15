package com.miguel.easyjobs.companyView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.adapters.OffersAdapter;
import com.miguel.easyjobs.domain.Offer;
import com.miguel.easyjobs.model.OffersTable;

import java.util.ArrayList;

public class HiddenOffersFragment extends Fragment {

    private ArrayList<Offer> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hidden_offers, container, false);

        OffersTable table = new OffersTable(getActivity());
        list = table.findByState(OffersTable.INACTIVE);
        OffersAdapter adapter = new OffersAdapter(list, getActivity());

        RecyclerView offersList = (RecyclerView) view.findViewById(R.id.inactived_offers);
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(getActivity());
        offersList.setLayoutManager(lManager);
        offersList.setAdapter(adapter);

        adapter.setOnItemClickListener(new OffersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getActivity(), MyOfferActivity.class);
                i.putExtra("offer", list.get(position));
                i.putExtra("visibility", false);
                startActivity(i);
                getActivity().finish();
            }
        });

        return view;
    }

}
