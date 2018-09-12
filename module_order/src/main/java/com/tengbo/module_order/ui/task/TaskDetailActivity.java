package com.tengbo.module_order.ui.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tengbo.commonlibrary.base.BaseMvpActivity;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.ui.inspection.InspectionActivity;
import widget.TitleBar;

import java.util.List;

public class TaskDetailActivity extends BaseMvpActivity<TaskContract.Presenter> implements TaskContract.View {


    TitleBar titleBar;
    TextView tvOrderId;
    TextView tvDriverName;
    TextView tvCarInfo;
    TextView tvDriverName1;
    TextView tvCarLicense;
    TextView tvBoxNumber;
    Button btnStartTask;
    TextView tvDeparture;
    TextView tvDepartureContact;
    TextView tvDestination;
    TextView tvDestinationContact;
    TextView tvGoodsName;
    TextView tvGoodsWeight;
    TextView tvGPSNumber;
    TextView tvGoodsFlight;
    TextView tvShippingTime;
    TextView tvStartTime;
    TextView tvArriveTime;
    TextView tvDriverFee;
    TextView tvDriverPreFee;
    TextView tvDriverActualFee;
    TextView tvPetroChina;
    TextView tvSinopec;
    Button btnAcceptOrder;
    Button btnCancelOrder;

    LinearLayout llContainer;


    private Order mOrder;


    public static void start(Activity activity, Order order) {
        Intent intent = new Intent(activity, TaskDetailActivity.class);
        intent.putExtra("order", order);
        activity.startActivity(intent);

    }


    @Override
    protected void initPresent() {
        mPresent = new TaskListPresenter();
        mPresent.bindView(this);
    }

    @Override
    protected void onIntent(Intent intent) {
        super.onIntent(intent);
        mOrder = (Order) intent.getSerializableExtra("order");

    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        titleBar = findViewById(R.id.titleBar);
        tvOrderId = findViewById(R.id.tv_order_id);
        tvDriverName = findViewById(R.id.tv_driver_name);
        tvCarInfo = findViewById(R.id.tv_car_info);
        tvDriverName1 = findViewById(R.id.tv_driver_name1);
        tvCarLicense = findViewById(R.id.tv_car_license);
        tvBoxNumber = findViewById(R.id.tv_box_number);
        tvStartTime = findViewById(R.id.tv_start_time);
        tvDeparture = findViewById(R.id.tv_departure);
        tvDepartureContact = findViewById(R.id.tv_departure_contact);
        tvDestination = findViewById(R.id.tv_destination);
        tvDestinationContact = findViewById(R.id.tv_destination_contact);
        tvGoodsName = findViewById(R.id.tv_goods_name);
        tvGoodsWeight = findViewById(R.id.tv_goods_weight);
        tvGPSNumber = findViewById(R.id.tv_GPS_number);
        tvGoodsFlight = findViewById(R.id.tv_goods_flight);
        tvShippingTime = findViewById(R.id.tv_shipping_time);
        tvStartTime = findViewById(R.id.tv_start_time);
        tvArriveTime = findViewById(R.id.tv_arrive_time);
        tvDriverFee = findViewById(R.id.tv_driver_fee);
        tvDriverPreFee = findViewById(R.id.tv_driver_preFee);
        tvDriverActualFee = findViewById(R.id.tv_driver_actualFee);
        tvPetroChina = findViewById(R.id.tv_PetroChina);
        tvSinopec = findViewById(R.id.tv_Sinopec);
        btnAcceptOrder = findViewById(R.id.btn_accept_order);
        btnCancelOrder = findViewById(R.id.btn_cancel_order);
        llContainer = findViewById(R.id.ll_unComplete_container);
        btnStartTask = findViewById(R.id.btn_start_task);


        titleBar.setOnBackClickListener(this::finish);

        if (mOrder != null) {
            tvOrderId.setText("订单编号：" + mOrder.getOrderNum() + "      " + "直达");

            //司机车辆信息
            tvDriverName.setText("王校长");
            tvCarInfo.setText(getString(R.string.execute_truck) + "渝B123456");
            tvDriverName1.setText(getString(R.string.truck_driver) + "王校长");
            tvCarLicense.setText(getString(R.string.trailer_license) + "渝B 123455(挂)");
            tvBoxNumber.setText(getString(R.string.container_num) + "ZXYD2344");

            //货物基本信息
            setTextStyleSoan(tvDeparture, R.string.place_of_pick_up_goods, "富士康");
            setTextStyleSoan(tvDepartureContact, R.string.contact_of_pick_up_goods, "王琦（18523520173）");
            setTextStyleSoan(tvDestination, R.string.place_of_unload, "王琦（18523520173）");
            setTextStyleSoan(tvDestinationContact, R.string.contact_of_unload, "王琦（18523520173）");
            setTextStyleSoan(tvGoodsName, R.string.name_of_goods, "王琦（18523520173）");
            setTextStyleSoan(tvGoodsWeight, R.string.weight_of_goods, "王琦（18523520173）");
            setTextStyleSoan(tvGPSNumber, R.string.gps_num, "王琦（18523520173）");
            setTextStyleSoan(tvGoodsFlight, R.string.transport_num, "王琦（18523520173）");

            //货运时间
            setTextStyleSoan(tvShippingTime, R.string.time_of_plan_load, "王琦（18523520173）");
            setTextStyleSoan(tvStartTime, R.string.latest_time_of_send_out_goods, "王琦（18523520173）");
            setTextStyleSoan(tvArriveTime, R.string.time_of_plan_to_arrive, "王琦（18523520173）");


            //运费
            setTextStyleSoan(tvDriverFee, R.string.driver_freight, "王琦（18523520173）");
            setTextStyleSoan(tvDriverPreFee, R.string.pre_pay, "王琦（18523520173）");
            setTextStyleSoan(tvDriverActualFee, R.string.actual_pre_pay, "王琦（18523520173）");

            //油款
            setTextStyleSoan(tvPetroChina, R.string.sinopec_card, "王琦（18523520173）");
            setTextStyleSoan(tvSinopec, R.string.petrochina_card, "王琦（18523520173）");

            //按钮
            btnStartTask.setVisibility(mOrder.getOrderStatus().equals("已接单") ? View.VISIBLE : View.INVISIBLE);
            llContainer.setVisibility(!mOrder.getOrderStatus().equals("已接单") ? View.VISIBLE : View.GONE);


            btnStartTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InspectionActivity.start(TaskDetailActivity.this);
                }
            });
        }


    }


    @SuppressLint("SetTextI18n")
    private void setTextStyleSoan(TextView tv, int before, String after) {
        String beforeString = getString(before);
        int beforeStringLength = beforeString.length();
        if (beforeStringLength > 0) {
            String resultString = beforeString + after;
            SpannableString spannableString = new SpannableString(resultString);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, beforeStringLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.NORMAL), resultString.length() - after.length(), resultString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            tv.setText(spannableString);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_task_detail;
    }


    @Override
    public void showTasks(List<Order> tasks) {

    }

    @Override
    public void changeStatusSuccess(int status) {

    }


}
