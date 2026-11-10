package com.ittxf.leetcode.hot100.p74_searcha2Dmatrix;

import java.util.Arrays;

public class LeetCode74_SearchA2DMatrix {
    public static void main(String[] args) {
        System.out.println(new Solution().searchMatrix(new int[][]{{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}}, 3));
        System.out.println(new Solution().searchMatrix(new int[][]{{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}}, 13));
    }
}

class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;

        int lt = 0;
        int rt = m * n - 1;
        while (lt <= rt) {
            int mid = lt + (rt - lt) / 2;
            if (matrix[mid / n][mid % n] == target) return true;
            else if (matrix[mid / n][mid % n] < target) lt = mid + 1;
            else rt = mid - 1;
        }
        return false;

        /*int[] flatMatrix = Arrays.stream(matrix)
                        .flatMapToInt(Arrays::stream)
                        .toArray();

        int lt = 0;
        int rt = flatMatrix.length - 1;

        while (lt < rt) {
            int mid = (lt + rt) / 2;
            if (flatMatrix[mid] == target) return true;
            else if (flatMatrix[mid] < target) lt = mid + 1;
            else rt = mid - 1;
        }
        return flatMatrix[lt] == target;*/
    }
}

