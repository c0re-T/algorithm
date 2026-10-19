package com.ittxf.leetcode.hot100.p11_containerwithmostwater;

public class LeetCode11_ContainerWithMostWater {
    public static void main(String[] args) {
        int[] height = new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println(new Solution().maxArea(height));
    }
}

class Solution {
    public int maxArea(int[] height) {
        int maxArea = 0;
        int area = 0;
        int left = 0;
        int right = height.length - 1;
        while (left < right) {
            area = Math.min(height[left], height[right]) * (right - left);
            maxArea = Math.max(maxArea, area);
            if (height[left] < height[right]) left++;
            else right--;
        }
        return maxArea;
    }
}
