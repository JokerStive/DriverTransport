package com.tengbo.module_order.ui.duty;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tengbo.basiclibrary.utils.SelectorFactory;
import com.tengbo.basiclibrary.utils.UiUtils;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.common.Config;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.DutyRecord;
import com.tengbo.module_order.bean.StepOperation;
import com.tengbo.module_order.bean.Task;
import com.tengbo.module_order.net.ApiOrder;
import com.tengbo.module_order.ui.processing.ProcessingOrderFragment;
import com.tengbo.module_order.ui.processing.StepPictureRecyclerView;
import com.tengbo.module_order.ui.processing.TakeStepPictureActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Objects;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import utils.BeanPropertiesUtil;
import utils.CommonDialog;
import utils.RetrofitUtils;
import utils.ToastUtils;

/**
 * @author yk_de
 */
public class AddDutyTaskFragment extends DialogFragment implements View.OnClickListener {

    private static final int PICTURE_COUNT = 3;
    private View view;
    private StepPictureRecyclerView stepRv;
    private Task mTask;
    protected CompositeSubscription mSubscriptionManager = new CompositeSubscription();
    private View ivEdit;
    private EditText etContainerCode;
    private boolean mIsAdd = true;
    private RadioGroup rg;
    private View positive;
    private LinearLayout llAddress;
    private EditText etAddress;

    private int mCheckedId;

    public static AddDutyTaskFragment newInstance(boolean isAdd, Task task) {
        AddDutyTaskFragment fragment = new AddDutyTaskFragment();
        Bundle args = new Bundle();
        args.putParcelable("task", task);
        args.putBoolean("isAdd", isAdd);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTask = arguments.getParcelable("task");
            mIsAdd = arguments.getBoolean("isAdd");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
            DisplayMetrics dm = new DisplayMetrics();
            Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(dm);
            Objects.requireNonNull(dialog.getWindow()).setLayout((int) (dm.widthPixels * 0.95), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
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
        view = inflater.inflate(R.layout.order_fragment_add_duty_task, container);
        initView();
        return view;
    }

    private void initView() {


        etContainerCode = view.findViewById(R.id.et_container_code);
        etContainerCode.setEnabled(mIsAdd);
        if (!mIsAdd) {
            etContainerCode.setText(mTask.getContainerCode());
        }

        ivEdit = view.findViewById(R.id.iv_edit);
        ivEdit.setVisibility(!mIsAdd ? View.VISIBLE : View.GONE);
        ivEdit.setOnClickListener(this);

        //获取rb
        rg = view.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mCheckedId = checkedId;
            }
        });
        RadioButton rb_1 = view.findViewById(R.id.rb_1);
        rb_1.setChecked(mIsAdd);

        RadioButton rb_4 = view.findViewById(R.id.rb_4);
        rb_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                llAddress.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        positive = view.findViewById(R.id.tv_positive);
        positive.setEnabled(mIsAdd);
        positive.setOnClickListener(this);
        view.findViewById(R.id.tv_negative).setOnClickListener(this);

        stepRv = view.findViewById(R.id.rv_picture);
        stepRv.init(getActivity(), PICTURE_COUNT, 4);


        llAddress = view.findViewById(R.id.ll_address);
        etAddress = view.findViewById(R.id.et_address);

        //如果是更新值班，先获取值班纪录
        if (!mIsAdd) {
            Task task = new Task();
            task.setTaskId(mTask.getTaskId());
            mSubscriptionManager.add(NetHelper.getInstance().getRetrofit().create(ApiOrder.class)
                    .getDutyRecord(task)
                    .compose(RxUtils.applySchedule())
                    .compose(RxUtils.handleResult())
                    .subscribe(new ProgressSubscriber<DutyRecord>(getActivity()) {
                        @Override
                        protected void on_next(DutyRecord dutyRecord) {
                            List<Task> records = dutyRecord.getRecords();
                            if (records != null && records.size() != 0) {
                                setRbEnable(records);
                            }
                        }
                    })

            );
        }
    }

    /**设置已经操作过的动作为不可操作
     * @param records
     */
    private void setRbEnable(List<Task> records) {
        positive.setEnabled(true);
        for (int j = 0; j < records.size(); j++) {
            Task task = records.get(j);
            for (int i = 0; i < rg.getChildCount(); i++) {
                RadioButton child = (RadioButton) rg.getChildAt(i);
                if (TextUtils.equals(child.getTag().toString(), task.getOperationType())) {
                    child.setSelected(true);
                    child.setEnabled(false);
                }
            }

        }
    }

    private String getCheckedRbTag() {
        String result = null;
        if (mCheckedId != 0) {
            RadioButton radioButton = view.findViewById(mCheckedId);
            if (radioButton != null) {
                result = radioButton.getTag().toString();
            }
        }
        return result;

    }


    int successCode  = 200;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TakeStepPictureActivity.REQUEST_CODE && resultCode == successCode && data != null) {
            String picturePath = data.getStringExtra(TakeStepPictureActivity.STEP_PICTURE_PATH);
            if (!TextUtils.isEmpty(picturePath)) {
                stepRv.insertItem(picturePath);
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
        } else if (id == R.id.iv_edit) {
            etContainerCode.setEnabled(!etContainerCode.isEnabled());
        }
    }

    private void uploadDate() {
        String containerCode = etContainerCode.getText().toString();
        if (TextUtils.isEmpty(containerCode)) {
            ToastUtils.show(getContext(), "请输入柜号");
            return;
        }

        String tag = getCheckedRbTag();
        String four ="4";
        if (TextUtils.isEmpty(tag)) {
            ToastUtils.show(getContext(), "请选择一个动作");
            return;
        } else if (four.equals(tag) && TextUtils.isEmpty(etAddress.getText().toString())) {
            ToastUtils.show(getContext(), "请输入甩重柜地址");
            return;
        }

        List<String> picturePaths = stepRv.getPicturePaths();
        if (picturePaths.size() == 0) {
            ToastUtils.show(getContext(), "请至少上传一张照片");
            return;
        }


        assignmentOperation();
        mSubscriptionManager.add(picturesObservable(picturePaths)
                .flatMap((Func1<List<String>, Observable<Object>>) this::stepInfoObservable)
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<Object>(getActivity()) {
                    @Override
                    protected void on_next(Object o) {
                        EventBus.getDefault().post(DutyListFragment.REFRESH);
                        dismiss();

                    }
                }));
    }


    public Observable<List<String>> picturesObservable(List<String> paths) {
        MultipartBody multipartBody = RetrofitUtils.createMultipartBody(paths);
        return NetHelper.getInstance()
                .getRetrofit().create(ApiOrder.class)
                .uploadMultiFiles(multipartBody)
                .compose(RxUtils.handleResult());

    }


    public Observable<Object> stepInfoObservable(List<String> fileUrls) {
        assert mTask != null;
        mTask.setAttachs(fileUrls);

        if (mIsAdd) {
            return NetHelper.getInstance().getRetrofit()
                    .create(ApiOrder.class)
                    .addDutyTasks(mTask)
                    .compose(RxUtils.handleResult());
        } else {
            return NetHelper.getInstance().getRetrofit()
                    .create(ApiOrder.class)
                    .updateDutyTasks(mTask)
                    .compose(RxUtils.handleResult());
        }


    }

    private void assignmentOperation() {
        assert mTask != null;
        mTask.setRecordTimeLength(0);
        mTask.setOperationType(getCheckedRbTag());
        mTask.setDropAddress(etAddress.getText().toString());
        mTask.setContainerCode(etContainerCode.getText().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscriptionManager.hasSubscriptions() && !mSubscriptionManager.isUnsubscribed()) {
            mSubscriptionManager.unsubscribe();

        }
    }
}
