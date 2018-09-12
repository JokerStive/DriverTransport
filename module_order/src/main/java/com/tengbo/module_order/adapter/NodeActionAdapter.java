package com.tengbo.module_order.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.base.QuickAdapter;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Action;

import java.util.List;

public class NodeActionAdapter extends QuickAdapter<Action> {
    public NodeActionAdapter(List<Action> data) {
        super(R.layout.item_node_action, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Action action) {
        helper.setText(R.id.tv_action_index, helper.getLayoutPosition() + "")
                .setText(R.id.tv_action_name, action.getName());

        TextView tvTimeDesc = helper.getView(R.id.tv_action_time_desc);
        TextView tvTime = helper.getView(R.id.tv_action_time);
        TextView tvName = helper.getView(R.id.tv_action_name);
        int status = action.getStatus();
        if (status == Action.UNSTART) {
            tvName.setBackgroundResource(R.drawable.node_action_no);
            tvTimeDesc.setText("计划时间");
            tvTime.setText(action.getScheduleTime());
        } else if (status == Action.DONE) {
            tvName.setBackgroundResource(action.isAuto() ? R.drawable.node_action_auto : R.drawable.node_action_done);
            tvTimeDesc.setText("发生时间");
            tvTime.setText(action.getActualTime());

        }
    }


    public void setActionPass(int position) {
        Action item = getItem(position);
        assert item != null;
        LogUtil.d("取得的的action---" + item.getName()+"--position"+position);
        item.setStatus(Action.DONE);
        notifyItemChanged(position+1);
    }
}
