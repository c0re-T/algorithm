package com.ittxf.leetcode.hot100.p114_flattenbinarytreetolinkedlist;

import java.util.ArrayList;
import java.util.List;

public class LeetCode117_FlattenBinaryTreeToLinkedList {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(3);
        root.left.right = new TreeNode(4);
        root.right.right = new TreeNode(6);
        new Solution().flatten(root);
        System.out.println("Flattened tree:");
        while (root != null) {
            System.out.print(root.val + " ");
            System.out.print("null ");
            root = root.right;
        }
    }
}

class Solution {
    public void flatten(TreeNode root) {
        TreeNode curr = root;
        while (curr != null) {
            if (curr.left != null) {
                // 1. 找到左子树的最右节点（前驱节点）
                TreeNode next = curr.left;
                while (next.right != null) {
                    next = next.right;
                }
                // 2. 把当前节点的右子树，接到前驱节点的右边
                next.right = curr.right;
                // 3. 把左子树挪到右边
                curr.right = curr.left;
                // 4. 清空左子树
                curr.left = null;
            }
            // 继续处理下一个节点
            curr = curr.right;
        }


        // if (root == null) return;
        // List<TreeNode> res = new ArrayList<>();
        // preOrder(root, res);
        // root.left = null;
        // for (int i = 1; i < res.size(); i++) {
        //     TreeNode prev = res.get(i - 1);
        //     TreeNode cur = res.get(i);
        //     prev.left = null;
        //     prev.right = cur;
        // }
    }

    // public void preOrder(TreeNode root, List<TreeNode> res) {
    //     if (root == null) return;
    //     res.add(root);
    //     preOrder(root.left, res);
    //     preOrder(root.right, res);
    // }
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

