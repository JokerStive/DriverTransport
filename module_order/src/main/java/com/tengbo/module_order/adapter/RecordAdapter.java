package com.tengbo.module_order.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.commonlibrary.base.QuickAdapter;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Record;

import java.util.List;

public class RecordAdapter extends QuickAdapter<Record> {

    private boolean isHint;

    public RecordAdapter(List<Record> data) {
        super(R.layout.item_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Record item) {

        helper.setText(R.id.tv_desc, "开始     18/08/05  15:20     重庆沙坪坝 ");
        helper.setVisible(R.id.container, !isHint);
    }


    public void setIsHint(boolean isHint){
        this.isHint = isHint;
        notifyDataChanged();
    }

}
