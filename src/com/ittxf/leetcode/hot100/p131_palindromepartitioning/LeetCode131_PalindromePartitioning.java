package com.ittxf.leetcode.hot100.p131_palindromepartitioning;

import java.util.ArrayList;
import java.util.List;

public class LeetCode131_PalindromePartitioning {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.partition("aab"));
    }
}

class Solution {
    public List<List<String>> partition(String s) {
        ArrayList<List<String>> res = new ArrayList<>();
        backtrack(res, new ArrayList<>(), s, 0);
        return res;
    }

    private void backtrack(ArrayList<List<String>> res, ArrayList<String> path, String s, int i) {
        if (i == s.length()) {
            res.add(new ArrayList<>(path));
            return;
        }

        for (int j = i; j < s.length(); j++) {
            if (isPalindrome(s, i, j)) {
                path.add(s.substring(i, j + 1));
                backtrack(res, path, s, j + 1);
                path.removeLast();
            }
        }
    }

    private boolean isPalindrome(String s, int i, int j) {
        while (i < j) {
            if (s.charAt(i) != s.charAt(j)) return false;
            i++;
            j--;
        }
        return true;
    }
}

