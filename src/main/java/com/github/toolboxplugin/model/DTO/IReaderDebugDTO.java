package com.github.toolboxplugin.model.DTO;

import java.util.List;

public class IReaderDebugDTO extends Throwable {
    public String bookFictionId;
    public String bookChapterInfoUrl;
    public String bookTitle;
    public String bookAuthor;
    public String bookFictionType;
    public String bookLastReadTime;
    public String ruleName;
    public String chapterTitleRuleInfo;
    public String chapterUrlRuleInfo;
    public String contentRuleInfo;
    public List<DirectoryChapter> chapters;

    public IReaderDebugDTO setRuleName(String ruleName) {
        this.ruleName = ruleName;
        return this;
    }

    public IReaderDebugDTO setChapterTitleRuleInfo(String chapterTitleRuleInfo) {
        this.chapterTitleRuleInfo = chapterTitleRuleInfo;
        return this;
    }

    public IReaderDebugDTO setChapterUrlRuleInfo(String chapterUrlRuleInfo) {
        this.chapterUrlRuleInfo = chapterUrlRuleInfo;
        return this;
    }

    public IReaderDebugDTO setContentRuleInfo(String contentRuleInfo) {
        this.contentRuleInfo = contentRuleInfo;
        return this;
    }

    public String getRuleName() {
        return ruleName;
    }

    public String getChapterTitleRuleInfo() {
        return chapterTitleRuleInfo;
    }

    public String getChapterUrlRuleInfo() {
        return chapterUrlRuleInfo;
    }

    public String getContentRuleInfo() {
        return contentRuleInfo;
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
