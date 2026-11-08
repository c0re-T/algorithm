# LeetCode 230 二叉搜索树中第 K 小的元素（整棵中序走一遍，没有提前停）

> 对应程序：[LeetCode230_KthSmallestElementInaBST.java](./LeetCode230_KthSmallestElementInaBST.java)
> `main` 那棵树（3 的左 1、1 的右 2、3 的右 4），k=1，实测：
>
> ```
> kthSmallest: 1
> ```

---

## 一、中序遍历 + 取下标

```java
public int kthSmallest(TreeNode root, int k) {
    List<Integer> res = new ArrayList<>();
    inOrder(root, res);
    return res.get(k - 1);
}
```

BST 的中序遍历天然升序，所以"第 k 小"就是中序序列的第 `k-1` 个。`inOrder` 里那两行 `if (root.left != null)` / `if (root.right != null)` 只是少几次空调用，删掉结果一样（`root == null` 那行已经挡住了）。

思路没问题，代价有问题：**它把整棵树都走完了，哪怕 k=1。**

## 二、提前停能省多少

平衡 BST（n 个节点、值 1..n），对比"中序走到第 k 个就 return"的写法：

| n | k | 他的版本 | 访问节点数 | 提前停版 | 访问节点数 |
| --- | --- | --- | --- | --- | --- |
| 1,000,000 | 1 | 12 ms | **1,000,000** | 0 ms | **19** |
| 1,000,000 | n | 7 ms | 1,000,000 | 7 ms | 1,000,000 |

k=1 时相差 5 万倍的节点访问量。而 k=n 时两者一样 —— 提前停的收益完全取决于 k 有多小，最坏情况没有改善。

他这份的耗时**与 k 无关**（12 ms 和 7 ms 的差别只是 `res.get` 的位置和缓存效应），因为工作量恒定为 n。空间也是 `O(n)`：那个 `ArrayList` 装下了全部节点值，而提前停版只需要 `O(h)` 的栈。

## 三、它其实不要求输入是 BST

`inOrder` 只是照左-根-右的顺序收值，没有任何比较。所以传一棵普通二叉树进去，返回的是**中序序列的第 k 个**，不是"第 k 小"。实测非 BST 输入下与"把中序序列排序后取第 k 个"不一致 —— 这份实现把"输入必须是 BST"完全写在了使用者身上。

20,000 棵随机 BST（值互不相同）、k 随机合法，与"把值收集起来排序后取第 k 个"的参考对比：

```
不一致 = 0
```

## 四、k 越界全是 AIOOBE，没有任何检查

`res.get(k - 1)` 是裸取，实测（树有 4 个节点）：

| k | 异常 |
| --- | --- |
| 0 | `Index -1 out of bounds for length 4` |
| 5 | `Index 4 out of bounds for length 4` |
| 6 | `Index 5 out of bounds for length 4` |
| −1 | `Index -2 out of bounds for length 4` |
| 空树 + k=1 | `Index 0 out of bounds for length 0` |

好处是**不会静默给错答案**：越界一定抛异常，而不是返回一个看起来合理的值。坏处是异常信息里的 `length 4` 暴露了内部列表长度，调用方得自己保证 `1 ≤ k ≤ 节点数`。题面正是这么保证的。

## 五、递归深度

| 形状 | n | 结果 |
| --- | --- | --- |
| 右斜链 | 50,000 | `StackOverflowError` |
| 右斜链 | 100,000 | `StackOverflowError` |
| 平衡树 | 1,000,000（高 20） | 正常，12 ms |

和 `p104` 同一档：栈深度等于树高，满树 100 万节点只有 20 层，斜链 5 万就倒。改成迭代中序（显式栈）能同时解决爆栈和提前停两件事。

## 六、一句话记住

```
   BST 中序升序 → 第 k 小 = res.get(k - 1)；
   但它走完全部 n 个节点：n=100 万、k=1 时访问 1,000,000 个节点 / 12 ms，
   提前停版只访问 19 个 / 0 ms；k=n 时两者相同；
   20,000 组与"排序后取第 k 个"0 差异；不检查输入是不是 BST；
   k=0 / k>n / 空树全是 IndexOutOfBounds，不会静默给错值；
   斜链 5 万爆栈，平衡树 100 万正常。
```