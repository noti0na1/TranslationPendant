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

import com.notnl.tp.TranslationPendant;
import com.notnl.tp.config.Info;
import com.notnl.tp.util.FrameUtil;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Java翻译挂件 (TranslationPendant)<br/>
 * 文本窗口类
 *
 * @author noti0na1
 */
public class TextBox extends TextBoxModel {

    /**
     * 图标路径
     */
    private static final String ICON_PATH = "data/icon.png";
    /**
     * 标题
     */
    private static final String TITLE = "在线翻译挂件 Ver." + Info.VERSION;

    /**
     * 窗口宽度
     */
    private static final int BOX_WIDTH = 860;
    /**
     * 窗口高度
     */
    private static final int BOX_HEIGHT = 260;
    /**
     * 各组件间间隔
     */
    private static final int SPACING = 12;
    /**
     * TranslationPendant类对象
     */
    private TranslationPendant tp;
    
    /**
     * 菜单按钮
     */
    private JButton button = null;

    //记录临时数据
    private Point loc = null;
    private Point tmp = null;
    //是否按住
    private boolean isDragged = false;

    /**
     * 初始化窗口
     * 
     * @param tp TranslationPendant类对象
     */
    public TextBox(TranslationPendant tp) {
        super(TITLE);
        this.tp = tp;
        //初始化组件
        this.initComponents();
    }

    /**
     * 初始化组件
     */
    private void initComponents() {
        //设置窗口图标
        super.setIconImage(new ImageIcon(ICON_PATH).getImage());
        //设置窗口大小
        super.setSize(BOX_WIDTH, BOX_HEIGHT);
        //剧中窗口
        FrameUtil.setFrameCenter(this, -200);
        //显示默认文本
        super.setDisplayText(new String[]{"等待载入文本。。。"});
        
        super.setLayout(null);
        //按钮图标
        Icon buttonIcon = new ImageIcon("data/button.png");
        this.button = new JButton(buttonIcon);
        this.button.setBounds(SPACING, SPACING,
                buttonIcon.getIconWidth(), buttonIcon.getIconHeight());
        //为按钮添加右键菜单
        this.initMenu();
        this.addButtonMouseListener();
        super.getContentPane().add(button);
        super.textLabel.setBounds(
                SPACING * 2 + buttonIcon.getIconWidth(),
                SPACING,
                BOX_WIDTH - SPACING * 3 - buttonIcon.getIconWidth(),
                BOX_HEIGHT - SPACING * 2
        );
        super.getContentPane().add(this.textLabel);
        //增加窗口监听器
        this.addWinListeners();
    }

    /**
     * 初始化按钮右键菜单
     *
     * @return
     */
    private void initMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.setFont(new Font("微软雅黑", 0, 12));
        JMenuItem configItem = new JMenuItem("设置");
        configItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showShowConfigFrame();
            }
        });
        menu.add(configItem);
        
        menu.setFont(new Font("微软雅黑", 0, 12));
        JMenuItem debug = new JMenuItem("Debug");
        configItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        menu.add(debug);
        
        JMenuItem about = new JMenuItem("关于");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAbout();
            }
        });
        menu.add(about);
        
        menu.addSeparator();
        
        JMenuItem exit = new JMenuItem("退出");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        menu.add(exit);
        
        this.button.setComponentPopupMenu(menu);
    }

    /**
     * 添加按钮的鼠标事件监听
     */
    private void addButtonMouseListener() {
        this.button.addMouseListener(new MouseAdapter() {
            /**
             * 鼠标进入到组件上时调用。
             *
             * @param e
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }

            /**
             * 鼠标离开组件时调用。
             *
             * @param e
             */
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            /**
             * 鼠标按钮在组件上释放时调用。
             *
             * @param e
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                isDragged = false;
                //为指定的光标设置光标图像
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            /**
             * 鼠标按键在组件上按下时调用。
             *
             * @param e
             */
            @Override
            public void mousePressed(MouseEvent e) {
                //判断是否为左键
                if (e.getButton() == 1) {
                    tmp = new Point(e.getX(), e.getY());
                    isDragged = true;
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                }
            }
        });
        this.button.addMouseMotionListener(new MouseMotionAdapter() {
            /**
             * 鼠标按键在组件上按下并拖动时调用。
             *
             * @param e
             */
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragged) {
                    loc = new Point(
                            getLocation().x + e.getX() - tmp.x,
                            getLocation().y + e.getY() - tmp.y
                    );
                    setLocation(loc);
                }
            }
        });
    }

    /**
     * 添加滑轮事件监听
     * 以后用于实现查看历史
     */
    private void addWinListeners() {
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        super.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                //TODO
                //System.out.println(e.paramString());
            }
        });
    }

    

    @Override
    /**
     * 重置各项设置
     */
    public void updataSetting() {
        super.updataSetting();
        this.tp.updata();
        super.repaint();
    }
}
