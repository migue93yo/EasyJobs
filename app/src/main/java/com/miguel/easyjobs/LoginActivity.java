package com.miguel.easyjobs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.miguel.easyjobs.companyView.CompanyContainerActivity;
import com.miguel.easyjobs.employeeView.EmployeeContainerActivity;
import com.miguel.easyjobs.model.CompanyTable;
import com.miguel.easyjobs.model.EmployeesTable;

public class LoginActivity extends AppCompatActivity {

    private EditText count;
    private EditText pass;
    private CheckBox check;

    private Intent iProfile;
    private Intent main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        main = new Intent(this, MainActivity.class);

        count = (EditText) findViewById(R.id.login_email_input);
        pass = (EditText) findViewById(R.id.login_pass_input);
        check = (CheckBox) findViewById(R.id.login_show_pass);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.isChecked()) {
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    pass.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        Button login = (Button) findViewById(R.id.login_enter);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Login(count.getText().toString(), pass.getText().toString()).execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(main);
        finish();
    }

    //AsyncTask para insertar Personas
    private class Login extends AsyncTask<String, String, String> {

        private String email;
        private String password;

        public Login(String count, String pass) {
            this.email = count;
            this.password = pass;
        }

        @Override
        protected String doInBackground(String... params) {
            EmployeesTable employees = new EmployeesTable(LoginActivity.this);
            CompanyTable companies = new CompanyTable(LoginActivity.this);

            final boolean existEmployee = employees.exist(email, password);
            boolean existCompanies = false;
            if (!existEmployee) {
                existCompanies = companies.exist(email, password);
            }

            if (existEmployee || existCompanies) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("email", email);
                        if (existEmployee) {
                            edit.putBoolean("employee", true);
                            iProfile = new Intent(LoginActivity.this, EmployeeContainerActivity.class);
                            iProfile.putExtra("fragment", EmployeeContainerActivity.PROFILE);
                        } else {
                            edit.putBoolean("employee", false);
                            iProfile = new Intent(LoginActivity.this, CompanyContainerActivity.class);
                            iProfile.putExtra("fragment", CompanyContainerActivity.PROFILE);
                        }
                        edit.commit();

                        startActivity(iProfile);
                        finish();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
    }
}
