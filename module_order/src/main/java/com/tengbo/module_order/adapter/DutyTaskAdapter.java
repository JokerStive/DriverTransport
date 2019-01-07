package com.tengbo.module_order.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Task;
import com.tengbo.module_order.utils.DateUtils;

import java.util.List;

public class DutyTaskAdapter extends BaseQuickAdapter<Task, BaseViewHolder> {
    public DutyTaskAdapter(@Nullable List<Task> data) {
        super(R.layout.order_item_duty_task, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Task task) {
        helper.setText(R.id.tv_container_code, "柜号：" + task.getContainerCode())
                .setText(R.id.tv_operation_type, "动作：" + type2String(task.getOperationType()))
                .setText(R.id.tv_create_time, "开始时间：" + DateUtils.iso2Utc(task.getCreateTime()))
                .setText(R.id.tv_operation_time, "操作时间：" + DateUtils.iso2Utc(task.getOperationTime()));

    }

    private String type2String(String type) {
        assert type != null;
        String result = "";
        if (TextUtils.equals(type, "1")) {
            result = "靠台";
        } else if (TextUtils.equals(type, "2")) {
            result = "开始装货";
        } else if (TextUtils.equals(type, "3")) {
            result = "完成装货";
        } else if (TextUtils.equals(type, "4")) {
            result = "甩重柜";
        }

        return result;
    }
}
