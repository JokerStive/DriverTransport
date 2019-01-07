package com.tengbo.module_order.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_order.bean.GPSLocation;
import com.tengbo.module_order.bean.GPSPoints;
import com.tengbo.module_order.net.ApiOrder;
import com.tengbo.module_order.ui.processing.ProcessingOrderFragment;

import org.litepal.LitePal;

import java.util.List;

import rx.subscriptions.CompositeSubscription;


public class UploadLocationService extends Service {

    private static final Handler MAIN_THREAD_HANDLER =
            new Handler(Looper.getMainLooper(), new MainThreadCallback());
    private static final int START = 1;
    private static final int STOP = 0;
    private static final int MAX_DATA_AMOUNT = 10;
    private static final int DELAY_TIME_MILLION = 5 * 60 * 1000;
    private ApiOrder mApiOrder;
    private UploadLocateBinder mBinder;

    protected CompositeSubscription mSubscriptionManager = new CompositeSubscription();
    private String mOrderCode;

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new UploadLocateBinder();

    }


    private static class MainThreadCallback implements Handler.Callback {

        @SuppressWarnings("WeakerAccess")
        MainThreadCallback() {

        }

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case START:
                    ((UploadLocationService) message.obj).uploadGPSPoints();
                    ((UploadLocationService) message.obj).start(DELAY_TIME_MILLION);
                    break;
                case STOP:
                    MAIN_THREAD_HANDLER.removeMessages(START);
                    break;
            }
            return true;
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


    private void start(long delayTime) {
        Message message = Message.obtain();
        message.what = START;
        message.obj = this;
        MAIN_THREAD_HANDLER.sendMessageDelayed(message, delayTime);
    }

    private void stop() {
        Message message = Message.obtain();
        message.what = STOP;
        message.obj = this;
        MAIN_THREAD_HANDLER.sendMessage(message);
    }


    public class UploadLocateBinder extends Binder {
        public UploadLocationService getService() {
            return UploadLocationService.this;
        }
    }

    public void setOrderCode(String orderCode) {
        this.mOrderCode = orderCode;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("upload location  service onStartCommand upload start..");
        mOrderCode = intent.getStringExtra(ProcessingOrderFragment.KEY_ORDER_CODE);
        start(0);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("upload location  service onDestroy upload stop..");
        if (mSubscriptionManager.hasSubscriptions() && !mSubscriptionManager.isUnsubscribed()) {
            mSubscriptionManager.unsubscribe();
        }
        stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mOrderCode = intent.getStringExtra("orderCode");
        LogUtil.d("upload location  service onBind upload start..");
        start(0);
        return mBinder;
    }
}
