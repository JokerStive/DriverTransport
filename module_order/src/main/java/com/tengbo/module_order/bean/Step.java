package com.tengbo.module_order.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 步骤模型
 */
public class Step implements Parcelable {
    private String nodeCode; //父节点id
    private String nodeName; //父节点名称
    private String stepName;
    private String stepType; //0 其他 1 开始 2到达 3装货
    private int stepSerialNumber; //编码
    private String startedTime;
    private String finishedTime;
    private int nodeType =1;  // 1系统业务节点 2自动触发节点
    private int processNecessary = 1;  // 1必须 2可以跳过
    private boolean processed;  // 是否已经执行过
    private boolean driggerLongitude;  // 精度
    private boolean driggerlatitude;  // 维度
    private int driggerDistance;  // 触发距离


    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public int getStepSerialNumber() {
        return stepSerialNumber;
    }

    public void setStepSerialNumber(int stepSerialNumber) {
        this.stepSerialNumber = stepSerialNumber;
    }

    public String getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(String startedTime) {
        this.startedTime = startedTime;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public int getProcessNecessary() {
        return processNecessary;
    }

    public void setProcessNecessary(int processNecessary) {
        this.processNecessary = processNecessary;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public boolean isDriggerLongitude() {
        return driggerLongitude;
    }

    public void setDriggerLongitude(boolean driggerLongitude) {
        this.driggerLongitude = driggerLongitude;
    }

    public boolean isDriggerlatitude() {
        return driggerlatitude;
    }

    public void setDriggerlatitude(boolean driggerlatitude) {
        this.driggerlatitude = driggerlatitude;
    }

    public int getDriggerDistance() {
        return driggerDistance;
    }

    public void setDriggerDistance(int driggerDistance) {
        this.driggerDistance = driggerDistance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nodeCode);
        dest.writeString(this.nodeName);
        dest.writeString(this.stepName);
        dest.writeString(this.stepType);
        dest.writeInt(this.stepSerialNumber);
        dest.writeString(this.startedTime);
        dest.writeString(this.finishedTime);
        dest.writeInt(this.nodeType);
        dest.writeInt(this.processNecessary);
        dest.writeByte(this.processed ? (byte) 1 : (byte) 0);
        dest.writeByte(this.driggerLongitude ? (byte) 1 : (byte) 0);
        dest.writeByte(this.driggerlatitude ? (byte) 1 : (byte) 0);
        dest.writeInt(this.driggerDistance);
    }

    public Step() {
    }

    protected Step(Parcel in) {
        this.nodeCode = in.readString();
        this.nodeName = in.readString();
        this.stepName = in.readString();
        this.stepType = in.readString();
        this.stepSerialNumber = in.readInt();
        this.startedTime = in.readString();
        this.finishedTime = in.readString();
        this.nodeType = in.readInt();
        this.processNecessary = in.readInt();
        this.processed = in.readByte() != 0;
        this.driggerLongitude = in.readByte() != 0;
        this.driggerlatitude = in.readByte() != 0;
        this.driggerDistance = in.readInt();
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
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
