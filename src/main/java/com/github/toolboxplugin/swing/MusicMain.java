package com.github.toolboxplugin.swing;

import com.github.toolboxplugin.config.GlobalConstant;
import com.github.toolboxplugin.model.music.CheckQrStatus;
import com.github.toolboxplugin.model.music.NewSongListDto;
import com.github.toolboxplugin.model.music.PlayLisitsDto;
import com.github.toolboxplugin.model.music.SongListDto;
import com.github.toolboxplugin.modules.music.MusicJpanel;
import com.github.toolboxplugin.service.music.MusicCommonService;
import com.github.toolboxplugin.service.music.MusicUserService;
import com.github.toolboxplugin.swing.handle.music.MusicPromptLabel;
import com.github.toolboxplugin.utils.Base64Utils;
import com.github.toolboxplugin.utils.OkHttpUtil;
import com.github.toolboxplugin.utils.PropertiesUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
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
    private JPanel userAvatarPanel;
    private JPanel userInfoPanel;
    private JLabel userAvatarLabel;
    private JScrollPane homeBelowSPane;
    private Integer currentPage = 1;
    private Integer pageSize = 6;
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
        userAvatarPanel.setBorder(BorderFactory.createLineBorder(Color.white, 1));
        MusicUserService musicUserService = new MusicUserService();
        //1. 校验用户是否已经登录，刷新登录状态
        LoginFlage = musicUserService.checkUserCookies(cookieSelf);
        MusicPromptLabel musicPromptLabel = new MusicPromptLabel();
        if (LoginFlage) {
            homePromptLabel = musicPromptLabel.homeTopPromptLabelHandle(homePromptLabel, "init_login_init");
        } else {
            userInfoPanel.setVisible(false);
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
        JPanel homebelowPanel = new JPanel();
        //jpanel布局  未登录，展示网友推荐歌单，单曲推荐，排行榜，热门评论
        homebelowPanel.setLayout(new GridLayout(5, 1, 3, 1));
        //1.歌单推荐模块
        fillHotPlaylistJPanel(homebelowPanel, currentPage == 1 ? currentPage : currentPage + 1);
        //2.新单曲推荐
        fillNewSonglistJPanel(homebelowPanel);
        homeBelowSPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        homeBelowSPane.setViewportView(homebelowPanel);
    }

    /***
     * 热门单曲
     */
    private void fillNewSonglistJPanel(JPanel homebelowPanel) {
        JPanel hotSongJp = new JPanel();
        CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white, 1), BorderFactory.createTitledBorder("好听的新单曲"));
        hotSongJp.setBorder(border);
        MusicCommonService musicCommonService = new MusicCommonService();
        NewSongListDto newSong = musicCommonService.getNewSong("50");
        //TODO-zy 展示推荐的新单曲
        homebelowPanel.add(hotSongJp);
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
     * 填充 推荐歌单模块
     *
     */
    public void fillHotPlaylistJPanel(JPanel homebelowPanel, Integer page) {
        currentPage = page;
        MusicJpanel musicJpanel = new MusicJpanel(currentPage, homebelowPanel, this);
        MusicPromptLabel musicPromptLabel = new MusicPromptLabel();
        //组合边框
        CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white, 1), BorderFactory.createTitledBorder("好听的歌单"));
        musicJpanel.setBorder(border);
        if (totalPage != null && page > totalPage) {
            homePromptLabel = musicPromptLabel.homeTopPromptLabelHandle(homePromptLabel, "page_add");
            return;
        }
        if (page < 1) {
            homePromptLabel = musicPromptLabel.homeTopPromptLabelHandle(homePromptLabel, "page_deduct");
            return;
        }
        MusicCommonService musicCommonService = new MusicCommonService();
        PlayLisitsDto playLisitsDto = musicCommonService.TopPlaylists("hot", null, pageSize, currentPage);
        totalPage = playLisitsDto.getTotal() % pageSize == 0 ? playLisitsDto.getTotal() / pageSize : playLisitsDto.getTotal() / pageSize + 1;

        List<SongListDto> playlists = playLisitsDto.getPlaylists();
        for (SongListDto item : playlists) {
            asyncGetImgIcon(item, musicJpanel);
        }
        homebelowPanel.add(musicJpanel);
    }


    /***
     * 异步加载图片
     */
    private void asyncGetImgIcon(SongListDto dto, JPanel imgPanel) {
        OkHttpUtil.builder().url(dto.getCoverImgUrl())
                .addHeader("Content-Type", "image/jpg")
                .addParameter("=")
                .get()
                .async(new OkHttpUtil.ICallBack() {
                    @Override
                    public void onSuccessful(Call call, ResponseBody responseBody) {
                        try {
                            BufferedImage bufferedImage = ImageIO.read(responseBody.byteStream());
                            Image itemImage = bufferedImage.getScaledInstance(130, 140, BufferedImage.SCALE_SMOOTH);
                            ImageIcon imageIcon = new ImageIcon(itemImage);
//                            JPanel infoJp = new JPanel(new GridLayout(2, 1, 2, 2));
//                            infoJp.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
                            //图片
                            JLabel playJLabel = new JLabel();
                            playJLabel.setLayout(new GridLayout());
                            playJLabel.setIcon(imageIcon);
//                            playJLabel.setVerticalAlignment(SwingConstants.CENTER);
                            playJLabel.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
                            //歌单名
//                            JLabel textJL = new JLabel();
//                            textJL.setText(dto.getName());
                            playJLabel.setText(dto.getName());

                   /*         infoJp.add(textJL);
                            infoJp.add(playJLabel);*/

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


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        musicMainPanel = new JPanel();
        musicMainPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        homeTopPanel = new JPanel();
        homeTopPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        musicMainPanel.add(homeTopPanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        userAvatarPanel = new JPanel();
        userAvatarPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        homeTopPanel.add(userAvatarPanel, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        userAvatarLabel = new JLabel();
        userAvatarLabel.setText("");
        userAvatarPanel.add(userAvatarLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        homeTopPanel.add(userInfoPanel, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        homePromptLabel = new JLabel();
        homePromptLabel.setText("提示栏");
        musicMainPanel.add(homePromptLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        homeBelowSPane = new JScrollPane();
        musicMainPanel.add(homeBelowSPane, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return musicMainPanel;
    }
}
