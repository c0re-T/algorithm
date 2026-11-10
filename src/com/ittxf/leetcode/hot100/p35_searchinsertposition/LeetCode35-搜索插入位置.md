# LeetCode 35 搜索插入位置（二分：没找到时 lt 就是答案）

> 对应程序：[LeetCode35_SearchInsertPosition.java](./LeetCode35_SearchInsertPosition.java)
> `main` 依次查 `{1,3,5,6}` 里的 5、2、7，实测：
>
> ```
> 2
> 1
> 4
> ```

---

## 一、循环结束时的 `lt` 就是第一个 ≥ target 的位置

```java
while (lt <= rt) {
    int mid = lt + (rt - lt) / 2;
    if (nums[mid] == target) return mid;
    else if (nums[mid] < target) lt = mid + 1;
    else rt = mid - 1;
}
return lt;
```

三种走法：命中直接返回；`nums[mid] < target` 说明答案在右边；否则右边整段都太大。循环在 `lt == rt + 1` 时结束，此时 `lt` 恰好是**第一个大于等于 target 的下标** —— 存在就是它，不存在就是插入点。所以 `return lt` 和 `return rt + 1` 完全等价，穷举 781,248 组实测 0 差异。

## 二、穷举与随机对拍

穷举 n=0..7、元素取 0..4 的所有有序数组，每个查目标 −1..6：

```
穷举 781,248 组：互不相同的数组上与 lowerBound 参考 0 差异
随机 20,000 组：互不相同的数组上 0 差异
```

## 三、有重复值时返回的是"某一个"，不是第一个

题面保证互不相同。放宽成重复值后，781,248 组里有 **114,705 组**和 lowerBound 不同，但返回的都是**合法的命中下标**：

```
{1,3,3,3,5} 找 3 -> 2（第一个 3 在下标 1）
{3,3,3}     找 3 -> 1（第一个在下标 0）
```

也就是说，如果题目要的是"第一个 ≥ target 的位置"（如 C++ `lower_bound`），这份在重复值上不合格；本题只要"存在就返回其下标"，所以题面约束下没问题。

## 四、边界与副作用

| 输入 | 结果 |
| --- | --- |
| `{}` | 0（`rt = -1`，直接落在插入点） |
| `{2,4,6}` 找 1 / 找 9 | 0 / 3 |
| `{1,3,5,6}` 找 5（连调两次） | 2 / 2 |
| `null` | `NullPointerException: Cannot read the array length because "<parameter1>" is null` |

入参只读；`mid = lt + (rt - lt) / 2` 不溢出；空数组不需要特判。

## 五、一句话记住

```
   命中就 return mid，没命中时 lt == rt + 1，lt 就是插入点；
   return lt 与 return rt + 1 穷举 781,248 组 0 差异；
   互不相同的数组上与 lowerBound 0 差异；
   有重复值时返回某个命中下标而不是第一个：{1,3,3,3,5} 找 3 给 2，{3,3,3} 给 1 —— 题面保证 distinct；
   空数组给 0，null NPE，入参只读。
```
