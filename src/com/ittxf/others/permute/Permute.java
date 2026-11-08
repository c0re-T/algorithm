package com.ittxf.others.permute;

import java.util.ArrayList;
import java.util.List;

public class Permute {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = {1, 2, 3};
        List<List<Integer>> res = solution.permute(nums);
        System.out.println(res);
    }
}

class Solution {
    List<List<Integer>> res = new ArrayList<>();
    public List<List<Integer>> permute(int[] nums) {
        List<Integer> path = new ArrayList<>();
        backtrack(nums, path);
        return res;
    }

    public void backtrack(int[] nums, List<Integer> path) {
        if (path.size() == nums.length) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (path.contains(nums[i])) continue;
            path.add(nums[i]);
            backtrack(nums, path);
            path.remove(path.size() - 1);
        }
    }
}

