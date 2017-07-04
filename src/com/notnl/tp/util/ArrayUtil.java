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

import java.util.Arrays;

/**
 *
 * @author noti0na1
 */
public class ArrayUtil {

    public static <T> T[] addAtEnd(T[] array1, T... array2) {
        T[] newArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return newArray;
    }

    public static byte[] addAtEnd(byte[] array1, byte... array2) {
        byte[] newArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return newArray;
    }

    public static short[] addAtEnd(short[] array1, short... array2) {
        short[] newArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return newArray;
    }

    public static int[] addAtEnd(int[] array1, int... array2) {
        int[] newArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return newArray;
    }

    public static long[] addAtEnd(long[] array1, long... array2) {
        long[] newArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return newArray;
    }

    public static char[] addAtEnd(char[] array1, char... array2) {
        char[] newArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return newArray;
    }

    public static float[] addAtEnd(float[] array1, float... array2) {
        float[] newArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return newArray;
    }

    public static double[] addAtEnd(double[] array1, double... array2) {
        double[] newArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return newArray;
    }

    public static boolean[] addAtEnd(boolean[] array1, boolean... array2) {
        boolean[] newArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return newArray;
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static void main(String[] args) {
        byte[] ba = new byte[]{0x1, 0x3, 0x5};
        System.out.println(Arrays.toString(ba));
        ba = ArrayUtil.addAtEnd(ba, (byte) 0x6, (byte) 0x9);
        System.out.println(Arrays.toString(ba));
        ba = ArrayUtil.addAtEnd(ba, ba);
        System.out.println(Arrays.toString(ba));

        String[] a = {"a", "b"};
        System.out.println(Arrays.toString(a));
        a = ArrayUtil.addAtEnd(a, "c");
        System.out.println(Arrays.toString(a));
        String[] b = {"1", "2"};
        a = ArrayUtil.addAtEnd(a, b);
        System.out.println(Arrays.toString(a));

    }
}
