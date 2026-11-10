# LeetCode 207 课程表（Kahn 拓扑排序：能全出队就没有环）

> 对应程序：[LeetCode207_CourseSchedule.java](./LeetCode207_CourseSchedule.java)
> `main` 传的是 `2` 和 `{{1,0},{0,1}}`（两门课互相依赖），实测：
>
> ```
> false
> ```

---

## 一、入度 = 0 才能开工，出队计数就是"修完几门"

```java
for (int[] pre : prerequisites) {
    int course = pre[0];    // 目标课程
    int requires = pre[1];  // 先修课程
    adj.get(requires).add(course);   // 先修课 → 目标课
    indegree[course]++;
}
...
while (!queue.isEmpty()) { int current = queue.poll(); count++; for (int next : adj.get(current)) if (--indegree[next] == 0) queue.offer(next); }
return count == numCourses;
```

`prerequisites[i] = {a, b}` 的含义是"修 a 之前必须先修 b"，所以边要从 `b` 指向 `a`。这份的命名（`course` / `requires`）把这一点写清楚了，不容易看反。

**方向接反在本题不影响答案** —— 把每条边都反过来，有环还是环、无环还是无环，环的存在性与边的方向无关。20,000 组随机数据实测方向接反的版本**差异 0 组**。但第 210 题要输出一个可行的修课顺序，那时方向一反就全错。

## 二、`count == numCourses` 是唯一的环检测

环上的节点入度永远减不到 0，也就永远进不了队列，`count` 停在环外那部分节点数上。实测：

| 输入 | 结果 | 说明 |
| --- | --- | --- |
| `(2, {{1,0},{0,1}})` | `false` | 二节点环（`main`） |
| `(3, {{0,0}})` | `false` | 自环 |
| `(2, {{0,1},{1,0}})` | `false` | 反向二节点环 |
| `(2, {{1,0}})` | `true` | 一条边 |
| `(3, {{1,0},{2,0}})` | `true` | 两根都指向 0 |
| `(2, {{1,0},{1,0}})` | `true` | 重复边：入度加 2 减 2，照样归零 |
| `(2, {})` / `(1, {})` / `(0, {})` | `true` | 无边，或根本 0 门课 |

把 `count == numCourses` 改成恒真（`count >= 0`）的话 `main` 从 `false` 变 `true` —— 这道题的全部判断就挂在这一行上。

## 三、入参没有被改

```java
int[][] pre = {{1, 0}, {2, 1}, {3, 1}};
// 调用前后一致 = true
```

只读 `pre[0]` / `pre[1]`，不往回写，和 `p200` / `p994` 那种"就地改棋盘"完全不同。这份可以放心复用同一个 `prerequisites` 数组。

## 四、边界与异常

| 输入 | 结果 |
| --- | --- |
| `(0, {})` | `true` |
| `(0, null)` / `(3, null)` | `NullPointerException: Cannot read the array length because "<local5>" is null` |
| `(-1, {})` | `NegativeArraySizeException: -1` |
| `(-5, {{0,0}})` | `NegativeArraySizeException: -5`（`new int[numCourses]` 先炸） |
| `(1, {{1,0}})` | `ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1` |
| `(3, {{-1,0}})` | `ArrayIndexOutOfBoundsException: Index -1 out of bounds for length 3` |
| `(3, {{1}})` | `ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1`（读 `pre[1]`） |
| `(3, {null})` | `NullPointerException: Cannot load from int array because "<local8>" is null` |

没有一处是静默的：课号越界、负数课程数、残缺的边数组都会当场抛异常。题面保证 `0 ≤ numCourses`、`0 ≤ a, b < numCourses`，所以线上不触发。

## 五、20,000 组随机对拍

`numCourses` 0..8、边数 0..12（允许自环和重复边），参考实现用**三色 DFS**（白 / 灰 / 黑，踩到灰色就是环）：

```
原版 vs 三色 DFS: 差异 = 0
其中可完成（true）的 = 6,549
判定方向接反: 差异 = 0
```

Kahn 的"数出队个数"和 DFS 的"踩回灰色"是两种完全不同的环检测，0 差异说明队列与入度的维护没错。

## 六、规模：BFS 不吃栈，DFS 会

| 形状 | 他的 Kahn | 三色 DFS 参考 |
| --- | --- | --- |
| 链 n=1,000 | `true`，0 ms | `true`，0 ms |
| 链 n=10,000 | `true`，1 ms | `true`，0 ms |
| 链 n=20,000 | — | **`StackOverflowError`** |
| 链 n=100,000（99,999 边） | `true`，7 ms | — |
| 链 n=500,000（499,999 边） | `true`，35 ms | — |
| 随机 n=10,000（50,000 边） | `false`，1 ms | — |
| 随机 n=100,000（500,000 边） | `false`，13 ms | — |

链状依赖是 DFS 的最坏形状（递归深度 = 节点数），2 万层就倒；Kahn 用队列，50 万节点 35 ms 稳稳跑完。**选 Kahn 不只是因为它能判环，还因为它不吃栈。**

复杂度是 `O(V + E)`：链 50 万节点约 35 ms，随机 10 万节点 50 万条边 13 ms。

## 七、一句话记住

```
   {a,b} 的含义是"修 a 先修 b"，边从 b 指向 a；方向接反在本题不影响答案（20,000 组 0 差异），但第 210 题要输出顺序就全错；
   环上的节点入度永远减不到 0 → count 停住 → count == numCourses 是唯一判断；
   把这一行改成恒真，main 就从 false 变 true；
   自环 false、重复边 true（入度加 2 减 2）、0 门课 true；
   入参只读不写，同一份 prerequisites 可以复用；
   负数课程数 NegativeArraySizeException、课号越界 AIOOBE、残缺边数组 AIOOBE —— 没有静默错误；
   20,000 组与三色 DFS 0 差异（6,549 组 true）；
   链状依赖：DFS 2 万层爆栈，Kahn 50 万节点 35 ms —— O(V+E) 且不吃栈。
```