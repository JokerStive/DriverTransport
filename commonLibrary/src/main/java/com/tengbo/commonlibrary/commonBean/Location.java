package com.tengbo.commonlibrary.commonBean;

import org.litepal.crud.LitePalSupport;

/**
 * 存入数据库的定位信息
 *
 * @Autor yk
 * @Description
 */
public class Location extends LitePalSupport {
    private double latitude;
    private double longitude;
    private boolean upload;
    private String address;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
