package com.github.toolboxplugin.model.DTO;


public  class StoryBookshelfDTO {
    //属性必须被public修饰
    public String fictionId;
    public String title;
    public String author;
    public String fictionType;
    public String LastReadTime;
    public DirectoryChapter chapter;

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

    public String getLastReadTime() {
        return LastReadTime;
    }

    public void setLastReadTime(String lastReadTime) {
        LastReadTime = lastReadTime;
    }

    public DirectoryChapter getChapter() {
        return chapter;
    }

    public void setChapter(DirectoryChapter chapter) {
        this.chapter = chapter;
    }
}
