package com.tengbo.module_order.ui.processing;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adyl.locationLibrary.SignalLocation;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.net.BaseResponse;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.commonlibrary.widget.TitleBar;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.MultiplePictureAdapter;
import com.tengbo.module_order.bean.Dictionary;
import com.tengbo.module_order.bean.Step;
import com.tengbo.module_order.bean.Trouble;
import com.tengbo.module_order.event.UploadTrouble;
import com.tengbo.module_order.net.ApiOrder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.functions.Func1;
import utils.CommonDialog;
import utils.RequestUtils;
import utils.RetrofitUtils;
import utils.ToastUtils;

public class TroubleActivity extends BaseActivity implements View.OnClickListener {

    private Step mStep;
    private TextView tvTroubleType;
    private int PICTURE_COUNT = 3;
    private StepPictureRecyclerView rvPictures;
    private List<MultiplePictureAdapter.Picture> pictures = new ArrayList<>();
    //    private MultiplePictureAdapter mPictureAdapter;
    private String TAG = "TroubleActivity";
    private List<String> troubleTypeStrings;
    private List<Dictionary> troubleTypes;
    private RadioGroup rgNeedCardSupport;
    private RadioGroup rgImpactPlan;
    private EditText etDuration;
    private Trouble mTrouble;
    private BDLocation mLocation;
    private int mChoiseTroubleTypePosition = -1;
    private static final String TYPE_CODE = "ABNORMAL_DRIVING";

    public static void start(Activity activity, Step step) {
        Intent intent = new Intent(activity, TroubleActivity.class);
        intent.putExtra("step", step);
        activity.startActivity(intent);
    }


    @Override
    protected void onIntent(Intent intent) {
        super.onIntent(intent);
        mStep = intent.getParcelableExtra("step");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.order_activity_trouble;
    }

    /**
     *
     */
    @Override
    protected void initView() {
        TitleBar titleBar = findViewById(R.id.titleBar);
        titleBar.setOnBackClickListener(this::finish);
        ((TextView) findViewById(R.id.tv_car_id)).setText(mStep.getVehicleHead());
        ((TextView) findViewById(R.id.tv_order_id)).setText("订单编号：" + mStep.getOrderCode());

        tvTroubleType = findViewById(R.id.tv_exceptions);

        etDuration = findViewById(R.id.et_duration);

        rgNeedCardSupport = findViewById(R.id.rg_need_card_support);
        View llNeedCardSupport = findViewById(R.id.ll_need_card_support);
        rgImpactPlan = findViewById(R.id.rg_impact_plan);
        rgImpactPlan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                llNeedCardSupport.setVisibility(checkedId == R.id.rb_yes ? View.VISIBLE : View.GONE);
            }
        });


        findViewById(R.id.tv_positive).setOnClickListener(this);
        findViewById(R.id.tv_negative).setOnClickListener(this);
        findViewById(R.id.iv_choose_type).setOnClickListener(this);

        getExceptionTypes();

        initRv();

    }

    private void initRv() {
        rvPictures = findViewById(R.id.rv_picture);
        rvPictures.init(this, PICTURE_COUNT, 4);
        rvPictures.setOrderData(mStep.getOrderCode(), mStep.getNodeName());


        MultiplePictureAdapter.Picture addPicture = new MultiplePictureAdapter.Picture(MultiplePictureAdapter.Picture.ADD_PICTURE);
        pictures.add(addPicture);


//        mPictureAdapter = new MultiplePictureAdapter(this, pictures);
//        mPictureAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                MultiplePictureAdapter.Picture picture = (MultiplePictureAdapter.Picture) adapter.getItem(position);
//                if (picture.getItemType() == MultiplePictureAdapter.Picture.ADD_PICTURE) {
//                    addPicture();
//                }
//
//            }
//        });
//        rvPicture.setAdapter(mPictureAdapter);
    }


//    /**
//     * 添加图片
//     */
//    private void addPicture() {
//        if ((mPictureAdapter.getData().size() - 1) < PICTURE_COUNT) {
//            int remainPictureSize = PICTURE_COUNT - (mPictureAdapter.getData().size() - 1);//剩余可以添加的图片数
//            TakePhotoDialogFragment takePhotoDialogFragment = TakePhotoDialogFragment.newInstance(remainPictureSize, false);
//            takePhotoDialogFragment.setOnResultListener(new TakePhotoDialogFragment.TakePhotoCallBack() {
//                @Override
//                public void onSuccess(List<File> files) {
//                    // 图片获取成功，关闭拍照和选择图片对话框
//                    takePhotoDialogFragment.dismissAllowingStateLoss();
//                    for (File file : files) {
//                        MultiplePictureAdapter.Picture picture = new MultiplePictureAdapter.Picture(MultiplePictureAdapter.Picture.SHOW_PICTURE);
//                        picture.setPicturePath(file.getAbsolutePath());
//                        mPictureAdapter.addData(mPictureAdapter.getData().size() - 1, picture);
//                    }
//                }
//
//                @Override
//                public void onError() {
//                    // 关闭拍照和选择图片对话框
//                    takePhotoDialogFragment.dismissAllowingStateLoss();
//                }
//            });
//
//            takePhotoDialogFragment.show(getSupportFragmentManager(), TAG);
//        } else {
//            ToastUtils.show(getApplicationContext(), "最多只能上传" + PICTURE_COUNT + "张照片");
//        }
//
//    }

    private void getExceptionTypes() {
        SignalLocation location = new SignalLocation(getApplicationContext());
        location.setOnLocateListener(new SignalLocation.onLocateListener() {
            @Override
            public void onLocate(BDLocation location) {
                mLocation = location;
            }
        });
        location.startLocate();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("typeCode", TYPE_CODE);
        mSubscriptionManager.add(NetHelper.getInstance().getRetrofit().create(ApiOrder.class)
                .getDictionaryItem(RequestUtils.createRequestBody(jsonObject.toJSONString()))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<List<Dictionary>>(this) {
                    @Override
                    protected void on_next(List<Dictionary> data) {
                        troubleTypes = data;
                        if (troubleTypes != null && troubleTypes.size() > 0) {
                            troubleTypeStrings = new ArrayList<>();
                            for (Dictionary dictionary : troubleTypes) {
                                troubleTypeStrings.add(dictionary.getItemName());
                            }
                        }

                    }
                })
        );
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.tv_positive) {
            submitException();
        } else if (id == R.id.tv_negative) {
            finish();
        }
        //异常类型选择
        else if (id == R.id.iv_choose_type) {
            if (troubleTypeStrings == null) {
                return;
            }
            new MaterialDialog.Builder(this)
                    .items(troubleTypeStrings)
                    .widgetColor(getApplicationContext().getResources().getColor(R.color.base_color))
                    .itemsColor(getApplicationContext().getResources().getColor(R.color.base_color))
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            mChoiseTroubleTypePosition = which;
                            tvTroubleType.setText(text);
                            return true;
                        }
                    }).build().show();
        }
    }


    /**
     * @Desc 提交异常信息
     */
    private void submitException() {
        String duration = etDuration.getText().toString();


        if (mChoiseTroubleTypePosition == -1) {
            ToastUtils.show(getApplicationContext(), "请选择故障类型");
            return;
        }

        if (TextUtils.isEmpty(duration)) {
            ToastUtils.show(getApplicationContext(), "请输入预计耗时");
            return;
        }


        List<String> picturePaths = rvPictures.getPicturePaths();
        if (picturePaths.size() == 0) {
            ToastUtils.show(this, "请至少上传一张照片");
            return;
        }

        assignmentTrouble(Float.parseFloat(duration));
        picturesObservable(picturePaths)
                .compose(RxUtils.handleResult())
                .flatMap(new Func1<List<String>, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(List<String> paths) {
                        return troubleObservable(paths);
                    }
                })
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<Object>(this) {
                    @Override
                    protected void on_next(Object o) {
                        //上传步骤信息成功，通知页面刷新步骤显示
                        EventBus.getDefault().post(new UploadTrouble(true));
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        //上传步骤信息失败，刷新页面
                        dealUploadError(picturePaths);

                    }
                });


    }


    public Observable<BaseResponse<List<String>>> picturesObservable(List<String> paths) {
        MultipartBody multipartBody = RetrofitUtils.createMultipartBody(paths);
        return NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .uploadMultiFiles(multipartBody);

    }

    public Observable<Object> troubleObservable(List<String> fileUrls) {
        List<String> attachs = mTrouble.getAttachs();
        if (attachs != null && attachs.size() > 0) {
            for (int i=0;i<attachs.size();i++) {
            }
        }
        assert mTrouble != null;
        mTrouble.setAttachs(fileUrls);
        return NetHelper.getInstance().getRetrofit()
                .create(ApiOrder.class)
                .addDriverTrouble(mTrouble)
                .compose(RxUtils.handleResult());


    }

    /**
     * 当步骤信息提交失败的情况下，询问用户是否保存信息
     * 如果保存信息后，刷新任务页面，关闭步骤提交页面
     * 下一次点击步骤的时候直接提交
     *
     * @param picturePaths 步骤照片的本地路径
     */
    private void dealUploadError(List<String> picturePaths) {
        new CommonDialog(this)
                .setNotice("信息提交失败,是否保存数据并退出当前页面")
                .setPositiveText("确定")
                .setNegativeText("取消")
                .setOnPositiveClickListener(new CommonDialog.OnPositiveClickListener() {
                    @Override
                    public void onPositiveClick() {
                        assert mTrouble != null;
                        mTrouble.setPictureLocalPaths(picturePaths);
                        mTrouble.save();
                        EventBus.getDefault().post(new UploadTrouble(false));
                        finish();
                    }
                }).show();

    }


    /**
     * 根据rg的选中状态确定故障等级
     *
     * @return 故障等级
     */
    private int getTroubleLevel() {
        int level = 3;
        int checkedIdImpactPlan = rgImpactPlan.getCheckedRadioButtonId();
        int checkedIdNeedCardSupport = rgNeedCardSupport.getCheckedRadioButtonId();
        if (checkedIdImpactPlan == R.id.rb_yes && checkedIdNeedCardSupport == R.id.rb_position) {
            level = 1;
        } else if (checkedIdNeedCardSupport == R.id.rb_navigation) {
            level = 2;
        }
        return level;
    }

    /**
     * @param duration 预计耗时
     */
    private void assignmentTrouble(float duration) {
        mTrouble = new Trouble();
        mTrouble.setOrderCode(mStep.getOrderCode());
        mTrouble.setNodeCode(mStep.getNodeCode());
        mTrouble.setTroubleType(troubleTypes.get(mChoiseTroubleTypePosition).getItemValue());
        mTrouble.setOperateTimeMillis(System.currentTimeMillis());
        mTrouble.setTroubleTimeLength(0);
        mTrouble.setHandlingTimeLength((long) (duration * 3600 * 1000));
        mTrouble.setTroubleLevel(getTroubleLevel());
        mTrouble.setVehicleHead(mStep.getVehicleHead());
        // 经纬度和地址
        if (mLocation != null) {
            mTrouble.setTroubleLongitude(mLocation.getLongitude());
            mTrouble.setTroubleLatitude(mLocation.getLatitude());
            mTrouble.setTroubleLocation(mLocation.getAddrStr());
        }

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
}
