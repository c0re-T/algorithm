package com.ittxf.leetcode.hot100.p49_3sum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode49_3Sum {
    public static void main(String[] args) {
        int[] nums = new int[]{-1, 0, 1, 2, -1, -4};
        System.out.println(new Solution().threeSum(nums));
    }
}

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) return result;

        // 1. 核心第一步：排序（这是能用双指针且能去重的前提）
        Arrays.sort(nums);
        System.out.println(Arrays.toString(nums));

        // 2. 第一层循环：固定第一个数
        for (int i = 0; i < nums.length - 2; i++) {
            // 剪枝优化：如果第一个数已经大于0了，后面的数肯定更大，相加不可能等于0
            if (nums[i] > 0) break;

            // 跳过重复的第一个数
            if (i > 0 && nums[i] == nums[i - 1]) continue;

            // 3. 双指针寻找另外两个数
            int left = i + 1;
            int right = nums.length - 1;

            // 4. 双指针循环，类似水桶原理，左指针从左向右，右指针从右向左
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];

                if (sum == 0) {
                    // 找到了！直接 new 一个 List，不需要在外面定义和清空
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));

                    // 移动双指针，并跳过重复的元素
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;

                    left++;
                    right--;
                } else if (sum < 0) {
                    // 和太小了，左指针往右移
                    left++;
                } else {
                    // 和太大了，右指针往左移
                    right--;
                }
            }
        }
        return result;


        // 错误的会重复
        /*System.out.println(Arrays.toString(nums));
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                for (int k = j + 1; k < nums.length; k++) {
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        list.add(nums[i]);
                        list.add(nums[j]);
                        list.add(nums[k]);
                        result.add(new ArrayList<>(list));
                        list.clear();
                    }
                }
            }
        }
        return result;*/
    }
}