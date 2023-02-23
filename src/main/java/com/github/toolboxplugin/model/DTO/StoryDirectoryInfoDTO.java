package com.github.toolboxplugin.model.DTO;

import java.util.List;

public class StoryDirectoryInfoDTO {
    private String fictionId;
    private String title;
    private String descs;
    private String author;
    private String fictionType;
    private String updateTime;
    private List<DirectoryChapter> chapterList;

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

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<DirectoryChapter> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<DirectoryChapter> chapterList) {
        this.chapterList = chapterList;
    }
}