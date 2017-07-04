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
package com.notnl.tp.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * Java翻译挂件
 * <br/>
 * 界面工具类
 *
 * @author noti0na1
 */
public class FrameUtil {

    /**
     * 窗口剧中并位移
     *
     * @param jf
     * @param windowUp
     */
    public static void setFrameCenter(JFrame jf, int windowUp) {
        //获得窗口大小
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screen.width - jf.getWidth() >> 1;
        int y = (screen.height - jf.getHeight() >> 1) - windowUp;
        //设置窗口位置
        jf.setLocation(x, y);
    }
}
