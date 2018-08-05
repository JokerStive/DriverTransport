package com.tengbo.commonlibrary.net;

/**
 * api异常，统一处理
 */
public class ApiException extends RuntimeException {
    private int errorCode;
    private String errorMessage;

    public ApiException(int code, String message) {
        this.errorCode = code;
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


    public int getErrorCode() {
        return errorCode;
    }


}
