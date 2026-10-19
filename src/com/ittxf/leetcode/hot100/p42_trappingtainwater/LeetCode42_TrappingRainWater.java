package com.ittxf.leetcode.hot100.p42_trappingtainwater;

public class LeetCode42_TrappingRainWater {
    public static void main(String[] args) {
        int[] height = new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        System.out.println(new Solution().trap(height));
    }
}

class Solution {
    public int trap(int[] height) {
        if (height == null || height.length <= 2) return 0;
        int left = 0;
        int right = height.length - 1;
        int leftMax = 0; // 左边遍历过的最高柱子
        int rightMax = 0; // 右边遍历过的最高柱子
        int water = 0;    // 总接水量

        while (left < right) {
            // 每次更新左右两边的最高柱子
            leftMax = Math.max(leftMax, height[left]);
            rightMax = Math.max(rightMax, height[right]);

            if (leftMax < rightMax) {
                // 左边的柱子更低，说明左边柱子的接水量由 leftMax 决定
                water += leftMax - height[left];
                left++;
            } else {
                // 右边的柱子更低或相等，说明右边柱子的接水量由 rightMax 决定
                water += rightMax - height[right];
                right--;
            }
        }
        return water;
    }
}
