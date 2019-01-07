package com.tengbo.module_order.ui.task;

import com.tengbo.commonlibrary.mvp.IPresenter;
import com.tengbo.commonlibrary.mvp.IView;
import com.tengbo.commonlibrary.net.BaseListResponse;
import com.tengbo.module_order.bean.Order;

import java.util.List;

public interface OrderContract {
    interface View extends IView<Presenter> {

        void startProgress();

        void stopProgress();

        void setOrderStatusSuccess(int status);

        void getOrderSuccess(Order order);

        void getOrderSuccess(int page, BaseListResponse<Order> listResponse);

        void getHistoryOrderSuccess(int page, BaseListResponse<Order> listResponse);

        void hasProcessingOrder(boolean hasProcessingOrder);

    }



    interface Presenter extends IPresenter<View> {

        void setOrderStatus(String orderCode,int status);

        void getOrder(String orderCode);

        void getOrders(int page);

        void getHistoryOrders(int page,String startTime,String endTime);

        void getHistoryOrder(String orderCode);

        void checkHasProcessingOrder();
    }

}
