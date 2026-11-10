package com.ittxf.leetcode.hot100.p739_dailytemperatures;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class LeetCode739_DailyTemperatures {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(new Solution().dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73})));
        System.out.println(Arrays.toString(new Solution().dailyTemperatures(new int[]{30, 40, 50, 60})));
        System.out.println(Arrays.toString(new Solution().dailyTemperatures(new int[]{30, 60, 90})));
    }
}

class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] res = new int[n];
        Deque<Integer> stack = new ArrayDeque<>(); // 存下标

        for (int i = 0; i < n; i++) {
            // 如果栈不为空，且当前温度 > 栈顶对应的温度
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int preIndex = stack.pop(); // 弹出栈顶
                res[preIndex] = i - preIndex; // 计算天数差
            }
            stack.push(i);
        }
        return res;


        // 暴力超时
        /*int[] res = new int[temperatures.length];
        for (int i = 0; i < temperatures.length - 1; i++) {
            for (int j = i + 1; j < temperatures.length; j++) {
                if (temperatures[j] > temperatures[i]) {
                    res[i] = j - i;
                    break;
                } else if (j == temperatures.length - 1) {
                    res[i] = 0;
                }
            }
        }
        return res;*/
    }
}
