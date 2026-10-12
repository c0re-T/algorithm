package com.ittxf.study.dynamic;

public class KnapsackProblem {
    public static void main(String[] args) {
        int[] weights = {1, 4, 3}; // 物品重量
        int[] values = {1500, 3000, 2000}; // 物品的价值
        int capacity = 4; // 背包容量
        int n = weights.length; // 物品数量
        int[][] dp = new int[n + 1][capacity + 1]; // 创建dp数组

        // 为了记录放入的物品情况，创建一个二维数组path
        int[][] path = new int[n + 1][capacity + 1];

        // 初始化第一行和第一列，由于默认是0，在本程序中可以不用处理
        for (int i = 1; i < dp.length; i++) { // 不处理第1行，因为第1行是0
            for (int j = 1; j < dp[i].length; j++) { // 不处理第1列，因为第1列是0
                // 当前程序是从1开始的，所以需要减1
                if (weights[i - 1] > j) { // 如果当前物品的重量大于当前背包容量，则不能放入
                    dp[i][j] = dp[i - 1][j];
                } else { // 如果当前物品的重量小于等于当前背包容量，则可以放入
                    // dp[i][j] = Math.max(dp[i - 1][j], values[i - 1] + dp[i - 1][j - weights[i - 1]]);
                    // 为了记录商品存放到背包的情况，我们不能简单的使用上面的公式，需要使用if-else来处理
                    if (dp[i - 1][j] > values[i - 1] + dp[i - 1][j - weights[i - 1]]) {
                        dp[i][j] = dp[i - 1][j];
                        path[i][j] = 0; // 不放入
                    } else {
                        dp[i][j] = values[i - 1] + dp[i - 1][j - weights[i - 1]];
                        path[i][j] = 1; // 放入
                    }
                }
                System.out.print(dp[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("放入的物品所有情况为：");
        for (int i = 0; i < path.length; i++) {
            for (int j = 0; j < path[i].length; j++) {
                System.out.print(path[i][j] + " ");
            }
            System.out.println();
        }

        // 根据path数组，输出最终放入的物品情况，只需要从path数组的最后一条路径开始查找即可
        // 行表示物品，列表示背包容量，值为1表示放入，值为0表示不放入
        System.out.println("最终放入的物品情况为：");
        for (int i = n, j = capacity; i > 0 && j > 0; i--) { // 从后往前查找
            if (path[i][j] == 1) {
                System.out.println("放入物品：" + i + "，重量：" + weights[i - 1] + "，价值：" + values[i - 1]);
                j -= weights[i - 1]; // 减去放入物品的重量
            }
        }

        System.out.println("最大价值为：" + dp[n][capacity]);
    }
}
