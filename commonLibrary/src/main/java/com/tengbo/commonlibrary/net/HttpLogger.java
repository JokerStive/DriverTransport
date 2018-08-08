package com.tengbo.commonlibrary.net;

import android.media.audiofx.LoudnessEnhancer;

import com.orhanobut.logger.Logger;
import com.tengbo.basiclibrary.utils.LogUtil;



public class HttpLogger implements LogInterceptor.Logger {
    private StringBuilder mMessage = new StringBuilder();

    @Override
    public void log(String message) {
        if (message.startsWith("--> POST")) {
            mMessage.setLength(0);
        }
        if ((message.startsWith("{") && message.endsWith("}"))
                || (message.startsWith("[") && message.endsWith("]"))) {
            Logger.json(message);
        }
        mMessage.append(message.concat("\n"));
        if (message.startsWith("<-- END HTTP")) {
            LogUtil.d(mMessage.toString());
        }
    }
}