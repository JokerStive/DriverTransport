package com.tengbo.commonlibrary.base;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tengbo.commonlibrary.BuildConfig;
import com.tengbo.commonlibrary.common.Constance;


public class BaseApplication extends Application {
    private static BaseApplication context;

    public static BaseApplication get() {

        return context;
    }

    public void toLogin() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initLogger();
    }



    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
                .methodCount(3)
                .methodOffset(5)
                .tag(Constance.LOGGER_TAG)
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
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
