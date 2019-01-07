package com.tengbo.module_order.bean;

public class Dictionary {


    /**
     * itemId : 条目id
     * typeCode : 类型编码
     * itemValue : 条目值
     * itemName : 条目名称
     * typeDes : 条目描述
     */

    private String itemId;
    private String typeCode;
    private String itemValue;
    private String itemName;
    private String typeDes;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getTypeDes() {
        return typeDes;
    }

    public void setTypeDes(String typeDes) {
        this.typeDes = typeDes;
    }
}
