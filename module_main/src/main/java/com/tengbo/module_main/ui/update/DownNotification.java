package com.tengbo.module_main.ui.update;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.tengbo.module_main.R;


/**
 * @author yk
 * @Description 后台下载时通知栏显示
 */
public class DownNotification {
    private NotificationCompat.Builder mNotifyBuilder;
    private Context mContext;
    private NotificationManager manager;
    private int notificationId = 100;

    DownNotification(Context context) {
        this.mContext = context;
        mNotifyBuilder = new NotificationCompat.Builder(mContext, "")
                .setContentTitle("文件下载中...")
                .setContentText("下载进度0%")
                .setSmallIcon(R.drawable.icon_password);
        manager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }


    public void cancel() {
        manager.cancel(notificationId);
    }


    public void updateProgress(int progress, int max) {
        int result = progress / max * 100;
        mNotifyBuilder.setContentText("下载进度" + result + "%");
        manager.notify(notificationId, mNotifyBuilder.build());
    }

}
