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

import com.notnl.tp.config.Info;
import com.notnl.tp.util.ObjectUtil;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Java翻译挂件 (TranslationPendant)<br/>
 * 自定义字体类
 *
 * @author noti0na1
 */
public class MyFonts {

    private static final Logger LOG = Logger.getLogger(MyFonts.class.getName());

    public static final int CUSTOM_FONT = 0;

    public static final int SYSTEM_FONT = 1;

    private static final String[] FONT_FILE_TYPES = {".ttf", ".ttc"};

    public static final Float[] FONT_SIZE
            = new Float[]{10f, 12f, 14f, 16f, 18f, 20f, 24f, 36f, 48f};

    private static final Map<String, Integer> FONT_STYLE = new HashMap<String, Integer>();

    static {
        FONT_STYLE.put("普通", Font.PLAIN);
        FONT_STYLE.put("粗体", Font.BOLD);
        FONT_STYLE.put("斜体", Font.ITALIC);
        FONT_STYLE.put("粗斜体", Font.BOLD | Font.ITALIC);
    }

    private static String[] styles;

    public static String[] getFontStyles() {
        if (styles == null) {
            styles = FONT_STYLE.keySet().toArray(new String[FONT_STYLE.size()]);
        }
        return styles;
    }

    public static int getStyle(String name) {
        return FONT_STYLE.get(name);
    }

    public static String getStyleName(int style) {
        for (Entry<String, Integer> s : FONT_STYLE.entrySet()) {
            if (style == s.getValue()) {
                return s.getKey();
            }
        }
        return "普通";
    }

    /**
     * 字体缓存
     */
    private static Map<FontFlag, Font> cache = new HashMap<FontFlag, Font>();

    public static String[] getFonts(int type) {
        if (type == CUSTOM_FONT) {
            return getCustomFonts();
        } else if (type == SYSTEM_FONT) {
            return getSystemFonts();
        }
        throw new IllegalArgumentException("Type is defined: " + type);
    }

    private static String[] getCustomFonts() {
        //要进行过滤的文件目录  
        File folder = new File(Info.CUSTOM_FONTS_PATH);
        String[] fontFiles = folder.list(new FontFileFilter());
        LOG.log(Level.FINEST, "get fonts", Arrays.toString(fontFiles));
        return fontFiles;
    }

    private static String[] getSystemFonts() {
        Font[] fontArray = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        List<String> fontList = new ArrayList<String>(fontArray.length);
        for (Font f : fontArray) {
            //fontList.add(f.getName());
            fontList.add(f.getFontName());
        }
        return fontList.toArray(new String[fontArray.length]);
    }

    /**
     * 简单文件过滤器
     */
    private static class FontFileFilter implements FilenameFilter {

        @Override
        public boolean accept(File directory, String filname) {
            for (String e : FONT_FILE_TYPES) {
                if (filname.endsWith(e)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class FontFlag {

        private String name;
        private int type;
        private int style;
        private float size;

        public FontFlag(String name, int type, int style, float size) {
            this.name = name;
            this.type = type;
            this.style = style;
            this.size = size;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 47 * hash + Objects.hashCode(this.name);
            hash = 47 * hash + this.type;
            hash = 47 * hash + this.style;
            hash = 47 * hash + Float.floatToIntBits(this.size);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final FontFlag other = (FontFlag) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            if (this.type != other.type) {
                return false;
            }
            if (this.style != other.style) {
                return false;
            }
            return Float.floatToIntBits(this.size) == Float.floatToIntBits(other.size);
        }

        @Override
        public String toString() {
            return ObjectUtil.toString(this);
        }
    }

    /**
     *
     * @param name
     * @param type
     * @param style
     * @param size
     * @return
     */
    public static Font getFont(String name, int type, int style, float size) {
        FontFlag flag = new FontFlag(name, type, style, size);
        LOG.log(Level.FINE, flag.toString());
        //从缓存中读取字体
        if (cache.containsKey(flag)) {
            LOG.log(Level.FINER, ("read font from cache: " + flag.toString()));
            return cache.get(flag);
        }
        Font font = null;
        if (type == CUSTOM_FONT) {
            font = loadFontFromFile(flag);
        } else if (type == SYSTEM_FONT) {
            font = loadSystemFont(flag);
        } else {
            throw new IllegalArgumentException("Type is defined: " + type);
        }
        if (font != null) {
            cache.put(flag, font);
        }
        return font;
    }

    private static Font loadFontFromFile(FontFlag flag) {
        Font font = null;
        try (BufferedInputStream fb = new BufferedInputStream(
                new FileInputStream(new File(Info.CUSTOM_FONTS_PATH + flag.name)))) {
            font = Font.createFont(Font.TRUETYPE_FONT, fb);
        } catch (IOException | FontFormatException ex) {
            LOG.log(Level.SEVERE, "loading font error ", ex);
        }
        if (font != null) {
            return font.deriveFont(flag.style, flag.size);
        }
        return null;
    }

    private static Font loadSystemFont(FontFlag flag) {
        return new Font(flag.name, flag.style, (int) flag.size);
    }

    private MyFonts() {
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(getFonts(CUSTOM_FONT)));
        System.out.println(Arrays.toString(getFonts(SYSTEM_FONT)));
    }
}
