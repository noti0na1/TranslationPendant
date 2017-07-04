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
package com.notnl.tp.util.net;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 支持: Mac OS X, GNU/Linux, Unix, Windows XP <br/>
 *
 * 原文件名：BareBonesBrowserLaunch.java <br/>
 * 原作者：Dem Pilafian, John Kristian <br/>
 * 修改者：noti0na1 <br/>
 * 发表时间：December 10, 2005 <br/>
 * 原文件来源于互联网，可自由修改、传播并免费使用 <br/>
 *
 * @author Dem Pilafian
 * @author John Kristian
 * @author noti0na1
 */
public class BrowserLauncher {

    private static final Logger LOG = Logger.getLogger(BrowserLauncher.class.getName());

    private BrowserLauncher() {
    }

    /**
     * 打开链接
     *
     * @param url 链接
     */
    public static void openURL(String url) {
        try {
            LOG.log(Level.FINE, ("Open URL: " + url));
            browse(url);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Open URL error.", ex);
        }
    }

    /**
     * 启动浏览器
     *
     * @param url
     * @throws Exception
     */
    private static void browse(String url) throws Exception {
        //获取操作系统的名字  
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Mac OS")) {
            //苹果的打开方式  
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[]{String.class});
            openURL.invoke(null, new Object[]{url});
        } else if (osName.startsWith("Windows")) {
            //windows的打开方式。  
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else {
            // Unix or Linux的打开方式  
            String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
            String browser = null;
            //执行代码，在brower有值后跳出，  
            for (int count = 0; count < browsers.length && browser == null; count++) {
                //这里是如果进程创建成功了，==0是表示正常结束。
                if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                    browser = browsers[count];
                }
            }
            if (browser == null) {
                throw new IOException("Could not find web browser");
            } else {
                //这个值在上面已经成功的得到了一个进程。  
                Runtime.getRuntime().exec(new String[]{browser, url});
            }
        }
    }
}
