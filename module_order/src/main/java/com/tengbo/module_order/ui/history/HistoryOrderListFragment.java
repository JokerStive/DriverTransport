package com.tengbo.module_order.ui.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.base.BaseMvpFragment;
import com.tengbo.commonlibrary.net.BaseListResponse;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.HistoryTaskListAdapter;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.custom.view.TimePickDialog;

import com.tengbo.commonlibrary.widget.TitleBar;
import com.tengbo.module_order.ui.task.OrderContract;
import com.tengbo.module_order.ui.task.OrderDetailActivity;
import com.tengbo.module_order.ui.task.OrderPresenter;

import java.util.ArrayList;

import utils.ToastUtils;

/**
 * @author yk_de
 */
public class HistoryOrderListFragment extends BaseMvpFragment<OrderContract.Presenter> implements OrderContract.View {


    TitleBar titleBar;
    RecyclerView rvHistoryOrders;
    SwipeRefreshLayout srl;
    private HistoryTaskListAdapter mAdapter;
    private BaseListResponse<Order> mListResponse;
    private String startTime;
    private String endTime;

    public static HistoryOrderListFragment newInstance() {
        return new HistoryOrderListFragment();
    }

    @Override
    protected void initPresent() {
        mPresent = new OrderPresenter();
        mPresent.bindView(this);
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
                        startTime = from;
                        endTime = to;
                        LogUtil.d(from + "--------" + to);
                        getHistoryOrders(0);
                        timePickDialog.dismiss();
                    }
                });
                timePickDialog.show(_mActivity.getSupportFragmentManager(), "");
            }
        });

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHistoryOrders(1);
            }
        });

        rvHistoryOrders.setLayoutManager(new LinearLayoutManager(_mActivity));
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.order_layout_empty, null);
        mAdapter = new HistoryTaskListAdapter(new ArrayList<>());
        mAdapter.setEmptyView(emptyView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Order order = (Order) adapter.getItem(position);
                assert order != null;
                OrderDetailActivity.start(_mActivity, order.getOrderCode(), true);
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mListResponse.isEnd()) {
                    mAdapter.loadMoreEnd();
//                    ToastUtils.show(getContext(), "没有更多数据了");
                } else {
                    getHistoryOrders(mListResponse.getPage() + 1);
                }
            }
        }, rvHistoryOrders);
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
        getHistoryOrders(1);
    }

    public void getHistoryOrders(int page) {
        mPresent.getHistoryOrders(page, startTime, endTime);
    }


    @Override
    public void startProgress() {
        if (!srl.isRefreshing()) {
            srl.setRefreshing(true);
        }
    }

    @Override
    public void stopProgress() {
        if (srl.isRefreshing()) {
            srl.setRefreshing(false);
        }
    }

    @Override
    public void setOrderStatusSuccess(int status) {

    }

    @Override
    public void getOrderSuccess(Order order) {

    }

    @Override
    public void getOrderSuccess(int page, BaseListResponse<Order> listResponse) {

    }

    @Override
    public void getHistoryOrderSuccess(int page, BaseListResponse<Order> listResponse) {
        mListResponse = listResponse;
        if (page == 1) {
            mAdapter.setNewData(listResponse.getRows());
        } else {
            mAdapter.loadMoreComplete();
            mAdapter.addData(listResponse.getRows());
        }
    }

    @Override
    public void hasProcessingOrder(boolean hasProcessingOrder) {

    }
}


