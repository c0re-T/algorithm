# LeetCode 153 寻找旋转排序数组中的最小值（二分：和右端比）

> 对应程序：[LeetCode153_FindMinimumInRotatedSortedArray.java](./LeetCode153_FindMinimumInRotatedSortedArray.java)
> `main` 依次传入 `{3,4,5,1,2}`、`{4,5,6,7,0,1,2}`、`{11,13,15,17}`，实测：
>
> ```
> 1
> 0
> 11
> ```

---

## 一、拿 `nums[mid]` 和 `nums[rt]` 比，而不是和 `nums[lt]` 比

```java
while (lt < rt) {
    int mid = lt + (rt - lt) / 2;
    if (nums[mid] < nums[rt]) rt = mid;   // mid 可能就是最小值，不能 +1
    else lt = mid + 1;                    // mid 只可能在左段，最小在右边
}
return nums[lt];
```

`nums[rt]` 是右边界：如果 `nums[mid] < nums[rt]`，说明 `[mid, rt]` 是升序的，最小值**可能在 mid 本身**，所以 `rt = mid`（不是 `mid - 1`）；否则 `mid` 落在左段，最小值在右边，`lt = mid + 1`。循环在 `lt == rt` 时停下，那个位置就是最小值。

## 二、穷举与随机对拍

```
n=1..8 的所有旋转（36 个）：与 min() 参考 0 差异
20,000 组随机互不相同的旋转数组：0 差异
```

有序未旋转 `{1,2,3,4}` 给 1，旋转 n−1 位 `{2,3,4,1}` 也给 1，单元素给自身。

## 三、`rt = mid - 1` 是最容易写错的一格

| 对拍 | 差异 |
| --- | --- |
| 穷举 36 个旋转 | 11 个 |
| 20,000 组随机 | 6,837 组 |

```
[2,0,1] -> 2（真值 0）
[3,1,2] -> 3（真值 1）
[1,3,2] -> 2（真值 1）
```

`mid` 本身可能就是最小值，`mid - 1` 直接把它跳过去了。

## 四、重复值会真的给错值

题面保证互不相同（重复值是第 154 题）。放宽后 20,000 组随机实测 **1,485 组**最小值错误：

```
[2,0,2,2,2] -> 2（真值 0）
[0,1,1,1,2,3] -> 1（真值 0）
```

`nums[mid] == nums[rt]` 落进 `else` 分支，`lt = mid + 1` 把最小值所在的一侧整个丢掉。第 154 题遇到相等时要 `rt--` 逐步缩。

## 五、边界与代价

| 输入 | 结果 |
| --- | --- |
| `{}` | `ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0`（`rt = -1`，循环不进，`return nums[lt]` 崩） |
| `null` | `NullPointerException: Cannot read the array length because "<parameter1>" is null` |
| `{9}` | 9 |

入参只读，同一数组连调两次结果一致。n=10,000,000 的数组上跑 100 万次 `findMin`，实测 **31 ms**（约 31 ns/次，`ceil(log₂ 10⁷) = 24` 轮）。

## 六、一句话记住

```
   和右端比：nums[mid] < nums[rt] 时 rt = mid（mid 可能就是答案），否则 lt = mid + 1；
   36 个旋转 + 20,000 组随机互不相同数据与 min() 0 差异；
   rt = mid 写成 rt = mid - 1：穷举错 11/36，随机错 6,837/20,000（[2,0,1] 给 2，真值 0）；
   重复值会真的给错：1,485/20,000，[2,0,2,2,2] 给 2 —— 第 154 题要另外处理相等；
   空数组 AIOOBE，null NPE，入参只读；
   10^7 数组上 100 万次调用 31 ms。
```
