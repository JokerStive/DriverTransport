package com.tengbo.module_order.bean;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class StepOperation extends LitePalSupport {

    public long getOperateTimeMillis() {
        return operateTimeMillis;
    }

    public void setOperateTimeMillis(long operateTimeMillis) {
        this.operateTimeMillis = operateTimeMillis;
    }

    /**
     * orderCode : 司机订单编号（必填）
     * nodeCode : 节点编号（必填）
     * processNumber : 子流程序号(必填）
     * stepSerialNumber : 步骤序号（必填）
     * stepType : 1 开始 2开始装货 3装货 4 完成装货 5开始卸货 6卸货 7完成卸货 8开始提柜 9提柜 10完成提柜 11甩柜 12送达（必填）
     * operatedTimeLength  : 已操作时长（必填）
     * containers : [{"containerCode":"货集装箱号","goodsBatch":"货物批次号"}]
     * attachRemark : 附件备注
     * attachs : [{"attach_url":"附件url"}]
     */


    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private String orderCode;
    private long operateTimeMillis;
    private String nodeCode;
    private int nodeNumber;

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    private int processNumber;
    private int stepSerialNumber;
    private int stepType;

    public int getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(int nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    private int nodeStatus;
    private long operatedTimeLength;
    private String attachRemark;
    private List<Mission> containers;
    private List<String> attachs;
    private List<String> pictureLocalPaths;

    public List<String> getPictureLocalPaths() {
        return pictureLocalPaths;
    }

    public void setPictureLocalPaths(List<String> pictureLocalPaths) {
        this.pictureLocalPaths = pictureLocalPaths;
    }
    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public int getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(int processNumber) {
        this.processNumber = processNumber;
    }

    public int getStepSerialNumber() {
        return stepSerialNumber;
    }

    public void setStepSerialNumber(int stepSerialNumber) {
        this.stepSerialNumber = stepSerialNumber;
    }

    public int getStepType() {
        return stepType;
    }

    public void setStepType(int stepType) {
        this.stepType = stepType;
    }

    public long getOperatedTimeLength() {
        return operatedTimeLength;
    }

    public void setOperatedTimeLength(long operatedTimeLength) {
        this.operatedTimeLength = operatedTimeLength;
    }

    public String getAttachRemark() {
        return attachRemark;
    }

    public void setAttachRemark(String attachRemark) {
        this.attachRemark = attachRemark;
    }

    public List<Mission> getContainers() {
        return containers;
    }

    public void setContainers(List<Mission> containers) {
        this.containers = containers;
    }

    public List<String> getAttachs() {
        return attachs;
    }

    public void setAttachs(List<String> attachs) {
        this.attachs = attachs;
    }


}
