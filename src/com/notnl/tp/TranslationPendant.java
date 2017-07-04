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

import com.notnl.tp.config.Config;
import com.notnl.tp.ui.TextBox;
import com.notnl.tp.util.ArrayUtil;
import com.notnl.tp.util.Observable;
import com.notnl.tp.util.Observer;
import com.notnl.tp.util.StringUtil;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Java翻译挂件 (TranslationPendant)<br/>
 * 中心类
 *
 * @author noti0na1
 */
public class TranslationPendant {

    private static final Logger LOG = Logger.getLogger(TranslationPendant.class.getName());

    /**
     * 存放配置实例
     */
    private Config config;
    /**
     * 文本框接口
     */
    private TextBox textBox;
    /**
     * 文本提供者
     */
    private TextProvider provider;
    /**
     * 文本处理者
     */
    private TextHandler handler;

    /**
     * 初始化TranslationPendant
     */
    public TranslationPendant() {
        //获得配置对象
        config = Config.getInstance();
        //获得文本提供者实例
        provider = TextProvider.getInstance();
        //设置（初始化）文本获取间隔
        provider.setInterval(config.getInterval());
        //初始化文本处理者
        handler = new TextHandler(config.getEngine(),
                config.getFrom(), config.getTo(), config.isOpenBeteTran());
        //初始化界面
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                textBox = new TextBox(TranslationPendant.this);
                //应用各项设置
                textBox.updataSetting();
            }
        });
        //增加事件观察者
        addEventObserver();
    }

    /**
     * 增加事件观察者
     */
    private void addEventObserver() {
        provider.addObserver(new Observer<String>() {
            @Override
            //获得新文本时调用此方法
            public void handle(Observable<String> subject, String src) {
                //获得文本
                if (StringUtil.isEmpty(src)) {
                    return;
                }
                String[] result = null;
                try {
                    //将源文本提供给处理者获取结果
                    result = handler.getResult(src);
                } catch (IOException ex) {
                    LOG.log(Level.WARNING, null, ex);
                    return;
                } catch (RuntimeException ex) {
                    LOG.log(Level.WARNING, null, ex);
                    //显示Exception
                    textBox.showError(ex);
                    return;
                }
                if (ArrayUtil.isEmpty(result)) {
                    return;
                }
                //控制台输出信息
                LOG.log(Level.INFO, Arrays.toString(result));
                //将结果显示到文本框
                textBox.setDisplayText(result);
            }
        });
    }

    /**
     * 启动文本获取
     */
    public void start() {
        LOG.log(Level.INFO, "TranslationPendant started!");
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                textBox.setVisible(true);
            }
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        provider.start();
    }

    /**
     * 重置各项设置
     */
    public void updata() {
        provider.setInterval(config.getInterval());
        handler.setEngine(config.getEngine());
        handler.setFrom(config.getFrom());
        handler.setTo(config.getTo());
        handler.setOpenBeteTran(config.isOpenBeteTran());
    }

}
