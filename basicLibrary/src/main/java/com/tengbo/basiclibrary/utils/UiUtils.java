package com.tengbo.basiclibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by youke on 2016/7/11.
 * px dp工具
 */
public class UiUtils {

    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }


    public static int dp2px(Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }


    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / fontScale + 0.5f);
    }


    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static float getScreenHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getHeight();
    }


    public static  void setBold(TextView textView){
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
    }


    /**
     * 设置当前activity透明度
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }
}
