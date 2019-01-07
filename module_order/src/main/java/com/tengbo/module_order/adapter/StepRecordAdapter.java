package com.tengbo.module_order.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.basiclibrary.widget.ninegridimageview.NineGridImageView;
import com.tengbo.basiclibrary.widget.ninegridimageview.NineGridImageViewAdapter;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Step;
import com.tengbo.module_order.utils.DateUtils;

import java.util.List;

public class StepRecordAdapter extends BaseQuickAdapter<Step, BaseViewHolder> {


    private Context context;
    private OnImageClickListener listener;

    public StepRecordAdapter(Context context, List<Step> data) {
        super(R.layout.order_item_step_record, data);
        this.context = context;
    }

    /**
     * @param helper
     * @param step
     */
    @Override
    protected void convert(BaseViewHolder helper, Step step) {
        helper.setText(R.id.tv_step_name, step.getStepName())
                .setText(R.id.tv_step_time, DateUtils.iso2Utc(step.getExecuteTime()))
                .setText(R.id.tv_step_address, step.getNodeName())
                .addOnClickListener(R.id.nine_image);
        NineGridImageView<String> nineGridImageView = helper.getView(R.id.nine_image);

        nineGridImageView.setAdapter(new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String url) {
                Glide.with(context).load(url).into(imageView);
            }

            @Override
            protected void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
                if (listener != null) {
                    listener.onImageClick(index, list);
                }
            }
        });

        nineGridImageView.setImagesData(step.getAttachs());

        //debug
//        int stepType = step.getStepType();
//        String stepName = step.getStepName();
//        if (stepType == 6
//                || stepType == 7
//                || TextUtils.equals("靠台", stepName)
//                ) {
//            helper.getView(R.id.next).setVisibility(View.VISIBLE);
//        }else {
//            helper.getView(R.id.next).setVisibility(View.GONE);
//        }

    }


    public interface OnImageClickListener {
        void onImageClick(int index, List<String> urls);
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.listener = listener;
    }
}
