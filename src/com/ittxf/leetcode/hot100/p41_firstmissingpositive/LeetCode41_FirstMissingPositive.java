package com.ittxf.leetcode.hot100.p41_firstmissingpositive;

import java.util.Arrays;

public class LeetCode41_FirstMissingPositive {
    public static void main(String[] args) {
        int[] nums = {7,8,9,11,12};
        System.out.println(new Solution().firstMissingPositive(nums));
    }
}

class Solution {
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            // 只要当前数字在 [1, n] 范围内，且它没有被放到正确的位置上，就不断交换
            // nums[i] 应该放在索引 nums[i] - 1 的位置
            while (nums[i] > 0 && nums[i] <= n && nums[i] != nums[nums[i] - 1]) {
                // 交换 nums[i] 和 nums[nums[i] - 1]
                int temp = nums[nums[i] - 1];
                nums[nums[i] - 1] = nums[i];
                nums[i] = temp;
            }
        }

        // 再次遍历，找第一个不在正确位置上的数字
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }
        return n + 1;
    }
}