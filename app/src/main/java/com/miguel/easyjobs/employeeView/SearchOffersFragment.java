package com.miguel.easyjobs.employeeView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.adapters.RecentAdapter;
import com.miguel.easyjobs.domain.Offer;
import com.miguel.easyjobs.domain.SearchCriteria;
import com.miguel.easyjobs.model.OffersTable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class SearchOffersFragment extends Fragment {

    private Intent searchResult;

    private EditText keyword;
    private EditText province;
    private EditText job;
    private Button search;

    private List<SearchCriteria> recent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_offers, container, false);

        searchResult = new Intent(getActivity(), SearchResultActivity.class);

        keyword = (EditText) view.findViewById(R.id.search_keyword);
        province = (EditText) view.findViewById(R.id.search_province);
        job = (EditText) view.findViewById(R.id.search_job);
        search = (Button) view.findViewById(R.id.search_searchbutton);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);

                SearchCriteria criteria = new SearchCriteria(
                        keyword.getText().toString(),
                        province.getText().toString(),
                        job.getText().toString());

                SearchOffers searchOffers = new SearchOffers(v);
                searchOffers.execute(criteria);
            }
        });

        recent = new ArrayList<>();
        try {
            InputStreamReader isr = new InputStreamReader(getActivity().openFileInput("SearchRecent.txt"));
            BufferedReader br = new BufferedReader(isr);

            String keywordCriteria = br.readLine();
            while (keywordCriteria != null) {
                String provinceCriteria = br.readLine();
                String jobCriteria = br.readLine();

                SearchCriteria criteria = new SearchCriteria();
                criteria.setKeyword(keywordCriteria);
                criteria.setProvince(provinceCriteria);
                criteria.setJob(jobCriteria);
                recent.add(criteria);

                keywordCriteria = br.readLine();
            }
            isr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ListView listView = (ListView) view.findViewById(R.id.search_list);
        RecentAdapter adapter = new RecentAdapter(recent, getActivity());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                search.setEnabled(false);
                SearchCriteria criteria = recent.get(position);
                SearchOffers searchOffers = new SearchOffers(search);
                searchOffers.execute(criteria);
            }
        });

        return view;
    }

    private class SearchOffers extends AsyncTask<SearchCriteria, String, String> {

        private View view;

        public SearchOffers(View view) {
            this.view = view;
        }

        @Override
        protected String doInBackground(final SearchCriteria... params) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SearchCriteria criteria = params[0];

                    OffersTable table = new OffersTable(getActivity());
                    ArrayList<Offer> offersList = table.findByCriteria(criteria);
                    if (offersList.size() > 0) {

                        if (!criteria.getKeyword().equals("")
                                || !criteria.getProvince().equals("")
                                || !criteria.getJob().equals("")) {
                            if (recent.size() == 3) {
                                recent.remove(recent.size() - 1);
                            }

                            try {
                                OutputStreamWriter osw = new OutputStreamWriter(getActivity().openFileOutput("SearchRecent.txt", Context.MODE_PRIVATE));
                                osw.write(criteria.getKeyword() + "\n");
                                osw.write(criteria.getProvince() + "\n");
                                osw.write(criteria.getJob() + "\n");

                                for (SearchCriteria c : recent) {
                                    osw.write(c.getKeyword() + "\n");
                                    osw.write(c.getProvince() + "\n");
                                    osw.write(c.getJob() + "\n");
                                }
                                osw.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("offersList", offersList);
                        bundle.putSerializable("criteria", criteria);
                        searchResult.putExtras(bundle);
                        startActivity(searchResult);
                    } else {
                        Snackbar.make(view, getResources().getString(R.string.dialog_dont_result), Snackbar.LENGTH_SHORT).show();
                    }
                    view.setEnabled(true);
                }
            });
            return null;
        }
    }
}
