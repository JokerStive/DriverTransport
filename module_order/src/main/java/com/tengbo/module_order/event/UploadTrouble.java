package com.tengbo.module_order.event;

public class UploadTrouble {

    private  boolean isSuccess;

    public UploadTrouble(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
