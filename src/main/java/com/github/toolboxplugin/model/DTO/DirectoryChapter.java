package com.github.toolboxplugin.model.DTO;

/**
 * @author Administrator
 */
public class DirectoryChapter {
    private String title;
    private String chapterId;

    public DirectoryChapter setTitle(String title) {
        this.title = title;
        return this;
    }

    public DirectoryChapter setChapterId(String chapterId) {
        this.chapterId = chapterId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getChapterId() {
        return chapterId;
    }
}
