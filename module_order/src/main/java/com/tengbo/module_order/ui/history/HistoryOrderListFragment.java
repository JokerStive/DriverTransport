package com.tengbo.module_order.ui.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.base.BaseFragment;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.HistoryTaskListAdapter;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.custom.view.TimePickDialog;

import widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class HistoryOrderListFragment extends BaseFragment {


    TitleBar titleBar;
    RecyclerView rvHistoryOrders;
    SwipeRefreshLayout srl;
    List<Order> historyOrders = new ArrayList<>();
    private HistoryTaskListAdapter mAdapter;

    public static HistoryOrderListFragment newInstance() {
        return new HistoryOrderListFragment();
    }

    @Override
    protected void initView() {
        titleBar = mRootView.findViewById(R.id.titleBar);
        rvHistoryOrders = mRootView.findViewById(R.id.rv_history_order_list);
        srl = mRootView.findViewById(R.id.srl);
        srl.setColorSchemeColors(BaseApplication.get().getResources().getColor(R.color.basic_blue));

        titleBar.setOnBackClickListener(this::pop);
        titleBar.setOnOperateClickListener(new TitleBar.OnOperateClickListener() {
            @Override
            public void onOperateClick() {
                TimePickDialog timePickDialog = TimePickDialog.newInstance();
                timePickDialog.setOnQueryListener(new TimePickDialog.onQueryListener() {
                    @Override
                    public void onQuery(String from, String to) {
                        LogUtil.d(from + "--------" + to);
                        timePickDialog.dismiss();
                    }
                });
                timePickDialog.show(_mActivity.getSupportFragmentManager(), "");
            }
        });


//        srl.setEnabled(false);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHistoryOrders();
            }
        });

        rvHistoryOrders.setLayoutManager(new LinearLayoutManager(_mActivity));
        mAdapter = new HistoryTaskListAdapter(historyOrders);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HistoryOrderDetailActivity.start(_mActivity, (Order) adapter.getItem(position));
            }
        });
        rvHistoryOrders.setAdapter(mAdapter);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_order_list;
    }

    @Override
    protected void getTransferData(Bundle arguments) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getHistoryOrders();
    }

    private void getHistoryOrders() {
        LogUtil.d("开始加载历史订单数据--");
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
            historyOrders.add(order);

            showData();
        }
    }

    private void showData() {
        mAdapter.replaceAll(historyOrders);
    }


}
