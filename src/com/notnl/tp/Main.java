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

import com.notnl.tp.config.Info;
import com.notnl.tp.util.ArrayUtil;
import com.notnl.tp.util.net.BrowserLauncher;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Java翻译挂件 (TranslationPendant)<br/>
 * 启动类
 *
 * @author noti0na1
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    /**
     * 主方法
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        configureLogger();
        LOG.log(Level.INFO, ("Args: " + Arrays.toString(args)));
        //配置Swing
        configureSwing();
        showVersionMessage(args);
        //启动TranslationPendant
        new TranslationPendant().start();
    }

    private static void configureLogger() {
        if (System.getProperty("java.util.logging.config.class") == null
                && System.getProperty("java.util.logging.config.file") == null) {
            try {
                File logFolder = new File("data/log");
                if (!logFolder.exists()) {
                    logFolder.mkdirs();
                }
                Logger logger = Logger.getLogger("com.notnl.tp");
                logger.setLevel(Level.ALL);
                final int LOG_ROTATION_COUNT = 10;
                Handler handler = new FileHandler("data/log/TP.log", 0, LOG_ROTATION_COUNT);
                logger.addHandler(handler);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Can't creat log file handler.", ex);
            }
        }
    }

    private static void configureSwing() {
        try {
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            for (UIManager.LookAndFeelInfo info
                    : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException ex) {
            LOG.log(Level.SEVERE, "Swing error.", ex);
        }
    }

    private static void showVersionMessage(String[] args) {
        if (ArrayUtil.isEmpty(args) || !args[args.length - 1].equals("1.3")) {
            JOptionPane.showMessageDialog(null,
                    "最新版启动器处理了严重Bug，请尽快从README.TXT里的下载链接里下载最新版！",
                    "版本警告", JOptionPane.WARNING_MESSAGE
            );
            BrowserLauncher.openURL(Info.DOWNLOAD_URL);
        }
    }

    private static void showBeteVersionMessage() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null,
                        "此为3.0测试版，不稳定。\n将在0323内发布正式版！",
                        "测试版警告", JOptionPane.WARNING_MESSAGE
                );
            }
        });
    }

}
