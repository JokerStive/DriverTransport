package com.tengbo.module_order.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 步骤模型
 * @author yk_de
 */
public class Step implements Parcelable {


    /**
     * processNumber : 子流程序号
     * stepName : 步骤名称
     * stepType : 1 开始 2开始装货 3装货 4 完成装货 5开始卸货 6卸货 7完成卸货 8开始提柜 9提柜 10完成提柜 11甩柜 12送达
     * stepSerialNumber : 步骤序号
     * processNecessary : 是否必须执行（1必须2可以跳过）
     * stepStatus : 步骤状态（0未执行 1已执行）
     * executeTime : 执行时间
     * stepDes : 步骤描述
     */

    private int processNumber;
    private String stepName;
    private String nodeName;
    private int position;
    private String excuterName;
    private String excuterCellPhone;
    private String positionName;
    private String positionCode;

    private int stepType;

    private int nodeNumber;

    private String vehicleHead;
    private String transportEnterpriseName;

    public String getTransportEnterpriseName() {
        return transportEnterpriseName;
    }

    public void setTransportEnterpriseName(String transportEnterpriseName) {
        this.transportEnterpriseName = transportEnterpriseName;
    }

    public String getVehicleTrailer() {
        return vehicleTrailer;
    }

    public void setVehicleTrailer(String vehicleTrailer) {
        this.vehicleTrailer = vehicleTrailer;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverCellPhone() {
        return driverCellPhone;
    }

    public void setDriverCellPhone(String driverCellPhone) {
        this.driverCellPhone = driverCellPhone;
    }

    private String vehicleTrailer;
    private String driverName;
    private String driverCellPhone;
    private int nodeStatus;
    private int stepSerialNumber;
    private int processNecessary;
    private int stepStatus;
    private String executeTime;
    private String stepDes;
    private List<Goods> targets;

    //组装的数据
    private String orderCode;
    private String nodeCode;
    private int nodeType; //（1系统业务节点2自动触发节点）
    private boolean isCached; //是否有缓存
    private boolean isProcessed; //是否执行了
    private double nodeLongitude; //节点精度
    private double nodeLatitude; //节点维度

    public List<Goods> getTargets() {
        return targets;
    }

    public void setTargets(List<Goods> targets) {
        this.targets = targets;
    }

    private double triggerDistance; //触发距离


    public String getExcuterName() {
        return excuterName;
    }

    public void setExcuterName(String excuterName) {
        this.excuterName = excuterName;
    }

    public String getExcuterCellPhone() {
        return excuterCellPhone;
    }

    public void setExcuterCellPhone(String excuterCellPhone) {
        this.excuterCellPhone = excuterCellPhone;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }



    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
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

    public String getVehicleHead() {
        return vehicleHead;
    }

    public void setVehicleHead(String vehicleHead) {
        this.vehicleHead = vehicleHead;
    }

    public int getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(int nodeStatus) {
        this.nodeStatus = nodeStatus;
    }



    public List<String> getAttachs() {
        return attachs;
    }

    public void setAttachs(List<String> attachs) {
        this.attachs = attachs;
    }

    private List<String> attachs;



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

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public void setTriggerDistance(double triggerDistance) {
        this.triggerDistance = triggerDistance;

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


    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }


    public boolean isCached() {
        return isCached;
    }

    public void setCached(boolean cached) {
        isCached = cached;
    }

    public int getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(int processNumber) {
        this.processNumber = processNumber;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public int getStepType() {
        return stepType;
    }

    public void setStepType(int stepType) {
        this.stepType = stepType;
    }

    public int getStepSerialNumber() {
        return stepSerialNumber;
    }

    public void setStepSerialNumber(int stepSerialNumber) {
        this.stepSerialNumber = stepSerialNumber;
    }

    public int getProcessNecessary() {
        return processNecessary;
    }

    public void setProcessNecessary(int processNecessary) {
        this.processNecessary = processNecessary;
    }

    public int getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(int stepStatus) {
        this.stepStatus = stepStatus;
        setProcessed(stepStatus == 1);
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    public String getStepDes() {
        return stepDes;
    }

    public void setStepDes(String stepDes) {
        this.stepDes = stepDes;
    }


    public Step() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.processNumber);
        dest.writeString(this.stepName);
        dest.writeString(this.nodeName);
        dest.writeInt(this.position);
        dest.writeString(this.excuterName);
        dest.writeString(this.excuterCellPhone);
        dest.writeString(this.positionName);
        dest.writeString(this.positionCode);
        dest.writeInt(this.stepType);
        dest.writeInt(this.nodeNumber);
        dest.writeString(this.vehicleHead);
        dest.writeInt(this.nodeStatus);
        dest.writeInt(this.stepSerialNumber);
        dest.writeInt(this.processNecessary);
        dest.writeInt(this.stepStatus);
        dest.writeString(this.executeTime);
        dest.writeString(this.stepDes);
        dest.writeList(this.targets);
        dest.writeString(this.orderCode);
        dest.writeString(this.nodeCode);
        dest.writeInt(this.nodeType);
        dest.writeByte(this.isCached ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isProcessed ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.nodeLongitude);
        dest.writeDouble(this.nodeLatitude);
        dest.writeDouble(this.triggerDistance);
        dest.writeStringList(this.attachs);
    }

    protected Step(Parcel in) {
        this.processNumber = in.readInt();
        this.stepName = in.readString();
        this.nodeName = in.readString();
        this.position = in.readInt();
        this.excuterName = in.readString();
        this.excuterCellPhone = in.readString();
        this.positionName = in.readString();
        this.positionCode = in.readString();
        this.stepType = in.readInt();
        this.nodeNumber = in.readInt();
        this.vehicleHead = in.readString();
        this.nodeStatus = in.readInt();
        this.stepSerialNumber = in.readInt();
        this.processNecessary = in.readInt();
        this.stepStatus = in.readInt();
        this.executeTime = in.readString();
        this.stepDes = in.readString();
        this.targets = new ArrayList<Goods>();
        in.readList(this.targets, Goods.class.getClassLoader());
        this.orderCode = in.readString();
        this.nodeCode = in.readString();
        this.nodeType = in.readInt();
        this.isCached = in.readByte() != 0;
        this.isProcessed = in.readByte() != 0;
        this.nodeLongitude = in.readDouble();
        this.nodeLatitude = in.readDouble();
        this.triggerDistance = in.readDouble();
        this.attachs = in.createStringArrayList();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
