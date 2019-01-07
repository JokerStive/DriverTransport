package com.tengbo.module_order.ui.duty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tengbo.commonlibrary.base.BaseFragment;
import com.tengbo.commonlibrary.common.Config;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.commonlibrary.widget.TitleBar;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.DutyTaskAdapter;
import com.tengbo.module_order.bean.DutyTask;
import com.tengbo.module_order.bean.Task;
import com.tengbo.module_order.net.ApiOrder;
import com.tengbo.module_order.ui.task.OrderListFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class DutyListFragment extends BaseFragment {

    private Task mTask;
    private SwipeRefreshLayout srl;
    private DutyTaskAdapter mDutyTaskAdapter;
    public static final String REFRESH="refresh";

    public static DutyListFragment newInstance() {
        return new DutyListFragment();
    }


    @Override
    protected void initView() {
        TitleBar titleBar = mRootView.findViewById(R.id.titleBar);
        titleBar.setOnOperateClickListener(new TitleBar.OnOperateClickListener() {
            @Override
            public void onOperateClick() {
                if (mTask != null) {
                    assert getFragmentManager() != null;
                    AddDutyTaskFragment.newInstance(true, mTask).show(getFragmentManager(), null);
                }
            }
        });

        srl = mRootView.findViewById(R.id.srl);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDutyTask();
            }
        });


        RecyclerView rvDuty = mRootView.findViewById(R.id.rv_duty);
        rvDuty.setLayoutManager(new LinearLayoutManager(getContext()));
        mDutyTaskAdapter = new DutyTaskAdapter(new ArrayList<>());
        mDutyTaskAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Task task = (Task) adapter.getItem(position);
                assert getFragmentManager() != null;
                AddDutyTaskFragment.newInstance(false, task).show(getFragmentManager(), null);
            }
        });

        rvDuty.setAdapter(mDutyTaskAdapter);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getDutyTask();
    }

    /**
     *
     */
    public void getDutyTask() {
        Task task = new Task();
        task.setDriverId(User.getIdNumber());
        task.setOperationTime("");
        mSubscriptionManager.add(
                NetHelper.getInstance()
                        .getRetrofit().create(ApiOrder.class)
                        .getDutyTasks(task)
                        .compose(RxUtils.handleResult())
                        .compose(RxUtils.applySchedule())
                        .subscribe(new ProgressSubscriber<DutyTask>() {
                            @Override
                            protected void on_next(DutyTask dutyTask) {
                                initTask(dutyTask);
                                srl.setRefreshing(false);
                                mDutyTaskAdapter.setNewData(dutyTask.getTasks());
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                srl.setRefreshing(false);
                            }
                        }));
    }

    private void initTask(DutyTask dutyTask) {
        mTask = new Task();
        mTask.setDriverId(User.getIdNumber());
        mTask.setNodeCode(dutyTask.getNodeCode());
        mTask.setPlateNumber(dutyTask.getPlateNumber());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.order_fragment_duty_list;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void refresh(String  s){
        if(TextUtils.equals(REFRESH,s)){
            getDutyTask();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
