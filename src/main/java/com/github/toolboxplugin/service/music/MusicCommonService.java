package com.github.toolboxplugin.service.music;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.toolboxplugin.config.GlobalConstant;
import com.github.toolboxplugin.model.ReturnCode;
import com.github.toolboxplugin.model.music.PlayLisitsDto;
import com.github.toolboxplugin.utils.OkHttpUtil;
import com.github.toolboxplugin.utils.PropertiesUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 无需登录的公共接口
 **/
public class MusicCommonService {

    /***
     * 1.网友精选碟歌单
     *  order: 可选值为 'new' 和 'hot', 分别对应最新和最热 , 默认为 'hot'
     *  cat: tag, 比如 " 华语 "、" 古风 " 、" 欧美 "、" 流行 ", 默认为 "全部",可从歌单分类接口获取(/playlist/catlist)
     *  limit: 取出歌单数量 , 默认为 50
     *  offset: 偏移数量 , 用于分页 , 如 :( 评论页数 -1)*50, 其中 50 为 limit 的值
     */
    public PlayLisitsDto TopPlaylists(String order, String cat, Integer limit, Integer offset) {
        String domain = PropertiesUtil.readProperties(GlobalConstant.DOMAIN_CONFIG, "toolBoxPlugin.music.wangyiyun");
        String path = PropertiesUtil.readProperties(GlobalConstant.REQUEST_HTTP_URL, "request.music.login.top.playlist");
        Map<String, Object> params = new HashMap<>();
        if (order.equals("new") || order.equals("hot")) {
            // 'new' 和 'hot', 分别对应最新和最热 , 默认为 'hot'
            params.put("order", order);
        }
        if (cat != null) {
            //  tag, 比如 " 华语 "、" 古风 " 、" 欧美 "、" 流行 ", 默认为 "全部",可从歌单分类接口获取(/playlist/catlist)
            params.put("cat", cat);
        }
        if (limit == null || limit == 0) {
            params.put("limit", 10);
        } else {
            params.put("limit", limit);
        }
        if (offset != null && offset != 0) {
            //偏移数量 , 用于分页 , 如 :( 评论页数 -1)*50, 其中 50 为 limit 的值
            params.put("offset", offset);
        }
        String data = OkHttpUtil.builder().url(domain + path)
                .addMapParam(params)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addParameter("=")
                .get()
                .sync();
        PlayLisitsDto playLisitsDto = JSONObject.toJavaObject(JSON.parseObject(data), PlayLisitsDto.class);
        if (playLisitsDto.getCode().equals(ReturnCode.SUCCESS_MUSIC.getCode())) {
            return playLisitsDto;
        }
        return null;
    }
}
