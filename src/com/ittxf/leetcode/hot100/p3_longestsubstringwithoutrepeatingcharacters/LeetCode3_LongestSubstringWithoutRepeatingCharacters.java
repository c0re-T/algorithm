package com.ittxf.leetcode.hot100.p3_longestsubstringwithoutrepeatingcharacters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LeetCode3_LongestSubstringWithoutRepeatingCharacters {
    public static void main(String[] args) {
        String s = "1R1T7";
        System.out.println(new Solution().lengthOfLongestSubstring(s));
    }
}

class Solution {
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) return 0;

        // key: 字符, value: 该字符最后出现的索引 + 1 （也就是如果遇到重复，left 应该跳跃到的位置）
        Map<Character, Integer> map = new HashMap<>();
        int maxLen = 0;
        int left = 0; // 窗口左边界

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);

            // 如果这个字符之前出现过，并且它的位置在当前窗口内（>= left）
            if (map.containsKey(c)) {
                // left 指针直接跳到该字符上一次出现位置的下一个位置
                // 取 max 是为了防止 left 指针往后退（例如 "abba" 的情况）
                left = Math.max(left, map.get(c) + 1);
            }

            // 记录/更新该字符最后出现的位置
            map.put(c, right);

            // 更新最大长度
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }
}

