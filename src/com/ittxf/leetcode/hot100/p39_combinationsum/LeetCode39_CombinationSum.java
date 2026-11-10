package com.ittxf.leetcode.hot100.p39_combinationsum;

import java.util.ArrayList;
import java.util.List;

public class LeetCode39_CombinationSum {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.combinationSum(new int[]{2, 3, 5}, 8));
    }
}

class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        ArrayList<List<Integer>> res = new ArrayList<>();
        backtrack(res, candidates, target, 0, new ArrayList<>(), 0);
        return res;
    }

    public void backtrack(List<List<Integer>> res, int[] candidates, int target, int start, List<Integer> lres, int sum) {
        if (sum == target) {
            res.add(new ArrayList<>(lres));
            return;
        } else if (sum > target) return;

        for (int i = start; i < candidates.length; i++) {
            lres.add(candidates[i]);
            backtrack(res, candidates, target, i, lres, sum + candidates[i]);
            lres.removeLast();
        }
    }
}