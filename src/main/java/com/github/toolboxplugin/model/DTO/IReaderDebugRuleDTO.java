package com.github.toolboxplugin.model.DTO;

public class IReaderDebugRuleDTO {
    public String ruleName;
    public String chapterTitleRuleInfo;
    public String chapterUrlRuleInfo;
    public String contentRuleInfo;

    public IReaderDebugRuleDTO setRuleName(String ruleName) {
        this.ruleName = ruleName;
        return this;
    }

    public IReaderDebugRuleDTO setChapterTitleRuleInfo(String chapterTitleRuleInfo) {
        this.chapterTitleRuleInfo = chapterTitleRuleInfo;
        return this;
    }

    public IReaderDebugRuleDTO setChapterUrlRuleInfo(String chapterUrlRuleInfo) {
        this.chapterUrlRuleInfo = chapterUrlRuleInfo;
        return this;
    }

    public IReaderDebugRuleDTO setContentRuleInfo(String contentRuleInfo) {
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
}
