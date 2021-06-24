package com.asm.code;

import java.util.Arrays;

public class Test {
    public static void m1() {
        Test.m1();
    }
    public static String m2() {
        char[] value = new char[100];
        Arrays.fill(value, '┆');
        return new String(value);
    }
}
