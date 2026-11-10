package com.ittxf.leetcode.hot100.p215_kthlargestelementinanarray;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class LeetCode215_KthLargestElementInAnArray {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));
        System.out.println(solution.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4));
    }
}

class Solution {
    public int findKthLargest(int[] nums, int k) {
        // 使用优先队列（小顶堆）来解决
        // 默认是小顶堆
        Queue<Integer> heap = new PriorityQueue<>();

        for (int num : nums) {
            heap.add(num);
            // 如果堆的大小超过了 k，就把最小的踢出去
            if (heap.size() > k) heap.poll();
        }

        // 堆顶就是第 K 大的元素
        return heap.peek();

        // 邪修
        /*Arrays.sort(nums);
        return nums[nums.length - k];*/
    }
}
