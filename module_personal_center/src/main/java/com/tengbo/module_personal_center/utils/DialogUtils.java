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


    public interface CallBack {
        void call();
    }

    /**
     */
    public void show(Context context, String title, String msg, CallBack callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_with_btn, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvMsg = view.findViewById(R.id.tv_msg);
        TextView tvNegative = view.findViewById(R.id.tv_not);
        TextView tvPositive = view.findViewById(R.id.tv_yes);

        tvTitle.setText(title);
        tvMsg.setText(msg);
        AlertDialog dialog = builder.setView(view).setCancelable(true).create();
        dialog.show();
        tvNegative.setOnClickListener(view1 -> dialog.dismiss());
        tvPositive.setOnClickListener(view12 -> {
            dialog.dismiss();
            if (callBack != null){
                callBack.call();
            }
        });
    }
}
