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

import commons.json.Json;
import java.util.Map;

/**
 *
 * @author noti0na1
 */
public class JsonUtil {

    /**
     * 处理Json
     *
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(String json) {
        if (StringUtil.isEmpty(json)) {
            throw new NullPointerException();
        }
        //将Json转为Map
        return (Map<String, Object>) Json.deserialize(json);
        //返回结果Lsit
        //return result.get("trans_result");
        //return (List<Map<String, String>>) result.get("data");
    }
}
