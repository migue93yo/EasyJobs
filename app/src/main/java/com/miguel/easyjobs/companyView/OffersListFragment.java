package com.miguel.easyjobs.companyView;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.adapters.OfferPageViewAdapter;

public class OffersListFragment extends Fragment {

    private ViewPager pager;
    private TabLayout tab;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offerslist, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.float_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewOfferActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        pager = (ViewPager) view.findViewById(R.id.offerslist_pager);
        tab = (TabLayout) view.findViewById(R.id.offerslist_tab_layout);

        OfferPageViewAdapter adapter = new OfferPageViewAdapter(getActivity(), getActivity().getSupportFragmentManager());
        pager.setAdapter(adapter);
        tab.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));

        return view;

    }

}
