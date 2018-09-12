package com.tengbo.module_order.custom.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.basiclibrary.utils.UiUtils;
import com.tengbo.module_order.R;

import java.util.List;


/**
 * 自定义SpinnerPopupWindow，用于实现SpinnerTextView的下拉框
 */
public class SpinnerPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    private final OnItemClickListener listener;
    private Context context;
    private LayoutInflater mInflater;
    /**
     * 下拉框选择列表
     */
    private RecyclerView rv_spinner;
    /**
     * 下拉列表数据，通过setList方法传入
     */
    private List<String> data;

    public SpinnerPopupWindow(Context context, List<String> data, OnItemClickListener listener) {
        super(context);
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.data = data;
        this.listener = listener;
        init();
    }

    public List<String> getList() {
        return data;
    }

    public void setList(List<String> list) {
        this.data = list;
    }

    private void init() {
        View view = mInflater.inflate(R.layout.order_lv_spinner, null);

        setContentView(view);

        setWidth(UiUtils.dp2px(context, 100));
        setHeight(LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);

        rv_spinner = view.findViewById(R.id.rv_spinner);
        StringAdapter adapter = new StringAdapter(data);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
        rv_spinner.setLayoutManager(new LinearLayoutManager(context));
        rv_spinner.setAdapter(adapter);

        setOnDismissListener(this);
    }

    @Override
    public void onDismiss() {
        super.dismiss();
        if (listener != null) {
            listener.onClick(-1);
        }
    }


    private class StringAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public StringAdapter(@Nullable List<String> data) {
            super(R.layout.order_item_exception, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.tv_exception, item);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }


}
