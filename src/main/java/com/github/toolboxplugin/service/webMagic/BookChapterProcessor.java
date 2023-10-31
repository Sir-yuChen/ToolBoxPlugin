package com.github.toolboxplugin.service.webMagic;

import com.github.toolboxplugin.model.DTO.DirectoryChapter;
import com.github.toolboxplugin.model.DTO.IReaderDebugDTO;
import com.github.toolboxplugin.utils.NotificationUtils;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于解析爬取到的XXX html页面
 **/
public class BookChapterProcessor implements PageProcessor {

    private static Logger logger = LogManager.getLogger(BookChapterProcessor.class);

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    // webmagic官方还有很多案例，更多内容自行参考学习了，例如配置代理，自带url去重、网页去重等功能
    // 官方文档地址：http://webmagic.io/docs/zh/
    //https://blog.csdn.net/weixin_44431371/article/details/106813271
    private IReaderDebugDTO iReaderDebugDTO;
    private Project project;

    public BookChapterProcessor(IReaderDebugDTO dtoAndCheck, Project project) {
        this.iReaderDebugDTO = dtoAndCheck;
        this.project = project;
    }

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        logger.info("BookChapterProcessor 开始解析页面 start");
        Html html = page.getHtml();
        List<String> chapterTitleList = html.xpath(iReaderDebugDTO.getChapterTitleRuleInfo()).all();
        List<String> chapterUrlList = html.xpath(iReaderDebugDTO.getChapterUrlRuleInfo()).all();
        if (CollectionUtils.isEmpty(chapterTitleList) || CollectionUtils.isEmpty(chapterUrlList)) {
            NotificationUtils.setNotification( "注意：未匹配到任何数据",
                    NotificationDisplayType.BALLOON, NotificationType.ERROR, project, 5000);
            return;
        }
        List<DirectoryChapter> directoryChapters = new ArrayList<>();
        for (int i = 0; i < chapterTitleList.size(); i++) {
            DirectoryChapter chapter = new DirectoryChapter();
            chapter.setTitle(chapterTitleList.get(i));
            if (i > chapterUrlList.stream().count()) {
                continue;
            }
            chapter.setChapterUrl(chapterUrlList.get(i));
            directoryChapters.add(chapter);
        }
        iReaderDebugDTO.setChapters(directoryChapters);
        page.putField("iReaderDebugDTO", iReaderDebugDTO);
        logger.info("BookChapterProcessor 结束解析页面 end");
    }

    @Override
    public Site getSite() {
        return site;
    }
}
