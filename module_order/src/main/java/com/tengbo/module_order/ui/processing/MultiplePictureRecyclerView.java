package com.tengbo.module_order.ui.processing;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.module_order.R;

import java.io.File;
import java.util.List;

public class MultiplePictureRecyclerView extends RecyclerView {
    private Context context;

    public MultiplePictureRecyclerView(Context context) {
        this(context, null);
    }

    public MultiplePictureRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiplePictureRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }


    static class MultiplePictureAdapter extends BaseMultiItemQuickAdapter<Picture, BaseViewHolder> {

        public MultiplePictureAdapter(List<Picture> data) {
            super(data);
            addItemType(Picture.SHOW_PICTURE, R.layout.show_picture);
            addItemType(Picture.ADD_PICTURE, R.layout.add_picture);
        }

        @Override
        protected void convert(BaseViewHolder helper, Picture picture) {
            switch (helper.getItemViewType()) {
                case Picture.ADD_PICTURE:
                    helper.setImageResource(R.id.iv_add_picture, R.drawable.add_picture);
                    break;
                case Picture.SHOW_PICTURE:
                    Glide.with(BaseApplication.get())
                            .applyDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                            .load(picture.getPicture()).into((ImageView) helper.getView(R.id.iv_show_picture));
                    break;
            }
        }



    }



    static class Picture implements MultiItemEntity {
        public static final int SHOW_PICTURE = 0;
        public static final int ADD_PICTURE = 1;
        private File picture;
        private int type;

        Picture(int type) {
            this.type = type;
        }

        public File getPicture() {
            return picture;
        }

        public void setPicture(File picture) {
            this.picture = picture;
        }

        @Override
        public int getItemType() {
            return type;
        }
    }
}
