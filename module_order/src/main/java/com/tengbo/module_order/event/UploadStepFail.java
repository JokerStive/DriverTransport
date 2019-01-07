package com.tengbo.module_order.event;

public class UploadStepFail {
    private int position;

    public UploadStepFail(int position) {
        setPosition(position);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
