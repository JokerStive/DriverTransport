package com.tengbo.module_order.ui.history;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.commonlibrary.widget.TitleBar;
import com.tengbo.commonlibrary.widget.takePhoto.imageselector.PreviewActivity;
import com.tengbo.commonlibrary.widget.takePhoto.imageselector.entry.Image;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.StepRecordAdapter;
import com.tengbo.module_order.bean.Node;
import com.tengbo.module_order.bean.Step;
import com.tengbo.module_order.net.ApiOrder;

import java.util.ArrayList;
import java.util.List;

import utils.RequestUtils;

public class StepRecordActivity extends BaseActivity {

    //    private int page = 0;
//    private boolean isEnd;
    private String mOrderCode;
    private StepRecordAdapter mAdapter;

    public static void start(Activity activity, String orderCode) {
        Intent intent = new Intent(activity, StepRecordActivity.class);
        intent.putExtra("orderCode", orderCode);
        activity.startActivity(intent);
    }

    @Override
    protected void onIntent(Intent intent) {
        super.onIntent(intent);
        mOrderCode = getIntent().getStringExtra("orderCode");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.order_activity_step_record;
    }

    @Override
    protected void initView() {
        TitleBar titleBar = findViewById(R.id.titleBar);
        titleBar.setOnBackClickListener(this::finish);
        titleBar.setTitle("纪录");


        RecyclerView rvStepRecord = findViewById(R.id.rv_step_record);
        rvStepRecord.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new StepRecordAdapter(this, new ArrayList<>());

        mAdapter.setOnImageClickListener(new StepRecordAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(int index, List<String> urls) {
                preView(index, urls);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Step step = (Step) adapter.getItem(position);
//                assert step != null;
//                int stepType = step.getStepType();
//                if (stepType == 6
//                        || stepType == 7
//                        || TextUtils.equals("靠台", step.getStepName())
//                        ) {
//                    DebugStepRecordActivity.start(StepRecordActivity.this, step);
//                }
            }
        });
        rvStepRecord.setAdapter(mAdapter);
        getSteps();
    }

    private void preView(int index, List<String> urls) {
        ArrayList<Image> images = new ArrayList<>();
        for (String url : urls) {
            Image image = new Image(url, 0, null, null);
            images.add(image);
        }
        PreviewActivity.openActivity(StepRecordActivity.this, images, index);
    }

    private void getSteps() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderCode", mOrderCode);
        NetHelper.getInstance().getRetrofit().create(ApiOrder.class)
                .getStepHistoryRecord(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new ProgressSubscriber<List<Node>>(this) {
                    @Override
                    protected void on_next(List<Node> nodes) {
                        ArrayList<Step> steps = generateData(nodes);
                        mAdapter.setNewData(steps);
                    }
                });

    }

    private ArrayList<Step> generateData(List<Node> nodes) {
        ArrayList<Step> steps = new ArrayList<>();
        for (Node node : nodes) {
            for (Step step : node.getSteps()) {
                step.setNodeName(node.getNodeName());
                steps.add(step);
            }
        }
        return steps;
    }

}
