# LeetCode 347 前 K 个高频元素（哈希 + 小顶堆）

> 对应程序：[LeetCode347_TopKFrequentElements.java](./LeetCode347_TopKFrequentElements.java)
> `main` 跑三组示例，实测：
>
> ```
> [2, 1]
> [1]
> [1, 2]
> ```

---

## 一、先数频率，再用"按频率比较"的小顶堆留下 k 个

```java
Map<Integer, Integer> hashMap = new HashMap<>();
for (int num : nums) hashMap.put(num, hashMap.getOrDefault(num, 0) + 1);

Queue<Integer> heap = new PriorityQueue<>(Comparator.comparingInt(hashMap::get));
for (int num : hashMap.keySet()) {
    heap.add(num);
    if (heap.size() > k) heap.poll();
}
return heap.stream().mapToInt(Integer::intValue).toArray();
```

堆里存的是**元素值**，比较器去 map 里取频率，所以堆顶是"当前 k 个里频率最低的"。超过 k 就把最低频的踢掉。时间 `O(n + d log k)`（d 是不同元素数）。

## 二、答案集合什么时候唯一？对拍分开看

20,000 组随机数据（值域 0..9，大量重复），用"按频率降序、频率相同按值升序"的参考比较：

| 情形 | 组数 | 差异 |
| --- | --- | --- |
| 第 k 与第 k+1 名频率**不同**（答案唯一） | 9,830 | **0** |
| 第 k 与第 k+1 名**频率相同**（任选其一都合法） | 10,170 | 10,170 组集合不同，但都是合法 top-k |
| 返回的集合不是合法 top-k | — | **0** |

也就是说，答案唯一时它从没错过；频率并列时它给出的也是合法的 k 个（只是不保证是你心里那一组）。例如 `{1,1,2,2,3,3,4,5} k=2` 实测给 `[3,2]`，而 1、2、3 的频率都是 2，取谁都对。

## 三、输出顺序不是频率降序

堆的 `stream()` 按数组顺序遍历，**不保证按频率排序**：

```
20,000 组里 13,688 组输出不是频率降序
```

LeetCode 只要求"任意顺序"，所以判题能过；但如果拿去做展示，需要自己再排一次（或改成 `List.sort`）。

## 四、边界

| 输入 | 结果 |
| --- | --- |
| `k = 0` | `[]`（堆保持空） |
| `k > 不同元素个数` | **不报错，返回不足 k 个**（`{1,2,3} k=5` → 长度 3） |
| `{}` + k=1 | `[]` |
| `null` | `NullPointerException: Cannot read the array length` |
| 负数元素 | 正常（map 的 key 可以是负数） |

题面保证 `1 ≤ k ≤ 不同元素个数`，所以"返回不足 k 个"只在题面之外出现。入参不被修改。

## 五、代价

n=1,000,000、k=10 实测 **51 ms**（其中大部分是哈希统计）。堆只保留 10 个元素，堆操作几乎可以忽略。

## 六、一句话记住

```
   哈希数频率，用 Comparator.comparingInt(map::get) 的小顶堆留 k 个，超编 poll 最低频；
   答案唯一（第 k 与 k+1 频率不同）的 9,830 组 0 错；并列的 10,170 组给出的也是合法 top-k；
   输出顺序不是频率降序：13,688/20,000 组如此，判题接受任意顺序；
   k=0 给 []，k>不同元素数静默返回不足 k 个，空数组 []，null NPE；
   n=1e6 k=10 只要 51 ms，入参不变。
```
