package com.ittxf.leetcode.hot100.p283_movezeroes;

import java.util.Arrays;

public class LeetCode283_MoveZeroes {
    public static void main(String[] args) {
        int[] nums = new int[]{0,1,0,3,12};
        new Solution().moveZeroes(nums);
        System.out.println(Arrays.toString(nums));
    }
}

class Solution {
    public void moveZeroes(int[] nums) {
        int index = 0; // 记录非零元素应该放入的位置

        // 1. 把所有非零元素往前挪
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[index] = nums[i];
                index++;
            }
        }

        // 2. 把 index 之后的位置全部补 0
        for (int i = index; i < nums.length; i++) {
            nums[i] = 0;
        }
        /*int[] copy = new int[nums.length];
        Arrays.fill(copy, 0);
        int index = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                copy[index] = nums[i];
                index++;
            }
        }
        for (int i = 0; i < nums.length; i++) {
            nums[i] = copy[i];
        }*/
    }
}

