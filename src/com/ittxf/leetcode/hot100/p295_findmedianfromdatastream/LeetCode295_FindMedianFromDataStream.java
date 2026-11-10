package com.ittxf.leetcode.hot100.p295_findmedianfromdatastream;

import java.util.PriorityQueue;

public class LeetCode295_FindMedianFromDataStream {
    public static void main(String[] args) {
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.addNum(1);    // arr = [1]
        medianFinder.addNum(2);    // arr = [1, 2]
        System.out.println(medianFinder.findMedian()); // 返回 1.5 ((1 + 2) / 2)
        medianFinder.addNum(3);    // arr[1, 2, 3]
        System.out.println(medianFinder.findMedian()); // return 2.0
    }
}

class MedianFinder {
    private PriorityQueue<Integer> left;
    private PriorityQueue<Integer> right;

    public MedianFinder() {
        left = new PriorityQueue<>((a, b) -> b - a);
        right = new PriorityQueue<>();
    }

    // 添加数字
    public void addNum(int num) {
        if (left.size() == right.size()) {
            right.add(num);
            left.add(right.poll());
        } else {
            left.add(num);
            right.add(left.poll());
        }
    }

    // 找中位数
    public double findMedian() {
        if (left.size() == right.size()) return (left.peek() + right.peek()) / 2.0;
        else return left.peek();
    }
}