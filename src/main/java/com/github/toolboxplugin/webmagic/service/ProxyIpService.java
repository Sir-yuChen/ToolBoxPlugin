package com.github.toolboxplugin.webmagic.service;

import com.github.toolboxplugin.webmagic.processor.ProxyIpPageProcessor;
import com.github.toolboxplugin.webmagic.processor.ProxyIpPipeline;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

@Service("proxyIpService")
public class ProxyIpService {

    private static Logger logger = LogManager.getLogger(ProxyIpService.class);

    public void getProxyJob() {
        Spider.create(new ProxyIpPageProcessor())
                //添加初始的URL
                .addUrl("http://www.66ip.cn/")
                //添加一个Pipeline，一个Spider可以有多个Pipeline
                .addPipeline(new ProxyIpPipeline())
                // 设置布隆过滤器去重操作（默认使用HashSet来进行去重，占用内存较大；使用BloomFilter来进行去重，占用内存较小，但是可能漏抓页面）
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000000)))
                //开启5个线程抓取
                .thread(10)
                .run();
    }

}
