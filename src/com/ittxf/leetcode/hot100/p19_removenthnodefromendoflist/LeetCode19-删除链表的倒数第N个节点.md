# LeetCode 19 删除链表的倒数第 N 个节点（dummy + 固定间距 n+1）

> 对应程序：[LeetCode19_RemoveNthNodeFromEndOfList.java](./LeetCode19_RemoveNthNodeFromEndOfList.java)
> `main` 对 `1 -> 2 -> 3 -> 4 -> 5` 删倒数第 2 个，实测：
>
> ```
> 删除后的链表: 1 -> 2 -> 3 -> 5
> ```

---

## 一、为什么先走 n+1 步

```java
ListNode dummy = new ListNode(0);
dummy.next = head;
ListNode first = dummy, second = dummy;
for (int i = 0; i <= n; i++) first = first.next;   // n+1 步
while (first != null) {
    first = first.next;
    second = second.next;
}
second.next = second.next.next;
```

`second` 最终要停在**被删节点的前一个**，才能执行 `second.next = second.next.next`。两条指针保持 `n+1` 的间距，当 `first` 走到 null 时，`second` 正好在倒数第 `n+1` 个位置。

对 `[1,2,3,4,5]`、`n=2` 的推演：

| 阶段 | first | second |
| --- | --- | --- |
| 初始 | dummy | dummy |
| 先走 3 步 | 3 | dummy |
| 循环 1 | 4 | 1 |
| 循环 2 | 5 | 2 |
| 循环 3 | null | 3 |

此时 `second = 3`，`3.next = 3.next.next` 就是跳过 4。

## 二、dummy 真正救的是"删头"

`n == 长度` 时，被删的是头节点，`second` 会停在 dummy 上 —— 没有 dummy 就没有"头节点的前一个"可用。实测：

```
n=5 -> [2, 3, 4, 5]
```

`n=1` 删尾节点也正常：

```
n=1 -> [1, 2, 3, 4]
```

## 三、边界实测：n 越界会 NPE

题目保证 `1 <= n <= 长度`，但这份代码没有自己的检查，越界行为如下（实测）：

| 调用 | 结果 |
| --- | --- |
| 长度 5，`n = 0` | `NullPointerException`（second 停在尾节点，`second.next` 是 null） |
| 长度 5，`n = 6` | `NullPointerException`（first 在第 7 步取 null.next） |
| 长度 5，`n = 7` | `NullPointerException` |
| 空链表，`n = 0` | `NullPointerException` |
| 单节点，`n = 1` | `[]` |

也就是说：**合法的 n 全部正确，非法的 n 全部崩**，没有"静默删错"的中间状态。`n=0` 之所以也崩，是因为循环结束时 `second` 落在尾节点而不是尾节点的前一个。

## 四、差分对拍与代价

长度 1..50、`n` 在 `1..长度` 内，固定种子 20,000 组，与"数组下标跳过 `len-n`"的参考实现对比：

```
结果不一致 = 0
```

10 万个节点删倒数第 5 万个：**0.41 ms**。一趟扫描，`O(L)` 时间、`O(1)` 空间。

## 五、一句话记住

```
   dummy 接在头上，first 先走 n+1 步，然后两条指针同步走；
   first 到 null 时 second 停在待删节点的前一个；
   dummy 是"n 等于链表长度、要删头"时唯一可用的前驱；
   n=0 或 n>长度 都会 NPE（second 落点 / first 越界），没有静默删错；
   20,000 组合法输入 0 差异，10 万节点 0.41 ms，O(L)/O(1)。
```
