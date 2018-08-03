package com.tengbo.module_main.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.ArrayList;

public class HomePageAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<Fragment> fragments;
    private final FragmentManager fm;
    private String[] tabTitles;
    private static final int FRAGMENT_COUNT = 4;

    public HomePageAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] tabTitles) {
        super(fm);
        this.fragments = fragments;
        this.fm = fm;
        this.tabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];

    }
}
