package com.ittxf.leetcode.hot100.p279_perfectsquares;

public class LeetCode279_PerfectSquares {
    public static void main(String[] args) {
        System.out.println(new Solution().numSquares(2));
        System.out.println(new Solution().numSquares(4));
        System.out.println(new Solution().numSquares(12));
        System.out.println(new Solution().numSquares(13));
    }
}

class Solution {
    public int numSquares(int n) {
        // 纯数学定理
        // 任何正整数都可以表示为不超过 4 个整数的平方和。
        // 1. 判断是否为 4 的情况
        while (n % 4 == 0) {
            n /= 4;
        }
        if (n % 8 == 7) return 4;

        // 2. 判断是否为 1 或 2 的情况
        // 如果是完全平方数，返回 1
        if (isPerfectSquare(n)) return 1;
        // 枚举拆成两个平方数的情况
        for (int i = 1; i * i <= n; i++) {
            if (isPerfectSquare(n - i * i)) return 2;
        }

        // 3. 剩下的全是 3
        return 3;


       // 动态规划
       /* // dp[i] 表示凑成和为 i 的最少完全平方数个数
        int[] dp = new int[n + 1];

        // 初始化：最坏的情况是全部用 1 组成，即 i 个 1
        for (int i = 1; i <= n; i++) {
            dp[i] = i;
            // 枚举所有小于等于 i 的完全平方数
            for (int j = 1; j * j <= i; j++) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }
        return dp[n];*/
    }

    private boolean isPerfectSquare(int x) {
        int sqrt = (int) Math.sqrt(x);
        return sqrt * sqrt == x;
    }
}
