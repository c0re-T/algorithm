package com.ittxf.leetcode.hot100.p118_pascalstriangle;

import java.util.ArrayList;
import java.util.List;

public class LeetCode118_PascalsTriangle {
    public static void main(String[] args) {
        System.out.println(new Solution().generate(5));
        System.out.println(new Solution().generate(1));
    }
}

class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> res = new ArrayList<>(numRows); // 预分配外层容量

        for (int i = 0; i < numRows; i++) {
            // 预分配内层容量，第 i 行刚好有 i + 1 个元素
            List<Integer> row = new ArrayList<>(i + 1);

            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) row.add(1);
                else row.add(res.get(i - 1).get(j - 1) + res.get(i - 1).get(j));
            }
            res.add(row);
        }
        return res;
    }
}
