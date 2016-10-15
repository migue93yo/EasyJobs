package com.miguel.easyjobs.employeeView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.domain.Inscription;
import com.miguel.easyjobs.domain.Offer;
import com.miguel.easyjobs.model.CompanyTable;
import com.miguel.easyjobs.model.InscriptionsTable;
import com.miguel.easyjobs.util.PictureSetting;

public class OfferActivity extends AppCompatActivity {

    private ImageView image;
    private TextView title;
    private TextView company;
    private TextView province;
    private TextView description;
    private TextView contractType;
    private TextView workingDay;
    private TextView inscriptions;
    private Button button;

    private boolean exist;
    private long numInscriptions;
    private InscriptionsTable inscriptionsTable;
    private Offer offer;

    public static byte SEARCH_MODE = 0;
    public static byte CANDIDATURE_MODE = 1;
    private byte type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_offer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_offer);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent myIntent = getIntent();
        Bundle bundle = myIntent.getExtras();
        type = myIntent.getByteExtra("type", SEARCH_MODE);
        offer = (Offer) bundle.getSerializable("offer");
        setTitle(offer.getTitle());

        image = (ImageView) findViewById(R.id.offer_picture);
        title = (TextView) findViewById(R.id.offer_title);
        company = (TextView) findViewById(R.id.offer_company);
        province = (TextView) findViewById(R.id.offer_province);
        description = (TextView) findViewById(R.id.offer_description);
        contractType = (TextView) findViewById(R.id.offer_contracttype);
        workingDay = (TextView) findViewById(R.id.offer_workingday);
        inscriptions = (TextView) findViewById(R.id.offer_inscriptions);

        inscriptionsTable = new InscriptionsTable(OfferActivity.this);
        numInscriptions = inscriptionsTable.countEmail(offer.getId());
        inscriptions.setText(String.valueOf(numInscriptions + " ").concat(inscriptions.getText().toString()));

        SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);
        exist = inscriptionsTable.isCheck(offer.getId(), sp.getString("email", ""));

        button = (Button) findViewById(R.id.offer_inscription_button);
        if (exist)
            button.setText(getResources().getString(R.string.cancel_inscription));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(OfferActivity.this);
                if (exist) {
                    dialog.setTitle(getResources().getString(R.string.inscription));
                    dialog.setMessage(getResources().getString(R.string.dialog_cancel_inscription));
                    dialog.setPositiveButton(getResources().getString(R.string.dialog_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);
                            boolean ok = inscriptionsTable.delete(offer.getId(), sp.getString("email", null));
                            if (ok) {
                                button.setText(getResources().getString(R.string.inscription));
                                exist = false;

                                numInscriptions--;
                                inscriptions.setText(String.valueOf(numInscriptions + " ").concat(getResources().getString(R.string.people_have_registered)));
                            }
                        }
                    });
                    dialog.setNegativeButton(getResources().getString(R.string.dialog_cancel), null);
                    dialog.show();
                } else {
                    dialog.setTitle(getResources().getString(R.string.inscription));
                    dialog.setMessage(getResources().getString(R.string.dialog_ask_inscription));
                    dialog.setPositiveButton(getResources().getString(R.string.dialog_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);
                            Inscription inscription = new Inscription(offer.getId(), sp.getString("email", null));
                            boolean ok = inscriptionsTable.set(inscription);
                            if (ok) {
                                button.setText(getResources().getString(R.string.cancel_inscription));
                                exist = true;

                                numInscriptions++;
                                inscriptions.setText(String.valueOf(numInscriptions + " ").concat(getResources().getString(R.string.people_have_registered)));
                            }
                        }
                    });
                    dialog.setNegativeButton(getResources().getString(R.string.dialog_cancel), null);
                    dialog.show();
                }
            }
        });

        //Cargar foto
        Bitmap bitmap = PictureSetting.loadPicture(this, offer.getEmail(), R.drawable.company);
        image.setImageBitmap(bitmap);

        title.setText(offer.getTitle());

        CompanyTable ct = new CompanyTable(this);
        company.setText(ct.getName(offer.getEmail()));
        province.setText(offer.getProvince());
        description.setText(offer.getDescription());
        contractType.setText(contractType.getText().toString().concat(offer.getContractType()));
        workingDay.setText(offer.getWorkDay());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (type == CANDIDATURE_MODE) {
            Intent back = new Intent(this, EmployeeContainerActivity.class);
            back.putExtra("fragment", EmployeeContainerActivity.CANDIDATURES);
            startActivity(back);
        }
    }
}
