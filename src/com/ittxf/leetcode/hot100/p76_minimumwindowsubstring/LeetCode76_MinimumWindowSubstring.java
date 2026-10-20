package com.ittxf.leetcode.hot100.p76_minimumwindowsubstring;

import java.util.HashMap;

public class LeetCode76_MinimumWindowSubstring {
    public static void main(String[] args) {
        String s = "ADOBECODEBANC", t = "ABC";
        System.out.println(new Solution().minWindow(s, t));
    }
}

class Solution {
    public String minWindow(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) return "";

        // 1. 用数组记录 t 中每个字符需要的数量
        int[] need = new int[128];
        for (char c : t.toCharArray()) {
            need[c]++;
        }

        int left = 0;
        int count = 0; // 记录当前窗口内，已经凑齐的 t 的字符总数（注意是个数，不是种类）
        int minLen = Integer.MAX_VALUE; // 记录最小窗口长度
        int start = 0; // 记录最小窗口的起始索引

        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            // 2. 进窗口
            if (need[rightChar] > 0) { // 如果这个字符是 t 需要的
                count++; // 凑齐的字符数 +1
            }
            need[rightChar]--; // 需要的数量减 1（如果是多余的，会变成负数）

            // 3. 当凑齐了 t 的所有字符，开始收缩左边界
            while (count == t.length()) {
                // 更新最小窗口
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    start = left;
                }

                // 尝试移出左边界字符
                char leftChar = s.charAt(left);
                need[leftChar]++; // 移出窗口，把需要的数量加回来
                if (need[leftChar] > 0) {
                    // 如果加回来之后大于 0，说明移走了一个有用的字符，凑齐的个数要减 1
                    // 循环就会打破，left 停止收缩
                    count--;
                }
                left++; // 左指针右移
            }
        }

        // 如果 minLen 没变过，说明没找到
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }
}