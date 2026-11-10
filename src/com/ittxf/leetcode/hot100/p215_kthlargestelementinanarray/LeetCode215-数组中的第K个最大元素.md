# LeetCode 215 数组中的第 K 个最大元素（大小为 k 的小顶堆）

> 对应程序：[LeetCode215_KthLargestElementInAnArray.java](./LeetCode215_KthLargestElementInAnArray.java)
> `main` 跑 `{3,2,1,5,6,4} k=2` 和 `{3,2,3,1,2,4,5,5,6} k=4`，实测：
>
> ```
> 5
> 4
> ```

---

## 一、小顶堆只留 k 个，堆顶就是第 k 大

```java
Queue<Integer> heap = new PriorityQueue<>();
for (int num : nums) {
    heap.add(num);
    if (heap.size() > k) heap.poll();   // 踢掉当前最小的
}
return heap.peek();
```

堆里始终保持"目前见过的最大的 k 个"，超编就把最小的踢掉。扫完之后堆顶是这 k 个里最小的 —— 正是全体第 k 大。时间 `O(n log k)`，空间 `O(k)`。

## 二、`heap.size() > k` 是这道题的全部逻辑

去掉这个判断，堆会装下整个数组，`peek()` 返回全局最小值：

| 版本 | 5,000 组随机里的差异 |
| --- | --- |
| 原版 | 0 |
| 去掉 `if (heap.size() > k) heap.poll();` | **4,549** |

```
{3,2,1,5,6,4} k=2
原版     -> 5
去掉判断 -> 1（全局最小）
```

代价也很直接：n=1,000,000、k=1000 时，无界堆慢 17 ms，还多占约 **30 MB**（每个 int 都装箱成 `Integer`）。

## 三、对拍

20,000 组随机（长度 1..40、值域 −10..10、k 合法）与"克隆后排序取 `nums[n-k]`"：

```
20,000 组：0 差异
```

## 四、边界

| 输入 | 结果 |
| --- | --- |
| `k = n` | 正确（堆里就是全部，堆顶最小值） |
| `k = 1` | 正确（堆里只有最大值） |
| 全部相同 `{4,4,4,4} k=3` | 4 |
| `k = 0` | `NullPointerException`：每轮都 `poll`，最后 `peek()` 拿到 null 拆箱 |
| `k > n` | **不报错，返回全局最小值**（`{9,8,7} k=5` 给 7），这个"第 5 大"并不存在 |
| `{}` | `NullPointerException`（同上，堆空） |
| `null` | `NullPointerException: Cannot read the array length` |

入参不被修改（注释里的 `Arrays.sort(nums)` 版会把调用方数组就地排序）。

## 五、代价

n=10,000,000、k=1000：堆 **359 ms**，全排序 711 ms（含排序本身；克隆不计）。k 越大堆越慢：k = n/2 时实测 **2,557 ms**，因为每次都要维护 500 万个元素的堆。k 很小时堆的优势最明显。

## 六、一句话记住

```
   小顶堆保持最多 k 个，超编就 poll 掉最小的，最后 peek 就是第 k 大；
   heap.size() > k 这行是全部逻辑：去掉后 4,549/5,000 组错，返回全局最小值，还多占 30 MB；
   20,000 组随机与排序参考 0 差异；
   k=0 / 空数组 / null 都是 NPE，k>n 静默返回全局最小值；
   n=1e7 k=1000：堆 359 ms vs 全排序 711 ms；k=n/2 时堆要 2,557 ms；
   入参不变。
```
