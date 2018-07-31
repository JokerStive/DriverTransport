package com.tengbo.basiclibrary.imageUtils;

import android.content.Context;
import android.widget.ImageView;

public interface ImageLoadStrategy {

    //无占位图无生命周期控制
    void loadImage(String url, ImageView imageView);

    //无占位图绑定生命周期
    void loadImage(Context context, String url, ImageView imageView);

    //占位图不绑定生命周期
    void loadImage(String url, int placeholder, ImageView imageView);

    //占位图绑定生命周期
    void loadImage(Context context, String url, int placeholder, ImageView imageView);

    //
//    void loadImageWithProgress(String url, ImageView imageView, ProgressLoadListener listener);


    //清除硬盘缓存
    void clearImageDiskCache(Context context);

    //清除内存缓存
    void clearImageMemoryCache(Context context);

    //
    void loadImageSkipCache();
}
