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
import com.miguel.easyjobs.domain.Title;
import com.miguel.easyjobs.model.TitlesTable;
import com.miguel.easyjobs.util.DatePickerFragment;
import com.miguel.easyjobs.util.Util;
import com.miguel.easyjobs.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NewTitleActivity extends AppCompatActivity {

    private EditText title;
    private EditText center;
    private EditText province;
    private EditText dateStart;
    private EditText dateFinish;
    private Switch switchButton;
    private Button create;

    private TitlesTable table;
    private Validator val;
    private int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_new_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_new_title);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        switchButton = (Switch) findViewById(R.id.new_title_currently_work);
        title = (EditText) findViewById(R.id.new_title_title);
        center = (EditText) findViewById(R.id.new_title_center);
        province = (EditText) findViewById(R.id.new_title_province);
        dateStart = (EditText) findViewById(R.id.new_title_datestart);
        dateFinish = (EditText) findViewById(R.id.new_title_datefinish);

        selected = -1;
        province.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(NewTitleActivity.this)
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
                    DialogFragment newFragment = DatePickerFragment.newInstance(NewTitleActivity.this, dateStart, dateFinish, DatePickerFragment.DATE_START_TYPE);
                    newFragment.show(getSupportFragmentManager(), "");
                }
                return true;
            }
        });

        dateFinish.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    DialogFragment newFragment = DatePickerFragment.newInstance(NewTitleActivity.this, dateStart, dateFinish, DatePickerFragment.DATE_END_TYPE);
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
        create = (Button) findViewById(R.id.new_title_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (val.titleValidator(title)
                        && val.centerValidator(center)
                        && val.provinceValidator(province)
                        && val.dateStartValidator(dateStart)
                        && val.dateEndValidator(dateFinish, switchButton)) {
                    Title t = new Title();

                    SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);

                    t.setEmail(sp.getString("email", null));
                    t.setTitle(title.getText().toString());
                    t.setCenter(center.getText().toString());
                    t.setProvince(province.getText().toString());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        t.setDateStart(dateFormat.parse(dateStart.getText().toString()));
                        if (switchButton.isChecked()) {
                            t.setDateEnd(dateFormat.parse("11/11/1111"));
                        } else {
                            t.setDateEnd(dateFormat.parse(dateFinish.getText().toString()));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    table = new TitlesTable(NewTitleActivity.this);
                    if (table.set(t) > 0) {
                        Toast.makeText(NewTitleActivity.this, getResources().getString(R.string.toast_create_successfull), Toast.LENGTH_SHORT).show();
                        Intent profile = new Intent(NewTitleActivity.this, EmployeeContainerActivity.class);
                        profile.putExtra("fragment", EmployeeContainerActivity.PROFILE);
                        startActivity(profile);
                        finish();
                    } else {
                        Toast.makeText(NewTitleActivity.this, getResources().getString(R.string.toast_create_error), Toast.LENGTH_SHORT).show();
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
