package com.ittxf.leetcode.hot100.p4_medianoftwosortedarrays;

import java.util.Arrays;
import java.util.stream.IntStream;

public class LeetCode4_MedianOfTwoSortedArrays {
    public static void main(String[] args) {
        int[] nums1 = {1, 2};
        int[] nums2 = {3, 4};
        Solution solution = new Solution();
        double medianSortedArrays = solution.findMedianSortedArrays(nums1, nums2);
        System.out.println(medianSortedArrays);
    }
}

/**
 * 我们不需要真的把两个数组合并成一个新数组，
 * 我们只需要用两个指针，像拉链一样，把两个数组从小到大“虚拟合并”一遍，
 * 只走到一半（中间位置）就停下，顺手拿到中间的数。
 */
class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int mn = m + n;
        int left = -1, right = -1; // 记录中间的两个数
        int aStart = 0, bStart = 0; // 记录当前遍历到的位置

        // 只需要遍历到一半 (len / 2) 即可
        for (int i = 0; i <= mn / 2; i++) {
            left = right; // left 保存上一次的结果

            // 核心判断：谁小就把谁选出来
            // 如果 a 数组还没遍历完，并且 (b 遍历完了 或者 a当前元素 <= b当前元素)
            if (aStart < m && (bStart >= n || nums1[aStart] <= nums2[bStart])) {
                right = nums1[aStart++];
            } else right = nums2[bStart++];
        }

        if ((mn & 1) == 0) return (left + right) / 2.0;
        else return right;

        // 时间复杂度不够log(m+n)自己写的
        /*// 合并两个数组
        int[] nums = IntStream.concat(Arrays.stream(nums1), Arrays.stream(nums2))
                .toArray();
        Arrays.sort(nums);
        int mn = nums.length;

        if (mn % 2 == 0) return (nums[mn / 2 - 1] + nums[mn / 2]) / 2.0;
        else return nums[mn / 2];*/
    }
}
