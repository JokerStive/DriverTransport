package com.tengbo.drivertransport;


import android.util.Log;

import com.billy.cc.core.component.CC;
import com.tengbo.commonlibrary.BuildConfig;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.common.ApplicationInterface;
import com.tengbo.commonlibrary.common.ApplicationInterfaceImplArr;

public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initCC();
//        initJPush();
//        executeApplicationInterfaceImpls();
    }

//    private void initJPush() {
//        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);     // 初始化 JPush
//         TODO 设置通知栏样式
//         TODO 设置别名和标签
//        Log.e("JPush", "init");
//    }

//    private void executeApplicationInterfaceImpls()
//    {
//        for(String impl : ApplicationInterfaceImplArr.impls)
//        {
//            try{
//                Class<?> clazz = Class.forName(impl);
//                Object o = clazz.newInstance();
//                if(o instanceof ApplicationInterface)
//                    ((ApplicationInterface) o).onCreate(this);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void initCC() {
        CC.init(get());
        CC.enableRemoteCC(true);
        CC.enableDebug(BuildConfig.DEBUG);
        CC.enableVerboseLog(BuildConfig.DEBUG);
    }
}
