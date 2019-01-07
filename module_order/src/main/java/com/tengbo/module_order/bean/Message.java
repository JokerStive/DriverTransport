package com.tengbo.module_order.bean;

public class Message {

    /**
     * accountId : 接收人账号
     * content : 内容
     * msgType : 消息类型
     * sendTime : 发送时间
     * msgStatus : 消息状态
     */

    private String accountId;
    private String content;
    private String msgType;
    private String sendTime;
    private String msgStatus;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }
}
