package com.tengbo.module_order.fragment.history;


import android.os.Bundle;

import com.tengbo.commonlibrary.base.BaseFragment;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Order;

import java.util.ArrayList;


/**
 * @author Wang Chenchen
 * @date 2018-8-20 18:04:59
 * 历史订单Fragment
 */
public class HistoryOrderDetailFragment extends BaseFragment {


    public static HistoryOrderDetailFragment newInstance() {
        HistoryOrderDetailFragment fragment = new HistoryOrderDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        // 初始化时间选择视图数据
//        tsFrom.setDate(System.currentTimeMillis());
//        tsTo.setDate(System.currentTimeMillis());
//        // 解决ListView与ScrollView滑动冲突
//        lvHistoryOrder.setOnTouchListener((view, motionEvent) -> {
//            view.getParent().requestDisallowInterceptTouchEvent(true);
//            return false;
//        });
//        // TODO demo list
//        lvHistoryOrder.setVisibility(View.VISIBLE);
        ArrayList<Order> items = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Order o = new Order();
            o.orderNum = "2837492";
            o.completeDate = "2018-8-21 11:14:24";
            o.freight = 3000;
            o.alreadyPay = 3000;
            o.shouldDeduct = 0;
            o.orderStatus = "已接单";
            items.add(o);
        }
//        HistoryOrderAdapter adapter = new HistoryOrderAdapter(getActivity(), items);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_order_detail;
    }

    @Override
    protected void getTransferData(Bundle arguments) {

    }

}
