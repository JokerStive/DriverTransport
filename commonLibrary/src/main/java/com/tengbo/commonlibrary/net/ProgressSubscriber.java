package com.tengbo.commonlibrary.net;

import android.app.Activity;
import android.os.Looper;

import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.tengbo.basiclibrary.widget.RxProgressDialog;

import java.lang.ref.WeakReference;

public abstract class ProgressSubscriber<T> extends BaseSubscriber<T> {

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
            if (Looper.getMainLooper()==Looper.myLooper()) {
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
        hideDialog();
    }

    @Override
    public void onNext(T t) {
        hideDialog();
        on_next(t);
    }

    protected  abstract  void on_next(T t);
    protected  abstract  void on_error(Throwable e);

    @Override
    public void onError(Throwable e) {
       hideDialog();
       on_error(e);
    }


    private void hideDialog() {
        if (needProgressBar) {
            if (activity != null && dialog.isShowing()) {
                dialog.cancel();
            }
        }
    }


}
