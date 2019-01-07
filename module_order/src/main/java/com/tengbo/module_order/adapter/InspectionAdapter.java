package com.tengbo.module_order.adapter;

import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.commonlibrary.base.QuickAdapter;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Dictionary;
import com.tengbo.module_order.bean.Inspection;

import java.util.List;

public class InspectionAdapter extends QuickAdapter<Dictionary> {

    private OnNegativeClickListener listener;

    public InspectionAdapter(List<Dictionary> data) {
        super(R.layout.item_inspection, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Dictionary dictionary) {
        RadioGroup radioGroup = helper.getView(R.id.rg_is_usual);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_no && listener != null) {
//                    listener.onNegativeClick();
                }
            }
        });

//        RadioButton rbNo = helper.getView(R.id.rb_no);
//        rbNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//            }
//        });
        helper.getView(R.id.rb_no).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && listener != null) {
                    listener.onNegativeClick();
                }
                return false;
            }
        });

        helper.setText(R.id.tv_inspect_name, dictionary.getItemName())
                .setText(R.id.tv_inspect_detail, dictionary.getTypeCode())
                .addOnClickListener(R.id.rb_no);
    }

    public interface OnNegativeClickListener {
        void onNegativeClick();
    }

    public void setOnNegativeClickListener(OnNegativeClickListener listener) {
        this.listener = listener;
    }

//    public void chooseAll(boolean checkehelper.getView(R.id.rb_no)d) {
//        if (checked != chooseAll) {
//            chooseAll = checked;
//            notifyDataChanged();
//        }

//    }
}
