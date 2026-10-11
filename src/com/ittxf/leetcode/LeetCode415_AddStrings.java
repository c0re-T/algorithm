package com.ittxf.leetcode;

public class LeetCode415_AddStrings {
    public static void main(String[] args) {
        String s = new LeetCode415_AddStrings().addStrings("456", "77");
        System.out.println(s);
    }

    public String addStrings(String num1, String num2) {
        // 从个位数开始加，每一位的最后开始
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        // 进位标志，初始为0
        int forward = 0;
        // StringBuilder本质是一个可变的字符序列，底层是一个字符数组，可以高效地进行字符串拼接
        StringBuilder stringBuilder = new StringBuilder();
        while (i >= 0 || j >= 0 || forward != 0) {
            // 隐式转换
            int x = i >= 0 ? num1.charAt(i--) - '0' : 0;
            int y = j >= 0 ? num2.charAt(j--) - '0' : 0;
            int sum = x + y + forward;
            // 测试相加是否进位
            forward = sum / 10;
            // 余数为当前位的值
            stringBuilder.append(sum % 10);
        }
        // 反转再转字符串
        return stringBuilder.reverse().toString();
    }
}