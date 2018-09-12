package com.tengbo.module_order.custom.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.tengbo.commonlibrary.widget.takePhoto.TakePhotoDialogFragment;
import com.tengbo.module_order.R;

public class TimePickDialog extends DialogFragment {

    private TimeSelector ts_from;
    private TimeSelector ts_to;
    private Button btn_query;
    private onQueryListener listener;

    public static TimePickDialog newInstance() {
        return new TimePickDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        if (window != null)
            window.setGravity(Gravity.BOTTOM);


        setStyle(R.style.ActionSheetDialogStyle, 0);
        View view = inflater.inflate(R.layout.layout_time_picker, null);
        ts_from = view.findViewById(R.id.ts_from);
        ts_to = view.findViewById(R.id.ts_to);
        btn_query = view.findViewById(R.id.btn_query);

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onQuery(ts_from.getSelectedDate(), ts_to.getSelectedDate());
                }
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
//            DisplayMetrics dm = new DisplayMetrics();
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            if (dialog.getWindow() != null)
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


    public interface onQueryListener {
        void onQuery(String from, String to);
    }

    public void setOnQueryListener(onQueryListener listener) {
        this.listener = listener;
    }
}
