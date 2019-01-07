package com.tengbo.module_order.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.widget.takePhoto.imageselector.PreviewActivity;
import com.tengbo.commonlibrary.widget.takePhoto.imageselector.entry.Image;
import com.tengbo.module_order.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MultiplePictureAdapter extends BaseMultiItemQuickAdapter<MultiplePictureAdapter.Picture, BaseViewHolder> {

    private Context context;

    public MultiplePictureAdapter(Context context, List<Picture> data) {
        super(data);
        this.context = context;
        addItemType(MultiplePictureAdapter.Picture.SHOW_PICTURE, R.layout.show_picture);
        addItemType(MultiplePictureAdapter.Picture.ADD_PICTURE, R.layout.add_picture);
    }

    @Override
    protected void convert(BaseViewHolder helper, Picture picture) {
        switch (helper.getItemViewType()) {
            case MultiplePictureAdapter.Picture.ADD_PICTURE:
                helper.setImageResource(R.id.iv_add_picture, R.drawable.order_take_picture);
                break;
            case MultiplePictureAdapter.Picture.SHOW_PICTURE:
                helper.setOnClickListener(R.id.iv_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(helper.getAdapterPosition());
                    }
                });
                Glide.with(BaseApplication.get())
                        .applyDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                        .load(picture.getPicturePath()).into((ImageView) helper.getView(R.id.iv_show_picture));
                break;
        }
    }


    public static class Picture implements MultiItemEntity {
        public static final int SHOW_PICTURE = 0;
        public static final int ADD_PICTURE = 1;
        private String picturePath;
        private int type;

        public Picture(int type) {
            this.type = type;
        }

        public String getPicturePath() {
            return picturePath;
        }

        public void setPicturePath(String picture) {
            this.picturePath = picture;
        }

        @Override
        public int getItemType() {
            return type;
        }
    }


    public List<String> getPicturePaths() {
        List<String> picturePaths = new ArrayList<>();
        for (MultiplePictureAdapter.Picture picture : getData()) {
            if (!TextUtils.isEmpty(picture.getPicturePath())) {
                picturePaths.add(picture.getPicturePath());
            }
        }
        return picturePaths;
    }
}
