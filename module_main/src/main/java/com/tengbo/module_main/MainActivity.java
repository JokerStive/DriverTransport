package com.tengbo.module_main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.orhanobut.logger.Logger;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.common.ComponentConfig;
import com.tengbo.module_main.adapter.HomePageAdapter;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private Fragment fragment;
    private TabLayout mTabLayout;
    private String[] mTabTitles = new String[]{"任务", "历史", "进行中", "我的"};
    private int[] mTabImages = new int[]{R.drawable.selector_tab_history, R.drawable.selector_tab_history, R.drawable.selector_tab_history,
            R.drawable.selector_tab_history};


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tabLayout);

        ArrayList<Fragment> fragments = getFragmentByComponent();
        if (fragments.size()==0)
            return;
        HomePageAdapter homePageAdapter = new HomePageAdapter(getSupportFragmentManager(), fragments, mTabTitles);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(homePageAdapter);

        mTabLayout.setSelectedTabIndicatorHeight(0);
        mTabLayout.setupWithViewPager(mViewPager);

        for (int i = 0; i < mTabTitles.length; i++) {
            //获得到对应位置的Tab
            TabLayout.Tab itemTab = mTabLayout.getTabAt(i);
            if (itemTab != null) {
                itemTab.setCustomView(R.layout.item_tab);
                assert itemTab.getCustomView() != null;
                TextView textView = itemTab.getCustomView().findViewById(R.id.tv_name);
                textView.setText(mTabTitles[i]);
                ImageView imageView = itemTab.getCustomView().findViewById(R.id.iv_img);
                imageView.setImageResource(mTabImages[i]);
            }

        }
        TabLayout.Tab tabAt0 = mTabLayout.getTabAt(0);
        assert tabAt0 != null;
        assert tabAt0.getCustomView() != null;
        tabAt0.getCustomView().setSelected(true);
    }

    private ArrayList<Fragment> getFragmentByComponent() {
        ArrayList<Fragment> fragments = new ArrayList<>();
//        Fragment targetFragment1 = HistoryOrderFragment.newInstance();
//        Fragment targetFragment2 = HistoryOrderFragment.newInstance();
//        Fragment targetFragment3 = HistoryOrderFragment.newInstance();
//        Fragment targetFragment4 = HistoryOrderFragment.newInstance();
//        fragments.add(targetFragment1);
//        fragments.add(targetFragment2);
//        fragments.add(targetFragment3);
//        fragments.add(targetFragment4);
        CCResult ccResult = CC.obtainBuilder(ComponentConfig.OrderComponentConfig.COMPONENT_NAME)
                .setActionName(ComponentConfig.OrderComponentConfig.ACTION_GET_HISTORY_FRAGMENT)
                .addParam("fragment",fragment)
                .build()
                .call();
        boolean success = ccResult.isSuccess();
        Logger.d(ccResult.getCode());
        if (success) {
//            Fragment targetFragment = ccResult.getDataItem(ComponentConfig.OrderComponentConfig.ACTION_GET_HISTORY_FRAGMENT);
//            fragments.add(targetFragment);
//            fragments.add(targetFragment);
//            fragments.add(targetFragment);
//            fragments.add(targetFragment);
        }

        return fragments;
    }


}
