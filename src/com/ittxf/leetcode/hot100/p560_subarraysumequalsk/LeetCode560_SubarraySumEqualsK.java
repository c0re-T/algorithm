package com.ittxf.leetcode.hot100.p560_subarraysumequalsk;

import java.util.HashMap;
import java.util.Map;

public class LeetCode560_SubarraySumEqualsK {
    public static void main(String[] args) {
        int[] nums = {1,2,3};
        int k = 3;
        System.out.println(new Solution().subarraySum(nums, k));
    }
}

class Solution {
    public int subarraySum(int[] nums, int k) {
        // key: 前缀和的值, value: 这个前缀和出现的次数
        Map<Integer, Integer> preSumMap = new HashMap<>();
        // 初始化：前缀和为 0 的情况出现了 1 次（为了处理从下标 0 开始的子数组和刚好为 k 的情况）
        preSumMap.put(0, 1);

        int count = 0; // 记录结果
        int preSum = 0; // 当前的前缀和

        for (int num : nums) {
            preSum += num; // 累加当前的前缀和

            // 核心逻辑：如果哈希表里存在 preSum - k
            // 说明之前有某段前缀和等于 preSum - k，那么这两者之间的差值（子数组）和就是 k
            if (preSumMap.containsKey(preSum - k)) {
                count += preSumMap.get(preSum - k); // 加上出现的次数
            }

            // 把当前的前缀和存入哈希表，出现次数 +1
            // 如果拿到了，就返回它的值；如果没拿到（键不存在），就返回给定的“默认值”
            preSumMap.put(preSum, preSumMap.getOrDefault(preSum, 0) + 1);
        }

        return count;

        /*int count = 0;
        for (int i = 0; i < nums.length; i++) {
            int sum = 0;
            for (int j = i; j < nums.length; j++) {
                sum += nums[j];
                if (sum == k) {
                    count++;
                }
            }
        }
        return count;*/
    }
}
