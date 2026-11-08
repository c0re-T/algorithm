package com.ittxf.leetcode.hot100.p437_pathsumIII;

import java.util.HashMap;
import java.util.Map;

public class LeetCode437_PathSumIII {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(5);
        root.right = new TreeNode(-3);
        root.right.right = new TreeNode(11);
        root.left.left = new TreeNode(3);
        root.left.right = new TreeNode(2);
        root.left.left.left = new TreeNode(3);
        root.left.left.right = new TreeNode(-2);
        root.left.right.right = new TreeNode(1);
        System.out.println("算路径和为8的路径个数: " + new Solution().pathSum(root, 8));
    }
}

class Solution {
    // public int pathSum(TreeNode root, int targetSum) {
    //     if (root == null) return 0;
    //     // 把当前 root 作为起点找一遍 (pathSumFromRoot)
    //     // 把 root.left 作为起点找一遍 (pathSum(root.left...))
    //     // 把 root.right 作为起点找一遍 (pathSum(root.right...))
    //     return pathSum(root.left, targetSum) +
    //             pathSum(root.right, targetSum) +
    //             pathSumFromRoot(root, (long) targetSum);
    // }
    //
    // public int pathSumFromRoot(TreeNode root, long targetSum) {
    //     if (root == null) return 0;
    //
    //     // 核心逻辑：看当前节点能不能单独作为一条路径的终点
    //     // 如果当前节点的值刚好等于剩下的目标值，说明找到了一条，记为 1，否则记为 0。
    //     // 返回 当前节点提供的数量 + 左子树凑出的数量 + 右子树凑出的数量
    //     return (root.val == targetSum ? 1 : 0) +
    //             // 既然当前节点用掉了 root.val 的值，
    //             // 那么对于左右孩子来说，它们需要凑齐的剩余目标值就变成了 targetSum - root.val
    //             pathSumFromRoot(root.left, targetSum - root.val) +
    //             pathSumFromRoot(root.right, targetSum - root.val);
    // }

    public int pathSum(TreeNode root, int targetSum) {
        // map 记录：<前缀和, 出现次数>
        // 初始化放入 (0, 1)，代表如果从根节点到当前节点的路径刚好等于 targetSum，也能被算进去
        Map<Long, Integer> prefixSumMap = new HashMap<>();
        prefixSumMap.put(0L, 1);

        return dfs(root, 0L, (long) targetSum, prefixSumMap);
    }

    private int dfs(TreeNode node, long curSum, long targetSum, Map<Long, Integer> prefixSumMap) {
        if (node == null) return 0;

        // 1. 计算当前路径的前缀和
        curSum += node.val;

        // 2. 查找是否有满足条件的路径
        // 如果之前存在前缀和等于 curSum - targetSum，说明这中间有一段路径的和为 targetSum
        int count = prefixSumMap.getOrDefault(curSum - targetSum, 0);

        // 3. 把当前前缀和加入 map
        prefixSumMap.put(curSum, prefixSumMap.getOrDefault(curSum, 0) + 1);

        // 4. 继续向左右子树找
        count += dfs(node.left, curSum, targetSum, prefixSumMap);
        count += dfs(node.right, curSum, targetSum, prefixSumMap);

        // 5. 回溯：离开当前节点时，必须把当前前缀和从 map 中移除
        // 因为当前节点已经不在接下来要遍历的路径里了，不能影响其他分支的计算
        prefixSumMap.put(curSum, prefixSumMap.get(curSum) - 1);

        return count;
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

