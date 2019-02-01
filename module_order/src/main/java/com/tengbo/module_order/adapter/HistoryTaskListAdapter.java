package com.tengbo.module_order.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.commonlibrary.base.QuickAdapter;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.utils.DateUtils;

import java.util.List;

public class HistoryTaskListAdapter extends QuickAdapter<Order> {

    public HistoryTaskListAdapter(List<Order> data) {
        super(R.layout.item_history_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Order order) {
        helper.setText(R.id.tv_complete_time, "完成日期：" + DateUtils.iso2Utc(order.getFinishTime()))
                .setText(R.id.tv_order_id, "订单编号：" + order.getPlanCode())
                .setText(R.id.tv_transport_fee, spliceWithUnit("运费：", order.getDriverOderFee()))
                .setText(R.id.tv_deduction_fee, spliceWithUnit("支付：", order.getPaiedAmount()))
                .setText(R.id.tv_paid_fee, spliceWithUnit("应扣：", order.getDeductAmount()))
                .setText(R.id.tv_order_status, "订单状态：" + (order.getOrderStatus() == 7 ? "异常结束" : "正常结束"));
    }

    private String spliceWithUnit(String before, Object after) {
        if (after == null) {
            after = "";
        }
        return before + after.toString() +
                "元";
    }
}
