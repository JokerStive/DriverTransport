package com.tengbo.module_order.ui.processing;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.location.BDLocation;
import com.billy.cc.core.component.CC;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.basiclibrary.utils.SelectorFactory;
import com.tengbo.basiclibrary.utils.UiUtils;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.base.BaseMvpFragment;
import com.tengbo.commonlibrary.common.ComponentConfig;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.StepAdapter;
import com.tengbo.module_order.bean.DutyTask;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.bean.Step;
import com.tengbo.module_order.bean.StepOperation;
import com.tengbo.module_order.bean.Trouble;
import com.tengbo.module_order.event.StartOrder;
import com.tengbo.module_order.event.UploadStepFail;
import com.tengbo.module_order.event.UploadStepSuccess;
import com.tengbo.module_order.event.UploadTrouble;
import com.tengbo.module_order.service.LocateService;
import com.tengbo.module_order.utils.LocationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import utils.BeanPropertiesUtil;
import utils.CommonDialog;
import utils.ToastUtils;

/**
 * @author yk
 * @Description 进行中的订单
 */
public class ProcessingOrderFragment extends BaseMvpFragment<ProcessingOrderContract.Presenter> implements ProcessingOrderContract.View, LocateService.OnLocationChangeCallback {
    //    public static final Integer STEP_UPLOAD_SUCCESS = 1000;
//    public static final Integer STEP_UPLOAD_CACHE = 2000;
//    public static final Integer TROUBLE_UPLOAD_CACHE = 3000;
    public static final String KEY_ORDER_CODE = "orderCode";
    public static final String KEY_PLATE_NUMBER = "plateNumber";
    TextView tv_order_id;
    TextView tv_departure;
    ImageView iv_car;
    TextView tv_destination;
    TextView tv_start;
    TextView tv_end;
    LinearLayout ll_card;
    RecyclerView rv_node_action;


    private HashMap<Integer, Step> autoSteps = new HashMap();
    private StepAdapter mAdapter;
    private Order mOrder;
    private View headerView;
    private View ivTroubleWarm;
    private SwipeRefreshLayout srl;
    private View emptyView;
    private MaterialDialog troubleDialog;
    private LocateService locateService;
    private ServiceConnection locateServiceConn;


    public static ProcessingOrderFragment newInstance() {
        return new ProcessingOrderFragment();
    }


    @Override
    protected void getTransferData(Bundle arguments) {

    }


    @Override
    protected void initPresent() {
        mPresent = new ProcessingOrderPresenter();
        mPresent.bindView(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_processing_order;
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresent.getProcessingOrder();
    }


    @Override
    protected void initView() {
        rv_node_action = mRootView.findViewById(R.id.rv_node_action);
        emptyView = mRootView.findViewById(R.id.empty_view);
        emptyView.setOnClickListener(v -> goTask(false));


        headerView = LayoutInflater.from(BaseApplication.get()).inflate(R.layout.head_processing_order, null);

        tv_order_id = headerView.findViewById(R.id.tv_order_id);
        tv_departure = headerView.findViewById(R.id.tv_departure);
        iv_car = headerView.findViewById(R.id.iv_car);
        tv_destination = headerView.findViewById(R.id.tv_destination);
        tv_end = headerView.findViewById(R.id.tv_end);
        tv_start = headerView.findViewById(R.id.tv_start);
        ll_card = headerView.findViewById(R.id.ll_card);
        TextView tvTrouble = headerView.findViewById(R.id.tv_trouble);
        srl = mRootView.findViewById(R.id.srl);
        View llTrouble = headerView.findViewById(R.id.ll_trouble);
        ivTroubleWarm = headerView.findViewById(R.id.iv_trouble_cached);

        StateListDrawable whiteStrokeShape = SelectorFactory.newShapeSelector()
                .setShape(GradientDrawable.OVAL)
                .setStrokeWidth(UiUtils.dp2px(BaseApplication.get(), 1))
                .setDefaultStrokeColor(BaseApplication.get().getResources().getColor(com.tengbo.commonlibrary.R.color.basic_white))
                .create();

        StateListDrawable blueShape = SelectorFactory.newShapeSelector()
                .setDefaultBgColor(BaseApplication.get().getResources().getColor(com.tengbo.commonlibrary.R.color.basic_blue))
                .setCornerRadius(UiUtils.dp2px(BaseApplication.get(), 10))
                .create();
        tv_start.setBackground(whiteStrokeShape);
        tv_end.setBackground(whiteStrokeShape);
        tvTrouble.setBackground(blueShape);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(false);
                mPresent.getProcessingOrder();
            }
        });

        llTrouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ivTroubleWarm.getVisibility() == View.INVISIBLE) {
                    Step latestProcessedStep = mAdapter.getLatestProcessedStep();
                    if (latestProcessedStep == null) {
                        ToastUtils.show(_mActivity.getApplicationContext(), "最后一个执行的步骤不存在");
                    } else {
                        latestProcessedStep.setVehicleHead(mOrder.getVehicleHead());
                        TroubleActivity.start(getActivity(), latestProcessedStep);
                    }

                } else {
                    //从数据库中取出缓存的异常信息，上报
                    new CommonDialog(Objects.requireNonNull(getActivity()))
                            .setNotice("请确认是否提交没有上传成功的异常信息？")
                            .setPositiveText("确定")
                            .setNegativeText("取消")
                            .setOnPositiveClickListener(() -> {
                                List<Trouble> troubles = LitePal.findAll(Trouble.class);
                                if (troubles != null && troubles.size() > 0) {
                                    Trouble trouble = troubles.get(0);
                                    mPresent.uploadCacheTrouble(trouble);
                                }
                            }).show();


                }
            }
        });


        initRv();

        getCachedTrouble();

    }

    private void initRv() {
        rv_node_action.setLayoutManager(new GridLayoutManager(_mActivity, 4));

        mAdapter = new StepAdapter(new ArrayList<>());
        mAdapter.addHeaderView(headerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Step step = (Step) adapter.getItem(position);
            assert step != null;
            step.setPosition(position);
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
        //如果订单异常，直接出弹框
        if (isOrderStatusError()) {
            showErrorDialog();
            return;
        }


        //如果该步骤标记为缓存，就直接上传数据
        if (step.isCached()) {
            dealCacheStep(step);
            return;
        }

        /*
         如果点击的步骤不是自动通过类型并且未完成,才判断是否满足执行条件
         nodeType  节点类型（1系统业务节点2自动触发节点）
         stepStatus 步骤状态（0未执行 1已执行)
         */
        if (step.getNodeType() == 1 && step.getStepStatus() == 0) {
            if (step.getPosition() == 0) {
                stepConfirm(step);
            } else {
                //找到当前点击的步骤以前必须执行的步骤，只有必须执行的步骤已执行后才能执行当前步骤
                // processNecessary 是否必须执行（1必须2可以跳过）
                // nodeType 是否自动通过（1不是 2自动触发）
                // processed 是否已经执行（异常保存的数据也是执行状态）
                for (int prePosition = step.getPosition() - 1; prePosition >= 0; prePosition--) {
                    Step preStep = mAdapter.getData().get(prePosition);
                    int nodeType = preStep.getNodeType();
                    int processNecessary = preStep.getProcessNecessary();
                    if (nodeType == 1 && processNecessary == 1) {
                        if (preStep.isProcessed()) {
                            stepConfirm(step);
                        } else {
                            ToastUtils.show(getContext(), "上一个必须执行的步骤还没有完成");
                        }
                        break;
                    }

                    //如果遍历到第一个步骤仍然是可以跳过的，就直接执行该步骤
                    if (prePosition == 0) {
                        stepConfirm(step);
                    }
                }


            }
        } else if (step.getStepStatus() == 0) {
            ToastUtils.show(getContext(), "该步骤是自动自动触发类型，不需要手动操作");
        } else if (step.getStepStatus() == 1) {
            ToastUtils.show(getContext(), "该步骤已执行");
        }

    }


    /**
     * 当获取到有正在中的订单时，开启定位和上传服务
     * 跟定位服务建立连接，当位置刷新时，通知该页面，根据位置操作自动通过的步骤
     */
    private void startNecessaryService() {
        locateServiceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                locateService = ((LocateService.LocateBinder) service).getService();
                locateService.setOnLocationChangeCallback(ProcessingOrderFragment.this);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        Intent locationIntent = new Intent(_mActivity, LocateService.class);
        locationIntent.putExtra(KEY_ORDER_CODE, mOrder.getOrderCode());
        locationIntent.putExtra(KEY_PLATE_NUMBER, mOrder.getVehicleHead());
        _mActivity.bindService(locationIntent, locateServiceConn, Context.BIND_AUTO_CREATE);


    }

    /**
     * 上传的缓存步骤信息(自动步骤要特殊处理)
     *
     * @param step 步骤
     */
    private void dealCacheStep(Step step) {
        List<StepOperation> stepCaches = LitePal.where("stepSerialNumber = ?", String.valueOf(step.getStepSerialNumber())).limit(1).find(StepOperation.class);
        if (stepCaches.size() > 0) {
            new CommonDialog(Objects.requireNonNull(getActivity()))
                    .setNotice("请确认是否提交没有上传成功的信息？")
                    .setPositiveText("确定")
                    .setNegativeText("取消")
                    .setOnPositiveClickListener(() -> {
                        StepOperation stepCache = stepCaches.get(0);
                        stepCache.setPosition(step.getPosition());
                        int nodeType = step.getNodeType();
                        if (nodeType == 1) {
                            mPresent.uploadCacheStep(stepCache);
                        } else if (nodeType == 2) {
                            mPresent.uploadAutoStep(true, stepCache);
                        }
                    }).show();

        }
    }


    /**
     * 执行步骤操作
     * 1.普通步骤-->弹窗拍照-->数据上链->更新订单的当前步骤
     * 2.特殊步骤-->进入额外的界面-->弹窗拍照-->数据上链->更新订单的当前步骤
     * stepType 1 开始 2开始装货 3装货 4 完成装货 5开始卸货
     * 6卸货 7完成卸货 8开始提柜 9提柜 10完成提柜 11甩柜 12送达
     *
     * @param step 需要操作的步骤
     */
    private void stepConfirm(Step step) {
        int stepType = step.getStepType();
        if (stepType == 3
                || stepType == 6
                || stepType == 9
                || stepType == 11
                ) {
            doSpecialStep(step);
        } else {
            doNormalStep(step);
        }
    }

    /**
     * 处理普通步骤
     *
     * @param step 步骤
     */
    private void doNormalStep(Step step) {
        NormalStepPassFragment dialog = NormalStepPassFragment.newInstance(step);
        assert getFragmentManager() != null;
        dialog.show(getFragmentManager(), "");
    }


    /**
     * 处理特殊步骤
     *
     * @param step 步骤
     */
    private void doSpecialStep(Step step) {
        SpecialStepPassFragment dialog = SpecialStepPassFragment.newInstance(step);
        assert getFragmentManager() != null;
        dialog.show(getFragmentManager(), "");
    }

    /**
     * @Desc 尝试从数据库获取没有提交成功的step和没有提交成功的异常信息
     * 如果有未提交的数据，则刷新界面，展示一个感叹号，点击步骤时直接上传数据
     */
    private void getCachedSteps() {
        List<StepOperation> stepCaches = LitePal.where("orderCode = ?", mOrder.getOrderCode()).find(StepOperation.class);
        for (StepOperation stepCache : stepCaches) {
            mAdapter.setStepCached(stepCache.getPosition());
        }

    }

    /**
     * 尝试从数据库获取未提交成功的trouble新
     * 如果有未提交的异常，则显示一个感叹号，待提交成功后，才能再次提交
     */
    private void getCachedTrouble() {
        List<Trouble> troubles = LitePal.findAll(Trouble.class);
        ivTroubleWarm.setVisibility(troubles.size() > 0 ? View.VISIBLE : View.INVISIBLE);
    }


    /**
     * 如果没有进行中的订单，显示空页面，查询知否值班，否则显示订单信息
     *
     * @param order 订单
     */
    @Override
    public void getProcessingOrderSuccess(Order order) {
        if (order == null) {
            mPresent.getDutyTask();
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
            mOrder = order;
            showOrderInfo();
        }

    }


    /**
     * 位置变化的回调,遍历所有自动通过类型的步骤
     * 算出是否在步骤的范围内，如果是，设置为通过
     *
     * @param location 位置信息
     */
    @Override
    public void onLocationChange(BDLocation location) {
        for (Map.Entry<Integer, Step> entry : autoSteps.entrySet()) {
            Integer position = entry.getKey();
            Step step = entry.getValue();
            double distance = LocationUtils.getDistance(step.getNodeLongitude(), step.getNodeLatitude(), location.getLongitude(), location.getLatitude());
            if (distance <= step.getTriggerDistance()) {
                LogUtil.d("步骤" + step.getStepName() + "在范围内,开始自动上传");
                StepOperation operation = new StepOperation();
                try {
                    BeanPropertiesUtil.copyProperties(step, operation);
                    operation.setOrderCode(mOrder.getOrderCode());
                    operation.setPosition(position);
                    operation.setOperatedTimeLength(0);
                    operation.setOperateTimeMillis(System.currentTimeMillis());
                    mPresent.uploadAutoStep(false, operation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }

    /**
     * 上传缓存步骤数据成功后
     * 1.刷新界面
     * 2.删除数据库数据
     */
    @Override
    public void uploadCacheStepSuccess(StepOperation stepOperation) {
        ToastUtils.show(getContext(), "数据提交成功");
        mAdapter.setStepProcessed(stepOperation.getPosition());
        deleteStepCacheFromDB(stepOperation.getStepSerialNumber());
    }

    private void deleteStepCacheFromDB(int stepNum) {
        LitePal.deleteAll(StepOperation.class, "stepSerialNumber = ?", String.valueOf(stepNum));
    }

    /**
     * 上传缓存的异常信息成功后
     * 1.更新上报异常按钮
     * 2.删除数据库异常信息
     */
    @Override
    public void uploadCacheTroubleSuccess() {
        ivTroubleWarm.setVisibility(View.INVISIBLE);
        LitePal.deleteAll(Trouble.class, "");
    }

    /**
     * 获取步骤成功后，从数据库查询缓存步骤，组装自动上传步骤，开启定位和上传服务
     *
     * @param steps 组装后的步骤
     */
    @Override
    public void showSteps(List<Step> steps) {

        mAdapter.setNewData(steps);

        getCachedSteps();

        checkoutAutoSteps();

        startNecessaryService();

    }


    @Override
    public void getDutyTaskSuccess(DutyTask dutyTask) {
        //如果是值班，通知主页面显示值班
        if (dutyTask.getDutyStatus() == 1) {
            goTask(true);
        }
    }


    /**
     * 检出自动通过的步骤并且没有执行过的步骤，作为独立的操作
     */
    private void checkoutAutoSteps() {
        List<Step> steps = mAdapter.getData();
        for (int position = 0; position < steps.size(); position++) {

            Step step = steps.get(position);
            if (step.getNodeType() == 2 && !step.isProcessed()) {
                autoSteps.put(position, step);
            }
        }
    }

    /**
     * 当前没有正在进行中的订单时，跳转到订单列表页面
     */
    public void goTask(boolean isDuty) {
        CC.obtainBuilder(ComponentConfig.Main.COMPONENT_NAME)
                .setContext(_mActivity.getApplicationContext())
                .setActionName(ComponentConfig.Main.ACTION_CHANGE_TAB)
                .addParam(ComponentConfig.Main.PARAM_TAB_POSITION, isDuty ? -1 : 0)
                .build().call();
    }


    @Override
    public void uploadAutoStepSuccess(boolean isCached, StepOperation operation) {
        int position = operation.getPosition();
        mAdapter.setStepProcessed(position);
        if (isCached) {

            deleteStepCacheFromDB(operation.getStepSerialNumber());
            LogUtil.d("从数据库中删除缓存的自动上传步骤");
        } else {
            Step remove = autoSteps.remove(position);
            LogUtil.d("从autoSteps中移除自动上传步骤");

        }
    }

    @Override
    public void updateTroubleSuccess() {
        if (troubleDialog != null && troubleDialog.isShowing()) {
            troubleDialog.dismiss();
            mOrder.setOrderStatus(4);
        }
    }

    @Override
    public void uploadStepFail(int position) {
        mAdapter.setStepCached(position);
    }


    /**
     * 显示订单信息
     */
    private void showOrderInfo() {
        setText(tv_order_id, R.string.order_id, mOrder.getPlanCode());
        tv_departure.setText(mOrder.getStartNodeName());
        tv_destination.setText(mOrder.getEndNodeName());

        if (isOrderStatusError()) {
            showErrorDialog();
        }
    }


    @SuppressLint("SetTextI18n")
    private void setText(TextView tv, int resId, String after) {
        String before = getString(resId);
        if (!TextUtils.isEmpty(before)) {
            tv.setText(before + after);
        }
    }


    @Subscribe
    public void uploadStepSuccess(UploadStepSuccess uploadStepSuccess) {
        int position = uploadStepSuccess.getPosition();
        mAdapter.setStepProcessed(position);
        LogUtil.d("位置--" + position + "的步骤上传成功，设置为执行状态");
    }

    @Subscribe
    public void uploadStepFail(UploadStepFail uploadStepFail) {
        int position = uploadStepFail.getPosition();
        mAdapter.setStepCached(position);
        LogUtil.d("位置--" + position + "的步骤上传失败，添加感叹号");
    }

    @Subscribe
    public void uploadTrouble(UploadTrouble uploadTrouble) {
        if (uploadTrouble.isSuccess()) {
            setOrderStatusError();
            showErrorDialog();
        } else {
            ivTroubleWarm.setVisibility(View.VISIBLE);
            LogUtil.d("异常上传失败，添加感叹号");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void startOrder(StartOrder startOrder) {
        mPresent.getProcessingOrder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(locateServiceConn!=null){
            _mActivity.unbindService(locateServiceConn);
            locateServiceConn=null;
        }
    }

    /**
     * 订单处于异常状态时，显示弹框
     */
    public void showErrorDialog() {
        troubleDialog = new MaterialDialog.Builder(_mActivity)
                .positiveText("已解决")
                .autoDismiss(false)
                .content(getString(R.string.trouble_solved))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        solveTrouble();
                    }
                })
                .show();
    }

    /**
     * 解决异常，先查询异常再修改异常
     */
    private void solveTrouble() {
        mPresent.getAndUpdateTrouble(mOrder.getOrderCode());
    }


    private boolean isOrderStatusError() {
        return mOrder.getOrderStatus() == 5;
    }

    private void setOrderStatusError() {
        mOrder.setOrderStatus(5);
    }


}
