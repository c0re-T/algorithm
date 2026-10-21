# LeetCode 24 两两交换链表中的节点（三根指针，先存下一组的入口）

> 对应程序：[LeetCode24_SwapNodesInPairs.java](./LeetCode24_SwapNodesInPairs.java)
> `main` 对 `1 -> 2 -> 3 -> 4 -> 5` 交换，实测：
>
> ```
> 交换后的链表: 2 -> 1 -> 4 -> 3 -> 5
> ```

---

## 一、每一组只动三根线

```java
ListNode first = prev.next;      // 这一组的第一个
ListNode second = first.next;    // 这一组的第二个
ListNode nextPair = second.next; // 下一组的入口，必须先存
second.next = first;
first.next = nextPair;
prev.next = second;
prev = first;                    // 原来的 first 变成这一组的尾巴
```

对 `[1,2,3,4,5]` 的前两组：

| 步骤 | 链表形态 |
| --- | --- |
| 初始 | dummy → 1 → 2 → 3 → 4 → 5 |
| 取 first=1, second=2, nextPair=3 | 不变 |
| `second.next = first` | 2 → 1（同时 2 原来的 next=3 只能靠 nextPair 记住） |
| `first.next = nextPair` | 1 → 3 |
| `prev.next = second`（prev 是 dummy） | dummy → 2 → 1 → 3 → 4 → 5 |
| `prev = first`（prev=1） | 下一组从 3 开始，前驱是 1 |
| 第二组同样四步 | dummy → 2 → 1 → 4 → 3 → 5 |

**`nextPair` 必须在改 `second.next` 之前取**。如果先执行 `second.next = first` 再想找"下一组"，`second.next` 已经是 first，3 及后面整段就丢了（或者更糟：first/second 成环）。

## 二、循环条件决定了"单数尾节点"怎么办

```java
while (prev.next != null && prev.next.next != null)
```

两个条件分别对应：

- `prev.next == null`：后面没节点了；
- `prev.next.next == null`：只剩一个节点，凑不成一对 → 原样保留。

实测：

```
n=0 -> null
n=1 -> [1]
n=2 -> [2, 1]
n=5 -> [2, 1, 4, 3, 5]
```

`prev = first` 而不是 `prev = second`：交换后这一组的**尾巴**是原来的 first，下一组要挂在它后面。

## 三、只改指针，不新建节点

4 个节点的链表交换后：

```
输出 4 个节点，复用原节点 4 个；总节点数 = 4（无新建、无环）
```

所以交换两次一定还原（每组又被换回去）：

```
连交换两次 = [1, 2, 3, 4, 5]
```

## 四、差分对拍与代价

n 在 0..100，固定种子 20,000 组，与"数组两两交换后重建"对比：

```
不一致 = 0
```

100 万节点：**3.4 ms**。每对节点做常数次指针赋值，`O(n)` 时间、`O(1)` 空间。

## 五、一句话记住

```
   dummy 起手，prev 指向"上一组的尾巴"；
   每轮先存 nextPair，再 second.next=first、first.next=nextPair、prev.next=second；
   循环条件挡两种情况：后面没了（prev.next==null）、只剩一个（prev.next.next==null）；
   prev 前进到 first（交换后的尾巴）；不 new 节点，两次交换必还原；
   20,000 组 0 差异，100 万节点 3.4 ms，O(n)/O(1)。
```
