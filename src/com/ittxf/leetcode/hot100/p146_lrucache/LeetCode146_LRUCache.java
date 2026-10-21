package com.ittxf.leetcode.hot100.p146_lrucache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LeetCode146_LRUCache {
    public static void main(String[] args) {

    }
}

class LRUCache extends LinkedHashMap<Integer, Integer> {

    private int capacity;

    public LRUCache(int capacity) {
        // 参数 1：初始容量；参数 2：负载因子；参数 3：true 表示按访问顺序排序（即 LRU）
        super(capacity, 0.75F, true);
        this.capacity = capacity;
    }

    public int get(int key) {
        return super.getOrDefault(key, -1);
    }

    public void put(int key, int value) {
        super.put(key, value);
    }

    // 核心：当 put 元素时触发此方法，如果超过容量，返回 true 就会删除最久未使用的元素
    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
        return size() > capacity;
    }
}

