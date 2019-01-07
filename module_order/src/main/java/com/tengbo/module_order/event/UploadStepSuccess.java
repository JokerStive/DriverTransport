package com.tengbo.module_order.event;

public class UploadStepSuccess {
    private int position;

    public UploadStepSuccess(int position) {
        setPosition(position);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
