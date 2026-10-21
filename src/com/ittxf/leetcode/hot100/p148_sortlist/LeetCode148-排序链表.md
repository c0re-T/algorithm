# LeetCode 148 排序链表（自顶向下归并：快慢断链 + 复用 21 题 merge）

> 对应程序：[LeetCode148_SortList.java](./LeetCode148_SortList.java)
> `main` 对 `4 -> 2 -> 1 -> 3` 排序，实测：
>
> ```
> 交换后的链表: 1 -> 2 -> 3 -> 4
> ```
>
> 打印文案"交换后的链表"是从 24 题抄过来的，这里做的是排序。

---

## 一、三段式

```java
if (head == null || head.next == null) return head;
ListNode slow = head, fast = head.next;
while (fast != null && fast.next != null) { slow = slow.next; fast = fast.next.next; }
ListNode mid = slow.next;
slow.next = null;                    // 断开
ListNode left = sortList(head);
ListNode right = sortList(mid);
return merge(left, right);           // 复用 21 题
```

- `fast = head.next` 而不是 `head`：这样 `slow` 停在**左半段的最后一个节点**，`slow.next` 就是右半段的头。偶数个节点时两半等长，奇数个时左边多一个。
- `slow.next = null` 必须写，否则左半段递归时会把整条链都带进去，无限递归。
- `merge` 与 21 题完全相同（`<` 相等取右边、剩余整段挂上）。

对 `[4,2,1,3]`：

| 层 | 左半 | 右半 |
| --- | --- | --- |
| 1 | `[4,2]` | `[1,3]` |
| 2 | `[4]` / `[2]` | `[1]` / `[3]` |
| 合并 | `[2,4]` | `[1,3]` |
| 根 | `[1,2,3,4]` | — |

## 二、随机对拍与节点复用

n 在 0..300、值域 −500..499，固定种子 20,000 组，与 `Arrays.sort` 对比：

```
不一致 = 0
```

边界：

```
null -> null
单节点 -> [7]
已有序 -> [1, 2, 3, 4]
全相同 -> [3, 3, 3, 3]
逆序 -> [1, 2, 3, 4, 5]
```

4 个节点排序后复用原节点 4/4（只改 `next`，不 new 结果节点、无环）。归并过程会创建短命的 dummy 节点，但结果链表全部是原节点。

## 三、代价：100 万节点实测

两个独立 JVM 各跑两轮（避免同一进程内的 JIT 先后顺序偏袒某一方）：

| 写法 | 第 1 轮 | 第 2 轮 | 额外空间 |
| --- | --- | --- | --- |
| 自顶向下归并（这份） | 195 ms | 215 ms | 递归栈 O(log n) |
| 注释里的"转 List + `list.sort`" | 307 ms | 294 ms | 100 万个 `ListNode` 引用 + 列表本身 |

归并是 `O(n log n)` 时间、`O(log n)` 栈；"转 List"也是 `O(n log n)`，但要多存一份引用数组，常数更大。100 万节点时差约 1.4~1.6 倍。

递归深度是 `log2(n)`：100 万节点约 20 层，不会像 94/104/226 那样在深链上爆栈 —— 因为每层都把链**对半砍**。

## 四、注释里那份"转 List"还有一个隐患

```java
listNodes.sort((a, b) -> a.val - b.val);
```

减法比较器在 int 极值上溢出。实测：

```
三个极值 [MAX, MIN, 0] 排序结果: 0 2147483647 -2147483648     ← 顺序错，不报错
20 万随机 + 两个极值: IllegalArgumentException:
    Comparison method violates its general contract!
```

`MAX - MIN` 溢出成 −1，比较器给出自相矛盾的顺序；数据量一大，TimSort 直接抛"违反契约"。正确写法是 `Integer.compare(a.val, b.val)`。这段代码现在被注释掉了，但复制出去就是坑。

## 五、一句话记住

```
   自顶向下归并：快慢指针找中点（fast 从 head.next 起，slow 停在左半段尾）、
   slow.next = null 断开、递归排两半、merge 合并；
   100 万节点 195/215 ms，比"转 List 再 sort"（294/307 ms）快且省一份引用；
   深度只有 log2(n) ≈ 20，不会爆栈；
   注释那份的 a.val - b.val 比较器会溢出：200,000 + 极值实测直接
   IllegalArgumentException（三个极值时静默排错）。
```
