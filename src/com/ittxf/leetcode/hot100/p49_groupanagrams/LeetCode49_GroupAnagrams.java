package com.ittxf.leetcode.hot100.p49_groupanagrams;

import java.util.*;

public class LeetCode49_GroupAnagrams {
    public static void main(String[] args) {
        String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
        System.out.println(new Solution().groupAnagrams(strs));
    }
}

class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        // Key: 排序后的字符串 (如 "aet")
        // Value: 属于该异位词组的原始字符串列表 (如 ["eat", "tea", "ate"])
        Map<String, List<String>> map = new HashMap<>();

        for (String str : strs) {
            // 1. 将当前字符串转为字符数组并排序
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            // 2. 排序后的字符数组转回字符串，作为 HashMap 的 Key
            String key = new String(chars);
            // 3. 如果 map 中没有这个 key，新建一个 List 放进去
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }

            // 4. 将原字符串加入对应的 List 中
            map.get(key).add(str);
        }

        return new ArrayList<>(map.values());

        /*List<List<String>> result = new ArrayList<>();

        boolean[] visited = new boolean[strs.length];
        List<String> list = new ArrayList<>();
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {
                list.add(strs[i]);
                visited[i] = true;
            } else continue;
            for (int j = i + 1; j < visited.length; j++) {
                if (!visited[j]) {
                    String str1 = strs[j];
                    char[] chars1 = str1.toCharArray();
                    Arrays.sort(chars1);
                    str1 = new String(chars1);

                    String str2 = strs[i];
                    char[] chars2 = str2.toCharArray();
                    Arrays.sort(chars2);
                    str2 = new String(chars2);

                    if (str1.equals(str2)) {
                        list.add(strs[j]);
                        visited[j] = true;
                    }
                }
            }
            result.add(list);
            list = new ArrayList<>();
        }

        return result.reversed();*/
    }
}