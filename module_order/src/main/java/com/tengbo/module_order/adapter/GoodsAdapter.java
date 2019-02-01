package com.tengbo.module_order.adapter;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.base.QuickAdapter;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Goods;
import com.tengbo.module_order.utils.DateUtils;

import java.util.List;

public class GoodsAdapter extends QuickAdapter<Goods> {

    public GoodsAdapter(List<Goods> data) {
        super(R.layout.order_item_goods, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods goods) {
        TextView tvLoadContact = helper.getView(R.id.tv_departure_contact);
        TextView tvUnloadContact = helper.getView(R.id.tv_destination_contact);
        tvLoadContact.setVisibility(TextUtils.isEmpty(goods.getLoadingContacts())?View.GONE:View.VISIBLE);
        tvUnloadContact.setVisibility(TextUtils.isEmpty(goods.getUnloadingContacts())?View.GONE:View.VISIBLE);
        if(!TextUtils.isEmpty(goods.getLoadingContacts())){
            tvLoadContact.setText(getTextStyleSoan(R.string.contact_of_pick_up_goods,goods.getLoadingContacts()+"("+goods.getLoadingContactsPhone()+")"));
        }

        if(!TextUtils.isEmpty(goods.getUnloadingContacts())){
           tvUnloadContact.setText(getTextStyleSoan(R.string.contact_of_unload,goods.getLoadingContacts()+"("+goods.getLoadingContactsPhone()+")"));
        }
        helper.setText(R.id.tv_departure,getTextStyleSoan(R.string.place_of_pick_up_goods, goods.getLoadingNodeName()))
                .setText(R.id.tv_destination,getTextStyleSoan(R.string.place_of_unload, goods.getUnloadingNodeName()))
                .setText(R.id.tv_goods_name,getTextStyleSoan(R.string.name_of_goods, goods.getGoodsName()))
                .setText(R.id.tv_goods_weight,getTextStyleSoan(R.string.weight_of_goods, String.valueOf(goods.getGoodsWeight())))
                .setText(R.id.tv_goods_flight,getTextStyleSoan(R.string.transport_num, String.valueOf(goods.getTransportNumber())))
                .setText(R.id.tv_GPS_number,getTextStyleSoan(R.string.gps_num, goods.getGpsCode()))

                .setText(R.id.tv_plan_start_time,getTextStyleSoan(R.string.time_plan_start_time, DateUtils.iso2Utc(goods.getPredictStartTime())))
                .setText(R.id.tv_plan_upload_time,getTextStyleSoan(R.string.time_of_plan_upload, DateUtils.iso2Utc(goods.getPredictUploadStartTime())))
                .setText(R.id.tv_plan_end_time,getTextStyleSoan(R.string.time_plan_end_time, DateUtils.iso2Utc(goods.getPredictEndTime())));



    }


    @SuppressLint("SetTextI18n")
    private SpannableString getTextStyleSoan(int before, String after) {
        SpannableString result = null;
        String beforeString = BaseApplication.get().getString(before);
        int beforeStringLength = beforeString.length();
        if (beforeStringLength > 0 && !TextUtils.isEmpty(after)) {
            String resultString = beforeString + after;
            result = new SpannableString(resultString);
            result.setSpan(new StyleSpan(Typeface.BOLD), 0, beforeStringLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            result.setSpan(new StyleSpan(Typeface.NORMAL), resultString.length() - after.length(), resultString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return  result;
    }
}
