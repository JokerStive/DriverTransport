package com.tengbo.module_order.ui.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tengbo.commonlibrary.base.BaseMvpActivity;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.net.BaseListResponse;
import com.tengbo.commonlibrary.widget.TitleBar;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.GoodsAdapter;
import com.tengbo.module_order.bean.Coast;
import com.tengbo.module_order.bean.Flow;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.event.RefreshOrderList;
import com.tengbo.module_order.ui.history.StepRecordActivity;
import com.tengbo.module_order.ui.inspection.InspectionActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import utils.ToastUtils;

/**
 * @author yk
 * @Description 司机任务详情页面
 */
public class OrderDetailActivity extends BaseMvpActivity<OrderContract.Presenter> implements OrderContract.View, View.OnClickListener {


    TitleBar titleBar;
    TextView tvOrderId;
    TextView tvDriverName;
    TextView tvCarInfo;
    TextView tvDriverName1;
    TextView tvCarLicense;
    TextView btnStartTask;
    TextView tvDriverFee;
    TextView tvDriverPreFee;
    TextView tvDriverActualFee;
    TextView tvPetroChina;
    TextView tvSinopec;


    private String mOrderCode;
    private String mCarId;
    private RecyclerView rvGoods;
    private GoodsAdapter mGoodsAdapter;
    private LinearLayout llExtraFee;
    private LinearLayout llDeductionFee;
    private TextView tvTitleTransFee;
    private TextView tvDriverPreActualFee;
    private LinearLayout llOilFee;
    private int mOrderStatus;
    private View llOperateContainer;
    private boolean mIsHistory;

    int orderStatusUnAccept = 1;
    int orderStatusHadAccept = 2;
    int orderStatusHadRefuse = 3;
    int orderStatusNormalCompleted = 6;
    int orderStatusUnNormalCompleted = 7;
    int orderStatusReserveCompleted = 8;


    public static void start(Activity activity, String orderCode, boolean isHistory) {
        Intent intent = new Intent(activity, OrderDetailActivity.class);
        intent.putExtra("orderCode", orderCode);
        intent.putExtra("isHistory", isHistory);
        activity.startActivity(intent);

    }


    @Override
    protected void initPresent() {
        mPresent = new OrderPresenter();
        mPresent.bindView(this);
        if (mIsHistory) {
            mPresent.getHistoryOrder(mOrderCode);
        } else {
            mPresent.getOrder(mOrderCode);
        }
    }

    @Override
    protected void onIntent(Intent intent) {
        super.onIntent(intent);
        mOrderCode = intent.getStringExtra("orderCode");
        mIsHistory = intent.getBooleanExtra("isHistory", false);
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        titleBar = findViewById(R.id.titleBar);
        titleBar.setOnBackClickListener(this::finish);


        rvGoods = findViewById(R.id.rv);
        rvGoods.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mGoodsAdapter = new GoodsAdapter(new ArrayList<>());
        mGoodsAdapter.addHeaderView(initHeader());
        rvGoods.setAdapter(mGoodsAdapter);


        llOperateContainer = findViewById(R.id.ll_operate_container);
        findViewById(R.id.tv_positive).setOnClickListener(this);
        findViewById(R.id.tv_negative).setOnClickListener(this);


    }

    private View initHeader() {
        View header = LayoutInflater.from(getApplicationContext()).inflate(R.layout.order_header_order_detail, null);

        tvOrderId = header.findViewById(R.id.tv_order_id);
        tvDriverName = header.findViewById(R.id.tv_driver_name);
        tvCarInfo = header.findViewById(R.id.tv_car_info);
        tvCarLicense = header.findViewById(R.id.tv_car_license);
        btnStartTask = header.findViewById(R.id.btn_start_task);
        btnStartTask.setOnClickListener(this);

        tvTitleTransFee = header.findViewById(R.id.tv_title_trans_fee);
        tvDriverFee = header.findViewById(R.id.tv_driver_fee);
        tvDriverPreFee = header.findViewById(R.id.tv_driver_preFee);
        tvDriverActualFee = header.findViewById(R.id.tv_driver_actualFee);
        tvDriverPreActualFee = header.findViewById(R.id.tv_driver_pre_actualFee);
        tvPetroChina = header.findViewById(R.id.tv_PetroChina);
        tvSinopec = header.findViewById(R.id.tv_Sinopec);

        llExtraFee = header.findViewById(R.id.ll_extra_fee);
        llOilFee = header.findViewById(R.id.ll_oil_fee);
        llDeductionFee = header.findViewById(R.id.ll_deduction_fee);
        return header;
    }


    @SuppressLint("SetTextI18n")
    private void setTextStyleSoan(TextView tv, int before, String after) {
        String beforeString = getString(before);
        setTextStyle(tv, beforeString, after);
    }


    private void setTextStyle(TextView tv, String beforeString, String after) {
        int beforeStringLength = beforeString.length();
        if (beforeStringLength > 0) {
            String resultString = beforeString + after;
            SpannableString spannableString = new SpannableString(resultString);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, beforeStringLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            tv.setText(spannableString);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.order_activity_order_detail;
    }


    @Override
    public void startProgress() {
    }

    @Override
    public void stopProgress() {

    }

    @Override
    public void setOrderStatusSuccess(int status) {
        postEvent();
        finish();

    }

    @Override
    public void getOrderSuccess(Order order) {
        showOrderDetail(order);
    }

    @Override
    public void getOrderSuccess(int page, BaseListResponse<Order> listResponse) {

    }

    @Override
    public void getHistoryOrderSuccess(int page, BaseListResponse<Order> listResponse) {

    }

    @Override
    public void hasProcessingOrder(boolean hasProcessingOrder) {
        if (!hasProcessingOrder) {
            InspectionActivity.start(this, mOrderCode, mCarId);
        } else {
            ToastUtils.show(getApplicationContext(), getString(R.string.has_processing_order));
        }
    }


    /**
     * @param order 订单
     */
    @SuppressLint("SetTextI18n")
    private void showOrderDetail(Order order) {


        mCarId = order.getVehicleHead();

        mGoodsAdapter.setNewData(order.getGoods());


        tvOrderId.setText(getString(R.string.order_id) + order.getPlanCode());
        tvDriverName.setText(User.getName());
        tvCarInfo.setText(getString(R.string.execute_truck) + order.getVehicleHead());
        tvCarLicense.setText(getString(R.string.trailer_license) + order.getVehicleTrailer());

        mOrderStatus = order.getOrderStatus();

        tvTitleTransFee.setText(getString(R.string.freight) + "(" + getAuditingStatus(order.getAuditingStatus()) + ")");
        setFlow(order.getFlows());

        //未接单
        if (mOrderStatus == orderStatusUnAccept) {
            llOilFee.setVisibility(View.VISIBLE);
            btnStartTask.setVisibility(View.INVISIBLE);
            llExtraFee.setVisibility(View.GONE);
            llDeductionFee.setVisibility(View.GONE);
            llOperateContainer.setVisibility(View.VISIBLE);
        }
        //已经预约完成
        else if (mOrderStatus==orderStatusHadAccept || mOrderStatus == orderStatusReserveCompleted) {
            btnStartTask.setVisibility(mOrderStatus==orderStatusReserveCompleted?View.VISIBLE:View.GONE);
            btnStartTask.setText(getString(R.string.start_task));
            llExtraFee.setVisibility(View.GONE);
            llDeductionFee.setVisibility(View.GONE);
            llOperateContainer.setVisibility(View.GONE);
        }
        //拒单
        else if (mOrderStatus == orderStatusHadRefuse) {
            btnStartTask.setVisibility(View.VISIBLE);
            btnStartTask.setText(getString(R.string.accept_again));
            llExtraFee.setVisibility(View.GONE);
            llDeductionFee.setVisibility(View.GONE);
            llOperateContainer.setVisibility(View.GONE);
        }

        //已完成(正常或者异常)
        else if (mOrderStatus == orderStatusNormalCompleted || mOrderStatus == orderStatusUnNormalCompleted) {
            btnStartTask.setVisibility(View.VISIBLE);
            btnStartTask.setText(getString(R.string.history_list));
            setCoast(order.getCoasts());
            llOperateContainer.setVisibility(View.GONE);
        }


    }

    /**
     * @param auditingStatus 订单财务审核状态（0待审核1 通过 2 驳回 ）
     * @return 订单财务审核状态对应的字符
     */
    private String getAuditingStatus(int auditingStatus) {
        String result = "";
        if (auditingStatus == 0) {
            result = "待审核";
        } else if (auditingStatus == 1) {
            result = "通过";
        } else if (auditingStatus == 2) {
            result = "驳回";
        }
        return result;
    }

    /**
     * 已经完成的订单，显示额外的费用
     *
     * @param coasts
     */
    private void setCoast(List<Coast> coasts) {
        if (coasts == null) {
            return;
        }
        for (Coast coast : coasts) {
            if (coast.getCostType() == 1) {
                //额外费用
                addExtraFeeView(coast);
            } else if (coast.getCostType() == 2) {
                //扣款
                addDeductionFeeView(coast);

            }
        }
    }

    /**
     * 添加扣款费用(支出)view到container
     *
     * @param coast 费用
     */
    private void addDeductionFeeView(Coast coast) {
        TextView textView = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.order_view_text_view, null);
        setTextStyle(textView, coast.getCostSubjectName() + "：", amountWithUnit(coast.getCostAmount()));
        llDeductionFee.addView(textView);
    }


    /**
     * 显示流水卡费用等(油卡)
     *
     * @param flows 流水s
     */
    private void setFlow(List<Flow> flows) {
        if (flows == null) {
            return;
        }
        int transFee = 0;
        int transRealFee = 0;
        int preFee = 0;
        int preRealFee = 0;
        for (Flow flow : flows) {
            //预付款
            if (flow.getBusinessType() == 2) {
                preFee = flow.getPayableAmount() + preFee;
                preRealFee = flow.getRealAmount() + preRealFee;
            }
            //运输费用
            else if (flow.getBusinessType() == 1) {
                transFee = flow.getPayableAmount() + transFee;
                transRealFee = flow.getRealAmount() + transRealFee;
            }

            //油卡
            if (flow.getCardType() == 2) {
                addCardView(flow);
            }
        }

        setTextStyleSoan(tvDriverFee, R.string.driver_freight, amountWithUnit(transFee));
        setTextStyleSoan(tvDriverPreFee, R.string.pre_pay, amountWithUnit(preFee));
        if (transRealFee != 0) {
            tvDriverActualFee.setVisibility(View.VISIBLE);
            setTextStyleSoan(tvDriverActualFee, R.string.driver_freight_actual, amountWithUnit(transRealFee));
        }

        if (preRealFee != 0) {
            tvDriverPreActualFee.setVisibility(View.VISIBLE);
            setTextStyleSoan(tvDriverPreActualFee, R.string.actual_pre_pay, amountWithUnit(preRealFee));
        }

    }


    /**
     * 添加油卡信息
     *
     * @param flow 流水
     */
    private void addCardView(Flow flow) {
        TextView textView = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.order_view_text_view, null);
        setTextStyle(textView, "中石油" + "：", amountWithUnit(flow.getPayableAmount()));
        llOilFee.addView(textView);
    }

    /**
     * 添加额外费用（收入）view到container
     *
     * @param coast 费用
     */
    private void addExtraFeeView(Coast coast) {
        TextView textView = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.order_view_text_view, null);
        setTextStyle(textView, coast.getCostSubjectName() + "：", amountWithUnit(coast.getCostAmount()));
        llExtraFee.addView(textView);
    }

    /**
     * @param costAmount 金额
     * @return 金额(单位)
     */
    private String amountWithUnit(int costAmount) {
        return costAmount + "元";
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_start_task) {
            if (mOrderStatus == orderStatusReserveCompleted) {
                mPresent.checkHasProcessingOrder();
            } else if (mOrderStatus == orderStatusNormalCompleted || mOrderStatus == orderStatusUnNormalCompleted) {
                StepRecordActivity.start(this, mOrderCode);
            }

        } else if (id == R.id.tv_positive) {
            setOrderStatusAndPop(orderStatusHadAccept);
            mPresent.setOrderStatus(mOrderCode, orderStatusHadAccept);
        } else if (id == R.id.tv_negative) {
            mPresent.setOrderStatus(mOrderCode, orderStatusHadRefuse);
        }
    }

    private void postEvent() {
        EventBus.getDefault().post(new RefreshOrderList());
    }

    private void setOrderStatusAndPop(int status) {

        postEvent();
        finish();
    }
}
