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
package com.notnl.tp.config;

import com.notnl.tp.util.ObjectUtil;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Java翻译挂件 (TranslationPendant)<br/>
 * 配置类
 *
 * @author noti0na1
 */
public class Config {

    private static final Logger LOG = Logger.getLogger(Config.class.getName());

    private static class ConfigInstance {

        /**
         * 存放单例对象
         */
        private static final Config INSTANCE = new Config();
    }

    /**
     * 配置文件地址
     */
    private static final String PROPERTIES = Info.PROPERTIES;
    /**
     * 引擎
     */
    private String engine;
    /**
     * 显示字体
     */
    private String fontName;
    /**
     * 源语言
     */
    private String from;
    /**
     * 目标语言
     */
    private String to;
    /**
     * 查询文本的间隔时间
     */
    private int interval;
    /**
     * 字体类型
     */
    private int fontType;
    /**
     * 字体风格
     */
    private int fontStyle;
    /**
     * 文字大小
     */
    private float fontSize;
    /**
     * 文字层透明度
     */
    private int diaphaneity;
    /**
     * 是否显示阴影
     */
    private boolean shadow;
    /**
     * 是否显示原文
     */
    private boolean originalText;
    /**
     * 是否开启测试翻译功能
     */
    private boolean openBeteTran;

    private Config() {
        //读取配置对象
        Properties pro = new Properties();
        try {
            pro.load(new FileInputStream(PROPERTIES));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "读取配置文件失败！\n"
                    + ObjectUtil.getErrorMessage(ex),
                    "错误", JOptionPane.WARNING_MESSAGE
            );
            System.exit(1);
        }
        this.engine = pro.getProperty("engine", "baidu");
        this.fontName = pro.getProperty("font", "方正粗圆简体.ttf");
        this.from = pro.getProperty("from", "auto");
        this.to = pro.getProperty("to", "zh");
        this.interval = Integer.parseInt(pro.getProperty("interval", "1000"));
        this.fontType = Integer.parseInt(pro.getProperty("fontType", "0"));
        this.fontStyle = Integer.parseInt(pro.getProperty("fontStyle", "" + 0));
        this.fontSize = Float.parseFloat(pro.getProperty("fontSize", "24"));
        this.diaphaneity = Integer.parseInt(pro.getProperty("diaphaneity", "50"));
        this.shadow = Boolean.parseBoolean(pro.getProperty("shadow", "true"));
        this.originalText = Boolean.parseBoolean(pro.getProperty("originalText", "true"));
        this.openBeteTran = Boolean.parseBoolean(pro.getProperty("operBeteTran", "false"));
        //增加关机钩子
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                //软件关闭时储存各项设置
                saveAllConfig();
            }
        });
        LOG.log(Level.INFO, ObjectUtil.toString(this));
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getFontType() {
        return fontType;
    }

    public void setFontType(int fontType) {
        this.fontType = fontType;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public int getDiaphaneity() {
        return diaphaneity;
    }

    public void setDiaphaneity(int diaphaneity) {
        this.diaphaneity = diaphaneity;
    }

    public boolean isShadow() {
        return shadow;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public boolean isOriginalText() {
        return originalText;
    }

    public void setOriginalText(boolean originalText) {
        this.originalText = originalText;
    }

    public boolean isOpenBeteTran() {
        return openBeteTran;
    }

    public void setOpenBeteTran(boolean isOpenBeteTran) {
        this.openBeteTran = isOpenBeteTran;
    }

    /**
     * 存储所有配置
     */
    public void saveAllConfig() {
        LOG.log(Level.CONFIG, "Save All Config");
        Properties pro = new Properties();
        pro.setProperty("engine", engine);
        pro.setProperty("font", fontName);
        pro.setProperty("from", from);
        pro.setProperty("to", to);
        pro.setProperty("interval", "" + interval);
        pro.setProperty("fontType", "" + fontType);
        pro.setProperty("fontStyle", "" + fontStyle);
        pro.setProperty("fontSize", "" + fontSize);
        pro.setProperty("diaphaneity", "" + diaphaneity);
        pro.setProperty("shadow", "" + shadow);
        pro.setProperty("originalText", "" + originalText);
        pro.setProperty("openBeteTran", "" + openBeteTran);
        try {
            pro.store(new FileOutputStream(PROPERTIES), "CONFIG");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }

    /**
     * 获得实例
     *
     * @return
     */
    public static Config getInstance() {
        return ConfigInstance.INSTANCE;
    }
}
