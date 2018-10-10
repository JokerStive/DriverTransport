package com.tengbo.module_order.bean;

public class ContainerMission {


    /**
     * containerCode : 集装箱号
     * pickNodeCode : 提柜节点编号
     * expectPickTime : 预期提柜时间
     * dropNodeCode : 甩柜节点编码
     * taskStatus : 任务状态
     * remark : 备注
     */

    private String containerCode;
    private String pickNodeCode;
    private String expectPickTime;
    private String dropNodeCode;
    private String taskStatus;
    private String remark;

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public String getPickNodeCode() {
        return pickNodeCode;
    }

    public void setPickNodeCode(String pickNodeCode) {
        this.pickNodeCode = pickNodeCode;
    }

    public String getExpectPickTime() {
        return expectPickTime;
    }

    public void setExpectPickTime(String expectPickTime) {
        this.expectPickTime = expectPickTime;
    }

    public String getDropNodeCode() {
        return dropNodeCode;
    }

    public void setDropNodeCode(String dropNodeCode) {
        this.dropNodeCode = dropNodeCode;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
