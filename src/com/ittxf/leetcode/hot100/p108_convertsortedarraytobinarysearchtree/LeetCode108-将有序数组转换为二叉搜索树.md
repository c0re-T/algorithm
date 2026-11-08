# LeetCode 108 将有序数组转换为二叉搜索树（取中点当根，高度天然 log n）

> 对应程序：[LeetCode108_ConvertSortedArrayToBinarySearchTree.java](./LeetCode108_ConvertSortedArrayToBinarySearchTree.java)
> `main` 实测（`TreeNode` 没重写 `toString`，打出来的是对象地址）：
>
> ```
> 排序数组转换为二叉搜索树: com.ittxf.leetcode.hot100.p108_convertsortedarraytobinarysearchtree.TreeNode@5caf905d
> ```
>
> 这行输出验证不了任何东西 —— 下面的结论全部来自把树取出来重新遍历。

---

## 一、中点当根，两边递归

```java
private TreeNode helper(int[] nums, int left, int right) {
    if (left > right) return null;
    int mid = (left + right) / 2;
    TreeNode root = new TreeNode(nums[mid]);
    root.left  = helper(nums, left, mid - 1);
    root.right = helper(nums, mid + 1, right);
    return root;
}
```

数组有序，所以"中点左边全部比它小、右边全部比它大"是白送的 —— 不需要任何比较就满足 BST 性质。两边节点数最多差 1，所以高度是 `⌈log₂(n+1)⌉`，这就是"平衡"的全部来源。

`(left + right) / 2` 是**向下取整**，偶数个元素时取偏左的那个：

| 输入 | 根 | 形状 |
| --- | --- | --- |
| `{1,2}` | 1 | 1 的右孩子是 2 |
| `{1,2,3}` | 2 | 左右各一个 |
| `{-10,-3,0,5,9}` | 0 | 高度 3 |

改成 `(left + right + 1) / 2`（向上取整）实测 `{1,2}` 变成根 2、左孩子 1 —— 同样合法、同样平衡，LeetCode 两个答案都收。

`left + right` 在题面 `n ≤ 10⁴` 下不会溢出；如果下标能到 10⁹ 量级，得写成 `left + (right - left) / 2`。

## 二、随机对拍：三件事分开验

20,000 个随机升序数组（长度 0..40，值域含重复），建完树之后分别检查：

```
中序遍历与原数组不一致 = 0
左右子树高度差 > 1     = 0
不是严格 BST           = 19,004
```

第三行看着吓人，但**全部 19,004 次都是输入里有重复值造成的**：数组里有两个相同的数，建出来的树中序就会出现相邻相等，严格 BST 判定当然否掉。把输入改成互不相同之后这一项归零。这不是 bug —— 题面的 BST 允许不允许重复取决于定义，而这份实现只是照搬数组顺序，输入有重复它就有重复。

| 输入 | 输出 |
| --- | --- |
| `new int[0]` | `null` |
| `null` | `null`（第一行就挡了） |
| `{Integer.MIN_VALUE}` / `{Integer.MAX_VALUE}` | 单节点，正常 |
| **未排序**数组 | 照样建，建出来的不是 BST |

最后一行是契约问题：方法名写着"有序数组转换"，但它不检查有序性。传乱序进去会得到一棵形状平衡、值却不满足 BST 的树，一声不吭。

## 三、代价与递归深度

| n | 树高 | 耗时 |
| --- | --- | --- |
| 1,000,000 | 20 | 6 ms |

每个元素建一个节点，`O(n)` 时间；递归深度等于树高 `O(log n)`，所以**这份不可能因为斜链爆栈** —— 它造出来的树永远是平衡的，100 万节点也只有 20 层。这和 `p104`/`p105` 那种"输入决定形状"的递归正好相反：这里的形状由算法自己保证。

空间上除了输出树本身，只有 `O(log n)` 的栈。

## 四、一句话记住

```
   mid = (left + right) / 2 向下取整，{1,2} 的根是 1；改成 +1 取整根变 2，两个都对；
   有序是前提，方法不检查：传乱序数组会建出一棵形状平衡但不是 BST 的树；
   20,000 组：中序还原 0 差异、不平衡 0 次，
   "不是严格 BST" 19,004 次全部由输入重复值造成；
   空数组 / null 都返回 null；
   100 万元素 6 ms、树高 20 —— 深度由算法保证，不会斜链爆栈。
```