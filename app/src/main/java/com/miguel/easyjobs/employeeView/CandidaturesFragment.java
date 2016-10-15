package com.miguel.easyjobs.employeeView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.adapters.OffersAdapter;
import com.miguel.easyjobs.domain.Inscription;
import com.miguel.easyjobs.domain.Offer;
import com.miguel.easyjobs.model.InscriptionsTable;
import com.miguel.easyjobs.model.OffersTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CandidaturesFragment extends Fragment {

    private RecyclerView recycler;
    private List<Offer> offersList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candidatures, container, false);

        InscriptionsTable inscriptionsTable = new InscriptionsTable(getActivity());
        SharedPreferences sp = getActivity().getSharedPreferences("logued", Context.MODE_PRIVATE);
        List<Inscription> inscriptionList = inscriptionsTable.findByEmail(sp.getString("email", ""));

        offersList = new ArrayList<>();
        OffersTable offersTable = new OffersTable(getActivity());
        Iterator i = inscriptionList.iterator();
        while (i.hasNext()) {
            Inscription inscription = (Inscription) i.next();
            offersList.add(offersTable.get(inscription.getId()));
        }

        recycler = (RecyclerView) view.findViewById(R.id.candidatures_list);
        recycler.setHasFixedSize(true);

        RecyclerView.LayoutManager lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);

        OffersAdapter adapter = new OffersAdapter(offersList, getActivity());
        recycler.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {
            ImageView imagen = (ImageView) view.findViewById(R.id.candidatures_briefcase);
            TextView text = (TextView) view.findViewById(R.id.candidatures_not_exist);
            imagen.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
        }

        adapter.setOnItemClickListener(new OffersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("offer", offersList.get(position));
                Intent i = new Intent(getActivity(), OfferActivity.class);
                i.putExtras(bundle);
                i.putExtra("type", OfferActivity.CANDIDATURE_MODE);
                startActivity(i);
                getActivity().finish();
            }
        });

        return view;
    }
}
