package com.ittxf.leetcode.hot100.p19_removenthnodefromendoflist;

public class LeetCode19_RemoveNthNodeFromEndOfList {
    public static void main(String[] args) {
        Solution solution = new Solution();
        // 1. 构建 list1: 1 -> 2 -> 3 -> 4 -> 5
        int[] nums = {1, 2, 3, 4, 5};
        ListNode list1 = new ListNode(nums[0]); // 保留头节点
        ListNode cur1 = list1;                  // 游标用来移动
        for (int i = 1; i < nums.length; i++) {
            cur1.next = new ListNode(nums[i]);
            cur1 = cur1.next;
        }

        // 2. 删除倒数第 n 个节点
        ListNode mergedList = solution.removeNthFromEnd(list1, 2);

        // 3. 遍历打印合并后的链表
        System.out.print("删除后的链表: ");
        ListNode cur = mergedList;
        while (cur != null) {
            System.out.print(cur.val);
            if (cur.next != null) {
                System.out.print(" -> ");
            }
            cur = cur.next; // 游标后移
        }
        System.out.println();
    }
}

class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head; // 将虚拟头结点指向链表的头结点
        // 定义两个指针，都指向虚拟头结点
        ListNode first = dummy;
        ListNode second = dummy;
        // 第一个指针先移动 n+1 步
        for (int i = 0; i <= n; i++) first = first.next;
        while (first != null) { // 当第一个指针到达链表末尾时，第二个指针正好到达倒数第 n 个节点的前一个节点
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }
}

class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

