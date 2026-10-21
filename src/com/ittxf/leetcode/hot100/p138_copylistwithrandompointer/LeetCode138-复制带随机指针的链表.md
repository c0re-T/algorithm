# LeetCode 138 复制带随机指针的链表（两趟 + 一张旧→新的表）

> 对应程序：[LeetCode138_CopyListWithRandomPointer.java](./LeetCode138_CopyListWithRandomPointer.java)
> `main` 复制 `1 -> 2 -> 3`，random 分别是 `1→3、2→1、3→2`，实测原链表和新链表打印完全一致：
>
> ```
> 节点值: 1, random指向: 3 -> 节点值: 2, random指向: 1 -> 节点值: 3, random指向: 2 -> null
> ```

---

## 一、为什么不能"边遍历边复制 random"

复制一个节点时，它 `random` 指向的那个节点可能**还没被创建**。所以拆成两趟：

```java
// 第一趟：只复制 val，建立 旧节点 -> 新节点 的映射
while (cur != null) { map.put(cur, new Node(cur.val)); cur = cur.next; }

// 第二趟：连接新链表的 next 和 random
newNode.next   = map.get(cur.next);
newNode.random = map.get(cur.random);
```

`random` 指向的是"旧链表里的某个节点"，用旧节点当 key 一查，就拿到对应的新节点。指针指向的是**新链表里对应的那个节点**，而不是新节点里再存一个旧引用。

## 二、null 为什么不用特判

`cur.next == null`（尾节点）时 `map.get(null)` 返回 null；`cur.random == null` 同理。`HashMap` 允许 null 作为查询的 key（这张表里并没有存 null key，所以只会拿到 null）。这行代码看着危险，实际安全。

唯一的空输入在方法开头挡了：`if (head == null) return null;`。

## 三、深拷贝怎么验证

只比较 `val` 序列不够 —— 如果新链表偷偷复用了原节点，值也一样。用"节点身份 + 指向关系"验证：

1. 两条链长度、每个 `val` 相同；
2. 新链表的每个节点都**不在**原链表的节点集合里；
3. 原链表第 i 个节点的 `random` 指向原链表第 j 个，则新链表第 i 个的 `random` 必须指向新链表第 j 个（按引用比，不按值）。

实测 5 节点样例：

```
结构相同 = true
节点全为新对象 = true
原链表长度仍为 5 = true, 原 0 的 random 仍是 5 = true
```

## 四、随机对拍与代价

长度 0..50、每个节点 30% 概率 `random = null`、其余随机指向任意节点，固定种子 5,000 组，按上面的三条规则逐一验证：

```
结构/深拷贝不一致 = 0
```

10 万个节点（random 随机、30% 为 null）复制：**12.2 ms**，长度 100,000，结构与原链表一致。`O(n)` 时间；额外空间是一张 `O(n)` 的 `HashMap`（键值都是节点引用）。

LeetCode 还有一版 `O(1)` 空间的"编织"做法（把新节点插到旧节点后面，再拆开），这份没有用 —— 换来的就是这张表。

## 五、一句话记住

```
   第一趟只 new 节点并建 map[旧] = 新；
   第二趟 next = map.get(旧.next)、random = map.get(旧.random)；
   null 不用特判：map.get(null) 返回 null（表里没存 null 键）；
   深拷贝要验"节点身份"而不只是值：5000 组 0 差异，10 万节点 12.2 ms；
   O(n) 时间 + O(n) 表。
```
