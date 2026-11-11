package com.ittxf.leetcode.hot100.p763_partitionlabels;

import java.util.ArrayList;
import java.util.List;

public class LeetCode763_PartitionLabels {
    public static void main(String[] args) {
        System.out.println(new Solution().partitionLabels("ababcbacadefegdehijhklij"));
        System.out.println(new Solution().partitionLabels("eccbbbbdec"));
    }
}

class Solution {
    public List<Integer> partitionLabels(String s) {
        char[] charArray = s.toCharArray();
        int[] last = new int[26];
        for (int i = 0; i < charArray.length; i++) {
            last[charArray[i] - 'a'] = i;
        }

        int left = 0;
        int right = 0;
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < charArray.length; i++) {
            right = Math.max(right, last[charArray[i] - 'a']);
            if (i == right) {
                result.add(right + 1 - left);
                left = right + 1;
            }
        }

        return result;
    }
}

