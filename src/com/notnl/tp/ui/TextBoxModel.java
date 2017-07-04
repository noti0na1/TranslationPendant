/*
 * Copyright (C) 2017 noti0na1 <i@notnl.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.notnl.tp.ui;

import com.notnl.tp.config.Config;
import com.notnl.tp.config.Info;
import com.notnl.tp.util.ObjectUtil;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 * Java翻译挂件 (TranslationPendant)<br/>
 * 文本窗口模型类
 *
 * @author noti0na1
 */
public abstract class TextBoxModel extends JFrame {

    /**
     * 文字层
     */
    TextLabel textLabel;
    /**
     * 配置窗口
     */
    protected ConfigFrame configFrame;
    /**
     * 配置对象
     */
    protected final Config config = Config.getInstance();

    public TextBoxModel() {
        //设置默认关闭属性（退出）
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //将窗体设置成无标题栏的语句
        super.setUndecorated(true);
        //不允许改变窗口大小
        super.setResizable(false);
        //使窗口总在最上
        super.setAlwaysOnTop(true);
        //初始化组件
        this.initComponents();
    }

    public TextBoxModel(String title) {
        this();
        super.setTitle(title);
    }

    /**
     * 初始化组件
     */
    private void initComponents() {
        this.textLabel = new TextLabel();
        this.textLabel.setForeground(Color.WHITE);
        //初始化配置窗口
        configFrame = new ConfigFrame(this);
    }

    /**
     * 设置显示文本 "/n"表示换行
     *
     * @param text
     */
    public void setDisplayText(String[] text) {
        //将文本设置到文本层
        this.textLabel.setText(text);
        super.repaint();
    }

    /**
     * TODO 显示错误信息
     *
     * @param ex
     */
    public void showError(Exception ex) {
        //TODO 另开错误窗口
        JOptionPane.showMessageDialog(null,
                "<html><body>"
                + "未知错误！<br/><br/>"
                + ObjectUtil.getErrorMessage(ex)
                + "</body></html>",
                "错误", JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * 显示配置窗口
     */
    protected void showShowConfigFrame() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                configFrame.updateAllSetting();
                configFrame.setVisible(true);
            }
        });
    }

    /**
     * 显示关于窗口
     */
    protected void showAbout() {
        JOptionPane.showMessageDialog(TextBoxModel.this,
                "<html><body>"
                + "Java在线翻译挂件 (TranslationPendant)" + "<br/>"
                + "版本：" + Info.VERSION + "<br/>"
                + "作者：noti0na1" + "<br/>"
                + "<br/>"
                + "此工具用于将系统剪贴版中内容译成中文并显示，" + "<br/>"
                + "需联网并配合ITH(推荐)或AGTH使用。" + "<br/>"
                + "</body></html>",
                "关于", JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * 退出
     */
    protected void exit() {
        //TODO
        //存储所有设置
        configFrame.applyAllSetting();
        configFrame.setVisible(false);
        System.exit(0);
    }

    /**
     * 重置各项设置
     */
    public void updataSetting() {
        //设置背景
        super.setBackground(
                new Color(0, 0, 0, (float) config.getDiaphaneity() / 100)
        );
        this.textLabel.setShowShadow(this.config.isShadow());
        this.textLabel.setOnlyShowEvenLines(!this.config.isOriginalText());
        Font font = MyFonts.getFont(
                this.config.getFontName(), this.config.getFontType(),
                this.config.getFontStyle(), this.config.getFontSize()
        );
        this.textLabel.setFont(font);
        this.configFrame.updateAllSetting();
    }
}
