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

    /**
     * @param helper
     * @param step
     */
    @Override
    protected void convert(BaseViewHolder helper, Step step) {
        helper.setText(R.id.tv_step_index, helper.getLayoutPosition() + "")
                .setText(R.id.tv_step_name, step.getStepName());

        TextView tvName = helper.getView(R.id.tv_step_name);
        View ivWarm = helper.getView(R.id.iv_warm);
        ivWarm.setVisibility(step.isCached() ? View.VISIBLE : View.INVISIBLE);
        if (step.isProcessed()) {
            tvName.setBackgroundResource(R.drawable.step_processed);
        } else {
            tvName.setBackgroundResource(step.getNodeType() == 2 ? R.drawable.step_unprocess_auto : R.drawable.step_unprocess);
        }

        boolean  needShowLine =  helper.getLayoutPosition()%4!=0;
        if(needShowLine){
            helper.getView(R.id.line).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.line).setVisibility(View.GONE);
        }
    }


    /**
     * 设置步骤为已经执行
     *
     * @param position 该步骤在列表中的位置
     */
    public void setStepProcessed(int position) {
        Step item = getData().get(position);
        assert item != null;
        LogUtil.d("取得的的step---" + item.getStepName() + "--position" + position);
        item.setStepStatus(1);
        item.setCached(false);
        item.setProcessed(true);
        notifyItemChanged(position+1);
    }

    /**
     * 缓存步骤，加感叹号
     *
     * @param position 该步骤在列表中的位置
     */
    public void setStepCached(int position) {
        Step item = getData().get(position);
        if (item != null) {
            item.setCached(true);
            item.setProcessed(true);
            notifyItemChanged(position+1);
        }
    }

    /**
     * 获取最后一个已经执行的步骤
     *
     * @return 步骤实例
     */
    public Step getLatestProcessedStep() {
        Step result = null;
        if(getData().size()==0){
            return null;
        }
        for (int i = 0; i < getData().size(); i++) {
            Step step = getData().get(i);
            if (step.getNodeType() != 2 && step.isProcessed()) {
                result = step;
            } else {
                break;
            }


        }

        if (result == null) {
            result = getData().get(0);
        }
        return result;
    }
}
