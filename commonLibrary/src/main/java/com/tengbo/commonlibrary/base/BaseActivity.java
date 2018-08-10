package com.tengbo.commonlibrary.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.tengbo.basiclibrary.widget.RxProgressDialog;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends AppCompatActivity {

    private WeakReference<BaseActivity> baseActivityWeakReference;
    protected CompositeSubscription mSubscriptionManager = new CompositeSubscription();
    private RxProgressDialog dialog;
    private Unbinder mUnbinder;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);
        baseActivityWeakReference = new WeakReference<>(this);
        setStatusBarColor();

        if (getIntent() != null) {
            onIntent(getIntent());
        }
        initView();

    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void onIntent(Intent intent) {
    }


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
        if (mSubscriptionManager.hasSubscriptions() && !mSubscriptionManager.isUnsubscribed()) {
            mSubscriptionManager.unsubscribe();
        }
    }




    /**
     * 修改状态栏颜色
     */
    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}

