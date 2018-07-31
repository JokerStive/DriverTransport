package com.tengbo.basiclibrary.imageUtils;

import android.widget.ImageView;

public class ImageLoader
{

    private static ImageLoader mInstance;
    private ImageLoadStrategy mStrategy;

    private ImageLoader() {
        mStrategy = new GlideStrategy();
    }



    public static ImageLoader newInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader();
                }
            }
        }
        return mInstance;
    }

    public void loadImage(String url, ImageView imageView){
        mStrategy.loadImage(url,imageView);
    }


}
