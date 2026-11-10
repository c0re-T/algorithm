package com.ittxf.leetcode.hot100.p17_lettercombinationsofaphonenumber;

import java.util.ArrayList;
import java.util.List;

public class LeetCode17_LetterCombinationsOfAPhoneNumber {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.letterCombinations("23"));
    }
}

class Solution {
    public List<String> letterCombinations(String digits) {
        String[] mapping = {"0", "1", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

        List<String> res = new ArrayList<>();
        if (digits.isEmpty()) return res;

        backtrack(res, digits, 0, new StringBuilder(), mapping);
        return res;
    }

    public void backtrack(List<String> res, String digits, int index, StringBuilder sb, String[] mapping) {
        // 1. 触底条件：如果 index 等于了，说明组合拼完了
        if (index == digits.length()) {
            res.add(sb.toString()); // 把这个完整的组合加入结果集
            return;
        }

        // 2. 取出当前要吃的数字对应的字母串，遍历这几个字母
        for (char c : mapping[digits.charAt(index) - '0'].toCharArray()) {
            // 3. 递归：吃下一颗糖葫芦 index + 1，并把当前字母拼到 sb 上
            backtrack(res, digits, index + 1, sb.append(c), mapping);
            sb.deleteCharAt(sb.length() - 1); // 撤销选择
        }
    }
}
