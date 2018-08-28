package com.tengbo.module_order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tengbo.commonlibrary.base.BaseActivity;

public class OrderMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findFragment(OrderRootFragment.class) == null) {
            loadRootFragment(R.id.container, OrderRootFragment.newInstance());  // 加载根Fragment
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }


}
