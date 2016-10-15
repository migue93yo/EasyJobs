package com.miguel.easyjobs.companyView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.domain.Offer;
import com.miguel.easyjobs.model.OffersTable;
import com.miguel.easyjobs.util.DatePickerFragment;
import com.miguel.easyjobs.util.Util;
import com.miguel.easyjobs.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NewOfferActivity extends AppCompatActivity {

    private EditText title;
    private EditText job;
    private EditText province;
    private EditText duration;
    private EditText description;
    private Spinner contractType;
    private Spinner workDay;
    private CheckBox visible;
    private Button create;

    private Validator val;
    private String[] contractTypesTitles;
    private String[] workDayTitle;
    private int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_new_offer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = (EditText) findViewById(R.id.new_offer_title);
        job = (EditText) findViewById(R.id.new_offer_job);
        province = (EditText) findViewById(R.id.new_offer_province);
        duration = (EditText) findViewById(R.id.new_offer_date);
        description = (EditText) findViewById(R.id.new_offer_description);
        contractType = (Spinner) findViewById(R.id.new_offer_contract_type);
        workDay = (Spinner) findViewById(R.id.new_offer_workday);
        visible = (CheckBox) findViewById(R.id.new_offer_visible);
        create = (Button) findViewById(R.id.new_offer_create);

        selected = -1;
        province.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(NewOfferActivity.this)
                            .setSingleChoiceItems(Util.PROVINCES, selected, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    province.setText(Util.PROVINCES[which]);
                                    selected = which;
                                    dialog.dismiss();
                                }
                            });
                    dialog.show();
                }
                return true;
            }
        });

        contractTypesTitles = new String[]{
                getResources().getString(R.string.indefinited),
                getResources().getString(R.string.temporal),
                getResources().getString(R.string.in_practices)};
        ArrayAdapter ctAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, contractTypesTitles);
        contractType.setAdapter(ctAdapter);

        workDayTitle = new String[]{
                getResources().getString(R.string.workday_complete),
                getResources().getString(R.string.workday_half),
                getResources().getString(R.string.workday_partial)};
        ArrayAdapter wdAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, workDayTitle);
        workDay.setAdapter(wdAdapter);

        duration.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    DialogFragment newFragment = DatePickerFragment.newInstance(NewOfferActivity.this, duration, null, DatePickerFragment.OFFER);
                    newFragment.show(getSupportFragmentManager(), "");
                }
                return true;
            }
        });

        val = new Validator(this);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (val.titleValidator(title)
                        && val.jobValidator(job)
                        && val.provinceValidator(province)
                        && val.durationValidator(duration)
                        && val.descriptionValidator(description)) {
                    SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);

                    Offer offer = new Offer();
                    offer.setEmail(sp.getString("email", ""));
                    offer.setTitle(title.getText().toString());
                    offer.setJob(job.getText().toString());
                    offer.setProvince(province.getText().toString());
                    offer.setTitle(title.getText().toString());
                    offer.setDescription(description.getText().toString());

                    String contractTypeSelected = null;
                    switch (contractType.getSelectedItemPosition()) {
                        case 0:
                            contractTypeSelected = Offer.CONTRACT_INDEFINITED;
                            break;
                        case 1:
                            contractTypeSelected = Offer.CONTRACT_TEMPORAL;
                            break;
                        case 2:
                            contractTypeSelected = Offer.CONTRACT_IN_PRACTICES;
                    }
                    offer.setContractType(contractTypeSelected);

                    String workDaySelected = null;
                    switch (contractType.getSelectedItemPosition()) {
                        case 0:
                            workDaySelected = Offer.WORKDAY_COMPLETE;
                            break;
                        case 1:
                            workDaySelected = Offer.WORKDAY_HALF;
                            break;
                        case 2:
                            workDaySelected = Offer.WORKDAY_PARTIAL;
                    }
                    offer.setWorkDay(workDaySelected);


                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        offer.setDuration(dateFormat.parse(duration.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (visible.isChecked())
                        offer.setState(true);
                    else
                        offer.setState(false);

                    OffersTable table = new OffersTable(NewOfferActivity.this);
                    if (table.set(offer)) {
                        Toast.makeText(NewOfferActivity.this, getResources().getString(R.string.toast_create_successfull), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(NewOfferActivity.this, CompanyContainerActivity.class);
                        i.putExtra("fragment", CompanyContainerActivity.OFFERS_LIST);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(NewOfferActivity.this, getResources().getString(R.string.toast_create_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, CompanyContainerActivity.class);
        i.putExtra("fragment", CompanyContainerActivity.OFFERS_LIST);
        startActivity(i);
    }
}
