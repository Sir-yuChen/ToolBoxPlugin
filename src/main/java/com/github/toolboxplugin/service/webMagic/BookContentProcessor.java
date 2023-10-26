package com.github.toolboxplugin.service.webMagic;

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

import java.util.List;

/**
 * 用于解析爬取到的XXX html页面
 **/
public class BookContentProcessor implements PageProcessor {

    private static Logger logger = LogManager.getLogger(BookContentProcessor.class);
    private IReaderDebugDTO iReaderDebugDTO;
    private Site site;
    private Project project;

    public BookContentProcessor(IReaderDebugDTO dtoAndCheck, Project project) {
        this.iReaderDebugDTO = dtoAndCheck;
        this.project = project;
    }

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        logger.info("BookContentProcessor 开始解析页面 start");
        List<String> content = page.getHtml().xpath(iReaderDebugDTO.getContentRuleInfo()).all();
        if (CollectionUtils.isEmpty(content)) {
            NotificationUtils.setNotification("ToolboxPlugin IReader", "注意：未匹配到任何数据",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 0);
            return;
        }
        page.putField("bookContent", content);
        logger.info("BookContentProcessor 结束解析页面 end");
    }

    @Override
    public Site getSite() {
        site = Site.me()
                //.setCharset("utf-8") //设置编码
                .setRetryTimes(3)
                .setSleepTime(1000)
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
        return site;
    }
}
