# LeetCode 146 LRU 缓存（`LinkedHashMap` 的第三个构造参数就是 LRU）

> 对应程序：[LeetCode146_LRUCache.java](./LeetCode146_LRUCache.java)
> **`main` 是空的**，直接运行输出 0 字节；下面的行为都是用独立测试台驱动这个类实测的。
> 关键是 `getOrDefault` 到底刷不刷新访问顺序 —— 实测会刷新：
>
> ```
> get(1) 后迭代顺序 = [2, 3, 1]
> getOrDefault(2) 后顺序 = [3, 1, 2]
> ```

---

## 一、三行核心

```java
class LRUCache extends LinkedHashMap<Integer, Integer> {
    private int capacity;
    public LRUCache(int capacity) {
        super(capacity, 0.75F, true);   // true = 按访问顺序排序
        this.capacity = capacity;
    }
    public int get(int key) { return super.getOrDefault(key, -1); }
    public void put(int key, int value) { super.put(key, value); }
    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
        return size() > capacity;
    }
}
```

`LinkedHashMap` 在哈希表之外还维护一条双向链表：

- `accessOrder = false`（默认）：链表按**插入顺序**；
- `accessOrder = true`：每次 `get` / `put` 已有 key 都把该节点挪到链尾，链头就是"最久未使用"。

`removeEldestEntry` 在**每次插入之后**被调用，返回 true 就删掉链头。所以容量控制不需要自己写任何 if。

## 二、`getOrDefault` 会不会刷新顺序？

很多"手写 LRU"的教程里，`getOrDefault` 是直接查哈希表、不碰链表的；但 `LinkedHashMap` **重写了 `getOrDefault`**（`javap` 可见），在 `accessOrder = true` 时同样执行 `afterNodeAccess`。实测：

| 操作序列（容量 3） | 迭代顺序（链头 → 链尾） |
| --- | --- |
| put 1, 2, 3 | `[1, 2, 3]` |
| get(1) | `[2, 3, 1]` |
| getOrDefault(2, -1) | `[3, 1, 2]` |
| get(2) | `[3, 1, 2]`（已经在链尾，不变） |

`keySet()` 的迭代顺序就是这条链，因此可以直接拿它当"LRU → MRU"的证据。**如果这里用的是自己写的 `HashMap` + 双向链表，`getOrDefault` 必须手动调用 `moveToTail`** —— 这份实现之所以三行就够，是因为 JDK 已经把这层写好了。

## 三、标准用例逐条验证

容量 2，`put(1,1)`、`put(2,2)`、`get(1)`、`put(3,3)`：

```
put(3) 之后 get(2) = -1     ← 2 是最近最少使用的，被淘汰
put(3) 之后 get(1) = 1      ← 1 刚被访问过，留下
put(3) 之后 get(3) = 3
```

对照"用 `super.get` 的版本"结果相同，说明这份 `getOrDefault` 没有引入顺序差异。

覆盖已有 key 也算一次访问：

```
put(1,1), put(2,2), put(1,100), put(3,3)
-> get(2) = -1, get(1) = 100
```

`put(1,100)` 既更新了值，也把 1 挪到链尾，所以被淘汰的是 2。

边界：

```
capacity = 0: put(7,7) 之后 get(7) = -1, size = 0    ← 插入后立刻淘汰
未命中 get(99) = -1
连续 put 10 万个 key（容量 1000）: size 仍为 1000, 最早 key0 已被淘汰
```

## 四、代价

容量 1000、连续 100 万次 `get`（命中/未命中各半）：**16.6 ms**，即每次约 16.6 ns。`get` / `put` 都是哈希表 + 链表摘挂，期望 `O(1)`；空间是 `O(capacity)`。

两个使用前提（代码没写、但工程上要知道）：

- **不是线程安全的**。`LinkedHashMap` 没有任何同步，多线程同时 `put` 会破坏链表；要并发就用 `Collections.synchronizedMap` 或自己加锁。
- 顺序只在**同一把锁/同一个线程**内可信；`accessOrder` 的链表是普通双向链表，没有并发保护。

## 五、`main` 为空

这个文件只有类定义，没有演示代码，`java ...LeetCode146_LRUCache` 的输出是 0 字节。功能是否正确只能像上面这样另写驱动验证 —— 这也是它唯一"缺"的东西。

## 六、一句话记住

```
   LinkedHashMap(capacity, 0.75f, true) + removeEldestEntry(size() > capacity)；
   accessOrder=true 时 get / put 已有 key 都把节点挪到链尾，链头就是 LRU；
   getOrDefault 也被 LinkedHashMap 重写，实测会刷新顺序（[2,3,1] → [3,1,2]）；
   容量 0 立即淘汰；100 万次 get 16.6 ms；非线程安全；
   main 是空的，直接运行没有任何输出。
```
