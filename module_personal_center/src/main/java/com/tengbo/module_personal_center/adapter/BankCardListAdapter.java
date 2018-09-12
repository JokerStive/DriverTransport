package com.tengbo.module_personal_center.adapter;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.commonlibrary.commonBean.BankCardInfo;
import com.tengbo.module_personal_center.R;
import com.tengbo.module_personal_center.utils.BankCardValidUtils;

import java.util.List;

public class BankCardListAdapter extends BaseQuickAdapter<BankCardInfo, BaseViewHolder> {

    public BankCardListAdapter(@Nullable List<BankCardInfo> data) {
        super(R.layout.center_item_bank_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BankCardInfo bankCardInfo) {
        helper.setText(R.id.tv_bank_name, BankCardValidUtils.getBankNameWithId(bankCardInfo.getCardCode()))
                .setText(R.id.tv_bank_card_num, dealBankCardId(bankCardInfo.getCardCode()));
        helper.setBackgroundRes(R.id.rl_bank_card_info, bankCardInfo.getIsTrade() == 1 ? R.drawable.center_shape_default_bank_card : R.drawable.center_shape_undefault_bank_card);

//        if (bankCardInfo.getIsTrade() == 1) {
//            oldDefaultPosition = helper.getAdapterPosition();
//        }
    }

//
//    public void setOldDefaultPosition(int position) {
//        getData().get(oldDefaultPosition).setIsTrade(0);
//        BankCardInfo newDefaultInfo = getData().get(position);
//        newDefaultInfo.setIsTrade(1);
//        getData().remove(position);
//        getData().add(0,newDefaultInfo);
//        notifyItemChanged(oldDefaultPosition);
//        notifyItemChanged(position);
//        oldDefaultPosition = position;
//    }

    /**
     * 银行卡号隐藏4位
     */
    @NonNull
    private String dealBankCardId(String bankCardCode) {
        StringBuilder stringBuilder = new StringBuilder();
        int oldBankCardNumLength = bankCardCode.length();
        for (int i = 0; i < oldBankCardNumLength; i += 4) {
            if (i + 4 < oldBankCardNumLength)
                stringBuilder.append(bankCardCode.substring(i, i + 4));
            else
                stringBuilder.append(bankCardCode.substring(i, oldBankCardNumLength));
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }
}
