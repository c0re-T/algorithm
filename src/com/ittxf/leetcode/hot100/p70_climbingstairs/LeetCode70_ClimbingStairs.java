package com.ittxf.leetcode.hot100.p70_climbingstairs;

public class LeetCode70_ClimbingStairs {
    public static void main(String[] args) {
        System.out.println(new Solution().climbStairs(2));
        System.out.println(new Solution().climbStairs(3));
        System.out.println(new Solution().climbStairs(4));
    }
}

class Solution {
    public int climbStairs(int n) {
        if (n <= 2) return n;
        int prev1 = 1, prev2 = 2, curr = 0;
        for (int i = 3; i <= n; i++) {
            curr = prev1 + prev2;
            // 滚动更新：为下一轮做准备
            prev1 = prev2;
            prev2 = curr;
        }
        return curr;
    }
}

