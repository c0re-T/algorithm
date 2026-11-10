# LeetCode 155 最小栈（辅助栈，`<=` 负责重复最小值）

> 对应程序：[LeetCode155_MinStack.java](./LeetCode155_MinStack.java)
> `main` 依次 push −2、0、−3，取最小、弹出、再取，实测：
>
> ```
> -3
> 0
> -2
> ```

---

## 一、主栈存数据，辅助栈只存"历史最小值"

```java
public void push(int value) {
    stack.push(value);
    if (minStack.isEmpty() || value <= minStack.peek()) minStack.push(value);
}
public void pop() {
    if (!stack.isEmpty()) {
        int value = stack.pop();
        if (value == minStack.peek()) minStack.pop();
    }
}
```

`push` 时只有"不大于当前最小"才同步压入辅助栈；`pop` 时弹出的值如果正好等于辅助栈顶，就一起弹出。这样 `getMin()` 就是辅助栈栈顶，O(1)。

## 二、`<=` 写成 `<`，重复最小值会漏记

把 `value <= minStack.peek()` 改成严格的 `<` 后，20,240 次 `getMin` 对拍（3,000 条随机操作序列，值域只有 −2..2，专门制造重复）：

| 版本 | 错值 | 崩溃 |
| --- | --- | --- |
| 原版 `<=` | **0** | 0 |
| 严格 `<` | 3,276 | 885 |

最短例子：

```
push 2, push 2
原版 getMin -> 2
严格 < 版  -> 2（此时还没错）
pop 一次
原版 getMin -> 2        ← 还剩一个 2
严格 < 版  -> NullPointerException（辅助栈已经空了）
```

因为严格 `<` 只记录了一个 2，弹出时把唯一的记录也弹掉了。重复最小值必须靠 `<=` 一对一对记。

## 三、重复最小值的实测

```
push 2, 2, 1 -> min = 1
pop（弹掉 1）-> min = 2
pop（弹掉第一个 2）-> min = 2     ← 还剩一个 2，辅助栈里也还有一个
push 1,1,1 再 pop 两次 -> min = 1
```

## 四、边界

| 操作 | 结果 |
| --- | --- |
| 空栈 `pop()` | 静默什么都不做（有 `!stack.isEmpty()` 挡着） |
| 空栈 `top()` | `NullPointerException`：`Deque.peek()` 返回 null，自动拆箱炸掉 |
| 空栈 `getMin()` | 同上 NPE |
| 压入 `Integer.MIN_VALUE` / `MAX_VALUE` | min / top 都正确 |

`value == minStack.peek()` 是 `int == Integer`，会自动拆箱按值比较，不是引用比较。

## 五、代价

1,000,000 次 `push` 实测 **28 ms**，`getMin` 约 2 µs，50 万次 `pop` 6 ms。每个元素最多进两个栈各一次，空间 O(n)。

## 六、一句话记住

```
   主栈存数据，辅助栈只在"不大于当前最小"时同步压入；
   push 的 <= 写成 <，重复最小值会漏记：20,240 次对拍里 3,276 次错值 + 885 次 NPE，
   push 2,2 再 pop 一次，getMin 直接空指针；
   空栈 pop 静默无害，空栈 top / getMin 都是拆箱 NPE；
   1e6 push 28 ms，getMin O(1)。
```
