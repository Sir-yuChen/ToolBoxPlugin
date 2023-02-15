package com.github.toolboxplugin.model.DTO;

import java.util.List;

public class LryStoryResultDTO {
    private String msg;
    private String code;
    private Integer count;
    private List<LryStoryDTO> data;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<LryStoryDTO> getData() {
        return data;
    }

    public void setData(List<LryStoryDTO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LryStoryResultDTO{" +
                "msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", count=" + count +
                ", data=" + data +
                '}';
    }
}
