package com.github.toolboxplugin.model;


import org.apache.commons.lang3.StringUtils;

public enum ReturnCode {
    SUCCESS_MUSIC("200", "success"),

    //login
    LOGIN_QR_STATUS_TIME_OVER("800", "二维码过期"),
    LOGIN_QR_STATUS_WAIT("801", "等待扫码"),
    LOGIN_QR_STATUS_confirmed("802", "待确认"),
    LOGIN_QR_STATUS_success("803", "授权登录成功"),
    // 服务器内部错误;
    UNKNOWN_ERROR("8888", "未知错误");


    private String code;
    private String message;

    private ReturnCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ReturnCode getByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (ReturnCode e : values()) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
        }
        return null;
    }


}
