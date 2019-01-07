package com.tengbo.module_order.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.base.QuickAdapter;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Goods;

import java.util.List;

public class PictureAdapter extends QuickAdapter<String> {

    private Context context;

    public PictureAdapter(Context context, List<String> data) {
        super(R.layout.order_item_picture, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String url) {
        Glide.with(context).load(url).into((ImageView) helper.getView(R.id.iv_picture));

    }

}
