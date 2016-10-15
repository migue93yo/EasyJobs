package com.miguel.easyjobs.companyView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.adapters.ExperiencesAdapter;
import com.miguel.easyjobs.adapters.TitlesAdapter;
import com.miguel.easyjobs.domain.Employee;
import com.miguel.easyjobs.domain.Experience;
import com.miguel.easyjobs.domain.Inscription;
import com.miguel.easyjobs.domain.Offer;
import com.miguel.easyjobs.domain.Title;
import com.miguel.easyjobs.model.ExperiencesTable;
import com.miguel.easyjobs.model.InscriptionsTable;
import com.miguel.easyjobs.model.TitlesTable;
import com.miguel.easyjobs.util.PictureSetting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EmployeeInfoActivity extends AppCompatActivity {

    private ImageView picture;
    private TextView fullname;
    private TextView province;
    private TextView birthdate;
    private TextView phone;
    private Button change;
    private RecyclerView listViewExp;
    private RecyclerView listViewtitles;
    private CardView cardExperiences;
    private CardView cardTitles;
    private TextView empty;

    private Inscription inscription;
    private InscriptionsTable table;
    private String[] states;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_employee_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_employee_information);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        states = new String[]{
                getResources().getString(R.string.sign_on),
                getResources().getString(R.string.selected),
                getResources().getString(R.string.finalized)};

        picture = (ImageView) findViewById(R.id.employee_information_image);
        fullname = (TextView) findViewById(R.id.employee_information_fullname);
        province = (TextView) findViewById(R.id.employee_information_province);
        birthdate = (TextView) findViewById(R.id.employee_information_birthday);
        phone = (TextView) findViewById(R.id.employee_information_phone);
        listViewExp = (RecyclerView) findViewById(R.id.employee_information_experienceslist);
        listViewtitles = (RecyclerView) findViewById(R.id.employee_information_educationlist);
        cardExperiences = (CardView) findViewById(R.id.employee_information_cardview_experiences);
        cardTitles = (CardView) findViewById(R.id.employee_information_cardview_titles);
        change = (Button) findViewById(R.id.employee_information_state);
        empty = (TextView) findViewById(R.id.employee_info_empty);

        Intent i = getIntent();
        Employee employee = (Employee) i.getSerializableExtra("employee");
        fullname.setText(employee.getFullname());
        province.setText(employee.getProvince());
        phone.setText(employee.getPhone());
        setTitle(employee.getFullname());

        table = new InscriptionsTable(this);
        inscription = table.get(i.getIntExtra("id", -1), employee.getEmail());
        change.setText(inscription.getState());
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EmployeeInfoActivity.this)
                        .setTitle(getResources().getString(R.string.dialog_change_state))
                        .setItems(states, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean ok = false;

                                switch (which) {
                                    case 0:
                                        inscription.setState(Offer.STATE_SIGN_UP);
                                        ok = table.update(inscription);
                                        change.setText(getResources().getString(R.string.sign_on));
                                        break;
                                    case 1:
                                        inscription.setState(Offer.STATE_SELECTED);
                                        ok = table.update(inscription);
                                        change.setText(getResources().getString(R.string.selected));
                                        break;
                                    case 2:
                                        inscription.setState(Offer.STATE_FINALIZED);
                                        ok = table.update(inscription);
                                        change.setText(getResources().getString(R.string.finalized));
                                }

                                if (ok)
                                    Snackbar.make(v, getResources().getString(R.string.toast_correct_update), Snackbar.LENGTH_SHORT).show();
                                else
                                    Snackbar.make(v, getResources().getString(R.string.toast_update_error), Snackbar.LENGTH_SHORT).show();
                            }
                        });
                dialog.show();
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        int myBirthday;
        int today;
        int year;

        myBirthday = Integer.valueOf(dateFormat.format((employee.getBirthdate())));
        today = Integer.parseInt(dateFormat.format(new Date()));
        year = (today - myBirthday) / 10000;
        birthdate.setText(String.valueOf(year) + " " + getResources().getString(R.string.year));

        //Cargar foto
        picture.setImageBitmap(PictureSetting.loadPicture(this, employee.getEmail(), R.drawable.buddyicon));

        //Cargar la lista de experiencias
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        listViewExp.setLayoutManager(lManager);

        ExperiencesTable expTable = new ExperiencesTable(this);
        final List<Experience> experiencesList = expTable.findByEmail(employee.getEmail());
        ExperiencesAdapter expAdapter = new ExperiencesAdapter(this, experiencesList);
        listViewExp.setAdapter(expAdapter);

        if (listViewExp.getAdapter().getItemCount() == 0) {
            cardExperiences.setVisibility(View.GONE);
        }

        //Cargar la lista de títulos
        RecyclerView.LayoutManager lManager2 = new LinearLayoutManager(this);
        listViewtitles.setLayoutManager(lManager2);

        TitlesTable titlesTable = new TitlesTable(this);
        final List<Title> titlesList = titlesTable.findByEmail(employee.getEmail());
        final TitlesAdapter titleAdapter = new TitlesAdapter(this, titlesList);
        listViewtitles.setAdapter(titleAdapter);

        if (listViewtitles.getAdapter().getItemCount() == 0) {
            cardTitles.setVisibility(View.GONE);
        }

        if (cardExperiences.getVisibility() == View.GONE
                && cardTitles.getVisibility() == View.GONE) {
            empty.setVisibility(View.VISIBLE);
        }

    }
}
