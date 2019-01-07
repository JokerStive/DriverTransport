package com.tengbo.module_order.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tengbo.module_order.adapter.SpecialStepAdapter;

public class GoodsBatch implements MultiItemEntity {
    private String goodsBatch;
    private String goodsName;
    private String containerCode;

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    private boolean isSelected;

    public String getGoodsBatch() {
        return goodsBatch;
    }

    public void setGoodsBatch(String goodsBatch) {
        this.goodsBatch = goodsBatch;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int getItemType() {
        return SpecialStepAdapter.GOODS;
    }
}
