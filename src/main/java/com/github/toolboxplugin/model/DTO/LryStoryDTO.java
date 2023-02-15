package com.github.toolboxplugin.model.DTO;


public class LryStoryDTO {
    private String fictionId;
    private String title;
    private String author;
    private String fictionType;
    private String descs;
    private String cover;
    private String updateTime;

    public String getFictionId() {
        return fictionId;
    }

    public void setFictionId(String fictionId) {
        this.fictionId = fictionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFictionType() {
        return fictionType;
    }

    public void setFictionType(String fictionType) {
        this.fictionType = fictionType;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "LryStoryDTO{" +
                "fictionId='" + fictionId + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", fictionType='" + fictionType + '\'' +
                ", descs='" + descs + '\'' +
                ", cover='" + cover + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
