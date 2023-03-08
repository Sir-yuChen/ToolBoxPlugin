package com.github.toolboxplugin.model.music;

import com.github.toolboxplugin.model.ReturnCode;

public class ReturnCommon<T> {
    private String code;
    private String msg;
    /**
     * 返回的数据
     */
    private T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 设置返回的状态码及提示信息
     */
    public void setApiReturnCode(ReturnCode apiReturnCode) {
        this.code = apiReturnCode.getCode();
        this.msg = apiReturnCode.getMessage();
    }

}
