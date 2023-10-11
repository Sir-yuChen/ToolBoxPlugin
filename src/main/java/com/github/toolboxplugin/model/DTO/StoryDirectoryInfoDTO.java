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

    public StoryDirectoryInfoDTO setFictionId(String fictionId) {
        this.fictionId = fictionId;
        return this;
    }

    public StoryDirectoryInfoDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public StoryDirectoryInfoDTO setDescs(String descs) {
        this.descs = descs;
        return this;
    }

    public StoryDirectoryInfoDTO setAuthor(String author) {
        this.author = author;
        return this;
    }

    public StoryDirectoryInfoDTO setFictionType(String fictionType) {
        this.fictionType = fictionType;
        return this;
    }

    public StoryDirectoryInfoDTO setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public StoryDirectoryInfoDTO setChapterList(List<DirectoryChapter> chapterList) {
        this.chapterList = chapterList;
        return this;
    }

    public String getFictionId() {
        return fictionId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescs() {
        return descs;
    }

    public String getAuthor() {
        return author;
    }

    public String getFictionType() {
        return fictionType;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public List<DirectoryChapter> getChapterList() {
        return chapterList;
    }
}