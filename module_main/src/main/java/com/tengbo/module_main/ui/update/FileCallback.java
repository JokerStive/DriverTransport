package com.tengbo.module_main.ui.update;

import android.os.Looper;

import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.net.RxUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * @author yk
 * @Description 文件下载的回调
 */
public abstract class FileCallback implements Callback<ResponseBody> {
    /**
     * 订阅下载进度
     */
    private CompositeSubscription rxSubscriptions = new CompositeSubscription();
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;

    FileCallback(String destFileName) {
        this.destFileName = destFileName;
        subscribeLoadProgress();// 订阅下载进度
    }

    /**
     * 成功后回调
     */
    public abstract void onSuccess(File file);

    /**
     * 下载过程回调
     */
    public abstract void onLoading(long progress, long total);

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            saveFile(response);
        } catch (Exception e) {
            LogUtil.e("error--", e);
            e.printStackTrace();
        }
    }

    private void saveFile(Response<ResponseBody> response) throws Exception {
        LogUtil.d(Looper.getMainLooper() == Looper.myLooper() ? "是主线程" : "不是主线程");
        InputStream in = null;
        FileOutputStream out = null;
        byte[] buf = new byte[2048 * 10];
        int len;
        try {
            File dir = BaseApplication.get().getExternalFilesDir("upload_apk");
            in = response.body().byteStream();
            File file = new File(dir, destFileName);
            out = new FileOutputStream(file);
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            // 回调成功的接口
            onSuccess(file);
            unSubscribe();// 取消订阅
        } finally {
            if (in != null) {
                {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        }

    }

    /**
     * 订阅文件下载进度
     */

    private void subscribeLoadProgress() {
        rxSubscriptions.add(RxBus.getDefault()
                .toObservable(DownloadInfo.class)
                .onBackpressureBuffer()
                .sample(100, TimeUnit.MILLISECONDS)
                .compose(RxUtils.applySchedule())
                .subscribe(new Action1<DownloadInfo>() {
                    @Override
                    public void call(DownloadInfo fileLoadEvent) {
                        onLoading(fileLoadEvent.getProgress(), fileLoadEvent.getTotal());
                    }
                }));
    }

    /**
     * 取消订阅，防止内存泄漏
     */
    private void unSubscribe() {
        if (!rxSubscriptions.isUnsubscribed()) {
            rxSubscriptions.unsubscribe();
        }
    }
}
