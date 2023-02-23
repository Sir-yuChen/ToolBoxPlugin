package com.github.toolboxplugin.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.toolboxplugin.config.GlobalConstant;
import com.github.toolboxplugin.model.DTO.LryStoryResultDTO;
import com.github.toolboxplugin.model.DTO.StoryContentDTO;
import com.github.toolboxplugin.model.DTO.StoryDirectoryDTO;
import com.github.toolboxplugin.utils.OkHttpUtil;
import com.github.toolboxplugin.utils.PropertiesUtil;

import java.util.ArrayList;
import java.util.List;

public class StoryService {

    public LryStoryResultDTO searchStory(String selectBoxText, String searchWord, Integer currentPage, Integer pageSize) {
        String ipApiUrl = PropertiesUtil.readProperties(GlobalConstant.SEO_HTTP_URL, "toolBoxPlugin.seo.lryApi");
        LryStoryResultDTO lryStoryResultDTO = null;
        String data = OkHttpUtil.builder().url(ipApiUrl + "/fiction/search/")
                .addHeader("Content-Type", "text/html;charset=utf-8")
                .addParam("option", selectBoxText == null ? "title" : selectBoxText)
                .addParam("key", searchWord)
                .addParam("from", currentPage.toString())
                .addParam("size", pageSize.toString())
                .addParameter("/")
                .get()
                .sync();
        lryStoryResultDTO = JSONObject.toJavaObject(JSON.parseObject(data), LryStoryResultDTO.class);
        return lryStoryResultDTO;
    }

    public StoryDirectoryDTO getStoryDirectory(String fictionId) {
        String ipApiUrl = PropertiesUtil.readProperties(GlobalConstant.SEO_HTTP_URL, "toolBoxPlugin.seo.lryApi");
        StoryDirectoryDTO storyDirectoryDTO = null;
        String data = OkHttpUtil.builder().url(ipApiUrl + "/fictionChapter/search/")
                .addHeader("Content-Type", "text/html;charset=utf-8")
                .addParam("fictionId", fictionId)
                .addParameter("/")
                .get()
                .sync();
        storyDirectoryDTO = JSONObject.toJavaObject(JSON.parseObject(data), StoryDirectoryDTO.class);
        if (storyDirectoryDTO.getCode().equals("0") && storyDirectoryDTO.getData().getChapterList().size() > 0) {
            return storyDirectoryDTO;
        }
        return null;
    }

    public StoryContentDTO getStoryDirContent(String chapterId) {
        String ipApiUrl = PropertiesUtil.readProperties(GlobalConstant.SEO_HTTP_URL, "toolBoxPlugin.seo.lryApi");
        StoryContentDTO storyContentDTO = null;
        String data = OkHttpUtil.builder().url(ipApiUrl + "/fictionContent/search/")
                .addHeader("Content-Type", "text/html;charset=utf-8")
                .addParam("chapterId", chapterId)
                .addParameter("/")
                .get()
                .sync();
        storyContentDTO = JSONObject.toJavaObject(JSON.parseObject(data), StoryContentDTO.class);
        List<String> handledStoryContent = new ArrayList<>();
        if (storyContentDTO.getCode().equals("0") && storyContentDTO.getData().size() > 0) {
            return storyContentDTO;
        }
        return null;
    }


}
