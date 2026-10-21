# LeetCode 101 对称二叉树（镜像比的是值，不是结构）

> 对应程序：[LeetCode101_SymmetricTree.java](./LeetCode101_SymmetricTree.java)
> `main` 跑三棵树，实测：
>
> ```
> 空树: true
> 是否对称: false
> 完全二叉树肯定对称: false
> ```
>
> 第三行的注释断言"完全二叉树肯定对称"是**错的**：`[1,2,3,4,5,6,7]` 结构完全对称，但 2 ≠ 3。

---

## 一、镜像的递归定义

```java
private boolean check(TreeNode p, TreeNode q) {
    if (p == null && q == null) return true;
    if (p == null || q == null || p.val != q.val) return false;
    return check(p.left, q.right) && check(p.right, q.left);
}
```

关键是第三行：**p 的左孩子要和 q 的右孩子比**，p 的右孩子要和 q 的左孩子比。写成 `check(p.left, q.left) && check(p.right, q.right)` 就退化成"两棵树是否相同"，不是镜像。

空树返回 true（`root == null` 单独挡在 `isSymmetric` 里）。

## 二、"结构相同"不等于"对称"

两棵都是 7 节点的完全二叉树：

| 层序 | 是否对称 | 原因 |
| --- | --- | --- |
| `[1,2,3,4,5,6,7]` | **false** | 镜像位置是 2 和 3，值不相等 |
| `[1,2,2,3,4,4,3]` | **true** | 镜像位置的值一一相等 |

结构只能保证"形状镜像"，对称还要求镜像位置的值相等。LeetCode 的两个官方示例正是这两种：

```
[1,2,2,3,4,4,3]        -> true
[1,2,2,null,3,null,3]  -> false
```

`main` 里那棵 `1 -> 右 2 -> 左 3` 也是不对称的（左空、右非空），所以 `false` 是对的，只是注释"LeetCode 示例树"容易让人以为是第一个示例。

## 三、随机对拍

1..20 个节点的随机树，值域 0..2（大量重复值，制造"结构像但值不镜像"的情况），固定种子 20,000 组，与"队列成对比较"的迭代参考实现对比：

```
不一致 = 0
```

迭代版把 `(p.left, q.right)`、`(p.right, q.left)` 成对入队，逻辑与递归完全同构，只是不吃调用栈。

## 四、递归深度

结构对称、深度 d 的"双斜链"（左链一直往左、右链一直往右，值全 0）实测：

| 深度 | 结果 |
| --- | --- |
| 5,000 | true |
| 10,000 | true |
| 20,000 | true |
| 50,000 | `StackOverflowError` |

`check` 每层只递归一个分支（另一个参数是 null 时立刻返回 true），但栈深度仍等于树高。10 万节点的满树深度只有 17，实测在第一次比较（1 vs 2）就返回 `false`，`0.0 ms`。

## 五、代价

每个节点只被访问一次：`O(n)` 时间；递归栈 `O(h)`。用队列的迭代版是 `O(n)` 空间（最坏一层有 n/2 个节点），递归版在深链上更省，但有爆栈风险。

## 六、一句话记住

```
   isSymmetric = check(root.left, root.right)，check 里左对右、右对左；
   两个都空 → true；一个空或值不等 → false；
   完全二叉树不一定对称：结构镜像 ≠ 值镜像（[1,2,3,4,5,6,7] 实测 false）；
   20,000 组与迭代参考 0 差异；深度 2 万可跑、5 万爆栈；O(n)/O(h)。
```
