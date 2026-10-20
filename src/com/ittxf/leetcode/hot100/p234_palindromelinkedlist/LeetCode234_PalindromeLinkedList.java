package com.ittxf.leetcode.hot100.p234_palindromelinkedlist;

public class LeetCode234_PalindromeLinkedList {
    public static void main(String[] args) {
        Solution solution = new Solution();

        // 案例 1：偶数个节点的回文 (1 -> 2 -> 2 -> 1)
        ListNode head1 = createLinkedList(new int[]{1, 2, 2, 1});
        System.out.print("案例1 [1,2,2,1] 是否回文: ");
        System.out.println(solution.isPalindrome(head1)); // 预期 true
        System.out.print("案例1 执行后链表恢复情况: ");
        printLinkedList(head1); // 验证你恢复原链表的代码是否生效

        // 案例 2：奇数个节点的回文 (1 -> 2 -> 3 -> 2 -> 1)
        ListNode head2 = createLinkedList(new int[]{1, 2, 3, 2, 1});
        System.out.print("案例2 [1,2,3,2,1] 是否回文: ");
        System.out.println(solution.isPalindrome(head2)); // 预期 true
        System.out.print("案例2 执行后链表恢复情况: ");
        printLinkedList(head2);

        // 案例 3：不是回文 (1 -> 2 -> 3 -> 1)
        ListNode head3 = createLinkedList(new int[]{1, 2, 3, 1});
        System.out.print("案例3 [1,2,3,1] 是否回文: ");
        System.out.println(solution.isPalindrome(head3)); // 预期 false
    }

    // 辅助方法：数组转链表
    public static ListNode createLinkedList(int[] arr) {
        ListNode dummy = new ListNode(-1);
        ListNode curr = dummy;
        for (int val : arr) {
            curr.next = new ListNode(val);
            curr = curr.next;
        }
        return dummy.next;
    }

    // 辅助方法：打印链表
    public static void printLinkedList(ListNode head) {
        ListNode curr = head;
        while (curr != null) {
            System.out.print(curr.val);
            if (curr.next != null) System.out.print(" -> ");
            curr = curr.next;
        }
        System.out.println();
    }
}

class Solution {
    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true;

        // 1. 快慢指针找中点
        ListNode slow = head;
        ListNode fast = head;
        // 当 fast 走到末尾时，slow 刚好指向前半段的最后一个节点（偶数）或正中间（奇数）
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // 2. 反转后半段链表
        // slow.next 是后半段的起点
        ListNode secondHalfStart = reverseList(slow.next);

        // 3. 开始比对
        ListNode p1 = head; // 前半段起点
        ListNode p2 = secondHalfStart; // 后半段起点
        boolean result = true;

        while (p2 != null) {
            if (p1.val != p2.val) {
                result = false;
                break; // 发现不相等的，直接标记失败并跳出
            }
            p1 = p1.next;
            p2 = p2.next;
        }

        // 4. （工程素养）为了不破坏原链表结构，比较完后把后半段再反转回来
        slow.next = reverseList(secondHalfStart);

        return result;
    }

    // 这就是你刚学会的【反转链表】直接拿过来用！
    private ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }
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

