package com.tengbo.module_order.ui.inspection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.billy.cc.core.component.CC;
import com.tengbo.basiclibrary.utils.SelectorFactory;
import com.tengbo.basiclibrary.utils.UiUtils;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.common.ComponentConfig;
import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.commonlibrary.widget.TitleBar;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.InspectionAdapter;
import com.tengbo.module_order.bean.CheckRecord;
import com.tengbo.module_order.bean.Dictionary;
import com.tengbo.module_order.event.StartOrder;
import com.tengbo.module_order.net.ApiOrder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import utils.RequestUtils;


public class InspectionActivity extends BaseActivity {
    TitleBar titleBar;
    TextView tv_driver_name;
    TextView tv_car_id;
    RecyclerView rv_inspection;
    TextView tv_start_task;
    TextView tv_not_check;

    private InspectionAdapter mAdapter;
    private RelativeLayout rl_bottom_bar;
    private String mOrderCode;
    private String mCarId;
    private int mInspectionStatus = 2;//检查状态（1未检查 2已检查通过3已检查未通过）
    private static final String TYPE_CODE = "THREE_CHECK_ITEMS_SETTING_PARAMETER";
    private static final String TAG = "inspectionActivity";
    protected CompositeSubscription mSubscriptionManager = new CompositeSubscription();

    public static void start(Activity activity, String orderCode, String carId) {
        Intent intent = new Intent(activity, InspectionActivity.class);
        intent.putExtra("orderCode", orderCode);
        intent.putExtra("carId", carId);
        activity.startActivity(intent);
    }

    @Override
    protected void onIntent(Intent intent) {
        super.onIntent(intent);
        mOrderCode = getIntent().getStringExtra("orderCode");
        mCarId = getIntent().getStringExtra("carId");

    }

    @Override
    protected void initView() {
        titleBar = findViewById(R.id.titleBar);
        titleBar.setOnBackClickListener(this::finish);

        tv_driver_name = findViewById(R.id.tv_driver_name);
        tv_car_id = findViewById(R.id.tv_car_id);
        tv_driver_name.setText(User.getName());
        tv_car_id.setText(mCarId);

        rv_inspection = findViewById(R.id.rv_inspection);
        tv_start_task = findViewById(R.id.tv_start_task);
        tv_not_check = findViewById(R.id.tv_not_check);
        rl_bottom_bar = findViewById(R.id.rl_bottom_bar);

        titleBar.setOnBackClickListener(this::finish);


        tv_start_task.setBackground(SelectorFactory.newShapeSelector()
                .setShape(GradientDrawable.RECTANGLE)
                .setDefaultBgColor(BaseApplication.get().getResources().getColor(com.tengbo.commonlibrary.R.color.basic_blue))
                .setCornerRadius(UiUtils.dp2px(BaseApplication.get(), 20))
                .create());

        tv_not_check.setBackground(SelectorFactory.newShapeSelector()
                .setShape(GradientDrawable.RECTANGLE)
                .setStrokeWidth(UiUtils.dp2px(BaseApplication.get(), 1))
                .setDefaultStrokeColor(BaseApplication.get().getResources().getColor(com.tengbo.commonlibrary.R.color.basic_blue))
                .setCornerRadius(UiUtils.dp2px(BaseApplication.get(), 20))
                .create());

        rl_bottom_bar.setBackground(SelectorFactory.newShapeSelector()
                .setShape(GradientDrawable.RECTANGLE)
                .setDefaultBgColor(Color.parseColor("#f3f3f3"))
                .create());


        tv_not_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInspectionStatus = 1;
                startTask();
            }
        });

        tv_start_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTask();
            }
        });

        mAdapter = new InspectionAdapter(new ArrayList<>());
        mAdapter.setOnNegativeClickListener(new InspectionAdapter.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
                InspectionUploadFragment.newInstance(mCarId).show(getSupportFragmentManager(), TAG);
            }
        });
        rv_inspection.setLayoutManager(new LinearLayoutManager(this));
        rv_inspection.setAdapter(mAdapter);


        getThreeCheck();
    }


    /**
     * 获取三检项
     */
    private void getThreeCheck() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("typeCode", TYPE_CODE);
        mSubscriptionManager.add(NetHelper.getInstance().getRetrofit().create(ApiOrder.class)
                .getDictionaryItem(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new ProgressSubscriber<List<Dictionary>>() {
                    @Override
                    protected void on_next(List<Dictionary> data) {
                        mAdapter.setNewData(data);
                    }
                })

        );
    }


    /**
     * 开始任务
     */
    private void startTask() {
        mSubscriptionManager.add(adCheckRecordObservable(mInspectionStatus)
                .flatMap(new Func1<Object, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(Object o) {
                        return changeOrderStatus();
                    }
                })
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<Object>() {
                    @Override
                    protected void on_next(Object o) {
                        goProcessingOrderFragment();

                    }
                })

        );
    }

    public Observable<Object> adCheckRecordObservable(int status) {
        CheckRecord record = new CheckRecord();
        record.setInspectionStatus(status);
        record.setPlateNumber(mCarId);
        record.setOperatorId(User.getIdNumber());
        String url = "http://192.168.1.204:9008/vehicle/addvehicleCheckRecord";
        return NetHelper.getInstance().getRetrofit()
                .create(ApiOrder.class)
                .addCheckRecord(record)
                .compose(RxUtils.handleResult());


    }

    public Observable<Object> changeOrderStatus() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderCode", mOrderCode);
        jsonObject.put("orderStatus", 4);
        return NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .setDriverOrderStatus(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.handleResult());
    }


    public void goProcessingOrderFragment() {
        CC.obtainBuilder(ComponentConfig.Main.COMPONENT_NAME).
                setContext(this)
                .addParam(ComponentConfig.Main.PARAM_TAB_POSITION, 2)
                .setActionName(ComponentConfig.Main.ACTION_OPEN_MAIN_ACTIVITY)
                .build().call();

        CC.obtainBuilder(ComponentConfig.Main.COMPONENT_NAME)
                .setContext(this)
                .setActionName(ComponentConfig.Main.ACTION_CHANGE_TAB)
                .addParam(ComponentConfig.Main.PARAM_TAB_POSITION, 2)
                .build().call();

        EventBus.getDefault().post(new StartOrder());

//        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_inspection;
    }

    public void uploadCheckDesc() {
        tv_not_check.setVisibility(View.GONE);
        mInspectionStatus = 3;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscriptionManager.hasSubscriptions() && mSubscriptionManager.isUnsubscribed()) {
            mSubscriptionManager.unsubscribe();
        }
    }
}


