package com.ittxf.leetcode.hot100.p347_topkfrequentelements;

import java.util.*;

public class LeetCode347_TopKFrequentElements {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(new Solution().topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2)));
        System.out.println(Arrays.toString(new Solution().topKFrequent(new int[]{1}, 1)));
        System.out.println(Arrays.toString(new Solution().topKFrequent(new int[]{1, 2, 1, 2, 1, 2, 3, 1, 3, 2}, 2)));
    }
}

class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        // 使用字典，统计每个元素出现的次数，元素为键，元素出现的次数为值
        Map<Integer, Integer> hashMap = new HashMap<>();
        for (int num : nums) {
            hashMap.put(num, hashMap.getOrDefault(num, 0) + 1);
        }
        // 遍历map，用最小堆保存频率最大的k个元素
        Queue<Integer> heap = new PriorityQueue<>(Comparator.comparingInt(hashMap::get));
        for (int num : hashMap.keySet()) {
            heap.add(num);
            if (heap.size() > k) heap.poll();
        }
        return heap.stream().mapToInt(Integer::intValue).toArray();
    }
}
