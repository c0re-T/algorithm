# LeetCode 98 验证二叉搜索树（上下界往下传，不是只比父子）

> 对应程序：[LeetCode98_ValidateBinarySearchTree.java](./LeetCode98_ValidateBinarySearchTree.java)
> `main` 那棵树（5 的右孩子 4 下面挂着 3 和 6），实测：
>
> ```
> 验证二叉搜索树: false
> ```

---

## 一、为什么不能只比父亲

最容易写出来的版本是这样：

```java
if (root.left != null && root.left.val >= root.val) return false;
if (root.right != null && root.right.val <= root.val) return false;
return isValidBST(root.left) && isValidBST(root.right);
```

它只保证"孩子和父亲"的关系。反例只要三层：

```
      5
     / \
    4   6
       / \
      3   7
```

`3 < 6` 合法、`7 > 6` 合法、`6 > 5` 合法 —— 每一对父子都过关，但 3 在 5 的右子树里却比 5 小，整棵树不是 BST。实测这棵树上"只比父子"的版本给 `true`，他的版本给 `false`（正确）。

他的写法是把**区间**往下传：

```java
return helper(root, Long.MIN_VALUE, Long.MAX_VALUE);

public boolean helper(TreeNode root, long lower, long upper) {
    if (root == null) return true;
    if (root.val <= lower || root.val >= upper) return false;
    return helper(root.left, lower, root.val) && helper(root.right, root.val, upper);
}
```

往左走，上界收紧成父亲的值；往右走，下界收紧成父亲的值。祖先留下的约束一路带到底，所以隔代的违规也抓得住。

## 二、两个边界为什么用 long

`root.val` 是 `int`，题面允许取到 `Integer.MIN_VALUE` / `Integer.MAX_VALUE`。如果边界也用 int：

| 单节点树 | 边界用 long | 边界用 int |
| --- | --- | --- |
| `val = Integer.MIN_VALUE` | `true`（正确） | `false`（`val <= lower` 成立） |
| `val = Integer.MAX_VALUE` | `true`（正确） | `false`（`val >= upper` 成立） |

初值必须严格落在 int 值域之外，`long` 是最省事的办法。另一种写法是传 `Integer` 对象、用 `null` 表示"这一侧没有约束"，效果一样。

判定用的是 `<=` / `>=`（严格），所以**重复值一律 false**：`(1,1)` 实测 `false`，符合 BST 定义。

## 三、随机对拍：错的写法很难被随机数据抓到

20,000 棵随机树（1..30 个节点，值随机、允许重复），与"中序遍历必须严格递增"的参考实现对比：

```
他的版本   差异 = 0
只比父子   差异 = 45 / 20,000（0.23%）
```

随机生成的树绝大多数本来就一眼非法（根附近就违规），"只比父子"也能否掉，所以 20,000 组里只暴露 45 次。**随机对拍 0.23% 的漏检率不代表这个写法可用。**

于是换成"先造合法 BST 再改一个值"的定向实验：16,368 个非法实例里

```
他的版本   抓住 16,368 个（100%）
只比父子   抓住 15,130 个，漏掉 1,238 个（7.6%）
```

漏掉的全是"违规发生在隔代"的那一类。要测一个判定函数，得专门造它容易放过的输入，不能只靠随机。

## 四、递归深度：合法斜链会爆栈，非法斜链反而不会

| 形状 | 节点数 | 结果 | 耗时 |
| --- | --- | --- | --- |
| 合法右斜链（值 0,1,2,…） | 20,000 | `true` | 0 ms |
| 合法右斜链 | 30,000 | `StackOverflowError` | — |
| 合法右斜链 | 50,000 / 200,000 / 1,000,000 | `StackOverflowError` | — |
| 合法左斜链（值 0,−1,−2,…） | 50,000 | `StackOverflowError` | — |
| **全 0 右斜链（非法）** | 50,000 / 200,000 / 1,000,000 | `false` | **0 ms** |
| 满树（中序填值，高 20） | 1,048,575 | `true` | 2 ms |

爆栈线在 2 万到 3 万之间，和 `p104` 同一档 —— 栈深度等于树高，这份并没有特殊豁免。

最后一行是个容易踩的坑：**用一条 100 万节点的链去测"会不会爆栈"，如果链上的值不构成合法 BST，测出来的是 0 ms 通过**。因为第二个节点就撞上 `root.val <= lower` 返回 false，`&&` 左边已经是 false，右支那次递归根本不执行，整条链只钻了 2 层。我第一次就是这么测的，得出了"这份不怕斜链"的错误结论；把值改成严格递增之后立刻爆栈。**测递归深度必须用能让递归走到底的输入。**

## 五、一句话记住

```
   往左收紧 upper = 父亲值，往右收紧 lower = 父亲值；
   只比父子会被隔代违规骗过：定向实验漏 1,238 / 16,368（7.6%），
   而 20,000 组随机树只暴露 45 次（0.23%）——随机数据测不出这个 bug；
   边界用 long，否则单节点 MIN_VALUE / MAX_VALUE 会被误判 false；
   <= / >= 是严格判定，重复值 false；空树 true；
   20,000 组与"中序严格递增"参考 0 差异；
   合法斜链 2 万通过、3 万爆栈；非法斜链 100 万反而 0 ms —— 测栈深度要用能走到底的输入。
```