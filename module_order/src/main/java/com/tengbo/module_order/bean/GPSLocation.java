package com.tengbo.module_order.bean;

import org.litepal.crud.LitePalSupport;

public class GPSLocation extends LitePalSupport {

    @Override
    public String toString() {
        return "GPSLocation{" +
                "plateNumber='" + plateNumber + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", deviceCode='" + deviceCode + '\'' +
                ", longitude=" + longitude +
                ", upload=" + upload +
                ", latitude=" + latitude +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", address='" + address + '\'' +
                ", receiveTime='" + receiveTime + '\'' +
                '}';
    }

    /**
     * plateNumber : 车牌号（必填）
     * orderCode : 订单编号（必填）
     * deviceCode : 设备编号（必填）
     * longitude : 经度（必填）
     * latitude : 纬度（必填）
     * province : 省
     * city : 市
     * county : 区县
     * address : 地址
     * receiveTime : 接收时间（必填）
     */

    private String plateNumber;
    private String orderCode;
    private String deviceCode;
    private double longitude;
    private boolean upload;
    private double latitude;
    private String province;
    private String city;
    private String county;

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    private String address;
    private String receiveTime;

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {

        this.receiveTime = receiveTime;
    }
}
