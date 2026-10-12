package com.ittxf.study.kmp;

import java.util.Arrays;

public class KMPAlgorithm {
    public static void main(String[] args) {
        String str1 = "BBC ABCDAB ABCDABCDABDE";
        String str2 = "ABCDABD";

        int[] next = kmpNext(str2); // [0, 0, 0, 0, 1, 2, 0] 前缀和后缀相等的长度
        System.out.println(Arrays.toString(next));

        int index = kmpSearch(str1, str2, next);
        if (index != -1) System.out.println("index = " + index);
        else System.out.println("未找到");
    }

    /**
     * 获取部分匹配值表，即next数组
     * @param dest 目标字符串
     * @return 部分匹配值表，即next数组
     */
    public static int[] kmpNext(String dest) {
        // 创建一个next数组保存部分匹配值
        int[] next = new int[dest.length()];
        next[0] = 0; // 如果字符串长度为1，部分匹配值为0
        for (int i = 1, j = 0; i < dest.length(); i++) { // i是后缀的末尾，j是前缀的末尾
            // KMP算法核心逻辑
            while (j > 0 && dest.charAt(i) != dest.charAt(j)) j = next[j - 1]; // 不相等时，j需要回退
            if (dest.charAt(i) == dest.charAt(j)) j++; // 前缀和后缀相等长度+1
            next[i] = j;
        }
        return next;
    }

    /**
     * KMP搜索算法
     * @param str1 主字符串
     * @param str2 子字符串
     * @param next 部分匹配值表，即next数组
     * @return 子字符串在主字符串中的起始位置，未找到返回-1
     */
    public static int kmpSearch(String str1, String str2, int[] next) {
        // 遍历主字符串，i是主字符串的索引，j是子字符串的索引
        for (int i = 0, j = 0; i < str1.length(); i++) {
            // KMP算法核心逻辑
            while (j > 0 && str1.charAt(i) != str2.charAt(j)) j = next[j - 1]; // 不相等时，j需要回退
            if (str1.charAt(i) == str2.charAt(j)) j++; // 匹配成功
            if (j == str2.length()) return i - j + 1; // 找到子字符串返回起始位置
        }
        return -1; // 未找到
    }
}
