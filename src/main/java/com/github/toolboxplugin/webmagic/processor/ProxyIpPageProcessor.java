package com.github.toolboxplugin.webmagic.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 用于解析爬取到的XXX html页面
 **/
public class ProxyIpPageProcessor implements PageProcessor {

    private static Logger logger = LogManager.getLogger(ProxyIpPageProcessor.class);

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    // webmagic官方还有很多案例，更多内容自行参考学习了，例如配置代理，自带url去重、网页去重等功能
    // 官方文档地址：http://webmagic.io/docs/zh/
    //https://blog.csdn.net/weixin_44431371/article/details/106813271

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        logger.info("小说分类，开始爬取数据");

    }

    @Override
    public Site getSite() {
        return site;
    }

}
