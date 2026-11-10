package com.ittxf.leetcode.hot100.p394_decodestring;

import java.util.ArrayDeque;
import java.util.Deque;

public class LeetCode394_DecodeString {
    public static void main(String[] args) {
        System.out.println(new Solution().decodeString("3[a]2[bc]"));
        System.out.println(new Solution().decodeString("3[a2[c]]"));
        System.out.println(new Solution().decodeString("2[abc]3[cd]ef"));
        System.out.println(new Solution().decodeString("abc3[cd]xyz"));
    }
}

class Solution {
    public String decodeString(String s) {
        Deque<Integer> numStack = new ArrayDeque<>();
        Deque<StringBuilder> strStack = new ArrayDeque<>();

        StringBuilder currentStr = new StringBuilder();
        int multi = 0;

        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) { // 判断是否为数字
                // 处理多位数字（比如 '10'，'100'）
                multi = multi * 10 + (c - '0');
            } else if (c == '[') {
                // 遇到左括号，把当前数字和已拼好的字符串压栈，并重置
                numStack.push(multi);
                strStack.push(currentStr);
                multi = 0;
                currentStr = new StringBuilder();
            } else if (c == ']') {
                // 遇到右括号，出栈进行拼接
                int curMulti = numStack.pop();
                StringBuilder prevStr = strStack.pop();

                // 把括号内的字符串重复 curMulti 并添加到prevStr后面
                prevStr.append(currentStr.toString().repeat(curMulti));

                // 更新当前字符串为拼接后的结果
                currentStr = prevStr;
            } else currentStr.append(c); // 遇到普通字母，直接追加
        }

        return currentStr.toString();

        // 只能处理单位数数字的情况
        /*while (s.contains("[")) {
            // 找到最内层的 []
            int left = s.lastIndexOf("[");
            int right = s.indexOf("]", left);
            // 找到最内层的 [] 的倍数
            int multi = Integer.parseInt(s.substring(left - 1, left));
            // 找到最内层的 [] 的内容
            String str = s.substring(left + 1, right);
            // 替换
            s = s.substring(0, left - 1) +
                    str.repeat(multi) +
                    s.substring(right + 1);
        }
        return s;*/
    }
}

