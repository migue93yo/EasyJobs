package com.miguel.easyjobs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.miguel.easyjobs.companyView.CompanyContainerActivity;
import com.miguel.easyjobs.domain.Company;
import com.miguel.easyjobs.domain.Employee;
import com.miguel.easyjobs.employeeView.EmployeeContainerActivity;
import com.miguel.easyjobs.model.CompanyTable;
import com.miguel.easyjobs.model.EmployeesTable;
import com.miguel.easyjobs.util.DatePickerFragment;
import com.miguel.easyjobs.util.Util;
import com.miguel.easyjobs.util.Validator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private EditText count;
    private EditText pass;
    private EditText name;
    private EditText birthdate;
    private EditText province;
    private Spinner type;
    private EditText phone;
    private Button register;

    private Intent profile;
    private String employeeString;
    private String companyString;
    private String[] typeList;
    private Validator val;
    private int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        employeeString = getResources().getString(R.string.employee);
        companyString = getResources().getString(R.string.company);
        typeList = new String[]{employeeString, companyString};

        count = (EditText) findViewById(R.id.register_email_input);
        pass = (EditText) findViewById(R.id.register_pass_input);
        name = (EditText) findViewById(R.id.register_name_input);
        province = (EditText) findViewById(R.id.register_province);
        birthdate = (EditText) findViewById(R.id.register_birthday);
        phone = (EditText) findViewById(R.id.register_phone);

        birthdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    DialogFragment newFragment = DatePickerFragment.newInstance(RegisterActivity.this, birthdate, null, DatePickerFragment.BIRTHDATE);
                    newFragment.show(getSupportFragmentManager(), "");
                }
                return true;
            }
        });

        selected = -1;
        province.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this)
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

        type = (Spinner) findViewById(R.id.register_type);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, typeList);
        type.setAdapter(typeAdapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    birthdate.setVisibility(View.VISIBLE);
                    phone.setVisibility(View.VISIBLE);
                } else {
                    birthdate.setVisibility(View.GONE);
                    phone.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        val = new Validator(this);
        register = (Button) findViewById(R.id.register_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (val.nameValidator(name)
                        && val.emailValidator(count)
                        && val.passValidator(pass)
                        && val.provinceValidator(province)
                        && val.phoneValidator(phone)) {

                    String workerType = "";
                    if (type.getSelectedItemPosition() == 0)
                        workerType = employeeString;
                    else
                        workerType = companyString;

                    new CreateNewUser(count.getText().toString(),
                            pass.getText().toString(),
                            name.getText().toString(),
                            province.getText().toString(),
                            workerType,
                            birthdate.getText().toString(),
                            phone.getText().toString()).execute();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    private class CreateNewUser extends AsyncTask<String, String, String> {

        private String email;
        private String pass;
        private String name;
        private String province;
        private String type;
        private String birthday;
        private String myPhone;
        private boolean exist;

        public CreateNewUser(String email, String pass, String name, String province, String type, String birthday, String myPhone) {
            this.email = email;
            this.pass = pass;
            this.name = name;
            this.province = province;
            this.type = type;
            this.birthday = birthday;
            this.myPhone = myPhone;
        }

        @Override
        protected String doInBackground(String... params) {
            final CompanyTable comp = new CompanyTable(RegisterActivity.this);
            final EmployeesTable emp = new EmployeesTable(RegisterActivity.this);

            exist = emp.exist(email);
            if (!exist)
                exist = comp.exist(email);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (exist) {
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.toast_email_exist), Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("email", count.getText().toString());
                        edit.putBoolean("notification", true);

                        if (type.equals(getResources().getString(R.string.employee))) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = null;
                            try {
                                date = dateFormat.parse(birthday);
                            } catch (java.text.ParseException e) {
                                e.printStackTrace();
                            }

                            edit.putBoolean("employee", true);
                            Employee employee = new Employee(email, pass, name, province, date, myPhone);
                            emp.set(employee);
                            profile = new Intent(RegisterActivity.this, EmployeeContainerActivity.class);
                            profile.putExtra("fragment", EmployeeContainerActivity.PROFILE);
                        } else {
                            edit.putBoolean("employee", false);
                            Company company = new Company(email, name, pass, province);
                            comp.set(company);
                            profile = new Intent(RegisterActivity.this, CompanyContainerActivity.class);
                            profile.putExtra("fragment", CompanyContainerActivity.PROFILE);
                        }

                        edit.commit();

                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.toast_correct_email), Toast.LENGTH_SHORT).show();
                        startActivity(profile);
                        finish();
                    }
                }
            });
            return null;
        }
    }
}
