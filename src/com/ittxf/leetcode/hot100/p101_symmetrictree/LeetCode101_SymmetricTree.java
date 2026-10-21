package com.ittxf.leetcode.hot100.p101_symmetrictree;

public class LeetCode101_SymmetricTree {
    public static void main(String[] args) {
        Solution solution = new Solution();

        // 1. 测试空树
        System.out.println("空树: " + solution.isSymmetric(null));

        // 2. 测试 LeetCode 示例树
        // 形如：
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        root.right.left = new TreeNode(3);

        System.out.println("是否对称: " + solution.isSymmetric(root));

        // 3. 测试完全二叉树
        TreeNode fullTree = buildTree(new int[]{1, 2, 3, 4, 5, 6, 7}, 0);
        System.out.println("完全二叉树肯定对称: " + solution.isSymmetric(fullTree));
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
    public boolean isSymmetric(TreeNode root) {
        if (root == null) return true;
        // 核心：判断左子树和右子树是否互为镜像
        return check(root.left, root.right);
    }

    // 辅助方法：判断两棵树 p 和 q 是否互为镜像
    private boolean check(TreeNode p, TreeNode q) {
        // 1. 两个都为空，是对称的
        if (p == null && q == null) return true;
        // 2. 只有一个为空，或者值不相等，肯定不对称
        if (p == null || q == null || p.val != q.val) return false;

        // 3. 核心镜像逻辑：
        // p 的左边 必须和 q 的右边 对称
        // p 的右边 必须和 q 的左边 对称
        return check(p.left, q.right) && check(p.right, q.left);
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

