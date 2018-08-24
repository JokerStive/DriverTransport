package com.tengbo.module_main.ui.home;

import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxFileCallBack;
import com.tamic.novate.util.FileUtil;
import com.tengbo.commonlibrary.base.BaseApplication;
import com.tengbo.commonlibrary.mvp.BasePresenter;
import com.tengbo.commonlibrary.net.NetHelper;
import com.tengbo.module_main.bean.UpdateInfo;

import java.io.File;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    @Override
    public void checkUpdate() {
        String downUrl = "http://wap.dl.pinyin.sogou.com/wapdl/hole/201512/03/SogouInput_android_v7.11_sweb.apk";
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setNeedUpdate(true);
        updateInfo.setFocusUpdate(true);
        updateInfo.setUpdateDescription("使用热更新技术来更新，这是一个测试");
        updateInfo.setUpdateUrl(downUrl);
        mView.checkUpdateResult(updateInfo);
    }

    @Override
    public void loadNewApk(String downUrl) {

    }
}
