package com.ittxf.leetcode.hot100.p208_implementtrie_PrefixTree;

public class LeetCode208_ImplementTrie_PrefixTree {
    public static void main(String[] args) {
        Trie obj = new Trie();
        obj.insert("apple");
        boolean apple = obj.search("apple");// 返回 True
        boolean app = obj.search("app");// 返回 False
        obj.startsWith("app");      // 返回 True
        obj.insert("app");
        boolean app1 = obj.search("app");// 返回 True
        System.out.println(apple + " " + app + " " + app1);
    }
}

class Trie {
    TrieNode root; // 根节点（不存任何字符）

    public Trie() {
        root = new TrieNode();
    }

    // 1. 插入单词
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            // 如果该字符对应的路径不存在，就新建一个节点
            if (node.children[c - 'a'] == null) node.children[c - 'a'] = new TrieNode();
            node = node.children[c - 'a']; // 移动到下一个节点
        }
        // 遍历结束后，标记当前节点是一个完整单词的结尾
        node.isEnd = true;
    }

    // 2. 查找完整单词
    public boolean search(String word) {
        TrieNode node = this.commonPrefix(word);
        // 遍历结束后，判断当前节点是否是一个完整单词的结尾
        return node != null && node.isEnd;
    }

    // 3. 查找是否包含该前缀
    public boolean startsWith(String prefix) {
        // 只要路径能走通，说明这个前缀存在，不需要管 isEnd
        TrieNode node = this.commonPrefix(prefix);
        return node != null;
    }

    // 公共前缀
    private TrieNode commonPrefix(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            // 如果该字符对应的路径不存在，就返回 null
            if (node.children[c - 'a'] == null) return null;
            node = node.children[c - 'a']; // 移动到下一个节点
        }
        // 遍历结束后，返回 node
        return node;
    }
}

// TrieNode类：定义前缀树的节点
class TrieNode {
    // 子节点数组，存放 26 个英文字母的引用
    TrieNode[] children;
    // 标记：从根节点到当前节点，是否构成了一个完整的单词
    boolean isEnd;

    public TrieNode() {
        children = new TrieNode[26];
        isEnd = false;
    }
}



