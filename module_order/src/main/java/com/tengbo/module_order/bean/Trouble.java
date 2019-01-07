package com.tengbo.module_order.bean;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class Trouble extends LitePalSupport {
    public String getTroubleType() {
        return troubleType;
    }

    public double getTroubleLongitude() {
        return troubleLongitude;
    }

    public double getTroubleLatitude() {
        return troubleLatitude;
    }

    public int getTroubleLevel() {
        return troubleLevel;
    }

    public long getHandlingTimeLength() {
        return handlingTimeLength;
    }

    public long getTroubleTimeLength() {
        return troubleTimeLength;
    }

    public String getTroubleRemark() {
        return troubleRemark;
    }

    public void setTroubleRemark(String troubleRemark) {
        this.troubleRemark = troubleRemark;
    }

    public List<String> getAttachs() {
        return attachs;
    }

    public void setAttachs(List<String> attachs) {
        this.attachs = attachs;
    }

    public String getTroubleId() {
        return troubleId;
    }

    public void setTroubleId(String troubleId) {
        this.troubleId = troubleId;
    }

    public int getTroubleStatus() {
        return troubleStatus;
    }

    public void setTroubleStatus(int troubleStatus) {
        this.troubleStatus = troubleStatus;
    }

    /**
     * orderCode : 司机订单编号（必填)
     * vehicleHead : 车头牌号（必填）
     * troubleType : 故障类型（必填）（取自字典）
     * nodeCode : 当前节点所在编码（必填）
     * troubleLocation : 故障位置（经纬度反编码）
     * troubleLongitude : 故障经度（必填）
     * troubleLatitude : 故障维度（必填）
     * troubleLevel : 故障等级（1 无法继续运输（需要派车支援）2 时效无法满足，但可以修复（影响运输计划）3时效可满足）（必填）
     * handlingTimeLength  : 处理时长（必填）
     * troubleTimeLength : 故障时长（必填）
     * troubleRemark : 故障备注

     * attachs : ["attach_url"]

     */

    private String orderCode;
    private String troubleId;
    private String vehicleHead;
    private String troubleType;
    private String nodeCode;
    private String troubleLocation;
    private double troubleLongitude;
    private double troubleLatitude;
    private int troubleLevel;
    private int troubleStatus;

    public void setTroubleType(String troubleType) {
        this.troubleType = troubleType;
    }

    public void setTroubleLongitude(double troubleLongitude) {
        this.troubleLongitude = troubleLongitude;
    }

    public void setTroubleLatitude(double troubleLatitude) {
        this.troubleLatitude = troubleLatitude;
    }

    public void setTroubleLevel(int troubleLevel) {
        this.troubleLevel = troubleLevel;
    }

    public void setHandlingTimeLength(long handlingTimeLength) {
        this.handlingTimeLength = handlingTimeLength;
    }

    public void setTroubleTimeLength(long troubleTimeLength) {
        this.troubleTimeLength = troubleTimeLength;
    }

    public long getOperateTimeMillis() {
        return operateTimeMillis;
    }

    public void setOperateTimeMillis(long operateTimeMillis) {
        this.operateTimeMillis = operateTimeMillis;
    }

    public List<String> getPictureLocalPaths() {
        return pictureLocalPaths;
    }

    public void setPictureLocalPaths(List<String> pictureLocalPaths) {
        this.pictureLocalPaths = pictureLocalPaths;
    }

    private long handlingTimeLength;
    private long troubleTimeLength;
    private String troubleRemark;
    private long operateTimeMillis;
    private List<String> attachs;
    private List<String> pictureLocalPaths;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getVehicleHead() {
        return vehicleHead;
    }

    public void setVehicleHead(String vehicleHead) {
        this.vehicleHead = vehicleHead;
    }


    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getTroubleLocation() {
        return troubleLocation;
    }

    public void setTroubleLocation(String troubleLocation) {
        this.troubleLocation = troubleLocation;
    }

}
