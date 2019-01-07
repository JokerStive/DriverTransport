package com.tengbo.module_order.bean;

import java.util.List;

public class DutyTask {
    private int dutyStatus;
    private String nodeCode;
    private String nodeName;
    private String plateNumber;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    List<Task> tasks;

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public int getDutyStatus() {
        return dutyStatus;
    }

    public void setDutyStatus(int dutyStatus) {
        this.dutyStatus = dutyStatus;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
