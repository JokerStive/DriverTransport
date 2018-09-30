package com.tengbo.module_order.ui.processing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.widget.takePhoto.imageselector.PreviewActivity;
import com.tengbo.commonlibrary.widget.takePhoto.imageselector.entry.Image;
import com.tengbo.module_order.adapter.MultiplePictureAdapter;
import com.tengbo.module_order.bean.StepPicture;

import java.util.ArrayList;
import java.util.List;

import utils.ToastUtils;

public class StepPictureRecyclerView extends RecyclerView {

    private List<StepPicture> stepPictures = new ArrayList<>();
    private List<MultiplePictureAdapter.Picture> pictures = new ArrayList<>();
    private MultiplePictureAdapter adapter;
    private int PICTURE_COUNT;
    private Activity activity;

    public StepPictureRecyclerView(Context context) {
        super(context);
    }


    public StepPictureRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StepPictureRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void init(Activity activity,int pictureCount, int spanCount) {
        this.activity = activity;
        this.PICTURE_COUNT = pictureCount;

        MultiplePictureAdapter.Picture picture = new MultiplePictureAdapter.Picture(MultiplePictureAdapter.Picture.ADD_PICTURE);
        setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        pictures.add(picture);


        adapter = new MultiplePictureAdapter(getContext(), pictures);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultiplePictureAdapter.Picture item = (MultiplePictureAdapter.Picture) adapter.getItem(position);
                if(item.getItemType()==MultiplePictureAdapter.Picture.ADD_PICTURE){
                    addPicture();
                }else if(item.getItemType()==MultiplePictureAdapter.Picture.SHOW_PICTURE){
                    previewPicture(position);
                }
            }
        });
        setAdapter(adapter);
    }



    /**
     * 添加图片
     */
    private void addPicture() {
        if ((adapter.getData().size() - 1) < PICTURE_COUNT) {
            Intent intent = new Intent(activity, TakeStepPictureActivity.class);
            intent.putExtra(TakeStepPictureActivity.ORDER_ID, "TY87745211111");
            intent.putExtra(TakeStepPictureActivity.STEP_NAME, "装货");
            activity.startActivityForResult(intent, TakeStepPictureActivity.REQUEST_CODE);
        } else {
            ToastUtils.show(getContext(), "最多只能上传" + PICTURE_COUNT + "张照片");
        }

    }



    /**
     * @param position 选中的position
     * @Desc 预览异常图片
     */
    private void previewPicture(int position) {
        ArrayList<Image> images = new ArrayList<>();
        for (MultiplePictureAdapter.Picture picture : adapter.getData()) {
            if (picture.getPicturePath() != null) {
                Image image = new Image(picture.getPicturePath(), 1, "", "");
                images.add(image);
            }
        }

        PreviewActivity.openActivity(activity, images, position);

    }

    public void insertItem(StepPicture stepPicture) {
        stepPictures.add(stepPicture);
        MultiplePictureAdapter.Picture picture = new MultiplePictureAdapter.Picture(MultiplePictureAdapter.Picture.SHOW_PICTURE);
        picture.setPicturePath(stepPicture.getPicturePath());
        pictures.add(picture);
        adapter.notifyItemInserted(adapter.getData().size());
    }


    public List<StepPicture> getData() {
        return stepPictures;
    }
}
