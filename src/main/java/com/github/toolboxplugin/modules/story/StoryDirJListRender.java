package com.github.toolboxplugin.modules.story;

import com.github.toolboxplugin.model.DTO.DirectoryChapter;

import javax.swing.*;
import java.awt.*;


public class StoryDirJListRender extends DefaultListCellRenderer {
    private int hoverIndex = -1;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        if (value instanceof DirectoryChapter) {
            DirectoryChapter l = (DirectoryChapter) value;
//            String f = "<span>" + l.getTitle().trim() + "</span>";
            setText(l.getTitle().trim());
        } else {
            setText(null);
        }
        if (isSelected) {
            setBackground(Color.CYAN);
        } else {
//            setBackground(index == hoverIndex ? Color.YELLOW : Color.WHITE);
            setBackground(Color.WHITE);
        }

        //实现鼠标划过改变颜色
      /*  list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                list.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                list.setCursor(Cursor.getDefaultCursor());
            }
        });*/
//        list.addMouseMotionListener(new MouseAdapter() {
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                int index = list.locationToIndex(e.getPoint());
//                setHoverIndex(list.getCellBounds(index, index).contains(e.getPoint())
//                        ? index : -1);
//            }
//
//            private void setHoverIndex(int index) {
//                if (hoverIndex == index) {
//                    return;
//                }
//                hoverIndex = index;
//                list.repaint();
//            }
//        });
        return this;
    }
}
