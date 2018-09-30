package com.tengbo.module_order.ui.processing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.basiclibrary.utils.SelectorFactory;
import com.tengbo.basiclibrary.utils.UiUtils;
import com.tengbo.commonlibrary.base.BaseActivity;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.widget.takePhoto.TakePhotoDialogFragment;
import com.tengbo.commonlibrary.widget.takePhoto.imageselector.PreviewActivity;
import com.tengbo.commonlibrary.widget.takePhoto.imageselector.entry.Image;
import com.tengbo.module_order.R;
import com.tengbo.module_order.adapter.MultiplePictureAdapter;
import com.tengbo.module_order.bean.Order;
import com.tengbo.module_order.custom.view.SpinnerPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import utils.ToastUtils;
import com.tengbo.commonlibrary.widget.TitleBar;

public class ExceptionFeedbackActivity extends BaseActivity implements View.OnClickListener {

    private Order morder;
    private TextView tvExceptions;
    private int PICTURE_COUNT = 3;
    private RecyclerView rvPicture;
    private List<MultiplePictureAdapter.Picture> pictures = new ArrayList<>();
    private MultiplePictureAdapter mPictureAdapter;
    private String TAG = "ExceptionFeedbackActivity";
    private boolean isExpand;
    private List<String> mTypeString;
    private int mChooseExceptionTypePosition;

    public static void start(Activity activity, Order order) {
        Intent intent = new Intent(activity, ExceptionFeedbackActivity.class);
        intent.putExtra("order", order);
        activity.startActivity(intent);
    }


    @Override
    protected void onIntent(Intent intent) {
        super.onIntent(intent);
        morder = intent.getParcelableExtra("order");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exception_feed;
    }

    /**
     *
     */
    @Override
    protected void initView() {
        TitleBar titleBar = findViewById(R.id.titleBar);
        titleBar.setOnBackClickListener(this::finish);
        tvExceptions = findViewById(R.id.tv_exceptions);
        tvExceptions.setEnabled(false);
        rvPicture = findViewById(R.id.rv_picture);
        findViewById(R.id.btn_positive).setOnClickListener(this);
        findViewById(R.id.btn_negative).setOnClickListener(this);


        StateListDrawable blueShape = SelectorFactory.newShapeSelector()
                .setShape(GradientDrawable.RECTANGLE)
                .setStrokeWidth(UiUtils.dp2px(BaseApplication.get(), 1))
                .setDefaultStrokeColor(BaseApplication.get().getResources().getColor(com.tengbo.commonlibrary.R.color.basic_blue))
                .setCornerRadius(UiUtils.dp2px(BaseApplication.get(), 5))
                .create();
        tvExceptions.setBackground(blueShape);
        getExceptionTypes();
        initRv();

    }

    private void initRv() {
        MultiplePictureAdapter.Picture addPicture = new MultiplePictureAdapter.Picture(MultiplePictureAdapter.Picture.ADD_PICTURE);
        pictures.add(addPicture);


        rvPicture.setLayoutManager(new GridLayoutManager(getApplicationContext(), 6));
        mPictureAdapter = new MultiplePictureAdapter(this,pictures);
        mPictureAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultiplePictureAdapter.Picture picture = (MultiplePictureAdapter.Picture) adapter.getItem(position);
                if (picture.getItemType() == MultiplePictureAdapter.Picture.ADD_PICTURE) {
                    addPicture();
                }

            }
        });
        rvPicture.setAdapter(mPictureAdapter);
    }



    /**
     * 添加图片
     */
    private void addPicture() {
        if ((mPictureAdapter.getData().size() - 1) < PICTURE_COUNT) {
            int remainPictureSize = PICTURE_COUNT - (mPictureAdapter.getData().size() - 1);//剩余可以添加的图片数
            TakePhotoDialogFragment takePhotoDialogFragment = TakePhotoDialogFragment.newInstance(remainPictureSize, false);
            takePhotoDialogFragment.setOnResultListener(new TakePhotoDialogFragment.TakePhotoCallBack() {
                @Override
                public void onSuccess(List<File> files) {
                    // 图片获取成功，关闭拍照和选择图片对话框
                    takePhotoDialogFragment.dismissAllowingStateLoss();
                    for (File file : files) {
                        MultiplePictureAdapter.Picture picture = new MultiplePictureAdapter.Picture(MultiplePictureAdapter.Picture.SHOW_PICTURE);
                        picture.setPicturePath(file.getAbsolutePath());
                        mPictureAdapter.addData(mPictureAdapter.getData().size() - 1, picture);
                    }
                }

                @Override
                public void onError() {
                    // 关闭拍照和选择图片对话框
                    takePhotoDialogFragment.dismissAllowingStateLoss();
                }
            });

            takePhotoDialogFragment.show(getSupportFragmentManager(), TAG);
        } else {
            ToastUtils.show(getApplicationContext(), "最多只能上传" + PICTURE_COUNT + "张照片");
        }

    }

    private void getExceptionTypes() {
        mTypeString = new ArrayList<>();
        mTypeString.add("爆炸");
        mTypeString.add("爆炸");
        mTypeString.add("爆炸");
        mTypeString.add("爆炸");
        mTypeString.add("爆炸");
        mTypeString.add("爆炸");
        tvExceptions.setEnabled(true);
        tvExceptions.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_positive) {
            submitException();
        } else if (id == R.id.btn_negative) {
            finish();
        }
        //异常类型选择
        else if (id == R.id.tv_exceptions) {
            if (isExpand) {
                closePop();
            } else {
                showPop();
            }

        }
    }

    private SpinnerPopupWindow popupWindow;

    private void showPop() {
        isExpand = true;
        if (popupWindow == null) {
            popupWindow = new SpinnerPopupWindow(getApplicationContext(), mTypeString, new SpinnerPopupWindow.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    isExpand = false;
                    if (position != -1) {
                        mChooseExceptionTypePosition = position;
                        tvExceptions.setText(mTypeString.get(position));
                    }
                    popupWindow.dismiss();
                }
            });
        }
        popupWindow.showAsDropDown(tvExceptions);
    }

    private void closePop() {
        isExpand = false;
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * @Desc 提交异常信息
     */
    private void submitException() {

    }
}
