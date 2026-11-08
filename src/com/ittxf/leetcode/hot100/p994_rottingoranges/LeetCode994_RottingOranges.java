package com.ittxf.leetcode.hot100.p994_rottingoranges;

import java.util.LinkedList;
import java.util.Queue;

public class LeetCode994_RottingOranges {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.orangesRotting(new int[][]{{2,1,1},{1,1,0},{0,1,1}}));
    }
}

class Solution {
    public int orangesRotting(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        Queue<int[]> queue = new LinkedList<>();
        int freshCount = 0; // 统计新鲜橘子的数量

        // 1. 初始化：把所有的腐烂橘子加入队列，统计新鲜橘子
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 2) queue.offer(new int[]{i, j});
                else if (grid[i][j] == 1) freshCount++;
            }
        }

        // 如果没有新鲜橘子，直接返回 0
        if (freshCount == 0) return 0;

        int minutes = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // 上下左右

        // 2. 开始 BFS 广度优先遍历
        while (!queue.isEmpty() && freshCount > 0) {
            minutes++; // 每一轮代表过了一分钟
            int size = queue.size();

            // 把当前这一分钟内所有腐烂的橘子都取出来，向外扩散
            for (int k = 0; k < size; k++) {
                int[] curr = queue.poll();

                for (int[] dir : directions) {
                    int newI = curr[0] + dir[0];
                    int newJ = curr[1] + dir[1];

                    // 如果没有越界，且是新鲜橘子，就感染它
                    if (newI >= 0 && newI < rows && newJ >= 0 && newJ < cols && grid[newI][newJ] == 1) {
                        grid[newI][newJ] = 2; // 标记为腐烂
                        freshCount--;         // 新鲜橘子少了一个
                        queue.offer(new int[]{newI, newJ}); // 这个新腐烂的橘子，下一分钟会去感染别人
                    }
                }
            }
        }

        // 3. 循环结束后，如果还有新鲜橘子，说明有孤岛，返回 -1
        return freshCount == 0 ? minutes : -1;
    }
}