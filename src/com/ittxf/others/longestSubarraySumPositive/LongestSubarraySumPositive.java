package com.ittxf.others.longestSubarraySumPositive;

import java.util.HashMap;
import java.util.Map;

public class LongestSubarraySumPositive {
    public static void main(String[] args) {
        int[] nums = {-1, -1, -1, 1, 1};
        int res = new Solution().longestSubarray(nums);
        System.out.println(res);

    }
}

class Solution {
    public int longestSubarray(int[] nums) {
        Map<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(0, 0); // 初始化：前缀和为 0 的情况出现在下标 0 处
        int preSum = 0; // 当前的前缀和
        int maxLen = 0;
        for (int i = 1; i <= nums.length; i++) {
            preSum += nums[i - 1];
            // 遍历哈希表，更新最大长度
            for (Map.Entry<Integer, Integer> entry : hashMap.entrySet()) {
                if (preSum > entry.getKey()) { // 前缀和差大于 0 表示子数组和为正 preSum - entry.getKey() > 0
                    maxLen = Math.max(maxLen, i - entry.getValue());
                }
            }
            // 只记录第一次出现的下标
            if (!hashMap.containsKey(preSum)) {
                hashMap.put(preSum, i);
            }

        }
        return maxLen;
    }
}