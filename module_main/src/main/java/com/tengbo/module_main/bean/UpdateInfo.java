package com.tengbo.module_main.bean;

import java.io.Serializable;

public class UpdateInfo implements Serializable{
    private boolean isNeedUpdate;
    private boolean isFocusUpdate;
    private String updateUrl;
    private String updateDescription;

    public String getUpdateDescription() {
        return updateDescription;
    }

    public void setUpdateDescription(String updateDescription) {
        this.updateDescription = updateDescription;
    }

    public boolean isNeedUpdate() {
        return isNeedUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        isNeedUpdate = needUpdate;
    }

    public boolean isFocusUpdate() {
        return isFocusUpdate;
    }

    public void setFocusUpdate(boolean focusUpdate) {
        isFocusUpdate = focusUpdate;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }
}
