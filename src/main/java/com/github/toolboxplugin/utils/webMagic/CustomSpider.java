package com.github.toolboxplugin.utils.webMagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;

/**
 * @author zhangyu
 * @description  自定义 Spider
 **/
public class CustomSpider extends AbstractDownloader {


    @Override
    public Page download(Request request, Task task) {
        return null;
    }

    @Override
    public void setThread(int threadNum) {

    }


}