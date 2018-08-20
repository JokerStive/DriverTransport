package com.tengbo.module_main.ui.home;

import android.support.v4.app.Fragment;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tengbo.commonlibrary.base.BaseMvpActivity;
import com.tengbo.module_main.R;
import com.tengbo.module_main.adapter.HomePageAdapter;
import com.tengbo.module_main.bean.UpdateInfo;
import com.tengbo.module_main.ui.login.HistoryOrderFragment;
import com.tengbo.module_main.ui.update.UpdateDialogFragment;
import com.tengbo.module_main.widget.NoScrollViewPager;

import java.util.ArrayList;

/**
 *
 */
public class MainActivity extends BaseMvpActivity<MainContract.Presenter> implements MainContract.View {

    private NoScrollViewPager mViewPager;
    private String[] mTabTitles = new String[]{"任务", "历史订单", "进行中", "消息", "我的"};
    private LinearLayout llTab;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.viewPager);
        llTab = findViewById(R.id.ll_tab);
        findViewById(R.id.iv_processing).setOnClickListener(v -> mViewPager.setCurrentItem(2));


        ArrayList<Fragment> fragments = getFragmentByComponent();
        if (fragments.size() == 0)
            return;

        initViewPager(fragments);

        initTab();


    }

    @Override
    protected void initPresent() {
        mPresent = new MainPresenter();
        mPresent.bindView(this);
        mPresent.checkUpdate();
    }


    private void initTab() {
        int childCount = llTab.getChildCount();
        llTab.getChildAt(2).setSelected(true);
        for (int i = 0; i < childCount; i++) {
            TextView childAt = (TextView) llTab.getChildAt(i);
            int finalI = i;
            childAt.setOnClickListener(view -> {
                mutuallyExclusiveSelect(finalI);
            });
        }
    }

    /**
     * 互斥select
     *
     * @param selectIndex 当前选中的tab
     */
    private void mutuallyExclusiveSelect(int selectIndex) {
        int childCount = llTab.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView childAt = (TextView) llTab.getChildAt(i);
            childAt.setSelected(selectIndex == i);
        }
        mViewPager.setCurrentItem(selectIndex);
    }


    /**
     * @param fragments 需要显示的fragment
     */
    private void initViewPager(ArrayList<Fragment> fragments) {
        HomePageAdapter homePageAdapter = new HomePageAdapter(getSupportFragmentManager(), fragments, mTabTitles);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(homePageAdapter);
        mViewPager.setCurrentItem(2);
    }


    /**
     * 组件间通讯传递fragment
     *
     * @return 需要显示在首页的fragments
     */
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


    @Override
    public void checkUpdateResult(UpdateInfo updateInfo) {
        if (updateInfo.isNeedUpdate()){
            UpdateDialogFragment.newInstance(updateInfo).show(getFragmentManager(),"");
        }
    }

    @Override
    public void installNewApk(String apkPath) {

    }
}
