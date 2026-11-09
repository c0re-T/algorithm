# LeetCode 39 组合总和（回溯：传 `i` 而不是 `i+1` 才允许重复选）

> 对应程序：[LeetCode39_CombinationSum.java](./LeetCode39_CombinationSum.java)
> `main` 传的是 `{2,3,5}`、`target = 8`，实测：
>
> ```
> [[2, 2, 2, 2], [2, 3, 3], [3, 5]]
> ```

---

## 一、能不能重复选，全看递归传的是 `i` 还是 `i + 1`

```java
for (int i = start; i < candidates.length; i++) {
    lres.add(candidates[i]);
    backtrack(res, candidates, target, i, lres, sum + candidates[i]);  // 这里传 i
    lres.removeLast();
}
```

传 `i` 表示"下一个还能选自己"，所以 `2+2+2+2` 能出现；传 `i + 1` 就是每个数只用一次，那是第 40 题。实测把 `i` 改成 `i + 1`：

```
原版 {2,3,5} 8 = [[2,2,2,2],[2,3,3],[3,5]]
每数只用一次   = [[3,5]]
```

20,000 组随机数据里错 **13,249 组**。`start` 递增同时负责另一件事：保证组合内下标不下降，所以 `[2,3,5]` 不会又生成 `[3,2,5]` —— 去重白送。

## 二、`lres.removeLast()` 是 JDK 21 才有的方法

`List.removeLast()` 来自 JDK 21 的 `SequencedCollection`。本机 JDK 21.0.12.1 默认编译通过，但换成目标 17：

```
LeetCode39_CombinationSum.java:29: error: cannot find symbol
            lres.removeLast();
                ^
  symbol:   method removeLast()
  location: variable lres of type List<Integer>
1 error
```

和 `p49` 里那份注释版的 `List.reversed()` 是同一类问题（README 的坑表里已有 `List.reversed()` 那条）。写成 `lres.remove(lres.size() - 1)` 就没有版本要求，而且删除意图更明确。

## 三、`sum > target` 那一句是剪枝，不是可选项

```java
if (sum == target) { res.add(new ArrayList<>(lres)); return; }
else if (sum > target) return;
```

删掉 `else if` 实测 **`StackOverflowError`**：`sum` 冲过 `target` 之后永远等不到相等，而候选又允许无限重复，递归一直往下钻（实测钻到 5,192 层才倒）。这道题的 `sum` 单调不减，所以超额就必须立刻回头；题面候选都是正整数，正是靠这一点。

候选里出现 `0` 或负数同样会炸（题面保证 ≥ 1）：

| 候选 | target | 结果 |
| --- | --- | --- |
| `{0, 1}` | 1 | `StackOverflowError`（`sum` 卡住不动，`i` 也不前进） |
| `{-1, 2}` | 1 | `StackOverflowError`（`sum` 越加越小） |
| `{2, -1}` | 1 | `StackOverflowError` |
| `{0}` | 0 | `[[]]`（`sum == target` 先命中，侥幸） |

## 四、`res.add(new ArrayList<>(lres))` 那个拷贝不能省

把拷贝去掉，`main` 的用例直接变成：

```
原版          = [[2, 2, 2, 2], [2, 3, 3], [3, 5]]
res.add(lres) = [[], [], []]
```

三条还在，内容全是空列表 —— 存进去的是同一个 `lres` 引用，回溯到底时它已经被清空。20,000 组里 **14,611 组**对不上。

## 五、结果顺序跟着输入顺序走

```
{2,3,5} 8 = [[2,2,2,2], [2,3,3], [3,5]]
{5,3,2} 8 = [[5,3], [3,3,2], [2,2,2,2]]
{3,2,5} 8 = [[3,3,2], [3,5], [2,2,2,2]]
```

组内元素也按候选顺序排列。20,000 组里把候选倒序后再比（按集合拍平）差异 **0 组**，即"集合相同、顺序不同"。题面不要求顺序，所以这不是 bug；但如果哪天要字典序输出，得先 `Arrays.sort(candidates)`。

## 六、边界与随机对拍

| 输入 | 结果 |
| --- | --- |
| `{2,3,5}, 0` | `[[]]`（空组合的和是 0） |
| `{2,3,5}, -3` | `[]` |
| `{2}, 1` | `[]` |
| `{}, 5` | `[]` |
| `{7}, 7` | `[[7]]` |
| `{1}, 5` | `[[1,1,1,1,1]]` |
| `null, 5` | `NullPointerException: Cannot read the array length because "<parameter2>" is null` |

**候选里有重复值**（题面保证互不相同）会给出重复解：`{2,3,3}` 找 5 → `[[2,3],[2,3]]`，`{1,1}` 找 2 → 三条一模一样的 `[1,1]`。按位置枚举就会这样。

20,000 组随机（候选 1..9 互不相同、target 0..30）与**完全背包 DP** 参考对拍（另一种完全不同的构造）：**差异 0**。同一实例连调三次给 `3 → 2 → 3` 组，`res` / `lres` 都是局部状态。

## 七、规模与递归深度

| 用例 | 组数 | 耗时 |
| --- | --- | --- |
| `{2,3,5}` target 30 / 60 / 100 | 21 / 71 / 184 | 0 ms |
| `{1,2,5}` target 30 / 60 / 100 | 58 / 205 / 541 | 0 ms |

递归深度 = `target / 最小候选`。用 `{1}` 单候选把深度打到 `target`：

```
target=1000  -> 1 组, 0 ms
target=5000  -> 1 组, 0 ms
target=10000 -> 1 组, 0 ms
target=20000 -> StackOverflowError
target=50000 -> StackOverflowError
```

**1 万层能过、2 万层爆**。题面 `target ≤ 40`、候选 `≥ 2`，深度最多 20 层，离得很远。真正让这份慢下来的是解的数量，不是深度。

## 八、一句话记住

```
   传 i 是"还能选自己"（本题），传 i+1 是"每个只用一次"（第 40 题）：改一下 main 就只剩 [[3,5]]，20,000 组错 13,249；
   start 递增顺带保证组合内下标不降，所以不用额外去重；
   lres.removeLast() 是 JDK 21 API，--release 17 直接 `找不到符号 方法 removeLast()`；
   sum > target 的剪枝删掉就 StackOverflowError（钻 5,192 层）；候选含 0 或负数同样爆栈；
   res.add 不拷贝 → 三条全变成 []，20,000 组错 14,611；
   结果顺序跟着输入顺序走（{5,3,2} 给 [[5,3],[3,3,2],[2,2,2,2]]），按集合比较 0 差异；
   重复候选会给出重复解：{1,1} 找 2 → 三条 [1,1]；
   20,000 组与完全背包 DP 0 差异；深度 = target/最小候选，1 万层能过、2 万层爆。
```