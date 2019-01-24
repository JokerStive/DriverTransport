package com.tengbo.module_order.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.DeviceUtils;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.GPSLocation;
import com.tengbo.module_order.bean.GPSPoints;
import com.tengbo.module_order.net.ApiOrder;
import com.tengbo.module_order.ui.processing.ProcessingOrderFragment;
import com.tengbo.module_order.utils.DateUtils;

import org.litepal.LitePal;

import java.util.List;

import rx.subscriptions.CompositeSubscription;


/**
 * 定位服务
 *
 * @author yk_de
 * @Description
 */
public class LocateService extends Service {


    private static final Handler MAIN_THREAD_HANDLER =
            new Handler(Looper.getMainLooper(), new LocateService.MainThreadCallback());

    private static final int START = 1;

    private static final int STOP = 0;

    private static final int MAX_DATA_AMOUNT = 10;

    /**
     * 自动位置变化检查的时间间隔
     */
    private static final int DELAY_TIME_MILLION = 1 * 60 * 1000;

    private static final int NOTIFICATION_ID = 10086;

    private ApiOrder mApiOrder;


    /**
     * 位置变化回调的最小距离（m）
     */
    private static final int MIN_DISTANCE = 5;

    private LocateBinder mBinder;

    public LocationClient mLocationClient = null;

    private MyLocationListener myListener = new MyLocationListener();

    private OnLocationChangeCallback onLocationChangeCallback;

    private String mOrderCode;

    private String mPlateNumber;

    protected CompositeSubscription mSubscriptionManager = new CompositeSubscription();


    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new LocateBinder();

        initBaiDuLocation();
    }

    private void initBaiDuLocation() {

        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());

        //注册监听函数
        mLocationClient.registerLocationListener(myListener);


        LocationClientOption option = new LocationClientOption();

        //精度模式，高精度、低功耗和仅用设备定位。
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //经纬度类型，分别是GCJ02（国测局坐标）、BD09（百度墨卡托坐标）和BD09ll（百度经纬度坐标）
        option.setCoorType("bd09ll");

        //发起定位请求的间隔
//        option.setScanSpan(DELAY_TIME_MILLION);

        //自动回调模式，最小时间，最小距离，敏感度
        option.setOpenAutoNotifyMode(DELAY_TIME_MILLION, MIN_DISTANCE, 1);

        //是否需要地址信息
        option.setIsNeedAddress(true);

        //是否打开GPS,高精度和设备定位时必须为true
        option.setOpenGps(true);

        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setLocationNotify(false);

        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false);

        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(false);


        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setEnableSimulateGps(false);

        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            int responseCode = bdLocation.getLocType();
            LogUtil.d("定位返回code--" + responseCode);
            GPSLocation location = new GPSLocation();
            location.setLongitude(bdLocation.getLongitude());
            location.setLatitude(bdLocation.getLatitude());
            location.setUpload(false);
            location.setAddress(bdLocation.getAddrStr());
            location.setReceiveTime(DateUtils.String2Long(bdLocation.getTime()));
            location.setProvince(bdLocation.getProvince());
            location.setCity(bdLocation.getCity());
            location.setCounty(bdLocation.getDistrict());
            location.setOrderCode(mOrderCode);
            location.setPlateNumber(mPlateNumber);
            location.setDeviceCode(DeviceUtils.getAndroidID());
            location.setReceiveTime(bdLocation.getTime());
            location.save();


            if (onLocationChangeCallback != null) {
                onLocationChangeCallback.onLocationChange(bdLocation);
            }
        }
    }


    private void startForegroundNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());

        builder.setContentTitle("司机运输")
                .setContentText("正在进行后台定位")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis());
        Notification notification = builder.build();
        assert manager != null;
        startForeground(NOTIFICATION_ID, notification);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForegroundNotificationO() {
        String notificationChannelId = "com.adyl.app";
        String channelName = "Locate Foreground Service";
        NotificationChannel chan = new NotificationChannel(notificationChannelId, channelName, NotificationManager.IMPORTANCE_NONE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, notificationChannelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("司机运输")
                .setContentText("正在进行后台定位")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    /**
     * 连接的binder
     */
    public class LocateBinder extends Binder {
        public LocateService getService() {
            return LocateService.this;
        }
    }


    /**
     * 设置回调接口
     *
     * @param callback 接口
     */
    public void setOnLocationChangeCallback(OnLocationChangeCallback callback) {
        this.onLocationChangeCallback = callback;
    }


    /**
     * 位置发生变化的回调接口
     */
    public interface OnLocationChangeCallback {
        void onLocationChange(BDLocation bdLocation);
    }


    private static class MainThreadCallback implements Handler.Callback {

        MainThreadCallback() {

        }

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case START:
                    ((LocateService) message.obj).uploadGPSPoints();
                    ((LocateService) message.obj).startUpload(DELAY_TIME_MILLION);
                    break;
                case STOP:
                    MAIN_THREAD_HANDLER.removeMessages(START);
                    break;
                default:
            }
            return true;
        }
    }


    /**
     * 开始检测数据库，是否有轨迹点需要上传
     *
     * @param delayTime 延时时间
     */
    private void startUpload(long delayTime) {
        Message message = Message.obtain();
        message.what = START;
        message.obj = this;
        MAIN_THREAD_HANDLER.sendMessageDelayed(message, delayTime);
    }

    /**
     * 停止检测
     */
    private void stopUpload() {
        Message message = Message.obtain();
        message.what = STOP;
        message.obj = this;
        MAIN_THREAD_HANDLER.sendMessage(message);
    }

    /**
     * 开始上传轨迹点
     */
    private void uploadGPSPoints() {
        List<GPSLocation> locations = LitePal.where("orderCode = ? and upload = ?", mOrderCode, "0").limit(MAX_DATA_AMOUNT).find(GPSLocation.class);
        LogUtil.d("查询到的定位信息个数" + locations.size());
        if (locations.size() > 0) {
            GPSPoints points = new GPSPoints();
            points.setPoints(locations);
            if (mApiOrder == null) {
                mApiOrder = NetHelper.getInstance().getRetrofit().create(ApiOrder.class);
            }
            mSubscriptionManager.add(mApiOrder.addGPSPoints(points)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new ProgressSubscriber<Object>() {
                        @Override
                        protected void on_next(Object o) {
                            updateDb(locations);
                            LogUtil.d("轨迹上传成功--" + points.getPoints().size());
                        }
                    }));

        }
    }

    /**
     * 更新信息
     *
     * @param locations 待更新的定位
     */
    private void updateDb(List<GPSLocation> locations) {
        for (GPSLocation location : locations) {
            location.setUpload(true);
            location.save();
        }
        LitePal.deleteAll(GPSLocation.class, "orderCode = ? and upload = ?", mOrderCode, "1");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d("location service onBind BD startUpload..");
        mOrderCode = intent.getStringExtra(ProcessingOrderFragment.KEY_ORDER_CODE);
        mPlateNumber = intent.getStringExtra(ProcessingOrderFragment.KEY_PLATE_NUMBER);
        mLocationClient.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundNotificationO();
        } else {
            startForegroundNotification();
        }
        startUpload(2000);
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.d("location service onUnBind BD stop..");
        mLocationClient.stop();
        stopUpload();
        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
        LogUtil.d("location service onDestroy BD stop..");

    }
}
