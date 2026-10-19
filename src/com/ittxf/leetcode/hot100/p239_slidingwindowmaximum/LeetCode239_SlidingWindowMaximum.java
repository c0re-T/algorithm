package com.ittxf.leetcode.hot100.p239_slidingwindowmaximum;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class LeetCode239_SlidingWindowMaximum {
    public static void main(String[] args) {
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        System.out.println(Arrays.toString(new Solution().maxSlidingWindow(nums, k)));
    }
}

class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        if (n == 0 || k == 0) return new int[0];

        int[] result = new int[n - k + 1];
        // 使用双端队列，存的是数组的下标
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            // 1. 判断队头元素是否已经滑出窗口（下标 <= i - k）
            if (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }

            // 2. 保持队列单调递减：如果新来的数比队尾大，把队尾踢出去
            // 注意：这里一定要用 <=，把相等的也踢出去，避免冗余
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }

            // 3. 将当前元素的下标加入队尾
            deque.offerLast(i);

            // 4. 窗口形成后，开始记录结果（队头就是当前窗口的最大值）
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        return result;

        // 案例超时
        /*int[] result = new int[nums.length - k + 1];

        int left = 0;

        for (int right = 0; right < result.length; right++) {
            int max = nums[left];
            for (int i = 0; i < k; i++) {
                if (nums[left + i] > max) max = nums[left + i];
            }
            result[left] = max;
            left++;
        }
        return result;*/
    }
}
