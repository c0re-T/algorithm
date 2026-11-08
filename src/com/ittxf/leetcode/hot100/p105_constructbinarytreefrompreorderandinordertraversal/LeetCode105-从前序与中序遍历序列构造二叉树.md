# LeetCode 105 从前序与中序遍历序列构造二叉树（四个下标一起动）

> 对应程序：[LeetCode105_ConstructBinaryTreeFromPreorderAndInorderTraversal.java](./LeetCode105_ConstructBinaryTreeFromPreorderAndInorderTraversal.java)
> `main` 用前序 `{3,9,20,15,7}`、中序 `{9,3,15,20,7}`，层序打印实测（末尾有一个空格、没有换行）：
>
> ```
> 3 9 20 15 7 
> ```

---

## 一、两个序列各管一件事

前序的第一个元素一定是当前子树的根；拿着这个根去中序里找位置，中序就被切成 `[左子树] 根 [右子树]`。左子树有几个节点，前序里紧跟根的那几个就是左子树的前序。

```java
int rootVal = preorder[preLeft];
int rootIndex = indexMap.get(rootVal);   // 根在中序里的位置
int leftSize = rootIndex - inLeft;       // 左子树节点个数

root.left  = build(preLeft + 1, preLeft + leftSize, inLeft, rootIndex - 1);
root.right = build(preLeft + leftSize + 1, preRight, rootIndex + 1, inRight);
```

`leftSize` 是把两个序列连起来的唯一桥梁：它由**中序**算出来，却用来切**前序**。四个下标里只要有一个写成 `rootIndex` 而不是 `rootIndex - inLeft`，切出来的段长就不对，但程序照样能建出一棵看着像树的树。

终止条件是 `preLeft > preRight || inLeft > inRight`。两个条件理论上等价（段长永远一致），写两个是防御性的。

## 二、indexMap 换来的是什么

不用 map 就得每层在中序段里线性找根。这个差别**只在特定形状上才显形**：

| 形状 | n | HashMap 定位 | 每层线性扫 |
| --- | --- | --- | --- |
| 平衡树 | 20,000 | 2 ms | 0 ms |
| 全右斜 | 2,000 | 0 ms | 0 ms |
| **全左斜** | 2,000 | 0 ms | **4 ms** |
| 全左斜 | 3,000 | 1 ms | 6 ms |
| 全左斜 | 4,000 | 1 ms | 7 ms |

右斜树上前序和中序完全同序，根永远落在 `inLeft`，线性扫第一步就命中，所以 map 白建；平衡树上根在段中间，但每层只扫 O(段长)，总量 O(n log n)，2 万规模下 HashMap 的常数反而更贵。

**只有左斜树能把线性扫打成 O(n²)**：根永远在 `inRight`，每层都要扫完整段，n=4,000 时已经慢 7 倍。我一开始拿右斜树测，两种写法都是 0 ms，差点得出"map 没用"的结论 —— 选错形状的对比等于没对比。

## 三、随机对拍

20,000 棵随机树（值互不相同，平均 47 个节点），先取出它的前序和中序，再交给 `buildTree` 还原，与原树逐层比形状和值：

```
不一致 = 0
```

空数组 → `null`；单点 → 只有一个节点。

## 四、四个实测出来的坑

**1）`indexMap` 和 `preorder` 都是实例字段，而且从不 clear。** 同一个 `Solution` 连调两次：第一次之后 map 里 3 个 key，第二次之后 5 个 —— 上一批的 key 一直留着。这次没出错是因为新 key 覆盖了旧值，但只要两次调用有交集又不完全覆盖，`indexMap.get(rootVal)` 就可能拿到上一棵树的下标。对比 `p94` 的 `list` 字段（那个是直接越调越长），这份的泄漏更隐蔽。

**2）值重复直接越界。** `preorder={1,1}`、`inorder={1,1}` → `ArrayIndexOutOfBoundsException: Index 2 out of bounds for length 2`。map 里 key `1` 只留下最后一个下标，`leftSize` 算错，前序段越切越宽。题面保证值互不相同，所以不触发，但这份实现没有任何检查。

**3）前序里的值在中序里不存在 → NPE。** `preorder={1,99}`、`inorder={1,2}` → `NullPointerException: Cannot invoke "java.lang.Integer.intValue()" because the return value of "java.util.Map.get(Object)" is null`。`int rootIndex = indexMap.get(...)` 自动拆箱，null 就在这一步炸。换新实例同样炸，所以不是字段泄漏导致的。

**4）两个数组长度不一致时静默建出错的树。** `pre={1,2,3}`、`in={1,2}` → 打印形状 `()1(()2())`，不报错、少一个节点。终止条件里的 `||` 只挡"段为空"，挡不住"两边段长对不上"。

## 五、递归深度

`build` 的栈深度等于树高：

| 形状 | n | 结果 |
| --- | --- | --- |
| 全右斜 | 10,000 | `StackOverflowError` |
| 全右斜 | 50,000 / 100,000 | `StackOverflowError` |
| 全左斜 | 5,000 | `StackOverflowError` |
| 平衡树 | 20,000（高 15） | 正常，2 ms |

爆栈线在 5,000 到 10,000 之间，比 `p104` 的 2 万更低 —— `build` 的栈帧里有四个 int 参数加 `rootVal`/`rootIndex`/`leftSize`/`root` 四个局部变量，帧更大，同样深度的链更早撑不住。

## 六、一句话记住

```
   leftSize = rootIndex - inLeft：由中序算出、用来切前序，是唯一的桥梁；
   20,000 组还原后与原树同形，0 差异；
   indexMap 只在左斜树上显形（n=4,000：1 ms vs 线性扫 7 ms），
   右斜树和平衡树上两种写法测不出差别；
   实例字段不 clear（连调两次 map 从 3 个 key 变 5 个）；
   重复值 AIOOBE、前序值不在中序里 NPE、长度不一致静默建错树；
   斜链 1 万爆栈，平衡树 2 万只要 2 ms。
```