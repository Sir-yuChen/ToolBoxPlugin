package com.github.toolboxplugin.model.DTO;

public class IReaderDebugRuleDTO {
    public String ruleName;
    public String chapterRuleInfo;
    public String contentRuleInfo;

    public IReaderDebugRuleDTO setRuleName(String ruleName) {
        this.ruleName = ruleName;
        return this;
    }

    public IReaderDebugRuleDTO setChapterRuleInfo(String chapterRuleInfo) {
        this.chapterRuleInfo = chapterRuleInfo;
        return this;
    }

    public IReaderDebugRuleDTO setContentRuleInfo(String contentRuleInfo) {
        this.contentRuleInfo = contentRuleInfo;
        return this;
    }

    public String getRuleName() {
        return ruleName;
    }

    public String getChapterRuleInfo() {
        return chapterRuleInfo;
    }

    public String getContentRuleInfo() {
        return contentRuleInfo;
    }
}
