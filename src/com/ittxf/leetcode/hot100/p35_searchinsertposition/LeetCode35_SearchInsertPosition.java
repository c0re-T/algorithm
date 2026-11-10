package com.ittxf.leetcode.hot100.p35_searchinsertposition;

public class LeetCode35_SearchInsertPosition {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.searchInsert(new int[]{1, 3, 5, 6}, 5));
        System.out.println(solution.searchInsert(new int[]{1, 3, 5, 6}, 2));
        System.out.println(solution.searchInsert(new int[]{1, 3, 5, 6}, 7));
    }
}

class Solution {
    public int searchInsert(int[] nums, int target) {
        int lt = 0;
        int rt = nums.length - 1;

        while (lt <= rt) {
            int mid = lt + (rt - lt) / 2;
            if (nums[mid] == target) return mid;
            else if (nums[mid] < target) lt = mid + 1;
            else rt = mid - 1;
        }

        return lt;
    }
}
