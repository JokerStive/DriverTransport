package com.tengbo.module_order.bean;

public class Flow {

    /**
     * cardName :
     * cardCode : 卡号
     * businessType : 业务类型（1 预付款 2 运输费用
     * cardType : 卡类型 （0 未知卡类型1 银行卡 2油卡 3 供应商磁卡）
     * payableAmount : 应付卡金额
     * realAmount : 实付金额
     * flowNumber : 第三方流水
     * payTime : 支付时间
     * flowStatus : 交易状态（0 待申请1待审核 2审核失败3未支付 4已支付 5支付失败
     */

    private String cardName;
    private String cardCode;
    private int businessType;
    private int cardType;
    private int payableAmount;
    private int realAmount;
    private String flowNumber;
    private String payTime;
    private String flowStatus;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(int payableAmount) {
        this.payableAmount = payableAmount;
    }

    public int getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(int realAmount) {
        this.realAmount = realAmount;
    }

    public String getFlowNumber() {
        return flowNumber;
    }

    public void setFlowNumber(String flowNumber) {
        this.flowNumber = flowNumber;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }
}
