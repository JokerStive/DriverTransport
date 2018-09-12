package com.tengbo.module_order.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.commonlibrary.base.QuickAdapter;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Order;

import java.util.List;

public class HistoryTaskListAdapter extends QuickAdapter<Order> {

    public HistoryTaskListAdapter(List<Order> data) {
        super(R.layout.item_history_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Order order) {
        helper.setText(R.id.tv_complete_time, "完成日期：2018/05/02    15:20")
                .setText(R.id.tv_order_id, "订单编号：TBQQ2018080100")
                .setText(R.id.tv_transport_fee, "运费：8000元")
                .setText(R.id.tv_deduction_fee, "支付：2000元")
                .setText(R.id.tv_paid_fee, "应扣：500元")
                .setText(R.id.tv_order_status, "接单状态：已接单");
    }
}
