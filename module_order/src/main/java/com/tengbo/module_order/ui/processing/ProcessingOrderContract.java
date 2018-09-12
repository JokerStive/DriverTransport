package com.tengbo.module_order.ui.processing;

import com.tengbo.commonlibrary.mvp.IPresenter;
import com.tengbo.commonlibrary.mvp.IView;
import com.tengbo.module_order.bean.Order;

public interface ProcessingOrderContract {

    interface View extends IView<ProcessingOrderContract.Presenter> {
        void  showOrder(Order order);
        void  passActionSuccess(int position);
    }

    interface Presenter extends IPresenter<ProcessingOrderContract.View> {
        void getOrder(int orderId);

        void passAction(int actionId,int position);

    }

}
