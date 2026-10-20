package com.ittxf.leetcode.hot100.p206_reverselinkedlist;

public class LeetCode206_ReverseLinkedList {
    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode temp = head;
        for (int i = 2; i < 6; i++) {
            temp.next = new ListNode(i);
            temp = temp.next;
        }
        System.out.print("反转前: ");
        printList(head);

        ListNode reversedHead = new Solution().reverseList(head);
        System.out.print("反转后: ");
        printList(reversedHead);
    }

    public static void printList(ListNode head) {
        ListNode curr = head;
        while (curr != null) {
            System.out.print(curr.val);
            if (curr.next != null) {
                System.out.print(" -> ");
            }
            curr = curr.next;
        }
        System.out.println();
    }
}

class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null; // 前驱节点，初始为 null
        ListNode curr = head; // 当前节点，从头节点开始

        while (curr != null) {
            // 1. 暂存当前节点的下一个节点（因为接下来要改 curr.next，不改会断链）
            ListNode nextTemp = curr.next; // nextTemp = 2 3 4 ...

            // 2. 将当前节点的 next 指针反向，指向前驱节点
            curr.next = prev; // 1 -> null 2 -> 1 -> null 3 -> 2 -> 1 -> null ...

            // 3. prev 指针向前移动一步，来到当前节点的位置
            prev = curr; // prev = 1 2 3 ...

            // 4. curr 指针向前移动一步，来到下一个节点的位置
            curr = nextTemp; // curr = 2 3 ...
        }

        // 循环结束时，curr 变成 null，prev 刚好指向原链表的最后一个节点（即新链表的头节点）
        return prev;
    }
}

class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}