package com.ittxf.leetcode.hot100.p155_minstack;

import java.util.ArrayDeque;
import java.util.Deque;

public class LeetCode155_MinStack {
    public static void main(String[] args) {
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println(minStack.getMin()); // 输出: -3
        minStack.pop();
        System.out.println(minStack.top());    // 输出: 0
        System.out.println(minStack.getMin()); // 输出: -2
    }
}

class MinStack {
    // 主栈：存所有数据
    private Deque<Integer> stack;
    // 辅助栈：存历史最小值
    private Deque<Integer> minStack;

    public MinStack() {
        stack = new ArrayDeque<>();
        minStack = new ArrayDeque<>();
    }

    public void push(int value) {
        stack.push(value);
        if (minStack.isEmpty() || value <= minStack.peek()) minStack.push(value);
    }

    public void pop() {
        if (!stack.isEmpty()) {
            int value = stack.pop();
            if (value == minStack.peek()) minStack.pop();
        }
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }
}
