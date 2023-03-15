package com.github.toolboxplugin.model.music;

import java.util.List;


public class NewSongListDto {

    private String code;
    private String category;
    private List<songAttribute> result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<songAttribute> getResult() {
        return result;
    }

    public void setResult(List<songAttribute> result) {
        this.result = result;
    }
}
