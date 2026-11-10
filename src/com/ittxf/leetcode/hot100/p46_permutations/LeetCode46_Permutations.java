package com.ittxf.leetcode.hot100.p46_permutations;

import java.util.ArrayList;
import java.util.List;

public class LeetCode46_Permutations {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.permute(new int[]{1, 2, 3}));
    }
}

class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        backtrack(nums, new ArrayList<>(), new boolean[nums.length], res);
        return res;
    }

    public void backtrack(int[] nums, List<Integer> path, boolean[] used, List<List<Integer>> res) {
        // 1. 结束条件：路径长度等于数组长度，说明排完了一个全排列
        if (path.size() == nums.length) {
            // 注意：这里必须 new 一个新的 ArrayList 存进去
            // 因为 path 在后续的回溯中还会被修改，如果直接存 path 的引用，最后 res 里全是空的
            res.add(new ArrayList<>(path));
            return;
        }

        // 2. 遍历选择列表
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue; // 如果这个数字已经被用过了，就跳过

            path.add(nums[i]); // 放进篮子
            used[i] = true; // 标记为已使用


            backtrack(nums, path, used, res); // 递归进入下一层决策树

            // 从下一层退回来时，要把刚才的选择清理掉，才能尝试同层的其他选择
            path.remove(path.size() - 1); // 从篮子里拿出来
            used[i] = false; // 恢复未使用状态
        }
    }
}
