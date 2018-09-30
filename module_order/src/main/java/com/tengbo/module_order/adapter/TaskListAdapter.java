package com.tengbo.module_order.adapter;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.basiclibrary.utils.SelectorFactory;
import com.tengbo.basiclibrary.utils.UiUtils;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Order;

import java.util.List;

public class TaskListAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {


    StateListDrawable grayShape = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(Color.parseColor("#12000000"))
            .setCornerRadius(UiUtils.dp2px(BaseApplication.get(), 5))
            .create();

    public TaskListAdapter(@Nullable List<Order> data) {
        super(R.layout.item_task, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, Order order) {
        helper.setText(R.id.tv_departure, order.getDepature())
                .setText(R.id.tv_destination, order.getDestination())
                .setText(R.id.tv_order_id, "订单编号：" + order.getOrderNum())
                .setText(R.id.tv_schedule_time, "计划装货：" + order.getScheduleStartTime())
                .setText(R.id.tv_latest_time, "最晚发货：" + order.getLatestStartTime())
                .setText(R.id.tv_schedule_arrive_time, "计划到达：" + order.getScheduleArriveTime())
                .setText(R.id.tv_method, order.getMethod())
                .addOnClickListener(R.id.btn_accept_task)
                .addOnClickListener(R.id.btn_reject_task)
                .addOnClickListener(R.id.btn_start_task);

        String orderStatus = order.getOrderStatus();
        helper.setImageResource(R.id.iv_order_status, TextUtils.equals(orderStatus, "已接单") ? R.drawable.accept_order : R.drawable.not_accept_order);
        helper.getView(R.id.btn_accept_task).setVisibility(TextUtils.equals(orderStatus, "已接单") ? View.GONE : View.VISIBLE);
        helper.getView(R.id.btn_reject_task).setVisibility(TextUtils.equals(orderStatus, "已接单") ? View.GONE : View.VISIBLE);
        helper.getView(R.id.btn_start_task).setVisibility(!TextUtils.equals(orderStatus, "已接单") ? View.GONE : View.VISIBLE);

        TextView tvCarId = helper.getView(R.id.tv_car_id);
        tvCarId.setText("渝BYK984");
        tvCarId.setBackground(grayShape);


    }
}
