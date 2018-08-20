package com.tengbo.module_main.ui.update;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;
import com.tengbo.commonlibrary.base.BaseApplication;

import java.io.File;

import utils.ToastUtils;

/**
 * @author yk
 * @Description 非强制更新时启动服务，下载apk
 */
public class DownLoadService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        startLoadApk(url);
        return super.onStartCommand(intent, flags, startId);

    }

    private void startLoadApk(String url) {
        DownNotification notification = new DownNotification(BaseApplication.get());
        DownloadManager.getInstance().download(url, new DownLoadCallBack() {
            @Override
            public void onSuccess(File file) {
                AppUtils.installApp(file);
                notification.cancel();
                stopSelf();
            }

            @Override
            public void onProgress(int progress, int total) {
                notification.updateProgress(progress, total);
            }

            @Override
            public void onFail(Throwable t) {
                ToastUtils.show(BaseApplication.get(), "文件下载失败，请稍后重试..");
                notification.cancel();
                stopSelf();
            }
        });
    }


}
