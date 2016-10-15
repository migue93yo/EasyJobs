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
import com.miguel.easyjobs.domain.Title;
import com.miguel.easyjobs.model.TitlesTable;
import com.miguel.easyjobs.util.DatePickerFragment;
import com.miguel.easyjobs.util.Util;
import com.miguel.easyjobs.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditTitleActivity extends AppCompatActivity {

    private EditText title;
    private EditText center;
    private EditText province;
    private EditText dateStart;
    private EditText dateEnd;
    private Switch switchButton;
    private Button modify;

    private Title myTitle;
    private TitlesTable table;
    private Validator val;
    private int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_edit_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit_title);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        switchButton = (Switch) findViewById(R.id.edit_title_currently_work);
        title = (EditText) findViewById(R.id.edit_title_title);
        center = (EditText) findViewById(R.id.edit_title_center);
        province = (EditText) findViewById(R.id.edit_title_province);
        dateStart = (EditText) findViewById(R.id.edit_title_datestart);
        dateEnd = (EditText) findViewById(R.id.edit_title_datefinish);

        //Cargar formulario
        Bundle bundle = getIntent().getExtras();
        myTitle = (Title) bundle.getSerializable("title");
        title.setText(myTitle.getTitle());
        province.setText(myTitle.getProvince());
        center.setText(myTitle.getCenter());

        selected = searchPositionByString(myTitle.getProvince());
        province.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditTitleActivity.this)
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
        dateStart.setText(dateFormat.format(myTitle.getDateStart()));

        if (!dateFormat.format(myTitle.getDateEnd()).equals("11/11/1111")) {
            dateEnd.setText(dateFormat.format(myTitle.getDateEnd()));
        } else {
            switchButton.setChecked(true);
            dateEnd.setHint("Actualmente");
        }

        dateStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    DialogFragment newFragment = DatePickerFragment.newInstance(EditTitleActivity.this, dateStart, dateEnd, DatePickerFragment.DATE_START_TYPE);
                    newFragment.show(getSupportFragmentManager(), "");
                }
                return true;
            }
        });

        dateEnd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    DialogFragment newFragment = DatePickerFragment.newInstance(EditTitleActivity.this, dateStart, dateEnd, DatePickerFragment.DATE_END_TYPE);
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
                    dateEnd.setHint("Actualmente");
                } else {
                    dateEnd.setEnabled(true);
                    dateEnd.setHint("Fecha de fin");
                }
            }
        });

        val = new Validator(this);
        table = new TitlesTable(EditTitleActivity.this);
        modify = (Button) findViewById(R.id.edit_title_edit);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (val.titleValidator(title)
                        && val.centerValidator(center)
                        && val.provinceValidator(province)
                        && val.dateStartValidator(dateStart)
                        && val.dateEndValidator(dateEnd, switchButton)) {
                    Title t = new Title();

                    SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);

                    t.setId(myTitle.getId());
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
                            t.setDateEnd(dateFormat.parse(dateEnd.getText().toString()));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (table.update(t) > 0) {
                        Toast.makeText(EditTitleActivity.this, getResources().getString(R.string.toast_correct_update), Toast.LENGTH_SHORT).show();
                        Intent profile = new Intent(EditTitleActivity.this, EmployeeContainerActivity.class);
                        profile.putExtra("fragment", EmployeeContainerActivity.PROFILE);
                        startActivity(profile);
                        finish();
                    } else {
                        Toast.makeText(EditTitleActivity.this, getResources().getString(R.string.toast_update_error), Toast.LENGTH_SHORT).show();
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
                .setMessage(getResources().getString(R.string.dialog_ask_delete_title))
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (table.delete(myTitle.getId())) {
                            Intent profile = new Intent(EditTitleActivity.this, EmployeeContainerActivity.class);
                            profile.putExtra("fragment", EmployeeContainerActivity.PROFILE);
                            startActivity(profile);
                            finish();

                            Toast.makeText(EditTitleActivity.this, getResources().getString(R.string.toast_delete_successfull), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditTitleActivity.this, getResources().getString(R.string.toast_delete_error), Toast.LENGTH_SHORT).show();
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
        Intent profile = new Intent(EditTitleActivity.this, EmployeeContainerActivity.class);
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
