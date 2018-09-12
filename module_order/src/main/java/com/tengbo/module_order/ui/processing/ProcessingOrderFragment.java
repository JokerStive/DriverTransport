package com.tengbo.module_order.ui.processing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.basiclibrary.utils.SelectorFactory;
import com.tengbo.basiclibrary.utils.UiUtils;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.base.BaseMvpFragment;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.NodeActionAdapter;
import com.tengbo.module_order.bean.Action;
import com.tengbo.module_order.bean.Order;

import java.util.ArrayList;
import java.util.List;

import utils.CommonDialog;
import utils.permission.PermissionManager;

public class ProcessingOrderFragment extends BaseMvpFragment<ProcessingOrderContract.Presenter> implements ProcessingOrderContract.View {
    TextView tv_order_id;
    TextView tv_departure;
    ImageView iv_car;
    TextView tv_destination;
    TextView tv_schedule_time;
    TextView tv_method;
    TextView tv_start;
    TextView tv_end;
    TextView tv_schdule_arrive_time;
    TextView tv_inspection_feedback;
    LinearLayout ll_card;
    RecyclerView rv_node_action;


    private int mOrderId;
    private List<Action> actions = new ArrayList<>();
    private NodeActionAdapter mAdapter;
    private Order morder;
    private View headerView;


    public static ProcessingOrderFragment newInstance(int orderId) {
        ProcessingOrderFragment processingOrderFragment = new ProcessingOrderFragment();
        Bundle args = new Bundle();
        args.putInt("orderId", orderId);
        processingOrderFragment.setArguments(args);
        return processingOrderFragment;
    }

    @Override
    protected void initPresent() {
        mPresent = new ProcessingOrderPresenter();
        mPresent.bindView(this);
    }

    @Override
    protected void initView() {
        headerView = LayoutInflater.from(BaseApplication.get()).inflate(R.layout.head_processing_order, null);

        tv_order_id = headerView.findViewById(R.id.tv_order_id);
        tv_departure = headerView.findViewById(R.id.tv_departure);
        iv_car = headerView.findViewById(R.id.iv_car);
        tv_method = headerView.findViewById(R.id.tv_method);
        tv_destination = headerView.findViewById(R.id.tv_destination);
        tv_schedule_time = headerView.findViewById(R.id.tv_schedule_time);
        tv_end = headerView.findViewById(R.id.tv_end);
        tv_start = headerView.findViewById(R.id.tv_start);
        ll_card = headerView.findViewById(R.id.ll_card);
        tv_schdule_arrive_time = headerView.findViewById(R.id.tv_schedule_arrive_time);
        tv_inspection_feedback = headerView.findViewById(R.id.tv_inspection_feedback);
        rv_node_action = mRootView.findViewById(R.id.rv_node_action);

        StateListDrawable whiteStrokeShape = SelectorFactory.newShapeSelector()
                .setShape(GradientDrawable.OVAL)
                .setStrokeWidth(UiUtils.dp2px(BaseApplication.get(), 1))
                .setDefaultStrokeColor(BaseApplication.get().getResources().getColor(com.tengbo.commonlibrary.R.color.basic_white))
                .create();

        StateListDrawable blueShape = SelectorFactory.newShapeSelector()
                .setDefaultBgColor(BaseApplication.get().getResources().getColor(com.tengbo.commonlibrary.R.color.basic_blue))
                .setCornerRadius(UiUtils.dp2px(BaseApplication.get(), 10))
                .create();
        LogUtil.d("设置背景");
        tv_start.setBackground(whiteStrokeShape);
        tv_end.setBackground(whiteStrokeShape);
        tv_inspection_feedback.setBackground(blueShape);

        tv_inspection_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExceptionFeedbackActivity.start(_mActivity, morder);
            }
        });


        initRv();

    }

    private void initRv() {
        rv_node_action.setLayoutManager(new GridLayoutManager(_mActivity, 4));

        mAdapter = new NodeActionAdapter(actions);
        mAdapter.addHeaderView(headerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Action action = (Action) adapter.getItem(position);
            assert action != null;
            if (action.getStatus() == Action.UNSTART) {
                passAction(action, position);
            }
        });

        rv_node_action.setAdapter(mAdapter);
    }

    private void passAction(Action action, int position) {
        new CommonDialog(_mActivity)
                .setNotice("这个操作会把节点变成'通过',确定并拍照？")
                .setPositiveText("拍照")
                .setNegativeText("取消")
                .setOnPositiveClickListener(new CommonDialog.OnPositiveClickListener() {
                    @Override
                    public void onPositiveClick() {
                        dealAction();
                        int status = action.getStatus();
                        LogUtil.d("点击的action---" + action.getName() + "--position" + position);
                        if (status == Action.UNSTART) {
                            mPresent.passAction(1, position);
                        }


                    }
                })
                .show();

    }

    private void dealAction() {
        PermissionManager.getInstance(BaseApplication.get())
                .setOnRequestPermissionResult(new PermissionManager.RequestPermissionResult() {
                    @Override
                    public void onGranted() {
                        TakeActionPictureActivity.open(_mActivity);
                    }

                    @Override
                    public void onDenied(boolean isNotAskAgain) {
//                        dealPermissionDenied(isNotAskAgain);
                    }
                })
                .execute(this, Manifest.permission.CAMERA);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_processing_order;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mOrderId = arguments.getInt("orderId");
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresent.getOrder(mOrderId);
    }


    @Override
    public void showOrder(Order order) {
        morder = order;
        showDetail();
    }

    @Override
    public void passActionSuccess(int position) {
        mAdapter.setActionPass(position);
    }

    private void showDetail() {
        setText(tv_order_id, R.string.order_id, "AHDG33333");
        tv_method.setText("直达");
        tv_departure.setText("重庆江北机场");
        tv_destination.setText("杭州国际机场");


        for (int i = 0; i < 30; i++) {
            Action action = new Action();
            action.setAuto(i == 2 || i == 9 || i == 13);
            action.setName("开始" + i);
            action.setActualTime("18/8/8  08:02:22");
            action.setScheduleTime("18/8/8  08:02:22");
            action.setStatus(i < 8 ? Action.DONE : Action.UNSTART);
            actions.add(action);
        }
        mAdapter.replaceAll(actions);
    }


    @SuppressLint("SetTextI18n")
    private void setText(TextView tv, int resId, String after) {
        String before = getString(resId);
        if (!TextUtils.isEmpty(before)) {
            tv.setText(before + after);
        }
    }
}
