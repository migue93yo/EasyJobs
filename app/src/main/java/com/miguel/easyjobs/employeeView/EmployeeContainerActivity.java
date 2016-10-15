package com.miguel.easyjobs.employeeView;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.IOException;

public class EmployeeContainerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int NONE = -1;
    public static final int PROFILE = 0;
    public static final int SEARCH_OFFERS = 1;
    public static final int CANDIDATURES = 2;
    public static final int SETTING = 3;

    private DrawerLayout drawer;
    private TextView drawerName;
    private TextView drawerEmail;
    private ImageView drawerImage;
    private View drawerHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_employee);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_employee);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_employee);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_employee);
        navigationView.setNavigationItemSelectedListener(this);
        drawerHeader = navigationView.getHeaderView(0);

        int num = getIntent().getIntExtra("fragment", NONE);
        loadFragment(num);
    }

    private void loadFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        switch (position) {
            case PROFILE:
                setTitle(R.string.title_activity_profile);
                fragment = new ProfileEmployeeFragment();
                transaction.add(R.id.employee_container, fragment).commit();
                break;
            case SEARCH_OFFERS:
                setTitle(R.string.title_activity_search_offers);
                fragment = new SearchOffersFragment();
                transaction.add(R.id.employee_container, fragment).commit();
                loadPhoto();
                break;
            case CANDIDATURES:
                setTitle(R.string.title_activity_candidatures);
                fragment = new CandidaturesFragment();
                transaction.add(R.id.employee_container, fragment).commit();
                loadPhoto();
                break;
            case SETTING:
                setTitle(R.string.title_activity_setting);
                fragment = new SettingEmployeeFragment();
                transaction.add(R.id.employee_container, fragment).commit();
                loadPhoto();
        }
    }

    @Override
    public void onBackPressed() {
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;

        if (id == R.id.nav_profile_employee) {
            setTitle(R.string.title_activity_profile);
            fragment = new ProfileEmployeeFragment();
            transaction.replace(R.id.employee_container, fragment).commit();
        } else if (id == R.id.nav_search) {
            setTitle(R.string.title_activity_search_offers);
            fragment = new SearchOffersFragment();
            transaction.replace(R.id.employee_container, fragment).commit();
        } else if (id == R.id.nav_candidatures) {
            setTitle(R.string.title_activity_candidatures);
            fragment = new CandidaturesFragment();
            transaction.replace(R.id.employee_container, fragment).commit();
        } else if (id == R.id.nav_setting_employee) {
            setTitle(R.string.title_activity_setting);
            fragment = new SettingEmployeeFragment();
            transaction.replace(R.id.employee_container, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_employee);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadPhoto() {
        drawerName = (TextView) drawerHeader.findViewById(R.id.drawer_name);
        drawerEmail = (TextView) drawerHeader.findViewById(R.id.drawer_email);
        drawerImage = (ImageView) drawerHeader.findViewById(R.id.drawer_photo);

        SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);
        drawerName.setText(sp.getString("fullname", ""));
        drawerEmail.setText(sp.getString("email", ""));

        ContentResolver cr = getContentResolver();
        File newFile = new File(PictureSetting.RUTE_IMAGE);
        Uri uri = Uri.fromFile(newFile);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
        } catch (IOException e) {
            Drawable drawable = getResources().getDrawable(R.drawable.buddyicon);
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }

        drawerImage.setImageBitmap(bitmap);
    }

}
