package com.ittxf.leetcode.hot100.p20_validparentheses;

import java.util.*;

public class LeetCode20_ValidParentheses {
    public static void main(String[] args) {
        Solution solution = new Solution();
        boolean valid = solution.isValid("([])");
        System.out.println(valid);
    }
}

class Solution {
    public boolean isValid(String s) {
        // 1. 剪枝：如果是奇数个字符，肯定无法完全匹配
        if (s.length() % 2 != 0) return false;

        /*Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put(']', '[');
        map.put('}', '{');

        // 使用 Deque 模拟栈
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            // 2. 遇到左括号，压入对应的右括号（技巧：直接存右括号，后面比对更方便）
            // 3. 遇到右括号，分两种情况
            if (!map.containsKey(c)) stack.push(c);
            else {
                if (stack.isEmpty() || stack.pop() != map.get(c)) return false;
            }
        }

        // 4. 最后检查栈是否为空（如果还有剩的，说明左括号多了）
        return stack.isEmpty();
        */

        char[] ch = s.toCharArray();
        int top = 0;
        for(char c : ch){
            switch (c) {
                case '(': ch[top++] = ')'; break;
                case '[': ch[top++] = ']'; break;
                case '{': ch[top++] = '}'; break;
                default:
                    if(top == 0 || ch[--top] != c){
                        return false;
                    }
            }
        }

        return top == 0;
    }
}
