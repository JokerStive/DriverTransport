package com.tengbo.commonlibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tengbo.basiclibrary.widget.RxProgressDialog;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity  {

    private WeakReference<BaseActivity> baseActivityWeakReference;
    private RxProgressDialog dialog;
    private Unbinder mUnbinder;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        baseActivityWeakReference = new WeakReference<>(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);

        if(getIntent()!=null){
            onIntent(getIntent());
        }

        initView();

    }

    protected abstract int getLayoutId();
    protected abstract void initView();
    protected  void onIntent(Intent intent){}




    protected void showProgressDialog() {
        if (dialog == null) {
            dialog = new RxProgressDialog(baseActivityWeakReference.get());
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void hideProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        baseActivityWeakReference.clear();
    }
}

