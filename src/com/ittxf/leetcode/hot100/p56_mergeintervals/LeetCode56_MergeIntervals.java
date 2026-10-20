package com.ittxf.leetcode.hot100.p56_mergeintervals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode56_MergeIntervals {
    public static void main(String[] args) {
        int[][] intervals = {{1,4},{2,3}};
        System.out.println(Arrays.deepToString(new Solution().merge(intervals)));
    }
}

class Solution {
    public int[][] merge(int[][] intervals) {
        if (intervals.length <= 1) return intervals;

        // 1. 按照起点排序
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        // 2. 结果集
        List<int[]> list = new ArrayList<>();
        list.add(intervals[0]); // 先把第一个放进去

        // 3. 从第二个开始遍历
        for (int i = 1; i < intervals.length; i++) {
            int[] current = intervals[i]; // 当前区间
            int[] last = list.get(list.size() - 1); // 结果集里的最后一个区间

            // 如果 当前区间的起点 <= 最后一个区间的终点，说明重叠了
            if (current[0] <= last[1]) {
                // 合并：直接修改最后一个区间的终点（取最大值）
                last[1] = Math.max(last[1], current[1]);
            } else {
                // 不重叠：直接把当前区间加进去
                list.add(current);
            }
        }

        return list.toArray(new int[list.size()][]);


        // 没考虑完全
        /*if (intervals == null || intervals.length == 0) return null;
        if (intervals.length == 1) return intervals;
        // 按照区间的起始位置进行排序
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        int[] array = Arrays.stream(intervals).flatMapToInt(Arrays::stream).toArray();
        int start = 1;
        List<int[]> list = new ArrayList<>();
        while (start <= array.length) {
            if (array[start] >= array[start + 1]) {
                if (array[start] >= array[start + 2]) list.add(new int[]{array[start - 1], array[start]});
                else list.add(new int[]{array[start - 1], array[start + 2]});
            } else {
                list.add(new int[]{array[start - 1], array[start]});
                list.add(new int[]{array[start + 1], array[start + 2]});
            }
            start += 4;
        }

        return list.toArray(new int[list.size()][2]);*/
    }
}