package com.miguel.easyjobs.employeeView;

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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.domain.Experience;
import com.miguel.easyjobs.model.ExperiencesTable;
import com.miguel.easyjobs.util.DatePickerFragment;
import com.miguel.easyjobs.util.Util;
import com.miguel.easyjobs.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NewExperienceActivity extends AppCompatActivity {

    private EditText job;
    private EditText company;
    private EditText province;
    private EditText dateStart;
    private EditText dateFinish;
    private Switch switchButton;
    private Button create;

    private ExperiencesTable table;
    private Validator val;
    private int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_new_experience);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_new_experience);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        switchButton = (Switch) findViewById(R.id.new_experience_currently_work);
        job = (EditText) findViewById(R.id.new_experience_job);
        company = (EditText) findViewById(R.id.new_experience_company);
        province = (EditText) findViewById(R.id.new_experience_province);
        dateStart = (EditText) findViewById(R.id.new_experience_datestart);
        dateFinish = (EditText) findViewById(R.id.new_experience_datefinish);

        selected = -1;
        province.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(NewExperienceActivity.this)
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

        dateStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    DialogFragment newFragment = DatePickerFragment.newInstance(NewExperienceActivity.this, dateStart, dateFinish, DatePickerFragment.DATE_START_TYPE);
                    newFragment.show(getSupportFragmentManager(), "");
                }
                return true;
            }
        });

        dateFinish.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    DialogFragment newFragment = DatePickerFragment.newInstance(NewExperienceActivity.this, dateStart, dateFinish, DatePickerFragment.DATE_END_TYPE);
                    newFragment.show(getSupportFragmentManager(), "");
                }
                return true;
            }
        });

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dateFinish.setEnabled(false);
                    dateFinish.setText("");
                    dateFinish.setHint(getResources().getString(R.string.actually));
                } else {
                    dateFinish.setEnabled(true);
                    dateFinish.setHint(getResources().getString(R.string.datefinish));
                }
            }
        });

        val = new Validator(this);
        create = (Button) findViewById(R.id.new_experience_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (val.jobValidator(job)
                        && val.companyValidator(company)
                        && val.provinceValidator(province)
                        && val.dateStartValidator(dateStart)
                        && val.dateEndValidator(dateFinish, switchButton)) {
                    SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);

                    Experience experience = new Experience();

                    experience.setEmail(sp.getString("email", null));
                    experience.setJob(job.getText().toString());
                    experience.setCompany(company.getText().toString());
                    experience.setProvince(province.getText().toString());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        experience.setDateStart(dateFormat.parse(dateStart.getText().toString()));
                        if (switchButton.isChecked()) {
                            experience.setDateEnd(dateFormat.parse("11/11/1111"));
                        } else {
                            experience.setDateEnd(dateFormat.parse(dateFinish.getText().toString()));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    table = new ExperiencesTable(NewExperienceActivity.this);
                    if (table.set(experience)) {
                        Toast.makeText(NewExperienceActivity.this, getResources().getString(R.string.toast_correct_update), Toast.LENGTH_SHORT).show();

                        Intent profile = new Intent(NewExperienceActivity.this, EmployeeContainerActivity.class);
                        profile.putExtra("fragment", EmployeeContainerActivity.PROFILE);
                        startActivity(profile);
                        finish();
                    } else {
                        Toast.makeText(NewExperienceActivity.this, getResources().getString(R.string.toast_update_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent profile = new Intent(this, EmployeeContainerActivity.class);
        profile.putExtra("fragment", EmployeeContainerActivity.PROFILE);
        startActivity(profile);
    }
}
