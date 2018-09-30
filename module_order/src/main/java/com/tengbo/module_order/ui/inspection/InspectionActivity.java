package com.tengbo.module_order.ui.inspection;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.tengbo.basiclibrary.utils.SelectorFactory;
import com.tengbo.basiclibrary.utils.UiUtils;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.common.ComponentConfig;
//import com.adyl.locationLibrary.LocateService;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.InspectionAdapter;
import com.tengbo.module_order.bean.Inspection;
//import com.tengbo.module_order.service.UploadLocationService;

import utils.permission.PermissionManager;

import com.tengbo.commonlibrary.widget.TitleBar;
import com.tengbo.module_order.service.LocateService;
import com.tengbo.module_order.service.UploadLocationService;

import java.util.ArrayList;
import java.util.List;

public class InspectionActivity extends BaseActivity {
    TitleBar titleBar;
    TextView tv_driver_name;
    TextView tv_car_id;
    RecyclerView rv_inspection;
    TextView tv_choose_all;
    TextView tv_start_task;
    TextView tv_not_check;

    private List<Inspection> inspections = new ArrayList<>();
    private InspectionAdapter mAdapter;
    private RelativeLayout rl_bottom_bar;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, InspectionActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void initView() {
        titleBar = findViewById(R.id.titleBar);
        titleBar.setOnBackClickListener(this::finish);

        tv_driver_name = findViewById(R.id.tv_driver_name);
        tv_car_id = findViewById(R.id.tv_car_id);
        rv_inspection = findViewById(R.id.rv_inspection);
        tv_choose_all = findViewById(R.id.tv_choose_all);
        tv_start_task = findViewById(R.id.tv_start_task);
        tv_not_check = findViewById(R.id.tv_not_check);
        rl_bottom_bar = findViewById(R.id.rl_bottom_bar);

        titleBar.setOnBackClickListener(this::pop);


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

        tv_choose_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean selected = tv_choose_all.isSelected();
                tv_choose_all.setSelected(!selected);
                mAdapter.chooseAll(!selected);
            }
        });

        tv_not_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTask();
            }
        });

        tv_start_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTask();
            }
        });

        mAdapter = new InspectionAdapter(inspections);
        rv_inspection.setLayoutManager(new LinearLayoutManager(this));
        rv_inspection.setAdapter(mAdapter);


        getInspections();
    }


    /**
     * 开始任务
     */
    private void startTask() {

    }

    /**
     * 权限通过后正式开始任务
     */
    private void onPermissionGranted() {
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
    }

    private void getInspections() {
        for (int i = 0; i < 30; i++) {
            Inspection inspection = new Inspection();
            inspections.add(inspection);
        }
        mAdapter.replaceAll(inspections);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_inspection;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.getInstance(BaseApplication.get()).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
