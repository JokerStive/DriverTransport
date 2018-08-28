package com.tengbo.module_order.bean;

import java.io.Serializable;

/**
 * @author Wang Chenchen
 * @date 2018/8/21
 * 订单bean
 */
public class Order  implements Serializable{

    private static final long serialVersionUID = 1L;
    // 订单编号
    public String orderNum;
    // 完成日期
    public String completeDate;
    // 运费
    public float freight;
    // 已支付
    public float alreadyPay;
    // 应扣
    public float shouldDeduct;
    // 接单状态
    public String orderStatus;

    public String depature;

    public String destination;

    public String scheduleStartTime;

    public String latestStartTime;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String scheduleArriveTime;

    public String method;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public float getFreight() {
        return freight;
    }

    public void setFreight(float freight) {
        this.freight = freight;
    }

    public float getAlreadyPay() {
        return alreadyPay;
    }

    public void setAlreadyPay(float alreadyPay) {
        this.alreadyPay = alreadyPay;
    }

    public float getShouldDeduct() {
        return shouldDeduct;
    }

    public void setShouldDeduct(float shouldDeduct) {
        this.shouldDeduct = shouldDeduct;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDepature() {
        return depature;
    }

    public void setDepature(String depature) {
        this.depature = depature;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getScheduleStartTime() {
        return scheduleStartTime;
    }

    public void setScheduleStartTime(String scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
    }

    public String getLatestStartTime() {
        return latestStartTime;
    }

    public void setLatestStartTime(String latestStartTime) {
        this.latestStartTime = latestStartTime;
    }

    public String getScheduleArriveTime() {
        return scheduleArriveTime;
    }

    public void setScheduleArriveTime(String scheduleArriveTime) {
        this.scheduleArriveTime = scheduleArriveTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNum='" + orderNum + '\'' +
                ", completeDate='" + completeDate + '\'' +
                ", freight='" + freight + '\'' +
                ", alreadyPay='" + alreadyPay + '\'' +
                ", shouldDeduct='" + shouldDeduct + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }
}
