package com.ittxf.leetcode.hot100.p198_houserobber;

public class LeetCode198_HouseRobber {
    public static void main(String[] args) {
        System.out.println(new Solution().rob(new int[]{1, 2, 3, 1}));
        System.out.println(new Solution().rob(new int[]{2, 7, 9, 3, 1}));
    }
}

class Solution {
    public int rob(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];

        int prev1 = nums[0];
        int prev2 = Math.max(nums[0], nums[1]);
        int curr = 0;

        if (nums.length == 2) return prev2;
        for (int i = 2; i < nums.length; i++) {
            curr = Math.max(prev2, prev1 + nums[i]);
            prev1 = prev2;
            prev2 = curr;
        }
        return curr;
    }
}