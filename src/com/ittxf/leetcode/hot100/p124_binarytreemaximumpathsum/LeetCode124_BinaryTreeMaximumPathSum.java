package com.ittxf.leetcode.hot100.p124_binarytreemaximumpathsum;

public class LeetCode124_BinaryTreeMaximumPathSum {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(-10);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);

        System.out.println("最大路径和：" + new Solution().maxPathSum(root)); // 输出 42
    }
}

class Solution {
    // 定义一个全局变量，用来记录所有节点中，最大的那条路径和
    // 为什么初始化为 Integer.MIN_VALUE？因为节点值可能是负数，初始化为 0 会导致错误
    private int maxSum = Integer.MIN_VALUE;
    public int maxPathSum(TreeNode root) {
        maxGain(root);
        return maxSum;
    }

    // 返回以当前节点为端点的"单边"最大路径和（只能选左或者选右）
    private int maxGain(TreeNode node) {
        if (node == null) return 0;

        // 递归计算左右子节点的最大贡献值
        // 只有在最大贡献值大于 0 时，才会选取对应子节点（如果是负数，不如不选，直接当做 0）
        int leftGain = Math.max(maxGain(node.left), 0);
        int rightGain = Math.max(maxGain(node.right), 0);

        // 节点的最大路径和取决于该节点的值与该节点的左右子节点的最大贡献值
        // 这里是在计算：如果"以当前节点为拐点"，路径和是多少
        int priceNewPath = node.val + leftGain + rightGain;

        // 更新全局最大路径和
        maxSum = Math.max(maxSum, priceNewPath);

        // 返回节点的最大贡献值给它的父节点
        // 注意：这里只能选左边或右边其中一条，因为不能分叉
        return node.val + Math.max(leftGain, rightGain);
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
