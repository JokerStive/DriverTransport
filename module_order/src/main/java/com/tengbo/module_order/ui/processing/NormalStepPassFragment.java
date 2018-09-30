package com.tengbo.module_order.ui.processing;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Step;
import com.tengbo.module_order.bean.StepPicture;

import java.util.Objects;

public class NormalStepPassFragment extends DialogFragment implements View.OnClickListener {

    private static final int PICTURE_COUNT = 3;
    private View view;
    private TextView tvStepDesc;
    private StepPictureRecyclerView stepRv;
    private Step mStep;


    public static NormalStepPassFragment newInstance(Step step) {
        NormalStepPassFragment fragment = new NormalStepPassFragment();
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
        if (window != null)

            window.setGravity(Gravity.CENTER);


        setStyle(com.tengbo.commonlibrary.R.style.ActionSheetDialogStyle, 0);
        view = inflater.inflate(R.layout.order_fragment_normal_step_pass, container);
        initView();
        return view;
    }

    private void initView() {
        tvStepDesc = view.findViewById(R.id.tv_step_desc);
        stepRv = view.findViewById(R.id.rv_picture);
        view.findViewById(R.id.tv_positive).setOnClickListener(this);
        view.findViewById(R.id.tv_negative).setOnClickListener(this);

        stepRv.init(getActivity(), 4, 4);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TakeStepPictureActivity.REQUEST_CODE && resultCode == 200 && data != null) {
            StepPicture stepPicture = data.getParcelableExtra(TakeStepPictureActivity.STEP_PICTURE);
            if (stepPicture != null) {
                stepRv.insertItem(stepPicture);
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

    }
}
