package com.ittxf.leetcode.hot100.p78_subsets;

import java.util.ArrayList;
import java.util.List;

public class LeetCode78_Subsets {
    public static void main(String[] args) {
        Solution solution = new Solution();
        // 通过结果很好看清思路
        System.out.println(solution.subsets(new int[]{1, 2, 3}));
    }
}

class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        backtrack(nums, 0, new ArrayList<>(), res);
        return res;
    }
    public void backtrack(int[] nums, int start, List<Integer> track, List<List<Integer>> res) {
        // 子集问题，每一个节点都是一个合法的子集，进来先存
        res.add(new ArrayList<>(track));
        // 固定 start，递归 i 及其之后的元素
        for (int i = start; i < nums.length; i++) {
            track.add(nums[i]);
            // 递归下一层时，传入 i + 1，只能选后面的
            backtrack(nums, i + 1, track, res);
            track.remove(track.size() - 1);
        }
    }
}

