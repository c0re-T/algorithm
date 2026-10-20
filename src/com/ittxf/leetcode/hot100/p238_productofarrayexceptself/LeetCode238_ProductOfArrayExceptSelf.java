package com.ittxf.leetcode.hot100.p238_productofarrayexceptself;

import java.util.Arrays;

public class LeetCode238_ProductOfArrayExceptSelf {
    public static void main(String[] args) {
        int[] nums = {1,2,3,4};
        System.out.println(Arrays.toString(new Solution().productExceptSelf(nums)));
    }
}

class Solution {
    public int[] productExceptSelf(int[] nums) {
        int[] result = new int[nums.length];

        // i 左侧所有数的乘积 × i 右侧所有数的乘积
        // 从左往右遍历，计算“前缀积”
        int leftProduct = 1;
        for (int i = 0; i < nums.length; i++) {
            result[i] = leftProduct; // 1 1 2 6
            leftProduct *= nums[i];
        }
        // 从右往左遍历，乘以“后缀积”
        int rightProduct = 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            result[i] *= rightProduct; // 6 8 12 24
            rightProduct *= nums[i];
        }
        return result;

        // 暴力但是超时
        /*int[] result = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            result[i] = 1;
            for (int j = 0; j < nums.length; j++) {
                if (i != j) result[i] *= nums[j];
            }
        }
        return result;*/
    }
}
