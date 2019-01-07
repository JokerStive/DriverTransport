package com.tengbo.module_main.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.orhanobut.logger.Logger;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.base.BaseMvpFragment;
import com.tengbo.commonlibrary.common.ComponentConfig;
import com.tengbo.module_main.R;
import com.tengbo.module_main.adapter.HomePageAdapter;
import com.tengbo.module_main.bean.UpdateInfo;
import com.tengbo.commonlibrary.common.Event;
import com.tengbo.module_main.ui.update.UpdateDialogFragment;
import com.tengbo.module_main.widget.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 *
 */
public class MainRootFragment extends BaseMvpFragment<MainContract.Presenter> implements MainContract.View {


    private NoScrollViewPager mViewPager;
    private String[] mTabTitles = new String[]{"任务", "历史订单", "进行中", "消息", "我的"};
    private LinearLayout llTab;
    private Fragment processing;
    private Fragment dutyFragment;
    private ArrayList<Fragment> fragments;
    private HomePageAdapter homePageAdapter;
    private TextView tvProcessing;


    public static MainRootFragment newInstance() {

        return new MainRootFragment();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeTab(Event.ChangeTab event) {
        int position = event.getPosition();
        LogUtil.d("待切换的tab--" + position);
        if (position == -1) {
            showDutyFragment();
        } else {
            llTab.getChildAt(position).performClick();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void getTransferData(Bundle arguments) {

    }

    @Override
    protected void initView() {
        mViewPager = mRootView.findViewById(R.id.viewPager);
        llTab = mRootView.findViewById(R.id.ll_tab);
        tvProcessing = mRootView.findViewById(R.id.tv_processing);


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
//        mPresent.checkUpdate();
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
        homePageAdapter = new HomePageAdapter(_mActivity.getSupportFragmentManager(), fragments, null);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(homePageAdapter);
        mViewPager.setCurrentItem(2);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mutuallyExclusiveSelect(position);
            }
        });
    }


    /**
     * 组件间通讯传递fragment
     *
     * @return 需要显示在首页的fragments
     */
    private ArrayList<Fragment> getFragmentByComponent() {
        fragments = new ArrayList<>();
        CCResult orderResult = CC.obtainBuilder(ComponentConfig.Order.COMPONENT_NAME)
                .setActionName(ComponentConfig.Order.ACTION_GET_HOME_PAGE_FRAGMENTS)
                .build()
                .call();
        boolean success = orderResult.isSuccess();
//        Logger.d("orderCode---" + orderResult.getCode());
        if (success) {
            Fragment history = orderResult.getDataItem(ComponentConfig.Order.ACTION_GET_HISTORY_FRAGMENT);
            Fragment task = orderResult.getDataItem(ComponentConfig.Order.ACTION_GET_TASK_FRAGMENT);
            Fragment info = orderResult.getDataItem(ComponentConfig.Order.ACTION_GET_INFO_FRAGMENT);
            processing = orderResult.getDataItem(ComponentConfig.Order.ACTION_GET_PROCESSING_FRAGMENT);
            dutyFragment = orderResult.getDataItem(ComponentConfig.Order.ACTION_GET_DUTY_FRAGMENT);
            fragments.add(task);
            fragments.add(history);
            fragments.add(processing);
            fragments.add(info);


        }

        CCResult centerResult = CC.obtainBuilder(ComponentConfig.PersonalCenter.COMPONENT_NAME)
                .setActionName(ComponentConfig.PersonalCenter.ACTION_GET_PERSONAL_CENTER_FRAGMENT)
                .build()
                .call();
        Logger.d("centerCode---" + centerResult.getCode());
        if (centerResult.isSuccess()) {
            Fragment personal = centerResult.getDataItem(ComponentConfig.PersonalCenter.ACTION_GET_PERSONAL_CENTER_FRAGMENT);
            fragments.add(personal);
        }

        return fragments;
    }


    public void showDutyFragment() {
        if (homePageAdapter != null
                && fragments != null
                && fragments.size() != 0
                && processing != null
                && dutyFragment != null
                ) {
            LogUtil.d("显示值班界面");
            fragments.remove(processing);
            fragments.add(2, dutyFragment);

//            mTabTitles = new String[]{"任务", "历史订单", "值班", "消息", "我的"};
            tvProcessing.setText("值班");

            homePageAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void checkUpdateResult(UpdateInfo updateInfo) {
        if (updateInfo.isNeedUpdate()) {
            UpdateDialogFragment.newInstance(updateInfo).show(_mActivity.getFragmentManager(), "");
        }
    }

    @Override
    public void installNewApk(String apkPath) {

    }
}
