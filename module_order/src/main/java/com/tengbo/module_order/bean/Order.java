package com.tengbo.module_order.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Wang Chenchen
 * @date 2018/8/21
 * 订单bean
 */
public class Order  implements Serializable{

    /**
     * planCode : 运输订单编号
     * orderCode : 司机订单编号
     * driverId : 司机身份证号
     * vehicleHead : 车头车牌号
     * startNodeName : 起始节点名称
     * endNodeName : 终点节点名称
     * predictStartTime : 计划出发时间
     * predictEndTime : 计划到达时间
     * orderStatus : 订单状态(1未接单 2已接单3已拒单4运输中正常5运输中异常6正常完成 7异常结束)
     * "auditingStatus":"财务审核状态"（0待审核1 通过 2 驳回 ）
     */


    private String planCode;
    private String orderCode;
    private String driverId;
    private String vehicleHead;
    private String startNodeName;
    private String endNodeName;
    private String predictStartTime;
    private String predictEndTime;
    private String orderStatus;
    private String auditingStatus;
    private List<Goods> goods;
    private List<ContainerMission> containerMissions;
    private List<Flow> flows;

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public List<ContainerMission> getContainerMissions() {
        return containerMissions;
    }

    public void setContainerMissions(List<ContainerMission> containerMissions) {
        this.containerMissions = containerMissions;
    }

    public List<Flow> getFlows() {
        return flows;
    }

    public void setFlows(List<Flow> flows) {
        this.flows = flows;
    }

    public String getAuditingStatus() {
        return auditingStatus;
    }

    public void setAuditingStatus(String auditingStatus) {
        this.auditingStatus = auditingStatus;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getVehicleHead() {
        return vehicleHead;
    }

    public void setVehicleHead(String vehicleHead) {
        this.vehicleHead = vehicleHead;
    }

    public String getStartNodeName() {
        return startNodeName;
    }

    public void setStartNodeName(String startNodeName) {
        this.startNodeName = startNodeName;
    }

    public String getEndNodeName() {
        return endNodeName;
    }

    public void setEndNodeName(String endNodeName) {
        this.endNodeName = endNodeName;
    }

    public String getPredictStartTime() {
        return predictStartTime;
    }

    public void setPredictStartTime(String predictStartTime) {
        this.predictStartTime = predictStartTime;
    }

    public String getPredictEndTime() {
        return predictEndTime;
    }

    public void setPredictEndTime(String predictEndTime) {
        this.predictEndTime = predictEndTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
