package com.github.toolboxplugin.webmagic.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.stream.Stream;

/**
 * 用于将XXX html页面解析出的数据存储到mysql数据库
 **/
@Component("proxyIpPipeline")
public class ProxyIpPipeline implements Pipeline {

    private static Logger logger = LogManager.getLogger(ProxyIpPipeline.class);

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<String> rultes = (List<String>) resultItems.get("rultes");
        logger.info("ProxyIpPipeline 共获取数据{}条", rultes.size());
        Stream<String> stringStream = rultes.parallelStream(); //第三方接口调用过快无法获取到IP正确的信息  部分可用IP无法入库
        stringStream.forEach(str -> {
            logger.info("当前线程名: " + Thread.currentThread().getName());
            //jsoup 解析判断Html会有问题  https://www.cnblogs.com/zhangyinhua/p/8037599.html
            String html = "<html>  <body> <table> <tbody>" + str + "</tbody> </table> </body> </html>";
            Document document = Jsoup.parseBodyFragment(html);
            Element body = document.body();
        });
    }
}
