package com.ittxf.leetcode.hot100.p23_mergeksortedlists;

public class LeetCode23_MergeKSortedLists {
    public static void main(String[] args) {
        // 构造测试用例：
        // lists[0]: 1 -> 4 -> 5
        // lists[1]: 1 -> 3 -> 4
        // lists[2]: 2 -> 6

        ListNode[] lists = new ListNode[3];
        lists[0] = buildList(new int[]{1, 4, 5});
        lists[1] = buildList(new int[]{1, 3, 4});
        lists[2] = buildList(new int[]{2, 6});

        System.out.println("合并前：");
        for (ListNode list : lists) {
            printList(list);
        }

        Solution solution = new Solution();
        ListNode mergedList = solution.mergeKLists(lists);

        System.out.println("合并后：");
        printList(mergedList);
    }

    // 辅助方法：通过数组构建链表
    private static ListNode buildList(int[] nums) {
        if (nums == null || nums.length == 0) return null;
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int num : nums) {
            cur.next = new ListNode(num);
            cur = cur.next;
        }
        return dummy.next;
    }

    // 辅助方法：打印链表
    private static void printList(ListNode head) {
        ListNode cur = head;
        while (cur != null) {
            System.out.print(cur.val);
            if (cur.next != null) System.out.print(" -> ");
            cur = cur.next;
        }
        System.out.println();
    }
}

class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        ListNode mergedList = lists[0];
        for (int i = 0; i < lists.length - 1; i++) {
            mergedList = merge(mergedList, lists[i + 1]);
        }
        return mergedList;
    }

    private ListNode merge(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                current.next = l1;
                l1 = l1.next;
            } else {
                current.next = l2;
                l2 = l2.next;
            }
            current = current.next;
        }
        current.next = l1 != null ? l1 : l2;
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

