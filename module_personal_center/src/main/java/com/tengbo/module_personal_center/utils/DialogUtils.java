package com.tengbo.module_personal_center.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.tengbo.module_personal_center.R;

public class DialogUtils {

    public static void show(Context context, int layoutId, String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        TextView tv_dialog = view.findViewById(R.id.tv_dialog);
        tv_dialog.setText(msg);
        builder.setView(view).setCancelable(true).show();
    }

    /**
     * convert dp to its equivalent px
     */
    protected static int dp2px(Context context, int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * convert sp to its equivalent px
     */
    protected static int sp2px(Context context, int sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
