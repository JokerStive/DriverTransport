package com.tengbo.module_order.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.basiclibrary.utils.LogUtil;
import com.tengbo.commonlibrary.base.QuickAdapter;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Step;

import java.util.List;

public class StepAdapter extends QuickAdapter<Step> {
    public StepAdapter(List<Step> data) {
        super(R.layout.item_node_action, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Step step) {
        helper.setText(R.id.tv_step_index, helper.getLayoutPosition() + "")
                .setText(R.id.tv_step_name, step.getStepName());

        TextView tvTimeDesc = helper.getView(R.id.tv_step_time_desc);
        TextView tvTime = helper.getView(R.id.tv_step_time);
        TextView tvName = helper.getView(R.id.tv_step_name);
        View ivWarm = helper.getView(R.id.iv_warm);
        boolean processed = step.isProcessed();
        ivWarm.setVisibility(step.isCached() ? View.VISIBLE : View.INVISIBLE);
        if (!processed) {
            tvName.setBackgroundResource(R.drawable.node_step_no);
            tvTimeDesc.setText("计划时间");
            tvTime.setText("18/8/8 08：00");
        } else {
            tvName.setBackgroundResource(step.getNodeType() == 1 ? R.drawable.node_step_auto : R.drawable.node_step_done);
            tvTimeDesc.setText("发生时间");
            tvTime.setText("18/8/8 08：00");

        }
    }


    /**
     * @Desc 设置该步骤为通过
     */
    public void setStepPass(int position) {
        Step item = getItem(position);
        assert item != null;
        LogUtil.d("取得的的step---" + item.getStepName() + "--position" + position);
        item.setProcessed(true);
        notifyItemChanged(position + 1);
    }

    /**
     * @Desc 设置该步骤是否缓存过
     */
    public void setStepCached(int stepSerialNumber, boolean isCached) {
        for (int i = 0; i < getData().size(); i++) {
            Step step = getData().get(i);
            if (step.getStepSerialNumber() == stepSerialNumber) {
                step.setCached(isCached);
                notifyItemChanged(i + 1);
            }
        }
    }
}
