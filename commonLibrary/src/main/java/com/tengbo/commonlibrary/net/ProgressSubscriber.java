package com.tengbo.commonlibrary.net;

import android.app.Activity;
import android.os.Looper;
import android.text.TextUtils;

import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.basiclibrary.widget.RxProgressDialog;
import com.tengbo.commonlibrary.base.BaseApplication;

import java.lang.ref.WeakReference;

import rx.Subscriber;
import utils.ToastUtils;

public abstract class ProgressSubscriber<T> extends Subscriber<T> {

    private WeakReference<Activity> activity;
    private boolean needProgressBar;
    private RxProgressDialog dialog;

    public ProgressSubscriber() {

    }

    public ProgressSubscriber(Activity content) {
        if (content != null) {
            this.activity = new WeakReference<>(content);
            needProgressBar = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (needProgressBar) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                if (activity != null) {
                    if (dialog == null) {
                        dialog = new RxProgressDialog(activity.get());
                    }
                    dialog.show();
                }
            } else {
                throw new RuntimeException("this is  not UI thread ");
            }
        }

    }

    @Override
    public void onCompleted() {
//        com.orhanobut.logger.Logger.d("rx onComplete");
        hideDialog();
    }

    @Override
    public void onNext(T t) {
//        com.orhanobut.logger.Logger.d("rx onNext");
        hideDialog();
        on_next(t);
    }

    protected abstract void on_next(T t);

    protected void on_error(ApiException e) {

    }

    @Override
    public void onError(java.lang.Throwable e) {
        hideDialog();
        if(TextUtils.isEmpty(e.getMessage())){
            LogUtil.d(e.getMessage());
        }
        if (e instanceof ApiException) {
            ApiException apiException = ((ApiException) e);
            LogUtil.d(apiException.getErrorCode() + "----" + apiException.getErrorMessage());
            ToastUtils.show(BaseApplication.get(), ((ApiException) e).getErrorMessage());
            on_error((ApiException) e);
        }
    }

    private void hideDialog() {
        if (needProgressBar) {
            if (activity != null && dialog.isShowing()) {
                dialog.cancel();
            }
        }
    }


}
