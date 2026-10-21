package com.ittxf.leetcode.hot100.p148_sortlist;

import java.util.ArrayList;
import java.util.List;

public class LeetCode148_SortList {
    public static void main(String[] args) {
        Solution solution = new Solution();
        // 1. 构建 list1: 4 -> 2 -> 1 -> 3
        int[] nums = {4, 2, 1, 3};
        ListNode list1 = new ListNode(nums[0]); // 保留头节点
        ListNode cur1 = list1;                  // 游标用来移动
        for (int i = 1; i < nums.length; i++) {
            cur1.next = new ListNode(nums[i]);
            cur1 = cur1.next;
        }

        // 2. 无序链表排序
        ListNode mergedList = solution.sortList(list1);

        // 3. 遍历打印交换后的链表
        System.out.print("交换后的链表: ");
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
    public ListNode sortList(ListNode head) {
        // 1. 递归终止条件：0个或1个节点
        if (head == null || head.next == null) return head;

        // 2. 快慢指针找中点（慢指针停在中间，快指针到结尾）
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // 3. 从中点断开链表
        ListNode mid = slow.next;
        slow.next = null;

        // 4. 递归排序左右两半
        ListNode left = sortList(head);
        ListNode right = sortList(mid);

        // 5. 合并两个有序链表（直接复用你之前那题的代码）
        return merge(left, right);

        /*ListNode dummy = new ListNode(0);
        List<ListNode> listNodes = new ArrayList<>();
        ListNode cur = head;
        while (cur != null) {
            listNodes.add(cur);
            cur = cur.next;
        }

        listNodes.sort((a, b) -> a.val - b.val);
        cur = dummy;
        for (ListNode listNode : listNodes) {
            cur.next = listNode;
            cur = cur.next;
            cur.next = null;
        }

        return dummy.next;*/
    }

    // 合并两个有序链表（你刚才写过的代码，直接拿来用）
    private ListNode merge(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                cur.next = l1;
                l1 = l1.next;
            } else {
                cur.next = l2;
                l2 = l2.next;
            }
            cur = cur.next;
        }
        cur.next = (l1 != null) ? l1 : l2;
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

