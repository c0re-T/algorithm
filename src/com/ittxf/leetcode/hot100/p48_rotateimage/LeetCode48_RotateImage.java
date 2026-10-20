package com.ittxf.leetcode.hot100.p48_rotateimage;

import java.util.Arrays;

public class LeetCode48_RotateImage {
    public static void main(String[] args) {
        int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
        new Solution().rotate(matrix);
        System.out.println(Arrays.deepToString(matrix));
    }
}

class Solution {
    public void rotate(int[][] matrix) {
        int n = matrix.length;

        // 转置矩阵,沿着对角线翻转
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        // 反转每一行
        for (int[] row : matrix) {
            for (int j = 0; j < row.length / 2; j++) {
                int temp = row[j];
                row[j] = row[row.length - 1 - j];
                row[row.length - 1 - j] = temp;
            }
        }
    }
}