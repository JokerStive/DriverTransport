package com.tengbo.commonlibrary.base;

import android.app.Application;


public class BaseApplication extends Application {
    private static BaseApplication context;

    public static BaseApplication get() {

        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

//        initGallery();
    }

//    private void initGallery() {
//        FunctionConfig functionConfig = new FunctionConfig.Builder()
////                .setMutiSelectMaxSize(3)
//                .setEnableCamera(true)
////                .setEnableCrop(true)
//                .setCropReplaceSource(false)
//                .build();
//
//        TakePhotoDialogFragment.GlideImageLoader glideImageLoader = new TakePhotoDialogFragment.GlideImageLoader();
//        ThemeConfig theme = new ThemeConfig.Builder()
//        .build();
//        CoreConfig coreConfig = new CoreConfig.Builder(context, glideImageLoader, theme)
//                .setFunctionConfig(functionConfig)
//                .build();
//        GalleryFinal.init(coreConfig);
//
//    }
}
