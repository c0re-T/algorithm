package com.ittxf.leetcode.hot100.p128_longestconsecutivesequence;

import java.util.HashSet;
import java.util.Set;

public class LeetCode128_LongestConsecutiveSequence {
    public static void main(String[] args) {
        int[] nums = {0,3,7,2,5,8,4,6,0,1};
        System.out.println(new Solution().longestConsecutive(nums));
    }
}

class Solution {
    public int longestConsecutive(int[] nums) {
        // 1. 将数组元素存入 HashSet，自动去重，且查找时间复杂度为 O(1)
        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }
        System.out.println(numSet);
        // 用于记录最长连续序列的长度
        int longestStreak = 0;

        // 2. 遍历集合中的每个数字
        for (int num : numSet) {
            // 3. 核心优化：只有当 num 是连续序列的起点时（即 num-1 不存在），才往下找
            // 这样保证了每个数字最多只会被访问两次（一次在外层遍历，一次在内层 while 遍历）
            if (!numSet.contains(num - 1)) {
                int currentNum = num;
                int currentStreak = 1;

                // 4. 往后找连续的数字
                while (numSet.contains(currentNum + 1)) {
                    currentNum++;
                    currentStreak++;
                }

                // 5. 更新最大长度
                longestStreak = Math.max(longestStreak, currentStreak);
            }
        }

        return longestStreak;
        /*if (nums.length == 0) return 0;
        Arrays.sort(nums);
        System.out.println(Arrays.toString(nums));
        int count = 1, maxCount = 1;
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i + 1] == nums[i]) continue;
            if (nums[i + 1] - nums[i] == 1) count++;
            else {
                if (nums[i + 1] == nums[i]) continue;
                maxCount = Math.max(maxCount, count);
                count = 1;
            }
        }
        maxCount = Math.max(maxCount, count);
        return maxCount;*/
    }
}