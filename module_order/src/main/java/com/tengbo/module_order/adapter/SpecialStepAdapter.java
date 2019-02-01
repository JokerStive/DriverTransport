package com.tengbo.module_order.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tengbo.module_order.R;
import com.tengbo.module_order.bean.Container;
import com.tengbo.module_order.bean.GoodsBatch;
import com.tengbo.module_order.bean.Mission;

import java.util.ArrayList;
import java.util.List;

public class SpecialStepAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int CONTAINER = 0;
    public static final int GOODS = 1;
    private final boolean showGoods;

    public SpecialStepAdapter(List<MultiItemEntity> data, boolean showGoods) {
        super(data);
        this.showGoods = showGoods;
        addItemType(CONTAINER, R.layout.item_container);
        addItemType(GOODS, R.layout.item_goods_batch);
    }

    @Override
    protected void convert(BaseViewHolder holder, MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case CONTAINER:
                Container container = ((Container) item);
                holder.setText(R.id.tv_name, "集裝箱柜号：" + container.getContainerCode());
                View view = holder.getView(R.id.iv_select);
                view.setVisibility(showGoods ? View.GONE : View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.setSelected(!view.isSelected());
                        container.setSelected(view.isSelected());
                    }
                });
                break;

            case GOODS:
                GoodsBatch goodsBatch = ((GoodsBatch) item);
                holder.setText(R.id.tv_name, goodsBatch.getGoodsName());
                View ivSelected = holder.getView(R.id.iv_select);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ivSelected.setSelected(!ivSelected.isSelected());
                        goodsBatch.setSelected(ivSelected.isSelected());
                    }
                });
                default:
                break;
        }
    }

    /**获取组装好的上传服务器的数据
     * @return
     */
    public List<Mission> createSelectedData() {
        List<Mission> missions = new ArrayList<>();
        List<MultiItemEntity> data = getData();
        for (MultiItemEntity multiItemEntity : data) {
            if(showGoods){
                if(multiItemEntity.getItemType()==GOODS){
                  GoodsBatch goodsBatch =   ((GoodsBatch) multiItemEntity);
                  if(goodsBatch.isSelected()){
                      Mission mission = new Mission();
                      mission.setContainerCode(goodsBatch.getContainerCode());
                      mission.setGoodsBatch(goodsBatch.getGoodsBatch());
                      missions.add(mission);
                  }
                }
            }else {
                if(multiItemEntity.getItemType()==CONTAINER ){
                    Container container = ((Container) multiItemEntity);
                    if(container.isSelected()){
                        Mission mission = new Mission();
                        mission.setContainerCode(container.getContainerCode());
                        missions.add(mission);
                    }
                }
            }
        }

        return missions;
    }

}



