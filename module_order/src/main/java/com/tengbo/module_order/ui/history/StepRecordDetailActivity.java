package com.tengbo.module_order.ui.history;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tengbo.basiclibrary.widget.ninegridimageview.NineGridImageView;
import com.tengbo.basiclibrary.widget.ninegridimageview.NineGridImageViewAdapter;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.commonlibrary.widget.TitleBar;
import com.tengbo.commonlibrary.widget.takePhoto.imageselector.PreviewActivity;
import com.tengbo.commonlibrary.widget.takePhoto.imageselector.entry.Image;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.GoodsAdapter;
import com.tengbo.module_order.bean.Goods;
import com.tengbo.module_order.bean.Step;
import com.tengbo.module_order.net.ApiOrder;
import com.tengbo.module_order.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import utils.RequestUtils;

/**
 * @author yk_de
 */
public class StepRecordDetailActivity extends BaseActivity {

    private Step mStep;
    private TextView tvStepName;
    private TextView tvStepTime;
    private TextView tvStepAddress;
    private TextView tvExecutorName;
    private TextView tvExecutorPhone;
    private TextView tvPositionName;
    private TextView tvTransportEnterpriseName;
    private TextView tvVehicleHead;
    private TextView tvVehicleTrailer;
    private TextView tvDriverName;
    private TextView tvDriverPhone;
    private RecyclerView rvGoods;
    private GoodsAdapter mGoodsAdapter;
    private View goodsView;

    public static void start(Activity activity, Step step) {
        Intent intent = new Intent(activity, StepRecordDetailActivity.class);
        intent.putExtra("step", step);
        activity.startActivity(intent);
    }

    @Override
    protected void onIntent(Intent intent) {
        super.onIntent(intent);
        mStep = getIntent().getParcelableExtra("step");
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_step_record_detail;
    }

    /**
     *
     */
    @Override
    protected void initView() {

        View head = LayoutInflater.from(getApplicationContext()).inflate(R.layout.head_step_record_detail, null);
        rvGoods = findViewById(R.id.rv_goods);
        rvGoods.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mGoodsAdapter = new GoodsAdapter(new ArrayList<>());
        mGoodsAdapter.addHeaderView(head);
        rvGoods.setAdapter(mGoodsAdapter);

        TitleBar titleBar = findViewById(R.id.titleBar);
        titleBar.setOnBackClickListener(this::finish);


        goodsView = head.findViewById(R.id.ll_goods);

        tvStepName = head.findViewById(R.id.tv_step_name);
        tvStepTime = head.findViewById(R.id.tv_step_time);
        tvStepAddress = head.findViewById(R.id.tv_step_address);
        tvStepName.setText(buildUpText("步骤名称", mStep.getStepName()));
        tvStepAddress.setText(buildUpText("节点名称", mStep.getNodeName()));
        tvStepTime.setText(buildUpText("执行时间", DateUtils.iso2Utc(mStep.getExecuteTime())));

        tvExecutorName = head.findViewById(R.id.tv_executor_Name);
        tvExecutorPhone = head.findViewById(R.id.tv_executor_phone);
        tvPositionName = head.findViewById(R.id.tv_position_name);
        tvTransportEnterpriseName = head.findViewById(R.id.tv_transport_enterprise_name);
        tvVehicleHead = head.findViewById(R.id.tv_vehicle_head);
        tvVehicleTrailer = head.findViewById(R.id.tv_vehicle_trailer);
        tvDriverName = head.findViewById(R.id.tv_driver_name);
        tvDriverPhone = head.findViewById(R.id.tv_driver_phone);


        NineGridImageView<String> nineGridImageView = head.findViewById(R.id.nine_image);

        nineGridImageView.setAdapter(new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String url) {
                Glide.with(context).load(url).into(imageView);
            }

            @Override
            protected void onItemImageClick(Context context, ImageView imageView, int index, List<String> urls) {
                preView(index, urls);
            }
        });

        nineGridImageView.setImagesData(mStep.getAttachs());


        getStepDetail();

    }

    private String buildUpText(@NonNull String desc, @NonNull String stepName) {
        return desc + "：" + stepName;
    }

    private void getStepDetail() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderCode", mStep.getOrderCode());
        jsonObject.put("nodeCode", mStep.getNodeCode());
        jsonObject.put("nodeNumber", mStep.getNodeNumber());
        jsonObject.put("processNumber", mStep.getProcessNumber());
        jsonObject.put("stepSerialNumber", mStep.getStepSerialNumber());
        mSubscriptionManager.add(NetHelper.getInstance().getRetrofit().create(ApiOrder.class)
                .getStepRecordDetail(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new ProgressSubscriber<Step>() {
                    @Override
                    protected void on_next(Step step) {
                        showStepDetail(step);
                    }
                })

        );

    }

    private void showStepDetail(Step step) {
        tvExecutorName.setText(buildUpText("对接人", step.getExcuterName()));
        tvExecutorPhone.setText(buildUpText("对接人电话", step.getExcuterCellPhone()));
        tvPositionName.setText(buildUpText("作业位置", step.getPositionName()));
        tvTransportEnterpriseName.setText(buildUpText("承运单位", step.getTransportEnterpriseName()));
        tvVehicleHead.setText(buildUpText("车头车牌号", step.getVehicleHead()));
        tvVehicleTrailer.setText(buildUpText("车尾车牌号", step.getVehicleTrailer()));
        tvDriverName.setText(buildUpText("驾驶员姓名", step.getDriverName()));
        tvDriverPhone.setText(buildUpText("驾驶员电话", step.getDriverCellPhone()));

        List<Goods> goods = step.getTargets();
        if (goods == null || goods.size() == 0) {
            goodsView.setVisibility(View.GONE);
        } else {
            mGoodsAdapter.setNewData(step.getTargets());
        }

    }


    private void preView(int index, List<String> urls) {
        ArrayList<Image> images = new ArrayList<>();
        for (String url : urls) {
            Image image = new Image(url, 0, null, null);
            images.add(image);
        }
        PreviewActivity.openActivity(this, images, index);
    }
}
