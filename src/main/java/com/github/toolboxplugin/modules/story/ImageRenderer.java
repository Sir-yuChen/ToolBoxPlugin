package com.github.toolboxplugin.modules.story;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Administrator
 * @date 2023/9/14 14:24
 * @desc 列表展示图片实现类
 */
public class ImageRenderer implements TableCellRenderer {

    @SuppressWarnings("unchecked")
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int rowIndex, int columnIndex) {

        if (value instanceof Image) {
            JLabel jLabel = new JLabel();
            //设置布局
            jLabel.setLayout(new BorderLayout());
            //给jlable设置图片
            jLabel.setIcon(new ImageIcon((Image) value));
            return jLabel;
        } else if (value instanceof File) {
            try {
                return new JLabel(new ImageIcon(ImageIO.read((File) value)));
            } catch (IOException ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        } else {
            String val = String.valueOf(value);
            try {
                return new JLabel(new ImageIcon(ImageIO.read(new File(val))));
            } catch (IOException ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
    }
}