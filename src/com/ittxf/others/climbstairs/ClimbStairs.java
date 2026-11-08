package com.ittxf.others.climbstairs;

public class ClimbStairs {
    public static void main(String[] args) {
        int n = 10;
        int[] bad = {3, 7}; // 3、7 阶不喜欢，不能踩
        System.out.println(new Solution().climb(n, bad));
    }
}

class Solution {
    private static final int MOD = 1000000007;
    public int climb(int n, int[] bad) {
        // 数组长度 n + 1，下标 1~n 对应台阶 1~n
        boolean[] isBad = new boolean[n + 1];

        for (int b : bad) if (b >= 1 && b <= n) isBad[b] = true;

        int[] dp = new int[n + 1]; // dp[i] 表示到达第 i 阶台阶的方法数
        dp[0] = 1; // 初始条件，有一种方法到达第 0 阶台阶

        for (int i = 1; i <= n; i++) {
            if (isBad[i]) { // 如果当前台阶是坏台阶，不能踩
                dp[i] = 0;
                continue;
            }
            for (int k = 1; k <= 4; k++) { // 可以上来的方式有 1、2、3、4 阶
                if (i - k >= 0) dp[i] = (dp[i] + dp[i - k]) % MOD;
                else break;
            }
        }
        return dp[n];
    }
}
