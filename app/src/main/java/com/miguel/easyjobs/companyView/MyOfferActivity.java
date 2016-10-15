package com.miguel.easyjobs.companyView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.adapters.EmployeeAdapter;
import com.miguel.easyjobs.domain.Employee;
import com.miguel.easyjobs.domain.Inscription;
import com.miguel.easyjobs.domain.Offer;
import com.miguel.easyjobs.model.EmployeesTable;
import com.miguel.easyjobs.model.InscriptionsTable;
import com.miguel.easyjobs.model.OffersTable;
import com.miguel.easyjobs.util.PictureSetting;

import java.util.ArrayList;
import java.util.List;

public class MyOfferActivity extends AppCompatActivity {

    private boolean visibility;

    private OffersTable table;
    private Offer offer;
    private ImageView picture;
    private TextView title;
    private TextView company;
    private TextView province;

    private CardView signed_up;
    private TextView description;
    private TextView contractType;
    private TextView workingDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_my_offer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_my_offer);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = (TextView) findViewById(R.id.my_offer_title);
        company = (TextView) findViewById(R.id.my_offer_company);
        province = (TextView) findViewById(R.id.my_offer_province);
        picture = (ImageView) findViewById(R.id.my_offer_picture);
        description = (TextView) findViewById(R.id.my_offer_description);
        contractType = (TextView) findViewById(R.id.my_offer_contracttype);
        signed_up = (CardView) findViewById(R.id.my_offer_signed_up_user);
        workingDay = (TextView) findViewById(R.id.my_offer_workingday);

        SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);
        offer = (Offer) getIntent().getSerializableExtra("offer");

        title.setText(offer.getTitle());
        company.setText(sp.getString("fullname", ""));
        province.setText(offer.getProvince());
        description.setText(offer.getDescription());
        contractType.setText(getResources().getString(R.string.contract_type) + " " + offer.getContractType());
        workingDay.setText(offer.getWorkDay());

        //Cargar imagen
        picture.setImageBitmap(PictureSetting.loadPicture(this, sp.getString("email", ""), R.drawable.company));

        //Adaptador
        InscriptionsTable inscriptionsTable = new InscriptionsTable(this);
        final List<Inscription> inscriptionList = inscriptionsTable.findById(offer.getId());

        EmployeesTable employeesTable = new EmployeesTable(this);
        final List<Employee> employeeList = new ArrayList<>();
        for (Inscription inscription : inscriptionList) {
            employeeList.add(employeesTable.get(inscription.getEmail()));
        }

        if (employeeList.size() == 0)
            signed_up.setVisibility(View.GONE);

        EmployeeAdapter adapter = new EmployeeAdapter(this, employeeList);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.my_offer_users_list);
        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new EmployeeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(MyOfferActivity.this, EmployeeInfoActivity.class);
                i.putExtra("employee", employeeList.get(position));
                i.putExtra("id", offer.getId());
                startActivity(i);
            }
        });

        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        Intent i = getIntent();
        visibility = i.getBooleanExtra("visibility", true);
        table = new OffersTable(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, CompanyContainerActivity.class);
        i.putExtra("fragment", CompanyContainerActivity.OFFERS_LIST);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        if (item.getItemId() == R.id.action_visibility) {
            String message = getResources().getString(R.string.dialog_ask_visible_offer);
            if (visibility) {
                message = getResources().getString(R.string.dialog_ask_invisible_offer);
            }

            dialog.setTitle(getResources().getString(R.string.dialog_ask_change_visibility))
                    .setMessage(message)
                    .setPositiveButton(getResources().getString(R.string.dialog_accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            visibility = !visibility;
                            table.setVisibility(offer.getId(), visibility);
                            if (visibility)
                                item.setIcon(R.drawable.visible);
                            else
                                item.setIcon(R.drawable.invisible);
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.dialog_cancel), null);
        } else {
            dialog.setTitle(getResources().getString(R.string.app_name))
                    .setMessage(getResources().getString(R.string.dialog_ask_delete_offer))
                    .setPositiveButton(getResources().getString(R.string.dialog_accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            InscriptionsTable ins = new InscriptionsTable(MyOfferActivity.this);
                            ins.delete(offer.getId());
                            if (table.delete(offer.getId())) {
                                Toast.makeText(MyOfferActivity.this, getResources().getString(R.string.toast_delete_successfull), Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.dialog_cancel), null);

        }
        dialog.show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_offer, menu);
        if (!visibility)
            menu.findItem(R.id.action_visibility).setIcon(R.drawable.invisible);
        return super.onCreateOptionsMenu(menu);
    }


}
