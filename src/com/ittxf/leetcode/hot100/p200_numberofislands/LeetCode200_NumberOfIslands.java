package com.ittxf.leetcode.hot100.p200_numberofislands;

public class LeetCode200_NumberOfIslands {
    public static void main(String[] args) {
        char[][] grid = {
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'}
        };
        System.out.println(new Solution().numIslands(grid));
    }
}

class Solution {
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfs(grid, i, j);
                }
            }
        }
        return count;
    }

    /**
     * 深度优先搜索的作用非常暴力，叫“淹没算法”。
     * 一旦发现了一块新陆地，它就立刻化身“洪水”，从这个点开始向上下左右四个方向蔓延：
     * 如果碰到边界，停下。
     * 如果碰到水 '0'，停下。
     * 如果碰到陆地 '1'，立刻把它变成 '0'（淹没），然后继续从这个新变成 '0' 的格子向它的四周蔓延。
     * @param grid 整块版图
     * @param i 当前位置的行坐标
     * @param j 当前位置的列坐标
     */
    private void dfs(char[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] != '1') return;
        grid[i][j] = '0';
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }
}
