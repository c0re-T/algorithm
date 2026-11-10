package com.ittxf.leetcode.hot100.p153_findminimuminrotatedsortedarray;

public class LeetCode153_FindMinimumInRotatedSortedArray {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.findMin(new int[]{3, 4, 5, 1, 2}));
        System.out.println(solution.findMin(new int[]{4, 5, 6, 7, 0, 1, 2}));
        System.out.println(solution.findMin(new int[]{11, 13, 15, 17}));
    }
}

class Solution {
    public int findMin(int[] nums) {
        int lt = 0;
        int rt = nums.length - 1;
        while (lt < rt) {
            int mid = lt + (rt - lt) / 2;
            if (nums[mid] < nums[rt]) rt = mid;
            else lt = mid + 1;
        }
        return nums[lt];
    }
}
