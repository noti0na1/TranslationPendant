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
package com.notnl.tp;

import com.notnl.tp.util.Observable;
import com.notnl.tp.util.Observer;
import com.notnl.tp.util.StringUtil;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Java翻译挂件 (TranslationPendant)<br/>
 * 文本提供者类
 *
 * @author noti0na1
 */
public class TextProvider extends Observable<String> {

    private static final Logger LOG = Logger.getLogger(TextProvider.class.getName());

    private static class TextProviderInstance {

        /**
         * 存放单例对象
         */
        private static final TextProvider instance = new TextProvider();
    }

    /**
     * 剪贴板实例
     */
    private Clipboard clipboard;

    /**
     * 获取文本时间间隔,默认800
     */
    private int interval = 800;
    /**
     * 文本读取线程
     */
    private Thread reader;
    /**
     * 读取标识
     */
    private boolean runningFlag = false;
    /**
     * 文本
     */
    private String text = "";

    private TextProvider() {
        //初始化剪贴板
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    /**
     * 启动文本读取线程
     */
    public void start() {
        if (reader == null) {
            LOG.log(Level.INFO, "TextProvider started!");
            this.runningFlag = true;
            reader = new Thread(new TextReader(), "TextProvider");
            reader.start();
        }
    }

    /**
     * 停止文本获取
     */
    public void stop() {
        LOG.log(Level.INFO, "TextProvider stopped!");
        this.runningFlag = false;
        reader.interrupt();
        reader = null;
    }

    public boolean isRunning() {
        return runningFlag;
    }

    /**
     * 处理文本
     */
    @SuppressWarnings("SleepWhileInLoop")
    private void handleText() {
        while (runningFlag) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();
            }
            //获取剪贴板中文本
            String data = getTextFromClipboard();
            //如何获取文本为空或重复，则不作处理跳过
            if (StringUtil.isEmpty(data) || text.equals(data)) {
                continue;
            }
            //更换最新文本
            text = data;
            LOG.log(Level.FINE, ("Get text from clipboard: " + data));
            //将文本分发给各接收者
            super.notifyObservers(data);
        }
    }

    /**
     * 获得当前剪贴板中文本
     *
     * @return 文本
     */
    private String getTextFromClipboard() {
        String data = null;
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            try {
                data = (String) clipboard.getData(DataFlavor.stringFlavor);
                data = data.trim();
            } catch (UnsupportedFlavorException | IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return data;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    private class TextReader implements Runnable {

        @Override
        public void run() {
            handleText();
        }

    }

    /**
     * 获得单例
     *
     * @return
     */
    public static TextProvider getInstance() {
        return TextProviderInstance.instance;
    }

    public static void main(String[] args) {
        TextProvider tp = TextProvider.getInstance();
        tp.addObserver(new Observer<String>() {
            @Override
            public void handle(Observable subject, String object) {
                System.out.println(object);
            }
        });
        tp.start();
    }
}
