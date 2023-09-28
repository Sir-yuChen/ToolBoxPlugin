package com.github.toolboxplugin.model.DTO;

import com.intellij.openapi.project.Project;

import java.util.List;

public class IReaderDebugDTO {
    public String bookFictionId;
    public String bookChapterInfoUrl;
    public String bookTitle;
    public String bookAuthor;
    public String bookFictionType;
    public String bookLastReadTime;
    public Project project;
    public List<DirectoryChapter> chapters;
    public IReaderDebugRuleDTO iReaderDebugRuleDTO;

    public IReaderDebugRuleDTO getiReaderDebugRuleDTO() {
        return iReaderDebugRuleDTO;
    }

    public IReaderDebugDTO setiReaderDebugRuleDTO(IReaderDebugRuleDTO iReaderDebugRuleDTO) {
        this.iReaderDebugRuleDTO = iReaderDebugRuleDTO;
        return this;
    }

    public IReaderDebugDTO setBookFictionId(String bookFictionId) {
        this.bookFictionId = bookFictionId;
        return this;
    }

    public IReaderDebugDTO setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }

    public IReaderDebugDTO setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
        return this;
    }

    public IReaderDebugDTO setBookFictionType(String bookFictionType) {
        this.bookFictionType = bookFictionType;
        return this;
    }

    public IReaderDebugDTO setBookLastReadTime(String bookLastReadTime) {
        this.bookLastReadTime = bookLastReadTime;
        return this;
    }

    public IReaderDebugDTO setChapters(List<DirectoryChapter> chapters) {
        this.chapters = chapters;
        return this;
    }

    public IReaderDebugDTO setBookChapterInfoUrl(String bookChapterInfoUrl) {
        this.bookChapterInfoUrl = bookChapterInfoUrl;
        return this;
    }

    public IReaderDebugDTO setProject(Project project) {
        this.project = project;
        return this;
    }

    public Project getProject() {
        return project;
    }

    public String getBookChapterInfoUrl() {
        return bookChapterInfoUrl;
    }

    public String getBookFictionId() {
        return bookFictionId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookFictionType() {
        return bookFictionType;
    }

    public String getBookLastReadTime() {
        return bookLastReadTime;
    }

    public List<DirectoryChapter> getChapters() {
        return chapters;
    }
}
