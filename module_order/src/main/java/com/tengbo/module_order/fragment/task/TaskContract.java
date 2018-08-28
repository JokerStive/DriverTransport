package com.tengbo.module_order.fragment.task;

import com.tengbo.commonlibrary.mvp.IPresenter;
import com.tengbo.commonlibrary.mvp.IView;
import com.tengbo.module_order.bean.Order;

import java.util.List;

public interface TaskContract {
    interface View extends IView<Presenter> {
        void showTasks(List<Order> tasks);

        void changeStatusSuccess(int status);
    }

    interface Presenter extends IPresenter<View> {

        void getTasks();

        void changeOrderStatus(int status);
    }

}
