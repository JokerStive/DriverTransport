package com.tengbo.commonlibrary.net;

import android.app.Activity;
import android.os.Looper;

import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxResultCallback;
import com.tengbo.basiclibrary.widget.RxProgressDialog;

import java.lang.ref.WeakReference;


public abstract class ProgressCallBack<T> extends RxResultCallback<T> {

    private WeakReference<Activity> activity;
    private boolean needProgressBar;
    private RxProgressDialog dialog;

    public ProgressCallBack() {

    }

    public ProgressCallBack(Activity content) {
        if (content != null) {
            this.activity = new WeakReference<>(content);
            needProgressBar = true;
        }
    }


    @Override
    public void onStart(Object tag) {
        super.onStart(tag);
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


    protected abstract void on_next(T t);

    protected void on_error(ApiException e) {
    }

    @Override
    public void onNext(Object tag, int code, String message, T response) {
        hideDialog();
        on_next(response);
    }

    @Override
    public void onError(Object tag, Throwable e) {

    }

    @Override
    public void onCancel(Object tag, Throwable e) {
        hideDialog();
    }

    @Override
    public void onCompleted(Object tag) {
        super.onCompleted(tag);
        hideDialog();
    }


    private void hideDialog() {
        if (needProgressBar) {
            if (activity != null && dialog.isShowing()) {
                dialog.cancel();
            }
        }
    }
}
