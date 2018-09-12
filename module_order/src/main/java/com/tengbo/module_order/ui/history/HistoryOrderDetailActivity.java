package com.tengbo.module_order.ui.history;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.RecordAdapter;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.bean.Record;

import java.util.ArrayList;
import java.util.List;

import widget.TitleBar;


/**
 * @author Wang Chenchen
 * @date 2018-8-20 18:04:59
 * 历史订单Fragment
 */
public class HistoryOrderDetailActivity extends BaseActivity {


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
    ImageView ivHint;
    RecyclerView rvProcessingList;


    boolean isShowBar = true;
//    private ViewWrapper mViewWraper;
    private RecordAdapter mAdapter;
    private View mHeader;


    public static void start(Activity activity, Order order) {
        Intent intent = new Intent(activity, HistoryOrderDetailActivity.class);
        intent.putExtra("order", order);
        activity.startActivity(intent);

    }


    @Override
    protected void initView() {
        mHeader = LayoutInflater.from(getApplicationContext()).inflate(R.layout.order_header_history_detail, null);
        titleBar = findViewById(R.id.titleBar);
        tvOrderId = mHeader.findViewById(R.id.tv_order_id);
        tvDriverName = mHeader.findViewById(R.id.tv_driver_name);
        tvCarInfo = mHeader.findViewById(R.id.tv_car_info);
        tvDriverName1 = mHeader.findViewById(R.id.tv_driver_name1);
        tvCarLicense = mHeader.findViewById(R.id.tv_car_license);
        tvBoxNumber = mHeader.findViewById(R.id.tv_box_number);
        tvStartTime = mHeader.findViewById(R.id.tv_start_time);
        tvDeparture = mHeader.findViewById(R.id.tv_departure);
        tvDepartureContact = mHeader.findViewById(R.id.tv_departure_contact);
        tvDestination = mHeader.findViewById(R.id.tv_destination);
        tvDestinationContact = mHeader.findViewById(R.id.tv_destination_contact);
        tvGoodsName = mHeader.findViewById(R.id.tv_goods_name);
        tvGoodsWeight = mHeader.findViewById(R.id.tv_goods_weight);
        tvGPSNumber = mHeader.findViewById(R.id.tv_GPS_number);
        tvGoodsFlight = mHeader.findViewById(R.id.tv_goods_flight);
        tvShippingTime = mHeader.findViewById(R.id.tv_shipping_time);
        tvStartTime = mHeader.findViewById(R.id.tv_start_time);
        tvArriveTime = mHeader.findViewById(R.id.tv_arrive_time);
        tvDriverFee = mHeader.findViewById(R.id.tv_driver_fee);
        tvDriverPreFee = mHeader.findViewById(R.id.tv_driver_preFee);
        tvDriverActualFee = mHeader.findViewById(R.id.tv_driver_actualFee);
        tvPetroChina = mHeader.findViewById(R.id.tv_PetroChina);
        tvSinopec = mHeader.findViewById(R.id.tv_Sinopec);
        btnStartTask = mHeader.findViewById(R.id.btn_start_task);
        ivHint = mHeader.findViewById(R.id.iv_hint);
        rvProcessingList = findViewById(R.id.rv_processing_list);

        titleBar.setOnBackClickListener(this::finish);
        ivHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowBar) {
                    hintBar();
                } else {
                    showBar();
                }
            }
        });

        getRecords();
    }

    private void hintBar() {
        mAdapter.setIsHint(true);
        isShowBar = false;
//        rvProcessingList.scrollToPosition(0);
//        ObjectAnimator animator = ObjectAnimator.ofInt(mViewWraper, "height", 500, 0)
//                .setDuration(3000);
//        animator.start();
    }

    private void showBar() {
        mAdapter.setIsHint(false);
        isShowBar = true;

//        rvProcessingList.setMinimumHeight();
//        frameLayout.setVisibility(View.VISIBLE);
//        ObjectAnimator animator = ObjectAnimator.ofInt(mViewWraper, "height", 0, 500)
//                .setDuration(3000);
//        animator.start();
    }

//
//    private class ViewWrapper {
//        private View target;
//        private int oldHeight;
//
//        ViewWrapper(View view) {
//            target = view;
//            oldHeight = view.getLayoutParams().height;
//        }
//
//        public void setHeight(int height) {
//            target.getLayoutParams().height = height;
//            target.requestLayout();
//        }
//
//        public int getOldHeight() {
//            return oldHeight;
//        }
//
//    }

    private void getRecords() {
        List<Record> records = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Record record = new Record();
            records.add(record);
        }

        rvProcessingList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new RecordAdapter(records);
        mAdapter.addHeaderView(mHeader);
        rvProcessingList.setAdapter(mAdapter);

//        mViewWraper = new ViewWrapper(rvProcessingList);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_order_detail;
    }


}
