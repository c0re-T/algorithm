package com.ittxf.leetcode.hot100.p51_nqueens;

import java.util.ArrayList;
import java.util.List;

public class LeetCode51_NQueens {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.solveNQueens(4));
    }
}

class Solution {
    public List<List<String>> solveNQueens(int n) {
        ArrayList<List<String>> res = new ArrayList<>();
        // queens[i] 表示第 i 行的皇后放在了第 queens[i] 列
        int[] queens = new int[n];
        backtrack(res, queens, n,  0);
        return res;
    }

    // row 表示当前正在尝试放置第几行的皇后
    private void backtrack(ArrayList<List<String>> res, int[] queens, int n, int row) {
        // 1. 触底条件：n 行都放完了，说明找到了一个解
        if (row == n) {
            res.add(buildBoard(queens, n));
            return;
        }

        // 2. 在当前 row 行，尝试每一列 col
        for (int col = 0; col < n; col++) {
            if (isValid(queens, row, col)) {
                queens[row] = col;
                backtrack(res, queens, n, row + 1);
            }
        }
    }

    // 核心校验逻辑
    private boolean isValid(int[] queens, int row, int col) {
        // 检查当前行（row）和前面所有的行（i）是否有冲突
        for (int i = 0; i < row; i++) {
            // 冲突条件1：同列
            // 冲突条件2：同一对角线（行差绝对值 == 列差绝对值）
            if (queens[i] == col || Math.abs(row - i) == Math.abs(col - queens[i])) return false;
        }
        return true;
    }

    // 将一维数组转换为要求的 List<String> 棋盘形式
    private List<String> buildBoard(int[] queens, int n) {
        ArrayList<String> board = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            char[] row = new char[n];
            for (int j = 0; j < n; j++) {
                row[j] = queens[i] == j ? 'Q' : '.';
            }
            board.add(String.valueOf(row));
        }
        return board;
    }
}
