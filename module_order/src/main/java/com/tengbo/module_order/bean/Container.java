package com.tengbo.module_order.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tengbo.module_order.adapter.SpecialStepAdapter;

public class Container extends AbstractExpandableItem<GoodsBatch> implements MultiItemEntity {

    private String containerCode;
    private String containerName;
    private boolean isSelected;

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return SpecialStepAdapter.CONTAINER;
    }
}
