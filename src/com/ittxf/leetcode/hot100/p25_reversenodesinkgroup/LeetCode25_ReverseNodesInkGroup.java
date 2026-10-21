package com.ittxf.leetcode.hot100.p25_reversenodesinkgroup;

public class LeetCode25_ReverseNodesInkGroup {
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
        ListNode mergedList = solution.reverseKGroup(list1, 2);

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
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode groupPrev = dummy;
        while (true) {
            // 1. 找到当前组的第 k 个节点
            ListNode kth = groupPrev;

            // 如果 kth 为 null，说明剩下的节点不足 k 个，直接结束
            for (int i = 0; i < k; i++) {
                if (kth == null) break;
                kth = kth.next;
            }

            if (kth == null) break;

            // 2. 记录下一组的头节点（kth 的下一个节点）
            ListNode groupNext = kth.next;

            // 3. 开始反转这 k 个节点（标准的局部链表反转）
            ListNode prev = groupNext;    // 把原标准反转的 null 替换成 groupNext，直接顺势连上下一组
            ListNode curr = groupPrev.next; // 当前组的第一个节点
            ListNode groupStart = curr;     // 记录当前组的第一个节点，它翻转后会变成这一组的尾巴

            for (int i = 0; i < k; i++) {
                ListNode temp = curr.next;
                curr.next = prev;
                prev = curr;
                curr = temp;
            }

            // 4. 将前一组的尾巴，连上反转后的新头（也就是 kth）
            groupPrev.next = kth;

            // 5. 更新 groupPrev，准备处理下一组
            // 因为 groupStart（原来的头）翻转后变成了尾，所以它变成了下一组的 prev
            groupPrev = groupStart;
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

