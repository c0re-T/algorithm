# LeetCode 146 LRU 缓存（HashMap + 哨兵双向链表，手写版）

> 对应程序：[LeetCode146_LRUCache.java](./LeetCode146_LRUCache.java)
> **这份已经从 `LinkedHashMap` 三行版重写成手写双向链表版**，`main` 也不再是空的。实测：
>
> ```
> 找1:1
> 找2:-1
> 找1:-1
> 找3:3
> 找4:4
> ```
>
> 这正是 LeetCode 官方示例序列 `put(1,1) put(2,2) get(1) put(3,3) get(2) put(4,4) get(1) get(3) get(4)` 的输出。

---

## 一、两个结构各管一件事

```java
private Map<Integer, Node> map;   // key → 链表节点，负责 O(1) 定位
private int capacity;
private Node head, tail;          // 虚拟头尾哨兵，负责 O(1) 摘挂
```

- **哈希表**解决"给我 key，我要立刻拿到节点"，但它不知道谁最久没用；
- **双向链表**解决"谁最久没用"：约定**靠近 head 的是最新鲜的，靠近 tail 的是最该淘汰的**，但它找节点要 O(n)。

两个拼起来才是 O(1) 的 LRU。`Node` 里同时存 `key` 和 `value`：

```java
class Node { int key, value; Node prev, next; }
```

`key` 字段是必需的 —— 淘汰时拿到的是 `tail.prev` 这个节点，必须知道它的 key 才能去 `map.remove(...)`。只存 value 的话哈希表里会留下一个指向已删节点的悬空条目。

## 二、哨兵省掉了所有 null 判断

```java
head = new Node(-1, -1);
tail = new Node(-1, -1);
head.next = tail;
tail.prev = head;
```

有了这两个永不删除的哨兵，链表**永远非空**，摘挂节点时不需要判断"是不是头/尾"：

```java
private void removeNode(Node node) {
    node.prev.next = node.next;
    node.next.prev = node.prev;
}
private void addNode(Node node) {      // 插到 head 后面 = 最新鲜
    node.next = head.next;
    node.prev = head;
    head.next.prev = node;
    head.next = node;
}
private void moveToHead(Node node) { removeNode(node); addNode(node); }
```

`addNode` 四句的顺序不能乱：必须先接好新节点的两条指针（前两句），再改邻居的两条（后两句）。反过来先写 `head.next = node`，原来的第一个节点就找不回来了。

`get` 命中之后调 `moveToHead`，`put` 覆盖已有 key 之后也调 —— **读和写都算一次"使用"**。

## 三、淘汰的判定位置

```java
public void put(int key, int value) {
    if (map.containsKey(key)) {          // 已有：更新值 + 刷新新鲜度，不淘汰
        Node node = map.get(key);
        node.value = value;
        moveToHead(node);
    } else {
        if (map.size() == capacity) {    // 新增且满了：先淘汰再插入
            Node tailNode = tail.prev;
            map.remove(tailNode.key);
            removeNode(tailNode);
        }
        Node newNode = new Node(key, value);
        map.put(key, newNode);
        addNode(newNode);
    }
}
```

`map.size() == capacity` 用 `==` 而不是 `>=`：因为每次只在"新增"分支里检查，而新增前 size 最多等于 capacity，不会超。判断放在 `else` 里面是关键 —— **覆盖已有 key 不该挤掉别人**。实测容量 2：

```
put(1,1) put(2,2) put(1,100) put(3,30)
-> get(1)=100  get(2)=-1  get(3)=30
```

`put(1,100)` 只是更新，没有触发淘汰；淘汰发生在 `put(3,30)`，踢掉的是最久未用的 2。

`get` 也刷新新鲜度，实测容量 2：

```
put(1,1) put(2,2) get(1) put(3,3)
-> get(2)=-1  get(1)=1  get(3)=3     ← 淘汰的是 2 而不是 1
```

如果 `get` 里漏掉 `moveToHead`，这里淘汰的就会是 1，官方示例序列立刻对不上。

## 四、随机对拍

3,000 条随机操作序列（容量 1..5、键 0..7，合计 **180,000 次操作**），与两个独立参考对比：

```
vs LinkedHashMap(capacity, 0.75f, true) + removeEldestEntry 版：差异 = 0
vs 手工 LRU 模型（用 ArrayList 维护顺序、每次线性查找）：差异 = 0
```

两个参考一个是 JDK 现成实现、一个是完全不看链表的笨办法，都对上了。

## 五、边界：capacity=0 会 NPE

| 输入 | 结果 |
| --- | --- |
| 容量 1：`put(1,1) get(1)` | 1；再 `put(2,2)` → `get(1)=-1 get(2)=2` |
| 未命中 `get(99)` | −1 |
| 存 `key=-1` 再塞满（容量 2） | `get(-1)=-1 get(1)=1 get(2)=2` |
| **容量 0 执行 put** | `NullPointerException: Cannot assign field "next" because "<parameter1>.prev" is null` |

`key=-1` 那条是个陷阱题：哨兵 head/tail 的 key 也是 −1，看起来会撞。实测不会 —— **哨兵从来不在 map 里**，`map.remove(tailNode.key)` 只在淘汰真实节点时执行，而哨兵永不被淘汰。用户存 −1 只是普通的键。

容量 0 那条是真 bug：`map.size() == capacity` 即 `0 == 0` 成立，于是去淘汰 `tail.prev` —— 空表时那就是哨兵 **head**，`removeNode(head)` 读 `head.prev` 得到 null，炸。对照旧的 `LinkedHashMap` 版：容量 0 时 `put` 之后 `get(1) = -1`，不抛异常。题面保证 `1 <= capacity <= 3000`，所以线上不触发，但重写之后这个边界从"正常"退化成了"崩溃"。

## 六、和 LinkedHashMap 版的代价对比

容量 1000、键 0..2999，**200 万次混合 get/put**：

```
手写双向链表    64 ms
LinkedHashMap   61 ms
```

基本打平（手写版慢约 5%，在噪声量级）。这在意料之中：`LinkedHashMap` 内部就是"哈希表 + 双向链表"，手写版做的是同一件事。旧版三行就能写完：

```java
class LRUCache extends LinkedHashMap<Integer, Integer> {
    public LRUCache(int capacity) { super(capacity, 0.75F, true); }
    public int get(int key) { return super.getOrDefault(key, -1); }
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> e) { return size() > capacity; }
}
```

它仍然是对的（180,000 次操作对拍就是拿它当参考），而且容量 0 的边界比手写版更稳。**换成手写的理由不是性能，是把"谁最久没用"这条链显式握在手里**：可以改成 LFU、可以加 TTL、可以批量淘汰，而 `removeEldestEntry` 只给你一个"要不要删最老的"的布尔开关。

`javap -p` 看内部结构（JDK 21.0.12）：

```
LRUCache 字段：map(Map) capacity(int) head(Node) tail(Node)
Node 字段：key(int) value(int) prev(Node) next(Node)
Node 构造器：Node(LRUCache, int, int)
```

`Node` 是**非静态内部类**，构造器因此多带一个 `LRUCache` 参数，但字段列表里**没有 `this$0`**。写成 `static class Node` 能把"我不需要外部类"这件事讲清楚，也省掉那个参数。

两个使用前提（代码没写、工程上要知道）：

- **不是线程安全的**。HashMap 和裸链表都没有同步，多线程 put 会同时破坏表和链。
- `new HashMap<>(capacity)` 把容量当初始桶数，负载因子仍是默认 0.75，所以塞到约 `0.75 × capacity` 就会扩容一次。传 `(int)(capacity / 0.75f) + 1` 才能完全避免扩容。

## 七、一句话记住

```
   HashMap 定位 O(1) + 双向链表定新鲜度 O(1)，Node 必须存 key（淘汰时要 map.remove）；
   head/tail 哨兵让摘挂不用判 null；addNode 四句先接新节点再改邻居；
   get 和 put 覆盖都要 moveToHead；淘汰判断在 else 分支里（覆盖不挤人）；
   官方序列实测 1 / -1 / -1 / 3 / 4；180,000 次随机操作与两个参考 0 差异；
   key=-1 与哨兵同值但安全（哨兵不在 map 里）；
   capacity=0 会 NPE（淘汰取到哨兵 head，head.prev 是 null），旧 LinkedHashMap 版不会；
   200 万次混合操作 64 ms vs LinkedHashMap 61 ms，打平；非线程安全。
```
