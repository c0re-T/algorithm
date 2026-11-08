package com.ittxf.leetcode.hot100.p146_lrucache;

import java.util.HashMap;
import java.util.Map;

public class LeetCode146_LRUCache {
    public static void main(String[] args) {
        LRUCache lRUCache = new LRUCache(2);
        lRUCache.put(1, 1); // 缓存是 {1=1}
        lRUCache.put(2, 2); // 缓存是 {2=2, 1=1}
        System.out.println("找1:" + lRUCache.get(1));    // 返回 1 {1=1, 2=2}
        lRUCache.put(3, 3); // 该操作会使得关键字 2 作废，缓存是 {3=3, 1=1}
        System.out.println("找2:" + lRUCache.get(2));    // 返回 -1 (未找到)
        lRUCache.put(4, 4); // 该操作会使得关键字 1 作废，缓存是 {4=4, 3=3}
        System.out.println("找1:" + lRUCache.get(1));    // 返回 -1 (未找到)
        System.out.println("找3:" + lRUCache.get(3));    // 返回 3
        System.out.println("找4:" + lRUCache.get(4));    // 返回 4
    }
}

class LRUCache {
    // 定义双向链表节点
    class Node {
        int key, value;
        Node prev, next;
        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private Map<Integer, Node> map;
    private int capacity;
    private Node head, tail; // 虚拟头尾节点

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>(capacity);
        head = new Node(-1, -1);
        tail = new Node(-1, -1);
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        Node node = map.get(key);
        moveToHead(node);
        return node.value;
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;
            moveToHead(node);
        }else {
            if (map.size() == capacity) {
                Node tailNode = tail.prev;
                map.remove(tailNode.key);
                removeNode(tailNode);
            }
            Node newNode = new Node(key, value);
            map.put(key, newNode);
            addNode(newNode);
        }
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void addNode(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addNode(node);
    }
}

