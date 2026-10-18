package com.ittxf.leetcode.hot100.leetcode1_twosum;

import java.util.Arrays;
import java.util.HashMap;

public class LeetCode1_TwoSum {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] ints = solution.twoSum(new int[]{2, 7, 11, 15}, 9);// 调用 twoSum 方法
        System.out.println(Arrays.toString(ints));
    }
}

class Solution {
    public int[] twoSum(int[] nums, int target) {
        // 创建一个哈希表，key 存数组的值，value 存数组的下标
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i]; // 计算需要的差值

            if (map.containsKey(complement)) { // 如果哈希表中包含需要的差值
                return new int[] { map.get(complement), i };
            }

            map.put(nums[i], i); // 将当前数组的值和下标存入哈希表
        }
        return new int[0]; // 返回一个长度为 0 的数组，表示没有找到符合条件的两个数
        /*int res[] = new int[2];
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    res[0] = i;
                    res[1] = j;
                }
            }
        }
        return res;*/
    }
}
