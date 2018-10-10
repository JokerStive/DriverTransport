package com.tengbo.module_order.bean;

public class StepCache {
    private int driverOrderId;
    private int stepSerialNumber;

    public int getDriverOrderId() {
        return driverOrderId;
    }

    public void setDriverOrderId(int driverOrderId) {
        this.driverOrderId = driverOrderId;
    }

    public int getStepSerialNumber() {
        return stepSerialNumber;
    }

    public void setStepSerialNumber(int stepSerialNumber) {
        this.stepSerialNumber = stepSerialNumber;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String data;
}
