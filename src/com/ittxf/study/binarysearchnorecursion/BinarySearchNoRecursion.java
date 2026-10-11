package com.ittxf.study.binarysearchnorecursion;

public class BinarySearchNoRecursion {
    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int target = 7;
        int result = binarySearch(array, target);
        System.out.println("目标值 " + target + " 的索引为 " + result);
    }

    /**
     * 二分查找非递归实现
     * @param array 待查找的数组
     * @param target 目标值
     * @return 目标值的索引，未找到返回-1
     */
    public static int binarySearch(int[] array, int target) {
        int left = 0; // 左边界
        int right = array.length - 1; // 右边界
        int mid;
        while (left <= right) {
            mid = (left + right) / 2;
            if (array[mid] == target) return mid; // 找到目标值，返回索引
            else if (array[mid] > target) right = mid - 1; // 目标值在左半部分
            else left = mid + 1; // 目标值在右半部分
        }
        return -1; // 未找到目标值
    }
}
