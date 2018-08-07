package com.tengbo.commonlibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tengbo.commonlibrary.mvp.BasePresenter;
import com.tengbo.commonlibrary.mvp.IPresenter;
import com.tengbo.commonlibrary.mvp.IView;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseMvpActivity<P extends IPresenter> extends AppCompatActivity implements IView {

    protected P mPresent;
    private Unbinder mUnbinder;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);

        if (getIntent() != null) {
            onIntent(getIntent());
        }

        initView();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void onIntent(Intent intent) {
    }

    protected abstract void initPresent();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresent.unBindView();
    }
}
