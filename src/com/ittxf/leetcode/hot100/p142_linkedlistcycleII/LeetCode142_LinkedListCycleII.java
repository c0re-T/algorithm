package com.ittxf.leetcode.hot100.p142_linkedlistcycleII;

public class LeetCode142_LinkedListCycleII {
    public static void main(String[] args) {
        Solution solution = new Solution();

        // 案例 1：有环链表 (尾部节点连接回索引为 1 的节点)
        // 链表：3 -> 2 -> 0 -> -4 -> (回到 2)
        ListNode head1 = createCycleLinkedList(new int[]{3, 2, 0, -4}, 1);
        System.out.println("案例1 (有环) 结果: " + solution.detectCycle(head1).val); // 预期 2

        // 案例 2：无环链表
        // 链表：1 -> 2 -> null
        ListNode head2 = createCycleLinkedList(new int[]{1, 2}, 0);
        System.out.println("案例2 (有环) 结果: " + solution.detectCycle(head2).val); // 预期 0

        // 案例 3：单节点自身成环
        // 链表：1 -> (回到 1)
        ListNode head3 = createCycleLinkedList(new int[]{1}, -1);
        System.out.println("案例3 (自身成环) 结果: " + solution.detectCycle(head3)); // 预期 null
    }

    /**
     * 辅助方法：根据数组和 pos 构建带有环的链表
     * @param nums 节点值的数组
     * @param pos  尾部节点要连接到的位置索引（-1 表示无环）
     * @return 链表的头节点
     */
    public static ListNode createCycleLinkedList(int[] nums, int pos) {
        if (nums == null || nums.length == 0) return null;

        ListNode head = new ListNode(nums[0]);
        ListNode current = head;
        ListNode cycleEntryNode = null; // 记录环的入口节点

        // 如果 pos == 0，说明头节点就是环的入口
        if (pos == 0) cycleEntryNode = head;

        // 依次创建后续节点
        for (int i = 1; i < nums.length; i++) {
            current.next = new ListNode(nums[i]);
            current = current.next;
            // 如果遍历到的索引正好是 pos，记录一下这个节点
            if (i == pos) cycleEntryNode = current;
        }

        // 如果 pos 有效，把最后一个节点的 next 指向环的入口节点
        if (pos != -1 && cycleEntryNode != null) {
            current.next = cycleEntryNode;
        }

        return head;
    }
}

class Solution {
    public ListNode detectCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next; // 移动 slow 一步
            fast = fast.next.next; // 移动 fast 两步
            if (slow == fast) {
                ListNode ptr = head; // 指向头节点
                while (ptr != slow) { // 当 ptr 和 slow 相遇时即为环的入口节点
                    ptr = ptr.next; // 移动 ptr 一步
                    slow = slow.next; // 移动 slow 一步
                }
                return ptr; // 返回环的入口节点
            }
        }
        return null;
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

