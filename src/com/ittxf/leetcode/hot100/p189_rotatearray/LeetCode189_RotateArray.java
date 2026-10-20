package com.ittxf.leetcode.hot100.p189_rotatearray;

import java.util.*;

public class LeetCode189_RotateArray {
    public static void main(String[] args) {
        int[] nums = {1,2,3,4,5,6,7};
        int k = 3;
        new Solution().rotate(nums, k);
        System.out.println(Arrays.toString(nums));
    }
}

class Solution {
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        // 核心大坑：如果 k 大于 n，必须取模。比如长度为 7，走 8 步等于走 1 步。
        k = k % n;

        // 1. 反转整个数组
        reverse(nums, 0, n - 1);
        // 2. 反转前 k 个元素
        reverse(nums, 0, k - 1);
        // 3. 反转剩余的 n-k 个元素
        reverse(nums, k, n - 1);
    }

    // 辅助方法：反转数组中从 start 到 end 的部分
    private void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }
}
