package com.tengbo.basiclibrary.imageUtils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideStrategy implements  ImageLoadStrategy{

    @Override
    public void loadImage(String url, ImageView imageView) {
    }

    @Override
    public void loadImage(Context context, String url, ImageView imageView) {

    }

    @Override
    public void loadImage(String url, int placeholder, ImageView imageView) {

    }

    @Override
    public void loadImage(Context context, String url, int placeholder, ImageView imageView) {

    }


    @Override
    public void clearImageDiskCache(Context context) {

    }

    @Override
    public void clearImageMemoryCache(Context context) {

    }

    @Override
    public void loadImageSkipCache() {

    }
}
