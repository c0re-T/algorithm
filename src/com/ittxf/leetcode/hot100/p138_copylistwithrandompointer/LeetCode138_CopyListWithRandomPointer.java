package com.ittxf.leetcode.hot100.p138_copylistwithrandompointer;

import java.util.HashMap;
import java.util.Map;

public class LeetCode138_CopyListWithRandomPointer {
    public static void main(String[] args) {
        // 1. 构建原链表：1 -> 2 -> 3
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);

        // 2. 连接 next 指针
        node1.next = node2;
        node2.next = node3;

        // 3. 连接 random 指针
        // 1 的 random 指向 3
        // 2 的 random 指向 1
        // 3 的 random 指向 2
        node1.random = node3;
        node2.random = node1;
        node3.random = node2;

        System.out.println("========== 原链表 ==========");
        printList(node1);

        // 4. 调用你的复制方法
        Solution solution = new Solution();
        Node copiedHead = solution.copyRandomList(node1);

        System.out.println("\n========== 复制的新链表 ==========");
        printList(copiedHead);
    }

    /**
     * 辅助方法：打印链表节点的 val 和 random 指针指向的值
     */
    private static void printList(Node head) {
        Node cur = head;
        while (cur != null) {
            int randomVal = (cur.random != null) ? cur.random.val : -1; // -1代表null
            System.out.print("节点值: " + cur.val + ", random指向: " + randomVal + " -> ");
            cur = cur.next;
        }
        System.out.println("null");
    }
}


class Solution {
    public Node copyRandomList(Node head) {
        if (head == null) return null;

        // 1. 创建一个 HashMap，key 放原节点，value 放新节点
        Map<Node, Node> map = new HashMap<>();

        // 2. 第一次遍历：复制所有节点，建立映射
        Node cur = head;
        while (cur != null) {
            map.put(cur, new Node(cur.val));
            cur = cur.next;
        }

        // 3. 第二次遍历：连接新节点的 next 和 random
        cur = head;
        while (cur != null) {
            // 从 map 中取出当前节点对应的新节点
            Node newNode = map.get(cur);
            // 新节点的 next 指向原节点 next 对应的新节点
            newNode.next = map.get(cur.next);
            // 新节点的 random 指向原节点 random 对应的新节点
            newNode.random = map.get(cur.random);
            cur = cur.next;
        }

        // 返回原头节点对应的新头节点
        return map.get(head);
    }
}

class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}

