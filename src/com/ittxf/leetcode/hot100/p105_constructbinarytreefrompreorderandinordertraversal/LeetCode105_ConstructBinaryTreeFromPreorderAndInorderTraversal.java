package com.ittxf.leetcode.hot100.p105_constructbinarytreefrompreorderandinordertraversal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class LeetCode105_ConstructBinaryTreeFromPreorderAndInorderTraversal {
    public static void main(String[] args) {
        int[] preorder = {3, 9, 20, 15, 7};
        int[] inorder = {9, 3, 15, 20, 7};
        TreeNode treeNode = new Solution().buildTree(preorder, inorder);

        printTree(treeNode);
    }

    // 层序遍历
    public static void printTree(TreeNode root) {
        if (root == null) return;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.print(node.val + " ");
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
    }
}

/**
 * 前序负责“告诉你谁是根”，中序负责“告诉你根左边是左子树、右边是右子树”。
 * 前序的第一个元素一定是当前子树的根。
 * 拿着这个根去中序里找位置，就能把中序切成两段：
 *      中序：[左子树的所有节点]  根  [右子树的所有节点]
 */
class Solution {
    private Map<Integer, Integer> indexMap = new HashMap<>();
    private int[] preorder;
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        this.preorder = preorder; // 局部变量赋值给成员变量，方便后续使用

        // 记录中序中每个值的位置，方便 O(1) 找根
        for (int i = 0; i < inorder.length; i++) {
            indexMap.put(inorder[i], i);
        }

        return build(0, preorder.length - 1, 0, inorder.length - 1);
    }

    /**
     * 根据 preorder 和 inorder，构建子树
     * @param preLeft preorder 前序的左边界
     * @param preRight preorder 前序的右边界
     * @param inLeft inorder 中序的左边界
     * @param inRight inorder 中序的右边界
     * @return
     */
    private TreeNode build(int preLeft, int preRight, int inLeft, int inRight) {
        if (preLeft > preRight || inLeft > inRight) return null; // 终止条件
        // 前序第一个就是根
        int rootVal = preorder[preLeft];
        TreeNode root = new TreeNode(rootVal);

        // 根在中序中的位置
        int rootIndex = indexMap.get(rootVal);

        // 左子树节点个数
        int leftSize = rootIndex - inLeft;

        // 递归构建左子树
        root.left = build(
                preLeft + 1, // 从 preLeft + 1 开始
                preLeft + leftSize, // 共 leftSize 个，所以preLeft + leftSize
                inLeft, // 中序左边界
                rootIndex - 1 // 中序右边界
        );

        // 递归构建右子树
        root.right = build(
                preLeft + leftSize + 1, // 左子树右边界+1就是右子树开始
                preRight, // 右子树结束
                rootIndex + 1, // 中序左边界
                inRight // 中序右边界
        );

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


