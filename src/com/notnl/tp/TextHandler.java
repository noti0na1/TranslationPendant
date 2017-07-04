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

import com.notnl.tp.util.ArrayUtil;
import com.notnl.tp.util.StringUtil;
import com.notnl.tp.util.net.TranslateInter;
import com.notnl.tp.util.net.Translater;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Java翻译挂件 (TranslationPendant)<br/>
 * 文本处理类
 *
 * @author noti0na1
 */
public class TextHandler {

    private static final Logger LOG = Logger.getLogger(TextHandler.class.getName());

    /**
     * 引擎
     */
    private String engine;
    /**
     * 源语言
     */
    private String from;
    /**
     * 目标语言
     */
    private String to;
    /**
     * 是否开启测试翻译功能
     */
    private boolean openBeteTran;

    public TextHandler() {
        this.engine = "";
        this.from = "auto";
        this.to = "auto";
        this.openBeteTran = false;
    }

    /**
     *
     * @param engine 引擎
     * @param from 源语言
     * @param to 目标语言
     * @param openBeteTran 是否开启测试翻译功能
     */
    public TextHandler(String engine, String from, String to, boolean openBeteTran) {
        this.engine = engine;
        this.from = from;
        this.to = to;
        this.openBeteTran = openBeteTran;
    }

    /**
     * 获得翻译结果
     *
     * @param src 源文本
     * @return 返回一个数组，0和偶数行为原文，奇数行为译文
     * @throws IOException
     */
    public String[] getResult(String src) throws IOException {
        if (StringUtil.isEmpty(src)) {
            throw new NullPointerException("src");
        }
        TranslateInter translater = Translater.getEngine(engine);
        if (openBeteTran && !"en".equals(to)) {
            //如果开启了测试翻译功能，则现将原文翻译为英文，再将英文翻译为目标语言
            String[] temp1 = translater.translate(src, from, "en");
            //System.out.println("-" + Arrays.toString(temp1));
            String[] temp2 = translater.translate(getDstFromArray(temp1), "en", to);
            //System.out.println("-" + Arrays.toString(temp2));
            return mergeSrcAndDst(temp1, temp2);
        } else {
            return translater.translate(src, from, to);
        }
    }

    /**
     * 从结果数组中取得译文
     *
     * @param res
     * @return 译文
     */
    private String getDstFromArray(String[] res) {
        if (ArrayUtil.isEmpty(res)) {
            throw new NullPointerException();
        }
        if (res.length % 2 != 0) {
            throw new IllegalArgumentException("result's length error! " + res.length);
        }
        int bufLen = res.length * 20;
        if (res.length != 0 && bufLen <= 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuilder buf = new StringBuilder(bufLen);
        for (int i = 1; i < res.length; i += 2) {
            buf.append(res[i]).append('\n');
        }
        return buf.toString().trim();
    }

    /**
     * 合并原文所在数组及译文所在数组
     *
     * @param srcIn
     * @param dstIn
     * @return
     */
    private String[] mergeSrcAndDst(String[] srcIn, String[] dstIn) {
        if (ArrayUtil.isEmpty(srcIn) || ArrayUtil.isEmpty(dstIn)) {
            throw new NullPointerException();
        }
        if (srcIn.length % 2 != 0 || dstIn.length % 2 != 0) {
            throw new IllegalArgumentException("array length error");
        }
        int srclen = srcIn.length;
        int dstlen = dstIn.length;
        int len = srclen > dstlen ? srclen : dstlen;

        String[] res = new String[len];
        for (int i = 0; i < len; i += 2) {
            res[i] = i < srclen ? srcIn[i] : "";
            res[i + 1] = i + 1 < dstlen ? dstIn[i + 1] : "";
        }
        return res;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
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

    public boolean isOpenBeteTran() {
        return openBeteTran;
    }

    public void setOpenBeteTran(boolean openBeteTran) {
        this.openBeteTran = openBeteTran;
    }
}
