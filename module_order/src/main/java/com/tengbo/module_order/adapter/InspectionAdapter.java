package com.tengbo.module_order.adapter;

import android.view.View;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.commonlibrary.base.QuickAdapter;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Inspection;

import java.util.List;

public class InspectionAdapter extends QuickAdapter<Inspection> {

    private boolean chooseAll = false;

    public InspectionAdapter(List<Inspection> data) {
        super(R.layout.item_inspection, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Inspection item) {
        helper.setText(R.id.tv_inspect_name, "发动机检查")
                .setText(R.id.tv_inspect_detail, "检查检查检查检查检查检查检查检查检查检查检查检查检查检查检查检查检查")
                .setChecked(R.id.rb_first_positive, chooseAll)
                .setChecked(R.id.rb_second_positive, chooseAll)
                .setChecked(R.id.rb_third_positive, chooseAll);

    }

    public void chooseAll(boolean checked) {
        if (checked != chooseAll) {
            chooseAll = checked;
            notifyDataChanged();
        }

    }
}
