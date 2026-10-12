package com.ittxf.study.kmp;

public class ViolenceMatch {
    public static void main(String[] args) {
        String str1 = "hello txf, thank you";
        String str2 = "ll";
        int index = violenceMatch(str1, str2);
        System.out.println("index = " + index);
    }

    /**
     * 暴力匹配算法
     * @param str1 主字符串
     * @param str2 子字符串
     * @return 返回子字符串在主字符串中的起始位置，如果没有返回-1
     */
    public static int violenceMatch(String str1, String str2) {
        char[] s1 = str1.toCharArray();
        char[] s2 = str2.toCharArray();
        int s1Len = s1.length;
        int s2Len = s2.length;
        int i = 0;
        int j = 0;
        while (i < s1Len && j < s2Len) {
            if (s1[i] == s2[j]) { // 匹配成功
                i++;
                j++;
            } else { // 匹配失败
                i = i - j + 1; // 回溯，i退回到匹配的下一个位置
                j = 0; // 重置j
            }
        }
        if (j == s2Len) return i - j + 1; // 子字符串匹配成功
        return -1; // 子字符串匹配失败
    }
}
