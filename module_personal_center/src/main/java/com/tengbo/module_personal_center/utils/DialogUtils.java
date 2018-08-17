package com.tengbo.module_personal_center.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.tengbo.module_personal_center.R;

public class DialogUtils {

    public static void show(Context context, int imgId, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_with_img, null);
        TextView tv_dialog = view.findViewById(R.id.tv_dialog);
        ImageView iv_dialog = view.findViewById(R.id.iv_dialog);
        tv_dialog.setText(msg);
        iv_dialog.setImageResource(imgId);
        builder.setView(view).setCancelable(true).show();
    }

    public static void show(Context context, String msg, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("是", listener)
                .setNegativeButton("否", null)
                .show();
    }

    public interface LogoutInterface{
        void logout();
    }

    public static void show(Context context, String title, String msg, LogoutInterface listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_with_btn, null);
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        TextView tv_not = view.findViewById(R.id.tv_not);
        TextView tv_yes = view.findViewById(R.id.tv_yes);

        tv_title.setText(title);
        tv_msg.setText(msg);
        AlertDialog dialog = builder.setView(view).setCancelable(true).create();
        dialog.show();
        tv_not.setOnClickListener(view1 -> dialog.dismiss());
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(listener != null)
                    listener.logout();
            }
        });
    }

    /**
     * convert dp to its equivalent px
     */
    protected static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * convert sp to its equivalent px
     */
    protected static int sp2px(Context context, int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
