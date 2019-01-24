package com.tengbo.module_order.ui.processing;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tengbo.commonlibrary.common.Config;
import com.tengbo.commonlibrary.commonBean.FileUrl;
import com.tengbo.commonlibrary.net.BaseResponse;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.SpecialStepAdapter;
import com.tengbo.module_order.bean.Container;
import com.tengbo.module_order.bean.GoodsBatch;
import com.tengbo.module_order.bean.Mission;
import com.tengbo.module_order.bean.Step;
import com.tengbo.module_order.bean.StepOperation;
import com.tengbo.module_order.event.UploadStepFail;
import com.tengbo.module_order.event.UploadStepSuccess;
import com.tengbo.module_order.net.ApiOrder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import utils.BeanPropertiesUtil;
import utils.CommonDialog;
import utils.RequestUtils;
import utils.RetrofitUtils;
import utils.ToastUtils;

public class SpecialStepPassFragment extends DialogFragment implements View.OnClickListener {

    private static final int PICTURE_COUNT = 3;
    private View view;
    private StepPictureRecyclerView rvPictures;
    private Step mStep;
    protected CompositeSubscription mSubscriptionManager = new CompositeSubscription();
    private StepOperation mStepOperate;
    private RecyclerView rvContainers;
    private SpecialStepAdapter specialStepAdapter;
    private boolean showGoods;


    public static SpecialStepPassFragment newInstance(Step step) {
        SpecialStepPassFragment fragment = new SpecialStepPassFragment();
        Bundle args = new Bundle();
        args.putParcelable("step", step);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mStep = arguments.getParcelable("step");
            assert mStep != null;
            int stepType = mStep.getStepType();
            showGoods = (stepType == 3 || stepType == 6);
        }

        getMissions();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
            DisplayMetrics dm = new DisplayMetrics();
            Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(dm);
            Objects.requireNonNull(dialog.getWindow()).setLayout((int) (dm.widthPixels * 0.95), (int) (dm.heightPixels * 0.6));
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscriptionManager.hasSubscriptions() && !mSubscriptionManager.isUnsubscribed()) {
            mSubscriptionManager.unsubscribe();
        }
    }

    private void getMissions() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderCode", mStep.getOrderCode());
        jsonObject.put("nodeCode", mStep.getNodeCode());
        jsonObject.put("stepType", mStep.getStepType());
        mSubscriptionManager.add(NetHelper.getInstance().getRetrofit().create(ApiOrder.class)
                .getOrderMissions(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<List<Mission>>() {
                    @Override
                    protected void on_next(List<Mission> missions) {
                        showMissions(missions);
                    }
                })
        );
    }


    private void showMissions(List<Mission> missions) {
        ArrayList<MultiItemEntity> multiItems = createMultiItems(missions);
        specialStepAdapter.setNewData(multiItems);
        if (showGoods) {
            specialStepAdapter.expandAll();
        }
    }

    private ArrayList<MultiItemEntity> createMultiItems(List<Mission> missions) {
        if (missions.size() == 0) {
            return null;
        }
        HashSet<String> containerCodes = new HashSet<>();
        ArrayList<GoodsBatch> goodsBatches = new ArrayList<>();
        for (int i = 0; i < missions.size(); i++) {
            Mission mission = missions.get(i);
            containerCodes.add(mission.getContainerCode());
            GoodsBatch goodsBatch = new GoodsBatch();
            goodsBatch.setContainerCode(mission.getContainerCode());
            goodsBatch.setGoodsName(mission.getGoodsName());
            goodsBatch.setGoodsBatch(mission.getGoodsBatch());
            goodsBatches.add(goodsBatch);
        }

        ArrayList<MultiItemEntity> multiItemEntities = new ArrayList<>();
        for (String containerCode : containerCodes) {
            Container container = new Container();
            container.setContainerCode(containerCode);
            for (GoodsBatch goodsBatch : goodsBatches) {
                if (goodsBatch.getContainerCode().equals(container.getContainerCode())) {
                    container.addSubItem(goodsBatch);
                }
            }
            multiItemEntities.add(container);

        }

        return multiItemEntities;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
        }

        setStyle(com.tengbo.commonlibrary.R.style.ActionSheetDialogStyle, 0);
        view = inflater.inflate(R.layout.order_fragment_special_step_pass, container);
        initView();
        getMissions();
        return view;
    }

    private void initView() {
        TextView tvStepDesc = view.findViewById(R.id.tv_step_desc);
        tvStepDesc.setText(mStep.getStepDes());

        TextView tvStepName = view.findViewById(R.id.tv_step_name);
        tvStepName.setText(mStep.getStepName());

        TextView tvContactName = view.findViewById(R.id.tv_contact_name);
        tvContactName.setText(mStep.getExcuterName());

        TextView tvContactPhone = view.findViewById(R.id.tv_contact_phone);
        tvContactPhone.setText(mStep.getExcuterCellPhone());

        TextView tvPositionName = view.findViewById(R.id.tv_position_name);
        tvPositionName.setText(mStep.getPositionName());

        rvPictures = view.findViewById(R.id.rv_picture);
        rvContainers = view.findViewById(R.id.rv_container);

        view.findViewById(R.id.tv_positive).setOnClickListener(this);
        view.findViewById(R.id.tv_negative).setOnClickListener(this);

        rvPictures.init(getActivity(), PICTURE_COUNT, 4);
        rvPictures.setOrderData(mStep.getOrderCode(), mStep.getStepName());

        rvContainers.setLayoutManager(new LinearLayoutManager(getContext()));
        if (specialStepAdapter == null) {
            specialStepAdapter = new SpecialStepAdapter(new ArrayList<>(), showGoods);
        }
        rvContainers.setAdapter(specialStepAdapter);

        String title = "装货信息：";
        TextView tvTitle = view.findViewById(R.id.tv_title);
        if (mStep.getStepType() == 6) {
            title = "卸货信息：";
        } else if (mStep.getStepType() == 9) {
            title = "提柜信息：";
        } else if (mStep.getStepType() == 11) {
            title = "甩柜信息：";
        }
        tvTitle.setText(title);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TakeStepPictureActivity.REQUEST_CODE && resultCode == 200 && data != null) {
            String picturePath = data.getStringExtra(TakeStepPictureActivity.STEP_PICTURE_PATH);
            if (!TextUtils.isEmpty(picturePath)) {
                rvPictures.insertItem(picturePath);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_positive) {
            uploadDate();
        } else if (id == R.id.tv_negative) {
            dismiss();
        }
    }

    private void uploadDate() {
        if (specialStepAdapter.createSelectedData().size() == 0) {
            ToastUtils.show(getContext(), "请至少选择一个数据");
            return;
        }
        List<String> picturePaths = rvPictures.getPicturePaths();
        if (picturePaths.size() == 0) {
            ToastUtils.show(getContext(), "请至少上传一张照片");
            return;
        }

        assignmentOperation();

        mSubscriptionManager.add(picturesObservable(picturePaths)
                .compose(RxUtils.handleResult())
                .flatMap(new Func1<List<String>, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(List<String> paths) {
                        return stepInfoObservable(paths);
                    }
                })
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<Object>(getActivity()) {
                    @Override
                    protected void on_next(Object o) {
                        //上传步骤信息成功，通知页面刷新步骤显示
                        EventBus.getDefault().post(new UploadStepSuccess(mStepOperate.getPosition()));
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        //上传步骤信息失败，刷新页面
                        dealUploadError(picturePaths);

                    }
                }));


    }

    /**
     * 当步骤信息提交失败的情况下，询问用户是否保存信息
     * 如果保存信息后，刷新任务页面，关闭步骤提交页面
     * 下一次点击步骤的时候直接提交
     *
     * @param picturePaths 步骤照片的本地路径
     */
    private void dealUploadError(List<String> picturePaths) {
        new CommonDialog(Objects.requireNonNull(getActivity()))
                .setNotice("信息提交失败,是否保存数据并退出当前页面")
                .setPositiveText("确定")
                .setNegativeText("取消")
                .setOnPositiveClickListener(() -> {
                    assert mStepOperate != null;
                    mStepOperate.setPictureLocalPaths(picturePaths);
                    mStepOperate.save();
                    EventBus.getDefault().post(new UploadStepFail(mStepOperate.getPosition()));
                    dismiss();
                }).show();

    }


    public Observable<BaseResponse<List<String>>> picturesObservable(List<String> paths) {
        MultipartBody multipartBody = RetrofitUtils.createMultipartBody(paths);
        return NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .uploadMultiFiles( multipartBody);

    }


    public Observable<Object> stepInfoObservable(List<String> fileUrls) {
        assert mStepOperate != null;
        mStepOperate.setAttachs(fileUrls);
        return NetHelper.getInstance().getRetrofit()
                .create(ApiOrder.class)
                .setOrderStepOperation(mStepOperate)
                .compose(RxUtils.handleResult());


    }

    private void assignmentOperation() {
        mStepOperate = new StepOperation();
        try {
            BeanPropertiesUtil.copyProperties(mStep, mStepOperate);
            mStepOperate.setStepType(mStep.getStepType());
            mStepOperate.setOperatedTimeLength(0);
            mStepOperate.setContainers(specialStepAdapter.createSelectedData());
            mStepOperate.setOperateTimeMillis(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        mStepOperate.setOrderCode(mStep.getOrderCode());
//        mStepOperate.setNodeCode(mStep.getNodeCode());
//        mStepOperate.setNodeNumber(mStep.getNodeNumber());
//        mStepOperate.setProcessNumber(mStep.getProcessNumber());
//        mStepOperate.setStepSerialNumber(mStep.getStepSerialNumber());
//        mStepOperate.setStepType(mStep.getStepType());
//        mStepOperate.setOperatedTimeLength(0);
//        mStepOperate.setContainers(specialStepAdapter.createSelectedData());
//        mStepOperate.setOperateTimeMillis(System.currentTimeMillis());
    }
}
