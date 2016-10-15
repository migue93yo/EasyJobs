package com.miguel.easyjobs.employeeView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.domain.Employee;
import com.miguel.easyjobs.model.EmployeesTable;
import com.miguel.easyjobs.util.DatePickerFragment;
import com.miguel.easyjobs.util.Util;
import com.miguel.easyjobs.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditProfileEmployeeActivity extends AppCompatActivity {

    private EditText fullname;
    private EditText province;
    private EditText birthday;
    private EditText phone;
    private Button edit;
    private Employee emp;

    private SimpleDateFormat dateFormat;
    private Validator val;
    private int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_edit_profile_employee);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit_profile_employee);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        emp = (Employee) bundle.getSerializable("employee");

        fullname = (EditText) findViewById(R.id.edit_profile_employee_fullname);
        province = (EditText) findViewById(R.id.edit_profile_employee_province);
        birthday = (EditText) findViewById(R.id.edit_profile_employee_birthdate);
        phone = (EditText) findViewById(R.id.edit_profile_employee_phone);
        edit = (Button) findViewById(R.id.edit_profile_employee_edit);

        selected = searchPositionByString(emp.getProvince());
        province.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditProfileEmployeeActivity.this)
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

        fullname.setText(emp.getFullname());
        province.setText(emp.getProvince());

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        birthday.setText(dateFormat.format(emp.getBirthdate()));
        phone.setText(emp.getPhone());

        birthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    DialogFragment newFragment = DatePickerFragment.newInstance(EditProfileEmployeeActivity.this, birthday, null, DatePickerFragment.BIRTHDATE);
                    newFragment.show(getSupportFragmentManager(), "");
                }
                return true;
            }
        });

        val = new Validator(this);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (val.nameValidator(fullname)
                        && val.provinceValidator(province)
                        && val.phoneValidator(phone)) {
                    Employee employeeEdit = new Employee();

                    employeeEdit.setEmail(emp.getEmail());
                    employeeEdit.setFullname(fullname.getText().toString());
                    employeeEdit.setProvince(province.getText().toString());
                    employeeEdit.setPhone(phone.getText().toString());

                    try {
                        employeeEdit.setBirthdate(dateFormat.parse(birthday.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    EmployeesTable table = new EmployeesTable(EditProfileEmployeeActivity.this);
                    if (table.update(employeeEdit) > 0) {
                        Toast.makeText(EditProfileEmployeeActivity.this, getResources().getString(R.string.toast_correct_update), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(EditProfileEmployeeActivity.this, EmployeeContainerActivity.class);
                        i.putExtra("fragment", EmployeeContainerActivity.PROFILE);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(EditProfileEmployeeActivity.this, getResources().getString(R.string.toast_update_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, EmployeeContainerActivity.class);
        i.putExtra("fragment", EmployeeContainerActivity.PROFILE);
        startActivity(i);
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
