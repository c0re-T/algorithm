package com.ittxf.leetcode.hot100.p102_binarytreelevelordertraversal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LeetCode102_BinaryTreeLevelOrderTraversal {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        System.out.println("二叉树的层序遍历: " + new Solution().levelOrder(root));
    }
}

class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        // 1. 结果集
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;

        // 2. 核心：借助队列
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        // 3. 开始剥洋葱
        while (!queue.isEmpty()) {
            // 记录当前这一层有多少个节点
            int levelSize = queue.size();
            // 存放当前层的节点值
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                // 弹出队头节点，收集当前节点的值
                TreeNode node = queue.poll();
                level.add(node.val);
                // 如果有左右孩子，放进队列（这是为了下一层做准备）
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            res.add(level);
        }
        return res;
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

