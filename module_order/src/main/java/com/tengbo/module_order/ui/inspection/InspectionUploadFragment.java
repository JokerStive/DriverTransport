package com.tengbo.module_order.ui.inspection;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.tengbo.commonlibrary.common.User;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.commonlibrary.net.ProgressSubscriber;
import com.tengbo.commonlibrary.net.RxUtils;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.CheckRecord;
import com.tengbo.module_order.net.ApiOrder;

import java.util.Objects;

import rx.Observable;
import rx.subscriptions.CompositeSubscription;
import utils.ToastUtils;

public class InspectionUploadFragment extends DialogFragment implements View.OnClickListener {

    private View view;
    protected CompositeSubscription mSubscriptionManager = new CompositeSubscription();
    private EditText etInspectionDesc;
    private String mCarId;


    public static InspectionUploadFragment newInstance(String carId) {
        InspectionUploadFragment fragment = new InspectionUploadFragment();
        Bundle args = new Bundle();
        args.putString("carId", carId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        mCarId = getArguments().getString("carId");

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
        view = inflater.inflate(R.layout.fragment_inspection_feedback, container);
        initView();
        return view;
    }


    private void initView() {
        etInspectionDesc = view.findViewById(R.id.et_inspection_desc);
        view.findViewById(R.id.tv_positive).setOnClickListener(this);
        view.findViewById(R.id.tv_negative).setOnClickListener(this);
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
        String desc = etInspectionDesc.getText().toString();
        if (TextUtils.isEmpty(desc)) {
            ToastUtils.show(getContext(), "请输入问题");
            return;
        }
        mSubscriptionManager.add(checkRecordObservable(desc)
                .compose(RxUtils.applySchedule())
                .subscribe(new ProgressSubscriber<Object>(getActivity()) {
                    @Override
                    protected void on_next(Object o) {
                        InspectionActivity  activity = (InspectionActivity) getActivity();
                        assert activity != null;
                        activity.uploadCheckDesc();
                        dismiss();
                    }
                }));


    }


    public Observable<Object> checkRecordObservable(String desc) {
        CheckRecord record = new CheckRecord();
        record.setInspectionDes(desc);
        record.setInspectionStatus(3);
        record.setPlateNumber(mCarId);
        record.setOperatorId(User.getIdNumber());
        return NetHelper.getInstance().getRetrofit()
                .create(ApiOrder.class)
                .addCheckRecord(record)
                .compose(RxUtils.handleResult());


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscriptionManager.hasSubscriptions() && mSubscriptionManager.isUnsubscribed()) {
            mSubscriptionManager.unsubscribe();
        }
    }
}
