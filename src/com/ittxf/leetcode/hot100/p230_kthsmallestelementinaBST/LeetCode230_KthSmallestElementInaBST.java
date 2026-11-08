package com.ittxf.leetcode.hot100.p230_kthsmallestelementinaBST;

import java.util.ArrayList;
import java.util.List;

public class LeetCode230_KthSmallestElementInaBST {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(1);
        root.right = new TreeNode(4);
        root.left.right = new TreeNode(2);
        System.out.println("kthSmallest: " + new Solution().kthSmallest(root, 1));
    }
}

class Solution {
    public int kthSmallest(TreeNode root, int k) {
        List<Integer> res = new ArrayList<>();
        inOrder(root, res);
        return res.get(k - 1);
    }

    public void inOrder(TreeNode root, List<Integer> res) {
        if (root == null) return;
        if (root.left != null) inOrder(root.left, res);
        res.add(root.val);
        if (root.right != null) inOrder(root.right, res);
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

