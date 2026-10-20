package com.ittxf.leetcode.hot100.p53_maximumsubarray;

public class LeetCode53_MaximumSubarray {
    public static void main(String[] args) {
        int[] nums = {-2,1,-3,4,-1,2,1,-5,4};
        System.out.println(new Solution().maxSubArray(nums));
    }
}

class Solution {
    public int maxSubArray(int[] nums) {
        int currentSum = nums[0]; // 当前连续子数组的和
        int maxSum = nums[0]; // 记录历史最大和
        for (int i = 1; i < nums.length; i++) {
            // 核心：是接着前面的队伍继续加，还是自立门户？
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            // 更新历史最大值
            maxSum = Math.max(maxSum, currentSum);

        }
        return maxSum;
    }
}