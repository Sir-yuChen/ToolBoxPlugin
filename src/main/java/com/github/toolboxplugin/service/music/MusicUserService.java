package com.github.toolboxplugin.service.music;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.toolboxplugin.config.GlobalConstant;
import com.github.toolboxplugin.model.ReturnCode;
import com.github.toolboxplugin.model.music.*;
import com.github.toolboxplugin.utils.OkHttpUtil;
import com.github.toolboxplugin.utils.PropertiesUtil;

/**
 * 用户相关service
 * 文档：https://binaryify.github.io/NeteaseCloudMusicApi/#
 **/
public class MusicUserService {

    //1.二维码登录--二维码 key 生成接口-
    public String loginQrGetKey() {
        String domain = PropertiesUtil.readProperties(GlobalConstant.DOMAIN_CONFIG, "toolBoxPlugin.music.wangyiyun");
        String path = PropertiesUtil.readProperties(GlobalConstant.REQUEST_HTTP_URL, "request.music.login.qr.key");
        String data = OkHttpUtil.builder().url(domain + path)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .get()
                .sync();
        ReturnCommon returnCommon = JSONObject.toJavaObject(JSON.parseObject(data), ReturnCommon.class);
        Unikey unikey = JSONObject.toJavaObject(JSON.parseObject(returnCommon.getData().toString()), Unikey.class);
        if (unikey.getCode().equals(ReturnCode.SUCCESS_MUSIC.getCode())) {
            return unikey.getUnikey();
        }
        return null;
    }

    //1.二维码登录--二维码 生成
    public String loginQrCreate(String key) {
        String domain = PropertiesUtil.readProperties(GlobalConstant.DOMAIN_CONFIG, "toolBoxPlugin.music.wangyiyun");
        String path = PropertiesUtil.readProperties(GlobalConstant.REQUEST_HTTP_URL, "request.music.login.qr.create");
        String data = OkHttpUtil.builder().url(domain + path)
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .addParam("key", key)
                .addParam("qrimg", "1") //返回base64位图片
                .addParameter("=")
                .get()
                .sync();
        ReturnCommon returnCommon = JSONObject.toJavaObject(JSON.parseObject(data), ReturnCommon.class);
        CreateQr createQr = JSONObject.toJavaObject(JSON.parseObject(returnCommon.getData().toString()), CreateQr.class);
        if (returnCommon.getCode().equals(ReturnCode.SUCCESS_MUSIC.getCode()) && createQr.getQrimg() != null) {
            return createQr.getQrimg();
        }
        return null;
    }

    //1.二维码登录--二维码状态检查
    public CheckQrStatus checkQrStatus(String key) {
        String domain = PropertiesUtil.readProperties(GlobalConstant.DOMAIN_CONFIG, "toolBoxPlugin.music.wangyiyun");
        String path = PropertiesUtil.readProperties(GlobalConstant.REQUEST_HTTP_URL, "request.music.login.qr.check.status");
        String data = OkHttpUtil.builder().url(domain + path)
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .addParam("key", key)
                .addParameter("=")
                .get()
                .sync();
        CheckQrStatus checkQrStatus = JSONObject.toJavaObject(JSON.parseObject(data), CheckQrStatus.class);
        if (checkQrStatus.getCode() != null) {
            return checkQrStatus;
        }
        return null;
    }


    //2. 检查用户token
    public Boolean checkUserCookies(String cookie) {
        if (cookie == null) {
            return false;
        }
        String domain = PropertiesUtil.readProperties(GlobalConstant.DOMAIN_CONFIG, "toolBoxPlugin.music.wangyiyun");
        String path = PropertiesUtil.readProperties(GlobalConstant.REQUEST_HTTP_URL, "request.music.login.check.status");
        String data = OkHttpUtil.builder().url(domain + path)
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .addHeader("Cookie", cookie)
                .addParameter("/")
                .get()
                .sync();
        ReturnCommon returnCommon = JSONObject.toJavaObject(JSON.parseObject(data), ReturnCommon.class);
        LoginStatus loginStatus = JSONObject.toJavaObject(JSON.parseObject(returnCommon.getData().toString()), LoginStatus.class);
        if (loginStatus.getCode().equals(ReturnCode.SUCCESS_MUSIC.getCode())) {
            return true;
        }
        return false;
    }
}
