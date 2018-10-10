package com.tengbo.module_order.ui.processing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.tengbo.basiclibrary.utils.SelectorFactory;
import com.tengbo.basiclibrary.utils.UiUtils;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.base.BaseMvpFragment;
import com.tengbo.commonlibrary.commonBean.Location;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.StepAdapter;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.bean.Step;
import com.tengbo.module_order.bean.StepCache;
import com.tengbo.module_order.service.LocateService;
import com.tengbo.module_order.service.UploadLocationService;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import utils.CommonDialog;
import utils.ToastUtils;
import utils.permission.PermissionManager;

/**
 *
 */
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
    private List<Step> steps = new ArrayList<>();
    private StepAdapter mAdapter;
    private Order mOrder;
    private View headerView;
    private int clickStepPosition;
    private boolean mIsNextStepAutoPass;
    private Step mNextStep;


    public static ProcessingOrderFragment newInstance(int orderId) {
        ProcessingOrderFragment processingOrderFragment = new ProcessingOrderFragment();
        Bundle args = new Bundle();
        args.putInt("orderId", orderId);
        processingOrderFragment.setArguments(args);
        return processingOrderFragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mOrderId = arguments.getInt("orderId");
        getProcessingOrder();
    }

    /**
     * 获取正在进行中的订单
     */
    private void getProcessingOrder() {
        _mActivity.startService(new Intent(_mActivity, LocateService.class));
        _mActivity.startService(new Intent(_mActivity, UploadLocationService.class));
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
//        LogUtil.d("设置背景");
        tv_start.setBackground(whiteStrokeShape);
        tv_end.setBackground(whiteStrokeShape);
        tv_inspection_feedback.setBackground(blueShape);

        tv_inspection_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NormalStepPassFragment dialog = NormalStepPassFragment.newInstance(null);
                dialog.show(getFragmentManager(), "");
//                ExceptionFeedbackActivity.start(_mActivity, mOrder);
            }
        });


        initRv();

    }

    private void initRv() {
        rv_node_action.setLayoutManager(new GridLayoutManager(_mActivity, 4));

        mAdapter = new StepAdapter(steps);
        mAdapter.addHeaderView(headerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Step step = (Step) adapter.getItem(position);
            clickStepPosition = position;
            assert step != null;
            dealAction(step);
        });

        rv_node_action.setAdapter(mAdapter);
    }


    /**
     * 1.如果步骤是自动通过的，不用处理
     * 2.非自动通过，如果是第一个步骤，直接处理
     * 3.不是第一步骤，如果上一个步骤是可以跳过，直接处理。否则需要判断上一步骤是否已经通过
     * 4.如果该步骤标记为已经缓存
     *
     * @param step 被点击的步骤
     */
    private void dealAction(Step step) {
        //如果该步骤标记为缓存，就直接上传数据
        if (step.isCached()) {
            mPresent.uploadStepDate(mOrder.getDriverOrderId(), step.getStepSerialNumber());
        }
        //如果点击的步骤不是自动通过类型并且未完成
        if (step.getNodeType() == 1 && !step.isProcessed()) {
            if (clickStepPosition == 0) {
                stepConfirm(step);
            } else {
                Step preStep = steps.get(clickStepPosition - 1);
                //如果上一个节点没有通过并且不是可以跳过的类型
                if (!preStep.isProcessed() && preStep.getProcessNecessary() != 2) {
                    ToastUtils.show(getContext(), "上一个步骤还没有完成");
                } else {
                    stepConfirm(step);
                }
            }
        } else if (step.getNodeType() == 2) {
            ToastUtils.show(getContext(), "该步骤是自动通过类型");
        } else {
            ToastUtils.show(getContext(), "该步骤已经通过");
        }

    }


    /** 执行步骤操作
     * 1.普通步骤-->弹窗拍照-->数据上链->更新订单的当前步骤
     * 2.特殊步骤-->进入额外的界面-->弹窗拍照-->数据上链->更新订单的当前步骤
     * @param step
     */
    private void stepConfirm(Step step) {
        NormalStepPassFragment dialog = NormalStepPassFragment.newInstance(step);
        assert getFragmentManager() != null;
        dialog.show(getFragmentManager(), "");
    }



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_processing_order;
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresent.getOrder(mOrderId);
    }


    /**
     * @Desc 尝试从数据库获取没有提交成功的step
     * 如果有未提交的数据，则刷新界面，展示一个感叹号，点击步骤时直接上传数据
     */
    private void getCachedSteps() {
        List<StepCache> stepCaches = LitePal.where("driverOrderId = ?",String.valueOf(mOrder.getDriverOrderId()) ).find(StepCache.class);
        for(StepCache stepCache:stepCaches){
            mAdapter.setStepCached(stepCache.getStepSerialNumber(),true);
        }

    }


    /** 显示订单、步骤等信息
     * @param order 订单
     */
    @Override
    public void showOrder(Order order) {
        mOrder = order;

        showDetail();

        getCachedSteps();

    }

    @Override
    public void passActionSuccess(int position) {
        mAdapter.setStepPass(position);
    }




    /**上传缓存步骤数据成功后
     * 1.刷新界面
     * 2.删除数据库数据
     * @param stepSerialNumber 已经上传成功后的缓存的步骤
     */
    @Override
    public void uploadStepDateSuccess(int stepSerialNumber) {
        mAdapter.setStepCached(stepSerialNumber ,false);
        LitePal.deleteAll(StepCache.class,"stepSerialNumber = ?", String.valueOf(stepSerialNumber));


    }

    private void showDetail() {
        setText(tv_order_id, R.string.order_id, "AHDG33333");
        tv_method.setText("直达");
        tv_departure.setText("重庆江北机场");
        tv_destination.setText("杭州国际机场");

        steps = mPresent.createSteps(null);
        mAdapter.replaceAll(steps);
    }


    @SuppressLint("SetTextI18n")
    private void setText(TextView tv, int resId, String after) {
        String before = getString(resId);
        if (!TextUtils.isEmpty(before)) {
            tv.setText(before + after);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            //图片已经上传完成，更新订单当前的步骤节点等信息，设置当前步骤为通过
            updateOrder();
        }
    }

    /**
     * @Desc 更新订单，确认下一个步骤类型，设置当前步骤为通过
     */
    private void updateOrder() {
        //TODO 更新订单
        mAdapter.setStepPass(clickStepPosition);
        if (clickStepPosition != steps.size() - 1) {
            Step step = steps.get(clickStepPosition);
            mNextStep = step;
            if (step.getNodeType() == 2) {
                mIsNextStepAutoPass = true;
            }
        }
    }

}
