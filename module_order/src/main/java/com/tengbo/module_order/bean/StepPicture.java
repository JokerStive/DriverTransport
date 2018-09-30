package com.tengbo.module_order.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**步骤图片需要上链的数据
*@Autor yk
*@Description
*/
public class StepPicture implements Parcelable {
    private String address;
    private long longtitude;
    private long latitude;
    private String time;
    private String picturePath;
    private String orderId;
    private String stepName;
    private String stepNum;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(long longtitude) {
        this.longtitude = longtitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepNum() {
        return stepNum;
    }

    public void setStepNum(String stepNum) {
        this.stepNum = stepNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeLong(this.longtitude);
        dest.writeLong(this.latitude);
        dest.writeString(this.time);
        dest.writeString(this.picturePath);
        dest.writeString(this.orderId);
        dest.writeString(this.stepName);
        dest.writeString(this.stepNum);
    }

    public StepPicture() {
    }

    protected StepPicture(Parcel in) {
        this.address = in.readString();
        this.longtitude = in.readLong();
        this.latitude = in.readLong();
        this.time = in.readString();
        this.picturePath = in.readString();
        this.orderId = in.readString();
        this.stepName = in.readString();
        this.stepNum = in.readString();
    }

    public static final Parcelable.Creator<StepPicture> CREATOR = new Parcelable.Creator<StepPicture>() {
        @Override
        public StepPicture createFromParcel(Parcel source) {
            return new StepPicture(source);
        }

        @Override
        public StepPicture[] newArray(int size) {
            return new StepPicture[size];
        }
    };
}
