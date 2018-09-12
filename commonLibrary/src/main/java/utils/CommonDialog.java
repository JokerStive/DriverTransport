package utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tengbo.commonlibrary.R;

public class CommonDialog extends Dialog {
    private Context content;
    private TextView tvNotice;
    private TextView tvPositive;
    private TextView tvNegative;
    private FrameLayout rlNegative;
    private FrameLayout rlPositive;
    private OnPositiveClickListener positiveListener;
    private OnNegativeClickListener negativeListener;

    public CommonDialog(@NonNull Context context) {
        super(context);
        this.content = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(content).inflate(R.layout.layout_metri_dialog, null);
        tvNotice = view.findViewById(R.id.tv_notice);
        tvPositive = view.findViewById(R.id.tv_positive);
        tvNegative = view.findViewById(R.id.tv_negative);
        rlPositive = view.findViewById(R.id.rl_positive);
        rlNegative = view.findViewById(R.id.rl_negative);
        tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (positiveListener != null) {
                    positiveListener.onPositiveClick();
                }
                dismiss();
            }
        });


        tvNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (negativeListener != null) {
                    negativeListener.onPNegativeClick();
                }
                dismiss();
            }
        });

        setCancelable(true);
        setContentView(view);
    }


    public CommonDialog setNotice(String notice) {
        tvNotice.setText(notice);
        return this;
    }

    public CommonDialog setPositiveText(String positiveText) {
        tvPositive.setText(positiveText);
        rlPositive.setVisibility(View.VISIBLE);
        return this;
    }

    public CommonDialog setNegativeText(String negativeText) {
        tvNegative.setText(negativeText);
        rlNegative.setVisibility(View.VISIBLE);
        return this;
    }

    public interface OnPositiveClickListener {
        void onPositiveClick();
    }

    public interface OnNegativeClickListener {
        void onPNegativeClick();
    }

    public CommonDialog setOnPositiveClickListener(OnPositiveClickListener listener) {
        this.positiveListener = listener;
        return this;
    }

    public CommonDialog setOnNegativeClickListener(OnNegativeClickListener listener) {
        this.negativeListener = listener;
        return this;
    }


}
