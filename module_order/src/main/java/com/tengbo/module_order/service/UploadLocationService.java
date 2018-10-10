package com.tengbo.module_order.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.commonBean.Location;

import org.litepal.LitePal;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UploadLocationService extends Service {

    private static final Handler MAIN_THREAD_HANDLER =
            new Handler(Looper.getMainLooper(), new MainThreadCallback());
    private static final int START = 1;
    private static final int STOP = 0;
    private static final int MAX_DATA_AMOUNT = 10;
    private static final int DELAY_TIME_MILLION = 30000;
    private ExecutorService mSignalExecutor;
    private int processTaskCount = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        mSignalExecutor = Executors.newSingleThreadExecutor();

    }


    private static class MainThreadCallback implements Handler.Callback {

        @SuppressWarnings("WeakerAccess")
        MainThreadCallback() {

        }

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case START:
                    ((UploadLocationService) message.obj).processTask();
                    ((UploadLocationService) message.obj).start();
                    break;
                case STOP:
                    MAIN_THREAD_HANDLER.removeMessages(START);
                    break;
            }
            return true;
        }
    }


    class Task implements Runnable {
        @Override
        public void run() {
            try {
                List<Location> locations = LitePal.where("upload = ?", "0").limit(MAX_DATA_AMOUNT).find(Location.class);
                LogUtil.d("查询到的定位信息个数" + locations.size());
                if (locations != null && locations.size() > 0) {
                    Thread.sleep(3000);
                    LogUtil.d("从数据库取出位置信息..上传位置信息..上传位置信息成功，刷新数据库,已经上传了" + processTaskCount);
                    processTaskCount++;
                    updateDb(locations);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 更新信息
     *
     * @param locations 待更新的定位
     */
    private void updateDb(List<Location> locations) {
        for (Location location : locations) {
            location.setUpload(true);
            location.save();
        }
    }

    private void processTask() {
        mSignalExecutor.execute(new Task());
    }

    private void start() {
        Message message = Message.obtain();
        message.what = START;
        message.obj = this;
        MAIN_THREAD_HANDLER.sendMessageDelayed(message, DELAY_TIME_MILLION);
    }

    private void stop() {
        Message message = Message.obtain();
        message.what = STOP;
        message.obj = this;
        MAIN_THREAD_HANDLER.sendMessage(message);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("上传服务已启动");
        start();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
