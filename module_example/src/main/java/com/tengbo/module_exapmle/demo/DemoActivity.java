package com.tengbo.module_exapmle.demo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.tengbo.commonlibrary.base.BaseMvpActivity;
import com.tengbo.commonlibrary.service.LocateService;
import com.tengbo.drivertransport.R;


public class DemoActivity extends BaseMvpActivity<DemoContract.Presenter> implements DemoContract.View{
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

    public void startService(View v){
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                LocateService.LocateBinder locateBinder =  (LocateService.LocateBinder)iBinder;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        bindService(new Intent(this,LocateService.class),connection, Context.BIND_ALLOW_OOM_MANAGEMENT);
    }

    @Override
    protected void initPresent() {
        mPresent = new DemoPresenter();
        mPresent.bindView(this);
    }
}
