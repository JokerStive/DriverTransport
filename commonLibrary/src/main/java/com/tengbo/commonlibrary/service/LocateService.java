package com.tengbo.commonlibrary.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import retrofit2.http.PUT;

public class LocateService extends Service {

    private static final int MAXIMUM_AUTOMATIC_THREAD_COUNT = 4;
    private static ThreadPoolExecutor mExecutor;
    private static final Handler MAIN_THREAD_HANDLER =
            new Handler(Looper.getMainLooper(), new MainThreadCallback());
    private static int bestThreadCount;
    private static final int DO_JOB = 1;
    private static final int REMOVE_JOB = 0;
    private static final int DELAY_TIME_MILLION = 5000;

    private LocateBinder mBinder;


    public class LocateBinder extends Binder {
        public LocateService getService(){
            return  LocateService.this;
        }

//        public void startJob() {
//            LocateService.this.startJob(0);
//        }
//
//        public void cancelJob() {
//            LocateService.this.cancelJob();
//        }
    }

    private static class MainThreadCallback implements Handler.Callback {

        @SuppressWarnings("WeakerAccess")
        MainThreadCallback() {
        }

        @Override
        public boolean handleMessage(Message message) {
            LocateService locateService = (LocateService) message.obj;
            switch (message.what) {
                case DO_JOB:
                    locateService.doJob();
                    locateService.startJob(DELAY_TIME_MILLION);
                    break;
                case REMOVE_JOB:
                    MAIN_THREAD_HANDLER.removeMessages(DO_JOB);
                    break;
            }
            return true;
        }
    }


    private void doJob() {
        mExecutor.execute(new LocateAndPushTask());
    }


    private void startJob(int delayTime) {
        Message message = Message.obtain();
        message.obj = this;
        message.what = DO_JOB;
        MAIN_THREAD_HANDLER.sendMessageDelayed(message, delayTime);
    }

    private void cancelJob() {
        Message message = Message.obtain();
        message.obj = this;
        message.what = REMOVE_JOB;
        MAIN_THREAD_HANDLER.sendMessage(message);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExecutor = new ThreadPoolExecutor(
                calculateBestThreadCount(),
                calculateBestThreadCount(),
                0,
                TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<Runnable>()
        );

        mBinder = new LocateBinder();
    }

    public static int calculateBestThreadCount() {
        if (bestThreadCount == 0) {
            bestThreadCount =
                    Math.min(MAXIMUM_AUTOMATIC_THREAD_COUNT, RuntimeCompat.availableProcessors());
        }
        return bestThreadCount;
    }


    private static class LocateAndPushTask implements Runnable {
        @Override
        public void run() {
            try {
                Log.d("yk", Thread.currentThread().getName() + "locating");
                Thread.sleep(10000);
                Log.d("yk", Thread.currentThread().getName() + "located");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startJob(0);
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelJob();
    }
}
