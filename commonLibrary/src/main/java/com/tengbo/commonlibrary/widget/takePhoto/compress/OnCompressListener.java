package com.tengbo.commonlibrary.widget.takePhoto.compress;

import java.io.File;

public interface OnCompressListener {


    void onStart();


    void onSuccess(File file);


    void onError(Throwable e);
}