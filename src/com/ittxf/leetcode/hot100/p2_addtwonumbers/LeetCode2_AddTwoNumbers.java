package com.ittxf.leetcode.hot100.p2_addtwonumbers;

public class LeetCode2_AddTwoNumbers {
    public static void main(String[] args) {
        Solution solution = new Solution();
        // 1. 构建 list1: 2 -> 4 -> 3
        int[] nums = {2, 4, 3};
        ListNode list1 = new ListNode(nums[0]); // 保留头节点
        ListNode cur1 = list1;                  // 游标用来移动
        for (int i = 1; i < nums.length; i++) {
            cur1.next = new ListNode(nums[i]);
            cur1 = cur1.next;
        }

        // 2. 构建 list2: 5 -> 6 -> 4
        int[] nums2 = {5, 6, 4};
        ListNode list2 = new ListNode(nums2[0]); // 保留头节点
        ListNode cur2 = list2;                   // 游标用来移动
        for (int i = 1; i < nums2.length; i++) {
            cur2.next = new ListNode(nums2[i]);
            cur2 = cur2.next;
        }

        // 3. 相加两个链表
        ListNode mergedList = solution.addTwoNumbers(list1, list2);

        // 4. 遍历打印相加后的链表
        System.out.print("相加后的链表: ");
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
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0); // 虚拟头结点
        ListNode cur = dummy;

        int carry = 0; // 进位，比如 7 + 8 = 15，carry 就是 1

        // 只要 l1 或 l2 还没遍历完，或者还有进位没处理，就继续加
        while (l1 != null || l2 != null || carry != 0) {
            // 如果链表节点为空，当做 0 处理
            int val1 = (l1 != null) ? l1.val : 0;
            int val2 = (l2 != null) ? l2.val : 0;

            // 计算当前位的和
            int sum = val1 + val2 + carry;

            // 当前位应该存的值（对 10 取余）
            carry = sum / 10;
            int currentDigit = sum % 10;

            // 把这一位挂到新链表上
            cur.next = new ListNode(currentDigit);
            cur = cur.next;

            // 指针后移
            if (l1 != null) l1 = l1.next;
            if (l2 != null) l2 = l2.next;
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

