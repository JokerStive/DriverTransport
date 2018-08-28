package com.tengbo.module_order.fragment.task;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tengbo.commonlibrary.base.BaseMvpFragment;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Order;

import java.util.List;

import butterknife.BindView;

public class TaskDetailFragment extends BaseMvpFragment<TaskContract.Presenter> implements TaskContract.View {


    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_driver_name)
    TextView tvDriverName;
    @BindView(R.id.tv_car_info)
    TextView tvCarInfo;
    @BindView(R.id.tv_driver_name1)
    TextView tvDriverName1;
    @BindView(R.id.tv_car_license)
    TextView tvCarLicense;
    @BindView(R.id.tv_box_number)
    TextView tvBoxNumber;
    @BindView(R.id.btn_start_task)
    Button btnStartTask;
    @BindView(R.id.tv_departure)
    TextView tvDeparture;
    @BindView(R.id.tv_departure_contact)
    TextView tvDepartureContact;
    @BindView(R.id.tv_destination)
    TextView tvDestination;
    @BindView(R.id.tv_destination_contact)
    TextView tvDestinationContact;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_goods_weight)
    TextView tvGoodsWeight;
    @BindView(R.id.tv_GPS_number)
    TextView tvGPSNumber;
    @BindView(R.id.tv_goods_flight)
    TextView tvGoodsFlight;
    @BindView(R.id.tv_shipping_time)
    TextView tvShippingTime;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_arrive_time)
    TextView tvArriveTime;
    @BindView(R.id.tv_driver_fee)
    TextView tvDriverFee;
    @BindView(R.id.tv_driver_preFee)
    TextView tvDriverPreFee;
    @BindView(R.id.tv_driver_actualFee)
    TextView tvDriverActualFee;
    @BindView(R.id.tv_PetroChina)
    TextView tvPetroChina;
    @BindView(R.id.tv_Sinopec)
    TextView tvSinopec;
    @BindView(R.id.btn_accept_order)
    Button btnAcceptOrder;
    @BindView(R.id.btn_cancel_order)
    Button btnCancelOrder;

    @BindView(R.id.ll_unComplete_container)
    LinearLayout btnContainer;


    private Order mOrder;


    public static TaskDetailFragment newInstance(Order order) {
        TaskDetailFragment taskDetailFragment = new TaskDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("order", order);
        taskDetailFragment.setArguments(args);
        return taskDetailFragment;
    }

    @Override
    protected void initPresent() {
        mPresent = new TaskListPresenter();
        mPresent.bindView(this);
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
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
//            tvDeparture.setText(getString(R.string.place_of_pick_up_goods) + "富士康");
            tvDepartureContact.setText(getString(R.string.contact_of_pick_up_goods) + "王琦（18523520173）");
            tvDestination.setText(getString(R.string.place_of_unload) + "重庆中心站");
            tvDestinationContact.setText(getString(R.string.contact_of_unload) + "王琦（18523520173）");
            tvGoodsName.setText(getString(R.string.name_of_goods) + "直升机");
            tvGoodsWeight.setText(getString(R.string.weight_of_goods) + "2000");
            tvGPSNumber.setText(getString(R.string.gps_num) + "55555555");
            tvGoodsFlight.setText(getString(R.string.transport_num) + "3345");

            //货运时间
            tvShippingTime.setText(getString(R.string.time_of_plan_load) + " 8月1日 18:56");
            tvStartTime.setText(getString(R.string.latest_time_of_send_out_goods) + " 8月1日 18:56");
            tvArriveTime.setText(getString(R.string.time_of_plan_to_arrive) + " 8月1日 18:56");

            //运费
            tvDriverFee.setText(getString(R.string.driver_freight) + "1200");
            tvDriverPreFee.setText(getString(R.string.pre_pay) + "1200");
            tvDriverActualFee.setText(getString(R.string.actual_pre_pay) + "1200");

            //油款
            tvPetroChina.setText(getString(R.string.sinopec_card) + "1500元（实际支付：200元）");
            tvSinopec.setText(getString(R.string.petrochina_card) + "1500元（实际支付：200元）");

            //按钮
            btnStartTask.setVisibility(mOrder.getOrderStatus().equals("已接单") ? View.VISIBLE : View.INVISIBLE);
            btnContainer.setVisibility(!mOrder.getOrderStatus().equals("已接单") ? View.VISIBLE : View.GONE);


        }


    }


    @SuppressLint("SetTextI18n")
    private void setTextStyleSoan(TextView tv, int before, String after) {
        String beforeString = getString(before);
        int beforeStringLength = beforeString.length();
        if (beforeStringLength > 0) {
            String resultString = beforeString + after;
            SpannableString spannableString = new SpannableString(resultString);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, beforeStringLength + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.NORMAL), resultString.length() - after.length(), resultString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            tv.setText(spannableString);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_task_detail;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mOrder = (Order) arguments.getSerializable("order");
    }

    @Override
    public void showTasks(List<Order> tasks) {

    }

    @Override
    public void changeStatusSuccess(int status) {

    }


}
