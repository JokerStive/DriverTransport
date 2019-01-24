package com.tengbo.commonlibrary.net;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.support.v4.app.Fragment;

import com.tamic.novate.util.NetworkUtil;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.basiclibrary.widget.progress_dialog.ProgressDialog;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.mvp.IView;

import java.lang.ref.WeakReference;

import rx.Subscriber;
import utils.ToastUtils;

public abstract class ProgressSubscriber<T> extends Subscriber<T> implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {

    private WeakReference<Activity> activityWeakReference;
    private boolean needProgressBar;
    private ProgressDialog dialog;

    public ProgressSubscriber() {

    }

    public ProgressSubscriber(Context context) {
        Activity activity = null;
        if (context instanceof Activity) {
            activity = ((Activity) context);
        } else {
            ToastUtils.show(BaseApplication.get(), "需要加载框传入activity");
        }

        if (activity != null) {
            activityWeakReference = new WeakReference<>(activity);
            needProgressBar = true;
        }
    }

    public ProgressSubscriber(IView iView) {
        Activity activity = null;
        if (iView instanceof Fragment) {
            activity = ((Fragment) iView).getActivity();
        } else if (iView instanceof Activity) {
            activity = ((Activity) iView);
        } else {
            ToastUtils.show(BaseApplication.get(), "需要加载框传入activity或者fragment");
        }
        if (activity != null) {
            activityWeakReference = new WeakReference<>(activity);
            needProgressBar = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (needProgressBar) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                showDialog();
            } else {
                throw new RuntimeException("this is  not UI thread ");
            }
        }

    }


    @Override
    public void onCompleted() {
        dismissDialog();
    }

    @Override
    public void onNext(T t) {
        dismissDialog();
        on_next(t);
    }

    protected abstract void on_next(T t);

    protected void on_error(ApiException e) {
    }


    private void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(activityWeakReference.get());
        }
        dialog.setOnCancelListener(this);
        dialog.setOnDismissListener(this);
        dialog.show();
    }

    @Override
    public void onError(java.lang.Throwable e) {
        dismissDialog();
        if (e instanceof ApiException) {
            dealApiError(((ApiException) e));
        } else if (!NetworkUtil.isNetworkAvailable(BaseApplication.get())) {
            dealApiError(new ApiException(40000, "无网络连接请检查"));
        } else {
            LogUtil.d(e.getMessage());
        }
    }

    private void dealApiError(ApiException apiException) {
        LogUtil.d(apiException.getErrorCode() + "----" + apiException.getErrorMessage());
        ToastUtils.show(BaseApplication.get(), apiException.getErrorMessage());
        on_error(apiException);
    }

    private void dismissDialog() {
        if (needProgressBar && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        clear();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        clear();
    }

    private void clear() {
        if(activityWeakReference!=null){
            activityWeakReference.clear();
            activityWeakReference=null;
        }
        if (!isUnsubscribed()) {
            unsubscribe();
        }
    }
}
