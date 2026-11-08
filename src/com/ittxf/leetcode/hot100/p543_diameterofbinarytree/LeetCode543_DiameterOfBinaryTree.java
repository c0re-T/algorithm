package com.ittxf.leetcode.hot100.p543_diameterofbinarytree;

public class LeetCode543_DiameterOfBinaryTree {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        System.out.println("二叉树的直径: " + new Solution().diameterOfBinaryTree(root));
    }
}

class Solution {
    // 1. 定义一个全局变量，用来记录遍历过程中出现过的最大直径
    private int maxDiameter = 0;
    public int diameterOfBinaryTree(TreeNode root) {
        if (root == null) return 0;
        getDepth(root);
        return maxDiameter;
    }

    // 2. 辅助函数：计算以 node 为根的树的最大深度
    private int getDepth(TreeNode node) {
        if (node == null) return 0;

        // 递归计算左右子树的深度
        int leftDepth = getDepth(node.left);
        int rightDepth = getDepth(node.right);

        // 核心动作：在计算深度的同时，计算经过当前节点的路径长度
        // 路径长度 = 左深度 + 右深度
        maxDiameter = Math.max(maxDiameter, leftDepth + rightDepth);

        // 返回当前节点为根的子树的最大深度给上一层
        return 1 + Math.max(leftDepth, rightDepth);
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
