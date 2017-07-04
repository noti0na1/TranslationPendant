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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author noti0na1
 */
public class ObjectUtil {

    private ObjectUtil() {
    }

    public static String toString(Object object) {
        if (object == null) {
            return "null";
        }
        StringBuilder buf = new StringBuilder(500);
        Class clazz = object.getClass();
        buf.append(clazz.getSimpleName());
        if (clazz.isArray()) {
            buf.append("(length:").append(Array.getLength(object)).append(")");
            getArrayData(object, buf);
        } else {
            try {
                buf.append('{');
                getFieldsSimpleData(object, buf);
                buf.append('}');
            } catch (IllegalAccessException ex) {
                buf.delete(0, buf.length());
                buf.append(getErrorMessage(ex));
            }
        }
        return buf.toString();
    }

    private static void getFieldsSimpleData(Object object, StringBuilder buf)
            throws IllegalAccessException {
        if (object == null || buf == null) {
            throw new NullPointerException();
        }

        Field[] fields = object.getClass().getDeclaredFields();
        if (ArrayUtil.isEmpty(fields)) {
            return;
        }
        int max = fields.length - 1;
        for (int i = 0;; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            buf.append(field.getName()).append("=");
            buf.append(String.valueOf(field.get(object)));
            if (i == max) {
                break;
            }
            buf.append(", ");
        }
    }

    public static String getObjectInfo(Object object) {
        if (object == null) {
            return "null";
        }
        StringBuilder buf = new StringBuilder(500);
        Class clazz = object.getClass();
        buf.append(Modifier.toString(clazz.getModifiers())).append(' ');
        if (clazz.isEnum()) {
            buf.append("enum ");
        } else {
            buf.append("class ");
        }
        buf.append(clazz.getName()).append("\n");

        if (clazz.isArray()) {
            getArrayData(object, buf);
        } else {
            try {
                getFieldsData(object, buf);
            } catch (IllegalAccessException ex) {
                buf.delete(0, buf.length());
                buf.append(getErrorMessage(ex));
            }
        }
        return buf.toString();
    }

    private static void getArrayData(Object object, StringBuilder buf) {
        if (object == null || buf == null) {
            throw new NullPointerException();
        }
        Class clazz = object.getClass();
        if (!clazz.isArray()) {
            throw new IllegalArgumentException("Object is not a array!");
        }

        if (clazz == byte[].class) {
            buf.append(Arrays.toString((byte[]) object));
        } else if (clazz == short[].class) {
            buf.append(Arrays.toString((short[]) object));
        } else if (clazz == int[].class) {
            buf.append(Arrays.toString((int[]) object));
        } else if (clazz == long[].class) {
            buf.append(Arrays.toString((long[]) object));
        } else if (clazz == char[].class) {
            buf.append(Arrays.toString((char[]) object));
        } else if (clazz == float[].class) {
            buf.append(Arrays.toString((float[]) object));
        } else if (clazz == double[].class) {
            buf.append(Arrays.toString((double[]) object));
        } else if (clazz == boolean[].class) {
            buf.append(Arrays.toString((boolean[]) object));
        } else {
            buf.append(Arrays.deepToString((Object[]) object));
        }
    }

    private static void getFieldsData(Object object, StringBuilder buf)
            throws IllegalAccessException {
        if (object == null || buf == null) {
            throw new NullPointerException();
        }

        Field[] fields = object.getClass().getDeclaredFields();
        if (ArrayUtil.isEmpty(fields)) {
            return;
        }
        int max = fields.length - 1;
        for (int i = 0;; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Class fClass = field.getType();
            buf.append("  ").append(Modifier.toString(field.getModifiers()));
            buf.append(' ').append(fClass.getSimpleName()).append(' ');
            buf.append(field.getName()).append(": ");

            Object fObject = field.get(object);
            if (fObject == null) {
                buf.append("null");
            } else {
                if (fClass.isArray()) {
                    buf.append("(length:").append(Array.getLength(fObject)).append(") ");
                    getArrayData(fObject, buf);
                } else {
                    buf.append(String.valueOf(fObject));
                }
            }

            if (i == max) {
                break;
            }
            buf.append('\n');
        }
    }

    public static String getErrorMessage(Exception ex) {
        if (ex == null) {
            throw new NullPointerException();
        }

        StackTraceElement[] stes = ex.getStackTrace();
        int bufLen = 30 + stes.length * 40;
        if (stes.length != 0 && bufLen <= 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuilder buf = new StringBuilder(bufLen);

        buf.append(ex.getMessage()).append('\n');
        int max = stes.length - 1;
        for (int i = 0;; i++) {
            buf.append('\t').append(stes[i].toString());
            if (i == max) {
                break;
            }
            buf.append('\n');
        }
        return buf.toString();
    }

    public static void main(String[] args) {
//        int[][] a = {{1, 4, 5}, {4, 8, 9}, {3}};
//        System.out.println(Arrays.toString(a));
        int[] a = {1, 2, 3, 4, 5};
        System.out.println(getObjectInfo("aaa"));
        List<String> b = new ArrayList<String>();
        b.add("aaa");
        b.add("bbb");
        System.out.println(getObjectInfo(b));
        System.out.println(getObjectInfo(new JFrame("test")));
        System.out.println(toString(b));
        System.out.println(toString(a));
    }
}
