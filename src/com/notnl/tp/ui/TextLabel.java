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

import com.notnl.tp.util.ArrayUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * Java翻译挂件 (TranslationPendant)<br/>
 * 文本栏组件
 *
 * @author noti0na1
 */
class TextLabel extends JPanel {

    private Font defaultFont;

    /**
     * 字与字间隔
     */
    private int tracking = 0;
    /**
     * 阴影位移
     */
    private int left_x = 1, left_y = 1, right_x = 1, right_y = 2;
    /**
     * 左右阴影颜色
     */
    private Color left_color = Color.GRAY, right_color = Color.BLACK;
    /**
     * 是否显示阴影 默认为是
     */
    private boolean showShadow = true;
    /**
     * 仅显示奇数行（译文）
     */
    private boolean onlyShowEvenLines = true;
    /**
     * 文本
     */
    private String[] text;

    public TextLabel() {
        init();
    }

    public TextLabel(String text) {
        this();
        this.text = new String[]{text};
    }

    private void init() {
        defaultFont = Font.getFont("微软雅黑");
    }

    public boolean isShowShadow() {
        return showShadow;
    }

    public void setShowShadow(boolean showShadow) {
        this.showShadow = showShadow;
        super.repaint();
    }

    public boolean isOnlyShowEvenLines() {
        return onlyShowEvenLines;
    }

    public void setOnlyShowEvenLines(boolean onlyShowEvenLines) {
        this.onlyShowEvenLines = onlyShowEvenLines;
        super.repaint();
    }

    public String[] getText() {
        return text;
    }

    public void setText(String text) {
        this.text = new String[]{text};
    }

    public void setText(String[] text) {
        this.text = text;
    }

    /**
     * 覆写字体绘图
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        //抗锯齿
        ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
        ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        this.drawText(g);
    }

    /**
     *
     * @param g
     */
    private void drawText(Graphics g) {
        if (ArrayUtil.isEmpty(text)) {
            return;
        }
        List<char[]> chars = new ArrayList<char[]>();
        if (this.text.length == 1) {
            chars.add(this.text[0].toCharArray());
        } else {
            for (int i = 0; i < text.length; i++) {
                if (this.onlyShowEvenLines && i % 2 == 0) {
                    continue;
                }
                chars.add(text[i].toCharArray());
            }
        }
        FontMetrics fm = this.getFontMetrics(super.getFont());
        int heigth = fm.getAscent();
        for (char[] c : chars) {
            int x = 0;
            for (int i = 0; i < c.length; i++) {
                char ch = c[i];
                if (x + fm.charWidth(ch) > this.getWidth()) {
                    x = 0;
                    heigth += (fm.getHeight() + this.tracking);
                }
                int width = fm.charWidth(ch) + tracking;
                //显示阴影
                if (this.showShadow) {
                    g.setColor(left_color);
                    drawChar(g, ch, x - left_x, heigth - left_y);
                    //g.drawString("" + ch, x - left_x, heigth - left_y);
                    g.setColor(right_color);
                    drawChar(g, ch, x + right_x, heigth + right_y);
                    //g.drawString("" + ch, x + right_x, heigth + right_y);
                }
                g.setColor(getForeground());
                drawChar(g, ch, x, heigth);
                //g.drawString("" + ch, x, heigth);

                x += width;
            }
            heigth += (fm.getHeight() + this.tracking);
        }
        chars.clear();
        chars = null;
        fm = null;
    }

    private void drawChar(Graphics g, char ch, int x, int y) {
        Font displayFont = super.getFont();
        if (displayFont.canDisplay(ch)) {
            g.drawString("" + ch, x, y);
        } else {
            g.setFont(this.defaultFont);
            g.drawString("" + ch, x, y);
            g.setFont(displayFont);
        }
    }
}
