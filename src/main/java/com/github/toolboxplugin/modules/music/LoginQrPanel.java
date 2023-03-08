package com.github.toolboxplugin.modules.music;


import javax.swing.*;
import java.awt.*;

public class LoginQrPanel extends JTextPane {
    private static final long serialVersionUID = -6352788025440244338L;
    private ImageIcon ic = null;

    public LoginQrPanel(String imagePath) {
        setText("点击刷新");
        setOpaque(false);
        this.ic = new ImageIcon(imagePath);
    }

    // 固定背景图片，允许这个JPanel可以在图片上添加其他组件
    @Override
    public void paint(Graphics g) {
        int x = 0, y = 0;
        g.drawImage(ic.getImage(), x, y, ic.getIconWidth(), ic.getIconHeight(), ic.getImageObserver());
        super.paint(g);
//        this.setSize(ic.getIconWidth(), ic.getIconHeight());
       /* //这段代码是为了保证在窗口大于图片时，图片仍能覆盖整个窗口
        while (true) {
            g.drawImage(ic.getImage(), x, y, this);
            if (x > getSize().width && y > getSize().height) {
                break;
            }
            //这段代码是为了保证在窗口大于图片时，图片仍能覆盖整个窗口
            if (x > getSize().width) {
                x = 0;
                y += ic.getIconHeight();
            } else {
                x += ic.getIconWidth();
            }
        }*/
    }
}
