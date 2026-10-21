package com.ittxf.leetcode.hot100.p24_swapnodesinpairs;

public class LeetCode24_SwapNodesInPairs {
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

        // 2. 交换相邻节点
        ListNode mergedList = solution.swapPairs(list1);

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
    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        while (prev.next != null && prev.next.next != null) {
            ListNode first = prev.next; // 当前段的头节点
            ListNode second = first.next; // 当前段的尾节点
            ListNode nextPair = second.next; // 下一段的头节点
            second.next = first;
            first.next = nextPair;
            prev.next = second; // 让上一段的尾巴（prev）连上新的头（second）
            prev = first;
        }
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

