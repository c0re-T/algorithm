package com.ittxf.leetcode.hot100.p34_findfirstandlastpositionofelementinsortedarray;

import java.util.Arrays;

public class LeetCode34_FindFirstAndLastPositionOfElementInSortedArray {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution.searchRange(new int[]{5, 7, 7, 8, 8, 10}, 8)));
    }
}

class Solution {
    public int[] searchRange(int[] nums, int target) {
        int left = searchLeft(nums, target);
        int right = searchRight(nums, target);

        // 如果左边界都没找到，说明数组里没这个数，直接返回 [-1, -1]
        if (left == -1) return new int[]{-1, -1};

        return new int[]{left, right};

    }

    // 寻找左边界
    private int searchLeft(int[] nums, int target) {
        int lt = 0;
        int rt = nums.length - 1;
        int leftBound = -1; // 记录左边界

        while (lt <= rt) {
            int mid = lt + (rt - lt) / 2;
            if (nums[mid] == target) {
                leftBound = mid; // 记录当前找到的符合条件的位置
                rt = mid - 1;    // 不要停！继续向左寻找有没有更早的
            } else if (nums[mid] < target) lt = mid + 1;
            else rt = mid - 1;
        }
        return leftBound;
    }

    // 寻找右边界
    private int searchRight(int[] nums, int target) {
        int lt = 0;
        int rt = nums.length - 1;
        int rightBound = -1; // 记录右边界

        while (lt <= rt) {
            int mid = lt + (rt - lt) / 2;
            if (nums[mid] == target) {
                rightBound = mid; // 记录当前找到的符合条件的位置
                lt = mid + 1;     // 不要停！继续向右寻找有没有更晚的
            } else if (nums[mid] < target) lt = mid + 1;
            else rt = mid - 1;
        }
        return rightBound;
    }
}


