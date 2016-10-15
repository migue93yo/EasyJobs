package com.miguel.easyjobs.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.companyView.HiddenOffersFragment;
import com.miguel.easyjobs.companyView.VisibleOffersFragment;

public class OfferPageViewAdapter extends FragmentStatePagerAdapter {

    private final int NUM_PAGES = 2;
    private String[] TITLE_PAGES;

    public OfferPageViewAdapter(Activity context, FragmentManager fragmentManager) {
        super(fragmentManager);
        TITLE_PAGES = new String[]{
                context.getResources().getString(R.string.active),
                context.getResources().getString(R.string.inactive)};
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new VisibleOffersFragment();
                break;
            case 1:
                fragment = new HiddenOffersFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE_PAGES[position];
    }

}
