package com.github.toolboxplugin.swing;

import com.github.toolboxplugin.config.GlobalConstant;
import com.github.toolboxplugin.model.music.CheckQrStatus;
import com.github.toolboxplugin.model.music.PlayLisitsDto;
import com.github.toolboxplugin.model.music.SongListDto;
import com.github.toolboxplugin.service.music.MusicCommonService;
import com.github.toolboxplugin.service.music.MusicUserService;
import com.github.toolboxplugin.swing.handle.music.MusicPromptLabel;
import com.github.toolboxplugin.utils.Base64Utils;
import com.github.toolboxplugin.utils.OkHttpUtil;
import com.github.toolboxplugin.utils.PropertiesUtil;
import com.intellij.ui.components.JBScrollPane;
import okhttp3.Call;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Label类提供两个设置其对齐方式的方法：
 * #setHorizontalAlignment：设置水平对齐方式；
 * 它的有效参数是：
 * ¨ SwingConstants.LEFT：左对齐；（默认值，也就是不设置时则左对齐）
 * ¨ SwingConstants.CENTER：居中对齐；
 * ¨ SwingConstants.RIGHT：右对齐；
 * #setVerticalAlignment：设置垂直对齐方式；
 * 它的有效参数是：
 * ¨ SwingConstants.TOP：向上对齐；
 * ¨ SwingConstants.CENTER：居中对齐；（默认值，也就是不设置时居中对齐）
 * ¨ SwingConstants.BOTTOM：向下对齐；
 * <p>
 * <p>
 * MUSIC main
 **/
public class MusicMain implements BaseUIAction {

    private static Logger logger = LogManager.getLogger(MusicMain.class);

    private JPanel musicMainPanel;
    private JPanel homeTopPanel;
    private JLabel homePromptLabel;
    private JScrollPane homeBelowSPane;
    private JPanel userAvatarPanel;
    private JPanel userInfoPanel;
    private JLabel userAvatarLabel;
    private JPanel homebelowPanel;
    private JTabbedPane myTabbedPane;
    private JPanel userPlayLists;
    private JPanel playsRecord;
    private Integer currentPage = 1;
    private Integer pageSize = 30;
    private Integer totalPage;

    private Boolean LoginFlage = false; //登录标识
    private String cookieSelf; //登录态

    @Override
    public JComponent getComponent() {
        return musicMainPanel;
    }

    @Override
    public String getToolWindowId() {
        return GlobalConstant.TOOL_WINDOW_ID_MUSIC;
    }

    /***
     * 前置操作
     */
    @Override
    public void setBefore() {
        homebelowPanel = new JPanel();
        userAvatarPanel.setBorder(BorderFactory.createLineBorder(Color.white, 1));
        MusicUserService musicUserService = new MusicUserService();
        //1. 校验用户是否已经登录，刷新登录状态
        LoginFlage = musicUserService.checkUserCookies(cookieSelf);
        MusicPromptLabel musicPromptLabel = new MusicPromptLabel();
        if (LoginFlage) {
            homePromptLabel = musicPromptLabel.homeTopPromptLabelHandle(homePromptLabel, "init_login_init");
        } else {
            userInfoPanel.setVisible(false);
            myTabbedPane.setVisible(false);
            homePromptLabel = musicPromptLabel.homeTopPromptLabelHandle(homePromptLabel, "init_login_N");
            userLogin();
        }
    }

    @Override
    public void setAfter() {

    }

    public MusicMain() {
        setBefore();
        //初始化用户为登录则，加载发现推荐模块
        fillHomeBelowSPane();
    }

    /***
     * 刷新页面
     */
    public void refresh() {
        setBefore();
        fillHomeBelowSPane();
        MusicPromptLabel musicPromptLabel = new MusicPromptLabel();
        homePromptLabel = musicPromptLabel.homeTopPromptLabelHandle(homePromptLabel, "refresh_pages");
    }

    /***
     * 填充首页非登录模块展示，推荐，发现，我的
     */
    public void fillHomeBelowSPane() {
        //jpanel布局
        homebelowPanel.setLayout(new GridLayout(5, 1, 3, 1));
        //禁止水平滑动
        homeBelowSPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //1. 未登录，展示网友推荐歌单，单曲推荐，排行榜，热门评论
        JPanel hotPlaylistJPanel = new JPanel();
        //组合边框
        CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white, 1), BorderFactory.createTitledBorder("好听的歌单"));
        hotPlaylistJPanel.setBorder(border);
        MusicCommonService musicCommonService = new MusicCommonService();
        PlayLisitsDto playLisitsDto = musicCommonService.TopPlaylists("hot", null, pageSize, currentPage);
        if (totalPage == null) {
            totalPage = playLisitsDto.getTotal() % pageSize == 0 ? playLisitsDto.getTotal() / pageSize : playLisitsDto.getTotal() / pageSize + 1;
        }
        if (currentPage < totalPage) {
            currentPage += 1;
        }
        //TODO-zy no dispay
        List<SongListDto> playlists = playLisitsDto.getPlaylists();
//        hotPlaylistJPanel.setLayout(new GridLayout(1, playlists.size(), 2, 0));
        //设置panel的首选大小，同时保证宽高大于JScrollPane的宽高，这样下面的JScrollPane才会出现滚动条
        hotPlaylistJPanel.setPreferredSize(new Dimension(playlists.size() * 150, 150));
        for (SongListDto item : playlists) {
            syncGetImgIcon(item.getCoverImgUrl(), hotPlaylistJPanel);
        }
        JScrollPane hotPlaylistSJPanel = new JBScrollPane(hotPlaylistJPanel);
        hotPlaylistSJPanel.setBounds(10, 10, hotPlaylistJPanel.getWidth() + 10, hotPlaylistJPanel.getWidth() + 10);

        JPanel singlesJPanel = new JPanel(new GridLayout(1, playlists.size(), 5, 0));
        CompoundBorder bor = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white, 1), BorderFactory.createTitledBorder("好听的单曲"));
        singlesJPanel.setBorder(bor);
        homebelowPanel.add(hotPlaylistJPanel);
        homebelowPanel.add(new JBScrollPane(singlesJPanel));
        homeBelowSPane.setViewportView(homebelowPanel);
    }


    /***
     * 用户登录
     */
    public void userLogin() {
        MusicPromptLabel musicPromptLabel = new MusicPromptLabel();
        if (LoginFlage) {
            homePromptLabel = musicPromptLabel.homeTopPromptLabelHandle(homePromptLabel, "init_login_repeat");
            return;
        }
        //未登录进行登录操作
        MusicUserService musicUserService = new MusicUserService();
        String key = musicUserService.loginQrGetKey();
        if (key == null) {
            homePromptLabel = musicPromptLabel.homeTopPromptLabelHandle(homePromptLabel, "init_login_fail");
            return;
        }
        String qrImg = musicUserService.loginQrCreate(key);
        if (key == null) {
            homePromptLabel = musicPromptLabel.homeTopPromptLabelHandle(homePromptLabel, "init_login_fail");
            return;
        } else {
            String imgpath = "/src/main/resources/images/icons/";
            String propertiesPath = PropertiesUtil.getPropertiesPath();
            File login_qr = Base64Utils.toImage(qrImg, new File(propertiesPath + imgpath), "login_qr");
            if (login_qr != null) {
                setQRpanel(userAvatarLabel, login_qr.getPath());
                getQrStatusTimer(musicUserService, key, musicPromptLabel);
            }
        }
    }

    /***
     * 设置登录二维码图片
     */
    private void setQRpanel(JLabel userAvatarTextpanel, String imgPath) {
        ImageIcon imageIcon = new ImageIcon(imgPath);
        userAvatarTextpanel.setIcon(imageIcon);
        userAvatarTextpanel.setPreferredSize(new Dimension(150, 150));
    }

    /***
     * 轮询获取扫码状态，直到登录成功
     */
    public void getQrStatusTimer(MusicUserService musicUserService, String key, MusicPromptLabel musicPromptLabel) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
        // 参数：1、任务体 2、首次执行的延时时间
        //      3、任务执行间隔 4、间隔时间单位
        // 上一个任务执行完后，间隔5秒执行下一个   https://www.cnblogs.com/xiaoxi666/p/10783879.html
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CheckQrStatus checkQrStatus = musicUserService.checkQrStatus(key);
                if (checkQrStatus.getCode() == null) {
                    return;
                }
                switch (checkQrStatus.getCode()) {
                    case "800":
                        homePromptLabel = musicPromptLabel.homeTopPromptLabelHandle(homePromptLabel, "login_rq_time_over");
                        //停止轮询。带用户刷新二维码后
                        scheduler.shutdown();
                        break;
                    case "801":
                        homePromptLabel = musicPromptLabel.homeTopPromptLabelHandle(homePromptLabel, "login_rq_wait");
                        break;
                    case "802":
                        homePromptLabel = musicPromptLabel.homeTopPromptLabelHandle(homePromptLabel, "login_rq_time_confirmed");
                        break;
                    case "803":
                        homePromptLabel = musicPromptLabel.homeTopPromptLabelHandle(homePromptLabel, "login_rq_successed");
                        cookieSelf = checkQrStatus.getCookie();
                        LoginFlage = true;
                        //登录信息
                        userInfoPanel.setVisible(true);
                        myTabbedPane.setVisible(false);
                        //停止轮询任务
                        scheduler.shutdown();
                        break;
                    default:
                        homePromptLabel = musicPromptLabel.homeTopPromptLabelHandle(homePromptLabel, "login_rq_time_over");
                        scheduler.shutdown();
                }
                homePromptLabel.updateUI();
            }
        });
        scheduler.scheduleWithFixedDelay(thread, 0L, 5L, TimeUnit.SECONDS);
    }

    /***
     * 异步加载图片
     */
    private void syncGetImgIcon(String url, JPanel imgPanel) {
        OkHttpUtil.builder().url(url)
                .addHeader("Content-Type", "image/jpg")
                .addParameter("=")
                .get()
                .async(new OkHttpUtil.ICallBack() {
                    @Override
                    public void onSuccessful(Call call, ResponseBody responseBody) {
                        try {
                            BufferedImage bufferedImage = ImageIO.read(responseBody.byteStream());
                            Image itemImage = bufferedImage.getScaledInstance(120, 120, BufferedImage.SCALE_SMOOTH);
                            ImageIcon imageIcon = new ImageIcon(itemImage);
                            JLabel playJLabel = new JLabel();
                            playJLabel.setLayout(new GridLayout(2, 1, 0, 1));
                            playJLabel.setIcon(imageIcon);
//                            playJLabel.add(new JLabel("你好"));
                            //向上对对齐
                            playJLabel.setVerticalAlignment(SwingConstants.TOP);
                            imgPanel.add(playJLabel);
                        } catch (Exception e) {
                            logger.error("加载图片异常error={}", e);
                        }
                    }

                    @Override
                    public void onFailure(Call call, String errorMsg) {
                        logger.error("加载图片异常errorMsg={}", errorMsg);
                    }
                });
    }

}
