package com.tengbo.module_order.custom.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tengbo.module_order.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangChenchen on 2016/11/15.
 */

public class SpinnerTextView extends TextView {
    /**
     * 下拉列表
     */
    private SpinnerPopupWindow mSpinnerPopupWindow;

    public SpinnerPopupWindow getmSpinnerPopupWindow() {
        return mSpinnerPopupWindow;
    }

    /**
     * 本类对象的代理，方便在匿名内部类中使用
     */
    private SpinnerTextView mSpinnerTextView;
    /**
     * 下拉列表数据
     */
    private List<String> strings = new ArrayList<>();
    /**
     * 用于SpinnerPopupWindow的初始化
     */
    private Context mContext;

    public SpinnerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSpinnerTextView = this;
        initObjects();
    }

    /**
     * 初始化下拉列表
     */
    private void initObjects()
    {
        mSpinnerPopupWindow = new SpinnerPopupWindow(mContext, strings, onItemClickListener);
        mSpinnerPopupWindow.setDismissListener(() -> {
            mSpinnerTextView.setBackgroundResource(R.drawable.shape_radius5_solid_white);
        });
        /**
         * 设置消失监听事件
         */
        mSpinnerPopupWindow.setOnDismissListener(onDismissListener);
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        /**
         * 设置SpinnerTextView默认值
         */
        if(strings.size() > 0)
            this.setText(strings.get(0));
        /**
         * 传入下拉列表数据
         */
        this.strings = strings;
        mSpinnerPopupWindow.setList(strings);
    }

    public interface OnItemClickListener{
        public void onItemClick(SpinnerPopupWindow spinnerPopupWindow, int position);
    }
    private OnItemClickListener mItemClickListener;
    public void setOnItemClickListener(OnItemClickListener itemClickListener)
    {
        this.mItemClickListener = itemClickListener;
    }

    /**
     * 下拉列表的项点击事件
     */
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            mItemClickListener.onItemClick(mSpinnerPopupWindow, position);
        }
    };

    /**
     * 下拉列表消失监听事件
     */
    private PopupWindow.OnDismissListener onDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            setTextViewRightImg(R.drawable.triangle_down);
        }
    };


    /**
     * 更新SpinnerTextView右侧图标的方法
     * @param mipmapId
     */
    public void setTextViewRightImg(int mipmapId)
    {
        Drawable d = getResources().getDrawable(mipmapId);
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        mSpinnerTextView.setCompoundDrawables(null, null, d, null);
    }

}
