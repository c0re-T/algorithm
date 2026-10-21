package com.ittxf.leetcode.hot100.p21_mergetwosortedlists;

public class LeetCode21_MergeTwoSortedLists {
    public static void main(String[] args) {
        Solution solution = new Solution();
        // 1. 构建 list1: 1 -> 2 -> 4
        int[] nums = {1, 2, 4};
        ListNode list1 = new ListNode(nums[0]); // 保留头节点
        ListNode cur1 = list1;                  // 游标用来移动
        for (int i = 1; i < nums.length; i++) {
            cur1.next = new ListNode(nums[i]);
            cur1 = cur1.next;
        }

        // 2. 构建 list2: 1 -> 3 -> 4
        int[] nums2 = {1, 3, 4};
        ListNode list2 = new ListNode(nums2[0]); // 保留头节点
        ListNode cur2 = list2;                   // 游标用来移动
        for (int i = 1; i < nums2.length; i++) {
            cur2.next = new ListNode(nums2[i]);
            cur2 = cur2.next;
        }

        // 3. 合并链表
        ListNode mergedList = solution.mergeTwoLists(list1, list2);

        // 4. 遍历打印合并后的链表
        System.out.print("合并后的链表: ");
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
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) { // 列表1的当前节点值小于列表2的当前节点值
                current.next = list1;
                list1 = list1.next;
            } else { // 列表2的当前节点值小于列表1的当前节点值
                current.next = list2;
                list2 = list2.next;
            }
            current = current.next;
        }
        // 将剩余节点直接连接
        current.next = list1 != null ? list1 : list2;
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

