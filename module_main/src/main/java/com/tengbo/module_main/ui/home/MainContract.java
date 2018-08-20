package com.tengbo.module_main.ui.home;

import com.tengbo.commonlibrary.mvp.IPresenter;
import com.tengbo.commonlibrary.mvp.IView;
import com.tengbo.module_main.bean.UpdateInfo;


public interface MainContract {
    interface View extends IView<Presenter> {
        void checkUpdateResult(UpdateInfo updateInfo);
        void installNewApk(String apkPath);
    }

    interface Presenter extends IPresenter<View> {
        void checkUpdate();
        void loadNewApk(String url);
    }
}
