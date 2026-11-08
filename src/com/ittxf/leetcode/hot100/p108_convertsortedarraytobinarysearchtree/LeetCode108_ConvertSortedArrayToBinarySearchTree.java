package com.ittxf.leetcode.hot100.p108_convertsortedarraytobinarysearchtree;

public class LeetCode108_ConvertSortedArrayToBinarySearchTree {
    public static void main(String[] args) {
        int[] nums = {-10,-3,0,5,9};
        System.out.println("排序数组转换为二叉搜索树: " + new Solution().sortedArrayToBST(nums));
    }
}

class Solution {
    public TreeNode sortedArrayToBST(int[] nums) {
        if (nums == null || nums.length == 0) return null;
        return helper(nums, 0, nums.length - 1);
    }

    private TreeNode helper(int[] nums, int left, int right) {
        if (left > right) return null;
        int mid = (left + right) / 2;
        TreeNode root = new TreeNode(nums[mid]);
        root.left = helper(nums, left, mid - 1);
        root.right = helper(nums, mid + 1, right);
        return root;
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

