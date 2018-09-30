package com.tengbo.module_order.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.commonBean.Location;

import org.litepal.LitePal;


/**
 * 定位服务
 *
 * @Autor yk
 * @Description
 */
public class LocateService extends Service {

    //自动扫描位置的时间间隔
    private static final int minTimeInterval = 15000;
    //位置变化回调的最小距离（m）
    private static final int minDistance = 50;

    private LocateBinder mBinder;

    public LocationClient mLocationClient = null;

    private MyLocationListener myListener = new MyLocationListener();

    private int locateCount;


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
//        option.setScanSpan(minTimeInterval);
        //自动回调模式，最小时间，最小距离，敏感度
        option.setOpenAutoNotifyMode(minTimeInterval, minDistance, 1);

        //是否需要地址信息
        option.setIsNeedAddress(true);

        //是否打开GPS,高精度和设备定位时必须为true
        option.setOpenGps(true);

        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setLocationNotify(true);

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
            locateCount++;
            int responseCode = bdLocation.getLocType();
            double latitude = bdLocation.getLatitude();    //获取纬度信息
            double longitude = bdLocation.getLongitude();    //获取经度信息
            String address = bdLocation.getAddress().address; //地址
            LogUtil.d("code --" + responseCode + "--" + "定位次数" + locateCount + " latitude--" + latitude + "--" + "longitude" + longitude + "---" + "address--" + address);
            Location location = new Location();
            location.setLongitude(longitude);
            location.setLatitude(latitude);
            location.setAddress(address);
            location.setUpload(false);
            location.save();
        }
    }


    public class LocateBinder extends Binder {
        public LocateService getService() {
            return LocateService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("定位服务已启动");
        mLocationClient.start();
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
        mLocationClient.stop();
    }
}
