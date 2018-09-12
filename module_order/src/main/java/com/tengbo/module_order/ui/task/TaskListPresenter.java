package com.tengbo.module_order.ui.task;

import com.tengbo.commonlibrary.mvp.BasePresenter;
import com.tengbo.module_order.bean.Order;

import java.util.ArrayList;
import java.util.List;

public class TaskListPresenter extends BasePresenter<TaskContract.View> implements TaskContract.Presenter {
    @Override
    public void getTasks() {
        List<Order> tasks = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Order order = new Order();
            order.setOrderNum(i + "");
            order.setOrderStatus(i % 2 == 0 ? "未接单" : "已接单");
            order.setMethod(i % 2 == 0 ? "直达" : "不是直达");
            order.setDepature("重庆");
            order.setDestination("杭州");
            order.setLatestStartTime("18/5/6 9:30");
            order.setScheduleStartTime("18/5/6 9:30");
            order.setScheduleArriveTime("18/5/6 9:30");
            tasks.add(order);
            mView.showTasks(tasks);
        }
    }

    @Override
    public void changeOrderStatus(int status) {

    }
}
