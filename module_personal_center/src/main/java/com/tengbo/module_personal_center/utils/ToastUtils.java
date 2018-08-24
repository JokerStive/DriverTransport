package com.tengbo.module_personal_center.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by WangChenchen on 2016/11/11.
 * 吐司工具类
 */

public class ToastUtils {

    static Toast toast;

    public static void show(Context context, String msg) {
        if (toast == null)
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }
}
