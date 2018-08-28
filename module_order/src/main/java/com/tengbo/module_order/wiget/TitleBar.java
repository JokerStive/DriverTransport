package com.tengbo.module_order.wiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tengbo.module_order.R;


/**
 * @author yk
 * @Description
 */
public class TitleBar extends RelativeLayout implements View.OnClickListener {

    private boolean mShowBack;
    private String mTitle;
    private OnBackClickListener listener;
    private TextView tvTitle;

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        mTitle = array.getString(R.styleable.TitleBar_title);
        mShowBack = array.getBoolean(R.styleable.TitleBar_showBack, true);
        init(context);
        array.recycle();
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.title_bar, this);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(mTitle);
        ImageView mIvBack = view.findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mIvBack.setVisibility(mShowBack?VISIBLE:INVISIBLE);
    }


    public void setTitle(String title) {
        tvTitle.setText(title);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_back) {
            if (listener != null) {
                listener.onBackClick();
            }

        }
    }


    public void setOnBackClickListener(OnBackClickListener listener) {
        this.listener = listener;
    }

    public interface OnBackClickListener {
        void onBackClick();
    }


}
