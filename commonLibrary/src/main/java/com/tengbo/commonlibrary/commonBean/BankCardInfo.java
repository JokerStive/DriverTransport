package com.tengbo.commonlibrary.commonBean;

public class BankCardInfo {

    /**
     * bankcardId : 银行卡id
     * cardCode : 卡号
     * idNumber : 身份证号
     * isTrade : 是否默认交易卡
     */

    private String bankcardId;
    private String cardCode;
    private String idNumber;
    private int isTrade;

    public String getBankcardId() {
        return bankcardId;
    }

    public void setBankcardId(String bankcardId) {
        this.bankcardId = bankcardId;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public int getIsTrade() {
        return isTrade;
    }

    public void setIsTrade(int isTrade) {
        this.isTrade = isTrade;
    }
}
