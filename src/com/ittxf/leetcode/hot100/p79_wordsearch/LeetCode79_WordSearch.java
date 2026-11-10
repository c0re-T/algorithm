package com.ittxf.leetcode.hot100.p79_wordsearch;

public class LeetCode79_WordSearch {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.exist(new char[][]{
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}},
                "ABCCED"));
    }
}

class Solution {
    public boolean exist(char[][] board, String word) {
        if (word == null || word.length() == 0) return true;

        // 优化：提前将 String 转成 char[]，提升访问速度
        char[] words = word.toCharArray();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                // 2. 优化：只要遇到开头匹配的，立刻去搜，搜到了直接返回 true
                if (board[i][j] == words[0] && backtrack(board, words, 0, i, j)) return true;
            }
        }
        return false;
    }

    public boolean backtrack(char[][] board, char[] word, int index, int i, int j) {
        // 1. 越界判断 + 字符匹配判断
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != word[index]) return false;

        // 2. 触底条件：已经找到了整个单词的最后一个字符
        if (index == word.length - 1) return true;

        // 3. 保存当前字符，并标记为已访问
        char temp = board[i][j];
        board[i][j] = '#'; // 用 # 或 0 都可以，只要不是字母就行

        // 4. 递归搜索四个方向
        boolean found = backtrack(board, word, index + 1, i + 1, j) ||
                backtrack(board, word, index + 1, i - 1, j) ||
                backtrack(board, word, index + 1, i, j + 1) ||
                backtrack(board, word, index + 1, i, j - 1);

        // 5. 🔥 核心修复：回溯，恢复状态
        board[i][j] = temp;

        return found;
    }
}

