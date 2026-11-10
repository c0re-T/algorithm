package com.ittxf.leetcode.hot100.p207_courseschedule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LeetCode207_CourseSchedule {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.canFinish(2, new int[][]{{1, 0},{0, 1}}));
    }
}

class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // 1. 构建邻接表和入度数组
        // 邻接表：adj.get(i) 存储所有依赖课程 i 的后继课程
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adj.add(new ArrayList<>());
        }

        int[] indegree = new int[numCourses];

        for (int[] pre : prerequisites) {
            int course = pre[0];   // 目标课程
            int requires = pre[1]; // 先修课程

            // 先修课 requires 指向目标课 course
            adj.get(requires).add(course);
            indegree[course]++; // 目标课的入度 +1
        }

        // 2. 将入度为 0 的节点（没有先修要求的课）加入队列
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) queue.offer(i);
        }

        // 3. BFS 拓扑排序
        int count = 0; // 记录已修的课程数量
        while (!queue.isEmpty()) {
            int current = queue.poll();
            count++; // 修完一门课

            // 遍历所有依赖当前课程的后继课程
            for (int nextCourse : adj.get(current)) {
                indegree[nextCourse]--; // 前置条件少了一个
                // 如果没有其他前置条件了，可以修这门课了
                if (indegree[nextCourse] == 0) queue.offer(nextCourse);
            }
        }

        // 4. 判断是否修完了所有的课程
        // 如果 count == numCourses，说明没有环，返回 true
        // 否则说明有环（互相依赖，无法入队），返回 false
        return count == numCourses;
    }
}

