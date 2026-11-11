package com.ittxf.leetcode.hot100.p55_jumpgame;

public class LeetCode55_JumpGame {
    public static void main(String[] args) {
        System.out.println(new Solution().canJump(new int[]{2, 3, 1, 1, 4}));
        System.out.println(new Solution().canJump(new int[]{3, 2, 1, 0, 4}));
    }
}

class Solution {
    public boolean canJump(int[] nums) {
        int maxReach = 0;
        for (int i = 0; i < nums.length - 1 && i <= maxReach; i++) {
            maxReach = Math.max(maxReach, i + nums[i]);
        }
        return maxReach >= nums.length - 1;
    }
}

