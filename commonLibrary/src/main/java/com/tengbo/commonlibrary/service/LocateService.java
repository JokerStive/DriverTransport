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

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

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
    private static final int DELAY_TIME_MILLION = 1000;
    private LocateBinder mBinder;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();


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

        initBaiduLocation();
    }

    private void initBaiduLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        option.setCoorType("bd09ll");

        option.setScanSpan(DELAY_TIME_MILLION);

        option.setOpenGps(true);

        option.setLocationNotify(true);

        option.setIgnoreKillProcess(false);

        option.SetIgnoreCacheException(false);

        option.setWifiCacheTimeOut(5 * 60 * 1000);

        option.setEnableSimulateGps(false);

        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            int responseCode = location.getLocType();
            Log.d("yk","获取到定位"+responseCode);
            if(responseCode==61){
                double latitude = location.getLatitude();    //获取纬度信息
                double longitude = location.getLongitude();    //获取经度信息
                mExecutor.execute(new LocateAndPushTask(latitude, longitude));

                Log.d("yk","关闭定位功能");
//                mLocationClient.stop();
            }

        }
    }


    public class LocateBinder extends Binder {
        public LocateService getService() {
            return LocateService.this;
        }
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
        Log.d("yk","开启定位功能");
        mLocationClient.start();
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


    public static int calculateBestThreadCount() {
        if (bestThreadCount == 0) {
            bestThreadCount =
                    Math.min(MAXIMUM_AUTOMATIC_THREAD_COUNT, RuntimeCompat.availableProcessors());
        }
        return bestThreadCount;
    }


    private class LocateAndPushTask implements Runnable {
        private final double longitude;
        private final double latitude;

        LocateAndPushTask(double latitude, double longitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }

        @Override
        public void run() {
            try {
                Log.d("yk", "精度" + latitude + "---" + "维度" + longitude + "---upload");
                Thread.sleep(2000);
                Log.d("yk", "精度" + latitude + "---" + "维度" + longitude + "---success");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        startJob(0);
        doJob();
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
        mLocationClient.stop();
    }
}
