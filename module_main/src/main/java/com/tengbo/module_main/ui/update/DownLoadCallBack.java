package com.tengbo.module_main.ui.update;

import java.io.File;

/**
* 下載回掉
*@author yk
*create at 2017/7/20 15:09
*email : yk_developer@163.com
*/
public interface DownLoadCallBack {
    void onSuccess(File file);
    void onProgress(int progress, int total);
    void onFail(Throwable t);
}
