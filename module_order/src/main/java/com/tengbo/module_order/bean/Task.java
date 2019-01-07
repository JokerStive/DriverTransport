package com.tengbo.module_order.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Task  implements Parcelable {

    /**
     * taskId : 任务id
     * createTime : 开始时间
     * operationTime : 操作时间
     * containerCode : 集装箱号
     * operationType : 操作类型（1 靠台 2 开始装货 3 完成装货 4甩重柜）
     */

    private String taskId;
    private String createTime;
    private String operationTime;
    private String containerCode;
    private String operationType;

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }

    public long getRecordTimeLength() {
        return recordTimeLength;
    }

    public void setRecordTimeLength(long recordTimeLength) {
        this.recordTimeLength = recordTimeLength;
    }

    public List<String> getAttachs() {
        return attachs;
    }

    public void setAttachs(List<String> attachs) {
        this.attachs = attachs;
    }

    private String nodeCode;
    private String plateNumber;
    private String driverId;
    private String dropAddress;
    private long recordTimeLength;
    private List<String> attachs;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.taskId);
        dest.writeString(this.createTime);
        dest.writeString(this.operationTime);
        dest.writeString(this.containerCode);
        dest.writeString(this.operationType);
        dest.writeString(this.nodeCode);
        dest.writeString(this.plateNumber);
        dest.writeString(this.driverId);
        dest.writeString(this.dropAddress);
        dest.writeLong(this.recordTimeLength);
        dest.writeStringList(this.attachs);
    }

    public Task() {
    }

    protected Task(Parcel in) {
        this.taskId = in.readString();
        this.createTime = in.readString();
        this.operationTime = in.readString();
        this.containerCode = in.readString();
        this.operationType = in.readString();
        this.nodeCode = in.readString();
        this.plateNumber = in.readString();
        this.driverId = in.readString();
        this.dropAddress = in.readString();
        this.recordTimeLength = in.readLong();
        this.attachs = in.createStringArrayList();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
