package com.tengbo.module_order.bean;

public class Goods {

    /**
     * containerCode : 集装箱编号
     * goodsBatch : 货物批次号
     * gpsCode : GPS编号
     * lockCode : 大锁编号
     * transportNumber : 货运班次
     * goodsName : 货物名称
     * goodsWeight : 货物重量
     * goodsVolume : 货物体积
     * goodsLength : 货物长度
     * goodsWidth : 货物宽度
     * goodsHeight : 货物高度
     * loadingNodeName : 装货节点名称
     * loadingNode : 装货节点编码
     * loadingContacts : 装货联系人
     * loadingContactsPhone : 装货联系方式
     * unloadingNodeName : 卸货节点名称
     * unloadingNode : 卸货节点编码
     * unloadingContacts : 卸货联系人
     * unloadingContactsPhone : 卸货联系方式
     * predictUploadStartTime : 预期装货开始时间
     * predictUploadEndTime : 预期装货完成时间
     * predictUnloadStartTime : 预期卸货开始时间
     * predictUnloadEndTime : 预期卸货完成时间
     * predictStartTime : 计划出发时间
     * predictEndTime : 计划到达时间
     */

    private String containerCode;
    private String goodsBatch;
    private String gpsCode;
    private String lockCode;
    private String transportNumber;
    private String goodsName;
    private long goodsWeight;
    private long goodsVolume;
    private long goodsLength;
    private long goodsWidth;
    private long goodsHeight;
    private String loadingNodeName;
    private String loadingNode;
    private String loadingContacts;
    private String loadingContactsPhone;
    private String unloadingNodeName;
    private String unloadingNode;
    private String unloadingContacts;
    private String unloadingContactsPhone;
    private String predictUploadStartTime;
    private String predictUploadEndTime;
    private String predictUnloadStartTime;
    private String predictUnloadEndTime;
    private String predictStartTime;
    private String predictEndTime;

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public String getGoodsBatch() {
        return goodsBatch;
    }

    public void setGoodsBatch(String goodsBatch) {
        this.goodsBatch = goodsBatch;
    }

    public String getGpsCode() {
        return gpsCode;
    }

    public void setGpsCode(String gpsCode) {
        this.gpsCode = gpsCode;
    }

    public String getLockCode() {
        return lockCode;
    }

    public void setLockCode(String lockCode) {
        this.lockCode = lockCode;
    }

    public String getTransportNumber() {
        return transportNumber;
    }

    public void setTransportNumber(String transportNumber) {
        this.transportNumber = transportNumber;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public long getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(long goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public long getGoodsVolume() {
        return goodsVolume;
    }

    public void setGoodsVolume(long goodsVolume) {
        this.goodsVolume = goodsVolume;
    }

    public long getGoodsLength() {
        return goodsLength;
    }

    public void setGoodsLength(long goodsLength) {
        this.goodsLength = goodsLength;
    }

    public long getGoodsWidth() {
        return goodsWidth;
    }

    public void setGoodsWidth(long goodsWidth) {
        this.goodsWidth = goodsWidth;
    }

    public long getGoodsHeight() {
        return goodsHeight;
    }

    public void setGoodsHeight(long goodsHeight) {
        this.goodsHeight = goodsHeight;
    }

    public String getLoadingNodeName() {
        return loadingNodeName;
    }

    public void setLoadingNodeName(String loadingNodeName) {
        this.loadingNodeName = loadingNodeName;
    }

    public String getLoadingNode() {
        return loadingNode;
    }

    public void setLoadingNode(String loadingNode) {
        this.loadingNode = loadingNode;
    }

    public String getLoadingContacts() {
        return loadingContacts;
    }

    public void setLoadingContacts(String loadingContacts) {
        this.loadingContacts = loadingContacts;
    }

    public String getLoadingContactsPhone() {
        return loadingContactsPhone;
    }

    public void setLoadingContactsPhone(String loadingContactsPhone) {
        this.loadingContactsPhone = loadingContactsPhone;
    }

    public String getUnloadingNodeName() {
        return unloadingNodeName;
    }

    public void setUnloadingNodeName(String unloadingNodeName) {
        this.unloadingNodeName = unloadingNodeName;
    }

    public String getUnloadingNode() {
        return unloadingNode;
    }

    public void setUnloadingNode(String unloadingNode) {
        this.unloadingNode = unloadingNode;
    }

    public String getUnloadingContacts() {
        return unloadingContacts;
    }

    public void setUnloadingContacts(String unloadingContacts) {
        this.unloadingContacts = unloadingContacts;
    }

    public String getUnloadingContactsPhone() {
        return unloadingContactsPhone;
    }

    public void setUnloadingContactsPhone(String unloadingContactsPhone) {
        this.unloadingContactsPhone = unloadingContactsPhone;
    }

    public String getPredictUploadStartTime() {
        return predictUploadStartTime;
    }

    public void setPredictUploadStartTime(String predictUploadStartTime) {
        this.predictUploadStartTime = predictUploadStartTime;
    }

    public String getPredictUploadEndTime() {
        return predictUploadEndTime;
    }

    public void setPredictUploadEndTime(String predictUploadEndTime) {
        this.predictUploadEndTime = predictUploadEndTime;
    }

    public String getPredictUnloadStartTime() {
        return predictUnloadStartTime;
    }

    public void setPredictUnloadStartTime(String predictUnloadStartTime) {
        this.predictUnloadStartTime = predictUnloadStartTime;
    }

    public String getPredictUnloadEndTime() {
        return predictUnloadEndTime;
    }

    public void setPredictUnloadEndTime(String predictUnloadEndTime) {
        this.predictUnloadEndTime = predictUnloadEndTime;
    }

    public String getPredictStartTime() {
        return predictStartTime;
    }

    public void setPredictStartTime(String predictStartTime) {
        this.predictStartTime = predictStartTime;
    }

    public String getPredictEndTime() {
        return predictEndTime;
    }

    public void setPredictEndTime(String predictEndTime) {
        this.predictEndTime = predictEndTime;
    }
}
