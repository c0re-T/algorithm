package com.ittxf.leetcode.hot100.p54_spiralmatrix;

import java.util.ArrayList;
import java.util.List;

public class LeetCode54_SpiralMatrix {
    public static void main(String[] args) {
        int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
        System.out.println(new Solution().spiralOrder(matrix));
    }
}

class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> list = new ArrayList<>();

        // 1. 初始化四个边界
        int top = 0;                            // 上边界
        int bottom = matrix.length - 1;         // 下边界
        int left = 0;                           // 左边界
        int right = matrix[0].length - 1;       // 右边界

        while (top <= bottom && left <= right) {
            // 2. 从左往右走（遍历上边界）
            for (int j = left; j <= right; j++) {
                list.add(matrix[top][j]);
            }
            top++; // 上边界往下缩

            // 3. 从上往下走（遍历右边界）
            for (int i = top; i <= bottom; i++) {
                list.add(matrix[i][right]);
            }
            right--; // 右边界往左缩

            // ⚠️ 关键：必须判断边界，防止单行/单列的情况下重复遍历
            if (top <= bottom) {
                // 4. 从右往左走（遍历下边界）
                for (int j = right; j >= left; j--) {
                    list.add(matrix[bottom][j]);
                }
                bottom--; // 下边界往上缩
            }

            if (left <= right) {
                // 5. 从下往上走（遍历左边界）
                for (int i = bottom; i >= top; i--) {
                    list.add(matrix[i][left]);
                }
                left++; // 左边界往右缩
            }
        }
        return list;
    }
}