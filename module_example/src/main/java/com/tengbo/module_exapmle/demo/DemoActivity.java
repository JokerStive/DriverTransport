package com.tengbo.module_exapmle.demo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.tengbo.commonlibrary.base.BaseMvpActivity;
import com.tengbo.commonlibrary.service.LocateService;
import com.tengbo.module_exapmle.R;


public class DemoActivity extends BaseMvpActivity<DemoContract.Presenter> implements DemoContract.View {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exapmle1);
    }


    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    public void startService(View v) {
        startService(new Intent(this, LocateService.class));
    }

    public void stopService(View v) {
        stopService(new Intent(this, LocateService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initPresent() {
        mPresent = new DemoPresenter();
        mPresent.bindView(this);
    }
}
