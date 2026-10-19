# LeetCode 128 最长连续序列（"只从起点往后扩"这一句把 O(n²) 压成 O(n)）

> 对应程序：[LeetCode128_LongestConsecutiveSequence.java](./LeetCode128_LongestConsecutiveSequence.java)
> `main` 用 `{0,3,7,2,5,8,4,6,0,1}`，实测打印两行：
>
> ```
> [0, 1, 2, 3, 4, 5, 6, 7, 8]
> 9
> ```
>
> 第一行是方法里那句 `System.out.println(numSet)` 打的 —— 去重后的 `HashSet`。

---

## 一、核心那一句

```java
for (int num : numSet) {
    if (!numSet.contains(num - 1)) {        // 只有"序列起点"才往里展开
        int currentNum = num, currentStreak = 1;
        while (numSet.contains(currentNum + 1)) { currentNum++; currentStreak++; }
        longestStreak = Math.max(longestStreak, currentStreak);
    }
}
```

去重之后，一个长度为 `L` 的连续段里只有最小的那个数能满足"`num - 1` 不在集合里"，
所以**每段只展开一次**：

```
   不过滤起点：每个数都要往后爬一遍 → 1+2+...+L = O(L²)
   加了起点判断：每段只爬一遍 → O(L)；所有段加起来 ≤ n 个数 → 总体 O(n)
```

"每个数字最多被访问两次"（一次外层遍历、一次内层 `while` 路过）就是这句判断的全部价值。
没有它，`{1,2,3,...,10⁵}` 这种一整个长段的输入会退化成 `5×10⁹` 次查找。

## 二、和"排序扫描"对拍

固定种子 20,000 组随机（长度 0..19、值域 -10..19，故意包含空数组、大量重复、跨零的连续段）：

```
   与排序法的不同 —— 0 次
```

| 输入 | 返回 | |
| --- | --- | --- |
| `{}` | 0 | 空数组不需要特判：`numSet` 为空，外层循环一次不进 |
| `{5,5,5,5}` | 1 | 去重后只剩一个 5，`5-1=4` 不在集合里 → 是起点，往后 `6` 不在 → 长度 1 |
| `{0,3,7,2,5,8,4,6,0,1}` | **9** | 0..8 连成一串（输入里缺 9，且 0 重复） |

## 三、`num - 1` 和 `currentNum + 1` 都会溢出

这两个相邻判断是纯 `int` 加减。取极值构造一对"看着相邻、其实差 2³²−1"的数：

```
   nums = {Integer.MIN_VALUE, Integer.MAX_VALUE}
   真实最长连续序列 = 1        （两个数根本不相邻）
   这个方法返回     = 2
```

原因正好是第一节那句优化自己的前提被绕开了：

```
   对 MIN_VALUE 判 contains(MIN_VALUE - 1) → MIN_VALUE - 1 溢出成 MAX_VALUE，而 MAX_VALUE 在集合里
        → 判定"我不是起点"，跳过
   对 MAX_VALUE 判 contains(MAX_VALUE + 1) → 溢出成 MIN_VALUE，而 MIN_VALUE 在集合里
        → 于是 currentNum 走到 MIN_VALUE，长度数成 2
```

题面约束 `nums[i] ∈ [-10⁹, 10⁹]`，`±10⁹ ± 1` 远不到 `int` 边界，**所以交上去不会触发**。
`{MAX_VALUE - 1, MAX_VALUE}` 实测返回 2（正确，这对是真相邻），说明溢出只在跨过 `int` 两端时才咬人。
要堵死它，比较时先判边界（`num != Integer.MIN_VALUE && set.contains(num - 1)`）或干脆用 `long`。

## 四、`System.out.println(numSet)` 打出来的顺序

这行打印容易让人误以为"`HashSet` 里小数在前"。同一批数字换个取值就露馅（实测）：

```
   HashSet {100, 3, 7, 250, 1, 99} 的迭代顺序 -> [1, 3, 99, 100, 7, 250]
   TreeSet  同一批                -> [1, 3, 7, 99, 100, 250]
   HashSet 0..16                -> [0, 1, 2, ..., 16]     ← 这里恰好是升序
```

`Integer.hashCode()` 就是它自己，落桶是 `hash & (容量-1)`，所以**小整数会"看起来有序"，一旦值大到跨桶就打乱**。
也就是说他 `main` 里那行打印出来的 `[0, 1, ..., 8]` 是巧合而不是保证。
算法本身不依赖遍历顺序（结果只取最大长度），但**读输出的人会被它误导** —— 这跟 49、贪心电台那两篇里
"HashSet/HashMap 顺序当结果"的问题同源，只是这里只影响一打印行。

那行打印交上去会多输出一行（题面只要一个返回值），和 `p49_3sum` 里打印排序数组是同一类"本地观察用的语句"。

## 五、注释里那份排序版

```java
if (nums.length == 0) return 0;
Arrays.sort(nums);                        // ← 注意：排的是调用方传进来的数组
System.out.println(Arrays.toString(nums));
int count = 1, maxCount = 1;
for (int i = 0; i < nums.length - 1; i++) {
    if (nums[i + 1] == nums[i]) continue;
    if (nums[i + 1] - nums[i] == 1) count++;
    else { if (nums[i + 1] == nums[i]) continue; maxCount = Math.max(maxCount, count); count = 1; }
}
maxCount = Math.max(maxCount, count);
```

三个点：

1. 复杂度是 `O(n log n)` + `O(1)` 额外空间（除了排序本身的栈），而主方法是 `O(n)` 时间 + `O(n)` 空间 —— 一个用时间换空间、一个反过来。
   同一批随机数组上实测：

```
   n=20,000  HashSet 版 2 ms vs 排序扫描 5 ms
   n=40,000           3 ms            8 ms
   n=80,000           5 ms           11 ms
   n=160,000         12 ms           11 ms     ← 这一档两者打平（JIT/GC 抖动，别当成"排序更快"）
```

   前几档是 2 倍上下的差距，到 16 万就缠在一起了 —— 说明这题**真正值得记的是那句起点判断，不是跑分**。
2. `else` 分支里那句 `if (nums[i + 1] == nums[i]) continue;` 是**死代码**：外层循环开头已经 `continue` 过一次相同的判断，走到 `else` 时 `nums[i+1] != nums[i]` 必然成立。
3. 它 `Arrays.sort(nums)` **直接把调用方的数组排序了**（主方法只读不改），所以放开用的话，原数组顺序就没了 —— 和 `p49_3sum`、Floyd 那两处是同一个"接管输入"的毛病。
4. 空数组要 `if (nums.length == 0) return 0;` 特判，否则 `maxCount = 1` 会给出错误答案 1；主方法不用特判（第二节的实测表里那行就是这个道理）。

## 六、一句话记住

```
   if (!set.contains(num - 1)) 一句把"每个数都往后爬"变成"每个连续段只爬一次"，O(L²) → O(L)；
   20,000 组随机与排序扫描 0 差异，空数组不需要特判（集合为空自然不进循环），全相同给 1；
   num-1 / currentNum+1 是裸 int 加减：{MIN_VALUE, MAX_VALUE} 实测返回 2（真值 1），题面 ±10⁹ 挡得住；
   方法里那行 println(numSet) 打的顺序不是升序（{100,3,7,250,1,99} → [1,3,99,100,7,250]），只是小整数恰好看起来有序；
   注释那份排序版 O(n log n)、要特判空数组、else 里有一句死代码，还会把调用方的数组就地排序。
```
