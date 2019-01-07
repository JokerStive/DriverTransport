package com.tengbo.module_order.bean;

import java.util.List;

/**
 *节点模型
 */
public class Node {

    /**
     * nodeCode : 节点编码
     * nodeNumber : 节点序号
     * nodeName : 节点名称
     * nodeType : 节点类型（1系统业务节点2自动触发节点）
     * nodeLongitude : 节点经度
     * nodeLatitude : 节点维度
     * triggerDistance : 触发距离
     * processNumber : 子流程序号
     * nodeStatus : 节点状态（1未通过 2已通过）
     * arrivedTime : 到达时间
     */

    private String nodeCode;
    private int nodeNumber;
    private String nodeName;
    private int nodeType;
    private double nodeLongitude;
    private double nodeLatitude;
    private double triggerDistance;
    private int processNumber;
    private int nodeStatus;
    private String arrivedTime;

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    private List<Step> steps;

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public double getNodeLongitude() {
        return nodeLongitude;
    }

    public void setNodeLongitude(double nodeLongitude) {
        this.nodeLongitude = nodeLongitude;
    }

    public double getNodeLatitude() {
        return nodeLatitude;
    }

    public void setNodeLatitude(double nodeLatitude) {
        this.nodeLatitude = nodeLatitude;
    }

    public double getTriggerDistance() {
        return triggerDistance;
    }

    public void setTriggerDistance(long triggerDistance) {
        this.triggerDistance = triggerDistance;
    }

    public int getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(int processNumber) {
        this.processNumber = processNumber;
    }

    public int getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(int nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public String getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(String arrivedTime) {
        this.arrivedTime = arrivedTime;
    }
}
