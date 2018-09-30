package com.tengbo.module_order.bean;

import java.util.List;

/**
 *节点模型
 */
public class Node {
    private String planCode; //计划编码
    private int nodeType;   //节点类型  1系统业务节点 2自动触发节点
    private int nodeNumber;   //节点序号
    private String nodeCode;   //节点编码
    private long nodeLongitude;   //精度
    private long nodeLatitude;   //维度
    private int driggerDistance;   //出发距离
    private String arrivedTime;   //到达时间
    private String leftTime;   //离开时间
    private List<Step> steps;   //步骤

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public long getNodeLongitude() {
        return nodeLongitude;
    }

    public void setNodeLongitude(long nodeLongitude) {
        this.nodeLongitude = nodeLongitude;
    }

    public long getNodeLatitude() {
        return nodeLatitude;
    }

    public void setNodeLatitude(long nodeLatitude) {
        this.nodeLatitude = nodeLatitude;
    }

    public int getDriggerDistance() {
        return driggerDistance;
    }

    public void setDriggerDistance(int driggerDistance) {
        this.driggerDistance = driggerDistance;
    }

    public String getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(String arrivedTime) {
        this.arrivedTime = arrivedTime;
    }

    public String getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(String leftTime) {
        this.leftTime = leftTime;
    }
}
