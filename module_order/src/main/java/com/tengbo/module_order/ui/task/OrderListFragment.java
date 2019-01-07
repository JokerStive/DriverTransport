package com.tengbo.module_order.ui.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.base.BaseMvpFragment;
import com.tengbo.commonlibrary.common.Event;
import com.tengbo.commonlibrary.net.BaseListResponse;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.TaskListAdapter;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.event.RefreshOrderList;
import com.tengbo.module_order.ui.inspection.InspectionActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import utils.ToastUtils;


public class OrderListFragment extends BaseMvpFragment<OrderContract.Presenter> implements OrderContract.View {

    RecyclerView rvTask;

    private BaseListResponse<Order> mListResponse;
    private TaskListAdapter mAdapter;
    private SwipeRefreshLayout srl;
    private String mClickOrderCode;
    private String mClickCardId;

    public static OrderListFragment newInstance() {
        return new OrderListFragment();
    }

    @Override
    protected void initPresent() {
        mPresent = new OrderPresenter();
        mPresent.bindView(this);
    }

    /**
     *
     */
    @Override
    protected void initView() {
        srl = mRootView.findViewById(R.id.srl);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrders(1);
            }
        });


        rvTask = mRootView.findViewById(R.id.rv_task);
        rvTask.setLayoutManager(new LinearLayoutManager(BaseApplication.get()));
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.order_layout_empty, null);
        mAdapter = new TaskListAdapter(new ArrayList<>());
        mAdapter.setEmptyView(emptyView);

        mAdapter.openLoadAnimation();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Order order = (Order) adapter.getItem(position);
                assert order != null;
                OrderDetailActivity.start(_mActivity, (order.getOrderCode()), false);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Order order = (Order) adapter.getItem(position);
                assert order != null;
                String orderCode = order.getOrderCode();
                int id = view.getId();
                if (id == R.id.btn_accept_task) {
                    setDriverOrderStatus(orderCode, 2);
                } else if (id == R.id.btn_reject_task) {
                    setDriverOrderStatus(orderCode, 3);
                } else if (id == R.id.btn_start_task) {
                    mClickCardId = order.getVehicleHead();
                    mClickOrderCode = orderCode;
                    int orderStatus = order.getOrderStatus();
                    if (orderStatus == 2) {
                        mPresent.checkHasProcessingOrder();
                    } else if (orderStatus == 3) {
                        setDriverOrderStatus(orderCode, 2);
                    }
                }
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mListResponse.isEnd()) {
                    mAdapter.loadMoreEnd();
//                    ToastUtils.show(getContext(), "没有更多数据了");
                } else {
                    getOrders(mListResponse.getPage() + 1);
                }
            }
        }, rvTask);
        rvTask.setAdapter(mAdapter);

    }

    /**
     * 设置司机订单状态
     *
     * @param orderCode
     * @param status    状态 2接单 3拒单 4开始订单
     */
    private void setDriverOrderStatus(String orderCode, int status) {
        mPresent.setOrderStatus(orderCode, status);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_task;
    }

    @Override
    protected void getTransferData(Bundle arguments) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getOrders(1);
    }


    /**
     * 获取指派给司机未开始的订单  订单运输状态（1 运输中 2所有未发车）
     *
     * @param page 页数
     */
    public void getOrders(int page) {
        mPresent.getOrders(page);
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

    /**
     * 设置订单状态成功后刷新页面
     *
     * @param status 设置的订单状态
     */
    @Override
    public void setOrderStatusSuccess(int status) {
        getOrders(1);
    }

    @Override
    public void getOrderSuccess(Order order) {

    }

    /**
     * 显示订单
     *
     * @param page         当前页面，1刷新数据否则添加数据
     * @param listResponse 数据
     */
    @Override
    public void getOrderSuccess(int page, BaseListResponse<Order> listResponse) {
        mListResponse = listResponse;
        if (page == 1) {
            mAdapter.setNewData(listResponse.getRows());
        } else {
            mAdapter.loadMoreComplete();
            mAdapter.addData(listResponse.getRows());
        }
    }

    @Override
    public void getHistoryOrderSuccess(int page, BaseListResponse<Order> listResponse) {

    }

    @Override
    public void hasProcessingOrder(boolean hasProcessingOrder) {
        if (!hasProcessingOrder) {
            InspectionActivity.start(getActivity(), mClickOrderCode, mClickCardId);
        } else {
            ToastUtils.show(getContext(), getString(R.string.has_processing_order));
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void refreshData(RefreshOrderList refreshOrderList) {
        mPresent.getOrders(1);
    }
}
