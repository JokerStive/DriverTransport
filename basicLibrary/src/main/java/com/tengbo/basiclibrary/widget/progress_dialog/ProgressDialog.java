package com.tengbo.basiclibrary.widget.progress_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import com.tengbo.basiclibrary.R;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.basiclibrary.widget.progress_dialog.OptAnimationLoader;

import java.util.Objects;


/**
*自定义的dialog
*/
public class ProgressDialog extends Dialog {

    public ProgressDialog(Context context) {
        super(context, R.style.alert_dialog);

        //默认返回键可以取消
        setCancelable(true);
        //其他区域不可取消
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_view);
    }

    @Override
    protected void onStart() {
    }

    @Override
    public void cancel() {

    }



}
