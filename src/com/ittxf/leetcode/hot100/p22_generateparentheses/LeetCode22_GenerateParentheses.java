package com.ittxf.leetcode.hot100.p22_generateparentheses;

import java.util.ArrayList;
import java.util.List;

public class LeetCode22_GenerateParentheses {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.generateParenthesis(3));
    }
}

class Solution {
    public List<String> generateParenthesis(int n) {
        ArrayList<String> res = new ArrayList<>();
        backtrack(res, new StringBuilder(), n, 0, 0);
        return res;
    }

    public void backtrack(List<String> res, StringBuilder track, int n, int open, int close) {
        if (track.length() == n * 2) {
            res.add(track.toString());
            return;
        }
        if (open < n){
            backtrack(res, track.append("("), n, open + 1, close);
            track.deleteCharAt(track.length() - 1);
        }
        if (close < open){
            backtrack(res, track.append(")"), n, open, close + 1);
            track.deleteCharAt(track.length() - 1);
        }
    }
}
