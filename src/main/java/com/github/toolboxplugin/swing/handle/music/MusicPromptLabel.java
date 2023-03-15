package com.github.toolboxplugin.swing.handle.music;


import javax.swing.*;

public class MusicPromptLabel {

    //home页 提示文本处理
    public JLabel homeTopPromptLabelHandle(JLabel label, String type) {
        switch (type) {
            case "init_login_N":
                label.setText("当前用户未登录,请扫码登录");
                break;
            case "init_login_init":
                label.setText("已登录，加载数据中");
                break;
            case "init_login_repeat":
                label.setText("请不要重复登录");
                break;
            case "init_login_fail":
                label.setText("登录失败，请刷新后重试");
                break;
            case "login_rq_time_over":
                label.setText("二维码已失效,请刷新重试");
                break;
            case "login_rq_wait":
                label.setText("请扫码登录");
                break;
            case "login_rq_time_confirmed":
                label.setText("扫码成功，加载中。。。");
                break;
            case "login_rq_successed":
                label.setText("登录成功，加载中。。。");
                break;
            case "refresh_pages":
                label.setText("刷新成功");
                break;
            case "page_add":
                label.setText("当前已是最后一页");
                break;
            case "page_deduct":
                label.setText("当前已是第一页");
                break;
            default:
                label.setText("畅想音乐");
                break;
        }
        return label;
    }

}
