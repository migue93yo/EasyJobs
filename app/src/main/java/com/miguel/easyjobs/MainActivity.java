package com.miguel.easyjobs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miguel.easyjobs.companyView.CompanyContainerActivity;
import com.miguel.easyjobs.employeeView.EmployeeContainerActivity;
import com.miguel.easyjobs.employeeView.NotificationsEmployeeService;

public class MainActivity extends AppCompatActivity {

    private Intent register;
    private Intent login;
    private Intent profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        register = new Intent(this, RegisterActivity.class);
        login = new Intent(this, LoginActivity.class);

        SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);
        if (sp.getBoolean("logued", false)) {
            if (sp.getBoolean("employee", true)) {
                profile = new Intent(this, EmployeeContainerActivity.class);
                profile.putExtra("fragment", EmployeeContainerActivity.PROFILE);
                Intent i = new Intent(this, NotificationsEmployeeService.class);
                startService(i);
            } else {
                profile = new Intent(this, CompanyContainerActivity.class);
                profile.putExtra("fragment", CompanyContainerActivity.PROFILE);
            }

            startActivity(profile);
            finish();
        }

        Button registerButton = (Button) findViewById(R.id.main_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(register);
                finish();
            }
        });

        TextView loginButton = (TextView) findViewById(R.id.main_enter);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(login);
                finish();
            }
        });

    }

}
