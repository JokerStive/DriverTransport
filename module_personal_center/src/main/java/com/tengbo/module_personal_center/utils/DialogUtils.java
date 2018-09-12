package com.tengbo.module_personal_center.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tengbo.module_personal_center.R;

/**
 * author WangChenchen
 * 对话框工具类
 */
public class DialogUtils {

    /**
     * 带图标和文本的对话框
     * @param context
     * @param imgId
     * @param msg
     */
    public  void show(Context context, int imgId, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_with_img, null);
        TextView tv_dialog = view.findViewById(R.id.tv_dialog);
        ImageView iv_dialog = view.findViewById(R.id.iv_dialog);
        tv_dialog.setText(msg);
        iv_dialog.setImageResource(imgId);
        builder.setView(view).setCancelable(true).show();
    }

    public interface CallBack {
        void call();
    }

    /**
     * 带文本和按钮的对话框，可以写回调
     * @param context
     * @param title
     * @param msg
     * @param callBack
     */
    public  void show(Context context, String title, String msg, CallBack callBack)
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
        tv_yes.setOnClickListener(view12 -> {
            dialog.dismiss();
            if(callBack != null)
                callBack.call();
        });
    }
}
