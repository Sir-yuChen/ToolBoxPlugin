package com.github.toolboxplugin.model.DTO;

/**
 * @author Administrator
 */
public class DirectoryChapter {
    private String title;
    private String chapterId;
    private String chapterPrefixUrl;
    private String chapterUrl;

    public DirectoryChapter setTitle(String title) {
        this.title = title;
        return this;
    }

    public DirectoryChapter setChapterId(String chapterId) {
        this.chapterId = chapterId;
        return this;
    }

    public DirectoryChapter setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
        return this;
    }

    public DirectoryChapter setChapterPrefixUrl(String chapterPrefixUrl) {
        this.chapterPrefixUrl = chapterPrefixUrl;
        return this;
    }

    public String getChapterPrefixUrl() {
        return chapterPrefixUrl;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getChapterId() {
        return chapterId;
    }
}
