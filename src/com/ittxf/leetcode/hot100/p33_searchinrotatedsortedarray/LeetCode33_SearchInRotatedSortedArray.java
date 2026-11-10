package com.ittxf.leetcode.hot100.p33_searchinrotatedsortedarray;

public class LeetCode33_SearchInRotatedSortedArray {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));
        System.out.println(solution.search(new int[]{6, 7, 8, 0, 1, 2, 3, 4, 5}, 2));
    }
}

class Solution {
    public int search(int[] nums, int target) {
        int lt = 0;
        int rt = nums.length - 1;

        while (lt <= rt) {
            int mid = lt + (rt - lt) / 2;

            // 找到了，直接返回
            if (nums[mid] == target) return mid;

            // 1. 判断左半边是否有序 (注意这里要带等号，因为存在 lt == mid 的情况)
            if (nums[lt] <= nums[mid]) {
                // 判断 target 是否在这个有序的区间 [lt, mid) 内
                if (nums[lt] <= target && target < nums[mid]) rt = mid - 1; // 在左半边，去左边找
                else lt = mid + 1; // 不在左半边，去右边找
            } else { // 2. 否则，右半边一定是有序的
                // 判断 target 是否在这个有序的区间 (mid, rt] 内
                if (nums[mid] < target && target <= nums[rt]) lt = mid + 1; // 在右半边，去右边找
                else rt = mid - 1; // 不在右半边，去左边找
            }
        }
        return -1;
    }
}

