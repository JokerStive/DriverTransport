package com.tengbo.module_order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.base.BaseFragment;
import com.tengbo.module_order.adapter.TaskListAdapter;
import com.tengbo.module_order.ui.InfoFragment;
import com.tengbo.module_order.ui.history.HistoryOrderListFragment;
import com.tengbo.module_order.ui.processing.ProcessingOrderFragment;
import com.tengbo.module_order.ui.task.TaskListFragment;

public class OrderRootFragment extends BaseFragment {

    @NonNull
    public static OrderRootFragment newInstance() {
        return new OrderRootFragment();
    }

    @Override
    protected void initView() {
        mRootView.findViewById(R.id.task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(TaskListFragment.newInstance());
            }
        });

        mRootView.findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(HistoryOrderListFragment.newInstance());
            }
        });

        mRootView.findViewById(R.id.processing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ProcessingOrderFragment.newInstance(1));
            }
        });

        mRootView.findViewById(R.id.info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(InfoFragment.newInstance());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_root;
    }

    @Override
    protected void getTransferData(Bundle arguments) {

    }


}
