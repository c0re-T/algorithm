package com.ittxf.leetcode.hot100.p160_intersectionoftwolinkedlists;

public class LeetCode160_IntersectionOfTwoLinkedLists {
    public static void main(String[] args) {
        // 1. 先创建公共部分的节点 (8 -> 4 -> 5)
        ListNode common8 = new ListNode(8);
        ListNode common4 = new ListNode(4);
        ListNode common5 = new ListNode(5);
        common8.next = common4;
        common4.next = common5;

        // 2. 创建链表 A 的独立部分 (4 -> 1)，并接上公共部分
        ListNode a4 = new ListNode(4);
        ListNode a1 = new ListNode(1);
        a4.next = a1;
        a1.next = common8; // 接上公共节点

        // 3. 创建链表 B 的独立部分 (5 -> 6 -> 1)，并接上公共部分
        ListNode b5 = new ListNode(5);
        ListNode b6 = new ListNode(6);
        ListNode b1 = new ListNode(1);
        b5.next = b6;
        b6.next = b1;
        b1.next = common8; // 接上公共节点

        // 4. 测试
        ListNode result = new Solution().getIntersectionNode(a4, b5);

        if (result != null) {
            System.out.println("相交节点的值是: " + result.val); // 应该输出 8
        } else {
            System.out.println("没有相交");
        }
    }
}

class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode pA = headA;
        ListNode pB = headB;

        // 当 pA 和 pB 不相等时，一直循环
        // 走到 null 时，就切换到对方的链表头
        while (pA != pB) {
            pA = pA == null ? headB : pA.next;
            pB = pB == null ? headA : pB.next;
        }
        return pA;
    }
}

class ListNode {
    int val;
    ListNode next;
    ListNode(int x) {
        val = x;
        next = null;
    }
}