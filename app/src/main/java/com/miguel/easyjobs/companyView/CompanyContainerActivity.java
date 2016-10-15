package com.miguel.easyjobs.companyView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.util.PictureSetting;

public class CompanyContainerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int NONE = -1;
    public static final int PROFILE = 0;
    public static final int OFFERS_LIST = 1;
    public static final int SETTING = 2;

    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_company);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_company);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_profile_company);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_company);
        navigationView.setNavigationItemSelectedListener(this);

        transaction = getSupportFragmentManager().beginTransaction();
        int position = getIntent().getIntExtra("fragment", NONE);

        if (position == OFFERS_LIST) {
            View drawerHeader = navigationView.getHeaderView(0);
            TextView drawerName = (TextView) drawerHeader.findViewById(R.id.drawer_name);
            TextView drawerEmail = (TextView) drawerHeader.findViewById(R.id.drawer_email);
            ImageView drawerImage = (ImageView) drawerHeader.findViewById(R.id.drawer_photo);

            SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);
            String email = sp.getString("email", "");

            drawerName.setText(sp.getString("fullname", ""));
            drawerEmail.setText(email);
            drawerImage.setImageBitmap(PictureSetting.loadPicture(this, email, R.drawable.buddyicon));
        }

        loadFragment(position);
    }

    private void loadFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case PROFILE:
                setTitle(R.string.title_activity_profile);
                fragment = new ProfileCompanyFragment();
                transaction.replace(R.id.company_layout_base, fragment).commit();
                break;
            case OFFERS_LIST:
                setTitle(R.string.title_activity_my_offers);
                fragment = new OffersListFragment();
                transaction.replace(R.id.company_layout_base, fragment).commit();
                break;
            case SETTING:
                setTitle(R.string.title_activity_setting);
                fragment = new SettingCompanyFragment();
                transaction.replace(R.id.company_layout_base, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_profile_company);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.nav_profile_company) {
            setTitle(R.string.title_activity_profile);
            fragment = new ProfileCompanyFragment();
        } else if (id == R.id.nav_my_offers) {
            setTitle(R.string.title_activity_my_offers);
            fragment = new OffersListFragment();
        } else if (id == R.id.nav_setting_company) {
            setTitle(R.string.title_activity_setting);
            fragment = new SettingCompanyFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.company_layout_base, fragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_profile_company);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
