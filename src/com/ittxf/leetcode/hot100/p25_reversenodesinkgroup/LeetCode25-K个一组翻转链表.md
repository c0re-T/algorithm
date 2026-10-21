# LeetCode 25 K 个一组翻转链表（局部反转时把 null 换成下一组的头）

> 对应程序：[LeetCode25_ReverseNodesInkGroup.java](./LeetCode25_ReverseNodesInkGroup.java)
> `main` 对 `1 -> 2 -> 3 -> 4 -> 5` 取 k=2，实测：
>
> ```
> 交换后的链表: 2 -> 1 -> 4 -> 3 -> 5
> ```

---

## 一、先数够 k 个，再翻

```java
while (true) {
    ListNode kth = groupPrev;
    for (int i = 0; i < k; i++) {
        if (kth == null) break;
        kth = kth.next;
    }
    if (kth == null) break;          // 剩余不足 k 个，原样保留
    ...
}
```

`kth` 从 `groupPrev`（上一组的尾巴）出发走 k 步，落在当前组的第 k 个节点。走不到 k 步就说明剩余不够一组，直接结束 —— 所以"不足 k 个的尾巴不反转"是循环条件自带的，不需要额外处理。

## 二、局部反转的精髓：`prev` 的初值不是 null

```java
ListNode prev = groupNext;       // 标准反转的 null 换成了下一组的头
ListNode curr = groupPrev.next;
ListNode groupStart = curr;
for (int i = 0; i < k; i++) {
    ListNode temp = curr.next;
    curr.next = prev;
    prev = curr;
    curr = temp;
}
groupPrev.next = kth;            // 前一组的尾巴接上新的头
groupPrev = groupStart;          // 原来的头变成这一组的尾巴
```

普通反转链表从 `prev = null` 开始，翻完得到的尾巴指向 null；这里从 `prev = groupNext` 开始，翻完得到的尾巴**自动指向下一组的第一个节点**，少一次接线。

对 `[1,2,3,4,5]`、k=2 的第一组：

| 步骤 | prev | curr | 动作 |
| --- | --- | --- | --- |
| 初始 | 3 | 1 | groupNext=3，groupStart=1 |
| i=0 | 1 | 2 | 1.next=3 |
| i=1 | 2 | 3 | 2.next=1 |
| 收尾 | — | — | dummy.next=2；groupPrev=1 |

第一组变成 `2 -> 1 -> 3 -> ...`；第二组从 3 开始，`groupPrev=1`。

实测各种 k：

```
n=5, k=1 -> [1, 2, 3, 4, 5]
n=5, k=2 -> [2, 1, 4, 3, 5]
n=5, k=3 -> [3, 2, 1, 4, 5]     ← 剩下 4、5 不足一组，原样保留
n=5, k=5 -> [5, 4, 3, 2, 1]
n=5, k=6 -> [1, 2, 3, 4, 5]     ← 一组都凑不齐
n=0, k=3 -> []
```

## 三、k ≤ 0 不会死循环，但会把链表毁掉

这是这份代码唯一没有挡住的输入（题面保证 `k >= 1`）。实测不是死循环，而是**返回一个自身成环的 dummy，原链表全部丢失**：

| k | 返回头节点值 | 可从返回值走到的不同节点数 | 是否成环 | 原 1..5 是否全部丢失 |
| --- | --- | --- | --- | --- |
| 0 | 0（dummy） | 1 | true | 是 |
| −1 | 0（dummy） | 1 | true | 是 |

原因：k ≤ 0 时反转循环一次都不进，`kth` 仍等于 `groupPrev`，`groupNext = groupPrev.next`，然后 `groupPrev.next = kth` 等于**指向自己**；`groupPrev = groupStart` 继续往后走，把每个节点都改成自环，最后 `groupPrev` 变成 null 退出。返回值是 `dummy.next`，而第一轮已经把 `dummy.next` 指成了 dummy 自己。整个过程不抛异常。

如果要在工程里用，进入循环前加一句 `if (k <= 1) return head;` 就够了（k=1 本来也是恒等变换）。

## 四、正确性与代价

n 在 0..60、k 在 1..10，固定种子 20,000 组，与"数组按 k 段反转"的参考实现对比：

```
不一致 = 0
```

10 个节点、k=3 时输出复用原节点 10/10（只改 `next`，不 new）。10 万节点、k=10 实测 **0.3 ms**；k=7（最后有剩余段）**0.1 ms**。`O(n)` 时间、`O(1)` 空间。

## 五、注释与输出

`main` 的注释和打印都写着"交换相邻节点 / 交换后的链表"，是从 24 题复制过来没改；这里做的是"每 k 个一组反转"，k=2 时才恰好和两两交换同形。

## 六、一句话记住

```
   先用 kth 走 k 步判断这一组够不够，不够就停；
   反转时 prev 从 groupNext 开始（不是 null），翻完尾巴自动接下一组；
   groupPrev 前进到 groupStart（原来的头变成尾巴）；
   k ≤ 0 没挡：不报错、不死循环，但返回 dummy 自环，整条链丢光；
   20,000 组（k>=1）0 差异，10 万节点 0.3 ms，O(n)/O(1)。
```
