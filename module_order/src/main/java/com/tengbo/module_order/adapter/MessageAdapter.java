package com.tengbo.module_order.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.commonlibrary.base.QuickAdapter;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Message;

import java.util.List;

public class MessageAdapter extends QuickAdapter<Message> {

    public MessageAdapter(List<Message> data) {
        super(R.layout.item_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Message item) {
        helper
                .setText(R.id.tv_info, item.getContent());
    }
}
