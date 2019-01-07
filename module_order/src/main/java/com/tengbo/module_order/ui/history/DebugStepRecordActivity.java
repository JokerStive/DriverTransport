package com.tengbo.module_order.ui.history;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tengbo.basiclibrary.widget.ninegridimageview.NineGridImageView;
import com.tengbo.basiclibrary.widget.ninegridimageview.NineGridImageViewAdapter;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.widget.TitleBar;
import com.tengbo.commonlibrary.widget.takePhoto.imageselector.PreviewActivity;
import com.tengbo.commonlibrary.widget.takePhoto.imageselector.entry.Image;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Step;
import com.tengbo.module_order.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class DebugStepRecordActivity extends BaseActivity {

    private Step mStep;

    public static void start(Activity activity, Step step) {
        Intent intent = new Intent(activity, DebugStepRecordActivity.class);
        intent.putExtra("Step", step);
        activity.startActivity(intent);
    }

    @Override
    protected void onIntent(Intent intent) {
        super.onIntent(intent);
        mStep = getIntent().getParcelableExtra("Step");
    }


    @Override
    protected int getLayoutId() {
        return R.layout.debug_step_record;
    }

    /**
     *
     */
    @Override
    protected void initView() {
        TitleBar titleBar = findViewById(R.id.titleBar);
        titleBar.setOnBackClickListener(this::finish);
        TextView tv_step_name = findViewById(R.id.tv_step_name);
        TextView tv_step_time = findViewById(R.id.tv_step_time);
        TextView tv_step_address = findViewById(R.id.tv_step_address);
        tv_step_name.setText("步骤名称：" + mStep.getStepName());
        tv_step_time.setText("结束时间：" + DateUtils.iso2Utc(mStep.getExecuteTime()));
        tv_step_address.setText("步骤地址：" + mStep.getNodeName());

        NineGridImageView<String> nineGridImageView = findViewById(R.id.nine_image);

        nineGridImageView.setAdapter(new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String url) {
                Glide.with(context).load(url).into(imageView);
            }

            @Override
            protected void onItemImageClick(Context context, ImageView imageView, int index, List<String> urls) {
                preView(index, urls);
            }
        });

        nineGridImageView.setImagesData(mStep.getAttachs());


        FrameLayout flContent = findViewById(R.id.fl_content);
        int contentLayoutId = -1;
        //针对特殊步骤添加不同的content
        int stepType = mStep.getStepType();

        if (stepType == 6) {
            //卸货
            contentLayoutId = R.layout.debug_xiehuo;
        } else if (stepType == 7) {
            //卸货完成
            contentLayoutId = R.layout.debug_lxiehuo_complete;
        } else if (TextUtils.equals("靠台", mStep.getStepName())) {
            //靠台
            contentLayoutId = R.layout.debug_kaotai;
        }

        if (contentLayoutId != -1) {
            View contentView = LayoutInflater.from(getApplicationContext()).inflate(contentLayoutId, null);
            flContent.addView(contentView);
        }


    }


    private void preView(int index, List<String> urls) {
        ArrayList<Image> images = new ArrayList<>();
        for (String url : urls) {
            Image image = new Image(url, 0, null, null);
            images.add(image);
        }
        PreviewActivity.openActivity(this, images, index);
    }
}
