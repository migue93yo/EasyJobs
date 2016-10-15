package com.miguel.easyjobs.employeeView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.adapters.OffersAdapter;
import com.miguel.easyjobs.domain.Offer;
import com.miguel.easyjobs.domain.SearchCriteria;
import com.miguel.easyjobs.model.OffersTable;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    private SearchCriteria criteria;
    private OffersAdapter adapter;
    private RecyclerView recycler;
    private ArrayList<Offer> offersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search_result);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        offersList = (ArrayList<Offer>) bundle.getSerializable("offersList");
        criteria = (SearchCriteria) bundle.getSerializable("criteria");

        recycler = (RecyclerView) findViewById(R.id.search_result);
        recycler.setHasFixedSize(true);
        loadAdapter(true);

        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        final SwipeRefreshLayout refresh = (SwipeRefreshLayout) findViewById(R.id.search_result_refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAdapter(false);
                refresh.setRefreshing(false);
            }
        });

    }

    private void loadAdapter(boolean existList) {
        if (!existList) {
            OffersTable table = new OffersTable(SearchResultActivity.this);
            offersList = table.findByCriteria(criteria);
        }
        adapter = new OffersAdapter(offersList, this);
        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new OffersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("offer", offersList.get(position));
                Intent i = new Intent(SearchResultActivity.this, OfferActivity.class);
                i.putExtra("type", OfferActivity.SEARCH_MODE);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

}
