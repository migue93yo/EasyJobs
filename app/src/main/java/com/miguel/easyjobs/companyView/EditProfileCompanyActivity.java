package com.miguel.easyjobs.companyView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.miguel.easyjobs.domain.Company;
import com.miguel.easyjobs.model.CompanyTable;
import com.miguel.easyjobs.util.Util;
import com.miguel.easyjobs.util.Validator;

public class EditProfileCompanyActivity extends AppCompatActivity {

    private EditText name;
    private EditText province;
    private EditText description;
    private Button modify;

    private Company company;
    private Validator val;
    private int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_edit_profile_company);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit_profile_company);
        setSupportActionBar(toolbar);

        name = (EditText) findViewById(R.id.edit_profile_company_name);
        province = (EditText) findViewById(R.id.edit_profile_company_province);
        description = (EditText) findViewById(R.id.edit_profile_company_description);
        modify = (Button) findViewById(R.id.edit_profile_company_edit);

        selected = searchPositionByString(province.getText().toString());
        province.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditProfileCompanyActivity.this)
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

        company = (Company) getIntent().getSerializableExtra("company");

        name.setText(company.getName());
        province.setText(company.getProvince());
        description.setText(company.getDescription());

        val = new Validator(this);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (val.nameValidator(name) && val.provinceValidator(province)) {
                    Company companyEdit = new Company();

                    companyEdit.setEmail(company.getEmail());
                    companyEdit.setName(name.getText().toString());
                    companyEdit.setProvince(province.getText().toString());
                    companyEdit.setDescription(description.getText().toString());

                    CompanyTable table = new CompanyTable(EditProfileCompanyActivity.this);
                    if (table.update(companyEdit)) {
                        Toast.makeText(EditProfileCompanyActivity.this, getResources().getString(R.string.toast_correct_update), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(EditProfileCompanyActivity.this, CompanyContainerActivity.class);
                        i.putExtra("fragment", CompanyContainerActivity.PROFILE);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(EditProfileCompanyActivity.this, getResources().getString(R.string.toast_update_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, CompanyContainerActivity.class);
        i.putExtra("fragment", CompanyContainerActivity.PROFILE);
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
