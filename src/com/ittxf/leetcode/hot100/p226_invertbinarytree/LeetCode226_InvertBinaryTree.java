package com.ittxf.leetcode.hot100.p226_invertbinarytree;

public class LeetCode226_InvertBinaryTree {
    public static void main(String[] args) {
        Solution solution = new Solution();

        // 1. 测试空树
        System.out.println("空树: " + solution.invertTree(null));

        // 2. 测试 LeetCode 示例树
        // 形如：
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        root.right.left = new TreeNode(3);

        System.out.println("翻转后的树: " + solution.invertTree(root));

        // 3. 测试完全二叉树
        TreeNode fullTree = buildTree(new int[]{1, 2, 3, 4, 5, 6, 7}, 0);
        System.out.println("翻转后的完全二叉树: " + solution.invertTree(fullTree));
    }

    /**
     * 辅助方法：根据层序遍历数组，构建一棵二叉树
     * @param arr 节点值数组（可以用 -1 或 null 表示空节点，这里为了简单，默认都是满的）
     * @param index 当前节点的索引
     */
    private static TreeNode buildTree(int[] arr, int index) {
        if (index >= arr.length) {
            return null;
        }
        TreeNode node = new TreeNode(arr[index]);
        // 完全二叉树的性质：左孩子索引 = 2 * index + 1，右孩子索引 = 2 * index + 2
        node.left = buildTree(arr, 2 * index + 1);
        node.right = buildTree(arr, 2 * index + 2);
        return node;
    }
}

class Solution {
    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        root.left = right;
        root.right = left;
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