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

/**
 * 字符串工具类
 *
 * @author chenhetong(chenhetong@baidu.com)
 * @author noti0na1
 */
public class StringUtil {

    /**
     * 判断字符串为空
     *
     * @param str 字符串信息
     * @return true or false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断字符数组，不为空
     *
     * @param values 字符数组
     * @return true or false
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
                if (result == false) {
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * join方法将 Stirng数组，通过separater分隔符进行分割
     *
     * @param resource 源数组
     * @param separater 分隔符
     * @return
     */
    public static String join(String[] resource, String separater) {
        if (resource == null || resource.length == 0) {
            return null;
        }
        int len = resource.length;
        StringBuilder sb = new StringBuilder(resource.length * 20);
        if (len > 0) {
            sb.append(resource[0]);
        }
        for (int i = 1; i < len; i++) {
            sb.append(separater);
            sb.append(resource[i]);
        }
        return sb.toString();
    }

    public static String getMiddle(String sourse, String first, String last) {
        if (!areNotEmpty(sourse, first, last)) {
            return null;
        }
        int beginIndex = sourse.indexOf(first) + first.length();
        int endIndex = sourse.lastIndexOf(last);
        return sourse.substring(beginIndex, endIndex);
    }
}
