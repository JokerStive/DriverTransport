package com.tengbo.module_main.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.adyl.pushlibrary.MessageManager;
import com.adyl.pushlibrary.PushCallBack;
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
        MessageManager.getInstance().register(getClass().getName(), new PushCallBack() {
            @Override
            public void onMessageReceive(String message) {
                super.onMessageReceive(message);
                if (message.contains("账号登录")) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageManager.getInstance().unRegister(getClass().getName());
//        EventBus.getDefault().unregister(this);
    }


}
