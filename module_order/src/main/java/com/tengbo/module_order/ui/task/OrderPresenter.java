package com.tengbo.module_order.ui.task;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.mvp.BasePresenter;
import com.tengbo.commonlibrary.net.BaseListResponse;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_order.bean.Coast;
import com.tengbo.module_order.bean.Flow;
import com.tengbo.module_order.bean.Goods;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.net.ApiOrder;

import java.util.ArrayList;
import java.util.List;

import utils.RequestUtils;

public class OrderPresenter extends BasePresenter<OrderContract.View> implements OrderContract.Presenter {


    @Override
    public void setOrderStatus(String orderCode, int orderStatus) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderCode", orderCode);
        jsonObject.put("orderStatus", orderStatus);
        NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .setDriverOrderStatus(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new ProgressSubscriber<Order>(mView) {
                    @Override
                    public void on_next(Order order) {
                        mView.setOrderStatusSuccess(orderStatus);
                    }
                });
    }

    @Override
    public void getOrder(String orderCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderCode", orderCode);
        NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .getDriverOrderInfo(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new ProgressSubscriber<Order>(mView) {
                    @Override
                    protected void on_next(Order order) {
                        mView.getOrderSuccess(order);
                    }
                });
    }



    @Override
    public void getOrders(int page) {
        if (page == 1) {
            mView.startProgress();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page", page);
        jsonObject.put("orderStatus", 2);
        jsonObject.put("driverId", User.getIdNumber());
        NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .getDriverOrders(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new ProgressSubscriber<BaseListResponse<Order>>() {
                    @Override
                    public void on_next(BaseListResponse<Order> listResponse) {
                        mView.stopProgress();
                        mView.getOrderSuccess(page, listResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.stopProgress();
                    }
                });
    }



    @Override
    public void getHistoryOrders(int page, String startTime, String endTime) {
        if (page == 1) {
            mView.startProgress();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page", page);
        jsonObject.put("driverId", User.getIdNumber());
        if (!TextUtils.isEmpty(startTime)) {
            jsonObject.put("finishTimeStart", startTime);
        }

        if (!TextUtils.isEmpty(endTime)) {
            jsonObject.put("finishTimeEnd", endTime);
        }
        NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .getDriverHistoryOrders(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new ProgressSubscriber<BaseListResponse<Order>>() {
                    @Override
                    protected void on_next(BaseListResponse<Order> listResponse) {
                        mView.stopProgress();
                        mView.getHistoryOrderSuccess(page, listResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.stopProgress();
                    }
                });

    }



    @Override
    public void getHistoryOrder(String orderCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderCode", orderCode);
        NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .getDriverOrderInfo(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new ProgressSubscriber<Order>(mView) {
                    @Override
                    protected void on_next(Order order) {
                        mView.getOrderSuccess(order);
                    }
                });
    }

    @Override
    public void checkHasProcessingOrder() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("driverId", User.getIdNumber());
        jsonObject.put("orderStatus", 1);
        jsonObject.put("page", 1);
        NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .getDriverOrders(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<BaseListResponse<Order>>(mView) {
                    @Override
                    protected void on_next(BaseListResponse<Order> listResponse) {
                        List<Order> rows = listResponse.getRows();
                       mView.hasProcessingOrder(rows.size()>0);
                    }
                });
    }


}
