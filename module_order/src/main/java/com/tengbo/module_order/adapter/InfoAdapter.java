package com.tengbo.module_order.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.commonlibrary.base.QuickAdapter;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Info;

import java.util.List;

public class InfoAdapter extends QuickAdapter<Info> {

    public InfoAdapter(List<Info> data) {
        super(R.layout.item_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Info item) {
        helper.setText(R.id.tv_to, "司机小四：")
                .setText(R.id.tv_info, "TBQQ20180801002 ,车辆已经报废 !TBQQ20180801002 ,车辆已经报废 !");
    }
}
