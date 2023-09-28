package com.github.toolboxplugin.service.webMagic;

import com.github.toolboxplugin.model.DTO.IReaderDebugDTO;
import com.github.toolboxplugin.utils.NotificationUtils;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private IReaderDebugDTO iReaderDebugDTO;

    public BookContentProcessor(IReaderDebugDTO dtoAndCheck) {
        this.iReaderDebugDTO = dtoAndCheck;
    }

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        logger.info("BookContentProcessor 开始解析页面 start");
        List<String> content = page.getHtml().xpath(iReaderDebugDTO.getiReaderDebugRuleDTO().getContentRuleInfo()).all();
        if (CollectionUtils.isEmpty(content)) {
            NotificationUtils.setNotification("ToolboxPlugin IReader", "注意：未匹配到任何数据",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, iReaderDebugDTO.getProject(), 0);
            return;
        }
        page.putField("bookContent", content);
        logger.info("BookContentProcessor 结束解析页面 end");
    }

    @Override
    public Site getSite() {
        return site;
    }
}
