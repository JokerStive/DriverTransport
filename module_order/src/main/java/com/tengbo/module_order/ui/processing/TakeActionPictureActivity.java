package com.tengbo.module_order.ui.processing;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.widget.takePhoto.custom_camera.CameraSurfaceView;
import com.tengbo.module_order.R;

public class TakeActionPictureActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvOrderNum;
    private TextView tvActionAddress;
    private CameraSurfaceView surfaceView;
    private ImageView ivTakePicture;


    public static void open(Activity activity) {
        Intent intent = new Intent(activity, TakeActionPictureActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.order_activity_take_action_picture;
    }

    @Override
    protected void initView() {
        tvOrderNum = findViewById(R.id.tv_order_num);
        tvActionAddress = findViewById(R.id.tv_action_address);
        surfaceView = findViewById(R.id.surfaceView);
        ivTakePicture = findViewById(R.id.iv_take_picture);
        ivTakePicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_take_picture) {
            surfaceView.takePicture();
        }
    }


}
