package com.github.toolboxplugin.swing;

import com.github.toolboxplugin.config.GlobalConstant;
import com.github.toolboxplugin.utils.OkHttpUtil;
import com.github.toolboxplugin.utils.PropertiesUtil;
import okhttp3.Call;

import javax.swing.*;

public class SearchBar {
    private JPanel searchPanl;
    private JTextPane searchResult;
    private JTextField searchInput;

    public static void main(String[] args) {
        JFrame frame = new JFrame("SearchBar");
        frame.setContentPane(new SearchBar().searchPanl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JButton searchButton;
    private JPanel searchJpanel;
    private JScrollPane resultScrollPane;

    public SearchBar() {

        searchButton.addActionListener(e -> {
            String inputText = searchInput.getText();
            String ipApiUrl = PropertiesUtil.readProperties(GlobalConstant.SEO_HTTP_URL, "toolBoxPlugin.seo.yuenov");
            OkHttpUtil.builder().url(ipApiUrl)
                    .addHeader("Content-Type", "text/html;charset=utf-8")
                    .addParam("wd", inputText)
                    .get()
                    .sync(new OkHttpUtil.ICallBack() {
                        @Override
                        public void onSuccessful(Call call, String html) {
                            searchResult.setEditable(true);
                            searchResult.setText(html);
                        }
                        @Override
                        public void onFailure(Call call, String errorMsg) {
                            searchResult.add(new JLabel(errorMsg));
                        }
                    });
        });
    }

    public JComponent getComponent() {
        searchPanl.setName("SEO");
        return searchPanl;
    }
}
