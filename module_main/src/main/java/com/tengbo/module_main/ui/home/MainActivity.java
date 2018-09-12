package com.tengbo.module_main.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.common.Event;
import com.tengbo.commonlibrary.net.HttpInterceptor;
import com.tengbo.module_main.R;
import com.tengbo.module_main.ui.login.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        if (findFragment(MainRootFragment.class) == null) {
            loadRootFragment(R.id.rl_container, MainRootFragment.newInstance());  // 加载根Fragment
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }



}
