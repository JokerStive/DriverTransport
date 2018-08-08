package com.tengbo.module_main.ui.home;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.orhanobut.logger.Logger;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.common.ComponentConfig;
import com.tengbo.module_main.R;
import com.tengbo.module_main.adapter.HomePageAdapter;
import com.tengbo.module_main.ui.login.HistoryOrderFragment;
import com.tengbo.module_main.widget.NoScrollViewPager;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private NoScrollViewPager mViewPager;
    private Fragment fragment;
    private TabLayout mTabLayout;
    private String[] mTabTitles = new String[]{"任务", "历史订单", "", "消息", "我的"};
    private int[] mTabImages = new int[]{R.drawable.selector_tab_task, R.drawable.selector_tab_history,
            R.drawable.selector_tab_info, R.drawable.selector_tab_personal};


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tabLayout);
        View mIvProcessing = findViewById(R.id.iv_processing);
        mIvProcessing.setOnClickListener(view -> mViewPager.setCurrentItem(2, true));

        ArrayList<Fragment> fragments = getFragmentByComponent();
        if (fragments.size() == 0)
            return;
        HomePageAdapter homePageAdapter = new HomePageAdapter(getSupportFragmentManager(), fragments, mTabTitles);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(homePageAdapter);
        mViewPager.setCurrentItem(2);

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
                if (i == 2) {
                    continue;
                }
                ImageView imageView = itemTab.getCustomView().findViewById(R.id.iv_img);
                imageView.setImageResource(i > 2 ? mTabImages[i - 1] : mTabImages[i]);
            }

        }
        TabLayout.Tab tabAt0 = mTabLayout.getTabAt(0);
        assert tabAt0 != null;
        assert tabAt0.getCustomView() != null;
        tabAt0.getCustomView().setSelected(true);
    }

    private ArrayList<Fragment> getFragmentByComponent() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        HistoryOrderFragment target1 = HistoryOrderFragment.newInstance();
        HistoryOrderFragment target2 = HistoryOrderFragment.newInstance();
        HistoryOrderFragment target3 = HistoryOrderFragment.newInstance();
        HistoryOrderFragment target4 = HistoryOrderFragment.newInstance();
        HistoryOrderFragment target5 = HistoryOrderFragment.newInstance();
        fragments.add(target1);
        fragments.add(target2);
        fragments.add(target3);
        fragments.add(target4);
        fragments.add(target5);

//        CCResult ccResult = CC.obtainBuilder(ComponentConfig.OrderComponentConfig.COMPONENT_NAME)
//                .setActionName(ComponentConfig.OrderComponentConfig.ACTION_GET_HISTORY_FRAGMENT)
//                .addParam("fragment", fragment)
//                .build()
//                .call();
//        boolean success = ccResult.isSuccess();
//        Logger.d(ccResult.getCode());
//        if (success) {
//            Fragment fragment1 = ccResult.getDataItem(ComponentConfig.OrderComponentConfig.ACTION_GET_HISTORY_FRAGMENT);
//            HistoryOrderFragment fragment2 = HistoryOrderFragment.newInstance();
//            HistoryOrderFragment fragment3 = HistoryOrderFragment.newInstance();
//            HistoryOrderFragment fragment4 = HistoryOrderFragment.newInstance();
//            HistoryOrderFragment fragment5 = HistoryOrderFragment.newInstance();
//            fragments.add(fragment1);
//            fragments.add(fragment2);
//            fragments.add(fragment3);
//            fragments.add(fragment4);
//            fragments.add(fragment5);
//        }

        return fragments;
    }


}
