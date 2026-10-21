package com.ittxf.leetcode.hot100.p94_binarytreeinordertraversal;

import java.util.ArrayList;
import java.util.List;

public class LeetCode94_BinaryTreeInorderTraversal {
    public static void main(String[] args) {
        Solution solution = new Solution();

        // 1. 测试空树
        System.out.println("空树: " + solution.inorderTraversal(null));

        // 2. 测试 LeetCode 示例树
        // 形如：
        //    1
        //     \
        //      2
        //     /
        //    3
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        root.right.left = new TreeNode(3);

        System.out.println("示例树中序遍历: " + solution.inorderTraversal(root)); // 期待输出 [1, 3, 2]

        // 3. 测试完美二叉树
        // 形如：
        //      1
        //    /   \
        //   2     3
        //  / \   / \
        // 4   5 6   7
        TreeNode fullTree = buildTree(new int[]{1, 2, 3, 4, 5, 6, 7}, 0);
        System.out.println("完美二叉树中序遍历: " + solution.inorderTraversal(fullTree)); // 期待输出 [4, 2, 5, 1, 6, 3, 7]
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
    // 保证递归调用使用同一个列表
    List<Integer> list = new ArrayList<>();
    public List<Integer> inorderTraversal(TreeNode root) {
        if (root == null) return list;
        if (root.left != null) inorderTraversal(root.left);
        list.add(root.val);
        if (root.right != null) inorderTraversal(root.right);

        return list;
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

