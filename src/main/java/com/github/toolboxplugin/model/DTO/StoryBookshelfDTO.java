package com.github.toolboxplugin.model.DTO;

public class StoryBookshelfDTO {
    //属性必须被public修饰
    public String fictionId;
    public String bookSource;//图书来源
    public String title;
    public String author;
    public String fictionType;
    public String lastReadTime;
    public DirectoryChapter chapter;

    public String getLastReadTime() {
        return lastReadTime;
    }

    public StoryBookshelfDTO setLastReadTime(String lastReadTime) {
        this.lastReadTime = lastReadTime;
        return this;
    }

    public String getFictionId() {
        return fictionId;
    }

    public StoryBookshelfDTO setFictionId(String fictionId) {
        this.fictionId = fictionId;
        return this;
    }

    public StoryBookshelfDTO setBookSource(String bookSource) {
        this.bookSource = bookSource;
        return this;
    }

    public String getBookSource() {
        return bookSource;
    }

    public String getTitle() {
        return title;
    }

    public StoryBookshelfDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public StoryBookshelfDTO setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getFictionType() {
        return fictionType;
    }

    public StoryBookshelfDTO setFictionType(String fictionType) {
        this.fictionType = fictionType;
        return this;
    }

    public DirectoryChapter getChapter() {
        return chapter;
    }

    public StoryBookshelfDTO setChapter(DirectoryChapter chapter) {
        this.chapter = chapter;
        return this;
    }
}
