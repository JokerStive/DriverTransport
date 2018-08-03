package com.tengbo.module_main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.module_main.adapter.HomePageAdapter;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] tabTitles = new String[]{"任务", "", "", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        ArrayList<Fragment> fragments = getFragmentByComponent();
        HomePageAdapter homePageAdapter = new HomePageAdapter(getSupportFragmentManager(), fragments, tabTitles);
        viewPager.setAdapter(homePageAdapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPager);


    }

    private ArrayList<Fragment> getFragmentByComponent() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        return fragments;
    }


}
