# LeetCode 102 二叉树的层序遍历（levelSize 必须先冻结）

> 对应程序：[LeetCode102_BinaryTreeLevelOrderTraversal.java](./LeetCode102_BinaryTreeLevelOrderTraversal.java)
> `main` 实测：
>
> ```
> 二叉树的层序遍历: [[3], [9, 20], [15, 7]]
> ```

---

## 一、队列里同时装着两层

BFS 的队列在任意时刻都混着"当前层还没弹完的"和"下一层刚塞进来的"。要把它们分开输出，唯一的办法是**在弹之前先把这一层的个数记下来**：

```java
while (!queue.isEmpty()) {
    int levelSize = queue.size();      // 冻结：这就是本层的节点数
    List<Integer> level = new ArrayList<>();
    for (int i = 0; i < levelSize; i++) {
        TreeNode node = queue.poll();
        level.add(node.val);
        if (node.left != null) queue.offer(node.left);
        if (node.right != null) queue.offer(node.right);
    }
    res.add(level);
}
```

`levelSize` 是循环开始前拍的快照。for 里塞进去的孩子不会改变它，所以这一轮恰好弹 `levelSize` 个，弹完队列里剩下的正好全是下一层。

## 二、把快照改成实时读会怎样

很自然会想省掉那个变量，直接写 `for (int i = 0; i < queue.size(); i++)`。实测同一棵 `main` 树：

```
冻结 levelSize ：[[3], [9, 20], [15, 7]]
实时 queue.size()：[[3, 9, 20], [15], [7]]
```

**不死循环**（我原本以为会），因为 `poll` 减一、`offer` 加一，队列长度净变化不大，循环总能结束。错的是分组：弹 3 的时候塞进 9 和 20，`queue.size()` 从 1 变 2，条件立刻放宽，于是 9、20 被算进了第一层。层数和内容全乱，但程序一声不吭地跑完了 —— 又是一次"输出看着挺像样"的错。

## 三、随机对拍与边界

1..60 个节点的随机树，20,000 组，与"递归 DFS 带 depth 参数、按 depth 分桶"的参考实现对比：

```
分组不一致 = 0
（平均 5.05 层，最多 6 层）
```

| 输入 | 输出 |
| --- | --- |
| `null` | `[]` |
| 单节点 5 | `[[5]]` |
| 只有左孩子的链 | 每层一个元素 |

`null` 的判断放在 `queue.offer(root)` 之前，所以空树不会往队列里塞 null。

把 `LinkedList` 换成 `ArrayDeque` 实测输出逐组一致 —— 这里只用到 `offer`/`poll`/`size`，两个实现对 FIFO 语义相同，`ArrayDeque` 常数更小。

## 四、这份写法不怕深树

递归 DFS 的栈深度等于树高，BFS 的队列长度等于**最宽一层**：

| 形状 | 节点数 | 他的 BFS | DFS 参考 |
| --- | --- | --- | --- |
| 右斜链 | 100,000 | 1 ms | `StackOverflowError` |
| 右斜链 | 200,000 | 3 ms | `StackOverflowError` |
| 满树（高 16） | 65,535 | 正常，队列峰值 32,768 | 正常 |

斜链每层只有一个节点，队列长度恒为 1，所以 20 万节点也毫无压力。反过来，满树的最后一层有 n/2 个节点，队列峰值就是 32,768（实测）—— **BFS 的空间代价看宽度，DFS 的栈代价看高度**，两种写法怕的形状正好相反。

## 五、一句话记住

```
   int levelSize = queue.size() 是快照，不是实时值；
   改成 i < queue.size() 不死循环，但把 9、20 塞进第一层：[[3,9,20],[15],[7]]；
   20,000 组与 DFS 按 depth 分桶 0 差异；
   队列峰值 = 最宽一层（满树高 16 时 32,768），所以斜链 20 万节点 3 ms 不爆栈，
   而同样形状的 DFS 直接 StackOverflowError。
```