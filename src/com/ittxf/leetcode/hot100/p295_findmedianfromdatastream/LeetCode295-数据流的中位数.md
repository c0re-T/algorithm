# LeetCode 295 数据流的中位数（对顶堆）

> 对应程序：[LeetCode295_FindMedianFromDataStream.java](./LeetCode295_FindMedianFromDataStream.java)
> `main` 依次加入 1、2、3，实测：
>
> ```
> 1.5
> 2.0
> ```

---

## 一、左大顶堆 + 右小顶堆，左边最多多一个

```java
public void addNum(int num) {
    if (left.size() == right.size()) {
        right.add(num);
        left.add(right.poll());     // 从右边借最小的给左边
    } else {
        left.add(num);
        right.add(left.poll());     // 从左边借最大的给右边
    }
}
public double findMedian() {
    if (left.size() == right.size()) return (left.peek() + right.peek()) / 2.0;
    else return left.peek();
}
```

`left` 存较小的一半（大顶堆，`(a,b) -> b - a`），`right` 存较大的一半（小顶堆）。每次插入都先塞再"借一个过去"，保证 `left.size()` 比 `right.size()` 多 0 或 1。中位数只跟两个堆顶有关，`findMedian` 是 O(1)。

## 二、对拍

2,000 条随机操作序列、共 **80,048 次**查询，与"每次排序后取中位"参考：

```
80,048 次：0 差异
```

降序插入 200,000 个数（最坏形状之一）也正确：中位数 100000.5。

## 三、比较器 `b - a` 会溢出

`(a, b) -> b - a` 在极值上会算错顺序：

```
加入 MIN_VALUE, MAX_VALUE, 0, MIN_VALUE, MAX_VALUE
原版 findMedian -> -2.147483648E9     真值 0.0
```

5 个数时中位数取 `left.peek()`，结果给出 `MIN_VALUE`，说明**大顶堆把最小值当成了最大值** —— `MAX_VALUE - MIN_VALUE` 溢出成 −1，比较器给出相反的顺序。比较器应写成 `Integer.compare(b, a)`。LeetCode 约束 ±10⁵，触发不到。

## 四、求平均那一步也会溢出

```java
return (left.peek() + right.peek()) / 2.0;   // 先 int 加法
```

```
加入 2_000_000_000, 2_000_000_000
原版 findMedian -> -1.47483648E8     真值 2.0E9
```

题面值域内（±10⁵）安全；想稳可以写 `left.peek() / 2.0 + right.peek() / 2.0`。

## 五、边界与代价

| 操作 | 结果 |
| --- | --- |
| 空数据流 `findMedian()` | `NullPointerException`：两个堆都空，`peek()` 返回 null 拆箱 |
| 单个 7 | 7.0 |
| 极端值混合（见上） | 比较器溢出，答案错 |

1,000,000 次 `addNum` 实测 **198 ms**，`findMedian` 约 6 µs。每次插入固定两次堆操作，`O(log n)`。

## 六、一句话记住

```
   左大顶堆存较小一半、右小顶堆存较大一半，size 相等就借一个过去，左边最多多一个；
   80,048 次随机查询与排序参考 0 差异；
   比较器 (a,b) -> b - a 在 MIN/MAX 上溢出：5 个极值的中位数给 MIN（真值 0），应写 Integer.compare；
   (left.peek() + right.peek()) / 2.0 先做 int 加法：两个 2e9 给 -1.47483648E8；
   空数据流 findMedian 是 NPE；1e6 次 addNum 198 ms。
```
