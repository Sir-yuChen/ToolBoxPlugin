package com.github.toolboxplugin.model.DTO;

public class StoryDirectoryDTO {
    private String msg;
    private String code;
    private Integer count;
    private StoryDirectoryInfoDTO data;

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

    public StoryDirectoryInfoDTO getData() {
        return data;
    }

    public void setData(StoryDirectoryInfoDTO data) {
        this.data = data;
    }
}
