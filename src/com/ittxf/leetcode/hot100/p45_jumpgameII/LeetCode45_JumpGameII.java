package com.ittxf.leetcode.hot100.p45_jumpgameII;

public class LeetCode45_JumpGameII {
    public static void main(String[] args) {
        System.out.println(new Solution().jump(new int[]{2, 3, 1, 1, 4}));
        System.out.println(new Solution().jump(new int[]{2, 3, 0, 1, 4}));
    }
}

class Solution {
    public int jump(int[] nums) {
        int maxReach = 0; // 当前步能到达的最远位置
        int steps = 0;
        int currentEnd = 0; // 目前的能到达的边界
        for (int i = 0; i < nums.length - 1 && i <= maxReach; i++) {
            maxReach = Math.max(maxReach, i + nums[i]);
            if (i == currentEnd) {
                ++steps;
                currentEnd = maxReach;
            }
        }
        return steps;
    }
}

