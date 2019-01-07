package com.adyl.locationLibrary;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;


public class SignalLocation {
    private Context context;
    private onLocateListener listener;
    private LocationClient mLocationClient;

    public SignalLocation(Context context) {
        this.context = context;
        initClient();
    }

    private void initClient() {

        //声明LocationClient类
        mLocationClient = new LocationClient(context);

        //注册监听函数
        mLocationClient.registerLocationListener(new MyLocationListener());


        LocationClientOption option = new LocationClientOption();

        //精度模式，高精度、低功耗和仅用设备定位。
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //经纬度类型，分别是GCJ02（国测局坐标）、BD09（百度墨卡托坐标）和BD09ll（百度经纬度坐标）
        option.setCoorType("bd09ll");


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

    public interface onLocateListener {
        void onLocate(BDLocation location);
    }

    public void setOnLocateListener(onLocateListener listener) {
        this.listener = listener;
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mLocationClient.stop();
            if (listener != null) {
                listener.onLocate(bdLocation);
            }
        }
    }

    public void startLocate() {
        mLocationClient.start();
    }

}
