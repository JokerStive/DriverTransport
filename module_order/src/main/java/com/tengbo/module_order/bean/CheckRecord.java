package com.tengbo.module_order.bean;

public class CheckRecord {

    /**
     * plateNumber : 车牌号（必填）
     * operatorId : 执行人员身份证号（必填）
     * inspectionStatus : 检查状态（1未检查 2已检查通过3已检查未通过）
     * inspectionDes : 检查说明
     */

    private String plateNumber;

    public int getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(int inspectionType) {
        this.inspectionType = inspectionType;
    }

    private String operatorId;
    private int inspectionStatus;
    private String inspectionDes;
    private int inspectionType=4;

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public int getInspectionStatus() {
        return inspectionStatus;
    }

    public void setInspectionStatus(int inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
    }

    public String getInspectionDes() {
        return inspectionDes;
    }

    public void setInspectionDes(String inspectionDes) {
        this.inspectionDes = inspectionDes;
    }
}
