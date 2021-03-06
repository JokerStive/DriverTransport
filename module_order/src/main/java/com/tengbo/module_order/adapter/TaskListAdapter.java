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

/**
 * @author yk_de
 */
public class TaskListAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {

    int orderStatusHadRefuse = 3;
    int orderStatusHadAccept = 2;
    int orderStatusReserveCompleted = 8;


    StateListDrawable grayShape = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(BaseApplication.get().getResources().getColor(R.color.basic_blue))
            .setCornerRadius(UiUtils.dp2px(BaseApplication.get(), 3))
            .create();

    public TaskListAdapter(@Nullable List<Order> data) {
        super(R.layout.item_task, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, Order order) {
        helper.setText(R.id.tv_departure, order.getStartNodeName())
                .setText(R.id.tv_destination, order.getEndNodeName())
                .setText(R.id.tv_order_id, "订单编号：" + order.getPlanCode())
                .setText(R.id.tv_car_id, order.getDriverId())
                .addOnClickListener(R.id.btn_accept_task)
                .addOnClickListener(R.id.btn_reject_task)
                .addOnClickListener(R.id.btn_start_task);

        int orderStatus = order.getOrderStatus();
        helper.getView(R.id.btn_accept_task).setVisibility(orderStatus == 1 ? View.VISIBLE : View.GONE);
        helper.getView(R.id.btn_reject_task).setVisibility(orderStatus == 1 ? View.VISIBLE : View.GONE);

        helper.getView(R.id.btn_start_task).setVisibility(
                (orderStatus == orderStatusHadAccept
                        || orderStatus == orderStatusHadRefuse
                        || orderStatus == orderStatusReserveCompleted) ? View.VISIBLE : View.GONE
        );


        if (orderStatus == orderStatusHadAccept) {
            helper.setText(R.id.btn_start_task, "等待预约完成");
        } else if (orderStatus == orderStatusReserveCompleted) {
            helper.setText(R.id.btn_start_task, "发车");
        } else if (orderStatus == orderStatusHadRefuse) {
            helper.setText(R.id.btn_start_task, "重新接单");
        }

        TextView tvCarId = helper.getView(R.id.tv_car_id);
        tvCarId.setText(order.getVehicleHead());
        tvCarId.setBackground(grayShape);


    }
}
