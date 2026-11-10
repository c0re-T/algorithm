package com.ittxf.leetcode.hot100.p84_largestrectangleinhistogram;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class LeetCode84_LargestRectangleInHistogram {
    public static void main(String[] args) {
        System.out.println(new Solution().largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}));
        System.out.println(new Solution().largestRectangleArea(new int[]{2, 4}));
    }
}

class Solution {
    public int largestRectangleArea(int[] heights) {
        int n = heights.length;
        // 1. 创建带有末尾哨兵 0 的新数组
        // int[] newHeights = new int[n + 1];
        // System.arraycopy(heights, 0, newHeights, 0, n);
        Deque<Integer> stack = new ArrayDeque<>();

        int max = 0; // 最大面积不可能为负数，初始化为 0 即可
        for (int i = 0; i < n + 1; i++) {
            int currentHeight = (i == n) ? 0 : heights[i];
            // 2. 注意这里改成了 newHeights[i]
            // 当当前柱子高度小于栈顶柱子高度时，说明栈顶柱子的右边界确定了，可以计算面积了
            while (!stack.isEmpty() && currentHeight < heights[stack.peek()]) {
                // 弹出栈顶，作为当前要计算的矩形高度
                int h = heights[stack.pop()];
                // 计算宽度：如果栈空了，说明左边没有比它矮的，宽度就是 i；否则是 i - 左边界 - 1
                int w = stack.isEmpty() ? i : i - stack.peek() - 1;
                max = Math.max(max, h * w);
            }
            stack.push(i);
        }
        return max;
    }
}
