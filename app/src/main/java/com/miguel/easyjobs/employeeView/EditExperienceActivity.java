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
import android.view.Menu;
import android.view.MenuItem;
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

public class EditExperienceActivity extends AppCompatActivity {

    private EditText job;
    private EditText company;
    private EditText province;
    private EditText dateStart;
    private EditText dateEnd;
    private Switch switchButton;
    private Button modify;

    private Experience myExperience;
    private ExperiencesTable table;
    private Validator val;
    private int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_edit_experience);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit_experience);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        switchButton = (Switch) findViewById(R.id.edit_experience_currently_work);
        job = (EditText) findViewById(R.id.edit_experience_job);
        company = (EditText) findViewById(R.id.edit_experience_company);
        province = (EditText) findViewById(R.id.edit_experience_province);
        dateStart = (EditText) findViewById(R.id.edit_experience_datestart);
        dateEnd = (EditText) findViewById(R.id.edit_experience_datefinish);
        modify = (Button) findViewById(R.id.edit_experience_create);

        //Cargar formulario
        Bundle bundle = getIntent().getExtras();
        myExperience = (Experience) bundle.getSerializable("experience");
        job.setText(myExperience.getJob());
        province.setText(myExperience.getProvince());
        company.setText(myExperience.getCompany());

        selected = searchPositionByString(myExperience.getProvince());
        province.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditExperienceActivity.this)
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateStart.setText(dateFormat.format(myExperience.getDateStart()));

        if (!dateFormat.format(myExperience.getDateEnd()).equals("11/11/1111")) {
            dateEnd.setText(dateFormat.format(myExperience.getDateEnd()));
        } else {
            dateEnd.setEnabled(false);
            dateEnd.setHint(getResources().getString(R.string.actually));
            switchButton.setChecked(true);
        }

        dateStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    DialogFragment newFragment = DatePickerFragment.newInstance(EditExperienceActivity.this, dateStart, dateEnd, DatePickerFragment.DATE_START_TYPE);
                    newFragment.show(getSupportFragmentManager(), "");
                }
                return true;
            }
        });

        dateEnd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    DialogFragment newFragment = DatePickerFragment.newInstance(EditExperienceActivity.this, dateStart, dateEnd, DatePickerFragment.DATE_END_TYPE);
                    newFragment.show(getSupportFragmentManager(), "");
                }
                return true;
            }
        });

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dateEnd.setEnabled(false);
                    dateEnd.setText("");
                    dateEnd.setHint(getResources().getString(R.string.actually));
                } else {
                    dateEnd.setEnabled(true);
                    dateEnd.setHint(getResources().getString(R.string.datefinish));
                }
            }
        });

        val = new Validator(this);
        table = new ExperiencesTable(EditExperienceActivity.this);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (val.jobValidator(job)
                        && val.companyValidator(company)
                        && val.provinceValidator(province)
                        && val.dateStartValidator(dateStart)
                        && val.dateEndValidator(dateEnd, switchButton)) {
                    SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);

                    Experience experience = new Experience();

                    experience.setId(myExperience.getId());
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
                            experience.setDateEnd(dateFormat.parse(dateEnd.getText().toString()));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (table.update(experience)) {
                        Toast.makeText(EditExperienceActivity.this, getResources().getString(R.string.toast_correct_update), Toast.LENGTH_SHORT).show();
                        Intent profile = new Intent(EditExperienceActivity.this, EmployeeContainerActivity.class);
                        profile.putExtra("fragment", EmployeeContainerActivity.PROFILE);
                        startActivity(profile);
                        finish();
                    } else {
                        Toast.makeText(EditExperienceActivity.this, getResources().getString(R.string.toast_update_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.dialog_delete))
                .setMessage(getResources().getString(R.string.dialog_ask_delete_expetience))
                .setPositiveButton(getResources().getString(R.string.dialog_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (table.delete(myExperience.getId())) {
                            Toast.makeText(EditExperienceActivity.this, getResources().getString(R.string.toast_delete_successfull), Toast.LENGTH_SHORT).show();

                            Intent profile = new Intent(EditExperienceActivity.this, EmployeeContainerActivity.class);
                            profile.putExtra("fragment", EmployeeContainerActivity.PROFILE);
                            startActivity(profile);
                            finish();
                        } else {
                            Toast.makeText(EditExperienceActivity.this, getResources().getString(R.string.toast_delete_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_cancel), null);
        dialog.show();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent profile = new Intent(EditExperienceActivity.this, EmployeeContainerActivity.class);
        profile.putExtra("fragment", EmployeeContainerActivity.PROFILE);
        startActivity(profile);
    }

    private int searchPositionByString(String province) {
        boolean ok = false;
        int i = -1;
        while (!ok && i != Util.PROVINCES.length - 1) {
            i++;
            if (Util.PROVINCES[i].equals(province))
                ok = true;
        }
        return i;
    }
}
