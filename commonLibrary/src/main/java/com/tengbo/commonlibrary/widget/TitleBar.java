package com.tengbo.commonlibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tengbo.commonlibrary.R;


/**
 * @author yk
 * @Description
 */
public class TitleBar extends RelativeLayout implements View.OnClickListener {

    private String mOperateText;
    private boolean mShowBack;
    private String mTitle;
    private OnBackClickListener backClickListener;
    private OnOperateClickListener operateClickListener;
    private TextView tvTitle;
    private TextView tvOperate;

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        mTitle = array.getString(R.styleable.TitleBar_title);
        mOperateText = array.getString(R.styleable.TitleBar_operate);
        mShowBack = array.getBoolean(R.styleable.TitleBar_showBack, true);
        init(context);
        array.recycle();
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.title_bar, this);
        tvTitle = view.findViewById(R.id.tv_title);
        tvOperate = view.findViewById(R.id.tv_operate);
        tvTitle.setText(mTitle);
        tvOperate.setOnClickListener(this);
        tvOperate.setText(mOperateText);

        ImageView mIvBack = view.findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mIvBack.setVisibility(mShowBack ? VISIBLE : INVISIBLE);
    }


    public void setTitle(String title) {
        tvTitle.setText(title);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_back) {
            if (backClickListener != null) {
                backClickListener.onBackClick();
            }

        } else if (i == R.id.tv_operate) {
            if (operateClickListener != null) {
                operateClickListener.onOperateClick();
            }
        }
    }


    public void setOnBackClickListener(OnBackClickListener listener) {

        this.backClickListener = listener;
    }

    public void setOnOperateClickListener(OnOperateClickListener listener) {
        this.operateClickListener = listener;
    }

    public interface OnBackClickListener {
        void onBackClick();
    }

    public interface OnOperateClickListener {
        void onOperateClick();
    }


}
