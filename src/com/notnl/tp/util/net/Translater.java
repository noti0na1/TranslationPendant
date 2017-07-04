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

import com.notnl.tp.util.ObjectUtil;
import com.notnl.tp.util.StringUtil;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author noti0na1
 */
public class Translater {

    private static final Logger LOG = Logger.getLogger(Translater.class.getName());

    private static Map<String, String> languages;

    private static String[] languageTypes;

    private static Map<String, TranslateInter> engines;

    private static String[] enginesName;

    static {
        Properties lanPro = new Properties();
        Properties engPro = new Properties();
        try {
            lanPro.load(Translater.class.getResource(
                    "Languages.properties"
            ).openStream());
            engPro.load(Translater.class.getResource(
                    "TranslateEngines.properties"
            ).openStream());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "翻译引擎配置文件读取失败！<br/>"
                    + ObjectUtil.getErrorMessage(ex),
                    "错误", JOptionPane.WARNING_MESSAGE
            );
            System.exit(1);
        }
        languages = new HashMap<>(lanPro.size());
        for (Entry l : lanPro.entrySet()) {
            languages.put(l.getKey().toString(), l.getValue().toString());
        }
        engines = new HashMap<>(engPro.size());
        try {
            for (Entry e : engPro.entrySet()) {
                engines.put(
                        e.getKey().toString(),
                        (TranslateInter) Class.forName(e.getValue().toString()).newInstance()
                );
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "翻译引擎载入失败！<br/>"
                    + ObjectUtil.getErrorMessage(ex),
                    "错误", JOptionPane.WARNING_MESSAGE
            );
            System.exit(1);
        }
    }

    public static String[] getEngines() {
        if (engines == null || engines.isEmpty()) {
            return null;
        }
        if (enginesName == null) {
            enginesName = engines.keySet().toArray(new String[engines.size()]);
        }
        return enginesName;
    }

    public static TranslateInter getEngine(String name) {
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        if (engines == null || engines.isEmpty()) {
            return null;
        }
        return engines.get(name);
    }

    public static String[] getLanguageTypes() {
        if (languages == null || languages.isEmpty()) {
            return null;
        }
        if (languageTypes == null) {
            languageTypes = languages.keySet().toArray(new String[languages.size()]);
        }
        return languageTypes;
    }

    public static String getLanguageCode(String type) {
        if (StringUtil.isEmpty(type)) {
            return null;
        }
        if (languages == null || languages.isEmpty()) {
            return null;
        }
        if (languages.containsKey(type)) {
            return languages.get(type);
        }
        return "auto";
    }

    public static String getLanguageType(String code) {
        if (StringUtil.isEmpty(code)) {
            return null;
        }
        if (languages == null || languages.isEmpty()) {
            return null;
        }
        for (Entry<String, String> l : languages.entrySet()) {
            if (code.equals(l.getValue())) {
                return l.getKey();
            }
        }
        return "自动检测";
    }
    
    static String simpleHandle(String text) {
        return text.replace("\r", "\n").replaceAll("·|☆|★|❤|♥|～|~", "")
                .replaceAll("「|『", "“").replaceAll("」|』", "”");
    }

    public static void main(String[] args) throws IOException {
        System.out.println(Arrays.toString(Translater.getLanguageTypes()));
        System.out.println(Arrays.toString(Translater.getEngines()));
        TranslateInter engine = Translater.getEngine("baidu");
        System.out.println(Arrays.toString(engine.translate(
                "お花の匂い！\nそんなワケないお。",
                "jp",
                "zh"
        )));
    }
}
